/**
 * SubjectCategoryReflexTasks.java
 * Created Sep 1, 2011 8:27:56 AM
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
import java.util.List;
import net.aa3sd.SMT.generated.subject.XReflexTasks;
/**
 * @author mole
 */
public class SubjectCategoryReflexTasks {
  List<SubjectCategoryPossibleTask> investigationTasks;

  List<SubjectCategoryPossibleTask> ippTasks;

  List<SubjectCategoryPossibleTask> hubTasks;

  List<SubjectCategoryPossibleTask> containmentTasks;

  List<SubjectCategoryPossibleTask> travelCoridorTasks;

  List<SubjectCategoryPossibleTask> highProbabilityAreasTasks;

  /**
   * public static SubjectCategoryReflexTasks subjectCategoryReflexTasksFactory(String category) { 
   * return new SubjectCategoryReflexTasks(category);
   * }
   */
  public static List<String> getReflexTaskCategories()
  {
 
		List<String> result = new ArrayList<String>();
		result.add("Investigation");
		result.add("Initial Planning Point");
		result.add("Hub");
		result.add("Containment");
		result.add("Travel Coridors");
		result.add("High Probability Areas");
		return result;
  }

  public SubjectCategoryReflexTasks() {
 
		investigationTasks = new ArrayList<SubjectCategoryPossibleTask>();
		ippTasks = new ArrayList<SubjectCategoryPossibleTask>();
		hubTasks = new ArrayList<SubjectCategoryPossibleTask>();
		containmentTasks = new ArrayList<SubjectCategoryPossibleTask>();
		travelCoridorTasks = new ArrayList<SubjectCategoryPossibleTask>();
		highProbabilityAreasTasks = new ArrayList<SubjectCategoryPossibleTask>();
		//initCommonForAll();
	    //initDefault();	
	    System.out.println("investigationtasks: " + investigationTasks.size());
  }

  /**
   * public SubjectCategoryReflexTasks(String category) { 
   * investigationTasks = new ArrayList<SubjectCategoryPossibleTask>();
   * ippTasks = new ArrayList<SubjectCategoryPossibleTask>();
   * hubTasks = new ArrayList<SubjectCategoryPossibleTask>();
   * containmentTasks = new ArrayList<SubjectCategoryPossibleTask>();
   * travelCoridorTasks = new ArrayList<SubjectCategoryPossibleTask>();
   * highProbabilityAreasTasks = new ArrayList<SubjectCategoryPossibleTask>();
   * initCommonForAll();
   * init(category);	
   * System.out.println("investigationtasks: " + investigationTasks.size());
   * }		
   */
  private void initCommonForAll() {
 
		investigationTasks.add(new SubjectCategoryPossibleTask(0, "This is the builtin task set."));
		/* 
		ippTasks.add("This is the builtin task set.");
		ippTasks.add("Preserve IPP");
		ippTasks.add("Search locale around IPP");
		ippTasks.add("Mantrackers/Signcutters.");
		ippTasks.add("Tracking/Trailing canine tasks.");
		
		hubTasks.add("Thorough search of 25% zone.");
		
        containmentTasks.add("Establish containment.");
        
        travelCoridorTasks.add("Hasty search trails leading away from IPP.");
        travelCoridorTasks.add("Brief hasty search teams to check decision points, and record field decision points.");
        travelCoridorTasks.add("Canine tasks to search drainages.");
        travelCoridorTasks.add("Task Mantrackers/signcutters to decision points.");
        travelCoridorTasks.add("Evaluate map for decision points.");
        
        highProbabilityAreasTasks.add("Check locations of historical finds");
        highProbabilityAreasTasks.add("Identify and search high hazard areas.");
        */
  }

