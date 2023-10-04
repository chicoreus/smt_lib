/**
 * Coordinate.java
 * Created Nov 3, 2008 9:42:22 AM
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
package net.aa3sd.SMT.math;

import net.aa3sd.SMT.geometry.Point;
/**
 * @author mole
 */
public class Coordinate {
  private net.aa3sd.SMT.geometry.Point point;

  private String spatialReferenceSystem;

  private String segmentName;

  public Coordinate(double latitude, double longitude) {
		this.point = new Point(latitude, longitude, "+proj=longlat +ellps=WGS84 +datum=WGS84 ");
  }

  public Coordinate(double latitude, double longitude, String segmentName) {
		this.point = new Point(latitude, longitude, "+proj=longlat +ellps=WGS84 +datum=WGS84 ");
		this.segmentName = segmentName;
  }

  public double getLatitude() {
		return point.getLatitude();
  }

  public double getLongitude() {
		return point.getLongitude();
  }

  public String asString() {
 
		String returnvalue = String.format("%s %8f5 %8f5", segmentName, point.getLatitude(), point.getLongitude());
		return returnvalue;
  }

  public String asLatLongString() {
 
		String returnvalue = String.format("%8f5 %8f5", point.getLatitude(), point.getLongitude());
		return returnvalue;
  }

}
