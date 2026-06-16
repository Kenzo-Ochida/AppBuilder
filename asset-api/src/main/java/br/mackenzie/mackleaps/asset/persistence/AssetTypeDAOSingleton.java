package br.mackenzie.mackleaps.asset.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mackenzie.mackleaps.asset.persistence.dao.AssetTypeDAO;

public class AssetTypeDAOSingleton {

	private AssetTypeDAOSingleton() {
	}
	private static AssetTypeDAO assetTypeDAO;
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetTypeDAOSingleton.class);

	public static synchronized AssetTypeDAO getInstance() {
		if (assetTypeDAO == null) {
			try {
				assetTypeDAO = new AssetTypeDAO(ConnectionSingleton.getInstance());
			} catch (Exception e) {
				LOGGER.error("getInstance", e);
			}
		}
		return assetTypeDAO;
	}
}
