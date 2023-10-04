/**
 * Segment.java
 * Created Oct 14, 2008 5:48:07 AM
 * 
 * Copyright (C) 2003 Vivid Solutions
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

import java.util.ArrayList;
import net.aa3sd.SMT.SMTSingleton;
import net.aa3sd.SMT.geometry.Polygon;
import net.aa3sd.SMT.interfaces.Assignable;
import net.aa3sd.SMT.log.LogEvent;
import net.aa3sd.SMT.math.BadCalculationException;
import net.aa3sd.SMT.math.SphericalUtility;
import net.aa3sd.SMT.math.UnitConverter;
import net.aa3sd.SMT.math.Utility;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.model.CategoryEvent;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.FeatureEventType;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerEvent;
import com.vividsolutions.jump.workbench.model.LayerEventType;
import com.vividsolutions.jump.workbench.model.LayerListener;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.model.ObservableFeatureCollection;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jts.util.AssertionFailedException;
import com.vividsolutions.jump.coordsys.impl.PredefinedCoordinateSystems;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureCollectionWrapper;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.LayerNamePanel;
import com.vividsolutions.jump.workbench.ui.TitledPopupMenu;
import com.vividsolutions.jump.workbench.ui.plugin.AddNewLayerPlugIn;
import com.vividsolutions.jump.workbench.ui.renderer.style.*;
/**
 * 
 * Adds colour, line-width, and other stylistic information to a Feature
 * Collection.
 * <p>
 * When adding or removing multiple features to this Layer's FeatureCollection,
 * prefer #addAll and #removeAll to #add and #remove -- fewer events will be
 * fired.
 */
public class Segment extends Layer implements net.aa3sd.SMT.interfaces.Assignable {
  private static final Log log =  LogFactory.getLog(Segment.class);

  private static final long serialVersionUID =  6504993615735124204L;

  public static final int BASE_ALPHA =  65;

  public static final String FIRING_APPEARANCE_CHANGED_ON_ATTRIBUTE_CHANGE =  Layer.class
			.getName()
			+ " - FIRING APPEARANCE CHANGED ON ATTRIBUTE CHANGE";

  private String name;

  /**
   *  mathematically validated value for current Probability of Area 
   */
  private double currentPOA;

  /**
   *  proposed, but not validated across set of segments, value for new POA.  
   */
  private double proposedPOA;

  private double area;

  private Search search;

  private net.aa3sd.SMT.geometry.Polygon geometry;

  private Segment thisSegment;

  /**
   *  from Layer, candidates for removal.
   */
  private String description =  "";

  private boolean drawingLast =  false;

  /**
   * private FeatureCollectionWrapper featureCollectionWrapper;
   */
  private boolean synchronizingLineColor =  true;

  private boolean selectable =  true;

  private boolean readonly =  false;

  private LayerListener layerListener =  null;

  private Blackboard blackboard =  new Blackboard() {
		{
			put(FIRING_APPEARANCE_CHANGED_ON_ATTRIBUTE_CHANGE, true);
		}
	};

  private boolean featureCollectionModified =  false;

  private DataSourceQuery dataSourceQuery;

  public static Segment segmentFactory()
  {
 
        FeatureSchema fs = new FeatureSchema();
        fs.addAttribute("GEOMETRY", AttributeType.GEOMETRY);
        fs.addAttribute("label", AttributeType.STRING);
        fs.setCoordinateSystem(PredefinedCoordinateSystems.GEOGRAPHICS_WGS_84);
		FeatureCollection fc = new FeatureDataset(fs);
		LayerManager lm = 	SMTSingleton.getSingletonInstance().getMapInternalFrame().getLayerManager();
		int rowOffset = 1;
		if (SMTSingleton.getSingletonInstance().getCurrentSearch().getSegmentByName(Segment.getROWName())!=null) {
			// if a ROW segment exists, use the segment count as the next number 
			rowOffset = 0;
		}
		Segment newSegment = new Segment(SMTSingleton.getSingletonInstance().getCurrentSearch(),
				        "Segment " + Integer.toString(SMTSingleton.getSingletonInstance().getCurrentSearch().getSegments().size() + rowOffset),
						Color.RED,
						fc, lm
						);
		return newSegment;
  }

