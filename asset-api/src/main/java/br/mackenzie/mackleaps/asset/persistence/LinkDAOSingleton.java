package br.mackenzie.mackleaps.asset.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mackenzie.mackleaps.asset.persistence.dao.LinkDAO;

public class LinkDAOSingleton {

	private LinkDAOSingleton() {
	}
	private static LinkDAO linkDAO;
	private static final Logger LOGGER = LoggerFactory.getLogger(LinkDAOSingleton.class);

	public static synchronized LinkDAO getInstance() {
		if (linkDAO == null) {
			try {
				linkDAO = new LinkDAO(ConnectionSingleton.getInstance());
			} catch (Exception e) {
				LOGGER.error("getInstance", e);
			}
		}
		return linkDAO;
	}
}