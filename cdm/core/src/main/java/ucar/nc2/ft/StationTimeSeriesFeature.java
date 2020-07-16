/*
 * Copyright (c) 1998-2018 John Caron and University Corporation for Atmospheric Research/Unidata
 * See LICENSE for license information.
 */
package ucar.nc2.ft;

import ucar.nc2.ft.point.StationFeature;
import ucar.nc2.time.CalendarDateRange;

/**
 * Time series of PointFeature at a named location aka 'station'.
 *
 * @author caron
 */
public interface StationTimeSeriesFeature extends StationFeature, PointFeatureCollection {

  /**
   * The number of points in the time series. May not be known until after iterating through the collection.
   * 
   * @return number of points in the time series, or -1 if not known.
   */
  int size();

  /**
   * Subset this collection by calendar dateRange
   * 
   * @param dateRange only points in this date range. may be null.
   * @return subsetted collection, may be null if empty
   */
  StationTimeSeriesFeature subset(CalendarDateRange dateRange);
}
