package br.mackenzie.mackleaps.asset.persistence;

import br.mackenzie.mackleaps.asset.persistence.db.Connection;
import br.mackenzie.mackleaps.utils.DatabaseUtils;

public class ConnectionSingleton {
	
	private ConnectionSingleton() {
	}
	private static Connection connection;
	
	private static void setConnection(String dbName, String userName, String server, int port, String password, String driver){
		connection = new Connection();
		connection.setDbName(dbName);
		connection.setUserName(userName);
		connection.setServer(server);
		connection.setPort(port);
		connection.setPassword(password);
		connection.setAttr1(password);
		connection.setAttr2(driver);
	}
	
	public static synchronized Connection getInstance(){
		if (connection == null){
			setConnection(DatabaseUtils.getDatabaseName(), DatabaseUtils.getDatabaseUsername(), 
					      DatabaseUtils.getDatabaseServer(), DatabaseUtils.getDatabasePort(), 
					      DatabaseUtils.getDatabasePassword(), DatabaseUtils.getDatabaseDriver());
		}
		return connection;
	}
}