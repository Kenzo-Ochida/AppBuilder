package br.mackenzie.mackleaps.asset.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mackenzie.mackleaps.asset.entity.Asset;
import br.mackenzie.mackleaps.asset.persistence.db.ClientConnection;
import br.mackenzie.mackleaps.utils.Util;

public class AssetDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetDAO.class);

	public static final String STATUS_DELETED = "deleted";
	private static final String STATUS_CREATED = "created";

	private br.mackenzie.mackleaps.asset.persistence.db.Connection conn;
	
	private static final String FIELD_ID = "ID";
	private static final String FIELD_URL = "URL";
	private static final String FIELD_VERSION = "VERSION";
	private static final String FIELD_NAME = "NAME";
	private static final String FIELD_STATUS = "STATUS";
	private static final String FIELD_ASSET_TYPE = "ASSET_TYPE_NAME";
	private static final String FIELD_ASSET_TYPE_DOMAIN = "ASSET_TYPE_DOMAIN";
	private static final String FIELD_CREATE_USER = "CREATE_USER";
	private static final String FIELD_DESCRIPTION = "DESCRIPTION";
	private static final String FIELD_DATE_CREATED = "DATE_CREATED";
	private static final String FIELD_LAST_UPDATED = "LAST_UPDATED";
	private static final String FIELD_UPDATE_USER = "UPDATE_USER";
	private static final String FIELD_ATTRIBUTES = "ATTRIBUTES";
	private static final String FIELD_ICON = "ICON";
	private static final String FIELD_ICON64 = "ICON64";
	private static final String FIELD_DOMAIN = "DOMAIN";
	private static final String SELECT_ASSET = "SELECT ID, ASSET_TYPE_NAME,ASSET_TYPE_DOMAIN,ATTRIBUTES,CREATE_USER,DATE_CREATED,DESCRIPTION,LAST_UPDATED,UPDATE_USER,NAME,STATUS,URL,VERSION,ICON,ICON64,DOMAIN";

	private String sqlInsert = "INSERT INTO ASSET(ASSET_TYPE_NAME,ASSET_TYPE_DOMAIN,ATTRIBUTES,CREATE_USER,DATE_CREATED,DESCRIPTION,LAST_UPDATED,UPDATE_USER,NAME,STATUS,URL,VERSION,ICON,ICON64,DOMAIN)"
			+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	private String sqlUpdate = "UPDATE ASSET SET ASSET_TYPE_NAME=?, ASSET_TYPE_DOMAIN=?, ATTRIBUTES=?, CREATE_USER=?, LAST_UPDATED=?, UPDATE_USER=?, NAME=?, DESCRIPTION=?, "
			+ "URL=?, STATUS=?, VERSION=?, ICON=?, ICON64=?, DOMAIN=? WHERE ID=?";

	private String sqlUpdateStatus = "UPDATE ASSET SET STATUS=?, LAST_UPDATED=? WHERE ID=? AND DOMAIN=?";

	private String sqlFindByIdOnly = SELECT_ASSET + " FROM ASSET WHERE ID = ? AND COALESCE(STATUS,NULL,'') <> '" + STATUS_DELETED + "'";

	private String sqlFindById = SELECT_ASSET + " FROM ASSET WHERE ID = ? AND COALESCE(STATUS,NULL,'') <> '" + STATUS_DELETED + "'";

	private String sqlFindByType = SELECT_ASSET + " FROM ASSET WHERE ASSET_TYPE_NAME = ? AND COALESCE(STATUS,NULL,'') <> '"
			+ STATUS_DELETED + "'";

	private String sqlFindAll = SELECT_ASSET + " FROM ASSET WHERE COALESCE(STATUS,NULL,'') <> '" + STATUS_DELETED + "'";

	private String sqlFindByTypeAndName = SELECT_ASSET
			+ " FROM ASSET WHERE ASSET_TYPE_NAME = ? AND NAME=? AND COALESCE(STATUS,NULL,'') <> '" + STATUS_DELETED + "'";

	private String sqlFindWithAttribute = SELECT_ASSET + " FROM ASSET WHERE attributes = ? AND DOMAIN = ? AND COALESCE(STATUS,NULL,'') <> '" + STATUS_DELETED + "'";

	private String sqlFindByTypeAndDomain = SELECT_ASSET + " FROM ASSET WHERE ASSET_TYPE_NAME = ? AND DOMAIN = ? AND COALESCE(STATUS,NULL,'') <> '" + STATUS_DELETED + "'";
	
	private String sqlFindByDomain = SELECT_ASSET + " FROM ASSET WHERE DOMAIN = ? AND COALESCE(STATUS,NULL,'') <> '" + STATUS_DELETED + "'";
	
	public AssetDAO(br.mackenzie.mackleaps.asset.persistence.db.Connection conn) {
		this.conn = conn;
	}

	public Asset findById(long id) throws SQLException {
		Asset asset = null;
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stFindByIdOnly = connection.prepareStatement(sqlFindByIdOnly)) {
			stFindByIdOnly.setLong(1, id);
			try(ResultSet rs = stFindByIdOnly.executeQuery()) {
				if (rs.next()) {
					asset = retrieveFromResult(rs);
				}
			}
		}
		return asset;
	}

	public List<Asset> findAll() throws SQLException {
		List<Asset> result = new ArrayList<>();
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stFindAll = connection.prepareStatement(sqlFindAll)) {
			try(ResultSet rs = stFindAll.executeQuery()) {
				while (rs.next()) {
					result.add(retrieveFromResult(rs));
				}
			}
		}
		return result;
	}

	public List<Asset> findByType(String assetType) throws SQLException {
		List<Asset> result = new ArrayList<>();
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stFindByType = connection.prepareStatement(sqlFindByType)) {
			stFindByType.setString(1, assetType);
			try(ResultSet rs = stFindByType.executeQuery()) {
				while (rs.next()) {
					result.add(retrieveFromResult(rs));
				}
			}
		}
		return result;
	}
	
	public List<Asset> findByTypeAndDomain(String assetType, String domain) throws SQLException {
		List<Asset> result = new ArrayList<>();
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stFindByTypeAndDomain = connection.prepareStatement(sqlFindByTypeAndDomain)) {
			stFindByTypeAndDomain.setString(1, assetType);
			stFindByTypeAndDomain.setString(2, domain);
			try(ResultSet rs = stFindByTypeAndDomain.executeQuery()) {
				while (rs.next()) {
					result.add(retrieveFromResult(rs));
				}
			}
		}
		return result;
	}
	
	public List<Asset> findByDomain(String domain) throws SQLException {
		List<Asset> result = new ArrayList<>();
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stFindByDomain = connection.prepareStatement(sqlFindByDomain)) {
			stFindByDomain.setString(1, domain);
			try(ResultSet rs = stFindByDomain.executeQuery()) {
				while (rs.next()) {
					result.add(retrieveFromResult(rs));
				}
			}
		}
		return result;
	}

	public List<Asset> findWithAttribute(String key, String value, String domain) throws SQLException {
		List<Asset> result = new ArrayList<>();
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stFindWithAttribute = connection.prepareStatement(sqlFindWithAttribute)) {
			String attr = "\\\"" + key + "\\\":\\\"" + value + "\\\"";
			LOGGER.debug("Will look for attribute with {}", attr);
			stFindWithAttribute.setString(1, attr);
			stFindWithAttribute.setString(2, domain);
			try(ResultSet rs = stFindWithAttribute.executeQuery()) {
				while (rs.next()) {
					result.add(retrieveFromResult(rs));
				}
			}
		}
		return result;
	}

	public Asset update(Asset asset) throws SQLException {
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stUpdate = connection.prepareStatement(sqlUpdate)) {
			stUpdate.setString(1, asset.getAssetTypeName());
			stUpdate.setString(2, asset.getAssetTypeDomain());
			stUpdate.setString(3, Util.mapToString(asset.getAttributes()));
			stUpdate.setString(4, asset.getCreateUser());
			stUpdate.setTimestamp(5, Timestamp.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant()));
			stUpdate.setString(6, asset.getUpdateUser());
			stUpdate.setString(7, asset.getName());
			stUpdate.setString(8, asset.getDescription());
			stUpdate.setString(9, asset.getUrl());
			stUpdate.setString(10, asset.getStatus());
			stUpdate.setInt(11, asset.getVersion() + 1);
			stUpdate.setString(12, asset.getIcon());
			stUpdate.setString(13, asset.getIcon64());
			stUpdate.setString(14, asset.getDomain());
			stUpdate.setLong(15, asset.getId());
			stUpdate.executeUpdate();
		}

		return this.findById(asset.getId());
	}

	public Asset updateStatus(long assetId, String status, String domain) throws SQLException {
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stUpdateStatus = connection.prepareStatement(sqlUpdateStatus)) {
			stUpdateStatus.setString(1, status);
			stUpdateStatus.setTimestamp(2, Timestamp.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant()));
			stUpdateStatus.setLong(3, assetId);
			stUpdateStatus.setString(4, domain);
			stUpdateStatus.executeUpdate();
		}

		return this.findById(assetId);
	}

	public void delete(Asset asset) throws SQLException {
		deleteById(asset.getId());
	}

	public void deleteById(long id) throws SQLException {
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stFindById = connection.prepareStatement(sqlFindById)) {
			Asset asset = this.findById(id);
			if(asset != null) {
				asset.setStatus(STATUS_DELETED);
			}
			this.update(asset);
		}
	}

	public Asset insert(Asset asset) throws SQLException {
		long id = 0;
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stInsert = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
			stInsert.setString(1, asset.getAssetTypeName());
			stInsert.setString(2, asset.getAssetTypeDomain());
			stInsert.setString(3, Util.mapToString(asset.getAttributes()));
			stInsert.setString(4, asset.getCreateUser());
			Timestamp timestamp = Timestamp.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant());
			stInsert.setTimestamp(5, timestamp);
			stInsert.setString(6, asset.getDescription());
			stInsert.setTimestamp(7, timestamp);
			stInsert.setString(8, asset.getUpdateUser());
			stInsert.setString(9, asset.getName());
			stInsert.setString(10, STATUS_CREATED);
			stInsert.setString(11, asset.getUrl());
			stInsert.setInt(12, 1);
			stInsert.setString(13, asset.getIcon());
			stInsert.setString(14, asset.getIcon64());
			stInsert.setString(15, asset.getDomain());
			
			int r = stInsert.executeUpdate();
			
			if (r == 0) {
				LOGGER.warn("Error while insert asset: {}", asset);
			}
			try (ResultSet generatedKeys = stInsert.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					id = generatedKeys.getLong(1);
				} else {
					LOGGER.warn("Error while insert asset: {}", asset);
				}
			}
		}
			
		return this.findById(id);
	}

	public Asset findByTypeAndName(String assetType, String name) throws SQLException {
		Asset result = null;
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stFindByTypeAndName = connection.prepareStatement(sqlFindByTypeAndName)) {
			stFindByTypeAndName.setString(1, assetType);
			stFindByTypeAndName.setString(2, name);
			try(ResultSet rs = stFindByTypeAndName.executeQuery()) {
				if (rs.next()) {
					result = retrieveFromResult(rs);
				}
			}
		}
		return result;
	}

	public void insert(List<Asset> assets) throws SQLException {
		for (Asset asset : assets) {
			insert(asset);
		}
	}

	public List<Asset> findByAttribute(Map<String, String> attributes) throws SQLException {
		List<Asset> assets = findAll();
		List<Asset> results = new ArrayList<>();

		int count;
		for (Asset a : assets) {
			count = 0;
			for (Map.Entry<String, String> entry : attributes.entrySet()) {
				if (entry.getValue().equals(a.getAttributes().get(entry.getKey()))) {
					count++;
				}
			}

			if (count == attributes.size())
				results.add(a);
		}

		return results;
	}

	private static Asset retrieveFromResult(ResultSet rs) throws SQLException {
		Asset asset = new Asset();
		asset.setId(rs.getLong(FIELD_ID));
		String url = rs.getString(FIELD_URL);
		asset.setUrl(url);
		int version = rs.getInt(FIELD_VERSION);
		asset.setVersion(version);
		String name = rs.getString(FIELD_NAME);
		asset.setName(name);
		String status = rs.getString(FIELD_STATUS);
		asset.setStatus(status);
		String assetType = rs.getString(FIELD_ASSET_TYPE);
		asset.setAssetTypeName(assetType);
		String assetTypeDomain = rs.getString(FIELD_ASSET_TYPE_DOMAIN);
		asset.setAssetTypeDomain(assetTypeDomain);
		String createUser = rs.getString(FIELD_CREATE_USER);
		asset.setCreateUser(createUser);
		String description = rs.getString(FIELD_DESCRIPTION);
		asset.setDescription(description);
		Timestamp t = rs.getTimestamp(FIELD_DATE_CREATED);
		asset.setDateCreated(t);
		asset.setLastUpdated(rs.getTimestamp(FIELD_LAST_UPDATED));
		String updateUser = rs.getString(FIELD_UPDATE_USER);
		asset.setUpdateUser(updateUser);
		String attributes = rs.getString(FIELD_ATTRIBUTES);
		Map<String, String> att = Util.toMap(attributes);
		asset.setAttributes(att);
		String icon = rs.getString(FIELD_ICON);
		asset.setIcon(icon);
		String icon64 = rs.getString(FIELD_ICON64);
		asset.setIcon64(icon64);
		String domain = rs.getString(FIELD_DOMAIN);
		asset.setDomain(domain);

		return asset;
	}
}
