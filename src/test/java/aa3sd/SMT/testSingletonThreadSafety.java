/**
 * testSingletonThreadSafety.java
 * Created Dec 5, 2008 8:22:19 AM
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

import net.aa3sd.SMT.SMTSingleton;
import net.aa3sd.SMT.log.EventLog;
import junit.framework.TestCase;
/**
 * @author mole
 */
public class testSingletonThreadSafety extends TestCase {
  /**
   * 
   * @param name
   */
  public testSingletonThreadSafety(String name) {
		super(name);
  }

  /**
   * Test method for {@link net.aa3sd.SMT.SMTSingleton#getSingletonInstance()}
   * Tries to test thread safety of the Singleton, check standard error for exceptions indicating
   * test failures.   
   */
  public void testGetSingletonInstance_check_stderr() {
		EventLog l = SMTSingleton.getSingletonInstance().getActiveLog();
		EventLog l1 = new EventLog();
		assertTrue(l==SMTSingleton.getSingletonInstance().getActiveLog());
		assertFalse(l==l1);
		// try to obtain a SMTSingleton in a separate thread, test to see if the 
		// logs are the same.
		new Thread(new ThreadTest(l)).start();
		new Thread(new ThreadTest(l)).start();
		new Thread(new ThreadTest(l)).start();
  }

  class ThreadTest extends Runnable {
    private net.aa3sd.SMT.log.EventLog outerLog;

    ThreadTest(net.aa3sd.SMT.log.EventLog l) {
			outerLog = l;
    }

    /**
     *  Runnable.run() seems to conflict with TestCase.run(), so throw exception to standard
     * error rather than raising a test case failure.  Need to look into JUnit support for testing
     * across threads.... 
     * (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
			EventLog l1 = new EventLog();
		    // will throw exception to console if SMTSingleton isn't thread safe.
			assertTrue(outerLog==SMTSingleton.getSingletonInstance().getActiveLog());
			assertFalse(outerLog==l1);			
    }

  }

}
