/**
 * testApplyPOD.java
 * Created Oct 15, 2008 4:54:41 AM
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

import java.util.ArrayList;
import net.aa3sd.SMT.math.BadCalculationException;
import net.aa3sd.SMT.math.Utility;
import net.aa3sd.SMT.search.Search;
import net.aa3sd.SMT.search.Segment;
import junit.framework.TestCase;
/**
 * @author mole
 */
public class testApplyPOD extends TestCase {
  /**
   * @param name
   */
  public testApplyPOD(String name) {
		super(name);
  }

  /**
   * Test method for {@link net.aa3sd.SMT.search.Segment#applyPOD(double)}.
   */
  public testApplyPOD() {
		String name = "test";
		Search search = new Search();
		Segment one = new Segment(search, name, null, null, null);
		one.setCurrentPOA(0.4d);
		one.setName("one");
		Segment two = new Segment(search, name, null, null, null);
		two.setCurrentPOA(0.2d);
		two.setName("two");
		Segment three = new Segment(search, name, null, null, null);
		three.setCurrentPOA(0.1d);
		three.setName("three");
		Segment four = new Segment(search, name, null, null, null);
		four.setCurrentPOA(0.2d);
		four.setName("four");
		Segment row = new Segment(search, name, null, null, null);
		row.setCurrentPOA(0.1d);
		row.setName("ROW");
		try {
			one.applyPOD(0.60d);
		} catch (BadCalculationException e) {
			fail("threw unexpected BadCalculationException");
		}
		try {
			two.applyPOD(0.85d);
		} catch (BadCalculationException e) {
			fail("threw unexpected BadCalculationException");
		}
		try {
			three.applyPOD(0.90d);
		} catch (BadCalculationException e) {
			fail("threw unexpected BadCalculationException");
		}
		try {
			one.applyPOD(1.5d);
			fail("Failed to throw BadCalculationException for pod > 1");
		} catch (BadCalculationException e) {
			// pass
		}
		try {
			one.applyPOD(-1.5d);
			fail("Failed to throw BadCalculationException for pod < 0");
		} catch (BadCalculationException e) {
			// pass
		}
		ArrayList <Segment> segments = search.getSegments();
		int matches = 0;
		for (int x=0; x< segments.size(); x++) {
			if (segments.get(x).getName().equals("one")) {  
			   assertTrue(Utility.equalTenPlaces(0.32d, segments.get(0).getCurrentPOA()));
			   matches ++;
			}
			if (segments.get(x).getName().equals("two")) { 
			   assertTrue(Utility.equalTenPlaces(0.06d, segments.get(1).getCurrentPOA()));
			   matches ++;
			}
			if (segments.get(x).getName().equals("three")) { 
			   assertTrue(Utility.equalTenPlaces(0.02d, segments.get(2).getCurrentPOA()));
			   matches ++;
			}
			if (segments.get(x).getName().equals("four")) { 
			   assertTrue(Utility.equalTenPlaces(0.40d, segments.get(3).getCurrentPOA()));
			   matches ++;
			}
			if (segments.get(x).getName().equals("ROW")) { 
			   assertTrue(Utility.equalTenPlaces(0.20d, segments.get(4).getCurrentPOA()));
			   matches ++;
			}
		}
		assertEquals(5,matches);
		assertTrue(Utility.equalTenPlaces(0.32d, segments.get(0).getCurrentPOA()));
		assertTrue(Utility.equalTenPlaces(0.06d, segments.get(1).getCurrentPOA()));
		assertTrue(Utility.equalTenPlaces(0.02d, segments.get(2).getCurrentPOA()));
		assertTrue(Utility.equalTenPlaces(0.40d, segments.get(3).getCurrentPOA()));
		assertTrue(Utility.equalTenPlaces(0.20d, segments.get(4).getCurrentPOA()));
  }

}
