/**
 * SubjectCategoryScheme.java
 * Created Aug 19, 2011 8:02:06 AM
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.swing.table.AbstractTableModel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import net.aa3sd.SMT.SMTSingleton;
import net.aa3sd.SMT.generated.subject.XCategoryScheme;
import net.aa3sd.SMT.generated.subject.XReflexTasks;
import net.aa3sd.SMT.generated.subject.XSubjectCategory;
import net.aa3sd.SMT.generated.subject.XSupplementalTaskDescription;
import net.aa3sd.SMT.generated.subject.XTask;
import net.aa3sd.SMT.generated.subject.XTaskList;
import net.aa3sd.SMT.interfaces.SubjectCategorySchemeListener;
import net.aa3sd.SMT.ui.MapInternalFrame;
/**
 * @author mole
 */
public class SubjectCategoryScheme extends AbstractTableModel {
  private static final long serialVersionUID =  6689274563249830407L;

  private static final Logger log =  Logger.getLogger(SubjectCategoryScheme.class);

  /**
   * TODO: Refactor to a more appropriate pattern
   */
  private String schemeName;

  private String copyrightInformation;

  private ArrayList<SubjectCategory> subjectCategories;

  private int selectedCategory;

  private ArrayList<SubjectCategoryPossibleTask> allTasks;

  public SubjectCategoryScheme() {
 
		// Load default subject category scheme
		setSchemeName("Built in Empty scheme.");
		copyrightInformation = new String();
		subjectCategories = new ArrayList<SubjectCategory>();
		allTasks = new ArrayList<SubjectCategoryPossibleTask>();
		subjectCategories.add(new SubjectCategory("Dementia"));
		subjectCategories.add(new SubjectCategory("Despondent"));
		setSelectedCategory(1);
		
  }

  public Object[] getCategoryNameList() {
		ArrayList<String> result = new ArrayList<String>();
		Iterator<SubjectCategory> i = subjectCategories.iterator();
		while (i.hasNext())  {
			result.add(i.next().getName());
		}
		return result.toArray();
  }

  public SubjectCategoryPossibleTask getPossibleTaskByID(int taskId) {
		SubjectCategoryPossibleTask result = null;
	   Iterator<SubjectCategoryPossibleTask> i = allTasks.iterator();
	   while (i.hasNext()) { 
		   SubjectCategoryPossibleTask task = i.next();
		   if (task.getTaskID()==taskId) {
			   result = task;
		   }
	   }
	   return result;
  }

  /**
   * @return the schemeName
   */
  public String getSchemeName() {
		return schemeName;
  }

