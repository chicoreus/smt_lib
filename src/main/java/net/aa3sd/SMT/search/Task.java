/**
 * Task.java
 * Created Sep 19, 2011 7:03:08 PM
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontStyle;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPage;
import com.itextpdf.text.pdf.PdfWriter;
import net.aa3sd.SMT.SMTProperties;
import net.aa3sd.SMT.SMTSingleton;
import net.aa3sd.SMT.interfaces.Assignable;
import net.aa3sd.SMT.log.LogEvent;
import net.aa3sd.SMT.resources.Resource;
/**
 * @author mole
 */
public class Task extends AbstractTableModel {
  public static final int KIND_NO_RESOURCE_SPECIFIED =  0;

  public static final int KIND_SINGLE_RESOURCE =  1;

  public static final int KIND_STRIKE_TEAM =  2;

  public static final int KIND_TASK_FORCE =  3;

  private static final Logger log =  Logger.getLogger(Task.class);

  /**
   *  single resources, strike team, task force;
   */
  private List<net.aa3sd.SMT.resources.Resource> resources;

  private String assignmentSummary;

  private StringBuffer assignmentDetails;

  /**
   *  planned, assigned, completed;
   */
  private String state;

  /**
   *  task name 
   */
  private String identifier;

  /**
   *  leader of the task
   */
  private String leader;

  /**
   *  Segment, Route, or Location to which this task is assigned
   */
  private net.aa3sd.SMT.interfaces.Assignable assignedToSearch;

  public Task() {
 
		resources = new ArrayList<Resource>();
		assignmentDetails = new StringBuffer();
		state = "Planned";
		leader = "";
  }

  public int getKind() {
 
		int result = KIND_NO_RESOURCE_SPECIFIED;		
		if (resources.size()>0) { 
			if (resources.size()==1) { 
				result = KIND_SINGLE_RESOURCE;
			} else { 
				Iterator<Resource> i = resources.iterator();
				// Strike team - two or more of the same resource
				result = KIND_STRIKE_TEAM;
				Resource previous = i.next();
				while (i.hasNext()) { 
					Resource current = i.next();
					if (!previous.getType().equals(current.getType())) {
						// Task force - two or more resources of different types
						result = KIND_TASK_FORCE;
					}
					previous = current;
				}
			}
		}
		return result;
  }

  public String getKindName() {
 
		if (getKind()==KIND_SINGLE_RESOURCE) return "Single Resource";
		if (getKind()==KIND_STRIKE_TEAM) return "Strike Team";
		if (getKind()==KIND_TASK_FORCE) return "Task Force";
		return "No Resource Specified";
  }

  /**
   * @return the resources
   */
  public List<net.aa3sd.SMT.resources.Resource> getResource() {
		return resources;
  }

  public int getPersonnelCount() {
		int result = 0;
		Iterator<Resource> i = resources.iterator();
		while (i.hasNext()) { 
		 	result = result + i.next().getNumberOfPeople();
		}
		return result;
  }

  /**
   * @param resources the resources to set
   */
  public void setResource(List<Resource> resources) {
		this.resources = resources;
  }

  public void addResource(net.aa3sd.SMT.resources.Resource resource) {
 
		log.debug("Added resource " + resource.getType() + " to task " + identifier);
		fireTableDataChanged();
		this.resources.add(resource);
  }

  /**
   * @return the assignmentSummary
   */
  public String getAssignmentSummary() {
		return assignmentSummary;
  }

  /**
   * @param assignmentSummary the assignmentSummary to set
   */
  public void setAssignmentSummary(String assignmentSummary) {
		this.assignmentSummary = assignmentSummary;
  }

  /**
   * @return the assignmentDetails
   */
  public StringBuffer getAssignmentDetails() {
		return assignmentDetails;
  }

  /**
   * @param assignmentDetails the assignmentDetails to set
   */
  public void setAssignmentDetails(StringBuffer assignmentDetails) {
		this.assignmentDetails = assignmentDetails;
  }

  /**
   * @return the leader
   */
  public String getLeader() {
		return leader;
  }

  /**
   * @param leader the leader to set
   */
  public void setLeader(String leader) {
		SMTSingleton.getSingletonInstance().getActiveLog().LogAnEvent(new LogEvent("Task",LogEvent.EVENT_SEARCH,"Set leader for " + getIdentifier() + " to " + this.leader));
		this.leader = leader;
  }

  /**
   * @return the state
   */
  public String getState() {
		return state;
  }

  /**
   * @param state the state to set
   */
  public void setState(String state) {
		SMTSingleton.getSingletonInstance().getActiveLog().LogAnEvent(new LogEvent("Task",LogEvent.EVENT_SEARCH,"Set status for " + getIdentifier() + " to " + this.state + ". Assignment " + assignmentSummary));
		this.state = state;
  }

