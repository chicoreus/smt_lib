/**
 * TaskList.java
 * Created Sep 20, 2011 8:25:12 AM
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import org.apache.log4j.Logger;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import net.aa3sd.SMT.SMTSingleton;
import net.aa3sd.SMT.interfaces.SubjectListener;
import net.aa3sd.SMT.interfaces.TaskListListener;
import net.aa3sd.SMT.log.LogEvent;
import net.aa3sd.SMT.resources.Resource;
/**
 * @author mole
 */
public class TaskList extends AbstractTableModel {
  private static final Logger log =  Logger.getLogger(TaskList.class);

  private static final long serialVersionUID =  -3834061282923184587L;

  private ArrayList<Task> tasks;

  private Vector<net.aa3sd.SMT.interfaces.TaskListListener> listeners;

  private int taskCount;

  public TaskList() {
 
		listeners = new Vector<TaskListListener>();
		taskCount = 0;
		tasks = new ArrayList<Task>();
  }

  public void addTask(Task task) {
 
		taskCount++;
		task.setIdentifier("Task " + taskCount);
		tasks.add(task);
		SMTSingleton.getSingletonInstance().getActiveLog().LogAnEvent(new LogEvent("Tasks",LogEvent.EVENT_SEARCH,"Added a task: " + task.getAssignmentSummary()));
		this.fireTableDataChanged();
		notifyListeners();
  }

  /**
   * @return the tasks as an ArrayList.
   */
  public ArrayList<Task> getTasks() {
		return tasks;
  }

  /**
   *  (non-Javadoc)
   * @see javax.swing.table.TableModel#getRowCount()
   */
  @Override
  public int getRowCount() {
		return tasks.size();
  }

  /**
   *  (non-Javadoc)
   * @see javax.swing.table.TableModel#getColumnCount()
   */
  @Override
  public int getColumnCount() {
		return 5;
  }

  /**
   *  (non-Javadoc)
   * @see javax.swing.table.AbstractTableModel#getColumnName(int)
   */
  @Override
  public String getColumnName(int columnIndex) {
		if (columnIndex==0) return "Task Name";
		if (columnIndex==1) return "Assignment";
		if (columnIndex==2) return "State";
		if (columnIndex==3) return "Search";
		if (columnIndex==4) return "Resource Type";
		return "";
  }

