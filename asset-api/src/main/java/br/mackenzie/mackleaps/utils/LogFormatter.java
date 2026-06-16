package br.mackenzie.mackleaps.utils;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import br.mackenzie.mackleaps.asset.entity.Asset;
import br.mackenzie.mackleaps.asset.entity.AssetType;
import br.mackenzie.mackleaps.asset.service.OperationStatus;

public final class LogFormatter {

	private LogFormatter() {
	}

	private static final String METHOD_KEY = "method";
	private static final String OP_STATUS_KEY = "operationStatus";
	private static final String USER_KEY = "user";
	private static final String ROLE_KEY = "role";
	private static final String PARAMS_KEY = "params";
	private static final String SIZE_KEY = "size";
	private static final String ASSET_ID_KEY = "assetId";

	public static String formatNoContentOperation(String method, String user, String role, JSONObject params) {

		JSONObject aux = new JSONObject();
		aux.put(METHOD_KEY, method);
		aux.put(OP_STATUS_KEY, OperationStatus.NOT_FOUND);
		aux.put(USER_KEY, user);
		aux.put(ROLE_KEY, role);
		aux.put(PARAMS_KEY, params);

		return aux.toString();
	}
	
	public static String formatNoContentOperation(String method, String user, String role) {

		JSONObject aux = new JSONObject();
		aux.put(METHOD_KEY, method);
		aux.put(OP_STATUS_KEY, OperationStatus.NOT_FOUND);
		aux.put(USER_KEY, user);
		aux.put(ROLE_KEY, role);
		aux.put(PARAMS_KEY, new JSONObject());

		return aux.toString();
	}

	public static String formatFailedOperation(String method, String user, String role, JSONObject params) {

		JSONObject aux = new JSONObject();
		aux.put(METHOD_KEY, method);
		aux.put(OP_STATUS_KEY, OperationStatus.FAILED);
		aux.put(USER_KEY, user);
		aux.put(ROLE_KEY, role);
		aux.put(PARAMS_KEY, params);

		return aux.toString();
	}

	public static String formatFailedOperation(String method, String user, String role) {

		JSONObject aux = new JSONObject();
		aux.put(METHOD_KEY, method);
		aux.put(OP_STATUS_KEY, OperationStatus.FAILED);
		aux.put(USER_KEY, user);
		aux.put(ROLE_KEY, role);
		aux.put(PARAMS_KEY, new JSONObject());

		return aux.toString();
	}
	
	public static String formatSuccessfulOperation(String method, List<?> items, String user, String role, JSONObject params) {

		JSONObject aux = new JSONObject();
		aux.put(METHOD_KEY, method);
		aux.put(OP_STATUS_KEY, OperationStatus.SUCCESS);
		aux.put(SIZE_KEY, items.size());
		aux.put(USER_KEY, user);
		aux.put(ROLE_KEY, role);
		aux.put(PARAMS_KEY, params);

		return aux.toString();
	}
	
	public static <T> String formatSuccessful(String method, Map<?, List<T>> items, String user, String role, JSONObject params) {
		
		JSONObject aux = new JSONObject();
		aux.put(METHOD_KEY, method);
		aux.put(OP_STATUS_KEY, OperationStatus.SUCCESS);
		aux.put(SIZE_KEY, items.size());
		aux.put(USER_KEY, user);
		aux.put(ROLE_KEY, role);
		aux.put(PARAMS_KEY, params);
		
		return aux.toString();
	}
	
	public static String formatSuccessfulOperation(String method, List<?> items, String user, String role) {

		JSONObject aux = new JSONObject();
		aux.put(METHOD_KEY, method);
		aux.put(OP_STATUS_KEY, OperationStatus.SUCCESS);
		aux.put(SIZE_KEY, items.size());
		aux.put(USER_KEY, user);
		aux.put(ROLE_KEY, role);
		aux.put(PARAMS_KEY, new JSONObject());

		return aux.toString();
	}

	public static String formatSuccessfulOperation(String method, Asset asset, String user, String role,
			JSONObject params) {

		JSONObject aux = new JSONObject();
		aux.put(METHOD_KEY, method);
		aux.put(OP_STATUS_KEY, OperationStatus.SUCCESS);
		aux.put(ASSET_ID_KEY, asset.getId());
		aux.put(USER_KEY, user);
		aux.put(ROLE_KEY, role);
		aux.put(PARAMS_KEY, params);

		return aux.toString();
	}

	public static String formatSuccessfulOperation(String method, AssetType assetType, String user, String role,
			JSONObject params) {
		JSONObject aux = new JSONObject();
		aux.put(METHOD_KEY, method);
		aux.put(OP_STATUS_KEY, OperationStatus.SUCCESS);
		aux.put("assetTypeName", assetType.getName());
		aux.put(USER_KEY, user);
		aux.put(ROLE_KEY, role);
		aux.put(PARAMS_KEY, params);
		return aux.toString();
	}

	public static String formatSuccessfulOperation(String method, String user, String role, JSONObject params) {

		JSONObject aux = new JSONObject();
		aux.put(METHOD_KEY, method);
		aux.put(OP_STATUS_KEY, OperationStatus.SUCCESS);
		aux.put(USER_KEY, user);
		aux.put(ROLE_KEY, role);
		aux.put(PARAMS_KEY, params);
		
		return aux.toString();
	}

	
}