package com.xqh.ad;

import com.xqh.ad.service.XQHAdService;
import com.xqh.ad.utils.config.AdIdfaReportConfigUtils;
import com.xqh.ad.utils.config.DiscountConfigUtils;
import com.xqh.ad.utils.UrlUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AdApplicationTests {

	@Resource
	private XQHAdService xqhAdService;

	@Resource
	private AdIdfaReportConfigUtils adIdfaReportConfigUtils;

	@Test
	public void testConfig()
	{
		Integer minValue = adIdfaReportConfigUtils.getMinValue(1, 29);
		log.info("==========config:{}", minValue);
	}

	@Test
	public void callbackTest() throws InterruptedException {
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				xqhAdService.callback(1, "url1");
			}
		});

		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				xqhAdService.callback(2, "url2");
			}
		});

		t1.start();
		t2.start();

		Thread.sleep(1000);

	}

	@Test
	public void contextLoads() {
	}

	public static void main(String[] args)
	{
		System.out.println(String.format("%05d%05d", 1, 22));
		System.out.println(Integer.valueOf(null));

		String url = "http://www.baidu.com";
		System.out.println(UrlUtils.UrlPage(url));
		System.out.println(UrlUtils.URLRequest(url));

	}

	@Autowired
	DiscountConfigUtils discountConfigUtils;
	@Test
	public void discountTest() {
		int unit = 8;
		Double discountRate = 0.9;
		String discountConfig = discountConfigUtils.getConfig(discountRate);
		char flag = discountConfig.charAt(unit);
		if(java.util.Objects.equals('1', flag))
		{
			log.info("第：{}位标识：1 回调 不扣量", unit);
			return ;
		}
		else
		{
			log.info("第：{}为标识：非1 不回调 扣量 flag:{}", unit, flag);
			return ;
		}
	}

}
