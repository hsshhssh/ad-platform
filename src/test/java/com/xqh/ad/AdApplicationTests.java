package com.xqh.ad;

import com.xqh.ad.service.AdClickService;
import com.xqh.ad.tkmapper.entity.AdClick;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdApplicationTests {

	@Autowired
	private AdClickService adClickService;

	@Test
	public void contextLoads()
	{
		AdClick adClick = new AdClick();
		adClick.setAppMediaId(1);
		adClickService.insert(adClick);
	}

	public static void main(String[] args)
	{

	}

}
