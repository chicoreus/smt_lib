/**
 * Find.java
 * Created Aug 19, 2011 7:08:52 PM
 * 
 * © Copyright 2011 Paul J. Morris 
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

import java.sql.Time;
import net.aa3sd.SMT.SMTSingleton;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.workbench.model.LayerManager;
/**
 * The location at which one or more subjects was located.  
 * 
 * @author Paul J. Morris
 */
public class Find extends Location {
  public static final String[] findLocationStandardTerms =  { "Structure","Road","Linear","Drainage","Water","Brush","Scrub","Woods","Field","Rock" };

  private Time foundAt;

  /**
   *  utm coordinate for the find
   */
  private Coordinate locationUTM;

  /**
   *  standard term for find location
   */
  private String locationStandardized;

  /**
   *  free text description of the find location
   */
  private String location;

  private boolean foundAlive;

  private String foundByResource;

  public Find(String aName, FeatureCollection featureCollection, LayerManager layerManager) {
		super(aName, featureCollection, layerManager);
  }

  /**
   * @return the foundAt
   */
  public Time getFoundAt() {
		return foundAt;
  }

  /**
   * @param foundAt the foundAt to set
   */
  public void setFoundAt(Time foundAt) {
		this.foundAt = foundAt;
  }

  /**
   * @return the locationUTM
   */
  public Coordinate getLocationUTM() {
		return locationUTM;
  }

  /**
   * @param locationUTM the locationUTM to set
   */
  public void setLocationUTM(Coordinate locationUTM) {
		this.locationUTM = locationUTM;
  }

  /**
   * @return the locationStandardized
   */
  public String getLocationStandardized() {
		return locationStandardized;
  }

  /**
   * @param locationStandardized the locationStandardized to set
   */
  public void setLocationStandardized(String locationStandardized) {
		this.locationStandardized = locationStandardized;
  }

  /**
   * @return the location
   */
  public String getLocation() {
		return location;
  }

  /**
   * @param location the location to set
   */
  public void setLocation(String location) {
		this.location = location;
  }

  /**
   * @return the foundAlive
   */
  public boolean isFoundAlive() {
		return foundAlive;
  }

  /**
   * @param foundAlive the foundAlive to set
   */
  public void setFoundAlive(boolean foundAlive) {
		this.foundAlive = foundAlive;
  }

  /**
   * @return the foundByResource
   */
  public String getFoundByResource() {
		return foundByResource;
  }

  /**
   * @param foundByResource the foundByResource to set
   */
  public void setFoundByResource(String foundByResource) {
		this.foundByResource = foundByResource;
  }

  /**
   * @return the findlocationstandardterms
   */
  public static String[] getFindlocationstandardterms()
  {
		return findLocationStandardTerms;
  }

}
