/**
 * SphericalUtility.java
 * Created Nov 2, 2011 5:44:17 PM
 * 
 * Copyright (C) 1999,2000  Frank Giannandrea
 * Copyright (C) 2000-2011  The Xastir Group 
 * © Copyright 2011 Paul J. Morris 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.aa3sd.SMT.math;

import net.aa3sd.SMT.geometry.Vector;
import org.apache.log4j.Logger;
import org.python.modules.math;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;
/**
 * Functions modified from XASTIR c code to support calculations of area and 
 * distance on the surface of a sphere.
 * 
 * @author The XASTIR group
 * @author Paul J. Morris
 */
public class SphericalUtility {
  private static final Logger log =  Logger.getLogger(SphericalUtility.class);

  /**
   *  XASTIR value = GRS80 value for the equ	atorial radius of the earth                                  
   *  public static final double EARTH_RADIUS_METERS = 6378138.0;
   *  IERS:  6378136.3- Equatorial Radius of the Earth
   *  Mean equatorial radius = 6,378,160.0  Wikipedia/Australian Geodetic Datum
   *  
   * Mean radius of the Earth in meters, from the CRC.
   * 
   *  Mean radius, from CRC
   */
  public static final double EARTH_RADIUS_METERS =  6370949.0;

  /**
   * Given a polygon (jts Polygon object) representing an area on the surface of the earth, 
   * return its area in square meters.
   * 
   * Combines the calculation of area and removal of holes from Polygon.getArea() with 
   * Greene's Theorem calculation of area from XASTIR CAD_object_compute_area().
   * 
   * @param geometry
   * @return area of the geometry in square meters.
   */
  public static double computeAreaForGeometry(Polygon polygon)
  {
 
		double result = 0;

		LineString shell = polygon.getExteriorRing();
		// get area of shell
		double shellArea = computeAreaForClosedLineString(shell);

		// for each hole in the shell, remove hole from shell.
		int holeCount = polygon.getNumInteriorRing();
		for (int i=0; i<holeCount; i++) { 
			LineString hole = polygon.getInteriorRingN(i);
			// get area of hole
			double holeArea = computeAreaForClosedLineString(hole);
			// subtract from area of shell
			shellArea = shellArea - holeArea;
		}

		result = shellArea;
		return result;
  }

  /**
   * Compute the area enclosed by a LineStrinng.  
   * Check that it is a closed, non-intersecting polygon first.
   * 
   * Modified from XASTIR objects.c  CAD_object_compute_area() 
   * function.
   * 
   * @param polygon  
   * @return area in square meters
   */
  public static double computeAreaForClosedLineString(LineString polygon)
  {
 
		double area = 0d;
	    // Walk the linked list, computing the area of the
	    // polygon.  Greene's Theorem is how we can compute the area of
	    // a polygon using the vertices.  We could also compute whether
	    // we're going clockwise or counter-clockwise around the polygon
	    // using Greene's Theorem. 
		
		com.vividsolutions.jts.geom.Coordinate[] coordinates = polygon.getCoordinates();
		for (int i=0; i<coordinates.length; i++) {
            // Because lat/long units can vary drastically w.r.t. real
            // units, we need to multiply the terms by the real units in
            // order to get real area.

            // Compute real distances from a fixed point.  Convert to
            // the current measurement units.  We'll use the starting
            // vertex (coordinates[0]) as our fixed point.
            Vector dx0 = SphericalUtility.calc_distance_course(
                    coordinates[0].x,
                    coordinates[0].y,
                    coordinates[0].x,
                    coordinates[i].y);
            
            if (coordinates[i].y < coordinates[0].y)
                dx0.setDistance( -dx0.getDistance() );

            Vector dy0 = SphericalUtility.calc_distance_course(
                    coordinates[0].x,
                    coordinates[0].y,
                    coordinates[i].x,
                    coordinates[0].y);

            if (coordinates[i].x < coordinates[0].x)
                dx0.setDistance( -dx0.getDistance() );
            

            int next = i+1;
            if (next>=coordinates.length) { 
            	next = 0;
            }
            Vector dx1 = SphericalUtility.calc_distance_course(
                    coordinates[0].x,
                    coordinates[0].y,
                    coordinates[0].x,
                    coordinates[next].y);

            if (coordinates[next].y < coordinates[0].y)
                dx0.setDistance( -dx0.getDistance() );


            Vector dy1 = SphericalUtility.calc_distance_course(
                    coordinates[0].x,
                    coordinates[0].y,
                    coordinates[next].x,
                    coordinates[0].y);


            // Add the minus signs back in, if any
            if (coordinates[i].y < coordinates[0].y)
                dx0.setDistance( -dx0.getDistance() );
            if (coordinates[i].x < coordinates[0].x)
                dy0.setDistance( -dy0.getDistance() );
            if (coordinates[next].y < coordinates[0].y)
                dx1.setDistance( -dx1.getDistance() );
            if (coordinates[next].x < coordinates[0].x)
                dy1.setDistance( -dy1.getDistance() );


            // Greene's Theorem:  Summation of the following, then
            // divide by two:
            //
            // A = X Y    - X   Y
            //  i   i i+1    i+1 i
            //
            area += (dx0.getDistance() * dy1.getDistance()) - (dx1.getDistance() * dy0.getDistance());
		}
        area = 0.5 * area; 
        
	    if (area < 0.0)
	        area = -area;
	    
		return area;
  }

