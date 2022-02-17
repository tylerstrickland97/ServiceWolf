/**
 * 
 */
package servicewolf.src.incident;

import java.util.ArrayList;

import servicewolf.src.command.Command;
import servicewolf.src.command.Command.CommandValue;



/**
 * Class that represents an Incident in the ServiceWolf program. Incidents are created and ran through an FSM. This class is responsible for constructing Incidents 
 * using one of two constructors. It has standard setters and getters and methods responsible for adding messages to an Incident's message log. It also has an update method that uses the 
 * inner state pattern to update Incidents using the FSM. 
 * @author Tyler Strickland
 *
 */
public class Incident {

	/** Id of the Incident */
	private int incidentId;
	/** Incident's title */
	private String title;
	/** Incident's caller */
	private String caller; 
	/** How many times an Incident has been reopened */
	private int reopenCount;
	/** Incident's caller */
	private String owner;
	/** Incident's status details. Helps keep track of what state the Incident is in  */
	private String statusDetails;
	/** Log of incident messages */
	private ArrayList<String> incidentLog;
	/** Constant used for the name of the New State */
	public static final String NEW_NAME = "New";
	/** Constant used for the name of the In Progress State */
	public static final String IN_PROGRESS_NAME = "In Progress";
	/** Constant used for the name of the On Hold State */
	public static final String ON_HOLD_NAME = "On Hold";
	/** Constant used for the name of the Resolved State */
	public static final String RESOLVED_NAME = "Resolved";
	/** Constant used for the name of the Canceled State */
	public static final String CANCELED_NAME = "Canceled";
	/** Constant used for the awaiting caller status details */
	public static final String HOLD_AWAITING_CALLER = "Awaiting Caller";
	/** Constant used for the awaiting change status details */
	public static final String HOLD_AWAITING_CHANGE = "Awaiting Change";
	/** Constant used for the awaiting vendor status details */
	public static final String HOLD_AWAITING_VENDOR = "Awaiting Vendor";
	/** Constant used for the permanently solved status details */
	public static final String RESOLUTION_PERMANENTLY_SOLVED = "Permanently Solved";
	/** Constant used for the workaround status details */
	public static final String RESOLUTION_WORKAROUND = "Workaround";
	/** Constant used for the caller closed status details */
	public static final String RESOLUTION_CALLER_CLOSED = "Caller Closed";
	/** Constant used for the duplicate status details */
	public static final String CANCELLATION_DUPLICATE = "Duplicate";
	/** Constant used for the unnecessary status details */
	public static final String CANCELLATION_UNNECESSARY = "Unnecessary";
	/** Constant used for the not an incident status details */
	public static final String CANCELLATION_NOT_AN_INCIDENT = "Not an Incident";
	/** Constant used for the caller canceled status details */
	public static final String CANCELLATION_CALLER_CANCELLED = "Caller Canceled";
	/** Constant used for the unowned status details */
	public static final String UNOWNED = "Unowned";
	/** Constant used for the no status status details */
	public static final String NO_STATUS = "No Status";
	/** Counter of how many incidents have been made. Helps ensure that newly created incidents increment the id from the last made incident. */
	private static int counter = 0;
	/** Instance of the new state in the fsm */
	private final IncidentState newState = new NewState();
	/** Instance of the resolved state in the fsm */
	private final IncidentState resolvedState = new ResolvedState();
	/** Instance of the in progress state in the fsm */
	private final IncidentState inProgressState = new InProgressState();
	/** Instance of the canceled state in the fsm */
	private final IncidentState canceledState = new CanceledState();
	/** Instance of the current state in the fsm */
	private IncidentState currentState;
	/** Instance of the on hold state in the fsm */
	private final IncidentState onHoldState = new OnHoldState();
	
	
	/** 
	 * Constructs a new Incident with only a title, message, and caller parameter. Sets the other fields of the Incident to that of a new Incident
	 * @param title title of the Incident
	 * @param message message to add to the Incident's incidentLog
	 * @param caller person who called and made the Incident.
	 */
	public Incident(String title, String caller, String message) {
		if (message == null || "".equals(message)) {
			throw new IllegalArgumentException();
		}
		incidentLog = new ArrayList<String>();
		setId(Incident.counter);
		setTitle(title);
		setCaller(caller);
		setOwner(UNOWNED);
		setStatusDetails(NO_STATUS);
		setReopenCount(0);
		setState(Incident.NEW_NAME);
		addMessageToIncidentLog(message);
		
	}
	
