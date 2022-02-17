package servicewolf.test.manager;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import servicewolf.src.command.Command;
import servicewolf.src.command.Command.CommandValue;
import servicewolf.src.incident.Incident;
import servicewolf.src.manager.ServiceWolfManager;
import servicewolf.src.service_group.ServiceGroup;


/**
 * Tests the ServiceWolfManager class
 * @author Tyler Strickland
 *
 */
public class ServiceWolfManagerTest {

	/** Singleton instance of the ServiceWolfManager */
	private ServiceWolfManager manager;
	/** Test file that can be used for testing */
	private final String testFile = "test-files/incidents1.txt";

	/**
	 * Sets up the Service Wolf Manager and clears the data.
	 * @throws Exception if error
	 */
	@Before
	public void setUp() throws Exception {
		manager = ServiceWolfManager.getInstance();
		manager.resetManager();
	}
	
	
	/**
	 * Tests saving ServiceGroups to a file
	 */
	@Test
	public void testSaveToFile() {
		manager.addServiceGroup("CSC IT");
		assertEquals(1, manager.getServiceGroupList().length);
		manager.loadServiceGroup("CSC IT");
		manager.addIncidentToServiceGroup("Moodle down", "sesmith5", "When I go to wolfware.ncsu.edu, I get a 500 error");
		manager.saveToFile("test-files/actual_incident13.txt");
		checkFiles("test-files/expected_saved_file.txt", "test-files/actual_incident13.txt");
		
		manager.clearServiceGroups();
		assertEquals(0, manager.getServiceGroupList().length);

		manager.addServiceGroup("ServiceGroup1");
		manager.addServiceGroup("ServiceGroup2");
		manager.addServiceGroup("ServiceGroup3");
		assertEquals(3, manager.getServiceGroupList().length);
		
		manager.loadServiceGroup("ServiceGroup1");
		manager.addIncidentToServiceGroup("title1", "caller1", "message1");
		Command c1 = new Command(CommandValue.ASSIGN, "sesmith5", "message2");
		manager.executeCommand(1, c1);
		
		manager.loadServiceGroup("ServiceGroup2");
		manager.addIncidentToServiceGroup("title2", "caller2", "message1");
		Command c2 = new Command(CommandValue.CANCEL, "Caller Canceled", "message2");
		manager.executeCommand(1, c2);
		
		manager.loadServiceGroup("ServiceGroup3");
		manager.addIncidentToServiceGroup("title3", "caller3", "message1");
		Command c3 = new Command(CommandValue.ASSIGN, "cgurley", "message2");
		manager.executeCommand(1, c3);
		Command c4 = new Command(CommandValue.RESOLVE, "Resolution Workaround", "message3");
		manager.executeCommand(1, c4);
		manager.addIncidentToServiceGroup("title4", "caller4", "message1");
		manager.executeCommand(2, c1);
		
		manager.saveToFile("test-files/actual_saved_file.txt");
		checkFiles("test-files/expected_saved_file2.txt", "test-files/actual_saved_file.txt");
		
		
	}

	/**
	 * Test loading ServiceGroups and their Incidents from a file
	 */
	@Test
	public void testLoadFromFile() {
		manager.loadFromFile(testFile);
		assertEquals(3, manager.getServiceGroupList().length);
	}
	
	/**
	 * Tests loading a ServiceGroup and making it the currentServiceGroup
	 */
	@Test
	public void testLoadServiceGroup() {
		manager.addServiceGroup("Service Group");
		
		try {
			manager.loadServiceGroup("Not Service Group");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(1, manager.getServiceGroupList().length);
		}
		
		manager.loadServiceGroup("Service Group");
		assertEquals("Service Group", manager.getServiceGroupName());
	}

	/**
	 * Tests getting the array of Incidents for each ServiceGroup
	 */
	@Test
	public void testGetIncidentsAsArray() {
		manager.addServiceGroup("ServiceWolf");
		manager.loadServiceGroup("ServiceWolf");
		manager.addIncidentToServiceGroup("title1", "caller1", "message1");
		
		String[][] actual = manager.getIncidentsAsArray();
		assertEquals(Integer.toString(1), actual[0][0]);
		assertEquals(Incident.NEW_NAME, actual[0][1]);
		assertEquals("title1", actual[0][2]);
		assertEquals(Incident.NO_STATUS, actual[0][3]);
		
	}

