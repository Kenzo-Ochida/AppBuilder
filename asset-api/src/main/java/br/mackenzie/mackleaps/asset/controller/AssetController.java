package br.mackenzie.mackleaps.asset.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.mackenzie.mackleaps.asset.entity.Asset;
import br.mackenzie.mackleaps.asset.service.AssetService;
import br.mackenzie.mackleaps.utils.LogFormatter;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/asset")
@RestController
public class AssetController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetController.class);
	private final AssetService service;
	private static final String DOMAINKEY = "domain";
	private static final String ASSETTYPEKEY = "assetType";
	
	public AssetController (AssetService service) {
		this.service = service;
	}
    

	@GetMapping(produces = {"application/json","application/xml"}, path = "/{id}")	
	public ResponseEntity<Asset> getAssetById(
			@PathVariable (name="id", required = true) Long id,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role,
			@RequestHeader(name = "domain", required = true) String domain) {
		LOGGER.debug("getAssetById");
		String method = "getAssetById";
		JSONObject parameters = new JSONObject();
		parameters.put("assetId", id);
		parameters.put("domain", domain);
		try {
			Asset asset = service.findById(id, domain == null ? "" : domain);
			if (asset == null) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatNoContentOperation(method, user, role, parameters));
				}
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			}
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, asset, user, role, parameters));
			}

			return ResponseEntity.ok(asset);
		} catch (Exception e) {
			LOGGER.error("getAssetById failed:", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, parameters));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}


	@GetMapping(produces = {"application/json","application/xml"}, path = "/{assetType}/{key}/{value}")
	public ResponseEntity<List<Asset>> getAssetsByTypeAndAttr(
			@PathVariable (name="assetType", required = true)  String assetType,
			@PathVariable (name="key", required = true)  String key,
			@PathVariable (name="value", required = true)  String value,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role,
			@RequestHeader (name="domain", required = true)  String domain) {
		String method = "getAssetsByTypeAndAttr";
		LOGGER.debug(method);
		JSONObject parameters = new JSONObject();
		parameters.put(ASSETTYPEKEY, assetType);
		parameters.put("key", key);
		parameters.put("value", value);
		parameters.put(DOMAINKEY, domain);
		try {
			List<Asset> assets = service.getAssetsByTypeAndDomainAndAttr(assetType, key, value, domain);
			if (assets.isEmpty()) {
				if (LOGGER.isInfoEnabled()) {

					LOGGER.info("{}", LogFormatter.formatNoContentOperation(method, user, role, parameters));
				}
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			}

			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, assets, user, role, parameters));
			}

			return ResponseEntity.ok(assets);
		} catch (Exception e) {
			LOGGER.error("getAssetsByTypeAndAttr failed:", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, parameters));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	@GetMapping(produces = {"application/json","application/xml"}, path = "listByType/{assetType}")
	public ResponseEntity<List<Asset>> getAssetsByType(
			@PathVariable (name="assetType", required = true)  String assetType,
			@RequestHeader (name="domain", required = true)  String domain,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role) {
		LOGGER.debug("getAssetsByType");
		String method = "getAssetsByType";
		JSONObject parameters = new JSONObject();
		parameters.put(ASSETTYPEKEY, assetType);
		parameters.put(DOMAINKEY, domain);
		try {
			List<Asset> assets = service.listAssetsByTypeAndDomain(assetType, domain);
			if (assets.isEmpty()) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatNoContentOperation(method, user, role, parameters));
				}
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			}

			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatNoContentOperation(method, user, role, parameters));
			}

			return ResponseEntity.ok(assets);
		} catch (Exception e) {
			LOGGER.error("getAssetByType failed:", e);
			LOGGER.info("{}", LogFormatter.formatNoContentOperation(method, user, role, parameters));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	

	@GetMapping(produces = {"application/json","application/xml"}, path = "list")
	public ResponseEntity<List<Asset>> getAssets(
			@RequestHeader (name="domain", required = true)  String domain,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role) {
		LOGGER.debug("getAssets");
		String method = "getAssets";
		JSONObject parameters = new JSONObject();
		parameters.put(DOMAINKEY, domain);
		try {
			List<Asset> assets = service.listAssetsByDomain( domain);
			if (assets.isEmpty()) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatNoContentOperation(method, user, role, parameters));
				}
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			}

			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatNoContentOperation(method, user, role, parameters));
			}

			return ResponseEntity.ok(assets);
		} catch (Exception e) {
			LOGGER.error("getAssets failed:", e);
			LOGGER.info("{}", LogFormatter.formatNoContentOperation(method, user, role, parameters));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}


	@DeleteMapping( path = "/{id}")
	public ResponseEntity<String> deleteAssetById(
			@PathVariable (name="id", required = true) Long id,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role,
			@RequestHeader(name = "domain", required = true) String domain) {
		LOGGER.debug("deleteAssetById");
		String method = "deleteAssetById";
		JSONObject parameters = new JSONObject();
		parameters.put("assetId", id);
		try {
			Asset deletedAsset = service.deleteAssetById(id, user, domain == null ? "" : domain);
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, deletedAsset, user, role, parameters));
			}
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Asset deleted.");
		} catch (Exception e) {
			LOGGER.error("deleteAssetById failed:", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, parameters));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}


	@PutMapping (produces = {"application/json","application/xml"}, consumes = {"application/json","application/xml"})
	public ResponseEntity<Asset> updateAsset(
			@RequestBody Asset asset,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role,
			@RequestHeader(name = "domain", required = true) String domain) {
		LOGGER.debug("updateAsset");
		String method = "updateAsset";
		JSONObject parameters = new JSONObject();
		parameters.put("asset", asset);
		try {
			Asset update = service.updateAsset(asset, user, role);
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, update, user, role, parameters));
			}
			return ResponseEntity.ok(update);
		} catch (Exception e) {
			LOGGER.error("updateAsset failed:", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, parameters));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}


	@PostMapping (produces = {"application/json","application/xml"}, consumes = {"application/json","application/xml"})
	public ResponseEntity<Asset> insert(
			@RequestBody Asset asset,
			@RequestHeader(name = "domain", required = true) String domain,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role) {
		LOGGER.debug("insert");
		String method = "insertAsset";
		JSONObject parameters = new JSONObject();
		parameters.put("asset", asset);
		try {
			LOGGER.debug("{}", asset);
			Asset answer = service.insertAsset(asset);
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, answer, user, role, parameters));
			}
			return ResponseEntity.ok(answer);
		} catch (Exception e) {
			LOGGER.error("insert asset failed:", e);
			LOGGER.error("{}", LogFormatter.formatFailedOperation(method, user, role, parameters));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}


	@GetMapping(produces = {"application/json","application/xml"}, path = "/list/{key}/{value}")
	public ResponseEntity<List<Asset>> getAssetsWithAttribute(
			@PathVariable (name="key", required = true)  String key,
			@PathVariable (name="value", required = true)  String value,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role,
			@RequestHeader (name="domain", required = true)  String domain) {
		LOGGER.debug("getAssetsWithAttribute");
		String method = "getAssetsWithAttribute";
		JSONObject parameters = new JSONObject();
		parameters.put("key", key);
		parameters.put("value", value);
		try {
			List<Asset> assets = service.getAssetsWithAttribute(key, value, domain);
			if (assets.isEmpty()) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, assets, user, role, parameters));
				}
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			}
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(LogFormatter.formatSuccessfulOperation(method, assets, user, role, parameters));
			}
			return ResponseEntity.ok(assets);
		} catch (Exception e) {
			LOGGER.error("getAssetsWithAttribute failed:", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, parameters));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