	/**
	 * Constructs a new Incident using all the possible fields of an Incident. 
	 * @param incidentId Incident's id
	 * @param state current state of the Incident.
	 * @param title title of the Incident.
	 * @param caller person who called and made the Incident
	 * @param reopenCount how many times the Incident has been reopened
	 * @param owner person in charge of the Incident
	 * @param statusDetails current details in regards to the Incident state
	 * @param incidentLog log holding all messages related to the Incident
	 * @throws IllegalArgumentException if the incidentLog is null or has a size of 0.
	 */
	public Incident(int incidentId, String state, String title, String caller, int reopenCount, String owner, String statusDetails, ArrayList<String> incidentLog) {
	
		setId(incidentId);
		setTitle(title);
		setCaller(caller);
		setReopenCount(reopenCount);
		setOwner(owner);
		setStatusDetails(statusDetails);
		setState(state);
		this.incidentLog = new ArrayList<String>();
		
		if (incidentLog == null) {
			throw new IllegalArgumentException("Incident cannot be created.");
		}
		else if (incidentLog.size() == 0) {
			throw new IllegalArgumentException("Incident cannot be created.");
		}
		
		for (int i = 0; i < incidentLog.size(); i++) {
			addMessageToIncidentLog(incidentLog.get(i));
		}
	}
	
	/**
	 * Returns the id of the Incident
	 * @return Incident's id
	 */
	public int getId() {
		return incidentId;
	}
	
	/** 
	 * Sets the Incident's id. If the id is greater than the current value of the counter, the counter is updated so that the next Incident created has a greater id than the current one. 
	 * @param incidentId id of the Incident to set
	 * @throws IllegalArgumentException if the id is less than or equal to 0. 
	 */
	public void setId(int incidentId) {
		if (incidentId <= 0) {
			throw new IllegalArgumentException("Incident cannot be created.");
		}
		if (incidentId > Incident.counter) {
			setCounter(incidentId);
		}
		this.incidentId = incidentId;
	}
	
	/** 
	 * Gets the name of the state in which the incident is currently in
	 * @return name of the state that the Incident is currently in
	 */
	public String getState() {
		if (currentState == onHoldState) {
			return currentState.getStateName();
		}
		
		else if (currentState == resolvedState) {
			return currentState.getStateName();
		}
		
		else if (currentState == inProgressState) {
			return currentState.getStateName();
		}
		
		else if (currentState == canceledState) {
			return currentState.getStateName();
		}
		
		else {
			return currentState.getStateName();
		}
		
		
	}
	
	/**
	 *  Checks the other parameters of the Incident and ensures that the given state parameter is appropriate given the other parameters.
	 * @param state name of the state to set
	 * @throws IllegalArgumentException if an Incident field is not appropriate for the given state parameter.
	 */
	public void setState(String state) {
		if (state == null || "".equals(state)) {
			throw new IllegalArgumentException("Incident cannot be creatd.");
		}
		if (!state.equals(NEW_NAME) && !state.equals(IN_PROGRESS_NAME) && !state.equals(RESOLVED_NAME) && !state.equals(ON_HOLD_NAME) && !state.equals(CANCELED_NAME)) {
			throw new IllegalArgumentException("Incident cannot be created.");
		}

		
		if (CANCELED_NAME.equals(state)) {
			currentState = canceledState;
			if (!statusDetails.equals(CANCELLATION_CALLER_CANCELLED) && !statusDetails.equals(CANCELLATION_DUPLICATE) && !statusDetails.equals(CANCELLATION_NOT_AN_INCIDENT) && !statusDetails.equals(CANCELLATION_UNNECESSARY)) {
				throw new IllegalArgumentException("Incident cannot be created");
			}
			
			if (!owner.equals(UNOWNED)) { 
				throw new IllegalArgumentException("Incident cannot be created");
			} 
		}
		
		else if (IN_PROGRESS_NAME.equals(state)) {
			currentState = inProgressState;
			if (!statusDetails.equals(NO_STATUS)) {
				throw new IllegalArgumentException("Incident cannot be created");
			}
			
			if (owner.equals(UNOWNED)) {
				throw new IllegalArgumentException("Incident cannot be created");
			}
			
		}
		else if (RESOLVED_NAME.equals(state)) {
			currentState = resolvedState;
			if (!statusDetails.equals(RESOLUTION_CALLER_CLOSED) && !statusDetails.equals(RESOLUTION_PERMANENTLY_SOLVED) && !statusDetails.equals(RESOLUTION_WORKAROUND)) {
				throw new IllegalArgumentException("Incident cannot be created");
			}
			if (owner.equals(UNOWNED)) {
				throw new IllegalArgumentException("Incident cannot be created");
			}
		}
		else if (ON_HOLD_NAME.equals(state)) {
			currentState = onHoldState;
			if (!statusDetails.equals(HOLD_AWAITING_CALLER) && !statusDetails.equals(HOLD_AWAITING_CHANGE) && !statusDetails.equals(HOLD_AWAITING_VENDOR)) {
				throw new IllegalArgumentException("Incident cannot be created");
			}
			if (owner.equals(UNOWNED)) {
				throw new IllegalArgumentException("Incident cannot be created");
			}
		}
		else {
			currentState = newState;
			if (!statusDetails.equals(NO_STATUS)) {
				throw new IllegalArgumentException("Incident cannot be created");
			}
			if (!owner.equals(UNOWNED)) {
				throw new IllegalArgumentException("Incident cannot be created");
			}
		}
		
		
	}
	