  private void initDefault() {
		/** 
		investigationTasks.add("LPQ: Ask dementia specific questions.");
		investigationTasks.add("LPQ: Ask about previous wandering history.");
		investigationTasks.add("LPQ: Ask about potential destinations and historical destinations.");
		investigationTasks.add("Check Mass Transit and taxis.");
		investigationTasks.add("Check hospitals, shelters, jails.");
		investigationTasks.add("Notify community by media, reverse 911, or other means.");
		
		ippTasks.add("Tracking/Trailing canine tasks prefereably trained with dementia subjects.");
		
		hubTasks.add("Canvas neighborhood.");
		hubTasks.add("Distribute flyers door-to-door in neigbhorhood.");
		
        containmentTasks.add("Establish containment using 95% statisical zone or theoretical zone. ");
        containmentTasks.add("Road patrols.");
        containmentTasks.add("Air patrols.");
        containmentTasks.add("Bike patrols.");
        containmentTasks.add("Contain entrances to gated communities.");
    
        travelCoridorTasks.add("50% track offset is 15 meters.  75% track offset is 71 meters.");
        
        highProbabilityAreasTasks.add("Check locations where this subject has been found previously");
        highProbabilityAreasTasks.add("Check locations where this subject used to live.");
        highProbabilityAreasTasks.add("Check locations where this subject used to work, go to school, go to church, or visit regularly.");
        highProbabilityAreasTasks.add("Brief task teams to search heavy brush and places where subject may become entangled/stuck.");
        highProbabilityAreasTasks.add("Search high hazard areas easily entered from continuing straight at likely decision points.");
        */
  }

  private void init(String category) {
		if (category.equals("Dementia")) { 
			initDefault();
		} else if (category.equals("Angler")) {
			/* 
		investigationTasks.add("LPQ: Ask about destination and route.");
		investigationTasks.add("LPQ: Ask about favorite spots and routes.");
		investigationTasks.add("LPQ: Ask about type of fishing.");
		investigationTasks.add("LPQ: Ask about potential stranding locations.");
		investigationTasks.add("Most frequent categories are Lost (44%), Overdue (30%), and Stranded (11%).");
		
		ippTasks.add("Air scent tasks along shorelines.");
		ippTasks.add("Canvas campground.");
		
		hubTasks.add("Canvas campground.");
		hubTasks.add("Consider water (cadaver) canine.");
		
        containmentTasks.add("Establish containment using 95% statisical zone or theoretical zone. ");
        containmentTasks.add("Road patrols and road blocks.");
        containmentTasks.add("Camp-ins.");
        containmentTasks.add("Bike patrols.");
    
        travelCoridorTasks.add("50% track offset is 15 meters.  75% track offset is 71 meters.");
		travelCoridorTasks.add("Aerial search, IPP, hub and out.");
		ippTasks.add("Air scent tasks along shorelines.");
        
        highProbabilityAreasTasks.add("Check high hazard areas, 11% of subjects are stranded.");
        highProbabilityAreasTasks.add("Check islands.");
        highProbabilityAreasTasks.add("Check possible drowining locations (low head dams, snags, etc.).");
        */ 
		} 
  }

  /**
   * @return the investigationTasks
   */
  public List<SubjectCategoryPossibleTask> getInvestigationTasks() {
		return investigationTasks;
  }

  /**
   * @return the ippTasks
   */
  public List<SubjectCategoryPossibleTask> getIppTasks() {
		return ippTasks;
  }

  /**
   * @return the hubTasks
   */
  public List<SubjectCategoryPossibleTask> getHubTasks() {
		return hubTasks;
  }

  /**
   * @return the containmentTasks
   */
  public List<SubjectCategoryPossibleTask> getContainmentTasks() {
		return containmentTasks;
  }

  /**
   * @return the travelCoridorTasks
   */
  public List<SubjectCategoryPossibleTask> getTravelCoridorTasks() {
		return travelCoridorTasks;
  }

  /**
   * @return the highProbabilityAreasTasks
   */
  public List<SubjectCategoryPossibleTask> getHighProbabilityAreasTasks() {
		return highProbabilityAreasTasks;
  }

}
