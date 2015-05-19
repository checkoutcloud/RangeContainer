/*
 * Threaded version of the RangeQuery class. 
 * Returns the indices of data array that which contain value of Net meeting the search criteria
 * 
 */
package com.workday.rangecontainer.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import static java.util.Arrays.asList;

import com.workday.rangecontainer.Ids;
import com.workday.rangecontainer.RangeContainer;

public class ThreadedRangeContainerImpl implements RangeContainer
{
	final long[] data;// Data cached in this node. Immutable.

	Logger log = Logger.getLogger(this.getClass().toString());

	public ThreadedRangeContainerImpl(final long[] data)
	{
		this.data = data;
		log.info("data size " + data.length);
	}

	public Ids findIdsInRange(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive)
	{
		log.info("fromValue :" + fromValue + ": toValue :" + toValue + " fromInclusive " + fromInclusive + " toInclusive +toInclusive");
		IdsImpl ids = new IdsImpl();// Each query will get its own resul-tset

		if (null != data)
		{
			//Creating a separator thread pool per query. 
			//If we expect heavy load on this component, we may be better off creating a shared Pool and throttling the number of threads.
			//Better yet, we can use a managed thread pool.
			ExecutorService executor = Executors.newFixedThreadPool(2);
			int split = data.length / 2;//Assuming 2 threads for the sake of simplicity. Pool size can be externalized
			try
			{
				List<Future<List<Short>>> results = executor.invokeAll(asList(new RangeCheck(fromValue, toValue, fromInclusive, toInclusive, data, (short) 0, (short) split), new RangeCheck(fromValue, toValue, fromInclusive, toInclusive, data, (short) split, (short) data.length)));

				executor.shutdown();

				for (Iterator<Future<List<Short>>> iterator = results.iterator(); iterator.hasNext();)
				{
					Future<List<Short>> future = (Future<List<Short>>) iterator.next();
					List<Short> indicies = future.get();
					for (Iterator<Short> iterator2 = indicies.iterator(); iterator2.hasNext();)
					{
						Short index = (Short) iterator2.next();
						ids.add(index);
					}
				}					
			}
			catch (InterruptedException e)//Since interface does not declare any exceptions we have to catch at this level and return a error response code.
			{
				e.printStackTrace();
				ids = new IdsImpl();//Re-initialize to blank object so that caller gets a default response
			}
			//Even if one thread failed for some reason, result-set is incomplete hence it is invalid result. Better to return error response in this case.
			catch (ExecutionException e)//Since interface does not declare any exceptions we have to catch at this level and return a error response code.
			{
				e.printStackTrace();
				ids = new IdsImpl();//Re-initialize to blank object so that caller gets a default response
			}

		}

		log.info("Found " + ids.size() + " results");
		return ids;
	}

	class RangeCheck implements Callable<List<Short>>
	{
		private final long fromValue;
		private final long toValue;
		private final boolean fromInclusive;
		private final boolean toInclusive;
		private final short indexStart;
		private final short indexEnd;
		RangeCheck(long from, long to, boolean fromInclusive, boolean toInclusive, long[] data, short indexStart, short indexEnd)
		{
			this.fromValue = from;
			this.toValue = to;
			this.fromInclusive = fromInclusive;
			this.toInclusive = toInclusive;
			this.indexStart = indexStart;
			this.indexEnd = indexEnd;
			
		}

		// @Override
		public List<Short> call()
		{
			List<Short> results = new ArrayList<Short>();

			for (short i = indexStart ; i < indexEnd; i++)
			{
				if ((fromInclusive && data[i] >= fromValue || data[i] > fromValue) && (toInclusive && data[i] <= toValue || data[i] < toValue))
				{
					results.add(i);
				}
			}
			return results;
		}
	}

}
