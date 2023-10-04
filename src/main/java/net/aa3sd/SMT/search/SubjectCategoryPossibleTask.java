/**
 * SubjectCategoryPossibleTask.java
 * Created Oct 13, 2011 8:16:11 AM
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

import java.util.ArrayList;
import java.util.UUID;
import org.apache.log4j.Logger;
import net.aa3sd.SMT.resources.Resource;
/**
 * @author mole
 */
public class SubjectCategoryPossibleTask {
  private static final Logger log =  Logger.getLogger(SubjectCategoryScheme.class);

  private StringBuffer taskSummary;

  private int taskID;

  private StringBuffer taskDetails;

  private ArrayList<net.aa3sd.SMT.resources.Resource> entailedResources;

  private UUID specificTaskID;

  public SubjectCategoryPossibleTask(int taskId) {
		specificTaskID = java.util.UUID.randomUUID();
		taskID = taskId;
		taskSummary = new StringBuffer();
		taskDetails = new StringBuffer();
		entailedResources = new ArrayList<Resource>();
		log.debug("Created task with id=" + taskID);
  }

  public SubjectCategoryPossibleTask(int taskId, String taskSummary) {
 
		specificTaskID = java.util.UUID.randomUUID();
		taskID = taskId;
		this.taskSummary = new StringBuffer();
		this.taskSummary.append(taskSummary);
		taskDetails = new StringBuffer();
		entailedResources = new ArrayList<Resource>();
		log.debug("Created task with id=" + taskID + " " + this.taskSummary);
  }

  /**
   * @return the taskSummary as a string
   */
  public String getTaskSummary() {
		return taskSummary.toString();
  }

  /**
   * @return the taskSummary as a string buffer
   */
  public StringBuffer getTaskSummaryBuffer() {
		return taskSummary;
  }

  /**
   * @param taskSummary the taskSummary to set
   */
  public void setTaskSummary(String taskSummary) {
		this.taskSummary = new StringBuffer();
		this.taskSummary.append(taskSummary);
  }

  /**
   * @return the taskID
   */
  public int getTaskID() {
		return taskID;
  }

  /**
   * @param taskID the taskID to set
   */
  public void setTaskID(int taskID) {
		this.taskID = taskID;
  }

  /**
   * @return the taskDetails as a string
   */
  public String getTaskDetails() {
		return taskDetails.toString();
  }

  /**
   * @param taskDetails the taskDetails to set
   */
  public void setTaskDetails(String taskDetails) {
		this.taskDetails = new StringBuffer();
		this.taskDetails.append(taskDetails);
  }

  public StringBuffer getTaskDetailsBuffer() {
 
		return taskDetails;
  }

  /**
   * @return the specificTaskID
   */
  public UUID getSpecificTaskID() {
		return specificTaskID;
  }

  /**
   * @return the entailedResources
   */
  public ArrayList<net.aa3sd.SMT.resources.Resource> getEntailedResources() {
		return entailedResources;
  }

}
