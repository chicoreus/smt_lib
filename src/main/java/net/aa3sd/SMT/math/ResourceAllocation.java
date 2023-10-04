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
package net.aa3sd.SMT.math;

import java.util.ArrayList;
import net.aa3sd.SMT.SMT;
import net.aa3sd.SMT.interfaces.ResourceAllocationModelInterface;
import net.aa3sd.SMT.interfaces.ResourceAllocationObservable;
import net.aa3sd.SMT.interfaces.ResourceAllocationObserver;
/**
 * @author mole
 * 
 * Performs calculations for ground searcher resources needed for grid searches using the 
 * "Grid Search Planning Formulas" in figure 15.5, page 222 of Hill and O'Connor eds, 2007, 
 * Managing the Lost Person Incident, NASAR, Centreville, VA. 
 */
public class ResourceAllocation implements net.aa3sd.SMT.interfaces.ResourceAllocationModelInterface, net.aa3sd.SMT.interfaces.ResourceAllocationObservable {
  public static final double BASE_PACE_PER_MILE =  0.286;

  public static final double BASE_SPACING_FEET =  25.0;

  public static final int AREA =  0;

  public static final int PEOPLE =  1;

  public static final int PACE =  2;

  public static final int SPACING =  3;

  public static final int TIME =  4;

  public static final String UNIT_SQUAREMILES =  "mile²";

  public static final String UNIT_ACRES =  "acres";

  public static final String UNIT_SQUAREKM =  "km²";

  public static final double UPPER_REASONABLE_PACE_PER_MILE =  0.50;

  public static final double UPPER_REASONABLE_AREA_SQ_MILES =  28.3;

  public static final double UPPER_REASONABLE_SPACING_FEET =  100;

  public static final double UPPER_REASONABLE_TIME_HOURS =  96;

  public static final double UPPER_REASONABLE_PEOPLE =  400;

  private double area;

  private double people;

  private double pace;

  private double spacing;

  private double time;

  private ArrayList<net.aa3sd.SMT.interfaces.ResourceAllocationObserver> observers;

  /**
   * @param args
   */
  public static void main(String[] args)
  {
		// TODO Auto-generated method stub
        
  }

  public ResourceAllocation() {
        this.area = 0.1;
        this.people = 0;
        this.pace = BASE_PACE_PER_MILE;
        this.time = 8;
        this.spacing = BASE_SPACING_FEET;
        observers = new ArrayList<ResourceAllocationObserver>();
  }

  public boolean isCalculable() {
 
		boolean returnvalue = true;
		int counter = 0;
		
		if (area <= 0.0) { counter++; } 
		if (people<=0.0) { counter++; } 
		if (pace<=0.0)   { counter++; } 
		if (time<=0.0)   { counter++; } 
		if (spacing<=0)  { counter++; } 
 		
		if (counter > 1) { returnvalue = false; } 
		return returnvalue;
  }

  /**
   * @return the area in square miles
   */
  public double getArea() {
		return area;
  }

  /**
   * @return the area in acres
   */
  public double getAreaAcres() {
		return UnitConverter.squareMilesToAcres(area);
  }

  /**
   * @param area the area to set
   */
  public void setArea(double anArea) {
		setArea(anArea, PEOPLE);
  }

  /**
   * @param area the area to set
   */
  public void setArea(double area, String units) {
        if (units.equals(UNIT_SQUAREMILES)) { 
		   setArea(area);
		}
        if (units.equals(UNIT_ACRES)) {
           area = UnitConverter.acresToSquareMiles(area);
		   setArea(area);
 		}
  }

  /**
   * @return the people
   */
  public double getPeople() {
		return people;
  }

  /**
   * @param people the people to set
   */
  public void setPeople(double people) {
		setPeople(people, AREA);
  }

  /**
   * @return the pace
   */
  public double getPace() {
		return pace;
  }

  /**
   * @param pace the pace to set
   */
  public void setPace(double aPace) {
        setPace(aPace, PEOPLE);
  }

  /**
   * @return the spacing
   */
  public double getSpacing() {
		return spacing;
  }

