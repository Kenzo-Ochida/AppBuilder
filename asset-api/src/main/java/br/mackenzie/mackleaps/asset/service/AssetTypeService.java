package br.mackenzie.mackleaps.asset.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;


import br.mackenzie.mackleaps.asset.entity.Asset;
import br.mackenzie.mackleaps.asset.entity.AssetType;
import br.mackenzie.mackleaps.asset.entity.Association;
import br.mackenzie.mackleaps.asset.entity.Link;
import br.mackenzie.mackleaps.asset.persistence.AssetDAOSingleton;
import br.mackenzie.mackleaps.asset.persistence.AssetTypeDAOSingleton;
import br.mackenzie.mackleaps.asset.persistence.AssociationDAOSingleton;
import br.mackenzie.mackleaps.asset.persistence.LinkDAOSingleton;

@Service
public class AssetTypeService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetTypeService.class);

	public AssetType findByName(String name, String domain) throws SQLException {
		return AssetTypeDAOSingleton.getInstance().findByNameAndDomain(name,domain);
	}
	
	
	public void deleteByName(String name, String domain) throws SQLException {
		LOGGER.info("Deleting Asset type {} from domain {}", name, domain);
		List<Asset> assets = AssetDAOSingleton.getInstance().findByTypeAndDomain(name, domain);
		LOGGER.debug("Deleting links of assets that are related to the AssetType to be deleted");
		for(Asset asset : assets) {
			List<Link> linksSubject = LinkDAOSingleton.getInstance().findSubjectLinksByObjectId(asset.getId(), asset.getDomain());
			LOGGER.debug("Deleting {} links where the asset {} was the subject", linksSubject.size(), asset.getName());
			for(Link link : linksSubject) {
				LinkDAOSingleton.getInstance().delete(link);
			}
			List<Link> linksObject = LinkDAOSingleton.getInstance().findObjectLinksBySubjectId(asset.getId(), asset.getDomain());
			LOGGER.debug("Deleting {} links where the asset {} was the object", linksSubject.size() ,asset.getName());
			for(Link link : linksObject) {
				LinkDAOSingleton.getInstance().delete(link);
			}
			LOGGER.debug("deleting asset with the name {} and the domain {}", asset.getName() ,asset.getDomain());
			AssetDAOSingleton.getInstance().delete(asset);
		}
		LOGGER.debug("Deleting associations with the asset type {} as subject", name);
		List<Association> associationsSubject = AssociationDAOSingleton.getInstance().findAssociationsBySubjectType(name, domain);
		for(Association association : associationsSubject) {
			AssociationDAOSingleton.getInstance().delete(association);
		}
		
		LOGGER.debug("Deleting associations with the asset type {} as object ", name);
		List<Association> associationsObject = AssociationDAOSingleton.getInstance().findAssociationsByObjectType(name, domain);
		for(Association association : associationsObject) {
			AssociationDAOSingleton.getInstance().delete(association);
		}
		LOGGER.debug("Deleting the following asset type {}", name);
		AssetTypeDAOSingleton.getInstance().deleteByName(name, domain);
		LOGGER.info("finished deleting the assettype and its dependencies");
	}
	
	public AssetType updateAssetType(AssetType assetType, String domain) throws IOException, SQLException {
		String iconData = assetType.getIcon64();
		if (iconData != null && !iconData.isEmpty()) {
			byte[] imageByte = Base64.getDecoder().decode(iconData);
			String nameImg = assetType.getName();
			InputStream is = new ByteArrayInputStream(imageByte);

			String mimeType = URLConnection.guessContentTypeFromStream(is);
			String fileExtension = "svg";
			if (mimeType != null) {
				String[] tokens = mimeType.split("/");
				fileExtension = tokens[1];
			}

			File outputfile = new File("img/");
			if (!outputfile.exists()) {
				outputfile.mkdirs();
			}

			try(FileOutputStream imageOutFile = new FileOutputStream(
					"img/" + nameImg.replace(" ", "_") + "." + fileExtension)) {
				imageOutFile.write(imageByte);
				if (assetType.getIcon64().length() > 4096) {
					assetType.setIcon64("");
				}
				assetType.setIcon("/dashboard/" + nameImg.replace(" ", "_") + "." + fileExtension);
			}
		}
		/* TODO: check if provided domain parameter is equal to the AssetType domain attribute*/
		return AssetTypeDAOSingleton.getInstance().update(assetType);
	}
	
	public AssetType insertAssetType(AssetType assetType, String domain) throws IOException, SQLException {
		String iconData = assetType.getIcon64();
		if (iconData != null && !iconData.isEmpty()) {
			byte[] imageByte = Base64.getDecoder().decode(iconData);
			String nameImg = assetType.getName();
			InputStream is = new ByteArrayInputStream(imageByte);

			String mimeType = URLConnection.guessContentTypeFromStream(is);
			String fileExtension = "svg";
			if (mimeType != null) {
				String[] tokens = mimeType.split("/");
				fileExtension = tokens[1];
			}

			File outputfile = new File("img/");
			if (!outputfile.exists()) {
				outputfile.mkdirs();
			}

			try(FileOutputStream imageOutFile = new FileOutputStream(
					"img/" + nameImg.replace(" ", "_") + "." + fileExtension)) {
				imageOutFile.write(imageByte);
				if (assetType.getIcon64().length() > 4096) {
					assetType.setIcon64("");
				}
				assetType.setIcon("/dashboard/" + nameImg.replace(" ", "_") + "." + fileExtension);
			}
		}
		/*
		 * TODO: Check if provided domain is equal to the domain attribute of the assetType.
		 */
		return AssetTypeDAOSingleton.getInstance().insert(assetType);
	}
	
	public List<String> listAssetTypes() throws SQLException {
		return AssetTypeDAOSingleton.getInstance().listAssetTypes();
	}
	
	public List<String> listAssetTypesByDomain(String domain) throws SQLException {
		return AssetTypeDAOSingleton.getInstance().listAssetTypesByDomain(domain);
	}
}
