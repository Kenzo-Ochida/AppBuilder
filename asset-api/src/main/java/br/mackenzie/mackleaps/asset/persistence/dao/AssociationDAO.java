package br.mackenzie.mackleaps.asset.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.mackenzie.mackleaps.asset.entity.Association;
import br.mackenzie.mackleaps.asset.persistence.db.ClientConnection;

public class AssociationDAO {

    private static final String OBJECT = "OBJECT_TYPE_NAME";
    private static final String SUBJECT = "SUBJECT_TYPE_NAME";
    private static final String ASSOCIATIONTYPE = "ASSOCIATION_TYPE_NAME";
    private static final String DOMAIN = "DOMAIN";
    private static final String CONNECTOR = "= ? AND ";

    private br.mackenzie.mackleaps.asset.persistence.db.Connection conn;

    private static final String SELECT_ASSOCIATION = "SELECT SUBJECT_TYPE_NAME, ASSOCIATION_TYPE_NAME, OBJECT_TYPE_NAME, DOMAIN FROM ASSOCIATION ";
    private String sqlFindById = SELECT_ASSOCIATION + "WHERE SUBJECT_TYPE_NAME = ? AND ASSOCIATION_TYPE_NAME = ? AND OBJECT_TYPE_NAME = ? AND DOMAIN = ?";
    private String sqlDelete = "DELETE FROM ASSOCIATION WHERE " + SUBJECT + CONNECTOR + ASSOCIATIONTYPE + CONNECTOR + OBJECT + CONNECTOR + DOMAIN + "= ?";
    private String sqlInsert = "INSERT INTO ASSOCIATION(SUBJECT_TYPE_NAME, ASSOCIATION_TYPE_NAME, OBJECT_TYPE_NAME, DOMAIN) VALUES(?,?,?,?)";
    private String sqlFindByAssociationTypeName = "SELECT SUBJECT_TYPE_NAME, OBJECT_TYPE_NAME, DOMAIN FROM ASSOCIATION WHERE ASSOCIATION_TYPE_NAME = ?";
    private String sqlFindByAssociationTypeNameAndDomain = SELECT_ASSOCIATION + "WHERE ASSOCIATION_TYPE_NAME = ? AND DOMAIN = ?";
    private String sqlFindByDomain = SELECT_ASSOCIATION + "WHERE DOMAIN = ?";
    private String sqlFindBySubjectTypeAndDomain = SELECT_ASSOCIATION + " WHERE " + SUBJECT + " = ? AND " + DOMAIN + " = ?";
    private String sqlFindByObjectTypeAndDomain = SELECT_ASSOCIATION + " WHERE " + OBJECT + " = ? AND " + DOMAIN + " = ?";

    public AssociationDAO(br.mackenzie.mackleaps.asset.persistence.db.Connection conn) {
        this.conn = conn;
    }

    public Map<String, List<String>> findByAssociationType(String associationTypeName, String domain) throws SQLException {
        Map<String, List<String>> result = new HashMap<>();
        try (Connection connection = ClientConnection.getClient(conn);
                PreparedStatement stFindByAssociationTypeName = connection
                        .prepareStatement(sqlFindByAssociationTypeName)) {
            stFindByAssociationTypeName.setString(1, associationTypeName);
            try (ResultSet rs = stFindByAssociationTypeName.executeQuery()) {
                while (rs.next()) {
                    String subjectType = rs.getString(SUBJECT);
                    String objectType = rs.getString(OBJECT);
                    if (!rs.getString(DOMAIN).equals(domain)) {
                        continue;
                    }
                    if (!result.containsKey(subjectType)) {
                        List<String> assetList = new ArrayList<>();
                        result.put(subjectType, assetList);
                    }
                    result.get(subjectType).add(objectType);
                }
            }
        }
        return result;
    }

    public Map<String, List<String>> findByAssociationTypeObject(String associationTypeName, String domain) throws SQLException {
        Map<String, List<String>> result = new HashMap<>();
        try (Connection connection = ClientConnection.getClient(conn);
                PreparedStatement stFindByAssociationTypeName = connection
                        .prepareStatement(sqlFindByAssociationTypeName)) {
            stFindByAssociationTypeName.setString(1, associationTypeName);
            try (ResultSet rs = stFindByAssociationTypeName.executeQuery()) {
                while (rs.next()) {
                    if (!rs.getString(DOMAIN).equals(domain)) {
                        continue;
                    }
                    String subjectType = rs.getString(SUBJECT);
                    String objectType = rs.getString(OBJECT);
                    if (!result.containsKey(objectType)) {
                        List<String> assetList = new ArrayList<>();
                        result.put(objectType, assetList);
                    }
                    result.get(objectType).add(subjectType);
                }
            }
        }
        return result;
    }

    private Association findById(Association p) throws SQLException {
        try (Connection connection = ClientConnection.getClient(conn);
                PreparedStatement stFindById = connection.prepareStatement(sqlFindById)) {
            stFindById.setString(1, p.getSubjectType());
            stFindById.setString(2, p.getAssociationTypeName());
            stFindById.setString(3, p.getObjectType());
            stFindById.setString(4, p.getDomain());
            try (ResultSet rs = stFindById.executeQuery()) {
                if (rs.next()) {
                    return p;
                }
            }
        }
        return null;
    }

