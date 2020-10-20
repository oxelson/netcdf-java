/*
 * Copyright (c) 1998-2018 John Caron and University Corporation for Atmospheric Research/Unidata
 * See LICENSE for license information.
 */
package ucar.nc2.grid;

import ucar.nc2.AttributeContainer;
import ucar.nc2.constants.CDM;
import ucar.nc2.constants.CF;
import ucar.nc2.time.*;

import javax.annotation.concurrent.Immutable;

/** Helper class for Time coordinate axes */
@Immutable
public class TimeHelper {

  public static TimeHelper factory(String units, AttributeContainer atts) {
    if (units == null)
      units = atts.findAttributeString(CDM.UDUNITS, null);
    if (units == null)
      units = atts.findAttributeString(CDM.UNITS, null);
    if (units == null)
      throw new IllegalStateException("No units");

    Calendar cal = getCalendarFromAttribute(atts);
    CalendarDateUnit dateUnit;
    try {
      dateUnit = CalendarDateUnit.withCalendar(cal, units); // this will throw exception on failure
      return new TimeHelper(dateUnit);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  //////////////////////////////////////////////

  // final Calendar cal;
  final CalendarDateUnit dateUnit;
  // final CalendarDate refDate;
  // final double duration;

  private TimeHelper(CalendarDateUnit dateUnit) {
    // this.cal = cal;
    this.dateUnit = dateUnit;
    // this.refDate = dateUnit.getBaseCalendarDate();
    // this.duration = dateUnit.getTimeUnit().getValueInMillisecs();
  }

  // copy on modify
  public TimeHelper setReferenceDate(CalendarDate refDate) {
    CalendarDateUnit cdUnit = CalendarDateUnit.of(dateUnit.getCalendar(), dateUnit.getCalendarField(), refDate);
    return new TimeHelper(cdUnit);
  }

  public String getUdUnit() {
    return dateUnit.getUdUnit();
  }

  // get offset from runDate, in units of dateUnit
  public double offsetFromRefDate(CalendarDate date) {
    return dateUnit.makeOffsetFromRefDate(date);
  }

  public CalendarDate getRefDate() {
    return dateUnit.getBaseCalendarDate();
  }

  public CalendarDate makeDate(double value) {
    return dateUnit.makeCalendarDate(value);
  }

  public CalendarDateRange getDateRange(double startValue, double endValue) {
    CalendarDate start = makeDate(startValue);
    CalendarDate end = makeDate(endValue);
    return CalendarDateRange.of(start, end);
  }

  public double getOffsetInTimeUnits(CalendarDate start, CalendarDate end) {
    return dateUnit.getCalendarPeriod().getOffset(start, end);
  }

  public CalendarDate makeDateInTimeUnits(CalendarDate start, double addTo) {
    return start.add(CalendarPeriod.of((int) addTo, dateUnit.getCalendarField()));
  }

  public static Calendar getCalendarFromAttribute(AttributeContainer atts) {
    String cal = atts.findAttributeString(CF.CALENDAR, null);
    if (cal == null)
      return null;
    return Calendar.get(cal);
  }

  public Calendar getCalendar() {
    return dateUnit.getCalendar();
  }

  public CalendarDateUnit getCalendarDateUnit() {
    return dateUnit;
  }

  public CalendarDate makeCalendarDateFromOffset(double offset) {
    return dateUnit.makeCalendarDate(offset);
  }

  public CalendarDate makeCalendarDateFromOffset(String offset) {
    return CalendarDateFormatter.isoStringToCalendarDate(dateUnit.getCalendar(), offset);
  }

}