  /**
   *  (non-Javadoc)
   * @see javax.swing.table.TableModel#getValueAt(int, int)
   */
  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex==0) return tasks.get(rowIndex).getIdentifier();
		if (columnIndex==1) return tasks.get(rowIndex).getAssignmentSummary();
		if (columnIndex==2) return tasks.get(rowIndex).getState();
		if (columnIndex==3) {
			if (tasks.get(rowIndex).getAssignedToSearch()==null) {
				return "";
			} else { 
			    return tasks.get(rowIndex).getAssignedToSearch().getName();
			}
		}
		if (columnIndex==4) return tasks.get(rowIndex).getKindName();
		return null;
  }

  public void registerTaskListListener(net.aa3sd.SMT.interfaces.TaskListListener listener) {
 
		listeners.add(listener);
  }

  public Task getTaskAt(int rowIndex) {
 
		return tasks.get(rowIndex);
  }

  private void notifyListeners() {
 
		Iterator<TaskListListener> i = listeners.iterator();
		while (i.hasNext()) { 
			i.next().taskListChanged();
		}
  }

  /**
   * Generate a ICS-204 task assignment summary
   * 
   * @param document
   */
  public void writeToPdf(Document document) {
		try { 
			int columns = 3;

			Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Font.BOLD);
			Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL);

			Paragraph header = new Paragraph("ASSIGNMENT LIST (ICS 204)", boldFont);
			header.setSpacingAfter(3f);
			header.setAlignment(Element.ALIGN_CENTER);
			document.add(header);

			PdfPTable table = new PdfPTable(columns);
			table.setWidthPercentage(100);
			table.setHorizontalAlignment(Element.ALIGN_CENTER);

			// Row 1

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
			
			// Row 1 flowing into row 2

			Paragraph p3 = new Paragraph();
			p3.add(new Paragraph("3. Branch:",boldFont));
			p3.add(new Paragraph("Division:",boldFont));
			p3.add(new Paragraph("Group:",boldFont));
			p3.add(new Paragraph("Staging Area:",boldFont));
			PdfPCell cell3 = new PdfPCell(p3);
			cell3.setBorderWidth(1);
			cell3.setRowspan(2);     // Two rows tall **
			cell3.setPadding(2);
			table.addCell(cell3);

			// Row 2

			Paragraph ppers = new Paragraph();
			ppers.add(new Paragraph("4. Operations Personnel:",boldFont));
			ppers.add(new Paragraph("Operations Section Chief:",normalFont));
			ppers.add(new Paragraph("Branch Director:",normalFont));
			ppers.add(new Paragraph("Division/Group Supervisor:",normalFont));
			PdfPCell celldiv = new PdfPCell(ppers);
			celldiv.setColspan(2);
			celldiv.setBorderWidth(1);
			celldiv.setPadding(2);
			table.addCell(celldiv);

			// Row 3

			Paragraph paragraph = new Paragraph();
			paragraph.add(new Paragraph("5. Resources Assigned:", boldFont));

			paragraph.add(new Paragraph("Resource Identifier, Leader, # of Persons", normalFont ));
			Iterator<Task> i = tasks.iterator();
			while (i.hasNext()) { 
				Task task = i.next();
				String taskName = "";
				if (task.getIdentifier()!=null)  taskName = task.getIdentifier();
				paragraph.add(new Paragraph(taskName + ", " + task.getLeader() + ", " + task.getPersonnelCount(), normalFont ));
			}
			PdfPCell cell6 = new PdfPCell(paragraph);
			cell6.setColspan(3);
			cell6.setBorderWidth(1);
			cell6.setPadding(2);	
			cell6.setPaddingBottom(50);
			cell6.setFixedHeight(PageSize.LETTER.getHeight()/4);
			table.addCell(cell6);		 

			// Row 4


			PdfPCell cellwa = new PdfPCell();
			cellwa.addElement(new Paragraph("6. Work Assignments",boldFont));
			i = tasks.iterator();
			paragraph = new Paragraph();
			while (i.hasNext()) { 
				Task task = i.next();
				String taskName = "";
				if (task.getIdentifier()!=null)  taskName = task.getIdentifier();
				paragraph.add(new Phrase(taskName + ": ", boldFont ));
				paragraph.add(new Phrase(task.getAssignmentSummary() + " ", normalFont ));
			}
			cellwa.addElement(paragraph);
			cellwa.setColspan(3);
			cellwa.setFixedHeight(PageSize.LETTER.getHeight()/6);
			cellwa.setBorderWidth(1);
			cellwa.setPadding(2);
			table.addCell(cellwa);					

			// Row 5

			PdfPCell cellsi = new PdfPCell(assembleFormCell("7. Special Instructions",""));
			cellsi.setColspan(3);
			cellsi.setFixedHeight(PageSize.LETTER.getHeight()/6.3f);
			cellsi.setBorderWidth(1);
			cellsi.setPadding(2);
			table.addCell(cellsi);	

			// Row 6
			PdfPCell cellcom = new PdfPCell(assembleFormCell("8. Communications",""));
			cellcom.setColspan(3);
			cellcom.setFixedHeight(PageSize.LETTER.getHeight()/6.3f);
			cellcom.setBorderWidth(1);
			cellcom.setPadding(2);
			table.addCell(cellcom);				

			// Row 7
			PdfPCell cellprep = new PdfPCell(assembleFormCell("11. Prepared By:",""));
			cellprep.setColspan(3);
			cellprep.setBorderWidth(1);
			cellprep.setPadding(2);
			table.addCell(cellprep);	

			// Row 8
			
			PdfPCell cell = new PdfPCell(new Phrase("ICS 204", boldFont));
			cell.setBorderWidth(1);
			cell.setPadding(2);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);
			
			PdfPCell cellp = new PdfPCell(new Phrase("IAP Page ____", boldFont));
			cellp.setBorderWidth(1);
			cellp.setPadding(2);
			cellp.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cellp);
			
			DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
			PdfPCell cellpd = new PdfPCell(assembleFormCell("12. Date/Time: ",dateFormatter.format(new Date()) ));
			cellpd.setBorderWidth(1);
			cellpd.setPadding(2);
			table.addCell(cellpd);	

			document.add(table);


			document.newPage();

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

}
