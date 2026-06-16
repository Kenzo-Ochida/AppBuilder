package br.mackenzie.mackleaps.asset.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mackenzie.mackleaps.asset.entity.Link;
import br.mackenzie.mackleaps.asset.persistence.db.ClientConnection;


public class LinkDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(LinkDAO.class);

	private static final String OBJECT = "OBJECT_ID";
	private static final String SUBJECT = "SUBJECT_ID";
	private static final String ASSOCIATIONTYPE = "ASSOCIATION_TYPE_NAME";
	private static final String ASSOCIATIONTYPEDOMAIN = "ASSOCIATION_TYPE_DOMAIN";
	private static final String DOMAIN = "DOMAIN";

	private br.mackenzie.mackleaps.asset.persistence.db.Connection conn;

	private String sqlDelete = "DELETE FROM LINK WHERE SUBJECT_ID = ? AND ASSOCIATION_TYPE_NAME = ? AND ASSOCIATION_TYPE_DOMAIN = ? AND OBJECT_ID = ? AND DOMAIN = ?";
	private String sqlDeleteInvolvement = "DELETE FROM LINK WHERE SUBJECT_ID = ? OR OBJECT_ID = ? AND DOMAIN = ?";
	private String sqlInsert = "INSERT INTO LINK(SUBJECT_ID, ASSOCIATION_TYPE_NAME, ASSOCIATION_TYPE_DOMAIN, OBJECT_ID, DOMAIN) VALUES(?,?,?,?,?)";
	private static final String SELECT_SQL = "SELECT SUBJECT_ID, ASSOCIATION_TYPE_NAME, ASSOCIATION_TYPE_DOMAIN, OBJECT_ID, DOMAIN FROM LINK ";
	private String sqlFindById = SELECT_SQL + "WHERE SUBJECT_ID = ? AND "
			+ "ASSOCIATION_TYPE_NAME = ? AND OBJECT_ID = ? AND DOMAIN = ?";
	private String sqlFindByObjectId = SELECT_SQL + "WHERE OBJECT_ID = ? AND DOMAIN = ?";
	private String sqlFindBySubjectId = SELECT_SQL + "WHERE SUBJECT_ID = ? AND DOMAIN = ?";
	private String sqlFindByAssociationTypeName = "SELECT SUBJECT_ID, OBJECT_ID FROM LINK WHERE ASSOCIATION_TYPE_NAME = ? AND DOMAIN = ? ORDER BY SUBJECT_ID";
	private String sqlFindBySubjectIdAndAssociationName = "SELECT OBJECT_ID FROM LINK WHERE ASSOCIATION_TYPE_NAME = ? AND SUBJECT_ID = ? AND DOMAIN = ?";
	private String sqlFindByObjectIdAndAssociationName = "SELECT SUBJECT_ID FROM LINK WHERE ASSOCIATION_TYPE_NAME = ? AND OBJECT_ID = ? AND DOMAIN = ?";
	private String sqlFindByDomain = SELECT_SQL + "WHERE DOMAIN = ?";

	public LinkDAO(br.mackenzie.mackleaps.asset.persistence.db.Connection conn) {
		this.conn = conn;
	}

	public Map<Long, List<Long>> findByAssociation(String associationTypeName, String domain) throws SQLException {

		Map<Long, List<Long>> result = new HashMap<>();
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stFindByAssociationTypeName = connection
						.prepareStatement(sqlFindByAssociationTypeName)) {
			stFindByAssociationTypeName.setString(1, associationTypeName);
			stFindByAssociationTypeName.setString(2, domain);
			try (ResultSet rs = stFindByAssociationTypeName.executeQuery()) {
				while (rs.next()) {
					long subjectId = rs.getLong(SUBJECT);
					long objectId = rs.getLong(OBJECT);

					if (!result.containsKey(subjectId)) {
						List<Long> assetList = new ArrayList<>();
						result.put(subjectId, assetList);
					}
					result.get(subjectId).add(objectId);
				}
			}
		}
		return result;
	}

	public List<Long> findObject(long subjectId, String associationName, String domain) throws SQLException {
		List<Long> result = new ArrayList<>();
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stFindBySubjectIdAndAssociationName = connection
						.prepareStatement(sqlFindBySubjectIdAndAssociationName)) {
			stFindBySubjectIdAndAssociationName.setString(1, associationName);
			stFindBySubjectIdAndAssociationName.setLong(2, subjectId);
			stFindBySubjectIdAndAssociationName.setString(3, domain);
			try (ResultSet rs = stFindBySubjectIdAndAssociationName.executeQuery()) {
				while (rs.next()) {
					long objectId = rs.getLong(OBJECT);
					result.add(objectId);
				}
			}
		}

		return result;
	}

	public List<Long> findSubject(long objectId, String associationName, String domain) throws SQLException {
		List<Long> result = new ArrayList<>();
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stFindByObjectIdAndAssociationName = connection
						.prepareStatement(sqlFindByObjectIdAndAssociationName)) {
			stFindByObjectIdAndAssociationName.setString(1, associationName);
			stFindByObjectIdAndAssociationName.setLong(2, objectId);
			stFindByObjectIdAndAssociationName.setString(3, domain);
			try (ResultSet rs = stFindByObjectIdAndAssociationName.executeQuery()) {
				while (rs.next()) {
					long subjectId = rs.getLong(SUBJECT);
					result.add(subjectId);
				}
			}
		}
		return result;
	}

	public Map<String, List<Long>> findObject(long subjectId, String domain) throws SQLException {
		Map<String, List<Long>> result = new HashMap<>();
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stFindBySubjectId = connection.prepareStatement(sqlFindBySubjectId)) {
			stFindBySubjectId.setLong(1, subjectId);
			stFindBySubjectId.setString(2, domain);
			try (ResultSet rs = stFindBySubjectId.executeQuery()) {
				while (rs.next()) {
					String associationTypeName = rs.getString(ASSOCIATIONTYPE);
					long objectId = rs.getLong(OBJECT);

					if (!result.containsKey(associationTypeName)) {
						List<Long> assetList = new ArrayList<>();
						assetList.add(objectId);
						result.put(associationTypeName, assetList);
					} else {
						result.get(associationTypeName).add(objectId);
					}
				}
			}
		}
		return result;
	}

	public Map<String, List<Long>> findSubject(long objectId, String domain) throws SQLException {
		Map<String, List<Long>> result = new HashMap<>();
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stFindByObjectId = connection.prepareStatement(sqlFindByObjectId)) {
			stFindByObjectId.setLong(1, objectId);
			stFindByObjectId.setString(2, domain);
			try (ResultSet rs = stFindByObjectId.executeQuery()) {
				while (rs.next()) {
					long subjectId = rs.getLong(SUBJECT);
					String associationTypeName = rs.getString(ASSOCIATIONTYPE);

					if (!result.containsKey(associationTypeName)) {
						List<Long> assetList = new ArrayList<>();
						assetList.add(subjectId);
						result.put(associationTypeName, assetList);
					} else {
						result.get(associationTypeName).add(subjectId);
					}
				}
			}
		}
		return result;
	}

	public List<Link> findObjectLinksBySubjectId(Long subjectId, String domain) throws SQLException {
		LOGGER.info("Searching for all the links that have the following asset id as subject {} in the given domain {}",
				subjectId, domain);
		List<Link> links = new ArrayList<>();
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stFindByObjectId = connection.prepareStatement(sqlFindBySubjectId)) {
			stFindByObjectId.setLong(1, subjectId);
			stFindByObjectId.setString(2, domain);
			try (ResultSet rs = stFindByObjectId.executeQuery()) {
				while (rs.next()) {
					long subjectIdLink = rs.getLong(SUBJECT);
					String associationTypeId = rs.getString(ASSOCIATIONTYPE);
					String associationTypeDomain = rs.getString(ASSOCIATIONTYPEDOMAIN);
					long objectId = rs.getLong(OBJECT);
					String domainLink = rs.getString(DOMAIN);

					links.add(new Link(subjectIdLink, objectId, associationTypeId, associationTypeDomain, domainLink));
				}
			}
		}
		LOGGER.debug("Found {} links with the asset id as subject", links.size());
		return links;

	}

	public List<Link> findSubjectLinksByObjectId(Long objectId, String domain) throws SQLException {
		LOGGER.info("Searching for all the links that have the following asset id as object {} in the given domain {}",
				objectId, domain);
		List<Link> links = new ArrayList<>();
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stFindByObjectId = connection.prepareStatement(sqlFindBySubjectId)) {
			stFindByObjectId.setLong(1, objectId);
			stFindByObjectId.setString(2, domain);
			try (ResultSet rs = stFindByObjectId.executeQuery()) {
				while (rs.next()) {
					long subjectIdLink = rs.getLong(SUBJECT);
					String associationTypeId = rs.getString(ASSOCIATIONTYPE);
					String associationTypeDomain = rs.getString(ASSOCIATIONTYPEDOMAIN);
					long objectIdLink = rs.getLong(OBJECT);
					String domainLink = rs.getString(DOMAIN);

					links.add(new Link(subjectIdLink, objectIdLink, associationTypeId, associationTypeDomain,
							domainLink));
				}
			}
		}
		LOGGER.debug("Found {} links with the asset id as subject", links.size());
		return links;
	}

	public List<Link> findAll() throws SQLException {
		List<Link> result = new ArrayList<>();
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stFindAll = connection.prepareStatement(SELECT_SQL);
				ResultSet rs = stFindAll.executeQuery()) {
			while (rs.next()) {
				long subjectId = rs.getLong(SUBJECT);
				String associationTypeId = rs.getString(ASSOCIATIONTYPE);
				String associationTypeDomain = rs.getString(ASSOCIATIONTYPEDOMAIN);
				long objectId = rs.getLong(OBJECT);
				String domain = rs.getString(DOMAIN);

				result.add(new Link(subjectId, objectId, associationTypeId, associationTypeDomain, domain));
			}
		}
		return result;
	}

	public void delete(Link link) throws SQLException {
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stDelete = connection.prepareStatement(sqlDelete)) {
			stDelete.setLong(1, link.getSubject());
			stDelete.setString(2, link.getAssociationName());
			stDelete.setString(3, link.getAssociationDomain());
			stDelete.setLong(4, link.getObject());
			stDelete.setString(5, link.getDomain());
			stDelete.execute();
		}
	}

	public void deleteAllAssociationsInvolvingAsset(Long assetId, String domain) throws SQLException {
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stDeleteInvolvement = connection.prepareStatement(sqlDeleteInvolvement)) {
			stDeleteInvolvement.setLong(1, assetId);
			stDeleteInvolvement.setLong(2, assetId);
			stDeleteInvolvement.setString(3, domain);
			stDeleteInvolvement.execute();
		}
	}

	public Link insertLink(Link link) throws SQLException {
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stInsert = connection.prepareStatement(sqlInsert)) {
			stInsert.setLong(1, link.getSubject());
			stInsert.setString(2, link.getAssociationName());
			stInsert.setString(3, link.getAssociationDomain());
			stInsert.setLong(4, link.getObject());
			stInsert.setString(5, link.getDomain());
			stInsert.execute();
		}
		return findById(link);
	}

	private Link findById(Link p) throws SQLException {
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stFindById = connection.prepareStatement(sqlFindById)) {
			stFindById.setLong(1, p.getSubject());
			stFindById.setString(2, p.getAssociationName());
			stFindById.setLong(3, p.getObject());
			stFindById.setString(4, p.getDomain());
			try (ResultSet rs = stFindById.executeQuery()) {
				if (rs.next()) {
					return p;
				}
			}
		}
		return null;
	}

	public List<Link> findByDomain(String domain) throws SQLException {
		List<Link> links = new ArrayList<>();
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stFindById = connection.prepareStatement(sqlFindByDomain)) {
			stFindById.setString(1, domain);

			ResultSet rs = stFindById.executeQuery();
			while (rs.next()) {
				long subjectId = rs.getLong(SUBJECT);
				String associationTypeId = rs.getString(ASSOCIATIONTYPE);
				String associationDomain = rs.getString(ASSOCIATIONTYPEDOMAIN);
				long objectId = rs.getLong(OBJECT);
				String domainObject = rs.getString(DOMAIN);
				links.add(new Link(subjectId, objectId, associationTypeId, associationDomain, domainObject));
			}
		} catch (Exception e2) {
			LOGGER.error("Error while retrieving links by domain", e2);
		}
		return links;
	}
}