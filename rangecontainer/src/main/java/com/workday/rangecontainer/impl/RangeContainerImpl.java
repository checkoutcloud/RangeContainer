package com.workday.rangecontainer.impl;

import java.util.logging.Logger;

import com.workday.rangecontainer.Ids;
import com.workday.rangecontainer.RangeContainer;

public class RangeContainerImpl implements RangeContainer
{
	final long[] data;//Data cached in this node. Immutable.
	
	Logger log = Logger.getLogger(this.getClass().toString());
	
	public RangeContainerImpl(final long[] data)
	{
		this.data = data;
		log.info("data size "+data.length);
	}
	
	public Ids findIdsInRange(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive)
	{
		log.info("fromValue :"+fromValue+": toValue :"+toValue +" fromInclusive "+fromInclusive+" toInclusive +toInclusive");
		IdsImpl ids = new IdsImpl();//Each query will get its own resultset
		
		if(null != data) 
		{
			for(short i=0;i<data.length;i++)
			{
				if( (fromInclusive && data[i] >= fromValue || data[i] > fromValue ) && (toInclusive && data[i] <= toValue || data[i] < toValue ) )
				{
					ids.add(i);
				}
			}
		}
		
		log.info("Found "+ids.size()+ " results");
		return ids;
	}
}