  /**
   * @return the identifier
   */
  public String getIdentifier() {
		return identifier;
  }

  /**
   * @param identifier the identifier to set
   */
  public void setIdentifier(String identifier) {
		this.identifier = identifier;
  }

  /**
   * @return the assignedToSearch
   */
  public net.aa3sd.SMT.interfaces.Assignable getAssignedToSearch() {
		return assignedToSearch;
  }

  /**
   * @param assignedToSearch the assignedToSearch to set
   */
  public void setAssignedToSearch(net.aa3sd.SMT.interfaces.Assignable assignedToSearch) {
		SMTSingleton.getSingletonInstance().getActiveLog().LogAnEvent(new LogEvent("Task",LogEvent.EVENT_SEARCH,getIdentifier() + " assigned to " + assignedToSearch.getName()));
		this.assignedToSearch = assignedToSearch;
  }

  /**
   *  (non-Javadoc)
   * @see javax.swing.table.TableModel#getRowCount()
   */
  @Override
  public int getRowCount() {
		return resources.size();
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
   * @see javax.swing.table.AbstractTableModel#getColumnName(int)
   */
  @Override
  public String getColumnName(int columnIndex) {
		if (columnIndex==0) return "Type";
		if (columnIndex==1) return "Name";
		if (columnIndex==2) return "Status";
		if (columnIndex==3) return "# of People";
		return "";
  }

  /**
   *  (non-Javadoc)
   * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
   */
  @Override
  public Class<?> getColumnClass(int columnIndex) {
		Class<?> result = Object.class;
		    switch (columnIndex) { 
		       case 0: 
		          result = String.class;
		          break;
		       case 1: 
		          result = String.class;
		          break;
		       case 2: 
		          result = String.class;
		          break;
		       case 3: 
		          result = Integer.class;
		          break;
		    } 
		return result;
  }

  /**
   *  (non-Javadoc)
   * @see javax.swing.table.TableModel#getValueAt(int, int)
   */
  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
		Object result = null;
		if (rowIndex>=0 & rowIndex <= resources.size()) { 
		    switch (columnIndex) { 
		       case 0: 
		          result = resources.get(rowIndex).getType();
		          break;
		       case 1: 
		          result = resources.get(rowIndex).getName();
		          break;
		       case 2: 
		          result = resources.get(rowIndex).getStatusString();
		          break;
		       case 3: 
		          result = resources.get(rowIndex).getNumberOfPeople();
		          break;
		    } 
	    }
		return result;
  }

