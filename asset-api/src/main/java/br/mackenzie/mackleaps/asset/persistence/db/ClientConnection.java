package br.mackenzie.mackleaps.asset.persistence.db;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ClientConnection {

        private static final Logger LOGGER = LoggerFactory.getLogger(ClientConnection.class);
        private static HikariConfig config = new HikariConfig();
        private static HikariDataSource ds;

        private ClientConnection(){}

        public static java.sql.Connection getClient(Connection conn) throws SQLException {
                if(ds == null) {
                        LOGGER.info("Configuring Url= {} and user={}", conn.getServer() + conn.getDbName(), conn.getUserName());
                        config.setJdbcUrl("jdbc:mariadb://assetApidb:3306/asset_api");
                    config.setUsername("asset_api");
                    config.setPassword("lfs123!@#");
                    config.addDataSourceProperty("cachePrepStmts", "true");
                    config.addDataSourceProperty("prepStmtCacheSize", "250");
                    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
                    ds = new HikariDataSource(config);
                }
                return ds.getConnection();
        }

}