	/**
	 * Returns the title of the Incident
	 * @return title as a String
	 */
	public String getTitle() {
		return title;
	}
	
	/** 
	 * Sets the title of the Incident
	 * @param title title of the Incident to set
	 * @throws IllegalArgumentException if the title is empty or null
	 */
	public void setTitle(String title) {
		if ("".equals(title) || title == null) {
			throw new IllegalArgumentException("Incident cannot be created.");
		}
		this.title = title;
	}
	
	/**
	 * Returns the caller for the Incident
	 * @return caller as a String
	 */
	public String getCaller() {
		return caller;
	}
	
	/** 
	 * Sets the caller for the Incident
	 * @param caller caller of the Incident to set
	 * @throws IllegalArgumentException if the caller is empty or null
	 */
	public void setCaller(String caller) {
		if ("".equals(caller) || caller == null) {
			throw new IllegalArgumentException("Incident cannot be created.");
		}
		this.caller = caller;
	}
	
	/** 
	 * Gets the reopen count for the Incident
	 * @return reopen count integer
	 */
	public int getReopenCount() {
		return reopenCount;
	}
	
	/** 
	 * Sets the number of times the Incident has been reopened. 
	 * @param reopenCount number of times the Incident has been reopened.
	 * @throws IllegalArgumentException if the reopenCount is less than 0.
	 */
	public void setReopenCount(int reopenCount) {
		if (reopenCount < 0) {
			throw new IllegalArgumentException("Incident cannot be created.");
		}
		this.reopenCount = reopenCount;
	}
	
	/**
	 * Gets the owner for the Incident
	 * @return owner of the Incident as a String
	 */
	public String getOwner() {
		return owner;
	}
	
	/**
	 * Sets the owner for the Incident
	 * @param owner owner of the Incident to set
	 * @throws IllegalArgumentException if the owner is empty or null
	 */
	public void setOwner(String owner) {
		
		if (owner == null || "".equals(owner)) {
			throw new IllegalArgumentException("Incident cannot be created.");
		}
		
		this.owner = owner;
		
	}
	
	/**
	 * Returns the status details for the Incident
	 * @return status details as a String
	 */
	public String getStatusDetails() {
		return statusDetails;
	}
	
	/**
	 * Sets the status details for the Incident
	 * @param statusDetails statusDetails of the Incident to set
	 * @throws IllegalArgumentException if the statusDetails are empty or null
	 */
	public void setStatusDetails(String statusDetails) {
		if (statusDetails == null || "".equals(statusDetails)) {
			throw new IllegalArgumentException("Incident cannot be created");
		}
		
		this.statusDetails = statusDetails;
		
	}
	
	/** 
	 * Adds a message to the Incident's message log
	 * @param message message to add to the log
	 * @return int value of the index in the ArrayList the message was added.
	 */
	private int addMessageToIncidentLog(String message) {
		incidentLog.add(message);
		return incidentLog.size() - 1;
	}
	
	/** 
	 * Increments the counter to help keep track of how many Incidents have been created. 
	 */
	public static void incrementCounter() {
		counter += 1;
	}
	
