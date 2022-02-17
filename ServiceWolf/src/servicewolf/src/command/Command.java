
   
package servicewolf.src.command;

/** 
 * Class that represents a Command object in ServiceWolf. It drives the fsm inside of ServiceWolf program by giving certain orders for each 
 * incident to follow, driving it through a state pattern. This class constructs Command objects and returns the parameters. 
 * @author Tyler Strickland
 *
 */
public class Command {
	
	/** Information related to the command. May contain status details that help determine what state the incident will be in. */
	private String commandInformation;
	/** Message that goes with the command that will be added to the incident log messages once executed */
	private String commandMessage;
	/** Value of the command. Determines what action to put the incident through */
	private CommandValue commandValue;
	
	/** 
	 * Constructs a new Command
	 * @param commandValue value of the Command. Values are in the enumeration
	 * @param commandInformation information related to the Command. Can be used to change the fields of Incidents when being ran through the FSM
	 * @param commandMessage message in the Command that will be added to the Incidents log messages
	 * @throws IllegalArgumentException if any of the parameters are invalid for the specific command value.
	 */
	public Command(CommandValue commandValue, String commandInformation, String commandMessage) {
		
		if (commandValue == null) {
			throw new IllegalArgumentException("Invalid command value");
		}
		
		if (commandMessage == null || "".equals(commandMessage)) {
			throw new IllegalArgumentException("Invalid command message");
		}
		
		if ((commandValue == Command.CommandValue.ASSIGN || commandValue == Command.CommandValue.HOLD || commandValue == Command.CommandValue.RESOLVE || commandValue == Command.CommandValue.CANCEL) && (commandInformation == null || "".equals(commandInformation))) {
			throw new IllegalArgumentException("Invalid command information");
			
		}
		
		if ((commandValue == Command.CommandValue.INVESTIGATE || commandValue == Command.CommandValue.REOPEN) && commandInformation != null) {
			throw new IllegalArgumentException("Invalid command information");
		}
		
		this.commandValue = commandValue;
		this.commandInformation = commandInformation;
		this.commandMessage = commandMessage;
	}
	
	/**
	 * Returns the command value for the Command
	 * @return CommandValue of the Command
	 */
	public CommandValue getCommand() {
		return commandValue;
	}
	
	/** 
	 * Returns the information related to the command
	 * @return information related to the command as a String
	 */
	public String getCommandInformation() {
		return commandInformation;
	}
	
	/** 
	 * Returns the message that is in the Command
	 * @return command message as a String
	 */
	public String getCommandMessage() {
		return commandMessage;
	}
	
	/** 
	 * Enumeration that holds a small amount of values, each a value that a Command might have. 
	 * @author Tyler Strickland
	 *
	 */
	public enum CommandValue { ASSIGN, HOLD, INVESTIGATE, RESOLVE, REOPEN, CANCEL }
}
