package br.mackenzie.mackleaps.asset.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.mackenzie.mackleaps.asset.entity.AssociationType;
import br.mackenzie.mackleaps.asset.persistence.db.ClientConnection;



public class AssociationTypeDAO{

	private br.mackenzie.mackleaps.asset.persistence.db.Connection conn;

	private static final String DESCRIPTION = "DESCRIPTION";
	private String sqlDeleteById = "DELETE FROM ASSOCIATION_TYPE WHERE NAME = ? AND DOMAIN = ?";
	private String sqlFindById = "SELECT NAME, DESCRIPTION, DOMAIN FROM ASSOCIATION_TYPE WHERE NAME = ? AND DOMAIN = ?";
	private String sqlUpdate = "UPDATE ASSOCIATION_TYPE SET DESCRIPTION=? WHERE NAME=? AND DOMAIN = ?";
	private String sqlInsert = "INSERT INTO ASSOCIATION_TYPE(NAME,DESCRIPTION, DOMAIN) VALUES(?,?,?)";
	private String sqlFindAll = "SELECT NAME, DESCRIPTION, DOMAIN FROM ASSOCIATION_TYPE";
	private String sqlFindByDomain = "SELECT NAME, DESCRIPTION, DOMAIN FROM ASSOCIATION_TYPE WHERE DOMAIN = ?";

	public AssociationTypeDAO(br.mackenzie.mackleaps.asset.persistence.db.Connection conn) {
		this.conn = conn;
	}

	public AssociationType findByName(String name, String domain) throws SQLException {
		AssociationType p = null;
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stFindById = connection.prepareStatement(sqlFindById)) {
			stFindById.setString(1, name);
			stFindById.setString(2,  domain);
			try(ResultSet rs = stFindById.executeQuery()) {
				if (rs.next()) {
					String description = rs.getString(DESCRIPTION);
					p = new AssociationType(name, description, domain);
				}
			}
		}
		return p;
	}

	public List<AssociationType> findAll() throws SQLException {
		List<AssociationType> result = new ArrayList<>();
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stFindAll = connection.prepareStatement(sqlFindAll);
				ResultSet rs = stFindAll.executeQuery()){
			while (rs.next()) {
				result.add(new AssociationType(rs.getString("NAME"), rs.getString(DESCRIPTION), rs.getString("DOMAIN")));
			}
		}
		return result;
	}
	
	public List<AssociationType> findByDomain(String domain) throws SQLException {
		List<AssociationType> result = new ArrayList<>();
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stFindByDomain = connection.prepareStatement(sqlFindByDomain)) {
			stFindByDomain.setString(1, domain);
			try(ResultSet rs = stFindByDomain.executeQuery()) {
				while (rs.next()) {
					result.add(new AssociationType(rs.getString("NAME"), rs.getString(DESCRIPTION), rs.getString("DOMAIN")));
				}
			}
		}
		return result;
	}

	public AssociationType update(AssociationType associationType) throws SQLException {
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stUpdate = connection.prepareStatement(sqlUpdate)) {
			stUpdate.setString(2, associationType.getName());
			stUpdate.setString(1, associationType.getDescription());
			stUpdate.setString(3, associationType.getDomain());
			stUpdate.executeUpdate();
		}

		return this.findByName(associationType.getName(), associationType.getDomain());
	}

	public void delete(AssociationType associationType) throws SQLException {
		deleteByName(associationType.getName(), associationType.getDomain());
	}

	public void deleteByName(String id, String domain) throws SQLException {
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stDeleteById = connection.prepareStatement(sqlDeleteById)) {
			stDeleteById.setString(1, id);
			stDeleteById.setString(2, domain);
			stDeleteById.executeUpdate();
		}
	}

	public AssociationType insert(AssociationType associationType) throws SQLException {
		try (Connection connection = ClientConnection.getClient(conn);
				PreparedStatement stInsert = connection.prepareStatement(sqlInsert)) {
			stInsert.setString(1, associationType.getName());
			stInsert.setString(2, associationType.getDescription());
			stInsert.setString(3, associationType.getDomain());
			stInsert.execute();
		}
		return findByName(associationType.getName(), associationType.getDomain());
	}
}
