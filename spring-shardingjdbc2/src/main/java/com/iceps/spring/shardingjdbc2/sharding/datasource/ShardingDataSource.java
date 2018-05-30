package com.iceps.spring.shardingjdbc2.sharding.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;

import com.iceps.spring.shardingjdbc2.sharding.algorithm.CustTableShardingAlgorithm;

import io.shardingjdbc.core.api.ShardingDataSourceFactory;
import io.shardingjdbc.core.api.config.ShardingRuleConfiguration;
import io.shardingjdbc.core.api.config.TableRuleConfiguration;
import io.shardingjdbc.core.api.config.strategy.StandardShardingStrategyConfiguration;

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

		ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
		shardingRuleConfig.getTableRuleConfigs().add(getOrderTableRuleConfiguration());
		shardingRuleConfig.getBindingTableGroups().add("t_order");

		try {
			sharding = ShardingDataSourceFactory.createDataSource(result, shardingRuleConfig,
					new HashMap<String, Object>(), new Properties());
		} catch (SQLException e) {
			System.err.println("ShardingDataSourceFactory.createDataSource failed." + e);
			e.printStackTrace();
		}

	}

	private TableRuleConfiguration getOrderTableRuleConfiguration() {
		TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
		orderTableRuleConfig.setLogicTable("t_order");
//		orderTableRuleConfig.setActualDataNodes("ds.t_order_${[0, 1, 2, 3, 4]}");
		orderTableRuleConfig.setActualDataNodes("ds.t_order_${[0, 1, 2, 3]}");
//		orderTableRuleConfig.setKeyGeneratorColumnName("order_id");

		// ShardingStrategyConfiguration

		System.out.println("start load table rule config");
		orderTableRuleConfig.setTableShardingStrategyConfig(
				new StandardShardingStrategyConfiguration("order_id", CustTableShardingAlgorithm.class.getName()));
		System.out.println("end load table rule config");
		return orderTableRuleConfig;
	}
}
