package br.mackenzie.mackleaps.asset.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.mackenzie.mackleaps.asset.entity.AssetType;
import br.mackenzie.mackleaps.asset.entity.Association;
import br.mackenzie.mackleaps.asset.service.AssociationService;
import br.mackenzie.mackleaps.utils.LogFormatter;



@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/association")
@RestController
public class AssociationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AssociationController.class);
	private AssociationService service;

	public AssociationController(AssociationService service) {
		this.service = service;
	}
	
	@GetMapping(produces = {"application/json","application/xml"}, path = "/list")	
	public ResponseEntity<List<Association>> getAssociations(
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role,
			@RequestHeader(name = "domain", required = true) String domain) {
		LOGGER.debug("getAssociations");
		String method = "getAssociations";
		JSONObject parameters = new JSONObject();
		parameters.put("domain", domain);
		try {
			List<Association> associations = service.findByDomain(domain);
			if (associations.isEmpty()) {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatNoContentOperation(method, user, role,parameters));
				}
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			}
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, associations, user, role, parameters));
			}
			return ResponseEntity.ok(associations);
		} catch (SQLException e) {
			LOGGER.error("getSubjectAssociation failed:", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role,parameters));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping(produces = {"application/json","application/xml"}, path = "/listByTypeAndDomain/{associationType}")	
	public ResponseEntity<List<Association>> getAssociationsByAssociationType(
			@PathVariable (name="associationType", required = true) String associationType,
			@RequestHeader (name="domain", required = true) String domain,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role) {
		LOGGER.debug("getAssociationsByAssociationType");
		String method = "getAssociationsByAssociationType";
		JSONObject params = new JSONObject().put("associationType", associationType).put("domain", domain);
		try {
			List<Association> associations = service.findByAssociationTypeAndDomain(associationType,domain);
			if (associations.isEmpty()) {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatNoContentOperation(method, user, role, params));
				}
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			}
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, associations, user, role, params));
			}
			return ResponseEntity.ok(associations);
		} catch (SQLException e) {
			LOGGER.error("getByTypeAndDomain failed:", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, params));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping(produces = {"application/json","application/xml"}, path = "/list/{associationTypeName}")	
	public ResponseEntity<AssetType> getAssociations(
			@PathVariable (name="associationTypeName", required = true) String associationTypeName,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role,
			@RequestHeader (name="domain", required = true)  String domain,
			@RequestParam(defaultValue="object", value = "type") SubjectObjectType type) {
		LOGGER.debug("getAssociations");
		String method = "getAssociations";
		JSONObject parameters = new JSONObject();
		parameters.put("associationTypeName", associationTypeName);
		try {
			Map<String, List<String>> finalAssociations =
					type == SubjectObjectType.OBJECT ?
							service.getAssociations(associationTypeName, domain) :
							service.getAssociationsConsideringObjects(associationTypeName, domain);

			if (finalAssociations.isEmpty()) {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatNoContentOperation(method, user, role, parameters));
				}
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			}
			
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessful(method, finalAssociations, user, role, parameters));
			}
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		} catch (Exception e) {
			LOGGER.error("getAssociations failed:", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, parameters));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}


	@PostMapping (produces = {"application/json","application/xml"}, consumes = {"application/json","application/xml"})
	public ResponseEntity<Association> insertAssociation(
			@RequestBody Association association,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role) {
		LOGGER.debug("insertAssociation");
		String method = "insertAssociation";
		JSONObject parameters = new JSONObject();
		parameters.put("association", association);
		try {
			Association newAssociation = service.insertAssociation(association);
			if (newAssociation == null) {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, parameters));
				}
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, user, role, parameters));
			}
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			LOGGER.error("insertAssociation failed:", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, parameters));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping(produces = {"application/json","application/xml"}, path = "/{name}")
	public ResponseEntity<String> deleteAssociation(
			@PathVariable(value = "The association to be deleted", required = true) Association association,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role) {
		LOGGER.debug("deleteAssociation");
		String method = "deleteAssociation";
		JSONObject parameters = new JSONObject();
		parameters.put("association", association);
		try {
			service.deleteAssociation(association);
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, user, role, parameters));
			}
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Association deleted.");
		} catch (Exception e) {
			LOGGER.error("deleteAssociation failed:", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, parameters));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	public enum SubjectObjectType {
		OBJECT,
		SUBJECT
	}
}
