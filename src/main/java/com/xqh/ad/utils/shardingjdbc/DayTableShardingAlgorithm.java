package com.xqh.ad.utils.shardingjdbc;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;

import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Created by hssh on 2017/9/7.
 */
public class DayTableShardingAlgorithm implements SingleKeyTableShardingAlgorithm<String>
{
    @Override
    public String doEqualSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue)
    {
        for (String tableName : availableTargetNames)
        {
            int dayTable = getDayByTableName(tableName);
            int dayId = getDayById(shardingValue.getValue());

            if(dayTable == dayId)
            {
                return tableName;
            }

        }

        throw new IllegalArgumentException();
    }

    @Override
    public Collection<String> doInSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue)
    {
        Collection<String> result = new LinkedHashSet<>(availableTargetNames.size());

        for (String id : shardingValue.getValues())
        {
            for (String tableName : availableTargetNames)
            {
                if(getDayByTableName(tableName) == getDayById(id))
                {
                    result.add(tableName);
                }
            }
        }

        return result;
    }

    @Override
    public Collection<String> doBetweenSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue)
    {
        //Collection<String> result = new LinkedHashSet<>(availableTargetNames.size());
        //
        //Range<BigInteger> valueRange = shardingValue.getValueRange();
        //
        //for (BigInteger id=valueRange.lowerEndpoint(); id.compareTo(valueRange.upperEndpoint())<=0; id.add(BigInteger.valueOf(1)))
        //{
        //    for (String tableName : availableTargetNames)
        //    {
        //        if(getDayById(id) == getDayByTableName(tableName))
        //        {
        //            result.add(tableName);
        //        }
        //    }
        //
        //}
        //return result;
        throw new IllegalArgumentException();
    }

    /**
     * 根据表名取得日期
     */
    private int getDayByTableName(String tableName)
    {
       return Integer.parseInt(tableName.substring(tableName.lastIndexOf("_") + 1));
    }

    /**
     * 根据id取得日期
     */
    private int getDayById(String id)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(id.substring(0, 13)));

        return calendar.get(Calendar.DAY_OF_MONTH);
    }
}
