package br.mackenzie.mackleaps.asset.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mackenzie.mackleaps.asset.persistence.dao.AssociationTypeDAO;

public class AssociationTypeDAOSingleton {

	private AssociationTypeDAOSingleton() {
	}
	private static AssociationTypeDAO associationTypeDAO;
	private static final Logger LOGGER = LoggerFactory.getLogger(AssociationTypeDAOSingleton.class);

	public static synchronized AssociationTypeDAO getInstance() {
		if (associationTypeDAO == null) {
			try {
				associationTypeDAO = new AssociationTypeDAO(ConnectionSingleton.getInstance());
			} catch (Exception e) {
				LOGGER.error("getInstance", e);
			}
		}
		return associationTypeDAO;
	}
}
