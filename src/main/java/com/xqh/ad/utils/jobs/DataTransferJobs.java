package com.xqh.ad.utils.jobs;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by hssh on 2017/9/18.
 */
@Component
public class DataTransferJobs
{
    private static String bkDatabaseName = "bkdb_xqhad";
    private static String databaseName = "db_xqhad";

    private static Logger logger = LoggerFactory.getLogger(DataTransferJobs.class);

    @Resource(name = "bkJdbcTemplate")
    private JdbcTemplate bkJdbcTemplate;


    @Scheduled(cron = "0 30 2 1-30 * ? ")
    public void dataTransfer()
    {


        // 计算需要备份的表 对应关系如下
        // 01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,
        // 16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,
        List<String> bkAdClickTableTemp = getBkAdClickTable();
        if(CollectionUtils.isEmpty(bkAdClickTableTemp))
        {
            return;
        }

        // 计算备份库的表名 => 几年几月
        String bkTableName = getBkTableName();

        // 防重
        List<String> bkAdClickTable = repeatRun(bkAdClickTableTemp, bkTableName);
        if(CollectionUtils.isEmpty(bkAdClickTable))
        {
            return;
        }


        // 创建数据库表
        bkJdbcTemplate.execute(getCreateTableSQL(bkDatabaseName, bkTableName));

        // 备份数据 => 从活跃库版搬移到备份库
        for (String tableName : bkAdClickTable)
        {
            bkJdbcTemplate.execute(getInsertSQL(bkDatabaseName, bkTableName, databaseName, tableName));
        }

        //TODO 校验数据 => 检查活跃库里面的数据在备份库中有没有


        // 删除数据 => 删除活跃库的数据
        for (String tableName : bkAdClickTable)
        {
            bkJdbcTemplate.execute(getDeleteSQL(databaseName, tableName));
        }

    }


    public List<String> getBkAdClickTable()
    {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);

        List<String> adClickTableList = Lists.newArrayList();

        if(15 == day)
        {
            adClickTableList.add("ad_click_" + 30);
            adClickTableList.add("ad_click_" + 31);
        }
        else if(31 == day)
        {

        }
        else {
            int bkDay = (day + 15)%30;
            adClickTableList.add("ad_click_" + bkDay);
        }

        return adClickTableList;
    }

    public String getBkTableName()
    {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);

        int moneyDiff = ((day + 15)/31 - 1);

        c.add(Calendar.MONTH, moneyDiff);
        SimpleDateFormat format =  new SimpleDateFormat("yyyy_MM");
        String yearMonth = format.format(c.getTime());

        return "ad_click_" + yearMonth;
    }

    public String getCreateTableSQL(String bkDatabaseName, String bkTableName)
    {
        return String.format("CREATE TABLE IF NOT EXISTS %s.%s (\n" +
                "  `id` char(30) NOT NULL COMMENT '主键id',\n" +
                "  `app_media_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '推广应用-媒体主键',\n" +
                "  `app_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '推广应用id',\n" +
                "  `media_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '媒体id',\n" +
                "  `league_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '联盟id',\n" +
                "  `phone_type` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '手机类型 1安卓 2苹果',\n" +
                "  `imei` char(50) NOT NULL DEFAULT '' COMMENT '手机标识imei',\n" +
                "  `mac` char(50) NOT NULL DEFAULT '' COMMENT '物理地址',\n" +
                "  `ip` char(50) NOT NULL DEFAULT '' COMMENT 'ip',\n" +
                "  `android_id` char(50) NOT NULL DEFAULT '' COMMENT '手机标识android_id',\n" +
                "  `idfa` char(50) NOT NULL DEFAULT '' COMMENT '手机标识idfa',\n" +
                "  `callback_url` varchar(500) NOT NULL DEFAULT '' COMMENT '回调url',\n" +
                "  `create_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',\n" +
                "  `update_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '修改时间',\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;", bkDatabaseName, bkTableName);
    }

    public String getInsertSQL(String bkDatabaseName, String bkTableName, String databaseName, String tableName)
    {
        return String.format("insert into %s.%s (id,app_media_id,app_id,media_id,league_id,phone_type,imei,mac,ip,android_id,idfa,callback_url,create_time,update_time) select id,app_media_id,app_id,media_id,league_id,phone_type,imei,mac,ip,android_id,idfa,callback_url,create_time,update_time from %s.%s", bkDatabaseName, bkTableName, databaseName, tableName);
    }

    public String getDeleteSQL(String databaseName, String tableName)
    {
        return String.format("delete from %s.%s", databaseName, tableName);
    }


    public List<String> repeatRun(List<String> bkAdClickTable, String bkTableName)
    {
        List<String> res = Lists.newArrayList();

        int nowTime = (int) (System.currentTimeMillis()/1000);

        String yearMoney = bkTableName.substring(bkAdClickTable.lastIndexOf("_") + 1);
        for (String tableName : bkAdClickTable)
        {
            String day = String.format("%02d", Integer.valueOf(tableName.substring(tableName.lastIndexOf("_") + 1)));
            int record = Integer.valueOf(yearMoney + day);

            try
            {
                bkJdbcTemplate.execute(String.format("insert into %s.ad_click_transfer_record (record, create_time, update_time) values (%d, %d, %d)", databaseName, record, nowTime, nowTime));
            } catch (DuplicateKeyException e)
            {
                logger.error("record:{} 重复备份", record);
                continue;
            }

            res.add(tableName);
        }
        return res;
    }
}
