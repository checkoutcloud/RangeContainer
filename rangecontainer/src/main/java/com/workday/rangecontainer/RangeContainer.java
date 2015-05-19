package com.workday.rangecontainer;

/**
 * * a specialized container of records optimized for efficient range queries on an attribute of the data.
 */
public interface RangeContainer
{
	/**
	 * * @return the Ids of all instances found in the container that have
	 * data value between fromValue and toValue with optional inclusivity.
	 * see Ids interface to learn how to process and use Ids.
	 */
	Ids findIdsInRange(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive);
}
