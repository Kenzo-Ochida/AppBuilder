package br.mackenzie.mackleaps.asset.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;


import br.mackenzie.mackleaps.asset.entity.Asset;
import br.mackenzie.mackleaps.asset.entity.Association;
import br.mackenzie.mackleaps.asset.entity.Link;
import br.mackenzie.mackleaps.asset.persistence.AssetDAOSingleton;
import br.mackenzie.mackleaps.asset.persistence.AssociationDAOSingleton;
import br.mackenzie.mackleaps.asset.persistence.LinkDAOSingleton;

@Service
public class AssociationService {


	public List<Association> findAll() throws SQLException {
		return AssociationDAOSingleton.getInstance().findAll();
	}
	
	public List<Association> findByAssociationTypeAndDomain(String associationType, String domain) throws SQLException {
		return AssociationDAOSingleton.getInstance().findByAssociationTypeAndDomain(associationType, domain);
	}
	
	public List<Association> findByDomain(String domain) throws SQLException {
		return AssociationDAOSingleton.getInstance().findByDomain(domain);
	}

	public Map<String, List<String>> getAssociations(String associationTypeName, String domain) throws SQLException {
		return AssociationDAOSingleton.getInstance().findByAssociationType(associationTypeName, domain);
	}

	/**
	 * The same as {@link #getAssociations(String, String)}, but the key are the object, and the list its subjects
	 * */
	public Map<String, List<String>> getAssociationsConsideringObjects (String associationTypeName, String domain) throws SQLException {
		return AssociationDAOSingleton.getInstance().findByAssociationTypeObject(associationTypeName, domain);
	}

	public Association insertAssociation(Association association) throws SQLException {
		if (!(AssociationDAOSingleton.getInstance().findEqual(association))) {
			return AssociationDAOSingleton.getInstance().insertAssociation(association);
		}
		return null;
	}

	public void deleteAssociation(Association association) throws SQLException {
		List<Link> linksToBeChecked = LinkDAOSingleton.getInstance().findAll();
		for (Link link : linksToBeChecked) {
			Asset assetObject = AssetDAOSingleton.getInstance().findById(link.getObject());
			Asset assetSubject = AssetDAOSingleton.getInstance().findById(link.getSubject());
			
			if (link.getAssociationName().equals(association.getAssociationTypeName())
					&& assetObject.getAssetTypeName().equals(association.getObjectType())
					&& assetSubject.getAssetTypeName().equals(association.getSubjectType())
					&& association.getDomain().equals(link.getDomain())) {
				LinkDAOSingleton.getInstance().delete(link);
			}
		}
		AssociationDAOSingleton.getInstance().delete(association);
	}

	
}
