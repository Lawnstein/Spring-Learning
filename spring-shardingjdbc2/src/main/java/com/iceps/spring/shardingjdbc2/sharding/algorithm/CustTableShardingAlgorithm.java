package com.iceps.spring.shardingjdbc2.sharding.algorithm;

import java.text.SimpleDateFormat;
import java.util.Collection;

import com.iceps.spring.shardingjdbc2.utils.StringUtils;

import io.shardingjdbc.core.api.algorithm.sharding.PreciseShardingValue;
import io.shardingjdbc.core.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

public class CustTableShardingAlgorithm implements PreciseShardingAlgorithm<String> {

	private String getShardingIndexTable(Collection<String> tableNames, int shardingIndex) {
		int i = 0;
		for (String tblName : tableNames) {
			if (i == shardingIndex)
				return tblName;
			i++;
		}
		return null;
	}
	
	@Override
	public String doSharding(Collection<String> tableNames, PreciseShardingValue<String> shardingValue) {
		System.out.println("CustTableShardingAlgorithm.tableNames:" + StringUtils.toString(tableNames));
		System.out.println("CustTableShardingAlgorithm.shardingValue:" + shardingValue);		
		String shardingValueStr = shardingValue.getValue().substring(shardingValue.getValue().length() - 2, shardingValue.getValue().length());
		int shardingIndex = Integer.valueOf(shardingValueStr) % tableNames.size();
		String shardingTableName = getShardingIndexTable(tableNames, shardingIndex);
		System.out.println("CustTableShardingAlgorithm.shardingValueStr:" + shardingValueStr + ",shardingIndex=" + shardingIndex + " ==> shardingTableName=" + shardingTableName);
		return shardingTableName;
//		throw new UnsupportedOperationException();
	}

}
