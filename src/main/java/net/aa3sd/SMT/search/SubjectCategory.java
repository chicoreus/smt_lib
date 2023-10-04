/**
 * SubjectCategory.java
 * Created Aug 18, 2011 8:00:31 AM
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
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import net.aa3sd.SMT.SMTSingleton;
import net.aa3sd.SMT.generated.subject.XDistanceSet;
import net.aa3sd.SMT.generated.subject.XDistancesFromIPP;
import net.aa3sd.SMT.generated.subject.XReflexTasks;
import net.aa3sd.SMT.generated.subject.XResource;
import net.aa3sd.SMT.generated.subject.XTask;
import net.aa3sd.SMT.generated.subject.XTaskList;
import net.aa3sd.SMT.resources.Resource;
/**
 * A statistical behavior category for missing subjects.  Includes a profile for behavior of 
 * subjects in this category, distances subjects in this category travel, and possible reflex 
 * tasks for search management to consider using for this behavioral category.
 * 
 * @author Paul J. Morris
 */
public class SubjectCategory {
  private static final Logger log =  Logger.getLogger(SubjectCategory.class);

  private String name;

  private String profile;

  private SubjectCategoryDistanceFromIPP distanceFromIPP;

  private SubjectCategoryReflexTasks reflexTasks;

  private ArrayList<SubjectCategoryPossibleTask> allCategoryTasks;

  public SubjectCategory(String name) {
		this.name = name;
		allCategoryTasks = new ArrayList<SubjectCategoryPossibleTask>();
		log.debug("Creating SubjectCategory " + name + " " +  this.toString());
		setProfile("");
		setDistanceFromIPP(new SubjectCategoryDistanceFromIPP());
		reflexTasks = new SubjectCategoryReflexTasks();
  }

  /**
   * @return the name of the category
   */
  public String getName() {
		return name;
  }

  /**
   * @return the reflexTasks
   */
  public SubjectCategoryReflexTasks getReflexTasks() {
		return reflexTasks;
  }

  /**
   * @param reflexTasks the reflexTasks to set
   */
  public void setReflexTasks(SubjectCategoryReflexTasks reflexTasks) {
		log.debug("Setting reflex tasks" + reflexTasks.toString());
		this.reflexTasks = reflexTasks;
  }

  /**
   * @return the distanceFromIPP
   */
  public SubjectCategoryDistanceFromIPP getDistanceFromIPP() {
		return distanceFromIPP;
  }

  /**
   * @param distanceFromIPP the distanceFromIPP to set
   */
  public void setDistanceFromIPP(SubjectCategoryDistanceFromIPP distanceFromIPP) {
		this.distanceFromIPP = distanceFromIPP;
  }

  public void setDistancesFromIPP(net.aa3sd.SMT.generated.subject.XDistancesFromIPP xDistancesFromIPP) {
 
		setDistanceFromIPP(new SubjectCategoryDistanceFromIPP(xDistancesFromIPP.getDistances()));
  }

  /**
   * @return the profile for this category.
   */
  public String getProfile() {
		return profile;
  }

  /**
   * @param profile the profile to set
   */
  public void setProfile(String profile) {
		this.profile = profile;
  }

  public void addXDistancesFromIPP(net.aa3sd.SMT.generated.subject.XDistancesFromIPP xDistances) {
 
		List<XDistanceSet> xdsList = xDistances.getDistances();
		this.distanceFromIPP = new SubjectCategoryDistanceFromIPP(xdsList); 
  }

