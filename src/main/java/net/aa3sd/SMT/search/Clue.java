/**
 * Clue.java
 * Created Oct 8, 2011 12:43:08 PM
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

import java.util.Date;
import net.aa3sd.SMT.SMTSingleton;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.workbench.model.LayerManager;
/**
 * A clue found or observed in a search.   For category of clues found or observed at locations, 
 * e.g. physical clues such as clothing, or observed clues such as the sound of gunshots.  Not
 * intended for information that doesn't relate to a a location (such as a cigarette brand 
 * preference elicited in an interview.  
 * 
 * @author Paul J. Morris
 */
public class Clue extends Location {
  private int clueNumber;

  private Date dateTimeFound;

  private Task foundByTask;

  private String foundBy;

  private String clueDescription;

  private String locationDescription;

  private String disposition;

  /**
   * @param aName
   * @param featureCollection
   * @param layerManager
   */
  protected Clue(String aName, FeatureCollection featureCollection, LayerManager layerManager) {
		super(aName, featureCollection, layerManager);
		clueNumber = SMTSingleton.getSingletonInstance().getCurrentSearch().getNextClueNumber();
		super.setName(super.getName() + " " + Integer.toString(clueNumber));
		// TODO Auto-generated constructor stub
  }

  /**
   * @return the dateTimeFound
   */
  public Date getDateTimeFound() {
		return dateTimeFound;
  }

  /**
   * @param dateTimeFound the dateTimeFound to set
   */
  public void setDateTimeFound(Date dateTimeFound) {
		this.dateTimeFound = dateTimeFound;
  }

  /**
   * @return the foundByTask
   */
  public Task getFoundByTask() {
		return foundByTask;
  }

  /**
   * @param foundByTask the foundByTask to set
   */
  public void setFoundByTask(Task foundByTask) {
		this.foundByTask = foundByTask;
  }

  /**
   * @return the foundBy
   */
  public String getFoundBy() {
		return foundBy;
  }

  /**
   * @param foundBy the foundBy to set
   */
  public void setFoundBy(String foundBy) {
		this.foundBy = foundBy;
  }

  /**
   * @return the clueDescription
   */
  public String getClueDescription() {
		return clueDescription;
  }

  /**
   * @param clueDescription the clueDescription to set
   */
  public void setClueDescription(String clueDescription) {
		this.clueDescription = clueDescription;
  }

  /**
   * @return the locationDescription
   */
  public String getLocationDescription() {
		return locationDescription;
  }

  /**
   * @param locationDescription the locationDescription to set
   */
  public void setLocationDescription(String locationDescription) {
		this.locationDescription = locationDescription;
  }

  /**
   * @return the disposition
   */
  public String getDisposition() {
		return disposition;
  }

  /**
   * @param disposition the disposition to set
   */
  public void setDisposition(String disposition) {
		this.disposition = disposition;
  }

  /**
   * @return the clueNumber
   */
  public int getClueNumber() {
		return clueNumber;
  }

}
