/**
 * UrgencyStandard.java
 * Created Jan 8, 2010 6:04:55 PM
 * 
 * © Copyright 2010 Paul J. Morris 
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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.text.StringContent;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import net.aa3sd.SMT.SMTSingleton;
import net.aa3sd.SMT.exceptions.BadValueException;
import net.aa3sd.SMT.generated.urgency.FactorGroupType;
import net.aa3sd.SMT.generated.urgency.FactorType;
import net.aa3sd.SMT.generated.urgency.ObjectFactory;
import net.aa3sd.SMT.generated.urgency.SearchUrgencyEstimationSchemeType;
import net.aa3sd.SMT.generated.urgency.ThresholdType;
import net.aa3sd.SMT.log.LogEvent;
/**
 * @author mole
 */
public class UrgencyScheme extends AbstractTableModel {
  private static final long serialVersionUID =  -8623241833022112008L;

  /**
   * Values that can be used to determine appropriate maximum widths for each of the colums in the table.
   */
  public static final Object[] longValues =  {"High Risk Modifiers (>24 hour survival < 50%)", "Predicted Hazardous Weather (less than 8 hours away): 1-2 ", "1", "Free Text Note"};

  private ArrayList<UrgencyFactorGroup> factorGroups;

  private ArrayList<net.aa3sd.SMT.generated.urgency.ThresholdType> thresholds;

  private String schemeName =  "Search Urgency Evaluation Scheme";

  public static final String DEFAULT_TEXT =  "Set one urgency factor in each category";

