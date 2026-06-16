package br.mackenzie.mackleaps.asset.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mackenzie.mackleaps.asset.persistence.dao.AssociationDAO;


public class AssociationDAOSingleton {
	
	private AssociationDAOSingleton() {
	}
	
	private static AssociationDAO associationDAO;
	private static final Logger LOGGER = LoggerFactory.getLogger(AssociationDAOSingleton.class);

	public static synchronized AssociationDAO getInstance() {
		if (associationDAO == null) {
			try {
				associationDAO = new AssociationDAO(ConnectionSingleton.getInstance());
			} catch (Exception e) {
				LOGGER.error("getInstance", e);
			}
		}
		return associationDAO;
	}
}
