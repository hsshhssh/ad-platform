package com.xqh.ad.utils.shardingjdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hssh on 2017/9/7.
 */
@Configuration
public class ShardingJdbcConfig
{

    @Bean
    @Primary
    public DataSource buildDataSource() throws SQLException {
        Map<String, DataSource> dataSourceMap = new HashMap<>(1);
        dataSourceMap.put("ds", getDataSource());
        DataSourceRule dataSourceRule = new DataSourceRule(dataSourceMap, "ds");


        List<String> clickTableName = Lists.newArrayList();
        for(int i=1; i<=31; i++)
        {
            clickTableName.add("ad_click_" + i);
        }
        TableRule clickTableRule = TableRule.builder("ad_click")
                .actualTables(clickTableName)
                .dataSourceRule(dataSourceRule)
                .tableShardingStrategy(new TableShardingStrategy("id", new DayTableShardingAlgorithm()))
                .databaseShardingStrategy(new DatabaseShardingStrategy("id") new )
                .build();

        ShardingRule shardingRule = ShardingRule.builder()
                .dataSourceRule(dataSourceRule)
                .tableRules(Arrays.asList(clickTableRule))
                .build();

        return ShardingDataSourceFactory.createDataSource(shardingRule);

    }

    public DataSource getDataSource()
    {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.001:3306/db_xqhad?useUnicode=true&characterEncoding=UTF-8 ");
        dataSource.setUsername("root");
        dataSource.setPassword("");
        try {
            dataSource.init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataSource;
    }

}
