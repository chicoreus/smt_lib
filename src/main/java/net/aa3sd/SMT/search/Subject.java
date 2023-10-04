/**
 * Subject.java
 * Created Aug 4, 2011 7:21:20 PM
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

import java.util.Iterator;
import java.util.Vector;
import net.aa3sd.SMT.SMTSingleton;
import net.aa3sd.SMT.interfaces.SubjectListener;
import net.aa3sd.SMT.log.LogEvent;
/**
 * @author mole
 */
public class Subject {
  private String name;

  private int numberOfSubjects;

  private String category;

  private String age;

  private String medicalConditions;

  private String clothing;

  private String equipment;

  private String searchInformation;

  private String publicSearchInformation;

  private String planningInformation;

  private Vector<net.aa3sd.SMT.interfaces.SubjectListener> listeners;

  /**
   *  minor factor
   */
  private String experience;

  private SubjectCategory subjectCategory =  null;

  /**
   * @param name
   * @param numberOfSubjects
   */
  public Subject(String name, int numberOfSubjects) {
		super();
		listeners = new Vector<SubjectListener>();
		this.name = name;
		//TODO: we probably want to use a subject factory to create subjects if numberOfSubjects=1 and subjectGroup if numberOfSubjects > 1
		//This should include an ability to increase the number of subjects from 1 if additional subjects are involved.
		this.numberOfSubjects = numberOfSubjects;
		category = new String();
		subjectCategory = new SubjectCategory("Unassigned");
		searchInformation = new String();
		planningInformation = new String();
		SMTSingleton.getSingletonInstance().getActiveLog().LogAnEvent(new LogEvent("Subject",LogEvent.EVENT_SEARCH,"Created new Subject: " + this.name));
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
		notifyListeners();
		SMTSingleton.getSingletonInstance().getActiveLog().LogAnEvent(new LogEvent("Subject",LogEvent.EVENT_SEARCH,"Changed subject name to: " + this.name));
  }

  /**
   * @return the numberOfSubjects
   */
  public int getNumberOfSubjects() {
		return numberOfSubjects;
  }

  /**
   * @param numberOfSubjects the numberOfSubjects to set
   */
  public void setNumberOfSubjects(int numberOfSubjects) {
		this.numberOfSubjects = numberOfSubjects;
		notifyListeners();
		SMTSingleton.getSingletonInstance().getActiveLog().LogAnEvent(new LogEvent("Subject",LogEvent.EVENT_SEARCH,"Changed number of subjects to: " + this.numberOfSubjects));
  }

  /**
   * @return the age
   */
  public String getAge() {
		return age;
  }

  /**
   * @param age the age to set
   */
  public void setAge(String age) {
		this.age = age;
		notifyListeners();
		SMTSingleton.getSingletonInstance().getActiveLog().LogAnEvent(new LogEvent("Subject",LogEvent.EVENT_SEARCH,"Changed subject age to: " + this.age));
  }

  /**
   * @return the medicalConditions
   */
  public String getMedicalConditions() {
		return medicalConditions;
  }

  /**
   * @param medicalConditions the medicalConditions to set
   */
  public void setMedicalConditions(String medicalConditions) {
		this.medicalConditions = medicalConditions;
		notifyListeners();
		SMTSingleton.getSingletonInstance().getActiveLog().LogAnEvent(new LogEvent("Subject",LogEvent.EVENT_SEARCH,"Changed subject medical conditions to: " + this.medicalConditions));
  }

  /**
   * @return the experience
   */
  public String getExperience() {
		return experience;
  }

  /**
   * @param experience the experience to set
   */
  public void setExperience(String experience) {
		this.experience = experience;
		notifyListeners();
		SMTSingleton.getSingletonInstance().getActiveLog().LogAnEvent(new LogEvent("Subject",LogEvent.EVENT_SEARCH,"Changed experience to: " + this.experience));
  }

  /**
   * @return the equipment
   */
  public String getEquipment() {
		return equipment;
  }

  /**
   * @param equipment the equipment to set
   */
  public void setEquipment(String equipment) {
		this.equipment = equipment;
		notifyListeners();
		SMTSingleton.getSingletonInstance().getActiveLog().LogAnEvent(new LogEvent("Subject",LogEvent.EVENT_SEARCH,"Changed equipment to: " + this.equipment));
  }

  /**
   * @return the searchInformation
   */
  public String getSearchInformation() {
		return searchInformation;
  }

  /**
   * @param searchInformation the searchInformation to set
   */
  public void setSearchInformation(String searchInformation) {
		this.searchInformation = searchInformation;
		notifyListeners();
		SMTSingleton.getSingletonInstance().getActiveLog().LogAnEvent(new LogEvent("Subject",LogEvent.EVENT_SEARCH,"Set searching data to: " + this.searchInformation));
  }

  /**
   * @return the planningInformation
   */
  public String getPlanningInformation() {
		return planningInformation;
  }

  /**
   * @param planningInformation the planningInformation to set
   */
  public void setPlanningInformation(String planningInformation) {
		this.planningInformation = planningInformation;
		notifyListeners();
		SMTSingleton.getSingletonInstance().getActiveLog().LogAnEvent(new LogEvent("Subject",LogEvent.EVENT_SEARCH,"Set planning data to: " + this.planningInformation));
  }

  public void registerSubjectListener(net.aa3sd.SMT.interfaces.SubjectListener listener) {
 
		listeners.add(listener);
  }

  private void notifyListeners() {
 
		Iterator<SubjectListener> i = listeners.iterator();
		while (i.hasNext()) { 
			i.next().subjectChanged();
		}
  }

  /**
   * @return the clothing
   */
  public String getClothing() {
		return clothing;
  }

  /**
   * @param clothing the clothing to set
   */
  public void setClothing(String clothing) {
		notifyListeners();
		this.clothing = clothing;
  }

  /**
   * @return the subjectCategory
   */
  public SubjectCategory getSubjectCategory() {
		return subjectCategory;
  }

  /**
   * @param subjectCategory the subjectCategory to set
   */
  public void setSubjectCategory(SubjectCategory subjectCategory) {
		this.subjectCategory = subjectCategory;
		this.category = subjectCategory.getName();
		notifyListeners();
		SMTSingleton.getSingletonInstance().getActiveLog().LogAnEvent(new LogEvent("Subject",LogEvent.EVENT_SEARCH,"Set subject category to: " + this.category));
  }

  /**
   * @return the publicSearchInformation
   */
  public String getPublicSearchInformation() {
		return publicSearchInformation;
  }

  /**
   * @param publicSearchInformation the publicSearchInformation to set
   */
  public void setPublicSearchInformation(String publicSearchInformation) {
		this.publicSearchInformation = publicSearchInformation;
  }

}
