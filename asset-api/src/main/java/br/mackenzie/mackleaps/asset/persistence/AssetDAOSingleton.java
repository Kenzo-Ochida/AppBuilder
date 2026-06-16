package br.mackenzie.mackleaps.asset.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mackenzie.mackleaps.asset.persistence.dao.AssetDAO;

public class AssetDAOSingleton {

	private AssetDAOSingleton() {
	}
	private static AssetDAO assetDAO;
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetDAOSingleton.class);

	public static synchronized AssetDAO getInstance() {
		if (assetDAO == null) {
			try {
				assetDAO = new AssetDAO(ConnectionSingleton.getInstance());
			}  catch (Exception e) {
				LOGGER.error("getInstance", e);
			}
		}
		return assetDAO;
	}
}