  /**
   *  (non-Javadoc)
   * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
   */
  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
		boolean result = false;
		if (columnIndex==1) { result = true; } 
		return result;
  }

  public void writeToPdf(String filename) {
 
    	Document document = new Document();
    	if ("A4".equals(SMTSingleton.getSingletonInstance().getProperties().getProperties().get(SMTProperties.KEY_PAPERSIZE))) { 
    	    document.setPageSize(PageSize.A4);
    	} else { 
    	    document.setPageSize(PageSize.LETTER);
    	}
    	try {
			PdfWriter.getInstance(document, new FileOutputStream(filename));
			document.open();
			
			writeToPdf(document);
			
			document.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
  }

  public void writeToPdf(Document document) {
    	 try { 
			int columns = 3;
			
			PdfPTable table = new PdfPTable(columns);
			table.setWidthPercentage(100);
			table.setHorizontalAlignment(Element.ALIGN_CENTER);
			
			// Row 1
			
			Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Font.BOLD);
			Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL);
			
			Paragraph name = assembleFormCell("1. Incident Name:", SMTSingleton.getSingletonInstance().getCurrentSearch().getName());
		    PdfPCell cell2 = new PdfPCell(name);
			cell2.setBorderWidth(1);
			cell2.setPadding(2);
			cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
		    table.addCell(cell2);

		    PdfPCell cellop = new PdfPCell(assembleFormCell("2. Operational Period: ", ""));
			cellop.setBorderWidth(1);
			cellop.setPadding(2);
			cellop.setHorizontalAlignment(Element.ALIGN_LEFT);
		    table.addCell(cellop);		    
		    
			PdfPCell cell = new PdfPCell(new Phrase("ICS 204a-OS", boldFont));
			cell.setBorderWidth(1);
			cell.setPadding(2);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);
			
			// Row 2
			
		    PdfPCell cell3 = new PdfPCell(assembleFormCell("3.  Branch:",""));
			cell3.setBorderWidth(1);
			cell3.setPadding(2);
			table.addCell(cell3);
			
		    PdfPCell celldiv = new PdfPCell(assembleFormCell("4. Division/Group:",""));
		    celldiv.setColspan(2);
			celldiv.setBorderWidth(1);
			celldiv.setPadding(2);
			table.addCell(celldiv);
			
			// Row 3
			
			Paragraph id = new Paragraph();
			String kindName = "Strike Team/Task Force";
			if (this.getKind()!= Task.KIND_NO_RESOURCE_SPECIFIED) { 
				kindName = this.getKindName();
			}
		    PdfPCell cell4 = new PdfPCell(assembleFormCell("5. " + kindName +  "  Resource Identifier",getIdentifier()));
			cell4.setBorderWidth(1);
			cell4.setPadding(2);
			table.addCell(cell4);
			
		    PdfPCell cell5 = new PdfPCell(assembleFormCell("6. Leader:", getLeader()));
			cell5.setBorderWidth(1);
			cell5.setPadding(2);
			table.addCell(cell5);			
			
		    PdfPCell cellloc = new PdfPCell(assembleFormCell("7. Assignment Location:",""));
			cellloc.setBorderWidth(1);
			cellloc.setPadding(2);
			table.addCell(cellloc);	
			
			// Row 4
			
            Paragraph paragraph = new Paragraph();
            paragraph.add(new Paragraph("8. Team Members:", boldFont));
			Iterator<Resource> i = resources.iterator();
			log.debug("Printing resources for task, resource count=" + resources.size());
		    while (i.hasNext()) { 
		    	Resource resource = i.next();
			    log.debug(resource.getName());
		    	String resourceName = "";
		    	if (resource.getName()!=null)  resourceName = resource.getName();
		    	String numberOfPeople = "";
		    	if (resource.getNumberOfPeople()>0) numberOfPeople = Integer.toString(resource.getNumberOfPeople());
		    	paragraph.add(new Paragraph(resource.getType() + " " + resourceName + " " + numberOfPeople, normalFont ));
		    }
		    PdfPCell cell6 = new PdfPCell(paragraph);
		    cell6.setColspan(3);
			cell6.setBorderWidth(1);
			cell6.setPadding(2);	
			cell6.setPaddingBottom(50);
			cell6.setFixedHeight(PageSize.LETTER.getHeight()/4.1f);
			table.addCell(cell6);		 

			// Row 5
			
			Paragraph paragraph6 = new Paragraph();
			paragraph6.add(new Paragraph("9. Work Assignment", boldFont));
			paragraph6.add(new Paragraph(new Phrase(this.getAssignmentSummary(),normalFont)));
		    paragraph6.add(new Paragraph(this.getAssignmentDetails().toString(),normalFont));			
			
		    PdfPCell cell7 = new PdfPCell(paragraph6);
		    cell7.setColspan(3);
			cell7.setBorderWidth(1);
			cell7.setPadding(2);		    
			cell7.setFixedHeight(PageSize.LETTER.getHeight()/2.3f);
			table.addCell(cell7);				
			
			// Row 6

		    PdfPCell cellcom = new PdfPCell(assembleFormCell("10. Communications",""));
		    cellcom.setColspan(3);
			cellcom.setBorderWidth(1);
			cellcom.setPadding(2);
			table.addCell(cellcom);			
			
			// Row 7
			
		    PdfPCell cellprep = new PdfPCell(assembleFormCell("11. Prepared By:",""));
		    cellprep.setColspan(2);
			cellprep.setBorderWidth(1);
			cellprep.setPadding(2);
			table.addCell(cellprep);	
			
			DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		    PdfPCell cellpd = new PdfPCell(assembleFormCell("12. Date/Time: ",dateFormatter.format(new Date()) ));
			cellpd.setBorderWidth(1);
			cellpd.setPadding(2);
			table.addCell(cellpd);	
			
		    document.add(table);
		    
		    Paragraph footer = new Paragraph("ICS 204a-OS: Ground Search and Rescue Task Assignment Form");
		    footer.setAlignment(Element.ALIGN_CENTER);
		    document.add(footer);
		    
			document.newPage();

		    SMTSingleton.getSingletonInstance().getActiveLog().LogAnEvent(new LogEvent("Task",LogEvent.EVENT_SEARCH,"ICS-204aOS for " + getIdentifier() + ". Generated pdf with " + this.leader + " " + this.assignmentSummary + " " + this.state + " # of People: " + this.getPersonnelCount()));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

  }

  /**
   *  Supporting function for pdf generation, creates a paragraph to put into a cell on
   * form containing a heading line in bold followed by the body not in bold. 
   * 
   * @param heading the text to begin the cell with, placed in bold.
   * @param body the text to follow the heading, not in bold
   * @return a Paragraph containing the heading and body.
   */
  private Paragraph assembleFormCell(String heading, String body) {
    	Paragraph result = new Paragraph();
		Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Font.BOLD);
		Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL);
    	if (body==null) body = "";
    	if (heading==null) heading = "";
    	result.add(new Paragraph(heading, boldFont));
    	result.add(new Paragraph(body));
    	return result;
  }

  /**
   *  (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
		return this.getIdentifier() + " " + this.getKindName() + " " + this.getAssignmentSummary();
  }

}