	/** 
	 * Sets the counter in the event that an incident id is greater than than the current counter
	 * @param incidentId id of the incident to set the counter to
	 */
	public static void setCounter(int incidentId) {
		counter = incidentId;
	}
	
	/**
	 * Returns a string representation of the incident's log messages
	 * @return Incident's log messages as a String
	 */
	public String getIncidentLogMessages() {
		String s = "";
		for (int i = 0; i < incidentLog.size(); i++) {
			s += "- " + incidentLog.get(i) + '\n';
		}
		
		return s;
	}
	
	/**
	 * Updates the state of the Incident and drives it through the FSM.
	 * @param command Command that controls the path the Incident takes through the FSM.
	 */
	public void update(Command command) {
		if (currentState == newState) {
			newState.updateState(command);
		}
		else if (currentState == onHoldState) {
			onHoldState.updateState(command);
		}
		else if (currentState == inProgressState) {
			inProgressState.updateState(command);
		}
		else if(currentState == resolvedState) {
			resolvedState.updateState(command);
		}
		else {
			canceledState.updateState(command);
		}
	}
	
	/** 
	 * Returns the Incident as a String
	 * @return String representation of the Incident. 
	 */
	public String toString() {
		return "* " + incidentId + "," + this.getState() + "," + title + "," + caller + "," + reopenCount + "," + owner + "," + statusDetails + '\n' + getIncidentLogMessages();
		
	}
	
	/**
	 * Interface for states in the Incident State Pattern.  All 
	 * concrete incident states must implement the IncidentState interface.
	 * The IncidentState interface should be a private interface of the 
	 * Incident class.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu) 
	 */
	private interface IncidentState {
		
		/**
		 * Update the Incident based on the given Command.
		 * An UnsupportedOperationException is thrown if the Command
		 * is not a valid action for the given state.  
		 * @param command Command describing the action that will update the Incident's
		 * state.
		 * @throws UnsupportedOperationException if the Command is not a valid action
		 * for the given state.
		 */
		void updateState(Command command);
		
		/**
		 * Returns the name of the current state as a String.
		 * @return the name of the current state as a String.
		 */
		String getStateName();

	}
	
	/** 
	 * Represents the New state of an Incident. Controls how the state of an Incident changes depending on the Command given.
	 * @author Tyler Strickland
	 *
	 */
	private class NewState implements IncidentState {
		
		/** 
		 * Constructs a new NewState
		 */
		private NewState() {
			
		}
		
		/** 
		 * Updates the currentState of the Incident depending on what the command value and command information is
		 * @param command command being given to the Incident
		 * @throws UnSupportedOperationExcepion if the command is not appropriate given the current state of the incident
		 */
		public void updateState(Command command) {
			if (command.getCommand() != CommandValue.ASSIGN && command.getCommand() != CommandValue.CANCEL) {
				throw new UnsupportedOperationException();
			}
			else {
				if (command.getCommand() == CommandValue.ASSIGN) {
					currentState = inProgressState;
					setOwner(command.getCommandInformation());
					addMessageToIncidentLog(command.getCommandMessage());
				}
				else if (command.getCommand() == CommandValue.CANCEL) {
					currentState = canceledState;
					setStatusDetails(command.getCommandInformation());
					addMessageToIncidentLog(command.getCommandMessage());
				}
			}
		}
		
		/**
		 * Returns the name of the state
		 * @return name of the state as a String
		 */
		public String getStateName() {
			return NEW_NAME;
		}
			
	}
	
	/**
	 * Represents the In Progress state of an Incident. Controls how the state changes depending on the command given. 
	 * @author Tyler Strickland
	 *
	 */
	private class InProgressState implements IncidentState {
		
		/**
		 * Creates a new InProgressState
		 */
		private InProgressState() {
			
		}
		