  public UrgencyScheme() {
		thresholds = new ArrayList<ThresholdType>();
		setSchemeName("MGH Search Urgency Evaluation Scheme");
		ThresholdType t = new ThresholdType();
		t.setMessage("Emergency that should be acted upon with haste");
		t.setOrdinalPosition(1);
		t.setUpperBound(11);
		thresholds.add(t);
		ThresholdType t1 = new ThresholdType();
		t1.setMessage("A measured response that should be enacted immediately");
		t1.setOrdinalPosition(2);
		t1.setUpperBound(17);
		thresholds.add(t1);
		ThresholdType t2 = new ThresholdType();
		t2.setMessage("Evaulate as a potential missing persons case");
		t2.setOrdinalPosition(3);
		t2.setUpperBound(24);
		thresholds.add(t2);		
		
		factorGroups = new ArrayList<UrgencyFactorGroup>();
		HashSet<Integer> validValues1to3 = new HashSet<Integer>();
		validValues1to3.add(Integer.valueOf(1));
		validValues1to3.add(Integer.valueOf(2));
		validValues1to3.add(Integer.valueOf(3));		
		UrgencyFactor uf1 = new UrgencyFactor("Very Young",1);
		UrgencyFactor uf2 = new UrgencyFactor("Very Old", 1);
		UrgencyFactor uf3 = new UrgencyFactor("Other",2,3);
		ArrayList<UrgencyFactor> f1 = new ArrayList<UrgencyFactor>();
		f1.add(uf1);
		f1.add(uf2);
		f1.add(uf3);
		UrgencyFactorGroup age = new UrgencyFactorGroup(f1, "Age",validValues1to3);
		factorGroups.add(age);
		
		UrgencyFactor uf4 = new UrgencyFactor("Known/Suspected injured, ill, or mental problem",1,2);
		UrgencyFactor uf5 = new UrgencyFactor("Healthy", 3);
		UrgencyFactor uf6 = new UrgencyFactor("Known Fatality",3);
		ArrayList<UrgencyFactor> f2 = new ArrayList<UrgencyFactor>();
		f2.add(uf4);
		f2.add(uf5);
		f2.add(uf6);
		UrgencyFactorGroup medical = new UrgencyFactorGroup(f2, "Medical Condition",validValues1to3);
		factorGroups.add(medical);

		UrgencyFactor uf7 = new UrgencyFactor("One Alone",1);
		UrgencyFactor uf8 = new UrgencyFactor("More than one (unless separated)", 2, 3);
		ArrayList<UrgencyFactor> f3 = new ArrayList<UrgencyFactor>();
		f3.add(uf7);
		f3.add(uf8);
		UrgencyFactorGroup number = new UrgencyFactorGroup(f3, "Number of Subjects",validValues1to3);
		factorGroups.add(number);

		UrgencyFactor uf9  = new UrgencyFactor("Inexperienced, does not know area",1);
		UrgencyFactor uf10 = new UrgencyFactor("Inexperienced, knows area", 1, 2);
		UrgencyFactor uf11 = new UrgencyFactor("Experienced, does not know area", 2);
		UrgencyFactor uf12 = new UrgencyFactor("Experienced, knows area",3);
		ArrayList<UrgencyFactor> f4 = new ArrayList<UrgencyFactor>();
		f4.add(uf9);
		f4.add(uf10);
		f4.add(uf11);
		f4.add(uf12);
		UrgencyFactorGroup experience = new UrgencyFactorGroup(f4, "Subject Experience",validValues1to3);
		factorGroups.add(experience);		

		UrgencyFactor uf13  = new UrgencyFactor("Past and/or existing hazardous weather",1);
		UrgencyFactor uf14 = new UrgencyFactor("Predicted hazardous weather (less than 8 hours away)", 1, 2);
		UrgencyFactor uf15 = new UrgencyFactor("Predicted hazardous weather (more than 8 hours away)",2);
		UrgencyFactor uf16 = new UrgencyFactor("No hazardous weather predicted",3);
		ArrayList<UrgencyFactor> f5 = new ArrayList<UrgencyFactor>();
		f5.add(uf13);
		f5.add(uf14);
		f5.add(uf15);
		f5.add(uf16);
		UrgencyFactorGroup weather = new UrgencyFactorGroup(f5, "Weather",validValues1to3);
		factorGroups.add(weather);		

		UrgencyFactor uf17 = new UrgencyFactor("Inadequete for environment and weather",1);
		UrgencyFactor uf18 = new UrgencyFactor("Questionable for environment and weather", 1, 2);
		UrgencyFactor uf19 = new UrgencyFactor("Adequete for environment and weather",3);
		ArrayList<UrgencyFactor> f6 = new ArrayList<UrgencyFactor>();
		f6.add(uf17);
		f6.add(uf18);
		f6.add(uf19);
		UrgencyFactorGroup equipment = new UrgencyFactorGroup(f6, "Equipment",validValues1to3);
		factorGroups.add(equipment);		

		UrgencyFactor uf20 = new UrgencyFactor("Known terrain or other hazards",1);
		UrgencyFactor uf21 = new UrgencyFactor("Few or no hazards", 2, 3);
		ArrayList<UrgencyFactor> f7 = new ArrayList<UrgencyFactor>();
		f7.add(uf20);
		f7.add(uf21);
		UrgencyFactorGroup terrain = new UrgencyFactorGroup(f7, "Terrain/Hazards",validValues1to3);
		factorGroups.add(terrain);

		HashSet<Integer> valid05 = new HashSet<Integer>();
		valid05.add(Integer.valueOf(-5));
		valid05.add(Integer.valueOf(-3));
		valid05.add(Integer.valueOf(0));
		valid05.add(Integer.valueOf(3));
		UrgencyFactor uf22 = new UrgencyFactor("None of Abandoned Vehicle,Runner,Child age 4-6,Substance Abuse,Despondent,Day Climber",0);
		UrgencyFactor uf23 = new UrgencyFactor("Any of the above missing less than 24 hours: ", -5, -5);
		UrgencyFactor uf24 = new UrgencyFactor("Any of the above missing 1-3 days ", -3, -3);
		UrgencyFactor uf25 = new UrgencyFactor("Any of the above missing more than 3 days ", 3);
		ArrayList<UrgencyFactor> f8 = new ArrayList<UrgencyFactor>();
		f8.add(uf22);
		f8.add(uf23);
		f8.add(uf24);
		f8.add(uf25);
		UrgencyFactorGroup urgent = new UrgencyFactorGroup(f8, "High Risk Modifiers (>24 hour survival < 50%)",valid05);
		factorGroups.add(urgent);			
		
		marshal(System.out);
  }

