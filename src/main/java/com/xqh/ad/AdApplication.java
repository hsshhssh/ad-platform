package com.xqh.ad;

import com.xqh.ad.utils.common.DozerUtils;
import org.dozer.DozerBeanMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableAsync
@MapperScan(basePackages = "com.xqh.ad.tkmapper.mapper")
public class AdApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdApplication.class, args);
	}

	@Bean
	public DozerBeanMapper dozerBean() {
		List<String> mappingFiles = Arrays.asList(
				//"dozer/dozer-mapping.xml"
		);

		DozerBeanMapper dozerBean = new DozerBeanMapper();
		dozerBean.setMappingFiles(mappingFiles);
		return dozerBean;
	}

	@Bean
	public DozerUtils dozerUtils() {
		return new DozerUtils();
	}
}
