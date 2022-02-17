package servicewolf.test.command;


import static org.junit.Assert.*;

import org.junit.Test;

import servicewolf.src.command.Command;
import servicewolf.src.command.Command.CommandValue;


/**
 * Tests the Command class
 * @author Tyler Strickland
 *
 */
public class CommandTest {

	/**
	 * Tests constructing a valid Command
	 */
	@Test
	public void testCommandCommandValueStringStringValid() {
		Command c = null;
		try {
			c = new Command(CommandValue.ASSIGN, "Assign", "Github down");
			assertEquals(CommandValue.ASSIGN, c.getCommand());
			assertEquals("Assign", c.getCommandInformation());
			assertEquals("Github down", c.getCommandMessage());
		} catch (IllegalArgumentException e) {
			fail();
		}
	}
	
	/**
	 * .Tests constructing an invalid Command invalid CommandValue
	 */
	@Test
	public void testCommandCommandValueStringStringInvalidValue() {
		Command c = null;
		try {
			c = new Command(null, "Assign", "Github down");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command value", e.getMessage());
			assertNull(c);
		}
	}
	
	/**
	 * .Tests constructing an invalid Command invalid CommandInformation
	 */
	@Test
	public void testCommandCommandValueStringStringInvalidInformation() {
		Command c = null;
		try {
			c = new Command(CommandValue.CANCEL, null, "Github down");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command information", e.getMessage());
			assertNull(c);
		}
		
		c = null;
		try {
			c = new Command(CommandValue.CANCEL, "", "Github down");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command information", e.getMessage());
			assertNull(c);
		}
		
	}
	
	/**
	 * .Tests constructing a new Command invalid CommandMessage
	 */
	@Test
	public void testCommandCommandValueStringStringInvalidMessage() {
		Command c = null;
		try {
			c = new Command(CommandValue.ASSIGN, "Assign", null);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command message", e.getMessage());
			assertNull(c);
		}
		
		c = null;
		try {
			c = new Command(CommandValue.ASSIGN, "Assign", "");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid command message", e.getMessage());
			assertNull(c);
		}
	}
	
	

	

}
