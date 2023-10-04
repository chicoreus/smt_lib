/**
 * SubjectGroup.java
 * Created Aug 19, 2011 6:50:37 PM
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
import net.aa3sd.SMT.interfaces.SubjectListener;
/**
 * @author mole
 */
public class SubjectGroup extends Subject {
  private Vector<SubjectGroupMember> members;

  /**
   * @param name
   * @param numberOfSubjects
   */
  public SubjectGroup(String name, int numberOfSubjects) {
		super(name, numberOfSubjects);
		members = new Vector<SubjectGroupMember>();
		for(int i=0; i<numberOfSubjects; i++) { 
			members.add(new SubjectGroupMember());
		}
		
  }

}
