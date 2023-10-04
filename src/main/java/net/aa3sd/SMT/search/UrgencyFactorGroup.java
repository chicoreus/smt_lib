/**
 * UrgencyFactorGroup.java
 * Created Jan 8, 2010 6:16:59 PM
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
package net.aa3sd.SMT.search;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.aa3sd.SMT.exceptions.BadValueException;
/**
 * @author mole
 */
public class UrgencyFactorGroup {
  /**
   *  The list of factors in this group, e.g. for FactorGroup=Age, factors might be very young, very old, and other.
   */
  private List<UrgencyFactor> factors;

  /**
   *  The name of this factor group.
   */
  private String name;

  /**
   * A set consisting of all values that are valid (complete) values for the selectedValue of this factor group. 
   */
  private Set<Integer> validValues;

  /**
   *  The value set for this factor group in a particular urgency calculation, e.g. "2".
   */
  private int selectedValue;

  /**
   *  Free text note about the value set for this factor group in a particular urgency calculation,
   * e.g.  "38y/o male".   Might provide a basis for the selected value.
   */
  private String selectionNote;

  /**
   * @param factors
   * @param name
   */
  public UrgencyFactorGroup(List<UrgencyFactor> factors, String name, Set<Integer> setOfValidValues) {
		super();
		setValidValues(setOfValidValues);
		this.factors = factors;
		this.name = name;
		Iterator<UrgencyFactor> i = this.factors.iterator();
		while (i.hasNext()) { 
			i.next().setGroup(this);
		}
		selectedValue = 0;
  }

  /**
   * @return the factors
   */
  public List<UrgencyFactor> getFactors() {
		return factors;
  }

  /**
   * @param factors the factors to set
   */
  public void setFactors(List<UrgencyFactor> factors) {
		this.factors = factors;
  }

  /**
   * @return the name
   */
  public String getName() {
		return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
		this.name = name;
  }

  /**
   *  Set the selected value of the urgency for this group of urgency factors.  
   * Accepts zero or a value between the minumum value for the factors in this group 
   * and the maximum value for the factors in this group (inclusive). 
   *  
   * @param value the value to set
   * @throws BadValueException 
   */
  public void setSelectedValue(int value) throws net.aa3sd.SMT.exceptions.BadValueException {
		int min = 0;
		int max = 0;
		Iterator<UrgencyFactor> i = factors.iterator();
		while (i.hasNext()) { 
			UrgencyFactor factor = i.next();
			if (factor.getMaxValue() > max) { max = factor.getMaxValue();  }
			if (factor.getMinValue() < min) { min = factor.getMinValue();  }
		}
		if (isValidValue(value)) { 
		    this.selectedValue = value;
		} else { 
			throw new BadValueException("Value " + value + " is outside allowed range of " + min + " to "+  max + ".");
		}
  }

  /**
   * @return the value that has been set for the urgency in this group of urgency factors.
   */
  public int getSelectedValue() {
		return selectedValue;
  }

  /**
   *  Find out the maximum value to which setValue() can accept.  
   * 
   * @return the maximum allowed value for the urgency factors in this group.
   */
  public int getMaxValue() {
 
		int result = 0;
		Iterator<UrgencyFactor> i = factors.iterator();
		while (i.hasNext()) { 
			UrgencyFactor factor = i.next();
			if (factor.getMaxValue() > result) { result = factor.getMaxValue();  }
		}		
		return result;
  }

  /**
   * @return the selectionNote
   */
  public String getSelectionNote() {
		return selectionNote;
  }

  /**
   * @param selectionNote the selectionNote to set
   */
  public void setSelectionNote(String selectionNote) {
		this.selectionNote = selectionNote;
  }

  /**
   * @param validValues the validValues to set
   */
  public void setValidValues(Set validValues) {
		this.validValues = validValues;
  }

  /**
   * @return the validValues
   */
  public Set getValidValues() {
		return validValues;
  }

  public boolean isValidValue(int valueToTest) {
 
		return (getValidValues().contains(Integer.valueOf(valueToTest)));
  }

}
