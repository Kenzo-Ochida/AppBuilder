package br.mackenzie.mackleaps.utils;


public class DatabaseUtils {

	private DatabaseUtils() {
	}
	
	private static String databaseName;
	private static String databaseServer;
	private static int databasePort;
	private static String databaseUsername;
	private static String databasePassword;
	private static String databaseDriver;

	public static String getDatabaseName() {
		return databaseName;
	}
	public static void setDatabaseName(String databaseNameParam) {
		databaseName = databaseNameParam;
	}
	public static String getDatabaseServer() {
		return databaseServer;
	}
	public static void setDatabaseServer(String databaseServerParam) {
		databaseServer = databaseServerParam;
	}
	public static int getDatabasePort() {
		return databasePort;
	}
	public static void setDatabasePort(int databasePortParam) {
		databasePort = databasePortParam;
	}
	public static String getDatabaseUsername() {
		return databaseUsername;
	}
	public static void setDatabaseUsername(String databaseUsernameParam) {
		databaseUsername = databaseUsernameParam;
	}
	public static String getDatabasePassword() {
		return databasePassword;
	}
	public static void setDatabasePassword(String databasePasswordParam) {
		databasePassword = databasePasswordParam;
	}
	public static String getDatabaseDriver() {
		return databaseDriver;
	}
	public static void setDatabaseDriver(String databaseDriverParam) {
		databaseDriver = databaseDriverParam;
	}

	
}