  /**
   * @param schemeName the schemeName to set
   */
  public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
  }

  /**
   * @return the copyrightInformation
   */
  public String getCopyrightInformation() {
		return copyrightInformation;
  }

  /**
   * @param copyrightInformation the copyrightInformation to set
   */
  public void setCopyrightInformation(String copyrightInformation) {
		this.copyrightInformation = copyrightInformation;
  }

  /**
   * @return the allTasks
   */
  public ArrayList<SubjectCategoryPossibleTask> getAllTasks() {
		return allTasks;
  }

  /**
   * Equivalent to getAllTasks().add(task), but includes logging.
   * 
   * @param task the task to add to the list of all tasks
   */
  public void addToAllTasks(SubjectCategoryPossibleTask task) {
		log.debug("Adding " + task.getTaskSummary() + " to list of all tasks");
		allTasks.add(task);
  }

  /**
   * @return the selectedCategory
   */
  public int getSelectedCategory() {
		return selectedCategory;
  }

  /**
   * @param selectedCategory the selectedCategory to set
   */
  public void setSelectedCategory(int selectedCategory) {
		this.selectedCategory = selectedCategory;
  }

  /**
   *  (non-Javadoc)
   * @see javax.swing.table.TableModel#getRowCount()
   */
  @Override
  public int getRowCount() {
		// TODO Auto-generated method stub
		return 1;
  }

  /**
   *  (non-Javadoc)
   * @see javax.swing.table.TableModel#getColumnCount()
   */
  @Override
  public int getColumnCount() {
		// TODO Auto-generated method stub
		return subjectCategories.size();
  }

  /**
   *  (non-Javadoc)
   * @see javax.swing.table.TableModel#getValueAt(int, int)
   */
  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return subjectCategories.get(rowIndex).getName();
  }

  private net.aa3sd.SMT.generated.subject.XCategoryScheme unmarshal(Class<XCategoryScheme> docClass, InputStream inputStream) throws JAXBException {
		String packageName = docClass.getPackage().getName();
		JAXBContext jc = JAXBContext.newInstance( packageName );
		Unmarshaller u = jc.createUnmarshaller();
		@SuppressWarnings("unchecked")
		JAXBElement<XCategoryScheme> doc = (JAXBElement<XCategoryScheme>)u.unmarshal( inputStream );
		return doc.getValue();
  }

  /**
   * Constructor for Subject category scheme loaded from an xml file.
   * 
   * @param stream input stream for the xml file.
   */
  public SubjectCategoryScheme(InputStream stream) {
 
		allTasks = new ArrayList<SubjectCategoryPossibleTask>();
		try {
			// Load subject category scheme from file into proxy classes.
			XCategoryScheme scheme = unmarshal(XCategoryScheme.class, stream);
			// Transfer scheme level information to this class
			this.schemeName = scheme.getSchemeName();
			this.copyrightInformation = scheme.getCopyrightStatement();
			// Load tasks in common to all categories.
			XReflexTasks commonTasks = scheme.getCommonReflexTasks();
			// Load subject categories and transfer to subjectCategories.
			subjectCategories = new ArrayList<SubjectCategory>();
			List<XSubjectCategory>loadCat = scheme.getCategory();
			Iterator<XSubjectCategory> i = loadCat.iterator();
			while (i.hasNext()) {
				XSubjectCategory xcategory = i.next();
				SubjectCategory newCategory = new SubjectCategory(xcategory.getCategoryName());
				newCategory.setDistancesFromIPP(xcategory.getDistancesFromIPP());
				newCategory.setProfile(xcategory.getProfile());
				newCategory.addXTasks(commonTasks, allTasks, true);
				newCategory.addXTasks(xcategory.getReflexTasks(), allTasks, false);
				newCategory.addXDistancesFromIPP(xcategory.getDistancesFromIPP());
				subjectCategories.add(newCategory);
				log.debug("Added subjectCategory " + newCategory.getName() + " " + newCategory.toString());
			} 
	        log.debug("Number of tasks found: " + allTasks.size());
            List<XSupplementalTaskDescription> suppList = commonTasks.getContainmentTasks().getSupplementalDescription();	 
            appendSupplementalDescriptions(suppList,null);
            suppList = commonTasks.getHighProbabilityAreasTasks().getSupplementalDescription();	 
            appendSupplementalDescriptions(suppList, null);
            suppList = commonTasks.getHubTasks().getSupplementalDescription();	 
            appendSupplementalDescriptions(suppList, null);
            suppList = commonTasks.getInvestigationTasks().getSupplementalDescription();	 
            appendSupplementalDescriptions(suppList, null);
            suppList = commonTasks.getIPPTasks().getSupplementalDescription();	 
            appendSupplementalDescriptions(suppList, null);
            suppList = commonTasks.getTravelCorridorTasks().getSupplementalDescription();	 
            appendSupplementalDescriptions(suppList, null);
			Iterator<XSubjectCategory> it = loadCat.iterator();
			while (it.hasNext()) {
				XSubjectCategory xcat = it.next();
				XReflexTasks tasks = xcat.getReflexTasks();
	            suppList = tasks.getContainmentTasks().getSupplementalDescription();	 
	            appendSupplementalDescriptions(suppList, xcat);
	            suppList = tasks.getHighProbabilityAreasTasks().getSupplementalDescription();	 
	            appendSupplementalDescriptions(suppList, xcat);
	            suppList = tasks.getHubTasks().getSupplementalDescription();	 
	            appendSupplementalDescriptions(suppList, xcat);
	            suppList = tasks.getInvestigationTasks().getSupplementalDescription();	 
	            appendSupplementalDescriptions(suppList, xcat);
	            suppList = tasks.getIPPTasks().getSupplementalDescription();	 
	            appendSupplementalDescriptions(suppList, xcat);
	            suppList = tasks.getTravelCorridorTasks().getSupplementalDescription();	
			}
	        try { 
	        	// TODO: Probably shouldn't be set here
	            SMTSingleton.getSingletonInstance().getCurrentSearch().setSubjectCategoryScheme(this);
	        } catch (NullPointerException e1) { 
	        	// On startup when search is instantiated
	        }
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }

  /**
   * Add supplemental descriptions to task details for the taskid specified in the supplemental description.
   * 
   * @param suppList  List of supplemental descriptions to add to the task details
   * @param xcat if null, add to matching taskid in all categories, otherwise add to 
   * matching taskid for categories of same name as xcat.
   */
  private void appendSupplementalDescriptions(List<XSupplementalTaskDescription> suppList, net.aa3sd.SMT.generated.subject.XSubjectCategory xcat) {
    	try { 
        Iterator<XSupplementalTaskDescription> it = suppList.iterator();
        while (it.hasNext()) {
        	XSupplementalTaskDescription supp = it.next();
    	    log.debug(supp.getDescription());
        	List<String> targetTasks = supp.getForTaskID();
        	log.debug(targetTasks);
        	Iterator<String> itt = targetTasks.iterator();
        	while (itt.hasNext()) { 
        		String value = itt.next();
        		log.debug(value);
        		int forTaskId = Integer.parseInt(value);
        		log.debug(forTaskId);
    			Iterator<SubjectCategory> i = this.subjectCategories.iterator();
    			while (i.hasNext()) {
    				SubjectCategory xcategory = i.next();
    				if (xcat==null || xcat.getCategoryName().equals(xcategory.getName())) { 
        		        xcategory.getTaskByID(forTaskId).getTaskDetailsBuffer().append(supp.getDescription());
    				}
    			} 
        	}
        }
    	} catch (NullPointerException e) { 
    		log.debug(e);
    	}
  }

  public SubjectCategoryPossibleTask getTaskBySpecificID(UUID taskID) {
		for (int i=0; i<allTasks.size(); i++) { 
			UUID possiblematch = allTasks.get(i).getSpecificTaskID();
			log.debug(possiblematch);
			if (possiblematch.equals(taskID)) { 
				return allTasks.get(i);
			}
		}
		return null;
  }

  public SubjectCategory getSubjectCategoryByName(String categoryName) {
 
		SubjectCategory result = null;
		Iterator<SubjectCategory> i = subjectCategories.iterator();
		while (i.hasNext()) {
			SubjectCategory category = i.next();
			if (category.getName().equals(categoryName)) { 
				return category;
			}
		}
		return result;
  }

}
