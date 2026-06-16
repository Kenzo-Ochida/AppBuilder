package br.mackenzie.mackleaps.asset.persistence.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.mackenzie.mackleaps.asset.entity.AssetType;
import br.mackenzie.mackleaps.asset.persistence.db.ClientConnection;
import br.mackenzie.mackleaps.utils.Util;


public class AssetTypeDAO {

	private br.mackenzie.mackleaps.asset.persistence.db.Connection conn;
	public static final String STATUS_DELETED = "deleted";
	private static final String STATUS_CREATED = "created";
	
	private static final String NAME = "NAME";
	private static final String STATUS = "STATUS";
	private static final String DOMAIN = "DOMAIN";
	private static final String ATTRIBUTESLABEL = "ATTRIBUTES_LABEL";
	private static final String ATTRIBUTESTYPE = "ATTRIBUTES_TYPE";
	private static final String I18NCREATEUSERLABEL = "I18N_CREATEUSER_LABEL";
	private static final String I18NCREATEDLABEL = "I18N_DATECREATED_LABEL";
	private static final String I18NDESCRIPTIONLABEL = "I18N_DESCRIPTION_LABEL";
	private static final String I18NIDLABEL= "I18N_ID_LABEL";
	private static final String I18NLASTUPDATEDLABEL = "I18N_LASTUPDATED_LABEL";
	private static final String I18NNAMELABEL = "I18N_NAME_LABEL";
	private static final String I18NSTATUSLABEL = "I18N_STATUS_LABEL";
	private static final String I18NUPDATEUSERLABEL = "I18N_UPDATEUSER_LABEL";
	private static final String ICON = "ICON";
	private static final String ICON64 = "ICON64";
	
	private String sqlInsert = "INSERT INTO ASSET_TYPE(NAME, ATTRIBUTES_LABEL, I18N_CREATEUSER_LABEL, I18N_DATECREATED_LABEL, I18N_DESCRIPTION_LABEL,"
			+ "I18N_ID_LABEL, I18N_LASTUPDATED_LABEL, I18N_NAME_LABEL, I18N_STATUS_LABEL, I18N_UPDATEUSER_LABEL, ICON, ICON64, ATTRIBUTES_TYPE, STATUS, DOMAIN)"
			+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	private String sqlUpdate = "UPDATE ASSET_TYPE SET ATTRIBUTES_LABEL=?,I18N_CREATEUSER_LABEL=?,I18N_DATECREATED_LABEL=?,I18N_DESCRIPTION_LABEL=?,"
			+ "I18N_ID_LABEL=?,I18N_LASTUPDATED_LABEL=?,I18N_NAME_LABEL=?,I18N_STATUS_LABEL=?,"
			+ "I18N_UPDATEUSER_LABEL=?,ICON=?,ICON64=?, ATTRIBUTES_TYPE=?, STATUS=?, DOMAIN=? WHERE NAME=?";

	private String sqlFindByName = "SELECT NAME,STATUS,ATTRIBUTES_LABEL,I18N_CREATEUSER_LABEL,I18N_DATECREATED_LABEL,I18N_DESCRIPTION_LABEL,"
			+ I18NIDLABEL + ",I18N_LASTUPDATED_LABEL,I18N_NAME_LABEL,I18N_STATUS_LABEL,I18N_UPDATEUSER_LABEL,"
			+ "ICON,ICON64, ATTRIBUTES_TYPE, DOMAIN FROM ASSET_TYPE WHERE NAME = ? AND COALESCE(STATUS,NULL,'') <> '" + STATUS_DELETED + "'";

	private String sqlFindByNameAndDomain = "SELECT NAME,STATUS,ATTRIBUTES_LABEL,I18N_CREATEUSER_LABEL,I18N_DATECREATED_LABEL,I18N_DESCRIPTION_LABEL,"
			+ "I18N_ID_LABEL,I18N_LASTUPDATED_LABEL,I18N_NAME_LABEL,I18N_STATUS_LABEL,I18N_UPDATEUSER_LABEL,"
			+ "ICON,ICON64, ATTRIBUTES_TYPE, DOMAIN FROM ASSET_TYPE WHERE NAME = ? AND DOMAIN = ? AND COALESCE(STATUS,NULL,'') <> '" + STATUS_DELETED + "'";


