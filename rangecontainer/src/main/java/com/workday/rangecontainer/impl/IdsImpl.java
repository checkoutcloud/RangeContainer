package com.workday.rangecontainer.impl;

import java.util.ArrayList;
import java.util.List;

import com.workday.rangecontainer.Ids;

public class IdsImpl implements Ids
{
	List<Long> ids ;//Since we don't know how many items may meet the search criteria use list instead of array

	short position = 0;
	
	public IdsImpl()
	{
		ids  = new ArrayList<Long>();
	}
	
	public void add(short index)
	{
		ids.add(Long.valueOf(index));
		
	}
	//Short works fine since Short.MAX_VALUE is 32768 and we know each node will at most save 32K results
	public short nextId()
	{
		if(position<ids.size())
			return position++;
		else 
			return Ids.END_OF_IDS;
	}
	public int size() 
	{
		return ids.size();
	}
}
