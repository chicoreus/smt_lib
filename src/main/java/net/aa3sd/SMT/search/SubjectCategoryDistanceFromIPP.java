/**
 * SubjectCategoryDistanceFromIPP.java
 * Created Aug 19, 2011 8:16:37 AM
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
import java.util.List;
import net.aa3sd.SMT.exceptions.NoSuchTerrainException;
import net.aa3sd.SMT.generated.subject.XDistanceSet;
/**
 * @author mole
 * 
 * TODO: Change from hard coded terrains to configurable terrains
 */
public class SubjectCategoryDistanceFromIPP {
  /**
   *  TODO: Provide sensible default values
   */
  private int tm_n =  95;

  /**
   *  temperate mountainous 
   *  km
   */
  private float tm_first_quartile =  (float) 0.3;

  /**
   *  km
   */
  private float tm_second_quartile =  (float) 0.8;

  /**
   * km
   */
  private float tm_third_quartile =  (float) 1.9;

  /**
   *  km
   */
  private float tm_nintey_fifth =  (float) 8.3;

  private int tf_n =  175;

  /**
   *  temperate flat 
   *  km
   */
  private float tf_first_quartile =  (float) 0.3;

  /**
   *  km
   */
  private float tf_second_quartile =  (float) 0.8;

  /**
   * km
   */
  private float tf_third_quartile =  (float) 1.9;

  /**
   *  km
   */
  private float tf_niney_fifth =  (float) 8.3;

  private int dm_n =  14;

  /**
   *  dry mountainous 
   *  km
   */
  private float dm_first_quartile =  (float) 0.3;

  /**
   *  km
   */
  private float dm_second_quartile =  (float)0.8;

  /**
   * km
   */
  private float dm_third_quartile =  (float)1.9;

  /**
   *  km
   */
  private float dm_nintey_fifth =  (float)8.3;

  private int df_n =  15;

  /**
   *  dry flat 
   *  km
   */
  private float df_first_quartile = (float) 0.3;

  /**
   *  km
   */
  private float df_second_quartile = (float) 0.8;

  /**
   * km
   */
  private float df_third_quartile = (float) 1.9;

  /**
   *  km	
   */
  private float df_nintey_fifth =  (float)8.3;

  private int urban_n =  336;

  /**
   *  urban/suburban. 
   *  km
   */
  private float urban_first_quartile =  (float)0.3;

  /**
   *  km
   */
  private float urban_second_quartile =  (float)0.8;

  /**
   * km
   */
  private float urban_third_quartile =  (float)1.9;

  /**
   *  km
   */
  private float urban_nintey_fifth =  (float)8.3;

  /**
   * Create a set of default subject distances from the IPP.
   */
  public SubjectCategoryDistanceFromIPP() {
 
  }

  /**
   * Given an instance of a proxy class for an xml representation of subject distances, create a
   * set of distances from that set, using default values when none are available.
   * 
   * @param xDistanceSet
   */
  public SubjectCategoryDistanceFromIPP(List<XDistanceSet> xDistanceSet) {
 
		Iterator<XDistanceSet> i = xDistanceSet.iterator();
		while (i.hasNext()) {
			XDistanceSet newDistances = i.next();
			if (newDistances.getRegionName().equals("Temperate:Mountainous") || 
					newDistances.getRegionName().equals("Temparate") || 
					newDistances.getRegionName().equals("All")) { 
				this.tm_n = newDistances.getSampleSize();
				// temperate mountainous 
				this.tm_first_quartile = newDistances.getTwentyFivePercentile(); 
				this.tm_second_quartile = newDistances.getFiftyPercentile();
				this.tm_third_quartile = newDistances.getSeventyFiveFivePercentile();
				this.tm_nintey_fifth = newDistances.getNinetyFivePercentile();
			}
			if (newDistances.getRegionName().equals("Temperate:Flat") || 
					newDistances.getRegionName().equals("Temparate") || 
					newDistances.getRegionName().equals("All")) { 			
				this.tf_n = newDistances.getSampleSize(); 
				// temperate flat 
				this.tf_first_quartile = newDistances.getTwentyFivePercentile();
				this.tf_second_quartile = newDistances.getFiftyPercentile();
				this.tf_third_quartile = newDistances.getSeventyFiveFivePercentile();
				this.tf_niney_fifth = newDistances.getNinetyFivePercentile();
			}

			if (newDistances.getRegionName().equals("Dry:Mountanous") || 
					newDistances.getRegionName().equals("Dry") || 
					newDistances.getRegionName().equals("All")) { 			
				this.dm_n = newDistances.getSampleSize();
				// dry mountainous 
				this.dm_first_quartile = newDistances.getTwentyFivePercentile(); // km
				this.dm_second_quartile = newDistances.getFiftyPercentile(); // km
				this.dm_third_quartile = newDistances.getSeventyFiveFivePercentile(); //km
				this.dm_nintey_fifth = newDistances.getNinetyFivePercentile(); // km
			}
			if (newDistances.getRegionName().equals("Dry:Flat") || 
					newDistances.getRegionName().equals("Dry") || 
					newDistances.getRegionName().equals("All")) { 			
				this.df_n = newDistances.getSampleSize(); 
				// dry flat 
				this.df_first_quartile = newDistances.getTwentyFivePercentile(); // km
				this.df_second_quartile = newDistances.getFiftyPercentile(); // km
				this.df_third_quartile = newDistances.getSeventyFiveFivePercentile(); //km
				this.df_nintey_fifth = newDistances.getNinetyFivePercentile(); // km	
			}
			if (newDistances.getRegionName().equals("Urban") || 
					newDistances.getRegionName().equals("All")) { 			
				this.urban_n = newDistances.getSampleSize(); 
				// urban/suburban. 
				this.urban_first_quartile = newDistances.getTwentyFivePercentile(); // km
				this.urban_second_quartile = newDistances.getFiftyPercentile(); // km
				this.urban_third_quartile = newDistances.getSeventyFiveFivePercentile(); //km
				this.urban_nintey_fifth = newDistances.getNinetyFivePercentile(); // km
			}
		} 
  }

