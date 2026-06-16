package br.mackenzie.mackleaps.asset.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.mackenzie.mackleaps.asset.entity.Asset;
import br.mackenzie.mackleaps.asset.entity.AssetType;
import br.mackenzie.mackleaps.asset.entity.Link;
import br.mackenzie.mackleaps.asset.service.LinkService;
import br.mackenzie.mackleaps.utils.LogFormatter;
import jakarta.websocket.server.PathParam;

import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/link")
@RestController
public class LinkController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LinkController.class);
	private LinkService service;

	private static final String GETSUBJECTASSOCIATION = "getSubjectAssociation";
	
	public LinkController(LinkService service) {
		this.service = service;
	}

	@GetMapping(produces = {"application/json","application/xml"}, path = "/list/{associationTypeName}/{subjectType}/{objectType}/{domain}")	
	public ResponseEntity<Map<Asset, List<Asset>>> getLinks(
			@PathVariable (name="associationTypeName", required = true) String associationTypeName,
			@PathVariable (name="subjectType", required = true) String subjectType,
			@PathVariable (name="objectType", required = true) String objectType,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role,
			@PathVariable (name="domain", required = true) String domain) {
		String method = "getAssociations";
		LOGGER.debug(method);
		JSONObject parameters = new JSONObject().put("associationTypeName", associationTypeName)
				.put("subjectType", subjectType).put("objectType", objectType);
		try {
			Map<Asset, List<Asset>> finalAssociations = service.getAssociations(associationTypeName, subjectType,
					objectType, domain);
			if (finalAssociations.isEmpty()) {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatNoContentOperation(method, user, role, parameters));
				}
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			}
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, user, role, parameters));
			}
			return ResponseEntity.ok(finalAssociations);
		} catch (Exception e) {
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, parameters));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping (produces = {"application/json","application/xml"}, consumes = {"application/json","application/xml"})
	public ResponseEntity<String> insertLink(
			@RequestBody Link link,
			@RequestHeader(name = "domain", required = true) String domain,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role) {
		String method = "insertLink";
		JSONObject parameters = new JSONObject().put("link", link);
		try {
			Link newLink = service.insertLink(link);
			if (newLink == null) {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, parameters));
				}
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, user, role, parameters));
			}
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Link created.");
		} catch (Exception e) {
			LOGGER.error("insertLink failed", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, parameters));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	public ResponseEntity<List<Asset>> getObject(
	@PathVariable(value = "The subject's id", required = true) @PathParam("subjectId") Long subjectId,
			@PathVariable(value = "The association name between the given subject and the Assets", required = true) @PathParam("associationName") String associationName,
			@PathVariable(value = "The AssetType of the Assets acting as objects are supposed to be", required = true) @PathParam("assetTypeName") String assetTypeName,
			@PathVariable(value = "The AssetTypeDomain of the Assets acting as objects are supposed to be", required = true) @PathParam("assetTypeDomain") String assetTypeDomain,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role,
			@PathVariable (name="domain", required = true)  String domain) {
		LOGGER.debug("getObject");
		String method = "getObject";
		try {
			List<Asset> array = service.getObject(subjectId, associationName, assetTypeName, assetTypeDomain, domain);
			if (array.isEmpty()) {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, array, user, role, new JSONObject()));
				}
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			}
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, user, role, new JSONObject()));
			}
			return ResponseEntity.ok(array);
		} catch (Exception e) {
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, new JSONObject()));
			LOGGER.error("getObject failed:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	public ResponseEntity<List<Asset>> getSubject(
			@PathVariable(value = "The object's id", required = true) @PathParam("objectId") Long objectId,
			@PathVariable(value = "The association's name", required = true) @PathParam("associationName") String associationName,
			@PathVariable(value = "The target AssetType for the subject") @PathParam("assetTypeName") String assetTypeName,
			@PathVariable(value = "The target AssetTypeDomain for the subject") @PathParam("assetTypeDomain") String assetTypeDomain,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role,
			@PathVariable (name="domain", required = true)  String domain) {
		LOGGER.debug("getSubject");
		String method = "getSubject";
		try {
			List<Asset> assetsAssetType = service.getSubject(objectId, associationName, assetTypeName, assetTypeDomain, domain);
			if (assetsAssetType.isEmpty()) {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, assetsAssetType, user, role, new JSONObject()));
				}
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			}
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, user, role, new JSONObject()));
			}
			return ResponseEntity.ok(assetsAssetType);
		} catch (Exception e) {
			LOGGER.error("getSubject failed:", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, new JSONObject()));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	public ResponseEntity<AssetType> getAssociation(
			@PathVariable(value = "The subject's id", required = true) @PathParam("subjectId") Long subjectId,
			@PathVariable(value = "The AssociationType's name", required = true) @PathParam("associationTypeName") String associationTypeName,
			@PathVariable(value = "The object's id", required = true) @PathParam("objectId") Long objectId,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role,
			@PathVariable (name="domain", required = true)  String domain){
		LOGGER.debug("getAssociation");
		String method = "getAssociation";
		try {
			JSONObject answer = service.getAssociation(objectId, subjectId, associationTypeName);
			if (answer.length() == 0) {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatNoContentOperation(method, user, role, new JSONObject()));
				}
				return ResponseEntity.noContent().build();
			}
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, user, role, new JSONObject()));
			}
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			LOGGER.error("getAssociation failed:", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, new JSONObject()));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping(produces = {"application/json","application/xml"}, path = "/object/{subjectId}/{associationTypeName}/{domain}")	
	public ResponseEntity<List<Asset>> getObjectAssociation(
			@PathVariable (name="subjectId", required = true) Long subjectId,
			@PathVariable (name="associationTypeName", required = true) String associationTypeName,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role,
			@PathVariable (name="domain", required = true)  String domain) {
		String method = "getObjectAssociation";
		LOGGER.debug(method);
		try {
			List<Asset> assets = service.getObjectAssociation(subjectId, associationTypeName, domain);
			if (assets != null && assets.isEmpty()) {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatNoContentOperation(method, user, role, new JSONObject()));
				}
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			}
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, user, role, new JSONObject()));
			}
			return ResponseEntity.ok(assets);
		} catch (Exception e) {
			LOGGER.error("getObjectAssociation failed:", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, new JSONObject()));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping(produces = {"application/json","application/xml"}, path = "/objects/{associationTypeName}/{assetType}/{assetTypeDomain}/{domain}")	
	public ResponseEntity<List<Asset>> getObjectsAssociation(
			@PathVariable (name="associationTypeName", required = true) String associationTypeName,
			@PathVariable (name="assetType", required = true) String assetType,
			@PathVariable (name="assetTypeDomain", required = true) String assetTypeDomain,
			@RequestParam(name="subjects", required = true) String subjects,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role,
			@PathVariable (name="domain", required = true)  String domain) {
		LOGGER.debug("getObjectsAssociation");
		String method = "getObjectsAssociation";
		List<Asset> assets = null;
		try {
			assets = service.getObjectsFromSubjects(subjects, associationTypeName, assetType, assetTypeDomain, domain);
			if(assets.isEmpty()) {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatNoContentOperation(method, user, role, new JSONObject()));
				}
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			}
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, assets, user, role, new JSONObject()));
			}
			return ResponseEntity.ok(assets);
		} catch (Exception e) {
			LOGGER.error("getObjectsAssociation failed:", e);
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, new JSONObject()));
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	public ResponseEntity<List<Asset>> getSubjectAssociation(
			@PathVariable(value = "The object's id", required = true) @PathParam("objectId") Long objectId,
			@PathVariable(value = "The AssociationType's name", required = true) @PathParam("associationTypeName") String associationTypeName,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role,
			@PathVariable (name="domain", required = true)  String domain) {
		LOGGER.debug(GETSUBJECTASSOCIATION);
		String method = GETSUBJECTASSOCIATION;
		try {
			List<Asset> assets = service.getSubjectAssociation(objectId, associationTypeName, domain);
			if (assets.isEmpty()) {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatNoContentOperation(method, user, role, new JSONObject()));
				}
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			}
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, user, role, new JSONObject()));
			}
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			LOGGER.error("getSubjectAssociation failed:", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, new JSONObject()));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	public ResponseEntity<Map<String, List<Asset>>> getObjectAssociation(
			@PathVariable(value = "The subject's id", required = true) @PathParam("subjectId") Long subjectId,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role,
			@PathVariable (name="domain", required = true)  String domain) {
		String method = "getObjectAssociation";
		LOGGER.debug(method);
		try {
			Map<String, List<Asset>> associations = service.getObjectAssociation(subjectId, domain);
			if (associations.isEmpty()) {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatNoContentOperation(method, user, role, new JSONObject()));
				}
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			}
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, user, role, new JSONObject()));
			}
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			LOGGER.error("getObjectAssociation failed:", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, new JSONObject()));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	public ResponseEntity<Map<String, List<Asset>>> getSubjectAssociation(
			@PathVariable(value = "The object's id", required = true) @PathParam("objectId") Long objectId,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role,
			@PathVariable (name="domain", required = true)  String domain) {
		LOGGER.debug(GETSUBJECTASSOCIATION);
		String method = GETSUBJECTASSOCIATION;
		try {
			Map<String, List<Asset>> associations = service.getSubjectAssociation(objectId, domain);
			if (associations.isEmpty()) {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatNoContentOperation(method, user, role, new JSONObject()));
				}
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			}
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, user, role, new JSONObject()));
			}
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			LOGGER.error("getSubjectAssociation failed:", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, new JSONObject()));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	

	public ResponseEntity<List<Link>> getAssociations(
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role) {
		String method = "getAssociations";
		LOGGER.debug(method);
		try {
			List<Link> associations = service.findAll();
			if (associations.isEmpty()) {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatNoContentOperation(method, user, role, new JSONObject()));
				}
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			}
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, user, role, new JSONObject()));
			}
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			LOGGER.error("getAssociations failed:", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, new JSONObject()));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	public ResponseEntity<AssetType> getAssociationsWithNames(
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role,
			@PathVariable (name="domain", required = true)  String domain) {
		LOGGER.debug("getAssociationsWithName");
		String method = "getAssociationsWithName";
		try {
			JSONArray associations = service.findAllWithName(domain);
			if (associations.isNull(0)) {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatNoContentOperation(method, user, role, new JSONObject()));
				}
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			}
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, user, role, new JSONObject()));
			}
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			LOGGER.error("getAssociationsWithNames failed:", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, new JSONObject()));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	

	public ResponseEntity<List<Link>> getAssociationsByDomain(
			@PathVariable (name="domain", required = true)  String domain,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role) {
		String method = "getAssociationsByDomain";
		LOGGER.debug(method);
		JSONObject params = new JSONObject().put("domain", domain);
		try {
			List<Link> associations = service.findByDomain(domain);
			if (associations == null ||associations.isEmpty()) {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatNoContentOperation(method, user, role, params));
				}
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			}
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, user, role, params));
			}
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			LOGGER.error("getAssociationsByDomain failed:", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, params));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	

	public ResponseEntity<AssetType> deleteAssociation(
			@PathVariable(value = "The association to be deleted", required = true) Link link,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role) {
		LOGGER.debug("deleteAssociation");
		String method = "deleteAssociation";
		try {
			boolean answer = service.deleteAssociation(link.getSubject(), link.getObject(), link.getAssociationName(), link.getAssociationDomain(), link.getDomain());
			if (answer) {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, user, role, new JSONObject()));
				}
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			}
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, new JSONObject()));
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		} catch (Exception e) {
			LOGGER.error("deleteAssociation failed:", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, new JSONObject()));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}