/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christian-fries.de.
 *
 * Created on 07.09.2013
 */

package net.finmath.time.daycount;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;
import org.threeten.bp.chrono.IsoChronology;

/**
 * Implementation of ACT/365A.
 * 
 * Calculates the day count by calculating the actual number of days between startDate and endDate.
 * 
 * A fractional day is rounded to the approximately nearest day.
 * 
 * The day count fraction is calculated using ACT/365A convention, that is, the
 * day count is divided by 366 if February 29 lies in between startDate (excluding) and endDate (including),
 * otherwise it the day count is divided by 365.
 * 
 * @see DayCountConvention_ACT_365
 * @see DayCountConvention_ACT_365L
 * 
 * @author Christian Fries
 */
public class DayCountConvention_ACT_365A extends DayCountConvention_ACT {

	/**
	 * Create an ACT/365 day count convention.
	 */
	public DayCountConvention_ACT_365A() {
	}
	

	/* (non-Javadoc)
	 * @see net.finmath.time.daycount.DayCountConventionInterface#getDaycountFraction(org.threeten.bp.LocalDate, org.threeten.bp.LocalDate)
	 */
	@Override
	public double getDaycountFraction(LocalDate startDate, LocalDate endDate) {
		if(startDate.isAfter(endDate)) return -getDaycountFraction(endDate,startDate);

		double daysPerYear = 365.0;
		
		// Check startDate for leap year
		if (startDate.isLeapYear()) {
			LocalDate leapDayStart = LocalDate.of(startDate.getYear(), Month.FEBRUARY, 29);
			if(startDate.isBefore(leapDayStart) && !endDate.isBefore(leapDayStart)) daysPerYear = 366.0;
		}

		// Check endDate for leap year
		if (endDate.isLeapYear()){
			LocalDate leapDayEnd = LocalDate.of(endDate.getYear(),  Month.FEBRUARY,  29);
			if(startDate.isBefore(leapDayEnd) && !endDate.isBefore(leapDayEnd)) daysPerYear = 366.0;
		}

		// Check in-between years for leap year
		for(int year = startDate.getYear()+1; year < endDate.getYear(); year++) if(IsoChronology.INSTANCE.isLeapYear(year)) daysPerYear = 366.0;
		
		double daycountFraction = getDaycount(startDate, endDate) / daysPerYear;

		return daycountFraction;
	}
}
