package servicewolf.test.service_group;


import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import servicewolf.src.command.Command;
import servicewolf.src.command.Command.CommandValue;
import servicewolf.src.incident.Incident;
import servicewolf.src.service_group.ServiceGroup;


/**
 * Tests the ServiceGroup class
 * @author Tyler Strickland
 *
 */
public class ServiceGroupTest {
	
	/**
	 * Tests creating valid ServiceGroup
	 */
	@Test
	public void testServiceGroup() {	
		//Test valid construction
		try {
			ServiceGroup s = new ServiceGroup("Pack IT");
			assertEquals("Pack IT", s.getServiceGroupName());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}	
		
	}

	/**
	 * Tests setting the incident counter
	 */
	@Test
	public void testSetIncidentCounter() {
		ArrayList<String> incidentLog = new ArrayList<String>();
		incidentLog.add("message");
		Incident i = new Incident(1, Incident.NEW_NAME, "Forgot password", "jctetter", 0, "Unowned", "No Status", incidentLog);
		ServiceGroup s = new ServiceGroup("Pack IT");
		s.addIncident(i);
		assertEquals(1, s.getIncidents().size());
		
		s.setIncidentCounter();
		Incident i2 = new Incident("title", "caller", "message");
		assertEquals(2, i2.getId());
	}