  /**
   * Adds reflex tasks from the proxy object for the XML representation.
   * 
   * @param commonTasks tasks to add for this category.
   * @param allTasks calling classes list of all tasks.
   * @param resetTaskList true to remove any existing reflex tasks.
   */
  public void addXTasks(net.aa3sd.SMT.generated.subject.XReflexTasks commonTasks, ArrayList<SubjectCategoryPossibleTask> allTasks, boolean resetTaskList) {
 
		XTaskList tl = commonTasks.getContainmentTasks();
		if (resetTaskList) { 
			reflexTasks = new SubjectCategoryReflexTasks();
		}
		List<XTask> t = tl.getTask();
		Iterator<XTask> ita = t.iterator();
		while (ita.hasNext()) {
			XTask xtask = ita.next();
			try {
				SubjectCategoryPossibleTask task = new SubjectCategoryPossibleTask(xtask.getTaskID(), xtask.getDescription());
				allTasks.add(task);
				allCategoryTasks.add(task);
				this.getReflexTasks().getContainmentTasks().add(task);
				log.debug("Adding task: " + task.getTaskSummary() + " to " + this.getName() );
			} catch (NullPointerException e) { 
				// null xtask or task
			}
		}
		tl = commonTasks.getHighProbabilityAreasTasks();
		t = tl.getTask();
		ita = t.iterator();
		while (ita.hasNext()) {
			XTask xtask = ita.next();
			try { 
				SubjectCategoryPossibleTask task = new SubjectCategoryPossibleTask(xtask.getTaskID(), xtask.getDescription());
				allTasks.add(task);
				allCategoryTasks.add(task);
				this.getReflexTasks().getHighProbabilityAreasTasks().add(task);
				log.debug("Adding task: " + task.getTaskSummary() + " to " + this.getName() );
			} catch (NullPointerException e) { 
				// null xtask or task
			}
		}
		tl = commonTasks.getHubTasks();
		t = tl.getTask();
		ita = t.iterator();
		while (ita.hasNext()) {
			XTask xtask = ita.next();
			try { 
				SubjectCategoryPossibleTask task = new SubjectCategoryPossibleTask(xtask.getTaskID(), xtask.getDescription());
				allTasks.add(task);
				allCategoryTasks.add(task);
				this.getReflexTasks().getHubTasks().add(task);
				log.debug("Adding task: " + task.getTaskSummary() + " to " + this.getName() );
			} catch (NullPointerException e) { 
				// null xtask or task
			}
		}
		tl = commonTasks.getInvestigationTasks();
		t = tl.getTask();
		ita = t.iterator();
		while (ita.hasNext()) {
			XTask xtask = ita.next();
			try { 
				SubjectCategoryPossibleTask task = new SubjectCategoryPossibleTask(xtask.getTaskID(), xtask.getDescription());
				allTasks.add(task);
				allCategoryTasks.add(task);
				this.getReflexTasks().getInvestigationTasks().add(task);
				log.debug("Adding task: " + task.getTaskSummary() + " to " + this.getName() );
				log.debug(this.getReflexTasks().toString() );
				log.debug(this.getReflexTasks().getInvestigationTasks().toString() );
			} catch (NullPointerException e) { 
				// null xtask or task
			}
		}
		tl = commonTasks.getIPPTasks();
		t = tl.getTask();
		ita = t.iterator();
		while (ita.hasNext()) {
			XTask xtask = ita.next();
			try {
				SubjectCategoryPossibleTask task = new SubjectCategoryPossibleTask(xtask.getTaskID(), xtask.getDescription());
			    List<XResource> xresources = xtask.getEntailedResource();
			    Iterator<XResource> itr = xresources.iterator();
			    while (itr.hasNext()) { 
			    	XResource xResource = itr.next();
			    	int count = 1;
			    	try { 
			    		count = Integer.parseInt(xResource.getCount());
			    	} catch (NumberFormatException e) { 
			    		count = 1;
			    	}
			    	if (count<1) count = 1; 
			    	for (int ct=0; ct<count;ct++) { 
			    		Resource proposedResource = new Resource();
			    		proposedResource.setType(xResource.getTypeDescription());
			    		// TODO: Remove magic phrase, add construct to xml.
			    		if (xResource.getTypeDescription().contains("task force")) {
			    			proposedResource.setKind(Task.KIND_TASK_FORCE);
			    		} else {
			    			proposedResource.setKind(Task.KIND_SINGLE_RESOURCE);
			    		}
			    		task.getEntailedResources().add(proposedResource);
			    	}
			    }
				allTasks.add(task);
				allCategoryTasks.add(task);
				this.getReflexTasks().getIppTasks().add(task);
				log.debug("Adding task: " + task.getTaskSummary() + " to " + this.getName() );
			} catch (NullPointerException e) { 
				// null xtask or task
			}
		}
  }

  /**
   * Get a task from this category's list of possible task by the taskID.
   * 
   * @param taskID id for the task to return
   * @return SubjectCategoryPossibleTask with the matching taskID.
   */
  public SubjectCategoryPossibleTask getTaskByID(int taskID) {
		for (int i=0; i<allCategoryTasks.size(); i++) { 
			int possiblematch = allCategoryTasks.get(i).getTaskID();
			log.debug(possiblematch);
			if (possiblematch==taskID) { 
				return allCategoryTasks.get(i);
			}
		}
		return null;
  }

}
