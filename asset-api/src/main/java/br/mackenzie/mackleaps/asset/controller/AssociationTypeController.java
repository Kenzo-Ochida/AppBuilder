package br.mackenzie.mackleaps.asset.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.mackenzie.mackleaps.asset.entity.AssociationType;
import br.mackenzie.mackleaps.asset.service.AssociationTypeService;
import br.mackenzie.mackleaps.utils.LogFormatter;
import jakarta.websocket.server.PathParam;

import org.json.JSONObject;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/associationType")
@RestController
public class AssociationTypeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AssociationTypeController.class);
	private AssociationTypeService service;
	
	public AssociationTypeController(AssociationTypeService service) {
		this.service = service;
	}

	@GetMapping(produces = {"application/json","application/xml"}, path = "/{name}")
	public ResponseEntity<AssociationType> getAssociationTypeById(
			@PathVariable(required = true) String name,
			@RequestHeader(name = "domain", required = true) String domain,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role) {
		LOGGER.debug("getAssociationTypeById");
		String method = "getAssociationTypeById";
		JSONObject parameters = new JSONObject();
		parameters.put("name", name);
		parameters.put("domain", domain);
		try {
			AssociationType associationType = service.findByName(name, domain);
			if (associationType == null) {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatNoContentOperation(method, user, role, parameters));
				}
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			}
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, user, role, parameters));
			}
			return ResponseEntity.ok(associationType);
		} catch (Exception e) {
			LOGGER.error("getAssociationTypeById failed:", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, new JSONObject()));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@DeleteMapping(produces = {"application/json","application/xml"}, path = "/{name}")
	public ResponseEntity<String> deleteAssociationTypeById(
			@PathVariable(required = true)@PathParam("name") String name,
			@RequestHeader(name = "domain", required = true) String domain,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role) {
		String method = "deleteAssociationTypeById";
		LOGGER.debug("deleteAssociationTypeById");
		JSONObject parameters = new JSONObject();
		parameters.put("name", name);
		parameters.put("domain", domain);
		try {
			
			service.deleteByName(name, domain);
			
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, user, role, parameters));
			}
			
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("The given AssociationType was deleted succesfully.");
		} catch (Exception e) {
			
			LOGGER.error("deleteAssociationTypeById failed:", e);
			
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, parameters));
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was a problem while processing the request.");
		}

	}

	@PutMapping (produces = {"application/json","application/xml"}, consumes = {"application/json","application/xml"})
	public ResponseEntity<AssociationType> updateAssociationType(
			@RequestBody AssociationType associationType, 
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role,
			@RequestHeader(name = "domain", required = true) String domain) {
		LOGGER.debug("updateAssociationType");
		String method = "updateAssociationType";
		JSONObject parameters = new JSONObject();
		parameters.put("domain", domain);
		
		try {
			AssociationType answer = service.updateAssociationType(associationType,domain);
			if(answer == null){
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, parameters));
				}
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			}
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, user, role, parameters)); 
			}
			return ResponseEntity.ok(answer);
		} catch (Exception e) {
			LOGGER.error("updateAssociationType failed:", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, parameters));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping (produces = {"application/json","application/xml"}, consumes = {"application/json","application/xml"})
	public ResponseEntity<AssociationType> insertAssociationType(
			@RequestBody AssociationType associationType,
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role,
			@RequestHeader(name = "domain", required = true) String domain) {
		String method = "insertAssociationType";
		LOGGER.debug("insertAssociationType");
		JSONObject parameters = new JSONObject();
		parameters.put("domain", domain);

		LOGGER.info("Body do associationType: " + associationType.toString());

		try {
			AssociationType answer =  service.insertAssociationType(associationType, user, role,domain);
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, user, role, parameters));
			}
			return ResponseEntity.ok(answer);
		} catch (Exception e) {
			LOGGER.error("insertAssociationType failed:", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, parameters));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping(produces = {"application/json","application/xml"}, path = "/list")
	public ResponseEntity<List<AssociationType>> listAssociationTypes(
			@RequestHeader(name = "user", required = true) String user,
			@RequestHeader(name = "role", required = true) String role,
			@RequestHeader(name = "domain", required = true) String domain) {
		String method = "listAssociationTypes";
		LOGGER.debug(method);
		JSONObject params = new JSONObject().put("domain", domain);
		try {
			List<AssociationType> types = service.findByDomain(domain);
			if (types.isEmpty()) {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("{}", LogFormatter.formatNoContentOperation(method, user, role, params));
				}
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			}
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("{}", LogFormatter.formatSuccessfulOperation(method, types, user, role,params));
			}
			return ResponseEntity.ok(types);
		} catch (Exception e) {
			LOGGER.error("listAssociationTypes failed:", e);
			LOGGER.info("{}", LogFormatter.formatFailedOperation(method, user, role, params));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	

}