    public void delete(Association association) throws SQLException {
        try (Connection connection = ClientConnection.getClient(conn);
                PreparedStatement stDelete = connection.prepareStatement(sqlDelete)) {
            stDelete.setString(1, association.getSubjectType());
            stDelete.setString(2, association.getAssociationTypeName());
            stDelete.setString(3, association.getObjectType());
            stDelete.setString(4, association.getDomain());
            stDelete.execute();
        }
    }

    public List<Association> findAll() throws SQLException {
        List<Association> result = new ArrayList<>();
        try (Connection connection = ClientConnection.getClient(conn);
                PreparedStatement stFindAll = connection.prepareStatement(SELECT_ASSOCIATION)) {
            try (ResultSet rs = stFindAll.executeQuery()) {
                while (rs.next()) {
                    String subjectType = rs.getString(SUBJECT);
                    String associationType = rs.getString(ASSOCIATIONTYPE);
                    String objectType = rs.getString(OBJECT);
                    String domain = rs.getString(DOMAIN);

                    result.add(new Association(subjectType,  objectType, associationType, domain));
                }
            }
        }
        return result;
    }

    public List<Association> findAssociationsBySubjectType(String subjectType, String domain) throws SQLException {
        List<Association> result = new ArrayList<>();
        try (Connection connection = ClientConnection.getClient(conn);
                PreparedStatement stFindBySubject = connection.prepareStatement(sqlFindBySubjectTypeAndDomain)) {
            stFindBySubject.setString(1, subjectType);
            stFindBySubject.setString(2, domain);
            try (ResultSet rs = stFindBySubject.executeQuery()) {
                while (rs.next()) {
                    String associationType = rs.getString(ASSOCIATIONTYPE);
                    String objectType = rs.getString(OBJECT);

                    result.add(new Association(subjectType, objectType, associationType, domain));
                }
            }
        }
        return result;
    }

    public List<Association> findAssociationsByObjectType(String objectType, String domain) throws SQLException {
        List<Association> result = new ArrayList<>();
        try (Connection connection = ClientConnection.getClient(conn);
                PreparedStatement stFindByObject = connection.prepareStatement(sqlFindByObjectTypeAndDomain)) {
            stFindByObject.setString(1, objectType);
            stFindByObject.setString(2, domain);
            try (ResultSet rs = stFindByObject.executeQuery()) {
                while (rs.next()) {
                    String associationType = rs.getString(ASSOCIATIONTYPE);
                    String subjectType = rs.getString(SUBJECT);

                    result.add(new Association(subjectType,  objectType, associationType, domain));
                }
            }
        }
        return result;
    }

    public List<Association> findByAssociationTypeAndDomain(String associationType, String domain) throws SQLException {
        List<Association> result = new ArrayList<>();
        try (Connection connection = ClientConnection.getClient(conn);
                PreparedStatement stFindByAssociationTypeAndDomain = connection.prepareStatement(sqlFindByAssociationTypeNameAndDomain)) {
            stFindByAssociationTypeAndDomain.setString(1, associationType);
            stFindByAssociationTypeAndDomain.setString(2, domain);
            try (ResultSet rs = stFindByAssociationTypeAndDomain.executeQuery()) {
                while (rs.next()) {
                    String subjectType = rs.getString(SUBJECT);
                    String associationTypeName = rs.getString(ASSOCIATIONTYPE);
                    String objectType = rs.getString(OBJECT);
                    String domainFromSQL = rs.getString(DOMAIN);

                    result.add(new Association(subjectType,  objectType, associationTypeName, domainFromSQL));
                }
            }
        }
        return result;
    }

    public List<Association> findByDomain(String domain) throws SQLException {
        List<Association> result = new ArrayList<>();
        try (Connection connection = ClientConnection.getClient(conn);
                PreparedStatement stFindByDomain = connection.prepareStatement(sqlFindByDomain)) {
            stFindByDomain.setString(1, domain);
            try (ResultSet rs = stFindByDomain.executeQuery()) {
                while (rs.next()) {
                    String subjectType = rs.getString(SUBJECT);
                    String associationType = rs.getString(ASSOCIATIONTYPE);
                    String objectType = rs.getString(OBJECT);
                    String domainFromSQL = rs.getString(DOMAIN);

                    result.add(new Association(subjectType,  objectType, associationType, domainFromSQL));
                }
            }
        }
        return result;
    }

    public boolean findEqual(Association association) throws SQLException {
        try (Connection connection = ClientConnection.getClient(conn);
                PreparedStatement stFindAll = connection.prepareStatement(SELECT_ASSOCIATION);
                ResultSet rs = stFindAll.executeQuery()) {
            while (rs.next()) {
                String subjectType = rs.getString(SUBJECT);
                String associationType = rs.getString(ASSOCIATIONTYPE);
                String objectType = rs.getString(OBJECT);
                String domain = rs.getString(DOMAIN);

                if (association.getSubjectType().equals(subjectType)
                        && association.getAssociationTypeName().equals(associationType)
                        && association.getObjectType().equals(objectType)
                        && association.getDomain().equals(domain)) {
                    return true;
                }
            }
        }
        return false;
    }

	public Association insertAssociation(Association association) throws SQLException {
		try (
			Connection connection = ClientConnection.getClient(conn);
			PreparedStatement stInsert = connection.prepareStatement(sqlInsert)
		) {
			if (findById(association) != null) {
				return association;
			}
	
			stInsert.setString(1, association.getSubjectType());
			stInsert.setString(2, association.getAssociationTypeName());
			stInsert.setString(3, association.getObjectType());
			stInsert.setString(4, association.getDomain());
			stInsert.executeUpdate();
		}
		return association;
	}
	
	}
