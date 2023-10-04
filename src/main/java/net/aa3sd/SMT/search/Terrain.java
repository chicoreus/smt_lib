/**
 * Terrain.java
 * Created Aug 18, 2011 8:06:15 AM
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

import java.util.Vector;
/**
 * @author mole
 */
public class Terrain {
  public static final String TERRAIN_URBAN =  "Urban";

  public static final String TERRAIN_FLAT_TEMPERATE =  "Flat Temperate: less than 1000 feet relief, with trees";

  public static final String TERRAIN_FLAT_DRY =  "Flat Dry: less than 1000 feet relief, without trees";

  public static final String TERRAIN_MNT_TEMPERATE =  "Mountainous Temperate: more than 1000' relief, with trees";

  public static final String TERRAIN_MNT_DRY =  "Mountainous Dry: more than 1000' relief, without trees ";

  private String name;

  public Terrain(String name) {
 
		this.setName(name);
  }

  public static Vector<String> getTerrainValues()
  {
 
		Vector<String> values = new Vector<String>();
		values.add(TERRAIN_URBAN);
		values.add(TERRAIN_FLAT_TEMPERATE);
		values.add(TERRAIN_FLAT_DRY);
		values.add(TERRAIN_MNT_TEMPERATE);
		values.add(TERRAIN_MNT_DRY);
		return values;
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

}