  /**
   * Area calculation from XASTIR objects.c   
   *  
   * // Compute the area enclosed by a CAD object.  Check that it is a
   * // closed, non-intersecting polygon first.
   * //
   * double CAD_object_compute_area(CADRow *CAD_list_head) {
   * VerticeRow *tmp;
   * double area;
   * char temp_course[20];
   * //
   * area = 0.0;
   * tmp = CAD_list_head->start;
   * if (is_CAD_object_open(CAD_list_head)==0) {
   * // Only compute the area if CAD object is a closed polygon,
   * // that is, not an open polygon.
   * while (tmp->next != NULL) {
   * double dx0, dy0, dx1, dy1;
   * 
   * 
   * // Because lat/long units can vary drastically w.r.t. real
   * // units, we need to multiply the terms by the real units in
   * // order to get real area.
   * 
   * 
   * // Compute real distances from a fixed point.  Convert to
   * // the current measurement units.  We'll use the starting
   * // vertice as our fixed point.
   * //
   * dx0 = calc_distance_course(
   * CAD_list_head->start->latitude,
   * CAD_list_head->start->longitude,
   * CAD_list_head->start->latitude,
   * tmp->longitude,
   * temp_course,
   * sizeof(temp_course));
   * 
   * 
   * if (tmp->longitude < CAD_list_head->start->longitude)
   * dx0 = -dx0;
   * 
   * dy0 = calc_distance_course(
   * CAD_list_head->start->latitude,
   * CAD_list_head->start->longitude,
   * tmp->latitude,
   * CAD_list_head->start->longitude,
   * temp_course,
   * sizeof(temp_course));
   * 
   * 
   * if (tmp->latitude < CAD_list_head->start->latitude)
   * dx0 = -dx0;
   * 
   * 
   * dx1 = calc_distance_course(
   * CAD_list_head->start->latitude,
   * CAD_list_head->start->longitude,
   * CAD_list_head->start->latitude,
   * tmp->next->longitude,
   * temp_course,
   * sizeof(temp_course));
   * 
   * 
   * if (tmp->next->longitude < CAD_list_head->start->longitude)
   * dx0 = -dx0;
   * 
   * 
   * dy1 = calc_distance_course(
   * CAD_list_head->start->latitude,
   * CAD_list_head->start->longitude,
   * tmp->next->latitude,
   * CAD_list_head->start->longitude,
   * temp_course,
   * sizeof(temp_course));
   * 
   * 
   * // Add the minus signs back in, if any
   * if (tmp->longitude < CAD_list_head->start->longitude)
   * dx0 = -dx0;
   * if (tmp->latitude < CAD_list_head->start->latitude)
   * dy0 = -dy0;
   * if (tmp->next->longitude < CAD_list_head->start->longitude)
   * dx1 = -dx1;
   * if (tmp->next->latitude < CAD_list_head->start->latitude)
   * dy1 = -dy1;
   * 
   * 
   * // Greene's Theorem:  Summation of the following, then
   * // divide by two:
   * //
   * // A = X Y    - X   Y
   * //  i   i i+1    i+1 i
   * //
   * area += (dx0 * dy1) - (dx1 * dy0);
   * 
   * 
   * tmp = tmp->next;
   * }
   * area = 0.5 * area;
   * }
   * 
   * 
   * if (area < 0.0)
   * area = -area;
   * 
   * 
   * return area;
   * 
   * 
   * 
   * }
   * 
   * 
   * 
   * 
   * 
   * 
   * /**
   *  Calculate distance between two locations in nautical miles and course from loc2 to loc1
   * 
   *  What type of calculation is this, Rhumb Line distance or Great
   *  Circle distance?
   *  Answer:  "Law of Cosines for Spherical Trigonometry", which is a
   *  great-circle calculation, or Haversine, also a great-circle
   *  calculation.
   * 
   *  NOTE:  The angle returned is a separate calculation, but using
   *  the unit sphere distance in its calculation.  A great circle
   *  bearing is computed, not a Rhumb-line bearing.
   * 
   * @param lat1 latitude of point 1 in degrees
   * @param lon1 longitude of point 1 in degrees
   * @param lat2 latitude of point 2 in degrees
   * @param lon2 longitude of point 2 in degrees
   * 
   * @return Vector of the great circle distance between point 1 and point 2, with the
   * distance in meters and the bearing in degrees from point 2 to point 1.
   */
  public static net.aa3sd.SMT.geometry.Vector calc_distance_course(double lat1, double lon1, double lat2, double lon2)
  {
		Vector result = new Vector();

		double r_lat1 = Math.toRadians(lat1);
		double r_lon1 = Math.toRadians(lon1);
		double r_lat2 = Math.toRadians(lat2);
		double r_lon2 = Math.toRadians(lon2);


		/* Compute the distance.  We have a choice between using Law of
		 * Cosines or Haversine Formula here.   However, the Law of Cosines
		 * for Spherical Trigonometry is unreliable for small distances 
		 * because the inverse cosine is ill-conditioned.  
		 * A computer carrying seven significant digits can't distinguish
		 *  the cosines of distances smaller than about one minute of arc.
		 *  So we'll use the Haversine Formula.  
		 *  calc_distance_haversine_radian() returns the answer in meters. */
		double r_m = calc_distance_haversine_radian(r_lat1, r_lon1, r_lat2, r_lon2);
		result.setDistance(r_m);

		// Convert from distance in meters back to unit sphere.
		// This is needed for the course calculation below.
		double r_d = r_m / EARTH_RADIUS_METERS;

		// Compute the great-circle bearing
		double r_c = 0d;
		if (Math.cos(r_lat1) < 0.0000000001) {
			if (r_lat1>0.0)
				r_c=Math.PI;
			else
				r_c=0.0;
		} else {
			if (Math.sin((r_lon2-r_lon1))<0.0)
				r_c = Math.acos((Math.sin(r_lat2)-Math.sin(r_lat1)*Math.cos(r_d))/(Math.sin(r_d)*Math.cos(r_lat1)));
			else
				r_c = (2*Math.PI) - Math.acos((Math.sin(r_lat2)-Math.sin(r_lat1)*Math.cos(r_d))/(Math.sin(r_d)*Math.cos(r_lat1)));
		}

		// Set the course
		result.setBearing((180d/Math.PI)*r_c);

		// Return great circle distance and great circle bearing. 
		return result;   
  }