	/**
	 * Test getting Incidents in a ServiceGroup by calling for their id
	 */
	@Test
	public void testGetIncidentById() {
		ServiceGroup s = new ServiceGroup("Service Group");
		manager.addServiceGroup(s.getServiceGroupName());
		manager.loadServiceGroup(s.getServiceGroupName());
		assertEquals(0, manager.getIncidentsAsArray().length);
		
		assertNull(manager.getIncidentById(1));
		ArrayList<String> incidentLog = new ArrayList<String>();
		incidentLog.add("message");
		manager.addIncidentToServiceGroup("title1", "caller1", "message1");
		manager.addIncidentToServiceGroup("title2", "caller2", "message2");
		assertEquals("New", manager.getIncidentById(1).getState());
		assertEquals("title1", manager.getIncidentById(1).getTitle());
		assertEquals("caller1", manager.getIncidentById(1).getCaller());
		assertEquals("Unowned", manager.getIncidentById(1).getOwner());
		assertEquals("No Status", manager.getIncidentById(1).getStatusDetails());
		
		
		assertEquals("New", manager.getIncidentById(2).getState());
		assertEquals("title2", manager.getIncidentById(2).getTitle());
		assertEquals("caller2", manager.getIncidentById(2).getCaller());
		assertEquals("Unowned", manager.getIncidentById(2).getOwner());
		assertEquals("No Status", manager.getIncidentById(2).getStatusDetails());
		
		Command c = new Command(CommandValue.ASSIGN, "cgurley", "new message");
		manager.executeCommand(2, c);
		assertEquals("In Progress", manager.getIncidentById(2).getState());
		assertEquals("title2", manager.getIncidentById(2).getTitle());
		assertEquals("caller2", manager.getIncidentById(2).getCaller());
		assertEquals("cgurley", manager.getIncidentById(2).getOwner());
		assertEquals("No Status", manager.getIncidentById(2).getStatusDetails());
	}

	/**
	 * Tests executing commands on Incidents in ServiceGroups
	 */
	@Test
	public void testExecuteCommand() {
		manager.addServiceGroup("Service Group");
		manager.loadServiceGroup("Service Group");
		String message = "message";
		ArrayList<String> incidentLog = new ArrayList<String>();
		incidentLog.add(message);
		Incident i = new Incident(1, "New", "Moodle", "caller", 0, "Unowned", "No Status", incidentLog);
		manager.addIncidentToServiceGroup(i.getTitle(), i.getIncidentLogMessages(), i.getCaller());
		assertEquals(Incident.NEW_NAME, manager.getIncidentById(1).getState());
		
		Command command = new Command(CommandValue.ASSIGN, "owner", "message");
		manager.executeCommand(1, command);
		
		assertEquals(Incident.IN_PROGRESS_NAME, manager.getIncidentById(1).getState());
		assertEquals("owner", manager.getIncidentById(1).getOwner());
		

		
	}

	/**
	 * Tests deleting a ServiceGroup's Incidents by calling their id
	 */
	@Test
	public void testDeleteIncidentById() {

		ServiceGroup s = new ServiceGroup("Service Group");
		manager.addServiceGroup(s.getServiceGroupName());
		manager.loadServiceGroup(s.getServiceGroupName());
		manager.addIncidentToServiceGroup("Moodle down", "tstrick2", "Help");
		assertEquals(1, manager.getIncidentsAsArray().length);
		manager.deleteIncidentById(1);
		assertEquals(0, manager.getIncidentsAsArray().length);
		
		
	}

	/**
	 * Tests adding an Incident to a ServiceGroup
	 */
	@Test
	public void testAddIncidentToServiceGroup() {
		String caller = "Joe";
		String title = "Piazza down";
		String message = "Fix";
		manager.addServiceGroup("Service Group");
		assertEquals(1, manager.getServiceGroupList().length);
		
		manager.loadServiceGroup("Service Group");
		manager.addIncidentToServiceGroup(title, caller, message);
		assertEquals(1, manager.getIncidentsAsArray().length);
		
		manager.clearServiceGroups();
		manager.loadFromFile("test-files/incidents1.txt");
		manager.loadServiceGroup("CSC IT");
		manager.addIncidentToServiceGroup("title1", "caller1", "message1");
		assertEquals(5, manager.getIncidentsAsArray().length);
		
		manager.loadServiceGroup("ITECS");
		manager.addIncidentToServiceGroup("title2", "caller2", "message2");
		assertEquals(2, manager.getIncidentsAsArray().length);
		
		manager.loadServiceGroup("OIT");
		manager.addIncidentToServiceGroup("title3", "caller3", "message3");
		assertEquals(2, manager.getIncidentsAsArray().length);
		
		
		
	}


	/**
	 * Tests getting the name of the current ServiceGroup
	 */
	@Test
	public void testGetServiceGroupName() {
		
		ServiceGroup s = new ServiceGroup("Service Group");
		manager.addServiceGroup(s.getServiceGroupName());
		
		manager.loadServiceGroup("Service Group");
		
		assertEquals("Service Group", manager.getServiceGroupName());
		
		
	}

