/**
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
package net.aa3sd.SMT.test;

import net.aa3sd.SMT.math.UnitConverter;
import junit.framework.Assert;
import junit.framework.TestCase;
public class testUnitConverter extends TestCase {
  public testUnitConverter(String name) {
		super(name);
  }

  public void testSquareMilesToAcres() {
		assertEquals(640d,UnitConverter.squareMilesToAcres(1d));
  }

  public void testAcresToSquareMiles() {
		assertEquals(1d,UnitConverter.acresToSquareMiles(640d));
  }

  public void testRadiusFromArea() {
		assertEquals(1d, UnitConverter.radiusFromArea(Math.PI));
		assertEquals(2d, UnitConverter.radiusFromArea(12.56637061435917295376));
		assertEquals(16d, UnitConverter.radiusFromArea(804.24771931898706904064));
		assertEquals(2.25d, UnitConverter.radiusFromArea(15.90431280879832826960));
  }

  public void testAreaFromRadius() {
		Assert.assertEquals(Math.PI, UnitConverter.areaFromRadius(1d));
		assertEquals(12.56637061435917295376, UnitConverter.areaFromRadius(2d));
		assertEquals(804.24771931898706904064, UnitConverter.areaFromRadius(16d));
		assertEquals(15.904312808798327, UnitConverter.areaFromRadius(2.25d));
  }

}
