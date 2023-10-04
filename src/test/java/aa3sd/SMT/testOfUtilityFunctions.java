/**
 * testOfUtilityFunctions.java
 * Created Oct 15, 2008 7:06:49 AM
 * 
 * © Copyright 2008 Paul J. Morris 
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

import net.aa3sd.SMT.math.Utility;
import junit.framework.TestCase;
/**
 * @author mole
 */
public class testOfUtilityFunctions extends TestCase {
  /**
   * @param name
   */
  public testOfUtilityFunctions(String name) {
		super(name);
  }

  /**
   * Test method for {@link net.aa3sd.SMT.math.Utility#equalTenPlaces(double, double)}.
   */
  public void testEqualTenPlaces() {
		assertTrue(Utility.equalTenPlaces(1d,1d));
		assertTrue(Utility.equalTenPlaces(1d,1.000000000006d));
		assertFalse(Utility.equalTenPlaces(1d,1.0000006d));
		assertFalse(Utility.equalTenPlaces(1d,2d));
  }

}