  /**
   * Constructor
   */
  public Segment(Search inSearch, String name, Color fillColor, FeatureCollection featureCollection, LayerManager layerManager) {
 
		super(name, fillColor, featureCollection, layerManager);
		name = "New Segment";
		currentPOA = 0d;
		area = 0d;
		search = inSearch;
		search.addSegment(this);
		thisSegment = this;

		super.setReadonly(false);

		Assert.isTrue(featureCollection != null);

		super.getBasicStyle().setAlpha(BASE_ALPHA);
		
		super.getLabelStyle().setColor(Color.BLACK);
		super.getLabelStyle().setEnabled(true);
		super.getLabelStyle().setVerticalAlignment(LabelStyle.ON_LINE);
		try { 
		    super.getLabelStyle().setAttribute("label");
		} catch (Exception e) { 
			log.error(e);
		}

  }

  public static String getROWName()
  {
 
		return "ROW";
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
   * Get the current value for the probability of area for this segment as a probability between 0 and 1.
   * 
   * @return the currentPOA
   */
  public double getCurrentPOA() {
		return currentPOA;
  }

  /**
   * Get the current value for the probability of area of this segment as a percent between 0 and 100. 
   * 
   * @return
   */
  public double getCurrentPOAPercent() {
 
		return currentPOA * 100d;
  }

  public void setCurrentPOAPercent(double currentPOAPercent) {
 
		setCurrentPOA(currentPOAPercent / 100d);
  }

  /**
   * Set the current probability of area for this segment as a probability between 0 and 1.
   * 
   * @param currentPOA the currentPOA to set 
   */
  public void setCurrentPOA(double currentPOA) {
		this.currentPOA = currentPOA;

		double podAlphaRange = 50d;
		int baseAlphaRange = (int) (BASE_ALPHA - (podAlphaRange/2));
		int topAlphaRange = (int) (BASE_ALPHA + (podAlphaRange/2));
		
		double pod = getProbabilityDensity() * podAlphaRange;

		if (pod > 0) { 

			int scaledAlpha = (int) (BASE_ALPHA - (podAlphaRange/2) + pod);
			if (scaledAlpha<baseAlphaRange) { scaledAlpha=baseAlphaRange; } 
			if (scaledAlpha>topAlphaRange) { scaledAlpha = topAlphaRange; } 

			System.out.println(scaledAlpha);
			super.getBasicStyle().setAlpha(scaledAlpha);	
			try { 
				super.fireAppearanceChanged();
			} catch (AssertionFailedException e) { 
				// will end up here if constructing a layer and layer firing events is not false.
				log.debug(e);
			}

		} 
		SMTSingleton.getSingletonInstance().getActiveLog().LogAnEvent(new LogEvent("Segment POA", LogEvent.EVENT_SYSTEM, "Segment " + this.name + " POA set to " + this.currentPOA));
  }

  /**
   * @return the area in square meters
   */
  public double getArea() {
		if (hasFeatures()) { 
			double featureAreas = 0d;
			Iterator<Feature> i = getFeatureCollectionWrapper().getFeatures().iterator();
			while (i.hasNext()) { 
				Feature f = i.next();
				int srid = f.getGeometry().getSRID();
				log.debug("Segment feature with SRID:" + srid);
			    // double featureArea = f.getGeometry().getArea();  Returns area in degrees.  
			    double featureArea = SphericalUtility.computeAreaForGeometry((com.vividsolutions.jts.geom.Polygon) f.getGeometry());
			    if (featureArea > 0) { 
			    	featureAreas =+ featureArea;
			    }
			}
			if (featureAreas > 0) { 
				setArea(featureAreas);
			}
		}
		 
		area = area;  
		
		log.debug("Segment area:" + area);
		return area;
  }

  /**
   * @param area the area to set
   */
  public void setArea(double area) {
		if (!Utility.equalTenPlaces(area, this.area)) { 
		    SMTSingleton.getSingletonInstance().getActiveLog().LogAnEvent(
		    		new LogEvent("Segment POA", LogEvent.EVENT_SYSTEM, "Segment " + this.name + " Area set to " + area)
		    		);
		}
		this.area = area;
  }

  public final static Double getProbabilityDensityTypeExample()
  {
 
		return 1d;
  }

  public double getProbabilityDensity() {
		return currentPOA / UnitConverter.squareMilesToAcres(this.area);
  }

  public void applyPOD(double pod) throws net.aa3sd.SMT.math.BadCalculationException {
 
		if (pod>1 || pod <0) { 
			throw new BadCalculationException("POD not between 0 and 1");
		}
		double POAnew = ((1d - pod) * this.getCurrentPOA()) / (1 - (pod * this.getCurrentPOA()));
		double POAold = this.getCurrentPOA();
		ArrayList <Segment> segments = search.getSegments();
		for (int x=0; x<segments.size(); x++) { 
			if (segments.get(x).equals(this)) { 
				segments.get(x).setCurrentPOA(POAnew);
			} else { 
				double POAnewY = segments.get(x).getCurrentPOA() / (1 - (pod * POAold));
				segments.get(x).setCurrentPOA(POAnewY);
			}
		}
  }

  private boolean hasFeatures() {
    	boolean result = true;
    	if (getFeatureCollectionWrapper().isEmpty()) { 
    		result = false;
    	}
    	return result;
  }

  /**
   * *****  From Layer ****
   */
  public void setDescription(String description) {
		Assert.isTrue(
				description != null,
				"Java2XML requires that the description be non-null. Use an empty string if necessary.");
		this.description = description;
  }

  /**
   * @return true if this layer should always be 'readonly' I.e.: The layer
   * should never have the editable field set to true.
   */
  public boolean isReadonly() {
		return readonly;
  }

  /**
   * Set whether this layer can be made editable.
   */
  public void setReadonly(boolean value) {
		readonly = value;
  }

  /**
   * @return true if features in this layer can be selected.
   */
  public boolean isSelectable() {
		return selectable;
  }

  /**
   * Set whether or not features in this layer can be selected.
   * @param value true if features in this layer can be selected
   */
  public void setSelectable(boolean value) {
		selectable = value;
  }

  /**
   * Used for lightweight layers like the Vector layer.
   * 
   * @param drawingLast
   *            true if the layer should be among those drawn last
   */
  public void setDrawingLast(boolean drawingLast) {
		this.drawingLast = drawingLast;
		fireAppearanceChanged();
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

  public void setFeatureCollection(final FeatureCollection featureCollection) {
		final FeatureCollection oldFeatureCollection = getFeatureCollectionWrapper() != null ? getFeatureCollectionWrapper()
				.getUltimateWrappee()
				: AddNewLayerPlugIn.createBlankFeatureCollection();
				ObservableFeatureCollection observableFeatureCollection = new ObservableFeatureCollection(
						featureCollection);
				observableFeatureCollection.checkNotWrappingSameClass();
				observableFeatureCollection
				.add(new ObservableFeatureCollection.Listener() {
					public void featuresAdded(Collection features) {
						setFeaturesText(features);
						getLayerManager().fireFeaturesChanged(features,
								FeatureEventType.ADDED, (Layer)Segment.this);
					}

					public void featuresRemoved(Collection features) {
						getLayerManager().fireFeaturesChanged(features,
								FeatureEventType.DELETED, (Layer)Segment.this);
					}
				});

				if ((getLayerManager() != null)
						&& getLayerManager().getLayers().contains(this)) {
					//Don't fire APPEARANCE_CHANGED immediately, to avoid the
					//following problem:
					//(1) Add fence layer
					//(2) LAYER_ADDED event will be called
					//(3) APPEARANCE_CHANGED will be fired in this method (before
					//the JTree receives its LAYER_ADDED event)
					//(4) The JTree will complain because it gets the
					// APPEARANCE_CHANGED
					//event before the LAYER_ADDED event:
					//            java.lang.ArrayIndexOutOfBoundsException: 0 >= 0
					//                at java.util.Vector.elementAt(Vector.java:412)
					//                at
					// javax.swing.tree.DefaultMutableTreeNode.getChildAt(DefaultMutableTreeNode.java:226)
					//                at
					// javax.swing.tree.VariableHeightLayoutCache.treeNodesChanged(VariableHeightLayoutCache.java:369)
					//                at
					// javax.swing.plaf.basic.BasicTreeUI$TreeModelHandler.treeNodesChanged(BasicTreeUI.java:2339)
					//                at
					// javax.swing.tree.DefaultTreeModel.fireTreeNodesChanged(DefaultTreeModel.java:435)
					//                at
					// javax.swing.tree.DefaultTreeModel.nodesChanged(DefaultTreeModel.java:318)
					//                at
					// javax.swing.tree.DefaultTreeModel.nodeChanged(DefaultTreeModel.java:251)
					//                at
					// com.vividsolutions.jump.workbench.model.LayerTreeModel.layerChanged(LayerTreeModel.java:292)
					//[Jon Aquino]
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							//Changed APPEARANCE_CHANGED event to FEATURE_DELETED and
							//FEATURE_ADDED events, but I think the lengthy comment
							//above still applies. [Jon Aquino]
							//Drop #isEmpty checks, so that database-backed feature
							//collections don't have to implement it.
							//[Jon Aquino 2005-03-02]
							getLayerManager().fireFeaturesChanged(
									oldFeatureCollection.getFeatures(),
									FeatureEventType.DELETED, (Layer)Segment.this);
							getLayerManager().fireFeaturesChanged(
									featureCollection.getFeatures(),
									FeatureEventType.ADDED, (Layer)Segment.this);
						}
					});
				}

