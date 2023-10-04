/**
 * testResource.java
 * Created Dec 5, 2008 7:58:38 AM
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
package net.aa3sd.SMT.test;

import net.aa3sd.SMT.resources.BadStatusException;
import net.aa3sd.SMT.resources.Resource;
import net.aa3sd.SMT.search.Task;
import junit.framework.TestCase;
/**
 * @author mole
 */
public class testResource extends TestCase {
  /**
   * @param name
   */
  public testResource(String name) {
		super(name);
  }

  /**
   * Test method for {@link net.aa3sd.SMT.resources.Resource#Resource(java.lang.String, java.lang.String)}.
   */
  public void testResourceConstructor() {
		Resource r = new Resource("PersonName","Canine Handler and Air Scent Canine", Task.KIND_TASK_FORCE);
		assertEquals(r.getStatus(), Resource.STATUS_NEEDED);
  }

  /**
   * Test method for {@link net.aa3sd.SMT.resources.Resource#changeStatus(int)}.
   */
  public void testChangeStatus() {
		Resource r = new Resource("PersonName","Canine Handler and Air Scent Canine", Task.KIND_TASK_FORCE);
		assertEquals(r.getStatus(), Resource.STATUS_NEEDED);
		try { 
		   r.changeStatus(Resource.STATUS_REQUESTED);
		   assertEquals(r.getStatus(), Resource.STATUS_REQUESTED);   
		} catch (BadStatusException e) { 
		   fail("Threw unexpected BadStatusException: " + e.getMessage());
		}
		try { 
			r.changeStatus(-1);
			fail("Failed to throw expected BadStatusException on setting status to an invalid value of -1");
		} catch (BadStatusException e) { 
			assertEquals(1,1);  // Threw expected exception.
		}
  }

}
