/**
 *  Copyright (C) 2008 Paul J. Morris  
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.aa3sd.SMT.test;

import net.aa3sd.SMT.math.BadCalculationException;
import net.aa3sd.SMT.math.ResourceAllocation;
import junit.framework.Assert;
import junit.framework.TestCase;
/**
 * @author mole
 */
public class testResourceAllocation extends TestCase {
  /**
   * @param name
   */
  public testResourceAllocation(String name) {
		super(name);
  }

  /**
   *  (non-Javadoc)
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception {
		super.setUp();
  }

  /**
   *  (non-Javadoc)
   * @see junit.framework.TestCase#tearDown()
   */
  protected void tearDown() throws Exception {
		super.tearDown();
  }

  /**
   * Test method for {@link net.aa3sd.SMT.math.ResourceAllocation#ResourceAllocation()}.
   */
  public void testResourceAllocationConstructor() {
		ResourceAllocation r = new ResourceAllocation();
		// newly constructed resource allocation object should be in a calculable state 
		// and use reasonable defaults for the early stages of a ground search.
	    Assert.assertTrue(r.isCalculable());
	    Assert.assertTrue(r.isReasonable());
  }

  /**
   * Test method for {@link net.aa3sd.SMT.math.ResourceAllocation#isCalculable()}.
   */
  public void testIsCalculable() {
		ResourceAllocation r = new ResourceAllocation(); 
		r.setArea(0.0); 
		r.setHours(0.0);
		// with two or more zero values, resource allocation should not be in calculable state.
		Assert.assertFalse(r.isCalculable());
		try {
			r.setAllButArea(1d, 0.25, 1d, 1d);
		} catch (BadCalculationException e) {
			fail("Threw Unexpected BadCalculationException");
		}
		r.setArea(0.0); 
		Assert.assertTrue(r.isCalculable());
  }

  /**
   * Test method for {@link net.aa3sd.SMT.math.ResourceAllocation#calculate(int)}.
   */
  public void testCalculate() {
		ResourceAllocation r = new ResourceAllocation();
		
		// test throwing exceptions on setting bad values.
		r.setPeople(0d);
		try { 
			r.setAllButPeople(1d, 0.25, 0d, 0d); // two zero values should throw exception on calculate()
			Assert.fail("Failed to throw bad calculation exception");
		} catch (BadCalculationException e) {
			Assert.assertEquals(0.0, r.getPeople());  // requested calculation should not have run
		}
		try {
			r.calculate(ResourceAllocation.PEOPLE);
			Assert.fail("Failed to throw bad calculation exception");
		} catch (BadCalculationException e) {
			Assert.assertEquals(0.0, r.getPeople());  // requested calculation should not have run
		}
		
		// test formula and rounding down of people
		try {
			r.setAllButPeople(1, 0.25, 25d, 8d); //  (1 * 5280 * (1/.25)) / (25 * 8) = 105.600000
		} catch (BadCalculationException e1) {
			Assert.fail("Threw unexpected bad calculation exception");;
		}
		try {
			Assert.assertEquals(106d, r.calculate(ResourceAllocation.PEOPLE));
		} catch (BadCalculationException e) {
			Assert.fail("Threw unexpected bad calculation exception");
		}		
		// test rounding up of people
		try {
			r.setAllButPeople(1, 0.251, 25d, 8d);   //  (1 * 5280 * (1/.251)) / (25 * 8) = 105.179263
		} catch (BadCalculationException e1) {
			Assert.fail("Threw unexpected bad calculation exception");
		}
		try {
			Assert.assertEquals(106d, r.calculate(ResourceAllocation.PEOPLE));
		} catch (BadCalculationException e) {
			Assert.fail("Threw unexpected bad calculation exception");
		}
		
		// test formula for area
		try {
			r.setAllButArea(106d, 0.25, 25d, 8d);  // (25 * 8 * 106) / ( 5280 * (1/.25)) = 1.00378787  
		} catch (BadCalculationException e1) {
			Assert.fail("Threw unexpected bad calculation exception");
		}   
		try {
			Assert.assertEquals(1003788, Math.round(r.calculate(ResourceAllocation.AREA)*1000000));
		} catch (BadCalculationException e) {
			Assert.fail("Threw unexpected bad calculation exception");
		}
		
		// test hours
		//TODO:
		
		// test spacing
		//TODO:
		
		// test pace
		//TODO:
		
		// test series of sets
		//TODO:
		
  }

  /**
   * Test method for {@link net.aa3sd.SMT.math.ResourceAllocation#isReasonable()}.
   */
  public void testIsReasonable() {
		ResourceAllocation r = new ResourceAllocation();
		r.setArea(1);   // a set of values that should be independently reasonable
		try {
			r.setAllButArea(10d, 0.25, 25d, 8d);
		} catch (BadCalculationException e) {
			fail("Threw Unexpected BadCalculationException");
		}
		Assert.assertTrue(r.isReasonable());
		// make unreasonable one at a time
		r.setArea(ResourceAllocation.UPPER_REASONABLE_AREA_SQ_MILES + 10.0);   
		Assert.assertFalse(r.isReasonable());		
		r.setArea(1.0);   
		r.setHours(ResourceAllocation.UPPER_REASONABLE_TIME_HOURS + 10.0);
		Assert.assertFalse(r.isReasonable());
		try {
			r.setAllButArea(10d, 0.25, 25d, 8d);
		} catch (BadCalculationException e) {
			fail("Threw Unexpected BadCalculationException");
		}
		r.setPace(ResourceAllocation.UPPER_REASONABLE_PACE_PER_MILE + 10.0);
		Assert.assertFalse(r.isReasonable());
		try {
			r.setAllButArea(10d, 0.25, 25d, 8d);
		} catch (BadCalculationException e) {
			fail("Threw Unexpected BadCalculationException");
		}
		r.setSpacing(ResourceAllocation.UPPER_REASONABLE_SPACING_FEET + 10.0);
		Assert.assertFalse(r.isReasonable());
		try {
			r.setAllButArea(10d, 0.25, 25d, 8d);
		} catch (BadCalculationException e) {
			fail("Threw Unexpected BadCalculationException");
		}
		r.setPeople(ResourceAllocation.UPPER_REASONABLE_PEOPLE + 10.0);
		Assert.assertFalse(r.isReasonable());
  }

}
