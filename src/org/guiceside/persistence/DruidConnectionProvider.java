package org.guiceside.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.hibernate.service.spi.Configurable;
import org.hibernate.service.spi.Stoppable;

/**
 * Created by zhenjiaWang on 14/12/2.
 */
public class DruidConnectionProvider implements ConnectionProvider, Configurable, Stoppable {

    private static final long serialVersionUID = 1026193803901107651L;

    private DruidDataSource   dataSource;

    public DruidConnectionProvider(){
        dataSource = new DruidDataSource();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return dataSource.isWrapperFor(unwrapType);
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return dataSource.unwrap(unwrapType);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void closeConnection(Connection conn) throws SQLException {
        conn.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void configure(Map configurationValues) {
        try {
            DruidDataSourceFactory.config(dataSource, configurationValues);
        } catch (SQLException e) {
            throw new IllegalArgumentException("config error", e);
        }
    }

    @Override
    public void stop() {
        dataSource.close();
    }

}