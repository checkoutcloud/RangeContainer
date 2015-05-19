package com.workday.rangecontainer.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.workday.rangecontainer.Ids;
import com.workday.rangecontainer.RangeContainer;
import com.workday.rangecontainer.RangeContainerFactory;
import com.workday.rangecontainer.impl.IdsImpl;
import com.workday.rangecontainer.impl.RangeContainerFactoryImpl;
import static org.junit.Assert.assertEquals;

public class TestRangeContainer
{
	long[] basicTestData = {100, 200, 300, 50, 4000, 600};
	RangeContainerFactory rcf  = new RangeContainerFactoryImpl();
	RangeContainer rc = rcf.createContainer(basicTestData);
	Ids result = null;

	@Before
	public void before()
	{
	}

	@After
	public void after()
	{
	}

	@Test
	public void testBasicData()
	{
		IdsImpl ids  = new IdsImpl();	

		ids.add((short)1);
		ids.add((short)2);
		
		long start = 100L;
		long end = 600L;

		try
		{
			Ids resultIds = rc.findIdsInRange(start, end, false, false);
			for(int i=0;i<ids.size();i++)
				assertEquals(basicTestData[ids.nextId()], basicTestData[resultIds.nextId()]);
			assertEquals(resultIds.nextId(), Ids.END_OF_IDS);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Test
	public void testBasicDataIncl()
	{
		IdsImpl ids  = new IdsImpl();	

		ids.add((short)0);
		ids.add((short)1);
		ids.add((short)2);
		ids.add((short)5);
		
		long start = 100L;
		long end = 600L;
		try
		{
			Ids resultIds = rc.findIdsInRange(start, end, true, true);
			for(int i=0;i<ids.size();i++)
				assertEquals(basicTestData[ids.nextId()], basicTestData[resultIds.nextId()]);
			assertEquals(resultIds.nextId(), Ids.END_OF_IDS);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Test
	public void testLeftIncl()
	{
		IdsImpl ids  = new IdsImpl();	

		ids.add((short)0);
		ids.add((short)1);
		ids.add((short)2);
		
		long start = 100L;
		long end = 600L;

		try
		{
			Ids resultIds = rc.findIdsInRange(start, end, true, false);
			for(int i=0;i<ids.size();i++)
				assertEquals(basicTestData[ids.nextId()], basicTestData[resultIds.nextId()]);
			assertEquals(resultIds.nextId(), Ids.END_OF_IDS);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testRightIncl()
	{
		IdsImpl ids  = new IdsImpl();	


		ids.add((short)1);
		ids.add((short)2);
		ids.add((short)5);
		
		long start = 100L;
		long end = 600L;
		try
		{
			Ids resultIds = rc.findIdsInRange(start, end, true, false);
			for(int i=0;i<ids.size();i++)
				assertEquals(basicTestData[ids.nextId()], basicTestData[resultIds.nextId()]);
			
			assertEquals(resultIds.nextId(), Ids.END_OF_IDS);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*
	 * Check how long worst case scenario takes.
	 */
	@Test
	public void testFullRange()
	{
		long startTime = System.currentTimeMillis();
		long[] basicTestData = new long[32000];

		for(int i=0; i<32000;i++)
		{
			basicTestData[i]= i;
		}
		
		rcf  = new RangeContainerFactoryImpl();
		rc = rcf.createContainer(basicTestData);
		
		IdsImpl ids  = new IdsImpl();	


		ids.add((short)31999);
		ids.add((short)31998);
		ids.add((short)31997);
		
		long start = 31997L;
		long end = 31999L;
		try
		{
			Ids resultIds = rc.findIdsInRange(start, end, true, true);
			for(int i=0;i<ids.size();i++)
				assertEquals(basicTestData[ids.nextId()], basicTestData[resultIds.nextId()]);
			
			assertEquals(resultIds.nextId(), Ids.END_OF_IDS);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Time taken "+(System.currentTimeMillis()-startTime)+ " msec");
	}
	@Test
	public void testNullData()
	{
		
		long[] basicTestData = null;
		
		rcf  = new RangeContainerFactoryImpl();
		rc = rcf.createContainer(basicTestData);
		assertEquals(null, rc);
		
	}
	
	/*
	 * Check no data found
	 */
	@Test
	public void testNoDataFound()
	{
		
		long[] basicTestData = new long[2000];

		for(int i=0; i<2000;i++)
		{
			basicTestData[i]= i;
		}
		
		rcf  = new RangeContainerFactoryImpl();
		rc = rcf.createContainer(basicTestData);
			
		long start = 4000;
		long end = 5000;
		try
		{
			Ids resultIds = rc.findIdsInRange(start, end, true, true);

			assertEquals(resultIds.nextId(), Ids.END_OF_IDS);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
