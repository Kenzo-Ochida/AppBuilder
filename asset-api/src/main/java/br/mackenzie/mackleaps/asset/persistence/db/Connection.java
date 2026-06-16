package br.mackenzie.mackleaps.asset.persistence.db;

public class Connection {
	private String server;
	private String userName;
	private String dbName;
	private String password;
	private String attr1;
	private String attr2;
	private int port;
	
	public Connection() {
		//constructor is empty so an object can be instantiated and then have its attributes instantiated
	}
	
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getAttr1() {
		return attr1;
	}
	public void setAttr1(String attr1) {
		this.attr1 = attr1;
	}
	public String getAttr2() {
		return attr2;
	}
	public void setAttr2(String attr2) {
		this.attr2 = attr2;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "Connection [server=" + server + ", userName=" + userName + ", dbName=" + dbName + ", password="
				+ password + ", attr1=" + attr1 + ", attr2=" + attr2 + ", port=" + port + "]";
	}

	
	
}
