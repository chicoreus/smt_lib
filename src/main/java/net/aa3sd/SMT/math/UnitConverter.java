/**
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
package net.aa3sd.SMT.math;

/**
 * UnitConverter
 * @author Paul J. Morris
 * 
 * Provides for conversion of between various units and systems for distance and area 
 * measures.
 */
public class UnitConverter {
  public static double FEET_PER_MILE =  5280d;

  /**
   * Intended to be available as a command line API, not yet implemented.
   * 
   * @param args
   */
  public static void main(String[] args)
  {
		// TODO Auto-generated method stub

  }

  public static double squareMilesToAcres(double squareMiles)
  {
 
		double returnvalue = 0d;
		
		if (squareMiles > 0 ) { 
			returnvalue = squareMiles * 640d;
		}
		
		return returnvalue;
  }

  public static double acresToSquareMiles(double acres)
  {
 
		double returnvalue = 0d;
		
		if (acres > 0 ) { 
			returnvalue = acres * 0.0015625;
		}
		
		return returnvalue;
  }

  public static double radiusFromArea(double area)
  {
		return Math.sqrt(area / Math.PI); 
  }

  public static double areaFromRadius(double radius)
  {
		return (radius * radius) * Math.PI ; 
  }

  public static double metersToMiles(double meters)
  {
 
		return meters * 0.00062137119d;
  }

  public static double milesToMeters(double miles)
  {
 
		return miles / 1609.334;
  }

  public static double metersToAcres(double squareMeters)
  {
 
	   return squareMeters * 0.00024710439d;
  }

  public static double acresToSquareMeters(double acres)
  {
 
	   return acres / 4046.8726d;
  }

}
