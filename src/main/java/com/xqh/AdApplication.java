package com.xqh;

import com.github.zkclient.ZkClient;
import com.xqh.ad.utils.common.DozerUtils;
import org.dozer.DozerBeanMapper;
import org.hssh.common.ZkdbcpConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableAsync
@MapperScan(basePackages = {"com.xqh.ad.tkmapper.mapper","com.xqh.account.tkmapper.mapper"})
@EnableScheduling
public class AdApplication
{

	@Autowired
	private ZkdbcpConfig zkdbcpConfig;

	public static void main(String[] args) {
		SpringApplication.run(AdApplication.class, args);
	}

	@Bean
	public DozerBeanMapper dozerBean() {
		List<String> mappingFiles = Arrays.asList(
				"dozer/dozer-mapping.xml"
		);

		DozerBeanMapper dozerBean = new DozerBeanMapper();
		dozerBean.setMappingFiles(mappingFiles);
		return dozerBean;
	}

	@Bean
	public DozerUtils dozerUtils() {
		return new DozerUtils();
	}

	@Bean
	public ZkClient zkClient()
	{
		return new ZkClient(System.getenv("ZK_HOST"));
	}

	@Bean("bkJdbcTemplate")
	public JdbcTemplate bkJdbcTemplate() throws SQLException
	{
		return new JdbcTemplate(zkdbcpConfig.createBkDataSource());
	}

}