  public UrgencyScheme(File file) throws FileNotFoundException {
 
		FileInputStream stream = new FileInputStream(file);
		try {
			SearchUrgencyEstimationSchemeType scheme = unmarshal(SearchUrgencyEstimationSchemeType.class, stream);
			factorGroups = new ArrayList<UrgencyFactorGroup>();
			this.schemeName = scheme.getSchemeName();
			this.thresholds = (ArrayList<ThresholdType>) scheme.getThresholds();
			List<FactorGroupType> factorGroupList = scheme.getFactorGroup();
			Iterator<FactorGroupType> i  = factorGroupList.iterator();
			while (i.hasNext()) { 
				ArrayList<UrgencyFactor> ufList = new ArrayList<UrgencyFactor>();
				FactorGroupType factorGroupType = i.next();
				List<FactorType> factorTypeList = factorGroupType.getFactor();
				Iterator<FactorType> i1= factorTypeList.iterator();
				while (i1.hasNext()) { 
					FactorType ft = i1.next();
					ufList.add(new UrgencyFactor(ft.getName(),ft.getMinValue(),ft.getMaxValue()));
				}
				HashSet<Integer> valid = new HashSet<Integer>();
				Iterator<Integer> iv = factorGroupType.getValidValue().iterator();
				while (iv.hasNext()) {
					valid.add(iv.next());
				}
				UrgencyFactorGroup ufGroup = new UrgencyFactorGroup(ufList, factorGroupType.getName(),valid);
				if (factorGroupType.getSelectedValue()!=null) { 
				     try {
						ufGroup.setSelectedValue(factorGroupType.getSelectedValue());
					} catch (BadValueException e) {
						// TODO Auto-generated catch block
					}
				}
				if (factorGroupType.getSelectionNote()!=null) { 
					ufGroup.setSelectionNote(factorGroupType.getSelectionNote());
				}
				factorGroups.add(ufGroup);
			}
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }

  private net.aa3sd.SMT.generated.urgency.SearchUrgencyEstimationSchemeType unmarshal(Class<SearchUrgencyEstimationSchemeType> docClass, InputStream inputStream) throws JAXBException {
		String packageName = docClass.getPackage().getName();
		JAXBContext jc = JAXBContext.newInstance( packageName );
		Unmarshaller u = jc.createUnmarshaller();
		JAXBElement<SearchUrgencyEstimationSchemeType> doc = (JAXBElement<SearchUrgencyEstimationSchemeType>)u.unmarshal( inputStream );
		return doc.getValue();
  }

  public void marshal(OutputStream outputStream) {
		System.out.println("Marshal called.");
		ObjectFactory urgencyObjectFactory = new ObjectFactory();
		SearchUrgencyEstimationSchemeType scheme = urgencyObjectFactory.createSearchUrgencyEstimationSchemeType();
		scheme.setSchemeName(this.schemeName);
		Iterator<ThresholdType> th = thresholds.iterator();
		while (th.hasNext()) { 
			scheme.getThresholds().add(th.next());
		}
        Iterator <UrgencyFactorGroup> ufgi = factorGroups.iterator();
        while (ufgi.hasNext()) { 
        	UrgencyFactorGroup fg = ufgi.next();
        	FactorGroupType factorGroup = urgencyObjectFactory.createFactorGroupType();
        	factorGroup.setName(fg.getName());
        	Iterator<Integer> validi = (Iterator<Integer>)fg.getValidValues().iterator();
        	while (validi.hasNext()) { 
        	    factorGroup.getValidValue().add(validi.next());
        	}
        	Iterator<UrgencyFactor> ufi = fg.getFactors().iterator();
        	while (ufi.hasNext()) { 
        		UrgencyFactor uf = ufi.next();
        		FactorType factor = urgencyObjectFactory.createFactorType();
        		factor.setName(uf.getName());
        		factor.setMaxValue(uf.getMaxValue());
        		factor.setMinValue(uf.getMinValue());
        		factorGroup.getFactor().add(factor);
        	}
        	if(fg.isValidValue(fg.getSelectedValue())) { 
        		factorGroup.setSelectedValue(fg.getSelectedValue());
        		factorGroup.setSelectionNote(fg.getSelectionNote());
        	}
        	scheme.getFactorGroup().add(factorGroup);
        }
        try {
            JAXBElement<SearchUrgencyEstimationSchemeType> schemeXml =
            	urgencyObjectFactory.createSearchUrgencyEstimationScheme(scheme);
            JAXBContext jc = JAXBContext.newInstance(SearchUrgencyEstimationSchemeType.class, FactorGroupType.class, FactorType.class);
            Marshaller m = jc.createMarshaller();
            m.marshal( schemeXml, outputStream );
            System.out.println("Marshal complete.");
        } catch( JAXBException jbe ){
        	System.out.println("Marshal failed.");
            jbe.printStackTrace(System.out);
        }

        
  }

  /**
   * @param schemeName the schemeName to set
   */
  public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
  }

