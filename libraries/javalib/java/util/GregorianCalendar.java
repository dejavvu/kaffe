package java.util;

import java.lang.ClassCastException;
import kaffe.util.NotImplemented;

/*
 * Java core library component.
 *
 * Copyright (c) 1997, 1998
 *      Transvirtual Technologies, Inc.  All rights reserved.
 *
 * See the file "license.terms" for information on usage and redistribution
 * of this file.
 */
public class GregorianCalendar
  extends Calendar
{
	private static final Calendar stdDateChange;
	private static final long serialVersionUID = -8125100834729963327L;
	final public static int BC = 0;
	final public static int AD = 1;
	final private static long MILLISECSPERSEC = 1000;
	final private static long MILLISECSPERMIN = 1000 * 60;
	final private static long MILLISECSPERHOUR = 1000 * 60 * 60;
	final private static long MILLISECSPERDAY = 1000 * 60 * 60 * 24;
	final private static int DAYSPERWEEK = 7;
	final private static int EPOCH_WDAY = Calendar.THURSDAY - Calendar.SUNDAY;
	final private static int EPOCH_YEAR = 1970;
	private Calendar dateChange = null;

static {
	// Initialize the day that time switched from Julian
	//  to Gregorian date keeping
	stdDateChange = new GregorianCalendar(1582, Calendar.OCTOBER, 15);
}

public GregorianCalendar()
	{
	this(TimeZone.getDefault(), Locale.getDefault());
}

public GregorianCalendar(Locale aLocale)
	{
	this(TimeZone.getDefault(), aLocale);
}

public GregorianCalendar(TimeZone zone)
	{
	this(zone, Locale.getDefault());
}

public GregorianCalendar(TimeZone zone, Locale aLocale)
	{
	super(zone, aLocale);
	setTime(new Date());
}

public GregorianCalendar(int year, int month, int date)
	{
	super();
	set(year, month, date);
}

public GregorianCalendar(int year, int month, int date, int hour, int minute)
	{
	super();
	set(year, month, date, hour, minute);
}

public GregorianCalendar(int year, int month, int date, int hour, int minute, int second)
	{
	super();
	set(year, month, date, hour, minute, second);
}

public void add(int field, int amount)
	{
	throw new NotImplemented(GregorianCalendar.class.getName() + ".add(int,int)");
}

public boolean after(Object when)
	{
	try {
		GregorianCalendar cal = (GregorianCalendar)when;
		if (getTimeInMillis() > cal.getTimeInMillis()) {
			return (true);
		}
	}
	catch (ClassCastException _) {
	}
	catch (NullPointerException __) {
	}
	return (false);
}

public boolean before(Object when)
	{
	try {
		GregorianCalendar cal = (GregorianCalendar)when;
		if (getTimeInMillis() < cal.getTimeInMillis()) {
			return (true);
		}
	}
	catch (ClassCastException _) {
	}
	catch (NullPointerException __) {
	}
	return (false);
}

// Calculate the date & time
private long computeDateTime()
	{
	long time = 0L;
	int end;

	if (isSet[YEAR]) {
		end = fields[YEAR];
		for (int year = EPOCH_YEAR; year < end; year++) {
			if (isLeapYear(year)) {
				time += 366;
			}
			else {
				time += 365;
			}
		}

		if (isSet[MONTH]) {
			end = fields[MONTH];
			for (int month = Calendar.JANUARY; month < end; month++) {
				switch (month) {
				case Calendar.JANUARY:
				case Calendar.MARCH:
				case Calendar.MAY:
				case Calendar.JULY:
				case Calendar.AUGUST:
				case Calendar.OCTOBER:
				case Calendar.DECEMBER:
				default:
					time += 31;
					break;
				case Calendar.FEBRUARY:
					if (isLeapYear(fields[YEAR])) {
						time += 29;
					}
					else {
						time += 28;
					}
					break;
				case Calendar.APRIL:
				case Calendar.SEPTEMBER:
				case Calendar.JUNE:
				case Calendar.NOVEMBER:
					time += 30;
					break;
				}
			}

			if (isSet[DAY_OF_MONTH]) {
				time += fields[DAY_OF_MONTH] - 1;
			}
			else if (isSet[WEEK_OF_MONTH] && isSet[DAY_OF_WEEK]) {
				throw new NotImplemented(GregorianCalendar.class.getName() + ".computeDateTime() " +
							 "with WEEK_OF_MONTH and DAY_OF_WEEK set.");
			}
			else if (isSet[DAY_OF_WEEK_IN_MONTH] && isSet[DAY_OF_WEEK]) {
				throw new NotImplemented(GregorianCalendar.class.getName() + ".computeDateTime() " +
							 "with WEEK_OF_MONTH_IN_MONTH and DAY_OF_WEEK set.");
			}
		}
		else if (isSet[DAY_OF_YEAR]) {
			time += fields[DAY_OF_YEAR];
		}
		else if (isSet[DAY_OF_WEEK] && isSet[WEEK_OF_YEAR]) {
			throw new NotImplemented(GregorianCalendar.class.getName() + ".computeDateTime() " +
						 "with DAY_OF_WEEK and WEEK_OF_YEAR set.");
		}
	}
	else {
		throw new NotImplemented(GregorianCalendar.class.getName() + ".computeDateTime() " +
					 "without YEAR set.");
	}
	time *= 24;

	if (isSet[HOUR_OF_DAY]) {
		time += fields[HOUR_OF_DAY];
	}
	else if (isSet[AM_PM] && isSet[HOUR]) {
		time += fields[HOUR];
		if (fields[AM_PM] == Calendar.PM) {
			time += 12;
		}
	}
	time *= 60;

	if (isSet[MINUTE]) {
		time += fields[MINUTE];
	}
	time *= 60;

	if (isSet[SECOND]) {
		time += fields[SECOND];
	}
	time *= 1000;

	if (isSet[MILLISECOND]) {
		time += fields[MILLISECOND];
	}

	if (isSet[ZONE_OFFSET]) {
		time += fields[ZONE_OFFSET];
	}
	
	return (time);
}

protected void computeFields()
	{
	// Calculate the calendar fields from the millisecond time.
	long rawoffset = getTimeZone().getRawOffset();
	computeFields(this.time + rawoffset);

	long offset = getTimeZone().getOffset(fields[ERA], fields[YEAR], fields[MONTH], fields[DAY_OF_MONTH], fields[DAY_OF_WEEK], fields[MILLISECOND]);

	// If we're in daylight saving, recompute based on this time
	if (offset != rawoffset) {
		computeFields(this.time + offset);
	}

	areFieldsSet = true;
}

// Calculate the calendar fields from the millisecond time.
private void computeFields(long time)
	{
	long rem = time;
	long days = rem / MILLISECSPERDAY;
	rem %= MILLISECSPERDAY;
	int hours = (int)(rem / MILLISECSPERHOUR);
	rem %= MILLISECSPERHOUR;
	int mins = (int)(rem / MILLISECSPERMIN);
	rem %= MILLISECSPERMIN;
	int secs = (int)(rem / MILLISECSPERSEC);
	int millis = (int)(rem % MILLISECSPERSEC);
	int wday = (int)((EPOCH_WDAY + days) % DAYSPERWEEK);
	int years = EPOCH_YEAR;
	long ydays = 0;
	int leap;
	for (;;) {
		leap = (isLeapYear(years) ? 1 : 0);
		if (days < 365 + leap) {
			break;
		}
		ydays += 365 + leap;
		days -= 365 + leap;
		years++;
	}
	long ymday = ydays;
	int months = Calendar.JANUARY;
	int yday = (int)days;
	for (;;) {
		long mdays;
		switch (months) {
		case Calendar.JANUARY:
		case Calendar.MARCH:
		case Calendar.MAY:
		case Calendar.JULY:
		case Calendar.AUGUST:
		case Calendar.OCTOBER:
		case Calendar.DECEMBER:
		default:
			mdays = 31;
			break;
		case Calendar.FEBRUARY:
			mdays = 28 + leap;
			break;
		case Calendar.APRIL:
		case Calendar.SEPTEMBER:
		case Calendar.JUNE:
		case Calendar.NOVEMBER:
			mdays = 30;
			break;
		}
		if (days < mdays) {
			break;
		}
		months++;
		days -= mdays;
		ymday += mdays;
	}

	if (years < 1) {
		set(Calendar.ERA, 0);
	}
	else {
		set(Calendar.ERA, 1);
	}
	set(Calendar.YEAR, years);
	set(Calendar.MONTH, months);
	set(Calendar.DAY_OF_MONTH, 1+(int)days);
	set(Calendar.DAY_OF_YEAR, yday);
	set(Calendar.DAY_OF_WEEK, wday + Calendar.SUNDAY);
	if (hours < 12) {
		set(Calendar.AM_PM, Calendar.AM);
		set(Calendar.HOUR, hours);
	}
	else {
		set(Calendar.AM_PM, Calendar.PM);
		set(Calendar.HOUR, hours - 12);
	}
	set(Calendar.HOUR_OF_DAY, hours);
	set(Calendar.MINUTE, mins);
	set(Calendar.SECOND, secs);
	set(Calendar.MILLISECOND, millis);

	int yearstartday = (int)((EPOCH_WDAY + ydays) % DAYSPERWEEK);
	int weekofyear = (yearstartday + yday) / DAYSPERWEEK;

	set(Calendar.WEEK_OF_YEAR, weekofyear);
	set(Calendar.DAY_OF_WEEK_IN_MONTH, 1 + (int)days / DAYSPERWEEK);

	int monthstartday = (int)((EPOCH_WDAY + ymday) % DAYSPERWEEK);
	int weekofmonth = (monthstartday + (int)days) / DAYSPERWEEK;

	set(Calendar.WEEK_OF_MONTH, weekofmonth);
}

protected void computeTime() {
	long offset = getTimeZone().getOffset(fields[ERA], fields[YEAR], fields[MONTH], fields[DAY_OF_MONTH], fields[DAY_OF_WEEK], fields[MILLISECOND]);
	time = computeDateTime() - offset;
	isTimeSet = true;
}

public boolean equals(Object obj)
	{
	if (obj instanceof GregorianCalendar) {
		GregorianCalendar cal = (GregorianCalendar)obj;
		if (isLenient() == cal.isLenient() &&
		    getFirstDayOfWeek() == cal.getFirstDayOfWeek() &&
		    getMinimalDaysInFirstWeek() == cal.getMinimalDaysInFirstWeek() &&
		    getTimeZone() == cal.getTimeZone() &&
		    getTime() == cal.getTime()) {
			return (true);
		}
	}
	return (false);
}

public int getGreatestMinimum(int field)
	{
	return (getMinimum(field));
}

final public Date getGregorianChange()
	{
	if (dateChange == null) {
		return (stdDateChange.getTime());
	}
	else {
		return (dateChange.getTime());
	}
}

public int getLeastMaximum(int field)
	{
	switch (field) {
	case DAY_OF_MONTH: // DATE
		return (28);
	default:
		return (getMaximum(field));
	}
}

public int getMaximum(int field)
	{
	switch (field) {
	case ERA:
		return (1);
	case YEAR:
		return (9999);
	case MONTH:
		return (DECEMBER);
	case WEEK_OF_YEAR:
		return (52);
	case WEEK_OF_MONTH:
		return (5);
	case DAY_OF_MONTH: // DATE
		return (31);
	case DAY_OF_YEAR:
		return (366);
	case DAY_OF_WEEK:
		return (7);
	case DAY_OF_WEEK_IN_MONTH:
		return (31);
	case AM_PM:
		return (0);
	case HOUR:
	case HOUR_OF_DAY:
		return (23);
	case MINUTE:
		return (59);
	case SECOND:
		return (59);
	case MILLISECOND:
		return (999);
	case ZONE_OFFSET:
		return (0);
	case DST_OFFSET:
		return (0);
	default:
		return (0);
	}
}

public int getMinimum(int field)
	{
	switch (field) {
	case ERA:
		return (0);
	case YEAR:
		return (0);
	case MONTH:
		return (JANUARY);
	case WEEK_OF_YEAR:
		return (1);
	case WEEK_OF_MONTH:
		return (1);
	case DAY_OF_MONTH: // DATE
		return (1);
	case DAY_OF_YEAR:
		return (1);
	case DAY_OF_WEEK:
	case DAY_OF_WEEK_IN_MONTH:
		return (1);
	case AM_PM:
		return (0);
	case HOUR:
	case HOUR_OF_DAY:
		return (0);
	case MINUTE:
		return (0);
	case SECOND:
		return (0);
	case MILLISECOND:
		return (0);
	case ZONE_OFFSET:
		return (0);
	case DST_OFFSET:
		return (0);
	default:
		return (0);
	}
}

public synchronized int hashCode()
	{
	return (getFirstDayOfWeek() ^ getMinimalDaysInFirstWeek());
}

public boolean isLeapYear(int year)
	{
	if (year % 400 == 0) {
		return (true);
	}
	if (year % 100 == 0) {
		return (false);
	}
	if (year % 4 == 0) {
		return (true);
	}
	return (false);
}

public void roll(int field, boolean up)
	{
	throw new NotImplemented(GregorianCalendar.class.getName() + ".roll(int,boolean)");
}

public void setGregorianChange(Date date)
	{
	dateChange = Calendar.getInstance();
	dateChange.setTime(date);
}
}