	private String sqlFindAll = "SELECT NAME,DOMAIN,ATTRIBUTES_LABEL,I18N_CREATEUSER_LABEL,I18N_DATECREATED_LABEL,I18N_DESCRIPTION_LABEL,"
			+ "I18N_ID_LABEL,I18N_LASTUPDATED_LABEL,I18N_NAME_LABEL,I18N_STATUS_LABEL,I18N_UPDATEUSER_LABEL,"
			+ "ICON,ICON64,ATTRIBUTES_TYPE FROM ASSET_TYPE WHERE COALESCE(STATUS,NULL,'') <> '" + STATUS_DELETED + "'";

	private String sqlList = "SELECT NAME FROM ASSET_TYPE";
	
	private String sqlListByDomain = "SELECT NAME FROM ASSET_TYPE WHERE DOMAIN=?";
	
	public AssetTypeDAO(br.mackenzie.mackleaps.asset.persistence.db.Connection conn) {
		this.conn = conn;
	}

	public List<AssetType> findByNameList(String name) throws SQLException {
		List<AssetType> results = new ArrayList<>();
		AssetType r = null;
		try(Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stFindByName = connection.prepareStatement(sqlFindByName)) {
			stFindByName.setString(1, name);
			try (ResultSet rs = stFindByName.executeQuery()) {
				while (rs.next()) {
					r = new AssetType();
					String strName = rs.getString("NAME");
					r.setName(strName);
					String strStatus = rs.getString(STATUS);
					r.setStatus(strStatus);
					String strDomain = rs.getString(DOMAIN);
					r.setDomain(strDomain);
					String attributesLabel = rs.getString(ATTRIBUTESLABEL);
					Map<String, String> label = Util.toMap(attributesLabel);
					r.setAttributesLabel(label);
					String attributesType = rs.getString(ATTRIBUTESTYPE);
					Map<String, String> type = Util.toMap(attributesType);
					r.setAttributesType(type);
					String i18nCreateUserLabel = rs.getString(I18NCREATEUSERLABEL);
					r.setI18nCreateUserLabel(i18nCreateUserLabel);
					String i18nDateCreatedLabel = rs.getString(I18NCREATEDLABEL);
					r.setI18nDateCreatedLabel(i18nDateCreatedLabel);
					String i18nDescriptionLabel = rs.getString(I18NDESCRIPTIONLABEL);
					r.setI18nDescriptionLabel(i18nDescriptionLabel);
					String i18nIdLabel = rs.getString(I18NIDLABEL);
					r.setI18nIdLabel(i18nIdLabel);
					String i18nLastUpdatedLabel = rs.getString(I18NLASTUPDATEDLABEL);
					r.setI18nLastUpdatedLabel(i18nLastUpdatedLabel);
					String i18nNameLabel = rs.getString(I18NNAMELABEL);
					r.setI18nNameLabel(i18nNameLabel);
					String i18nStatusLabel = rs.getString(I18NSTATUSLABEL);
					r.setI18nStatusLabel(i18nStatusLabel);
					String i18nUpdateUserLabel = rs.getString(I18NUPDATEUSERLABEL);
					r.setI18nUpdateUserLabel(i18nUpdateUserLabel);
					String icon = rs.getString(ICON);
					r.setIcon(icon);
					String icon64 = rs.getString(ICON64);
					r.setIcon64(icon64);
					results.add(r);
				}
			}
		}
		return results;
	}
	
	public AssetType findByName(String name) throws SQLException {
		AssetType r = null;
		try(Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stFindByName = connection.prepareStatement(sqlFindByName)) {
			stFindByName.setString(1, name);
			try (ResultSet rs = stFindByName.executeQuery()) {
				if (rs.next()) {
					r = new AssetType();
					String strName = rs.getString(NAME);
					r.setName(strName);
					String strStatus = rs.getString(STATUS);
					r.setStatus(strStatus);
					String strDomain = rs.getString(DOMAIN);
					r.setDomain(strDomain);
					String attributesLabel = rs.getString(ATTRIBUTESLABEL);
					Map<String, String> label = Util.toMap(attributesLabel);
					r.setAttributesLabel(label);
					String attributesType = rs.getString(ATTRIBUTESTYPE);
					Map<String, String> type = Util.toMap(attributesType);
					r.setAttributesType(type);
					String i18nCreateUserLabel = rs.getString(I18NCREATEUSERLABEL);
					r.setI18nCreateUserLabel(i18nCreateUserLabel);
					String i18nDateCreatedLabel = rs.getString(I18NCREATEDLABEL);
					r.setI18nDateCreatedLabel(i18nDateCreatedLabel);
					String i18nDescriptionLabel = rs.getString(I18NDESCRIPTIONLABEL);
					r.setI18nDescriptionLabel(i18nDescriptionLabel);
					String i18nIdLabel = rs.getString(I18NIDLABEL);
					r.setI18nIdLabel(i18nIdLabel);
					String i18nLastUpdatedLabel = rs.getString(I18NLASTUPDATEDLABEL);
					r.setI18nLastUpdatedLabel(i18nLastUpdatedLabel);
					String i18nNameLabel = rs.getString(I18NNAMELABEL);
					r.setI18nNameLabel(i18nNameLabel);
					String i18nStatusLabel = rs.getString(I18NSTATUSLABEL);
					r.setI18nStatusLabel(i18nStatusLabel);
					String i18nUpdateUserLabel = rs.getString(I18NUPDATEUSERLABEL);
					r.setI18nUpdateUserLabel(i18nUpdateUserLabel);
					String icon = rs.getString(ICON);
					r.setIcon(icon);
					String icon64 = rs.getString(ICON64);
					r.setIcon64(icon64);
				}
			}
		}
		return r;
	}

