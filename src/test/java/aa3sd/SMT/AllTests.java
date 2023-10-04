/**
 *  Copyright (C) 2008 Paul J. Morris  
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.aa3sd.SMT.test;

import net.aa3sd.SMT.SMTSingleton;
import net.aa3sd.SMT.log.EventLog;
import net.aa3sd.SMT.log.LogEvent;
import junit.framework.Test;
import junit.framework.TestSuite;
public class AllTests {
  public static Test suite()
  {
		init();
		TestSuite suite = new TestSuite("Test for net.aa3sd.SMT.test");
		//$JUnit-BEGIN$
		suite.addTestSuite(testSingletonThreadSafety.class);
		suite.addTestSuite(testResourceAllocation.class);
		suite.addTestSuite(testApplyPOD.class);
		suite.addTestSuite(testPODCalculator.class);
		suite.addTestSuite(testUnitConverter.class);
		suite.addTestSuite(testOfUtilityFunctions.class);
		suite.addTestSuite(testResource.class);
		suite.addTestSuite(TestUrgencyFactorGroup.class);
		//$JUnit-END$
		return suite;
  }

  private static void init()
  {
 
		// Instantiate a EventLog and provide it to Singleton. 
		// Some tests call methods that log events to the active log obtained from the singleton.
		EventLog eventLog = new EventLog();
		eventLog.LogAnEvent(new LogEvent("TestSuite",LogEvent.EVENT_SYSTEM,"AllTests test suite started."));
		SMTSingleton.getSingletonInstance().setActiveLog(eventLog);
  }

}
