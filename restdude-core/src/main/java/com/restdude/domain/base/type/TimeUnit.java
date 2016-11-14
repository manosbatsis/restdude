/**
 * calipso-hub-framework - A full stack, high level framework for lazy application hackers.
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.base.type;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public enum TimeUnit {
	MILLISECOND,
	SECOND,
	MINUTE,
	HOUR,
	DAY,
	WEEK,
	MONTH,
	YEAR;
	
	private static Map<TimeUnit, Integer> toCalendarUnit = new HashMap<TimeUnit, Integer>();
	static{
		toCalendarUnit.put(YEAR, Calendar.YEAR);
		toCalendarUnit.put(MONTH, Calendar.MONTH);
		toCalendarUnit.put(DAY, Calendar.DATE);
	}
	
	public int toCalendarUnit(){
		return toCalendarUnit.get(this);
	}
}