	/**
	 * Tests getting the list of ServiceGroups
	 */
	@Test
	public void testGetServiceGroupList() {
		
		manager.addServiceGroup("C");
		manager.addServiceGroup("B");
		manager.addServiceGroup("A");
		
		
		String[] actual = manager.getServiceGroupList();
		
		assertEquals("A", actual[0]);
		assertEquals("B", actual[1]);
		assertEquals("C", actual[2]);
	}

	/**
	 * Tests clearing the list of ServiceGroups
	 */
	@Test
	public void testClearServiceGroups() {
		ServiceGroup s = new ServiceGroup("Service Group");
		manager.addServiceGroup(s.getServiceGroupName());
		assertEquals(1, manager.getServiceGroupList().length);
		
		manager.clearServiceGroups();
		
		assertEquals(0, manager.getServiceGroupList().length);
		
	}

	/**
	 * Tests editing the name of the current ServiceGroup
	 */
	@Test
	public void testEditServiceGroup() {
		//Invalid update name null 
		ServiceGroup s = new ServiceGroup("Service Group");
		manager.addServiceGroup("Service Group");
		manager.loadServiceGroup(s.getServiceGroupName());
		try {
			manager.editServiceGroup(null);
			fail();
			
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid service group name.", e.getMessage());
		}
		
		//Invalid update name empty string
		try {
			manager.editServiceGroup("");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid service group name.", e.getMessage());
		}
		
		// Invalid update name duplicate
		ServiceGroup s2 = new ServiceGroup("Pack IT");
		manager.addServiceGroup(s2.getServiceGroupName());
		manager.loadServiceGroup(s2.getServiceGroupName());
		try {
			manager.editServiceGroup("Service Group");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid service group name.", e.getMessage());
			assertEquals("Pack IT", s2.getServiceGroupName());
		}
	}

	/**
	 * Tests adding a new ServiceGroup to the list of ServiceGroups
	 */
	@Test
	public void testAddServiceGroup() {
		ServiceGroup s1 = new ServiceGroup("Service Group");
		ServiceGroup s2 = new ServiceGroup("Pack IT");
		ServiceGroup s3 = new ServiceGroup("ITECS");
		ServiceGroup s4 = new ServiceGroup("Service Group");
		
		try {
			manager.addServiceGroup(s1.getServiceGroupName());
			assertEquals(1, manager.getServiceGroupList().length);
			manager.addServiceGroup(s2.getServiceGroupName());
			assertEquals(2, manager.getServiceGroupList().length);
			manager.addServiceGroup(s3.getServiceGroupName());
			assertEquals(3, manager.getServiceGroupList().length);
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}

		try {
			manager.addServiceGroup(s4.getServiceGroupName());
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid service group name.", e.getMessage());
		}
			
		try {
			manager.addServiceGroup(null);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid service group name.", e.getMessage());
		}
		
		try {
			manager.addServiceGroup("");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid service group name.", e.getMessage());
		}
	
	}
	/**
	 * Tests deleting the currentServiceGroup
	 */
	@Test
	public void testDeleteServiceGroup() {
		
		
		//Test delete with service group selected
		manager.addServiceGroup("Service Group");
		assertEquals(1, manager.getServiceGroupList().length);
		manager.addServiceGroup("Pack IT");
		assertEquals(2, manager.getServiceGroupList().length);
		manager.addServiceGroup("ITECS");
		assertEquals(3, manager.getServiceGroupList().length);
		
		manager.loadServiceGroup("Service Group");
		manager.deleteServiceGroup();
		assertEquals(2, manager.getServiceGroupList().length);
		manager.loadServiceGroup("Pack IT");
		manager.deleteServiceGroup();
		assertEquals(1, manager.getServiceGroupList().length);
		manager.loadServiceGroup("ITECS");
		manager.deleteServiceGroup();
		assertEquals(0, manager.getServiceGroupList().length);
		
		
		
	}
	
	/**
	 * Helper method to compare two files for the same contents
	 * @param expFile expected output
	 * @param actFile actual output
	 */
	private void checkFiles(String expFile, String actFile) {
		try {
			Scanner expScanner = new Scanner(new FileInputStream(expFile));
			Scanner actScanner = new Scanner(new FileInputStream(actFile));
			
			while (expScanner.hasNextLine()) {
				assertEquals(expScanner.nextLine(), actScanner.nextLine());
			}
			
			expScanner.close();
			actScanner.close();
		} catch (IOException e) {
			fail("Error reading files.");
		}
	}

}
