package br.mackenzie.mackleaps.utils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {
	
	private Util() {
	}
	private static ObjectMapper mapper = new ObjectMapper();
	private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);
	private static String bin;
	public static Map<String, String> toMap(String jsonString) {
		Map<String, String> map = new HashMap<>();
		try {
			map = mapper.readValue(jsonString, new TypeReference<Map<String, String>>() {
			});
		} catch (IOException | NullPointerException e) {
			LOGGER.error("toMap", e);
		}
		return map;
	}

	public static String mapToString(Map<String, String> map) {
		try {
			return mapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			LOGGER.error("mapToString", e);
		}
		return null;
	}

	public static String toJsonSyntax(String method, String operationStatus, String asset, String user, String role,
			String params) {
		JSONObject aux = new JSONObject();
		aux.put("method", method);
		aux.put("operationStatus", operationStatus);
		aux.put("asset", asset);
		aux.put("user", user);
		aux.put("role", role);
		aux.put("params", params);

		return aux.toString();
	}
	
	public static String getBin() {
		return bin;
	}
	
	public static void setBin(String value) {
		bin = value;
	}
}