		/** 
		 * Updates the currentState of the Incident depending on what the command value and command information is
		 * @param command command being given to the Incident
		 * @throws UnSupportedOperationExcepion if the command is not appropriate given the current state of the incident
		 */
		public void updateState(Command command) {
			//hold, resolve, assign, cancel
			if (command.getCommand() != CommandValue.HOLD && command.getCommand() != CommandValue.RESOLVE && command.getCommand() != CommandValue.ASSIGN && command.getCommand() != CommandValue.CANCEL) {
				throw new UnsupportedOperationException();
			}
			
			else {
				if (command.getCommand() == CommandValue.HOLD) {
					currentState = onHoldState;
					setStatusDetails(command.getCommandInformation());
					setState(ON_HOLD_NAME);
					addMessageToIncidentLog(command.getCommandMessage());
				}
				else if (command.getCommand() == CommandValue.RESOLVE) {
					currentState = resolvedState;
					setStatusDetails(command.getCommandInformation());
					addMessageToIncidentLog(command.getCommandMessage());
				}
				else if (command.getCommand() == CommandValue.ASSIGN) {
					currentState = inProgressState;
					setOwner(command.getCommandInformation());
					addMessageToIncidentLog(command.getCommandMessage());
				}
				else if (command.getCommand() == CommandValue.CANCEL) {
					currentState = canceledState;
					setStatusDetails(command.getCommandInformation());
					setOwner(UNOWNED);
					addMessageToIncidentLog(command.getCommandMessage());
				}
			}
		}
		
		/**
		 * Returns the name of the state
		 * @return name of the State as a String
		 */
		public String getStateName() {
			return IN_PROGRESS_NAME;
		}
			
	}
	
	/**
	 * Represents the Canceled state of an Incident. Controls how the state of an Incident changes when in the Canceled state depending on the command given. 
	 * @author Tyler Strickland
	 *
	 */
	private class CanceledState implements IncidentState {
		
		/**
		 * Creates a new CanceledState
		 */
		private CanceledState() {
			
		}
		
		/** 
		 * Updates the currentState of the Incident depending on what the command value and command information is
		 * @param command command being given to the Incident
		 * @throws UnSupportedOperationExcepion if the command is not appropriate given the current state of the incident
		 */
		public void updateState(Command command) {
			currentState = canceledState;
			throw new UnsupportedOperationException();
		}
		
		/**
		 * Returns the name of the state
		 * @return name of the state as a String
		 */
		public String getStateName() {
			return CANCELED_NAME;
		}
			
	}
	
	/**
	 * Represents the On Hold state for an Incident. Controls how the state of an Incident changes when in the On Hold state depending on the command given. 
	 * @author Tyler Strickland
	 *
	 */
	private class OnHoldState implements IncidentState {
		
		/**
		 * Creates a new OnHoldState
		 */
		private OnHoldState() {
			
		}
		
		/** 
		 * Updates the currentState of the Incident depending on what the command value and command information is
		 * @param command command being given to the Incident
		 * @throws UnSupportedOperationExcepion if the command is not appropriate given the current state of the incident
		 */
		public void updateState(Command command) {
			if (command.getCommand() == CommandValue.INVESTIGATE) {
				currentState = inProgressState;
				setStatusDetails(NO_STATUS);
				addMessageToIncidentLog(command.getCommandMessage());
			}
			else {
				throw new UnsupportedOperationException();
			}
		}
		
		/**
		 * Returns the name of the state
		 * @return name of the State as a String
		 */
		public String getStateName() {
			return ON_HOLD_NAME;
		}
			
	}
	
	/** 
	 * Represents the Resolved State for an Incident. Controls how the state of an Incident changes when in the Resolved state depending on the command given. 
	 * @author Tyler Strickland
	 *
	 */
	private class ResolvedState implements IncidentState {
	
		/**
		 * Creates a new Resolved State
		 */
		private ResolvedState() {
			
		}
	
		/** 
		 * Updates the currentState of the Incident depending on what the command value and command information is
		 * @param command command being given to the Incident
		 * @throws UnSupportedOperationExcepion if the command is not appropriate given the current state of the incident
		 */
		public void updateState(Command command) {
			if (command.getCommand() == CommandValue.CANCEL) {
				currentState = canceledState;
				setStatusDetails(command.getCommandInformation());
				setOwner(UNOWNED);
				addMessageToIncidentLog(command.getCommandMessage());
			}
			else if(command.getCommand() == CommandValue.REOPEN) {
				currentState = inProgressState;
				setStatusDetails(NO_STATUS);
				setReopenCount(reopenCount + 1);
				addMessageToIncidentLog(command.getCommandMessage());
			}
			else {
				throw new UnsupportedOperationException();
			}
		}
	
		/**
		 * Returns the name of the state
		 * @return name of the state as a String
		 */
		public String getStateName() {
			return RESOLVED_NAME;
		}
		
	}
	
	
}