				setFeatureCollectionWrapper(observableFeatureCollection);
				
  }

  /**
   * Editability is not enforced; all parties are responsible for heeding this
   * flag.
   * 
   * public void setEditable(boolean editable) {
   * 	if (this.editable == editable) {
   * 		return;
   * 	}
   * 	this.editable = editable;
   * 	fireLayerChanged(LayerEventType.METADATA_CHANGED);
   * }
   * public boolean isEditable() {
   * 	return editable;
   * }
   */
  public void setSynchronizingLineColor(boolean synchronizingLineColor) {
		this.synchronizingLineColor = synchronizingLineColor;
		fireAppearanceChanged();
  }

  public String getDescription() {
		return description;
  }

  /**
   * Returns a wrapper around the FeatureCollection which was added using
   * #wrapFeatureCollection. The original FeatureCollection can be retrieved
   * using FeatureCollectionWrapper#getWrappee. However, parties are
   * encouraged to use the FeatureCollectionWrapper instead, so that feature
   * additions and removals cause FeatureEvents to be fired (by the Layer).
   */
  public FeatureCollectionWrapper getFeatureCollectionWrapper() {
		return super.getFeatureCollectionWrapper();
  }

  protected void setFeatureCollectionWrapper(FeatureCollectionWrapper featureCollectionWrapper) {
		super.setFeatureCollectionWrapper(featureCollectionWrapper);
  }

  /**
   * Styles do not notify the Layer when their parameters change. Therefore,
   * after you modify a Style's parameters (for example, the fill colour of
   * BasicStyle), be sure to call #fireAppearanceChanged
   * 
   * @param c
   *            Can even be the desired Style's superclass or interface
   * @return The style value
   * 
   * public Style getStyle(Class c) {
   * 	for (Iterator i = styles.iterator(); i.hasNext();) {
   * 		Style p = (Style) i.next();
   * 		if (c.isInstance(p)) {
   * 			return p;
   * 		}
   * 	}
   * 	return null;
   * }
   * public List getStyles() {
   * 	return Collections.unmodifiableList(styles);
   * }
   */
  public boolean hasReadableDataSource() {
		return dataSourceQuery != null
				&& dataSourceQuery.getDataSource().isReadable();
  }

  public boolean isDrawingLast() {
		return drawingLast;
  }

  public boolean isSynchronizingLineColor() {
		return synchronizingLineColor;
  }

  /**
   * public void addStyle(Style style) {
   * 	styles.add(style);
   * 	fireAppearanceChanged();
   * }
   * 
   * Releases references to the data, to facilitate garbage collection.
   * Important for MDI apps like the JUMP Workbench. Called when the last
   * JInternalFrame viewing the LayerManager is closed (i.e. internal frame's
   * responsibility). To conserve memory, if layers are frequently added and
   * removed from the LayerManager, parties may want to call #dispose
   * themselves rather than waiting for the internal frame to be closed.
   */
  public void dispose() {
		//Don't just call FeatureCollection#removeAll, because it may be a
		// database
		//table, and we don't want to delete its contents! [Jon Aquino]
		setFeatureCollection(AddNewLayerPlugIn.createBlankFeatureCollection());
  }

  /**
   * public void removeStyle(Style p) {
   * 	Assert.isTrue(styles.remove(p));
   * 	fireAppearanceChanged();
   * }
   */
  public Collection cloneStyles() {
		ArrayList styleClones = new ArrayList();

		for (Iterator i = getStyles().iterator(); i.hasNext();) {
			Style style = (Style) i.next();
			styleClones.add(style.clone());
		}

		return styleClones;
  }

  public void setStyles(Collection newStyles) {
		boolean firingEvents = getLayerManager().isFiringEvents();
		getLayerManager().setFiringEvents(false);

		try {
			//new ArrayList to prevent ConcurrentModificationException [Jon
			// Aquino]
			for (Iterator i = new ArrayList(getStyles()).iterator(); i
					.hasNext();) {
				Style style = (Style) i.next();
				removeStyle(style);
			}

			for (Iterator i = newStyles.iterator(); i.hasNext();) {
				Style style = (Style) i.next();
				addStyle(style);
			}
		} finally {
			getLayerManager().setFiringEvents(firingEvents);
		}

		fireAppearanceChanged();
  }

  public void setLayerManager(LayerManager layerManager) {
		if (layerManager != null) {
			layerManager.removeLayerListener(getLayerListener());
		}

		super.setLayerManager(layerManager);
		layerManager.addLayerListener(getLayerListener());
  }

  private LayerListener getLayerListener() {
		//Need to create layerListener lazily because it will be called by the
		//superclass constructor. [Jon Aquino]
		if (layerListener == null) {
			layerListener = new LayerListener() {
				public void featuresChanged(FeatureEvent e) {
					if (e.getLayer() == (Layer)Segment.this) {
						setFeatureCollectionModified(true);

						//Before I wasn't firing appearance-changed on an
						// attribute
						//change. But now with labelling and colour theming,
						//I have to. [Jon Aquino]
						if (e.getType() != FeatureEventType.ATTRIBUTES_MODIFIED
								|| getBlackboard()
								.get(
										FIRING_APPEARANCE_CHANGED_ON_ATTRIBUTE_CHANGE,
										true)) {
							//Fixed bug above -- wasn't supplying a default
							// value to
							//Blackboard#getBoolean, resulting in a
							// NullPointerException
							//when the Layer was created using the
							// parameterless
							//constructor (because that constructor doesn't
							// initialize
							//FIRING_APPEARANCE_CHANGED_ON_ATTRIBUTE_CHANGE
							//on the blackboard [Jon Aquino 10/21/2003]
							fireAppearanceChanged();
						}
					}
				}

				public void layerChanged(LayerEvent e) {
				}

				public void categoryChanged(CategoryEvent e) {
				}
			};
		}

		return layerListener;
  }

  public Blackboard getBlackboard() {
		return blackboard;
  }

  /**
   * Enables a layer to be changed undoably. Since the layer's features are
   * saved, only use this method for layers with few features.
   */
  public static UndoableCommand addUndo(final String layerName, final LayerManagerProxy proxy, final UndoableCommand wrappeeCommand)
  {
		return new UndoableCommand(wrappeeCommand.getName()) {
			private Layer layer;

			private String categoryName;

			private Collection features;

			private boolean visible;

			private Layer currentLayer() {
				return proxy.getLayerManager().getLayer(layerName);
			}

			public void execute() {
				layer = currentLayer();

				if (layer != null) {
					features = new ArrayList(layer
							.getFeatureCollectionWrapper().getFeatures());
					categoryName = layer.getName();
					visible = layer.isVisible();
				}

				wrappeeCommand.execute();
			}

			public void unexecute() {
				wrappeeCommand.unexecute();

				if ((layer == null) && (currentLayer() != null)) {
					proxy.getLayerManager().remove(currentLayer());
				}

				if ((layer != null) && (currentLayer() == null)) {
					proxy.getLayerManager().addLayer(categoryName, layer);
				}

				if (layer != null) {
					layer.getFeatureCollectionWrapper().clear();
					layer.getFeatureCollectionWrapper().addAll(features);
					layer.setVisible(visible);
				}
			}
		};
  }

  /**
   * Does nothing if the underlying feature collection is not a
   * FeatureDataset.
   */
  public static void tryToInvalidateEnvelope(Layer layer)
  {
		if (layer.getFeatureCollectionWrapper().getUltimateWrappee() instanceof FeatureDataset) {
			((FeatureDataset) layer.getFeatureCollectionWrapper()
					.getUltimateWrappee()).invalidateEnvelope();
		}
  }

  public DataSourceQuery getDataSourceQuery() {
		return dataSourceQuery;
  }

  public Layer setDataSourceQuery(DataSourceQuery dataSourceQuery) {
		this.dataSourceQuery = dataSourceQuery;

		return this;
  }

  /**
   * @return the proposedPOA
   */
  public double getProposedPOA() {
		return proposedPOA;
  }

  /**
   * @param proposedPOA the proposedPOA to set
   */
  public void setProposedPOA(double proposedPOA) {
		this.proposedPOA = proposedPOA;
  }

}