	public AssetType findByNameAndDomain(String name, String domain) throws SQLException {
		AssetType r = null;
		try(Connection connection = ClientConnection.getClient(conn);
			PreparedStatement stFindByName = connection.prepareStatement(sqlFindByNameAndDomain)) {
			stFindByName.setString(1, name);
			stFindByName.setString(2, domain);
			try (ResultSet rs = stFindByName.executeQuery()) {
				if (rs.next()) {
					r = new AssetType();
					String strName = rs.getString(NAME);
					r.setName(strName);
					String strStatus = rs.getString(STATUS);
					r.setStatus(strStatus);
					String strDomain = rs.getString(DOMAIN);
					r.setDomain(strDomain);
					String attributesLabel = rs.getString(ATTRIBUTESLABEL);
					Map<String, String> label = Util.toMap(attributesLabel);
					r.setAttributesLabel(label);
					String attributesType = rs.getString(ATTRIBUTESTYPE);
					Map<String, String> type = Util.toMap(attributesType);
					r.setAttributesType(type);
					String i18nCreateUserLabel = rs.getString(I18NCREATEUSERLABEL);
					r.setI18nCreateUserLabel(i18nCreateUserLabel);
					String i18nDateCreatedLabel = rs.getString(I18NCREATEDLABEL);
					r.setI18nDateCreatedLabel(i18nDateCreatedLabel);
					String i18nDescriptionLabel = rs.getString(I18NDESCRIPTIONLABEL);
					r.setI18nDescriptionLabel(i18nDescriptionLabel);
					String i18nIdLabel = rs.getString(I18NIDLABEL);
					r.setI18nIdLabel(i18nIdLabel);
					String i18nLastUpdatedLabel = rs.getString(I18NLASTUPDATEDLABEL);
					r.setI18nLastUpdatedLabel(i18nLastUpdatedLabel);
					String i18nNameLabel = rs.getString(I18NNAMELABEL);
					r.setI18nNameLabel(i18nNameLabel);
					String i18nStatusLabel = rs.getString(I18NSTATUSLABEL);
					r.setI18nStatusLabel(i18nStatusLabel);
					String i18nUpdateUserLabel = rs.getString(I18NUPDATEUSERLABEL);
					r.setI18nUpdateUserLabel(i18nUpdateUserLabel);
					String icon = rs.getString(ICON);
					r.setIcon(icon);
					String icon64 = rs.getString(ICON64);
					r.setIcon64(icon64);
				}
			}
		}
		return r;
	}

	public List<AssetType> findAll() throws SQLException {
		List<AssetType> result = new ArrayList<>();
		try(Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stFindAll = connection.prepareStatement(sqlFindAll);
				ResultSet rs = stFindAll.executeQuery()) {
			while (rs.next()) {
				AssetType r = new AssetType();
				String name = rs.getString(NAME);
				r.setName(name);
				String strStatus = rs.getString(STATUS);
				r.setStatus(strStatus);
				String domain = rs.getString(DOMAIN);
				r.setDomain(domain);
				String attributesLabel = rs.getString(ATTRIBUTESLABEL);
				Map<String, String> label = Util.toMap(attributesLabel);
				r.setAttributesLabel(label);
				String attributesType = rs.getString(ATTRIBUTESTYPE);
				Map<String, String> type = Util.toMap(attributesType);
				r.setAttributesType(type);
				String i18nCreateUserLabel = rs.getString(I18NCREATEUSERLABEL);
				r.setI18nCreateUserLabel(i18nCreateUserLabel);
				String i18nDateCreatedLabel = rs.getString(I18NCREATEDLABEL);
				r.setI18nDateCreatedLabel(i18nDateCreatedLabel);
				String i18nDescriptionLabel = rs.getString(I18NDESCRIPTIONLABEL);
				r.setI18nDescriptionLabel(i18nDescriptionLabel);
				String i18nIdLabel = rs.getString(I18NIDLABEL);
				r.setI18nIdLabel(i18nIdLabel);
				String i18nLastUpdatedLabel = rs.getString(I18NLASTUPDATEDLABEL);
				r.setI18nLastUpdatedLabel(i18nLastUpdatedLabel);
				String i18nNameLabel = rs.getString(I18NNAMELABEL);
				r.setI18nNameLabel(i18nNameLabel);
				String i18nStatusLabel = rs.getString(I18NSTATUSLABEL);
				r.setI18nStatusLabel(i18nStatusLabel);
				String i18nUpdateUserLabel = rs.getString(I18NUPDATEUSERLABEL);
				r.setI18nUpdateUserLabel(i18nUpdateUserLabel);
				String icon = rs.getString(ICON);
				r.setIcon(icon);
				String icon64 = rs.getString(ICON64);
				r.setIcon64(icon64);
				
				result.add(r);
			}
		}
		return result;
	}

