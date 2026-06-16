package br.mackenzie.mackleaps.asset.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.json.JSONObject;

import br.mackenzie.mackleaps.asset.entity.AssociationType;
import br.mackenzie.mackleaps.asset.persistence.AssociationTypeDAOSingleton;
import br.mackenzie.mackleaps.utils.Marshalling;

import org.springframework.stereotype.Service;

@Service
public class AssociationTypeService {

	public AssociationType findByName(String name, String domain) throws SQLException {
		return AssociationTypeDAOSingleton.getInstance().findByName(name, domain);
	}
	
	public void deleteByName(String name, String domain) throws SQLException {
		AssociationTypeDAOSingleton.getInstance().deleteByName(name, domain);
	}
	
	public List<AssociationType> findAll() throws SQLException {
		return AssociationTypeDAOSingleton.getInstance().findAll();
	}
	public List<AssociationType> findByDomain(String domain) throws SQLException {
		return AssociationTypeDAOSingleton.getInstance().findByDomain(domain);
	}
	
	public AssociationType insertAssociationType(AssociationType associationType, String user, String role, String domain) throws IOException, SQLException {
		/* TODO: check if provided domain parameter is equal to the AssetType domain attribute*/
		return AssociationTypeDAOSingleton.getInstance().insert(associationType);
	}
	
	public AssociationType updateAssociationType(AssociationType associationType, String domain) throws SQLException {
		/* TODO: check if provided domain parameter is equal to the AssetType domain attribute*/
		return AssociationTypeDAOSingleton.getInstance().update(associationType);
	}
}