  public static double calc_distance_haversine_degree(double lat1, double lon1, double lat2, double lon2)
  {
 
		return calc_distance_haversine_radian(Math.toRadians(lat1),Math.toRadians(lon1),Math.toRadians(lat2),Math.toRadians(lon2));
  }

  /**
   * Distance calculation (Great Circle) using the Haversine formula
   * (2-parameter arctan version), which gives better accuracy than
   * the "Law of Cosines" for short distances.  It should be
   * equivalent to the "Law of Cosines for Spherical Trigonometry" for
   * longer distances.  Haversine is a great-circle calculation.
   * 
   * Modified from XASTIR util.c function calc_distance_haversine_radian
   * 
   * Desired accuracy is at least 1.11 meters, which is 0.00001 degrees at the equator.
   * Assumes distance is calculated on a sphere (does not model shape of Earth).
   * 
   * Inputs:  lat1/long1/lat2/long2 in radians (double)
   * 
   * Outputs: Distance in meters between them (double)
   */
  public static double calc_distance_haversine_radian(double lat1radian, double lon1radian, double lat2radian, double lon2radian)
  {
		double result = 0d;

		double longDelta = lon2radian - lon1radian;  
		double latDelta = lat2radian - lat1radian; 
		double radius = SphericalUtility.EARTH_RADIUS_METERS;
		double a = Math.pow((Math.sin(latDelta/2.0)),2d) + Math.cos(lat1radian) * Math.cos(lat2radian) * Math.pow((Math.sin(longDelta/2.0)),2d);
		double c = 2.0d * Math.atan2(Math.sqrt(a), Math.sqrt(1.0-a));
		result = radius * c;

		/* 
	    double dlon, dlat;
	    double a, c, d;
	    double R = EARTH_RADIUS_METERS;
	    #define square(x) (x)*(x)


	    dlon = lon2 - lon1;
	    dlat = lat2 - lat1;
	    a = square((sin(dlat/2.0))) + cos(lat1) * cos(lat2) * square((sin(dlon/2.0)));
	    c = 2.0 * atan2(sqrt(a), sqrt(1.0-a));
	    d = R * c;

	    return(d);
		 */
		return result;
  }

}