  /**
   * @param spacing the spacing to set
   */
  public void setSpacing(double aSpacing) {
         setSpacing(aSpacing, PEOPLE);
  }

  /**
   * @return the time
   */
  public double getHours() {
		return time;
  }

  /**
   * @param time the time to set
   */
  public void setHours(double timeInHours) {
		setHours(timeInHours, PEOPLE);		
  }

  protected boolean recalculate() {
 
		boolean returnvalue = false;
		if (isCalculable()) { 
			try { 
			    if (people==0) { calculate(ResourceAllocation.PEOPLE);  returnvalue = true; }
			    if (area==0) { calculate(ResourceAllocation.AREA); returnvalue = true; }
			    if (pace==0) { calculate(ResourceAllocation.PACE); returnvalue = true; }
			    if (spacing==0) { calculate(ResourceAllocation.SPACING); returnvalue = true; }
			    if (time==0) { calculate(ResourceAllocation.TIME); returnvalue = true; }
			} catch (BadCalculationException e) { 
				System.out.println(e.getMessage());
				returnvalue = false;
			}
		}
		return returnvalue;
  }

  protected boolean recalculateTarget(int targetToRecalculate) {
 
		boolean returnvalue = false;
		if (isCalculable()) { 
			try { 
			    if (targetToRecalculate == ResourceAllocation.PEOPLE) { 
			    	calculate(ResourceAllocation.PEOPLE);  
			    	returnvalue = true; }
			    if (targetToRecalculate == ResourceAllocation.AREA) { 
			    	calculate(ResourceAllocation.AREA); 
			    	returnvalue = true; }
			    if (targetToRecalculate == ResourceAllocation.PACE) { 
			    	calculate(ResourceAllocation.PACE); 
			    	returnvalue = true; }
			    if (targetToRecalculate == ResourceAllocation.SPACING) { 
			    	calculate(ResourceAllocation.SPACING); 
			    	returnvalue = true; }
			    if (targetToRecalculate == ResourceAllocation.TIME) { 
			    	calculate(ResourceAllocation.TIME); 
			    	returnvalue = true; }
			} catch (BadCalculationException e) { 
				System.out.println(e.getMessage());
				returnvalue = false;
			}
		}
		return returnvalue;
  }

  public double calculate(int valueToCalculate) throws BadCalculationException {
		double returnvalue = 0.0;
		
		if (!isCalculable()) 
		    throw new BadCalculationException("Two or more variables are unset, can't calculate resource allocation in ResourceAllocation.calculate()");

		switch (valueToCalculate) { 
		    case PEOPLE:
				// searchers = (area in sq miles * 5280 ft/mile * 3.5 hours/mile) / (spacing ft * time in hours) 
		    	// People are integral, return result rounded up.
		    	returnvalue =  Math.ceil((area * UnitConverter.FEET_PER_MILE * (1.0/pace) ) / (spacing * time)); 
		    	people = returnvalue;
			    break;
		    case AREA:  
		    	returnvalue =  (spacing * time * people) / (UnitConverter.FEET_PER_MILE * (1d/pace) ) ; 
		    	area = returnvalue;
			    break;		
		    case PACE:
		    	returnvalue =  1/((spacing * time * people) / (UnitConverter.FEET_PER_MILE  * area )); 
		    	pace = returnvalue;
		    	break;
		    case SPACING:
		    	returnvalue =  (area * UnitConverter.FEET_PER_MILE * (1.0/pace) ) / (people * time); 
		    	spacing = returnvalue;
		    	break;
		    case TIME:
		    	returnvalue =  (area * UnitConverter.FEET_PER_MILE * (1.0/pace) ) / (spacing * people); 
		    	time = returnvalue;
		    	break;
			default:
			    throw new BadCalculationException("No value to calculate was provided to ResourceAllocation.calculate()");
			    
		}
		notifyObservers();
		
		return returnvalue;
  }

