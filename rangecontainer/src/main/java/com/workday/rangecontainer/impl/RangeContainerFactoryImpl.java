package com.workday.rangecontainer.impl;

import com.workday.rangecontainer.RangeContainer;
import com.workday.rangecontainer.RangeContainerFactory;

/*
 * Creates a Container, initializes the factory with data to query.
 * Creates a basic container if size of data array is small
 * If size of data Array is more, crates a threaded version of the container since it is worth the overhead of threading at this level.
 * Since we use Factory Method pattern, client of this class does not have to know which type of container is returned. Just works with the interface.
*/

/**
 * builds an immutable container optimized for range queries. 
 * Data is expected to be 32k items or less. 
 * The position in the “data” array represents the “id” for that instance in question. 
 * For the “PayrollResult” example before, the “id” might be the worker’s employee number, the data value is the corresponding net pay.
 * E.g, data[5]=2000 means that employee #6 has net pay of 2000.
 */

public class RangeContainerFactoryImpl implements RangeContainerFactory
{
	public RangeContainer createContainer(long[] data)
	{
		if(null == data) return null;//Just in case factory is initialized with null data. We could add checked exception to the interface
		else if( data.length<6000)//It may be worth using a threaded version only if data is more.
			return new RangeContainerImpl(data);
		else 
			return new ThreadedRangeContainerImpl(data);
	}

}
