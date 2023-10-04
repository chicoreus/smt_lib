/**
 * Search.java
 * Created Oct 14, 2008 5:53:55 AM
 * 
 * © Copyright 2008 Paul J. Morris 
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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.AbstractTableModel;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openjump.core.ui.plugin.edittoolbox.cursortools.AutoCompletePolygonCursorTool;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.ui.LayerNamePanel;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelProxy;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelProxy;
import com.vividsolutions.jump.workbench.ui.TitledPopupMenu;
import com.vividsolutions.jump.workbench.ui.WorkbenchToolBar;
import com.vividsolutions.jump.workbench.ui.cursortool.CursorTool;
import net.aa3sd.SMT.SMTSingleton;
import net.aa3sd.SMT.interfaces.Assignable;
import net.aa3sd.SMT.interfaces.SubjectListener;
import net.aa3sd.SMT.jumpplugins.cursortools.DrawLocationTool;
import net.aa3sd.SMT.log.LogEvent;
import net.aa3sd.SMT.math.BadCalculationException;
import net.aa3sd.SMT.math.Utility;
import net.aa3sd.SMT.ui.AddSegmentDialog;
import net.aa3sd.SMT.ui.MapInternalFrame;
import net.aa3sd.SMT.ui.TaskPickerDialog;
/**
 * @author mole
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Search extends AbstractTableModel {
  private static final long serialVersionUID =  1859759783659098560L;

  private static final Log log =  LogFactory.getLog(Search.class);

  /**
   *  name of the search
   */
  private String name;

  /**
   *  list of segments in the search
   */
  private ArrayList<Segment> segments;

  /**
   *  list of locations (e.g. PLS) in the search
   */
  private ArrayList<Location> locations;

  /**
   *  list of trails/drainages/linear features in the search
   */
  private ArrayList<Route> routes;

  /**
   *  TODO: Allow array of multiple subjects.
   *  the subject of the search  
   */
  private Subject subject;

  /**
   *  requested, completed, assigned tasks.
   */
  private TaskList tasks;

  private Terrain terrain;

  private Vector<net.aa3sd.SMT.interfaces.SubjectListener> listeners;

  private SubjectCategoryScheme subjectCategoryScheme;

  private int clueCount;

  /**
   * Constructor
   */
  public Search() {
		name = "";
	    segments = new ArrayList<Segment>();
	    locations = new ArrayList<Location>();
	    routes = new ArrayList<Route>();
	    subject = new Subject("",1);
	    tasks = new TaskList();
	    terrain = new Terrain("Unset");
	    listeners = new Vector<SubjectListener>();
	    subjectCategoryScheme = new SubjectCategoryScheme();
	    clueCount = 0;
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
		SMTSingleton.getSingletonInstance().getActiveLog().LogAnEvent(new LogEvent("Search",LogEvent.EVENT_SEARCH,"Set name of search to: " + this.name));
		notifyListeners();
  }

  public ArrayList<Route> getRoutes() {
 
		return routes;
  }

  public void addRoute(Route aRoute) {
 
		routes.add(aRoute);
  }

  public ArrayList<Location> getLocations() {
		return locations;
  }

  public void addLocation(Location aLocation) {
 
		locations.add(aLocation);
  }

  /**
   * @return the segments
   */
  public ArrayList<Segment> getSegments() {
		return segments;
  }

  /**
   * @param segments the segments to set
   */
  public void addSegment(Segment aSegment) {
		this.segments.add(aSegment);
		SMTSingleton.getSingletonInstance().getActiveLog().LogAnEvent(new LogEvent("Search",LogEvent.EVENT_SEARCH,"Added Segment " + aSegment.getName()));
		fireTableDataChanged();
  }

  public Segment getSegmentByName(String name) {
		Iterator<Segment> i = segments.iterator();
		while (i.hasNext()) { 
			Segment s = i.next();
			if (s.getName().equals(name)) { 
				return s;
			}
		}
		return null;
  }

  /**
   * @return the subject
   */
  public Subject getSubject() {
		return subject;
  }

  /**
   * @param subject the subject to set
   */
  public void setSubject(Subject subject) {
		this.subject = subject;
		SMTSingleton.getSingletonInstance().getActiveLog().LogAnEvent(new LogEvent("Search",LogEvent.EVENT_SEARCH,"Set subject of search " + this.subject.getName()));
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
   * @see javax.swing.table.TableModel#getRowCount()
   */
  @Override
  public int getRowCount() {
		return segments.size();
  }

  /**
   * @return the terrain
   */
  public Terrain getTerrain() {
		return terrain;
  }

  /**
   * @param terrain the terrain to set
   */
  public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
		SMTSingleton.getSingletonInstance().getActiveLog().LogAnEvent(new LogEvent("Search",LogEvent.EVENT_SEARCH,"Terrain for search set as: " + this.terrain.getName()));
		notifyListeners();
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
   * 
   * /* (non-Javadoc)
   * @see javax.swing.table.TableModel#getValueAt(int, int)
   */
  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
		Object returnvalue = null;
		if (columnIndex == 0) {
			returnvalue = segments.get(rowIndex).getName();
		}
		if (columnIndex == 1) {
			returnvalue = segments.get(rowIndex).getCurrentPOAPercent();
		}
		if (columnIndex == 2) {
			returnvalue = segments.get(rowIndex).getArea();
		}
		if (columnIndex == 3) {
			returnvalue = segments.get(rowIndex).getProbabilityDensity();
		}
		if (columnIndex == 4) {
			returnvalue = segments.get(rowIndex).getProposedPOA();
		}
		return returnvalue;
  }

  @SuppressWarnings("unchecked")
  public Class getColumnClass(int columnIndex) {
    	Class returnvalue = Object.class;
		if (columnIndex == 0) {
			returnvalue = String.class;
		}
		if (columnIndex == 1) {
			returnvalue = Double.class;
		}
		if (columnIndex == 2) {
			returnvalue = Double.class;
		}
		if (columnIndex == 3) {
			returnvalue = Segment.getProbabilityDensityTypeExample().getClass();
		}
		if (columnIndex == 4) {
			returnvalue = Double.class;
		}
        return returnvalue; 
        
  }

  public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col==3 || col==2 || col==1) {
            return false;
        } else {
            return true;
        }
  }

  public void setValueAt(Object value, int rowIndex, int columnIndex) {
    	System.out.println("Search.setValueAt() row:" + rowIndex + " on segments.size=" + segments.size());
		if (columnIndex == 0) {
			segments.get(rowIndex).setName((String) value);
		}
		if (columnIndex == 1) {
			segments.get(rowIndex).setCurrentPOAPercent(((Double) value).doubleValue());
		}
		if (columnIndex == 2) {
			segments.get(rowIndex).setArea(((Double) value).doubleValue());
		}
		if (columnIndex == 4) {
			segments.get(rowIndex).setProposedPOA(((Double) value).doubleValue());
		}		
		fireTableDataChanged();
  }

  /**
   * Validate proposed POA values, and if valid, copy to current POA for each segment.
   * 
   * @return true if proposed POA values across segments form a valid set.
   */
  public boolean setProposedPOAsAsCurrent() {
 
    	boolean result = false;
    	result = validateProposedPOAs();
    	if (result) { 
    		Iterator<Segment> i = segments.iterator();
    		while (i.hasNext()) { 
    			Segment s = i.next();
    			s.setCurrentPOA(s.getProposedPOA());
    		}
    	}
    	return result;
  }

  /**
   * Test to see if proposed POA values can be used as the current POA values.
   * 
   * @return true if set of ProposedPOA values from a valid set across the segments for this search.
   */
  public boolean validateProposedPOAs() {
 
    	boolean result = false;
    	Iterator<Segment> i = segments.iterator();
    	double poaSum = 0d;
    	while (i.hasNext()) { 
    		Segment s = i.next();
    	    poaSum += s.getProposedPOA();
    	}
    	if (Utility.equalTenPlaces(poaSum, 1d)) { 
    		result = true;
    	}
    	return result;
  }

  public double getProposedPOAsum() {
 
    	double poaSum = 0d;
    	Iterator<Segment> i = segments.iterator();
    	while (i.hasNext()) { 
    		Segment s = i.next();
    	    poaSum += s.getProposedPOA();
    	}
        return poaSum;   	
  }

  /**
   * @return the tasks
   */
  public TaskList getTasks() {
		return tasks;
  }

  /**
   * @param tasks the tasks to set
   */
  public void setTasks(TaskList tasks) {
		this.tasks = tasks;
  }

  /**
   * @return the subjectCategoryScheme
   */
  public SubjectCategoryScheme getSubjectCategoryScheme() {
		return subjectCategoryScheme;
  }

  /**
   * @param subjectCategoryScheme the subjectCategoryScheme to set
   */
  public void setSubjectCategoryScheme(SubjectCategoryScheme scheme) {
		this.subjectCategoryScheme = scheme;
		SMTSingleton.getSingletonInstance().getActiveLog().LogAnEvent(new LogEvent("System",LogEvent.EVENT_SYSTEM,"Changed subject category scheme to " + scheme.getSchemeName()));
  }

  public JPopupMenu getTablePopupMenu() {
 

		TitledPopupMenu layerNamePopupMenu = new TitledPopupMenu() {
				
				@Override
				public void show(Component invoker, int x, int y) {
					String name = (String)((JTable)invoker).getModel().getValueAt(((JTable)invoker).getSelectedRow(),0);
					// Set the title of the menu from the Segment on which this popup was launched
					setTitle(name);
					
					log.debug("Creating popup menu on " + name);
					
					// Addition of new menu items for context is triggered each time menu is shown,
					// so remove all menu items except for the title and separator (components 0 and 1). 
					for (int comp = this.getComponentCount(); comp>2; comp--) { 
						// Component count is 1 higher than component index
					    remove(getComponent(comp-1));
					}
					// Add a new menu item for the Segment on which this popup was launched to set POD
		            if (!name.equals(Segment.getROWName())) { 
		            	// but only do so if the segment is not ROW.
		            JMenuItem item = new JMenuItem("Apply POD");
		            item.setToolTipText(name);
		    		item.addActionListener(new ActionListener() {

		    			@Override
		    			public void actionPerformed(ActionEvent e) {
		    				JMenuItem item = ((JMenuItem)e.getSource());
		    				//String name = (String)((JTable)e.getSource()).getModel().getValueAt(((JTable)e.getSource()).getSelectedRow(),1);
		    				String name = item.getToolTipText();
		    				Segment target = SMTSingleton.getSingletonInstance().getCurrentSearch().getSegmentByName(name);
		    				if (target!=null) { 
		    					String returnvalue = (String)JOptionPane.showInputDialog(
		    							SMTSingleton.getSingletonInstance().getMainFrame(),
		    							"Probability of detection (%) to apply to " + target.getName(), 
		    							"POD Dialog",
		    							JOptionPane.PLAIN_MESSAGE,
		    							null,
		    							null,
		    							"10");
		    					double pod = Double.parseDouble(returnvalue);
		    					pod = pod / 100d;  // change percent to probability 0 to 1.
		    					try {
		    						target.applyPOD(pod);
		    					} catch (BadCalculationException e1) {
		    						JOptionPane.showMessageDialog(SMTSingleton.getSingletonInstance().getMainFrame(), "Couldn't calculate POD. " + e1.getMessage(), "Error calcualating POD", JOptionPane.ERROR_MESSAGE);
		    					}
		    				}
		    			}} );		            
		            add(item);
		            }
		    		JMenuItem itemSplit = new JMenuItem("Split Segment");
		            itemSplit.setToolTipText(name);
		    		itemSplit.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							JMenuItem item = ((JMenuItem)e.getSource());
		    				String name = item.getToolTipText();
		    				Segment target = SMTSingleton.getSingletonInstance().getCurrentSearch().getSegmentByName(name);
							if (target!=null) { 
								AddSegmentDialog dialog = new AddSegmentDialog(target.getName());
								dialog.setVisible(true);
							}
							
						}
		    			
		    		});
		            add(itemSplit);
		            
		    		JMenuItem itemTask = new JMenuItem("Assign Task");
		            itemTask.setToolTipText(name);
		    		itemTask.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							JMenuItem item = ((JMenuItem)e.getSource());
		    				String name = item.getToolTipText();
		    				Segment target = SMTSingleton.getSingletonInstance().getCurrentSearch().getSegmentByName(name);
							if (target!=null) { 
								TaskPickerDialog dialog = new TaskPickerDialog((Assignable)target);
								dialog.setVisible(true);
							}
							
						}
		    			
		    		});
		    		itemTask.setEnabled(true);
		    		add(itemTask);
		            
		            /** 
		             *  don't add this on a table, just on the tree 
		            JMenuItem itemdraw = new JMenuItem("Draw on map");
					CursorTool autocompletePolygon = AutoCompletePolygonCursorTool.create(SMTSingleton.getSingletonInstance().getMapInternalFrame());
					WorkbenchToolBar toolbar = new WorkbenchToolBar((LayerViewPanelProxy) SMTSingleton.getSingletonInstance().getMapInternalFrame());
					toolbar.addCursorTool("Draw Polygon", autocompletePolygon);
					itemdraw.add(toolbar);
		            add(itemdraw);
		             */
                    super.show(invoker, x, y);					
				}
				
				/**
				{ 
				addPopupMenuListener(new PopupMenuListener() {
					public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
						String name = e.getSource().toString();
						setTitle(name);
					}
					public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
					}
					public void popupMenuCanceled(PopupMenuEvent e) {
					}
				});
				}
				*/
		};
		//layerNamePopupMenu.add(item);
		return (JPopupMenu)layerNamePopupMenu;	

  }

  public int getNextClueNumber() {
 
		clueCount++;
		return clueCount;
  }

}
