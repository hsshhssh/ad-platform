package com.xqh.ad;

import com.xqh.ad.utils.UrlUtils;
import org.junit.Test;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class AdApplicationTests {

	@Test
	public void contextLoads() {
	}

	public static void main(String[] args)
	{
		System.out.println(String.format("%05d%05d", 1, 22));


		String url = "http://www.baidu.com";
		System.out.println(UrlUtils.UrlPage(url));
		System.out.println(UrlUtils.URLRequest(url));

	}

}
