/**
 * testPODCalculator.java
 * Created Oct 12, 2008 4:39:14 PM
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
import net.aa3sd.SMT.math.PODCalculator;
import junit.framework.TestCase;
/**
 * @author mole
 */
public class testPODCalculator extends TestCase {
  /**
   * @param name
   */
  public testPODCalculator(String name) {
		super(name);
  }

  /**
   * Test method for {@link net.aa3sd.SMT.math.PODCalculator#calcululateCumulativePOD(java.util.ArrayList)}.
   */
  public void testCalcululateCumulativePOD() {
		ArrayList searchesOfSameSegment = new ArrayList<Double>();
		searchesOfSameSegment.add(Double.valueOf(0.3d));
		searchesOfSameSegment.add(Double.valueOf(0.3d));
		searchesOfSameSegment.add(Double.valueOf(0.7d));
		try {
			assertEquals(0.853d, PODCalculator.calcululateCumulativePOD(searchesOfSameSegment));
		} catch (BadCalculationException e) {
			fail("Threw unexpected exception");
		}
		searchesOfSameSegment.clear();
		searchesOfSameSegment.add(Double.valueOf(0.05d));
		searchesOfSameSegment.add(Double.valueOf(0.05d));
		try {
			assertEquals(Math.round(1000000d * 0.0975d), Math.round(1000000d * PODCalculator.calcululateCumulativePOD(searchesOfSameSegment)));
			
		} catch (BadCalculationException e) {
			fail("Threw unexpected exception");
		}
		searchesOfSameSegment.clear();
		searchesOfSameSegment.add(Double.valueOf(0.5d));
		searchesOfSameSegment.add(Double.valueOf(0.25d));
		try {
			assertEquals(0.625d, PODCalculator.calcululateCumulativePOD(searchesOfSameSegment));
		} catch (BadCalculationException e) {
			fail("Threw unexpected exception");
		}
  }

  public void testCalculateCumulativePODExceptions() {
 
		ArrayList searchesOfSameSegment = new ArrayList<Double>();
		searchesOfSameSegment.add(Double.valueOf(0.3d));
		searchesOfSameSegment.add(Double.valueOf(-0.3d));
		searchesOfSameSegment.add(Double.valueOf(0.7d));
		try {
			assertEquals(0.85d, PODCalculator.calcululateCumulativePOD(searchesOfSameSegment));
			fail("Failed to throw exception on probability less than zero.");
		} catch (BadCalculationException e) {
			//pass("Threw expected exception");
		}
		searchesOfSameSegment.clear();
		searchesOfSameSegment.add(Double.valueOf(0.3d));
		searchesOfSameSegment.add(Double.valueOf(30d));
		searchesOfSameSegment.add(Double.valueOf(0.7d));
		try {
			assertEquals(0.85d, PODCalculator.calcululateCumulativePOD(searchesOfSameSegment));
			fail("Failed to throw exception on probability more than one.");
		} catch (BadCalculationException e) {
			//pass("Threw expected exception");
		}
  }

}
