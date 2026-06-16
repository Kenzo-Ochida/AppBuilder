package br.mackenzie.mackleaps.asset.controller;


import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import br.mackenzie.mackleaps.asset.entity.AssetType;
import br.mackenzie.mackleaps.asset.service.AssetTypeService;
import br.mackenzie.mackleaps.utils.LogFormatter;


@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/assetType")
@RestController
public class AssetTypeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AssetTypeController.class);
	private AssetTypeService service;

	public AssetTypeController(AssetTypeService service) {
		this.service = service;
	}

	@GetMapping(produces = {"application/json","application/xml"}, path = "/{name}")
	public ResponseEntity <AssetType> getAssetTypeById(
			 @PathVariable(required = true) String name,
			 @RequestHeader(name = "user", required = true) String user,
			 @RequestHeader(name = "role", required = true) String role,
			 @RequestHeader(name = "domain", required = true) String domain) {
		LOGGER.debug("getAssetTypeById");
		String method = "getAssetTypeById";
		JSONObject parameters = new JSONObject();
		parameters.put("name", name);
		parameters.put("domain", domain);
		try {
			AssetType assetType= service.findByName(name,domain);
			if (assetType == null) {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatNoContentOperation(method, user, role, parameters));
				}
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			}
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, assetType, user, role, parameters));
			}
			return ResponseEntity.ok(assetType);
		} catch (Exception e) {
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, parameters));
			}
			LOGGER.error("getAssetTypeById failed: {}}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}


	@DeleteMapping(produces = {"application/json","application/xml"}, path = "/{name}")
	public ResponseEntity<String> deleteAssetTypeByName(
			@PathVariable(name = "name", required = true) String name,
			@RequestHeader (name="domain", required = true) String domain,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role) {
		String method = "deleteAssetTypeByName";
		LOGGER.debug("deleteAssetTypeByName");
		JSONObject parameters = new JSONObject();
		parameters.put("name", name);
		parameters.put("domain", domain);
		try {
			service.deleteByName(name, domain);
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, user, role, parameters));
			}
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("The given AssetType was deleted succesfully.");
		} catch (Exception e) {
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, parameters));
			LOGGER.error("deleteAssetTypeByName failed: {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was a problem while processing the request.");
		}
	}


	@PutMapping (produces = {"application/json","application/xml"}, consumes = {"application/json","application/xml"})
	public ResponseEntity<AssetType> updateAssetType(
			@RequestBody AssetType assetType,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role,
			@RequestHeader(name = "domain", required = true) String domain) {
		String method = "updateAssetType";
		LOGGER.debug("updateAssetType");
		JSONObject parameters = new JSONObject();
		parameters.put("assetType", assetType);
		parameters.put("domain", domain);
		try {
			AssetType answer = service.updateAssetType(assetType,domain);
			if(answer == null){
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, parameters));
				}
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			}
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, answer, user, role, parameters));
			}
			return ResponseEntity.ok(answer);
		} catch (Exception e) {
			LOGGER.error("updateAssetType failed:", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, parameters));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping (produces = {"application/json","application/xml"}, consumes = {"application/json","application/xml"})
	public ResponseEntity<AssetType> insertAssetType(
			@RequestBody AssetType assetType,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role,
			@RequestHeader(name = "domain", required = true) String domain) {
		String method = "insertAssetType";
		LOGGER.debug("insertAssetType");
		JSONObject parameters = new JSONObject();
		try {
			AssetType finalAssetType = service.insertAssetType(assetType,domain);
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, finalAssetType, user, role, parameters));
			}
			return ResponseEntity.ok(finalAssetType);
		} catch (Exception e) {
			LOGGER.error("insertAssetType failed:", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, parameters));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping(produces = {"application/json","application/xml"}, path = "/list")
	public ResponseEntity<List<String>> listAssetTypes(
			@RequestHeader (name="domain", required = true) String domain,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role) {
		LOGGER.debug("listAssetTypes");
		String method = "listAssetTypes";
		JSONObject parameters = new JSONObject();
		parameters.put("domain", domain);
		try {
			List<String> types = service.listAssetTypesByDomain(domain);
			if (types.isEmpty()) {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatNoContentOperation(method, user, role, parameters));
				}
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			}
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, types, user, role, parameters));
			}
			return ResponseEntity.ok(types);
		} catch (Exception e) {
			LOGGER.error("listAssetTypes failed:", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, parameters));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
