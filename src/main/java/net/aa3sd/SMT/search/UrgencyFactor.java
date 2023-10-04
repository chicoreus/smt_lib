/**
 * UrgencyFactor.java
 * Created Jan 8, 2010 6:07:17 PM
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
/**
 * @author mole
 */
public class UrgencyFactor {
  /**
   * 
   * The factor group for which this is a factor.
   * 
   */
  private UrgencyFactorGroup group;

  /**
   * A text description of this urgency factor.
   */
  private String name;

  /**
   * The smallest value in the range of values for this urgency factor. 
   * For example, for urgency group Age, the factor Very Old has a minumum
   * value of 1 (and a maximum value of 1, while the factor other than 
   * very old or very young has a minimum value of 2 and a maximum value of 3.
   * 
   * Ranges of factors within the same urgency group may overlap (thus factors 
   * may not be distinguishable by integer value), for example, in the factor 
   * group Age, both the factor Very Young and the factor Very Old can take
   * only values of 1, so a UrgencyFactorGroup.getSelectedValue() of 1 might
   * represent either a very young or a very old subject.   
   */
  private int minValue;

  /**
   * The largest value in the range of values for this urgency factor. 
   * For example, for urgency group Age, the factor Very Old has a maximum
   * value of 1 (and a minimum value of 1, while the factor other than 
   * very old or very young has a minimum value of 2 and a maximum value of 3.
   */
  private int maxValue;

  /**
   * @param name
   * @param value
   */
  public UrgencyFactor(String factorName, int value) {
 
		name = factorName;
		minValue = value;
		maxValue = value;
		//exclusive = true;
  }

  /**
   * @param name
   * @param minValue
   * @param maxValue
   */
  public UrgencyFactor(String name, int minValue, int maxValue) {
		super();
		this.name = name;
		if (minValue > maxValue) { 
			this.minValue = maxValue;
			this.maxValue = minValue;			
		} else { 
			this.minValue = minValue;
			this.maxValue = maxValue;
		} 
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
   * @return the minValue
   */
  public int getMinValue() {
		return minValue;
  }

  /**
   * @param minValue the minValue to set
   */
  public void setMinValue(int minValue) {
		this.minValue = minValue;
  }

  /**
   * @return the maxValue
   */
  public int getMaxValue() {
		return maxValue;
  }

  /**
   * @param maxValue the maxValue to set
   */
  public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
  }

  /**
   * @param group the group to set
   */
  public void setGroup(UrgencyFactorGroup group) {
		this.group = group;
  }

  /**
   * @return the group
   */
  public UrgencyFactorGroup getGroup() {
		return group;
  }

}