  /**
   * @return the schemeName
   */
  public String getSchemeName() {
		return schemeName;
  }

  public List<UrgencyFactor> getFactors() {
		ArrayList<UrgencyFactor> result = new ArrayList<UrgencyFactor>();
		Iterator<UrgencyFactorGroup> i = factorGroups.iterator();
		while (i.hasNext()) {
			UrgencyFactorGroup g = i.next();
			result.addAll(g.getFactors());
		}
		return result; 
  }

  public List<UrgencyFactorGroup> getFactorGroups() {
 
		return factorGroups;		
  }

  /**
   *  (non-Javadoc)
   * @see javax.swing.table.TableModel#getColumnClass(int)
   */
  @Override
  public Class<?> getColumnClass(int columnIndex) {
		Class<?> result = Object.class;
		switch (columnIndex) { 
		case 0:
		   result = String.class;
		   break;
		case 1:
		   result = Document.class;
		   break;
		case 2:
		   result = Integer.class;
		   break;
		case 3:
		   result = String.class;
		   break;		   
		}
		return result;
  }

  /**
   *  (non-Javadoc)
   * @see javax.swing.table.TableModel#getColumnCount()
   */
  @Override
  public int getColumnCount() {
		return 4;
  }

  /**
   *  (non-Javadoc)
   * @see javax.swing.table.TableModel#getColumnName(int)
   */
  @Override
  public String getColumnName(int columnIndex) {
		String result = "";
		switch (columnIndex) { 
		case 0:
		   result = "Urgency Factor";
		   break;
		case 1:
		   result = "Conditions/Scores";
		   break;
		case 2:
		   result = "Score";
		   break;
		case 3:
		   result = "Notes";
		   break;		   
		}
		return result;
  }

  /**
   *  (non-Javadoc)
   * @see javax.swing.table.TableModel#getRowCount()
   */
  @Override
  public int getRowCount() {
		return factorGroups.size();
  }

  /**
   *  (non-Javadoc)
   * @see javax.swing.table.TableModel#getValueAt(int, int)
   */
  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
		Object result = null;
		UrgencyFactorGroup f = factorGroups.get(rowIndex);
		switch (columnIndex) { 
	    case 0:
		   result = f.getName();
		   break;
		case 1:
		   Iterator<UrgencyFactor> i = f.getFactors().iterator();
		   String sresult = "";
		   while (i.hasNext()) { 
			   UrgencyFactor factor = i.next();
			   String values = "" + factor.getMaxValue();
	           if (factor.getMinValue()!=factor.getMaxValue()) {
	               values =  factor.getMinValue() + "-" + factor.getMaxValue();
	           }
			   sresult = sresult + factor.getName() + ": " + values + " \n";
		   }
		   StringContent c = new StringContent();
			try {
				c.insertString(0, sresult);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		   Document d = new PlainDocument(c);
		   result = d;
		   break;
		case 2:		   
		   result = f.getSelectedValue();
		   break;
		case 3:		   
		   result = f.getSelectionNote();
		   break;		   
		}
		return result;
  }

