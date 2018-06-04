package com.iceps.spring.shardingjdbc1.sharding.algorithm;

import java.util.Collection;
import java.util.LinkedHashSet;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;
import com.iceps.spring.shardingjdbc1.utils.StringUtils;

public class CustTableShardingAlgorithm implements SingleKeyTableShardingAlgorithm<String> {

	private String getShardingIndexTable(Collection<String> tableNames, int shardingIndex) {
		int i = 0;
		for (String tblName : tableNames) {
			if (i == shardingIndex)
				return tblName;
			i++;
		}
		return null;
	}

	// @Override
	// public String doSharding(Collection<String> tableNames,
	// PreciseShardingValue<String> shardingValue) {
	// System.out.println("CustTableShardingAlgorithm.tableNames:" +
	// StringUtils.toString(tableNames));
	// System.out.println("CustTableShardingAlgorithm.shardingValue:" +
	// shardingValue);
	// String shardingValueStr =
	// shardingValue.getValue().substring(shardingValue.getValue().length() - 2,
	// shardingValue.getValue().length());
	// int shardingIndex = Integer.valueOf(shardingValueStr) %
	// tableNames.size();
	// String shardingTableName = getShardingIndexTable(tableNames,
	// shardingIndex);
	// System.out.println("CustTableShardingAlgorithm.shardingValueStr:" +
	// shardingValueStr + ",shardingIndex=" + shardingIndex + " ==>
	// shardingTableName=" + shardingTableName);
	// return shardingTableName;
	//// throw new UnsupportedOperationException();
	// }

	@Override
	public Collection<String> doBetweenSharding(Collection<String> tableNames, ShardingValue<String> shardingValue) {
		System.out.println("CustTableShardingAlgorithm.doBetweenSharding.tableNames:" + StringUtils.toString(tableNames));
		return tableNames;
	}

	@Override
	public String doEqualSharding(Collection<String> tableNames, ShardingValue<String> shardingValue) {
		System.out.println("CustTableShardingAlgorithm.doEqualSharding.tableNames:" + StringUtils.toString(tableNames));
		System.out.println("CustTableShardingAlgorithm.doEqualSharding.shardingValue:" + shardingValue);
		String shardingValueStr = shardingValue.getValue().substring(shardingValue.getValue().length() - 2,
				shardingValue.getValue().length());
		int shardingIndex = Integer.valueOf(shardingValueStr) % tableNames.size();
		String shardingTableName = getShardingIndexTable(tableNames, shardingIndex);
		System.out.println("CustTableShardingAlgorithm.doEqualSharding.shardingValueStr:" + shardingValueStr + ",shardingIndex="
				+ shardingIndex + " ==> shardingTableName=" + shardingTableName);
		return shardingTableName;
	}

	@Override
	public Collection<String> doInSharding(Collection<String> tableNames, ShardingValue<String> shardingValue) {
		Collection<String> result = new LinkedHashSet<String>(tableNames.size());
		for (String singleValue : shardingValue.getValues()) {
			String shardingValueStr = singleValue.substring(singleValue.length() - 2, singleValue.length());
			int shardingIndex = Integer.valueOf(shardingValueStr) % tableNames.size();
			String shardingTableName = getShardingIndexTable(tableNames, shardingIndex);
			System.out.println("CustTableShardingAlgorithm.doInSharding.shardingValueStr:" + singleValue
					+ ",shardingIndex=" + shardingIndex + " ==> shardingTableName=" + shardingTableName);
			result.add(shardingTableName);
		}
		return result;
	}

}