  /**
   * @return the tm_n
   */
  public int getTm_n() {
		return tm_n;
  }

  /**
   * @return the tm_first_quartile
   */
  public float getTm_first_quartile() {
		return tm_first_quartile;
  }

  /**
   * @return the tm_second_quartile
   */
  public float getTm_second_quartile() {
		return tm_second_quartile;
  }

  /**
   * @return the tm_third_quartile
   */
  public float getTm_third_quartile() {
		return tm_third_quartile;
  }

  /**
   * @return the tm_nintey_fifth
   */
  public float getTm_nintey_fifth() {
		return tm_nintey_fifth;
  }

  /**
   * @return the tf_n
   */
  public int getTf_n() {
		return tf_n;
  }

  /**
   * @return the tf_first_quartile
   */
  public float getTf_first_quartile() {
		return tf_first_quartile;
  }

  /**
   * @return the tf_second_quartile
   */
  public float getTf_second_quartile() {
		return tf_second_quartile;
  }

  /**
   * @return the tf_third_quartile
   */
  public float getTf_third_quartile() {
		return tf_third_quartile;
  }

  /**
   * @return the tf_niney_fifth
   */
  public float getTf_niney_fifth() {
		return tf_niney_fifth;
  }

  /**
   * @return the dm_n
   */
  public int getDm_n() {
		return dm_n;
  }

  /**
   * @return the dm_first_quartile
   */
  public float getDm_first_quartile() {
		return dm_first_quartile;
  }

  /**
   * @return the dm_second_quartile
   */
  public float getDm_second_quartile() {
		return dm_second_quartile;
  }

  /**
   * @return the dm_third_quartile
   */
  public float getDm_third_quartile() {
		return dm_third_quartile;
  }

  /**
   * @return the dm_nintey_fifth
   */
  public float getDm_nintey_fifth() {
		return dm_nintey_fifth;
  }

  /**
   * @return the df_n
   */
  public int getDf_n() {
		return df_n;
  }

  /**
   * @return the df_first_quartile
   */
  public float getDf_first_quartile() {
		return df_first_quartile;
  }

  /**
   * @return the df_second_quartile
   */
  public float getDf_second_quartile() {
		return df_second_quartile;
  }

  /**
   * @return the df_third_quartile
   */
  public float getDf_third_quartile() {
		return df_third_quartile;
  }

  /**
   * @return the df_nintey_fifth
   */
  public float getDf_nintey_fifth() {
		return df_nintey_fifth;
  }

  /**
   * @return the urban_n
   */
  public int getUrban_n() {
		return urban_n;
  }

  /**
   * @return the urban_first_quartile
   */
  public float getUrban_first_quartile() {
		return urban_first_quartile;
  }

  /**
   * @return the urban_second_quartile
   */
  public float getUrban_second_quartile() {
		return urban_second_quartile;
  }

  /**
   * @return the urban_third_quartile
   */
  public float getUrban_third_quartile() {
		return urban_third_quartile;
  }

  /**
   * @return the urban_nintey_fifth
   */
  public float getUrban_nintey_fifth() {
		return urban_nintey_fifth;
  }

  /**
   * Given a value for terrain, returns the sample size (n) for the number of searches
   * contributing to the statistical 25%,50%,75%, and 95% distances.
   * 
   * @param terrain String representation of the terrain category (e.g Temperate:Flat).
   * @return n
   * @throws NoSuchTerrainException if terrain is not a known value.
   */
  public int getN(String terrain) throws net.aa3sd.SMT.exceptions.NoSuchTerrainException {
 
		int result = -1;
		if (terrain.equals("Urban")) { 
			result = this.getUrban_n();
		}
		if (terrain.equals("Temperate:Flat")) { 
			result = this.getTf_n();
		}
		if (terrain.equals("Temperate:Mountanous")) { 
			result = this.getTm_n();
		}
		if (terrain.equals("Dry:Flat")) { 
			result = this.getDf_n();
		}
		if (terrain.equals("Dry:Mountanous")) { 
			result = this.getDm_n();
		}
		if (result==-1) { 
			throw new NoSuchTerrainException("Unknown terrain: " + terrain);
		}
		return result;
  }

  public float get25thPercentile(String terrain) throws net.aa3sd.SMT.exceptions.NoSuchTerrainException {
 
		float result = -1f;
		if (terrain.equals("Urban")) { 
			result = this.getUrban_first_quartile();
		}
		if (terrain.equals("Temperate:Flat")) { 
			result = this.getTf_first_quartile();
		}
		if (terrain.equals("Temperate:Mountanous")) { 
			result = this.getTm_first_quartile();
		}
		if (terrain.equals("Dry:Flat")) { 
			result = this.getDf_first_quartile();
		}
		if (terrain.equals("Dry:Mountanous")) { 
			result = this.getDm_first_quartile();
		}
		if (result<0) { 
			throw new NoSuchTerrainException("Unknown terrain: " + terrain);
		}
		return result;
  }

}