  /**
   *  (non-Javadoc)
   * @see javax.swing.table.TableModel#isCellEditable(int, int)
   */
  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
		boolean result = false;
		if (columnIndex==2 || columnIndex==3) {  
			result = true;
		}
		return result;
  }

  /**
   *  (non-Javadoc)
   * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
   */
  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (isCellEditable(rowIndex,columnIndex))  {
			UrgencyFactorGroup f = factorGroups.get(rowIndex);
			if (columnIndex==2) { 			
			try {
				Integer i = (Integer) aValue;
				f.setSelectedValue(i.intValue());
				SMTSingleton.getSingletonInstance().getActiveLog().LogAnEvent(new LogEvent("SearchUrgency",LogEvent.EVENT_SEARCH,"Urgency Value " + f.getName() + "=" + f.getSelectedValue() ));
			} catch (BadValueException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			if (columnIndex==3) { 
				f.setSelectionNote(aValue.toString());
			}
			fireTableCellUpdated(rowIndex, columnIndex);
		}
	    
  }

  /**
   * 
   * @return the sum of the selected values for all factors
   */
  public int getTotal() {
	   int total = 0;
	   Iterator<UrgencyFactorGroup> i = factorGroups.iterator();
	   while (i.hasNext()) {
	       total = total + i.next().getSelectedValue();	   
	   }
	   return total;
  }

  public int getThresholdEmergency() {
		int result = 0;
		Iterator<ThresholdType> i =thresholds.iterator();
		while (i.hasNext()) {
			ThresholdType t = i.next();
			if (t.getOrdinalPosition()==1) { 
				result = t.getUpperBound();
			}
		}
		return result;
  }

  public int getThresholdUrgent() {
		int result = 0;
		Iterator<ThresholdType> i =thresholds.iterator();
		while (i.hasNext()) {
			ThresholdType t = i.next();
			if (t.getOrdinalPosition()==2) { 
				result = t.getUpperBound();
			}
		}
		return result;
  }

  public boolean isEmergency() {
		boolean result = false;
		if (isComplete() && getTotal()< getThresholdEmergency()) { 
			result = true;
		}
		return result;
  }

  public boolean isUrgent() {
		boolean result = false;
		if (isComplete() && getTotal()>= getThresholdEmergency() && getTotal() < getThresholdUrgent()) { 
			result = true;
		}
		return result;
  }

  public net.aa3sd.SMT.generated.urgency.ThresholdType getThresholdOfType(ArrayList<ThresholdType> list, int level) {
		ThresholdType result = null;
		Iterator<ThresholdType> i =thresholds.iterator();
		while (i.hasNext()) {
			ThresholdType t = i.next();
			if (t.getOrdinalPosition()==level) { 
				result = t;
			}
		}		
		return result;
  }

  public String getUrgency() {
 
	   String result = 	DEFAULT_TEXT;
	   if (isComplete())  {
		   if (isEmergency()) {
			   result =  getThresholdOfType(thresholds,1).getMessage() + " (" + getTotal() + ")" ;
		   } else { 
			   if (isUrgent()) { 
				   result = getThresholdOfType(thresholds,2).getMessage() + " (" + getTotal() + ")";   
			   } else { 
				   result = getThresholdOfType(thresholds,3).getMessage() + " ("  + getTotal() + ")";
			   }
		   }
		   SMTSingleton.getSingletonInstance().getActiveLog().LogAnEvent(new LogEvent("SearchUrgency",LogEvent.EVENT_SEARCH,"Urgency: " + result));
	   }
	   return result;
  }

  /**
   * 
   * @return true if at least one factor in each group has a value.
   */
  public boolean isComplete() {
 
		boolean result = true;
		Iterator<UrgencyFactorGroup> igroups = factorGroups.iterator();
		int groupSum = 0;
		int groupCount = 0;
		while (igroups.hasNext()) {
			UrgencyFactorGroup f = igroups.next();
			if (f.isValidValue(f.getSelectedValue())) { groupSum++; } 
			//if (igroups.next().getSelectedValue() != 0) { groupSum++; } 
			groupCount++;
		}
		if (groupSum<groupCount) {
			result = false;
		}		
		return result;
  }

  public static void main(String[] args)
  {
 
		UrgencyScheme defaultscheme = new UrgencyScheme();
		FileOutputStream file;
		String outfile = "defaulturgencyscheme.xml";
		try {
			file = new FileOutputStream(outfile);	   
			defaultscheme.marshal(file);
			System.out.println("Wrote default scheme to " + outfile);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}

  }

}