  public boolean isReasonable() {
		boolean returnvalue = true;
		if (area >  UPPER_REASONABLE_AREA_SQ_MILES) { returnvalue = false; }
		if (pace > UPPER_REASONABLE_PACE_PER_MILE) { returnvalue = false; }
		if (people > UPPER_REASONABLE_PEOPLE) { returnvalue = false; }
		if (spacing > UPPER_REASONABLE_SPACING_FEET) { returnvalue = false; }
		if (time > UPPER_REASONABLE_TIME_HOURS) { returnvalue = false; }
		return returnvalue;
  }

  @Override
  public void notifyObservers() {
		for (int i=0; i<observers.size(); i++) { 
			ResourceAllocationObserver o = observers.get(i);
			o.update();
		}
		
  }

  @Override
  public void registerObserver(net.aa3sd.SMT.interfaces.ResourceAllocationObserver observer) {
		   observers.add(observer);
  }

  @Override
  public void removeObserver(net.aa3sd.SMT.interfaces.ResourceAllocationObserver observer) {
		int i = observers.indexOf(observer);
		if (i >= 0) { 
			observers.remove(i);
		}
		
  }

  @Override
  public void setAllButArea(double people, double pace, double spacing, double time) throws BadCalculationException {
		this.people = people;
		this.pace = pace;
		this.spacing = spacing;
		this.time = time;
	    calculate(AREA);
  }

  @Override
  public void setAllButPeople(double area, double pace, double spacing, double time) throws BadCalculationException {
		this.area = area;
		this.pace = pace;
		this.spacing = spacing;
		this.time = time;
		calculate(PEOPLE);
  }

  @Override
  public void setArea(double anArea, int targetToRecalculate) {
		this.area = anArea;
		if (anArea>0)  {
			if (!recalculateTarget(targetToRecalculate)) { 
				try {
					people = this.calculate(targetToRecalculate);
				} catch (BadCalculationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
  }

  @Override
  public void setArea(double anArea, String units, int targetToRecalculate) {
		this.area = anArea;
		if (this.area > 0) { 
			if (recalculateTarget(targetToRecalculate)) { 
				try {
					area = this.calculate(targetToRecalculate);
				} catch (BadCalculationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
  }

  @Override
  public void setPace(double aPace, int targetToRecalculate) {
		this.pace = aPace;
		if (aPace > 0) { 
			if (!recalculateTarget(targetToRecalculate)) { 
				try {
					people = this.calculate(targetToRecalculate);
				} catch (BadCalculationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}	
  }

  @Override
  public void setPeople(double aPeople, int targetToRecalculate) {
		this.people = aPeople;
		if (this.people > 0) { 
			if (!recalculateTarget(targetToRecalculate)) { 
				try {
					area = this.calculate(targetToRecalculate);
				} catch (BadCalculationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
  }

  @Override
  public void setSpacing(double aSpacing, int targetToRecalculate) {
		this.spacing = aSpacing;
		if (aSpacing > 0) { 
			if (!recalculateTarget(targetToRecalculate)) { 
				try {
					people = this.calculate(targetToRecalculate);
				} catch (BadCalculationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
  }

  /**
   * @param time the time to set
   */
  public void setHours(double timeInHours, int targetToRecalculate) {
		this.time = timeInHours;
		if (timeInHours>0) { 
			if (!recalculateTarget(targetToRecalculate)) { 
				try {
					people = this.calculate(targetToRecalculate);
				} catch (BadCalculationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
  }

  @Override
  public void setAll(double anArea, double people, double pace, double spacing, double time) {
		this.area = anArea;
		this.people = people;
		this.pace = pace;
		this.spacing = spacing;
		this.time = time;
  }

  public String toString() {
 
	   String returnvalue = "Resource Allocation Estimate for Ground Grid Search." + SMT.LF + "Area(square miles)=" + area +  SMT.LF + "People="+ people + SMT.LF + "Time(hours)=" + time + SMT.LF + "Spacing=" + spacing + SMT.LF + "Pace= " + pace + SMT.LF + "Area(acres)=" + UnitConverter.squareMilesToAcres(area)  + SMT.LF + "Radius(miles)=" +  UnitConverter.radiusFromArea(area) + SMT.LF ;
	   return returnvalue;
  }

}
