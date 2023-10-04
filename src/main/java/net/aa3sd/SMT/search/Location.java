/**
 * Location.java
 * Created Dec 7, 2008 6:34:32 AM
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

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import net.aa3sd.SMT.SMTSingleton;
import net.aa3sd.SMT.interfaces.Assignable;
import net.aa3sd.SMT.log.LogEvent;
import net.aa3sd.SMT.math.Coordinate;
import com.cadplan.jump.AnyShapeVertexStyle;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureCollectionWrapper;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.workbench.model.CategoryEventType;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerEventType;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.renderer.style.LabelStyle;
import com.vividsolutions.jump.workbench.ui.renderer.style.VertexStyle;
/**
 * A geographic location associated with a search.
 * 
 * @author Paul J. Morris
 */
public class Location extends Layer implements net.aa3sd.SMT.interfaces.Assignable {
  private static final Logger log =  Logger.getLogger(Route.class);

  private String name;

  private net.aa3sd.SMT.math.Coordinate centerCoordinate;

  private String svgGraphic;

  /**
   * @return an ArrayList of possible Coordinate names
   */
  public static ArrayList<String> getLocationList()
  {
 
		//TODO: Make configurable and allow internationalization, including magic location IPP.
		ArrayList<String> LocationList = new ArrayList<String>();
		LocationList.add("PLS");
		LocationList.add("IPP");
		LocationList.add("ICP");
		LocationList.add("Base");
		LocationList.add("Staging");
		LocationList.add("Helipoint");
		LocationList.add("Clue");
		
		return LocationList;
  }

  public static Location locationFactory(String locationName, FeatureCollection featureCollection, LayerManager layerManager)
  {
		LayerViewPanel panel = SMTSingleton.getSingletonInstance().getMapLayerViewPanel();
		//FeatureSchema fc = new FeatureSchema();
		//fc.addAttribute("GEOMETRY", AttributeType.GEOMETRY);
		//FeatureCollection featureDataSet = new FeatureDataset(fc);
	    boolean firingEvents = panel.getLayerManager().isFiringEvents();
	    panel.getLayerManager().setFiringEvents(false);
	    Location location = null;
	    if (locationName.equals("Clue")) { 
		    location = new Clue(locationName, featureCollection, panel.getLayerManager());
	    } else { 
		    location = new Location(locationName, featureCollection, panel.getLayerManager());
	    }
		Layer layerLoc = panel.getLayerManager().addLayer("Places", location);
		layerLoc.removeStyle(layerLoc.getVertexStyle());
		layerLoc.setEditable(true);
		layerLoc.setSelectable(true);
		layerLoc.addStyle(Location.getVertexStyleForLocation(location.getName()));
	    panel.getLayerManager().setFiringEvents(firingEvents);
	    panel.getLayerManager().fireLayerChanged(location, LayerEventType.ADDED);
		panel.getLayerManager().fireCategoryChanged(panel.getLayerManager().getCategory("Places"), CategoryEventType.METADATA_CHANGED);
		layerLoc.setVisible(true);
		SMTSingleton.getSingletonInstance().getCurrentSearch().addLocation(location);
		return location;
  }

  public static VertexStyle getVertexStyleForLocation(String locationName)
  {
 
		AnyShapeVertexStyle result = new AnyShapeVertexStyle();
		result.setType(4);
		result.setEnabled(true);
		result.setLineColor(Color.getHSBColor(294, 100, 100));
		return result;
  }

  protected Location(String aName, FeatureCollection featureCollection, LayerManager layerManager) {
		super(aName,  GUIUtil.alphaColor(Color.WHITE, 0), featureCollection, layerManager);;
		super.setName(aName);
		super.setReadonly(false);
		super.setEditable(true);
		super.getBasicStyle().setFillColor( GUIUtil.alphaColor(Color.WHITE, 5));
		//super.addStyle(getVertexStyleForLocation(aName));
		super.getBasicStyle().setLineColor(Color.BLACK);
		super.getLabelStyle().setColor(Color.BLACK);
		super.getLabelStyle().setEnabled(true);
		super.getLabelStyle().setVerticalAlignment(LabelStyle.ON_LINE);
		try { 
		    super.getLabelStyle().setAttribute("label");
		    featureCollection.getFeatureSchema().addAttribute("label", AttributeType.STRING);
		} catch (Exception e) { 
			log.error(e);
			log.error("Coordinate, can't add attribute label");
		}		
		name = aName;
  }

  public FeatureCollectionWrapper getFeatureCollectionWrapper() {
		log.debug(name + " Getting feature collection wrapper for Coordinate.");
		log.debug("with feature count = " + super.getFeatureCollectionWrapper().getFeatures().size());
		return super.getFeatureCollectionWrapper();
  }

  /**
   *  (non-Javadoc)
   * @see com.vividsolutions.jump.workbench.model.Layer#setEditable(boolean)
   */
  @Override
  public void setEditable(boolean editable) {
		// TODO Auto-generated method stub
		super.setEditable(editable);
  }

  public boolean isEditable() {
 
		return super.isEditable();
  }

  /**
   * For each feature in collection, sets the label attribute.
   * The label is displayed on each feature when 
   * getLabelStyle().setAttribute("label"); is set.
   * 
   * @param features the feature collection to a uniform label on.
   */
  public void setFeaturesText(Collection<Feature> features) {
		Iterator<Feature> i = features.iterator();
		final String key = "label";
		while (i.hasNext()) {
			Feature f = i.next();
			try { 
				f.getAttribute(key);
			} catch (IllegalArgumentException e) { 
	            log.error(e);
			} 
			f.setAttribute(key, this.name);
		}
		
  }

  /**
   * @return the centerCoordinate
   */
  public net.aa3sd.SMT.math.Coordinate getCenterCoordinate() {
		return centerCoordinate;
  }

  /**
   * @param centerCoordinate the centerCoordinate to set
   */
  public void setCenterCoordinate(net.aa3sd.SMT.math.Coordinate centerCoordinate) {
		this.centerCoordinate = centerCoordinate;
		SMTSingleton.getSingletonInstance().getActiveLog().LogAnEvent(new LogEvent(name,LogEvent.EVENT_SEARCH,name + " location set to: " + this.centerCoordinate.getLatitude() + "," + this.centerCoordinate.getLongitude() + " .", this.centerCoordinate));
  }

  /**
   *  (non-Javadoc)
   * @see com.vividsolutions.jump.workbench.model.LayerManagerProxy#getLayerManager()
   */
  @Override
  public LayerManager getLayerManager() {
		return super.getLayerManager();
  }

  public double getArea() {
		double result = 0d;
		List<Feature> features = getFeatureCollectionWrapper().getFeatures();
		Iterator<Feature> i = features.iterator();
		while (i.hasNext()) { 
			result = i.next().getGeometry().getArea();
			log.debug("Area = " + result);
		}
		return result;
  }

}