	public AssetType update(AssetType asset) throws SQLException {
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stUpdate = connection.prepareStatement(sqlUpdate)) {
			stUpdate.setString(1, Util.mapToString(asset.getAttributesLabel()));
			stUpdate.setString(2, asset.getI18nCreateUserLabel());
			stUpdate.setString(3, asset.getI18nDateCreatedLabel());
			stUpdate.setString(4, asset.getI18nDescriptionLabel());
			stUpdate.setString(5, asset.getI18nIdLabel());
			stUpdate.setString(6, asset.getI18nLastUpdatedLabel());
			stUpdate.setString(7, asset.getI18nNameLabel());
			stUpdate.setString(8, asset.getI18nStatusLabel());
			stUpdate.setString(9, asset.getI18nUpdateUserLabel());
			stUpdate.setString(10, asset.getIcon());
			stUpdate.setString(11, asset.getIcon64());
			stUpdate.setString(12, Util.mapToString(asset.getAttributesType()));
			stUpdate.setString(13, asset.getStatus());
			stUpdate.setString(14, asset.getDomain());
			stUpdate.setString(15, asset.getName());
			stUpdate.executeUpdate();
		}

		return this.findByName(asset.getName());
	}

	public void delete(AssetType asset) throws SQLException {
		deleteByName(asset.getName(), asset.getDomain());
	}

	public void deleteByName(String name, String domain) throws SQLException {
		AssetType assetType = this.findByNameAndDomain(name, domain);
		if(assetType != null) {
			assetType.setStatus(STATUS_DELETED);
			this.update(assetType);
		}
	}

	public List<String> listAssetTypes() throws SQLException {
		List<String> l = new ArrayList<>();
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stList = connection.prepareStatement(sqlList)) {
			try(ResultSet rs = stList.executeQuery()) {
				while (rs.next()) {
					l.add(rs.getString("NAME"));
				}
			}
		}
		return l;
	}
	
	public List<String> listAssetTypesByDomain(String domain) throws SQLException {
		List<String> l = new ArrayList<>();
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stListByDomain = connection.prepareStatement(sqlListByDomain)) {
			stListByDomain.setString(1, domain);
			try(ResultSet rs = stListByDomain.executeQuery()) {
				while (rs.next()) {
					l.add(rs.getString("NAME"));
				}
			}
		}
		return l;
	}

	public AssetType insert(AssetType asset) throws SQLException {
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stInsert = connection.prepareStatement(sqlInsert)) {
			stInsert.setString(1, asset.getName());
			stInsert.setString(2, Util.mapToString(asset.getAttributesLabel()));
			stInsert.setString(3, asset.getI18nCreateUserLabel());
			stInsert.setString(4, asset.getI18nDateCreatedLabel());
			stInsert.setString(5, asset.getI18nDescriptionLabel());
			stInsert.setString(6, asset.getI18nIdLabel());
			stInsert.setString(7, asset.getI18nLastUpdatedLabel());
			stInsert.setString(8, asset.getI18nNameLabel());
			stInsert.setString(9, asset.getI18nStatusLabel());
			stInsert.setString(10, asset.getI18nUpdateUserLabel());
			stInsert.setString(11, asset.getIcon());
			stInsert.setString(12, asset.getIcon64());
			stInsert.setString(13, Util.mapToString(asset.getAttributesType()));
			stInsert.setString(14, STATUS_CREATED);
			stInsert.setString(15, asset.getDomain());
			stInsert.executeUpdate();
		}
		
		return this.findByName(asset.getName());
	}

}
