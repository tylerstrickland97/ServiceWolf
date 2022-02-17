package servicewolf.test.incident;


import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import servicewolf.src.command.Command;
import servicewolf.src.command.Command.CommandValue;
import servicewolf.src.incident.Incident;


/**
 * Tests the Incident class
 * @author Tyler Strickland
 *
 */
public class IncidentTest {
	
	/** Title of an Incident to use in testing */
	private static final String TITLE = "Github down";
	/** Message of an Incident to use in testing */
	private static final String MESSAGE = "message";
	/** Caller of an Incident to use in testing */
	private static final String CALLER = "tstrick2";
	/** State of an Incident to use in testing */
	private static final String STATE = "New";
	/** Owner of an Incident to use in testing */
	private static final String OWNER = "Unowned";
	/** Status Details of an Incident to use in testing */
	private static final String STATUS_DETAILS = "No Status";
	/** incidentLog of an Incident to use in testing */
	private ArrayList<String> incidentLog;
	
	
	/**
	 * Tests constructing a new Incident using small constructor with valid parameters 
	 */
	@Test
	public void testIncidentStringStringString() {
		Incident i = null;
		try {
			i = new Incident(TITLE, CALLER, MESSAGE);
			assertEquals(TITLE, i.getTitle());
			assertEquals("- " + MESSAGE + '\n', i.getIncidentLogMessages());
			assertEquals(CALLER, i.getCaller());
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Tests constructing a new Incident using big constructor with valid parameters and invalid parameters
	 */
	@Test
	public void testIncidentIntStringStringStringIntStringStringArrayListOfString() {
		incidentLog = new ArrayList<String>();
		incidentLog.add("message");
		Incident i = null;
		try {
			i = new Incident(1, STATE, TITLE, CALLER, 0, OWNER, STATUS_DETAILS, incidentLog);
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
		
		i = null;

		
		
		//Invalid state name
		try {
			i = new Incident(1, null, TITLE, CALLER, 0, OWNER, STATUS_DETAILS, incidentLog);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(i);
		}
		
		//Invalid caller
		try {
			i = new Incident(1, STATE, null, CALLER, 0, OWNER, STATUS_DETAILS, incidentLog);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(i);
		}
		
		//Invalid caller
		try {
			i = new Incident(1, STATE, TITLE, null, 0, OWNER, STATUS_DETAILS, incidentLog);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(i);
		}
		
		//Invalid reopenCount
		try {
			i = new Incident(1, STATE, TITLE, CALLER, -1, OWNER, STATUS_DETAILS, incidentLog);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(i);
		}
		
		//Invalid owner
		try {
			i = new Incident(1, STATE, TITLE, CALLER, 0, null, STATUS_DETAILS, incidentLog);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(i);
		}
		
		//Invalid statusDetails
		try {
			i = new Incident(1, STATE, TITLE, CALLER, 0, OWNER, null, incidentLog);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(i);
		}
		
	
		
	}

	/**
	 * Tests setting the id of the Incident
	 */
	@Test
	public void testSetId() {
		// Invalid incidentId null
		incidentLog = new ArrayList<String>();
		incidentLog.add("Message");
		Incident i = null;
		try {
			i = new Incident(-1, STATE, TITLE, CALLER, 0, OWNER, STATUS_DETAILS, incidentLog);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(i);
		}
		
			Incident i2 = new Incident(1, STATE, TITLE, CALLER, 0, OWNER, STATUS_DETAILS, incidentLog);
			assertEquals(1, i2.getId());
			i2.setId(2);
			assertEquals(2, i2.getId());
			
			Incident i3 = new Incident(3, STATE, TITLE, CALLER, 0, OWNER, STATUS_DETAILS, incidentLog);
			assertEquals(3, i3.getId());
			i3.setId(4);
			assertEquals(4, i3.getId());

		
		
	}

	/**
	 * Tests setting the state of the Incident
	 */
	@Test
	public void testSetState() {
		//Invalid state name null
		Incident i = null;
		incidentLog = new ArrayList<String>();
		incidentLog.add("message");
		try {
			i = new Incident(1, null, TITLE, CALLER, 0, OWNER, STATUS_DETAILS, incidentLog);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(i);
		}
		
	
		//Invalid state name empty string
		Incident i2 = null;
		try {
			i2 = new Incident(2, "", TITLE, CALLER, 0, OWNER, STATUS_DETAILS, incidentLog);
			fail();
		} catch(IllegalArgumentException e) {
			assertNull(i2);
		}
		
		//Valid state names
		try {
			incidentLog = new ArrayList<String>();
			String message = "message";
			incidentLog.add(message);
			Incident i3 = new Incident(3, STATE, TITLE, CALLER, 0, OWNER, STATUS_DETAILS, incidentLog);
			assertEquals(STATE, i3.getState());
			i3.setOwner("joe");
			assertEquals("joe", i3.getOwner());
			i3.setState(Incident.IN_PROGRESS_NAME);
			assertEquals("In Progress", i3.getState());
			
			ArrayList<String> incidentLog2 = new ArrayList<String>();
			String message1 = "message";
			incidentLog2.add(message1);
			Incident i4 = new Incident(4, STATE, TITLE, CALLER, 0, OWNER, STATUS_DETAILS, incidentLog);
			assertEquals(STATE, i4.getState());
			i4.setOwner("joe");
			i4.setStatusDetails(Incident.HOLD_AWAITING_CHANGE);
			i4.setState(Incident.ON_HOLD_NAME);
			assertEquals("On Hold", i4.getState());
		} catch (IllegalArgumentException e) {
			fail();
		}
		
	}

	/**
	 * Tests setting the title of the Incident
	 */
	@Test
	public void testSetTitle() {
		//Invalid title null
		Incident i = null;
		incidentLog = new ArrayList<String>();
		incidentLog.add("message");
		try {
			i = new Incident(1, STATE, null, CALLER, 0, OWNER, STATUS_DETAILS, incidentLog);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(i);
		}
		
		//Invalid state title empty string
		Incident i2 = null;
		try {
			i2 = new Incident(2, STATE, "", CALLER, 0, OWNER, STATUS_DETAILS, incidentLog);
			fail();
		} catch(IllegalArgumentException e) {
			assertNull(i2);
		}
		
		//Valid state title
		try {
			Incident i3 = new Incident(3, STATE, TITLE, CALLER, 0, OWNER, STATUS_DETAILS, incidentLog);
			assertEquals(TITLE, i3.getTitle());
			i3.setTitle("Piazza down");
			assertEquals("Piazza down", i3.getTitle());
			
			Incident i4 = new Incident(4, STATE, TITLE, CALLER, 0, OWNER, STATUS_DETAILS, incidentLog);
			assertEquals(TITLE, i4.getTitle());
			i4.setTitle("Jenkins crashed");
			assertEquals("Jenkins crashed", i4.getTitle());
		} catch (IllegalArgumentException e) {
			fail();
		}
	}

	/**
	 * Tests setting the caller of the Incident
	 */
	@Test
	public void testSetCaller() {
		//Invalid caller null
		Incident i = null;
		incidentLog = new ArrayList<String>();
		incidentLog.add("message");
		
		try {
			i = new Incident(1, STATE, TITLE, null, 0, OWNER, STATUS_DETAILS, incidentLog);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(i);
		}
		
		// Invalid caller empty string
		Incident i2 = null;
		try {
			i2 = new Incident(2, STATE, TITLE, "", 0, OWNER, STATUS_DETAILS, incidentLog);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(i2);
		}
		
		//Valid caller
		try {
			Incident i3 = new Incident(3, STATE, TITLE, CALLER, 0, OWNER, STATUS_DETAILS, incidentLog);
			assertEquals(CALLER, i3.getCaller());
			i3.setCaller("john");
			assertEquals("john", i3.getCaller());
			
			Incident i4 = new Incident(4, STATE, TITLE, CALLER, 0, OWNER, STATUS_DETAILS, incidentLog);
			assertEquals(CALLER, i4.getCaller());
			i4.setCaller("bill");
			assertEquals("bill", i4.getCaller());
		} catch (IllegalArgumentException e) {
			fail();
		}
	}

	/**
	 * Tests setting the reopenCount of an Incident
	 */
	@Test
	public void testSetReopenCount() {
		//Invalid reopen count
		Incident i = null;
		incidentLog = new ArrayList<String>();
		incidentLog.add("message");
		try {
			i = new Incident(1, STATE, TITLE, CALLER, -1, OWNER, STATUS_DETAILS, incidentLog);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(i);
		}
		
		
		//Valid reopen count
		try {
			Incident i2 = new Incident(2, STATE, TITLE, CALLER, 0, OWNER, STATUS_DETAILS, incidentLog);
			assertEquals(0, i2.getReopenCount());
			i2.setReopenCount(1);
			assertEquals(1, i2.getReopenCount());
		} catch (IllegalArgumentException e) {
			fail();
		}
	}

	/**
	 * Tests setting the owner of an Incident
	 */
	@Test
	public void testSetOwner() {
		//Invalid owner null 
		Incident i = null;
		incidentLog = new ArrayList<String>();
		incidentLog.add("message");
		try {
			i = new Incident(1, STATE, TITLE, CALLER, 0, null, STATUS_DETAILS, incidentLog);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(i);
		}
		
		//Invalid owner empty string 
		Incident i2 = null;
		try {
			i2 = new Incident(2, STATE, TITLE, CALLER, 0, "", STATUS_DETAILS, incidentLog);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(i2);
		}
		
		//Invalid owner unowned when in progress
		Incident i4 = null;
		try {
			i4 = new Incident(4, Incident.IN_PROGRESS_NAME, TITLE, CALLER, 0, OWNER, STATUS_DETAILS, incidentLog);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(i4);
		}
		
		
		
		//Valid owner
		try {
			Incident i3 = new Incident(3, STATE, TITLE, CALLER, 0, OWNER, STATUS_DETAILS, incidentLog);
			assertEquals(OWNER, i3.getOwner());
			i3.setOwner("john");
			assertEquals("john", i3.getOwner());
			
		} catch (IllegalArgumentException e) {
			fail();
		}
	}



	/**
	 * Tests adding a message to the Incident's log
	 */
	@Test
	public void testAddMessageToIncidentLog() {
		
		Incident i = new Incident("Title", "Caller", "Message");
		assertEquals("- Message" + '\n', i.getIncidentLogMessages());
		
			
	}

	/**
	 * Tests getting the Incident's log messages
	 */
	@Test
	public void testGetIncidentLogMessages() {
		String expected = "- Help" + '\n';
		
		String message1 = "Help";
		Incident i = new Incident("Moodle down", "tstrick2", message1);
		
		
		assertEquals(expected, i.getIncidentLogMessages());
	}
	
	/**
	 * Tests creating a String representation of the Incident
	 */
	@Test
	public void testToString() {
		incidentLog = new ArrayList<String>();
		incidentLog.add("Help");
		Incident i = new Incident(1, STATE, TITLE, CALLER, 0, OWNER, STATUS_DETAILS, incidentLog);
		
		String expected = "* 1,New,Github down,tstrick2,0,Unowned,No Status" + '\n' + "- Help" + '\n';
		
		assertEquals(expected, i.toString());
	}
	
	/**
	 * Tests executing invalid and valid commands on Incidents
	 */
	@Test
	public void testUpdate() {
		
		//Valid command
		Command command = new Command(CommandValue.ASSIGN, "sesmith5", "Message");
		
		Incident i = new Incident("Moodle down", "tstrick2", "Message");
		assertEquals("New", i.getState());
		i.update(command);
		assertEquals("In Progress", i.getState());
		assertEquals("sesmith5", i.getOwner());
		Command command2 = new Command(CommandValue.RESOLVE, "Caller Closed", "Message");
		i.update(command2);
		assertEquals("Resolved", i.getState());
		assertEquals("Caller Closed", i.getStatusDetails());
		assertEquals("sesmith5", i.getOwner());
		Command c = new Command(CommandValue.REOPEN, null, "Valid command");
		i.update(c);
		assertEquals("In Progress", i.getState());
		assertEquals("No Status", i.getStatusDetails());
		assertEquals("sesmith5", i.getOwner());
		assertEquals(1, i.getReopenCount());
		assertEquals("- Message" + '\n' + "- Message" + '\n' + "- Message" + '\n' + "- Valid command" +  '\n', i.getIncidentLogMessages());
		
		
	
		
		//More valid commands
		Command command3 = new Command(CommandValue.CANCEL, "Caller Canceled", "Message");
		incidentLog = new ArrayList<String>();
		incidentLog.add("Help");
		Incident i3 = new Incident(1, "New", "Moodle down", "tstrick2", 0, "Unowned", "No Status", incidentLog);
		i3.update(command3);
		assertEquals(Incident.CANCELED_NAME, i3.getState());
		assertEquals("Caller Canceled", i3.getStatusDetails());
		
		
		Command command4 = new Command(CommandValue.HOLD, "Awaiting Vendor", "Message");
		Incident i4 = new Incident(9, "In Progress", "Jenkins behind firewall", "sesmith5", 0, "cgurley", "No Status", incidentLog);
		i4.update(command4);
		assertEquals(Incident.ON_HOLD_NAME, i4.getState());
		assertEquals("Awaiting Vendor", i4.getStatusDetails());
		
		
		Command command5 = new Command(CommandValue.RESOLVE, Incident.RESOLUTION_PERMANENTLY_SOLVED, "Permanently solved by cgurley");
		Incident i5 = new Incident(10, "In Progress", "Can't push to Github", "joe", 0, "tstrick2", "No Status", incidentLog);
		i5.update(command5);
		assertEquals(Incident.RESOLVED_NAME, i5.getState());
		assertEquals("Permanently Solved", i5.getStatusDetails());
		
		Command command6 = new Command(CommandValue.ASSIGN, "cgurley", "Message");
		Incident i6 = new Incident(10, "In Progress", "Can't push to Github", "joe", 0, "tstrick2", "No Status", incidentLog);
		i6.update(command6);
		assertEquals(Incident.IN_PROGRESS_NAME, i6.getState());
		assertEquals("cgurley", i6.getOwner());
		assertEquals("- Help" + '\n' + "- Message" + '\n', i6.getIncidentLogMessages());
		
		Command command7 = new Command(CommandValue.CANCEL, Incident.CANCELLATION_UNNECESSARY, "Not necessary");
		Incident i7 = new Incident(10, "In Progress", "Can't push to Github", "joe", 0, "tstrick2", "No Status", incidentLog);
		i7.update(command7);
		assertEquals(Incident.CANCELED_NAME, i7.getState());
		assertEquals("Unowned", i7.getOwner());
		assertEquals("Unnecessary", i7.getStatusDetails());
	
		Command command8 = new Command(CommandValue.INVESTIGATE, null, "Message");
		Incident i8 = new Incident(11, "On Hold", "Jenkins issue", "sesmith5", 0, "tstrick2", Incident.HOLD_AWAITING_CALLER, incidentLog);
		i8.update(command8);
		assertEquals(Incident.IN_PROGRESS_NAME, i8.getState());
		assertEquals("No Status", i8.getStatusDetails());
		
		Command command9 = new Command(CommandValue.REOPEN, null, "Needs more work");
		Incident i9 = new Incident(12, "Resolved", "Moodle down", "tstrick2", 0, "cgurley", Incident.RESOLUTION_WORKAROUND, incidentLog);
		i9.update(command9);
		assertEquals(Incident.IN_PROGRESS_NAME, i9.getState());
		assertEquals("No Status", i9.getStatusDetails());
		
		Command command10 = new Command(CommandValue.CANCEL, Incident.CANCELLATION_CALLER_CANCELLED, "Caller canceled");
		Incident i10 = new Incident(13, "Resolved", "Technical problem", "tstrick2", 0, "sesmith5", Incident.RESOLUTION_PERMANENTLY_SOLVED, incidentLog);
		i10.update(command10);
		assertEquals(Incident.CANCELED_NAME, i10.getState());
		assertEquals("Unowned", i10.getOwner());
		assertEquals("Caller Canceled", i10.getStatusDetails());
		
		
		//Invalid command
		Command command11 = new Command(CommandValue.REOPEN, null, "Needs more work");
		Incident i11 = new Incident(1, "New", "Moodle down", "tstrick2", 0, "Unowned", "No Status", incidentLog);
		try {
			i11.update(command11);
			fail();
		} catch (UnsupportedOperationException e) {
			assertEquals("New", i11.getState());
		}
		
		Incident i12 = new Incident(2, "Canceled", "Piazza", "sesmith5", 0, "Unowned", "Not an Incident", incidentLog);
		try {
			i12.update(command9);
			fail();
		} catch (UnsupportedOperationException e) {
			assertEquals(Incident.CANCELED_NAME, i12.getState());
		}
		
		Incident i13 = new Incident(13, "On Hold", "Jenkins issue", "sesmith5", 0, "tstrick2", Incident.HOLD_AWAITING_CALLER, incidentLog);
		try {
			i13.update(command11);
			fail();
		} catch (UnsupportedOperationException e) {
			assertEquals(Incident.ON_HOLD_NAME, i13.getState());
		}
		
		
		
		Incident i14 = new Incident(14, "New", "Moodle", "sesmith5", 0, "Unowned", Incident.NO_STATUS, incidentLog);
		Command command14 = new Command(CommandValue.ASSIGN, "tstrick2", "Assign to tstrick2");
		i14.update(command14);
		assertEquals(Incident.IN_PROGRESS_NAME, i14.getState());
	}
	
	

}
