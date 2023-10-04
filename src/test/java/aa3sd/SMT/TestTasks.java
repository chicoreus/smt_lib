/**
 * TestTasks.java
 * Created Sep 19, 2011 7:20:59 PM
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
package net.aa3sd.SMT.test;

import net.aa3sd.SMT.resources.Resource;
import net.aa3sd.SMT.search.Task;
import junit.framework.TestCase;
/**
 * @author mole
 */
public class TestTasks extends TestCase {
  /**
   * Test method for {@link net.aa3sd.SMT.search.Task#getKind()}.
   */
  public void testGetKind() {
		
		Resource engine = new Resource();
		engine.setType("Engine");
		Resource engine2 = new Resource();
		engine2.setType("Engine");
	    Resource canine = new Resource();
	    canine.setType("Air Scent Canine and Handler");
	    
	    Task task1 = new Task();
	    assertEquals(Task.KIND_NO_RESOURCE_SPECIFIED, task1.getKind());
	    
	    // Single resource
	    task1.addResource(engine);
	    assertEquals(Task.KIND_SINGLE_RESOURCE,task1.getKind());
	    
	    // Strike team - two or more resources of same type.
	    task1.addResource(engine2);
	    assertEquals(Task.KIND_STRIKE_TEAM,task1.getKind());

	    // Task force - two or more resources of different types under common leader with common communications.
	    task1.addResource(canine);
	    assertEquals(Task.KIND_TASK_FORCE,task1.getKind());
		
  }

}