	/**
	 * Tests setting the ServiceGroup name
	 */
	@Test
	public void testSetServiceGroupName() {
		//Test invalid null
		ServiceGroup s = null;
		try {
			s = new ServiceGroup(null);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid service group name.", e.getMessage());
			assertNull(s);
		}
		
		//Test invalid empty string 
		ServiceGroup s1 = null;
		try {
			s = new ServiceGroup("");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid service group name.", e.getMessage());
			assertNull(s1);
		}
		
		//Test valid names
		try {
			ServiceGroup s2 = new ServiceGroup("CSC IT");
			assertEquals("CSC IT", s2.getServiceGroupName());
			
			ServiceGroup s3 = new ServiceGroup("Pack IT");
			assertEquals("Pack IT", s3.getServiceGroupName());
			
			ServiceGroup s4 = new ServiceGroup("ITECS");
			assertEquals("ITECS", s4.getServiceGroupName());
			
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Tests adding an Incident to the ServiceGroup
	 */
	@Test
	public void testAddIncident() {
		ArrayList<String> incidentLog = new ArrayList<String>();
		incidentLog.add("message");
		
		Incident i = new Incident(1, Incident.NEW_NAME, "Forgot password", "jctetter", 0, "Unowned", "No Status", incidentLog);
		ServiceGroup s = new ServiceGroup("Pack IT");
		s.addIncident(i);
		assertEquals(1, s.getIncidents().size());
		
		Incident i2 = new Incident(2, "Resolved", "Technical problem", "tstrick2", 0, "sesmith5", Incident.RESOLUTION_PERMANENTLY_SOLVED, incidentLog);
		s.addIncident(i2);
		assertEquals(2, s.getIncidents().size());
		
		Incident i3 = new Incident(11, "On Hold", "Jenkins issue", "sesmith5", 0, "tstrick2", Incident.HOLD_AWAITING_CALLER, incidentLog);
		s.addIncident(i3);
		assertEquals(3, s.getIncidents().size());
		
		Incident i2a = new Incident(2, "In Progress", "Jenkins behind firewall", "sesmith5", 0, "cgurley", "No Status", incidentLog);
		try {
			s.addIncident(i2a);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Incident cannot be created.", e.getMessage());
			assertEquals(3, s.getIncidents().size());
		}
		
		Incident i4 = new Incident(4, Incident.NEW_NAME, "Can't get into email", "jctetter", 0, "Unowned", "No Status", incidentLog);
		s.addIncident(i4);
		assertEquals(4, s.getIncidents().size());

		assertEquals(i, s.getIncidents().get(0)); //Check incident with smallest id is at the front of the list
		assertEquals(i2, s.getIncidents().get(1)); //Check incident with second smallest id is at the second index of the list.
		assertEquals(i4, s.getIncidents().get(2)); //Check incident with third smallest id is at the third index of the list.
		assertEquals(i3, s.getIncidents().get(3)); //check incident with largest id is at the last spot in the list.
		
		
	}

	/**
	 * Tests getting the ArrayList of Incidents for the ServiceGroup
	 */
	@Test
	public void testGetIncidents() {
		ArrayList<String> incidentLog = new ArrayList<String>();
		incidentLog.add("New message");
		Incident i = new Incident(1, "New", "Piazza down", "tstrick2", 0, "Unowned", "No Status", incidentLog);
		Incident i2 = new Incident(2, "New", "Github down", "tstrick2", 0, "Unowned", "No Status", incidentLog);
		Incident i3 = new Incident(3, "New", "Jenkins down", "bob", 0, "Unowned", "No Status", incidentLog);
		ServiceGroup s = new ServiceGroup("Pack IT");
		
		s.addIncident(i);
		s.addIncident(i2);
		s.addIncident(i3);
		
		ArrayList<Incident> expected = new ArrayList<Incident>();
		expected.add(i);
		expected.add(i2);
		expected.add(i3);
		
		assertEquals(expected, s.getIncidents());
	}

	/**
	 * Tests getting an Incident in the ServiceGroup by calling the Incident id
	 */
	@Test
	public void testGetIncidentById() {
		ArrayList<String> incidentLog = new ArrayList<String>();
		incidentLog.add("message");
		Incident i = new Incident(1, "New", "Piazza down", "tstrick2", 0, "Unowned", Incident.NO_STATUS, incidentLog);
		Incident i2 = new Incident(2, "New", "Github down", "tstrick2", 0, "Unowned", Incident.NO_STATUS, incidentLog);
		Incident i3 = new Incident(3, "New", "Jenkins down", "bob", 0, "Unowned", Incident.NO_STATUS, incidentLog);
		ServiceGroup s = new ServiceGroup("Pack IT");
		s.addIncident(i);
		s.addIncident(i2);
		s.addIncident(i3);
		
		assertEquals(i, s.getIncidentById(1));
		assertEquals(i2, s.getIncidentById(2));
		assertEquals(i3, s.getIncidentById(3));
		
		
	}

	/**
	 * Tests executing commands on Incidents in the ServiceGroup
	 */
	@Test
	public void testExecuteCommand() {
		ArrayList<String> incidentLog = new ArrayList<String>();
		incidentLog.add("New");
		Incident i = new Incident(1, "New", "Piazza down", "tstrick2", 0, "Unowned", "No Status", incidentLog);
		ServiceGroup s = new ServiceGroup("Pack IT");
		s.addIncident(i);
		
		Command command = new Command(CommandValue.ASSIGN, "cgurley", "message");
		
		s.executeCommand(1, command);
		
		assertEquals(Incident.IN_PROGRESS_NAME, i.getState());
		assertEquals("cgurley", i.getOwner());
		
		Command command2 = new Command(CommandValue.HOLD, "Awaiting Vendor", "message");
		s.executeCommand(1, command2);
		assertEquals(Incident.ON_HOLD_NAME, i.getState());
		
		Command c = new Command(CommandValue.INVESTIGATE, null, "Message");
		s.executeCommand(1, c);
		assertEquals(Incident.IN_PROGRESS_NAME, i.getState());
		
		Command command3 = new Command(CommandValue.RESOLVE, Incident.RESOLUTION_WORKAROUND, "message");
		s.executeCommand(1, command3);
		assertEquals(Incident.RESOLVED_NAME, i.getState());
		
		Command command4 = new Command(CommandValue.CANCEL, Incident.CANCELLATION_CALLER_CANCELLED, "message");
		s.executeCommand(1, command4);
		assertEquals(Incident.CANCELED_NAME, i.getState());
		
		
	}

	/**
	 * Tests deleting an Incident in the ServiceGroup by calling the Incident id
	 */
	@Test
	public void testDeleteIncidentById() {
		ArrayList<String> incidentLog = new ArrayList<String>();
		incidentLog.add("Message");
		Incident i = new Incident(1, "New", "Piazza down", "tstrick2", 0, "Unowned", "No Status", incidentLog);
		Incident i2 = new Incident(2, "New", "Github down", "tstrick2", 0, "Unowned", "No Status", incidentLog);
		Incident i3 = new Incident(3, "New", "Jenkins down", "bob", 0, "Unowned", "No Status", incidentLog);
		ServiceGroup s = new ServiceGroup("Pack IT");
		s.addIncident(i);
		s.addIncident(i2);
		s.addIncident(i3);
		
		assertEquals(3, s.getIncidents().size());
		s.deleteIncidentById(1);
		assertEquals(2, s.getIncidents().size());
		s.deleteIncidentById(2);
		assertEquals(1, s.getIncidents().size());
		s.deleteIncidentById(3);
		assertEquals(0, s.getIncidents().size());
	}

}
