package com.iceps.spring.shardingjdbc1.sharding.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;

import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.iceps.spring.shardingjdbc1.sharding.algorithm.CustTableShardingAlgorithm;

public class ShardingDataSource implements DataSource, InitializingBean {

	/**
	 * 代理数据源
	 */
	private DataSource source;

	/**
	 * 分表数据源
	 */
	private DataSource sharding;

	public DataSource getSource() {
		return source;
	}

	public void setSource(DataSource source) {
		this.source = source;
	}

	public DataSource getSharding() {
		return sharding;
	}

	public void setSharding(DataSource sharding) {
		this.sharding = sharding;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return sharding.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		sharding.setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		sharding.setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return sharding.getLoginTimeout();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return sharding.getParentLogger();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return sharding.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return sharding.isWrapperFor(iface);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return sharding.getConnection();
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return sharding.getConnection(username, password);
	}

	public void init() {
		if (source instanceof com.alibaba.druid.pool.DruidDataSource) {
			try {
				((com.alibaba.druid.pool.DruidDataSource) source).init();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void close() {
		if (source instanceof com.alibaba.druid.pool.DruidDataSource) {
			try {
				((com.alibaba.druid.pool.DruidDataSource) source).close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, DataSource> result = new HashMap<String, DataSource>();
		result.put("ds", source);
		
        DataSourceRule dataSourceRule = new DataSourceRule(result);

        //tableShardingStrategy
        TableShardingStrategy tableShardingStrategy = new TableShardingStrategy("order_id",new CustTableShardingAlgorithm());

        //tableRule
        TableRule orderTableRule = new TableRule.TableRuleBuilder("t_order")
                .actualTables(Arrays.asList("t_order_0", "t_order_1", "t_order_2", "t_order_3"))
                .dataSourceRule(dataSourceRule)
                .build();

        //shardingRule
        ShardingRule shardingRule = ShardingRule.builder()
                .dataSourceRule(dataSourceRule)
                .tableRules(Arrays.asList(orderTableRule))
                .tableShardingStrategy(tableShardingStrategy)
                .build();

		try {
			sharding = ShardingDataSourceFactory.createDataSource(shardingRule);
		} catch (SQLException e) {
			System.err.println("ShardingDataSourceFactory.createDataSource failed." + e);
			e.printStackTrace();
		}
	}

}
