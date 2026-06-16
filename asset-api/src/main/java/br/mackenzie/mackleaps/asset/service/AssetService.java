package br.mackenzie.mackleaps.asset.service;

import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import br.mackenzie.mackleaps.asset.entity.Asset;
import br.mackenzie.mackleaps.asset.entity.AssetType;

import br.mackenzie.mackleaps.asset.persistence.AssetDAOSingleton;
import br.mackenzie.mackleaps.asset.persistence.LinkDAOSingleton;
import br.mackenzie.mackleaps.utils.Codec;
import br.mackenzie.mackleaps.asset.persistence.AssetTypeDAOSingleton;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Service
public class AssetService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AssetService.class);
	private boolean encryptionIsActive;

	public AssetService(/*boolean encryptionIsActive*/) {
		LOGGER.debug("AssetController has been initialized and encyrptionIsActive: {} " , encryptionIsActive);
		this.encryptionIsActive = false;
	}

	public Asset findById(long id, String domain) throws SQLException {
		LOGGER.debug("Findign Asset with the ID {} that belong to the following given domain {} ", id, domain);
		Asset asset = AssetDAOSingleton.getInstance().findById(id);
		decryptAttributes(asset);
		if (domain == null || domain.equals("")) {
			LOGGER.debug("Domain of the asset was not informed");
			return asset;
		}
		if (asset.getDomain().equals(domain)) {
			return asset;
		}
		LOGGER.debug("Domain informed and domain of the asset found do not match");
		return null;
	}

	public List<Asset> getAssetsByTypeAndAttr(String assetType, String key, String value, String domain)
			throws SQLException, NoSuchFieldException, IllegalAccessException {
		List<Asset> assets = AssetDAOSingleton.getInstance().findByType(assetType);
		List<Asset> result = new ArrayList<>();
		for (Asset asset : assets) {
			decryptAttributes(asset);
			if (!asset.getDomain().equals(domain)) {
				continue;
			}
			if (asset.getAttributes().containsKey(key)) {
				if (asset.getAttributes().get(key).equals(value)) {
					result.add(asset);
				}
			} else {
				Field f = Asset.class.getDeclaredField(key);
				if (f.get(asset) != null && f.get(asset).equals(value)) {
					result.add(asset);
				}
			}
		}
		return result;
	}

	public List<Asset> getAssetsByTypeAndDomainAndAttr(String assetType, String key, String value, String domain)
			throws SQLException, NoSuchFieldException, IllegalAccessException {
		List<Asset> assets = AssetDAOSingleton.getInstance().findByTypeAndDomain(assetType, domain);
		List<Asset> result = new ArrayList<>();
		for (Asset asset : assets) {
			decryptAttributes(asset);
			if (asset.getAttributes().containsKey(key)) {
				if (asset.getAttributes().get(key).equals(value)) {
					result.add(asset);
				}
			} else {
				Field f = Asset.class.getDeclaredField(key);
				if (f.get(asset) != null && f.get(asset).equals(value)) {
					result.add(asset);
				}
			}
		}
		return result;
	}

	public List<Asset> findAll() throws SQLException {
		List<Asset> assets = AssetDAOSingleton.getInstance().findAll();
		for (Asset asset : assets) {
			decryptAttributes(asset);
		}
		return assets;
	}

	public List<Asset> listAssetsByTypeAndDomain(String assetType, String domain) throws SQLException {
		List<Asset> assets = AssetDAOSingleton.getInstance().findByTypeAndDomain(assetType, domain);
		if (assets == null || assets.isEmpty()) {
			return new ArrayList<>();
		}
		for (Asset asset : assets) {
			decryptAttributes(asset);
		}
		return assets;
	}

	public List<Asset> listAssetsByDomain(String domain) throws SQLException {
		List<Asset> assets = AssetDAOSingleton.getInstance().findByDomain(domain);
		if (assets == null || assets.isEmpty()) {
			return new ArrayList<>();
		}
		for (Asset asset : assets) {
			decryptAttributes(asset);
		}
		return assets;
	}

	public Asset deleteAssetById(long id, String user, String domain) throws SQLException {
		Asset asset = AssetDAOSingleton.getInstance().findById(id);
		asset.setUpdateUser(user);
		LinkDAOSingleton.getInstance().deleteAllAssociationsInvolvingAsset(id, domain);
		AssetDAOSingleton.getInstance().deleteById(id);
		return asset;
	}

	public Asset updateAsset(Asset asset, String user, String domain) throws IOException, SQLException {
		if (asset.getDomain() == null) {
			asset.setDomain(domain);
		}
		if (asset.getIcon64() != null && !asset.getIcon64().isEmpty()) {
			byte[] imageByte = Base64.getDecoder().decode(asset.getIcon64());

			InputStream is = new ByteArrayInputStream(imageByte);

			String mimeType = URLConnection.guessContentTypeFromStream(is);
			String fileExtension = "svg";
			if (mimeType != null) {
				String[] tokens = mimeType.split("/");
				fileExtension = tokens[1];
			}
			String nameImg = asset.getName();

			File outputfile = new File("img/");
			if (!outputfile.exists()) {
				outputfile.mkdirs();
			}

			try (FileOutputStream imageOutFile = new FileOutputStream(
					"img/" + nameImg.replace(" ", "_") + "." + fileExtension)) {
				imageOutFile.write(imageByte);
				if (asset.getIcon64().length() > 4096) {
					asset.setIcon64("");
				}
				asset.setIcon("/dashboard/" + nameImg.replace(" ", "_") + "." + fileExtension);
			}

		} else {
			AssetType at = AssetTypeDAOSingleton.getInstance().findByNameAndDomain(asset.getAssetTypeName(),
					asset.getAssetTypeDomain());
			if (at.getIcon() != null && !at.getIcon().isEmpty()) {
				asset.setIcon(at.getIcon());
				asset.setIcon64(at.getIcon64());
			}
		}
		if (asset.getStatus() == null) {
			Asset currentAsset = AssetDAOSingleton.getInstance().findById(asset.getId());
			asset.setStatus(currentAsset.getStatus());
		}
		asset.setUpdateUser(user);
		encryptAttributes(asset);
		return AssetDAOSingleton.getInstance().update(asset);
	}

	public Asset updateAssetStatus(String params, String domain) throws SQLException {
		JSONObject json = new JSONObject(params);
		return AssetDAOSingleton.getInstance().updateStatus(json.getLong("assetId"), json.getString("status"), domain);
	}

	public Asset insertAsset(Asset asset) throws IOException, SQLException {
		encryptAttributes(asset);
		if (asset.getIcon64() != null && !asset.getIcon64().isEmpty()) {
			byte[] imageByte = Base64.getDecoder().decode(asset.getIcon64());

			InputStream is = new ByteArrayInputStream(imageByte);

			String mimeType = URLConnection.guessContentTypeFromStream(is);
			String fileExtension = "svg";
			if (mimeType != null) {
				String[] tokens = mimeType.split("/");
				fileExtension = tokens[1];
			}

			String nameImg = asset.getName();

			File outputfile = new File("img/");
			if (!outputfile.exists()) {
				outputfile.mkdirs();
			}

			try (FileOutputStream imageOutFile = new FileOutputStream(
					"img/" + nameImg.replace(" ", "_") + "." + fileExtension)) {
				imageOutFile.write(imageByte);
				if (asset.getIcon64().length() > 4096) {
					asset.setIcon64("");
				}
				asset.setIcon("/dashboard/" + nameImg.replace(" ", "_") + "." + fileExtension);
			}
		} else {
			AssetType at = AssetTypeDAOSingleton.getInstance().findByNameAndDomain(asset.getAssetTypeName(),
					asset.getAssetTypeDomain());
			LOGGER.info(at + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + at.toString());
			if (at.getIcon() != null && !at.getIcon().isEmpty()) {
				asset.setIcon(at.getIcon());
				asset.setIcon64(at.getIcon64());
			}
		}

		return AssetDAOSingleton.getInstance().insert(asset);
	}

	public List<Asset> getAssetsByType(String assetType) throws SQLException {
		AssetType aType = AssetTypeDAOSingleton.getInstance().findByName(assetType);
		if (aType != null) {
			return AssetDAOSingleton.getInstance().findByType(assetType);
		}
		return new ArrayList<>();
	}

	public List<Asset> getAssetsWithAttribute(String key, String value, String domain) throws SQLException {
		return AssetDAOSingleton.getInstance().findWithAttribute(key, encryptAttribute(value), domain);
	}

	private void encryptAttributes(Asset asset) {
		if (isEncryptionIsActive()) {
			LOGGER.debug("Encrypting attributes of asset");
			for (String key : asset.getAttributes().keySet()) {
				String encrypted = Codec.encrypt(asset.getAttributes().get(key));
				asset.getAttributes().put(key, encrypted);
			}
		}
	}

	private void decryptAttributes(Asset asset) {
		if (isEncryptionIsActive()) {
			LOGGER.debug("Decrypting attributes of asset");
			for (String key : asset.getAttributes().keySet()) {
				String decrypted = Codec.decrypt(asset.getAttributes().get(key));
				asset.getAttributes().put(key, decrypted);
			}
		}
	}

	private String encryptAttribute(String attribute) {
		if (isEncryptionIsActive()) {
			return Codec.encrypt(attribute);
		}
		return attribute;
	}

	public boolean isEncryptionIsActive() {
		return encryptionIsActive;
	}

	public void setEncryptionIsActive(boolean encryptionIsActive) {
		this.encryptionIsActive = encryptionIsActive;
	}
}
