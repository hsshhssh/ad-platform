package com.xqh.ad;

import com.xqh.ad.service.AdClickService;
import com.xqh.ad.tkmapper.entity.AdClick;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

//@RunWith(SpringRunner.class)
//@SpringBootTest
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
		for (int i=1; i<=15; i++)
		{
			System.out.print(String.format("%02d", i));
			System.out.print(",");
		}
		System.out.println("");
		for (int i = 16; i <=31 ; i++)
		{

			System.out.print(i);
			System.out.print(",");
		}
	}

}
