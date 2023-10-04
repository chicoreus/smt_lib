/**
 * TestUrgencyFactorGroup.java
 * Created Jan 22, 2010 8:12:54 PM
 * 
 * © Copyright 2010 Paul J. Morris 
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
import java.util.HashSet;
import net.aa3sd.SMT.exceptions.BadValueException;
import net.aa3sd.SMT.search.UrgencyFactor;
import net.aa3sd.SMT.search.UrgencyFactorGroup;
import junit.framework.TestCase;
/**
 * @author mole
 */
public class TestUrgencyFactorGroup extends TestCase {
  /**
   * Test method for {@link net.aa3sd.SMT.search.UrgencyFactorGroup#UrgencyFactorGroup(java.util.List, java.lang.String, java.util.Set)}.
   */
  public void testUrgencyFactorGroup() {
		ArrayList<UrgencyFactorGroup >factorGroups = new ArrayList<UrgencyFactorGroup>();
		HashSet validValues1to3 = new HashSet();
		validValues1to3.add(Integer.valueOf(1));
		validValues1to3.add(Integer.valueOf(2));
		validValues1to3.add(Integer.valueOf(3));		
		UrgencyFactor uf1 = new UrgencyFactor("Very Young",1);
		UrgencyFactor uf2 = new UrgencyFactor("Very Old", 1);
		UrgencyFactor uf3 = new UrgencyFactor("Other",2,3);
		ArrayList<UrgencyFactor> f1 = new ArrayList<UrgencyFactor>();
		f1.add(uf1);
		f1.add(uf2);
		f1.add(uf3);
		UrgencyFactorGroup age = new UrgencyFactorGroup(f1, "Age",validValues1to3);
		factorGroups.add(age);
		
		assertEquals("Age", age.getName());
		assertEquals(3,age.getMaxValue());
		assertTrue(age.isValidValue(1));
		assertTrue(age.isValidValue(2));
		assertTrue(age.isValidValue(3));
		assertFalse(age.isValidValue(0));

  }

  /**
   * Test method for {@link net.aa3sd.SMT.search.UrgencyFactorGroup#setSelectedValue(int)}.
   */
  public void testSetSelectedValue() {
		ArrayList<UrgencyFactorGroup >factorGroups = new ArrayList<UrgencyFactorGroup>();
		HashSet validValues1to3 = new HashSet();
		validValues1to3.add(Integer.valueOf(1));
		validValues1to3.add(Integer.valueOf(2));
		validValues1to3.add(Integer.valueOf(3));		
		UrgencyFactor uf1 = new UrgencyFactor("Very Young",1);
		UrgencyFactor uf2 = new UrgencyFactor("Very Old", 1);
		UrgencyFactor uf3 = new UrgencyFactor("Other",2,3);
		ArrayList<UrgencyFactor> f1 = new ArrayList<UrgencyFactor>();
		f1.add(uf1);
		f1.add(uf2);
		f1.add(uf3);
		UrgencyFactorGroup age = new UrgencyFactorGroup(f1, "Age",validValues1to3);
		factorGroups.add(age);
		
		try {
			age.setSelectedValue(1);
		} catch (BadValueException e) {
			fail("Threw unexpected exception on setting selected value to a valid value.");
		}

		try {
			age.setSelectedValue(2);
		} catch (BadValueException e) {
			fail("Threw unexpected exception on setting selected value to a valid value.");
		}	
		
		try {
			age.setSelectedValue(3);
		} catch (BadValueException e) {
			fail("Threw unexpected exception on setting selected value to a valid value.");
		}		
		
		try {
			age.setSelectedValue(0);
			fail("Failed to throw exception on setting selected value to an invalid value.");
		} catch (BadValueException e) {
			// pass
		}		

		try {
			age.setSelectedValue(4);
			fail("Failed to throw exception on setting selected value to an invalid value.");
		} catch (BadValueException e) {
			// pass
		}
		
		try {
			age.setSelectedValue(Integer.MAX_VALUE);
			fail("Failed to throw exception on setting selected value to an invalid value.");
		} catch (BadValueException e) {
			// pass
		}
		
		try {
			age.setSelectedValue(Integer.MIN_VALUE);
			fail("Failed to throw exception on setting selected value to an invalid value.");
		} catch (BadValueException e) {
			// pass
		}			
		
  }

  /**
   * Test method for {@link net.aa3sd.SMT.search.UrgencyFactorGroup#getValidValues()}.
   */
  public void testGetValidValues() {
		ArrayList<UrgencyFactorGroup >factorGroups = new ArrayList<UrgencyFactorGroup>();
		HashSet validValues1to3 = new HashSet();
		validValues1to3.add(Integer.valueOf(1));
		validValues1to3.add(Integer.valueOf(2));
		validValues1to3.add(Integer.valueOf(3));		
		UrgencyFactor uf1 = new UrgencyFactor("Very Young",1);
		UrgencyFactor uf2 = new UrgencyFactor("Very Old", 1);
		UrgencyFactor uf3 = new UrgencyFactor("Other",2,3);
		ArrayList<UrgencyFactor> f1 = new ArrayList<UrgencyFactor>();
		f1.add(uf1);
		f1.add(uf2);
		f1.add(uf3);
		UrgencyFactorGroup age = new UrgencyFactorGroup(f1, "Age",validValues1to3);
		factorGroups.add(age);
		 
		assertEquals(validValues1to3,age.getValidValues());
  }

  /**
   * Test method for {@link net.aa3sd.SMT.search.UrgencyFactorGroup#isValidValue(int)}.
   */
  public void testIsValidValue() {
		ArrayList<UrgencyFactorGroup >factorGroups = new ArrayList<UrgencyFactorGroup>();
		HashSet validValues1to3 = new HashSet();
		validValues1to3.add(Integer.valueOf(1));
		validValues1to3.add(Integer.valueOf(2));
		validValues1to3.add(Integer.valueOf(3));		
		UrgencyFactor uf1 = new UrgencyFactor("Very Young",1);
		UrgencyFactor uf2 = new UrgencyFactor("Very Old", 1);
		UrgencyFactor uf3 = new UrgencyFactor("Other",2,3);
		ArrayList<UrgencyFactor> f1 = new ArrayList<UrgencyFactor>();
		f1.add(uf1);
		f1.add(uf2);
		f1.add(uf3);
		UrgencyFactorGroup age = new UrgencyFactorGroup(f1, "Age",validValues1to3);
		factorGroups.add(age);
		
		assertTrue(age.isValidValue(1));
		assertTrue(age.isValidValue(2));
		assertTrue(age.isValidValue(3));
		
		assertFalse(age.isValidValue(0));
		assertFalse(age.isValidValue(4));
		assertFalse(age.isValidValue(Integer.MAX_VALUE));
		assertFalse(age.isValidValue(Integer.MIN_VALUE));
		assertFalse(age.isValidValue(Integer.MAX_VALUE + 1));
  }

}
