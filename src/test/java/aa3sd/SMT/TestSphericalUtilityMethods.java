/**
 * TestSphericalUtilityMethods.java
 * Created Nov 2, 2011 6:10:14 PM
 * 
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
package net.aa3sd.SMT.test;

import junit.framework.TestCase;
import net.aa3sd.SMT.geometry.Vector;
import net.aa3sd.SMT.math.SphericalUtility;
import org.apache.log4j.Logger;
/**
 * @author Paul J. Morris
 */
public class TestSphericalUtilityMethods extends TestCase {
  private static final Logger log =  Logger
			.getLogger(TestSphericalUtilityMethods.class);

  private static final double METERS_PER_DEGREE =  111194d;

  public void testVectorEquality() {
 
		Vector v1 = new Vector();
		Vector v2 = new Vector();
		assertTrue(v1.equals(v2));
		v2.setBearing(1d);
		assertFalse(v1.equals(v2));
		v1.setBearing(1d);
		assertTrue(v1.equals(v2));
		v2.setDistance(1d);
		assertFalse(v1.equals(v2));
		v1.setDistance(1d);
		assertTrue(v1.equals(v2));
  }

  /**
   * Test method for {@link net.aa3sd.SMT.math.SphericalUtility#calc_distance_haversine_radian(double, double, double, double)}.
   */
  public void testCalc_distance_haversine_radian() {
		// Test to precision of 1 meter.
		// one degree longitude at the equator is 111,194 meters
	    assertEquals((int)METERS_PER_DEGREE, (int) SphericalUtility.calc_distance_haversine_radian(Math.toRadians(0), Math.toRadians(0), Math.toRadians(0), Math.toRadians(1)));
	    assertEquals((int)METERS_PER_DEGREE, (int) SphericalUtility.calc_distance_haversine_radian(Math.toRadians(0), Math.toRadians(359), Math.toRadians(0), Math.toRadians(0)));
	    for (int d=0; d<360; d++) {
	          assertEquals((int)METERS_PER_DEGREE, (int) SphericalUtility.calc_distance_haversine_radian(Math.toRadians(0), Math.toRadians(d), Math.toRadians(0), Math.toRadians(d+1)));
	    }
	    // One degree of latitude is a constant 111,194 meters 
	    assertEquals((int)METERS_PER_DEGREE, (int) SphericalUtility.calc_distance_haversine_radian(Math.toRadians(0), Math.toRadians(0), Math.toRadians(1), Math.toRadians(0)));
	    for (int d=-90; d<90; d++) {
	          assertEquals((int)METERS_PER_DEGREE, (int) SphericalUtility.calc_distance_haversine_radian(Math.toRadians(d), Math.toRadians(0), Math.toRadians(d+1), Math.toRadians(0)));
	    }
	    
		//fail("Not yet implemented");
  }

  /**
   * Test method for {@link net.aa3sd.SMT.math.SphericalUtility#computeAreaForGeometry(com.vividsolutions.jts.geom.Polygon)}.
   */
  public void testComputeAreaForGeometry() {
		fail("Not yet implemented");
  }

  /**
   * Test method for {@link net.aa3sd.SMT.math.SphericalUtility#computeAreaForClosedLineString(com.vividsolutions.jts.geom.LineString)}.
   */
  public void testComputeAreaForClosedLineString() {
		fail("Not yet implemented");
  }

  /**
   * Test method for {@link net.aa3sd.SMT.math.SphericalUtility#calc_distance_course(long, long, long, long, net.aa3sd.SMT.geometry.Vector)}.
   */
  public void testCalc_distance_course() {
		// Bearing returned is back bearing from point 2 to point 1 (thus 270 here).  
		Vector result = new Vector(METERS_PER_DEGREE,270d);
		assertFalse(result.equals(SphericalUtility.calc_distance_course(0l,0l,0l,0l)));
		assertTrue(result.equals(SphericalUtility.calc_distance_course(0l,0l,0l,1l)));
  }

}
