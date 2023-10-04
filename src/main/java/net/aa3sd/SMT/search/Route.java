/**
 * Route.java
 * Created Oct 3, 2011 9:11:59 PM
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

import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;
import javax.jws.soap.SOAPBinding.Style;
import org.apache.log4j.Logger;
import net.aa3sd.SMT.SMTSingleton;
import net.aa3sd.SMT.interfaces.Assignable;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.workbench.model.CategoryEventType;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerEventType;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.renderer.style.LabelStyle;
/**
 * @author mole
 */
public class Route extends Layer implements net.aa3sd.SMT.interfaces.Assignable {
  private static final Logger log =  Logger.getLogger(Route.class);

  private String name;

  public static Route routeFactory(String routeName, FeatureCollection featureCollection, LayerManager layerManager)
  {
		LayerViewPanel panel = SMTSingleton.getSingletonInstance().getMapLayerViewPanel();
	    boolean firingEvents = panel.getLayerManager().isFiringEvents();
	    panel.getLayerManager().setFiringEvents(false);
		Route route = new Route(routeName, featureCollection, panel.getLayerManager());
		Layer layerLoc = panel.getLayerManager().addLayer("Routes", route);
		layerLoc.removeStyle(layerLoc.getVertexStyle());
		layerLoc.setEditable(true);
		layerLoc.setSelectable(true);
	    panel.getLayerManager().setFiringEvents(firingEvents);
	    panel.getLayerManager().fireLayerChanged(route, LayerEventType.ADDED);
		panel.getLayerManager().fireCategoryChanged(panel.getLayerManager().getCategory("Routes"), CategoryEventType.METADATA_CHANGED);
		layerLoc.setVisible(true);
		SMTSingleton.getSingletonInstance().getCurrentSearch().addRoute(route);
		return route;
  }

  /**
   * @param name
   */
  protected Route(String aName, FeatureCollection featureCollection, LayerManager layerManager) {
		super(aName,  GUIUtil.alphaColor(Color.WHITE, 0), featureCollection, layerManager);
		Iterator<Style> i = this.getStyles().iterator();
		log.debug(this.getStyles().size());
		while (i.hasNext()) { 
			log.debug(i.next());
		}
		super.setName(aName);
		super.setDescription(aName);
		super.setReadonly(false);
		super.setEditable(true);
		super.getBasicStyle().setLineColor(Color.ORANGE);
		super.getBasicStyle().setLineWidth(3);
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
  }

  /**
   *  (non-Javadoc)
   * @see com.vividsolutions.jump.workbench.model.LayerManagerProxy#getLayerManager()
   */
  @Override
  public LayerManager getLayerManager() {
		return super.getLayerManager();
  }

  /**
   * For each feature in collection, sets the label attribute.
   * The label is displayed on each feature when 
   * getLabelStyle().setAttribute("label"); is set.
   * 
   * @param features the feature collection to a uniform label on.
   */
  private void setFeaturesText(Collection<Feature> features) {
		Iterator<Feature> i = features.iterator();
		final String key = "label";
		while (i.hasNext()) {
			Feature f = i.next();
			try { 
				f.getAttribute(key);
			} catch (IllegalArgumentException e) { 
	            System.out.println(e);			
			} 
			f.setAttribute(key, this.name);
		}
		
  }

  /**
   *  (non-Javadoc)
   * @see net.aa3sd.SMT.interfaces.Assignable#getArea()
   */
  @Override
  public double getArea() {
	    // Routes are expected to be 1D features with no area.
		return 0;
  }

}
