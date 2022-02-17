
/**
 * 
 */
package servicewolf.src.manager;

import java.util.ArrayList;

import servicewolf.src.command.Command;
import servicewolf.src.incident.Incident;
import servicewolf.src.io.ServiceGroupWriter;
import servicewolf.src.io.ServiceGroupsReader;
import servicewolf.src.service_group.ServiceGroup;



/**
 * Manager class of ServiceWolf. Responsible for controlling both ServiceGroups and Incidents and is the main driver for the program. It is responsible for 
 * loading and saving files, creating arrays of Incidents for display, adding incidents to ServiceGroups, returning Incidents, and executing commands on Incidents
 * @author Tyler Strickland
 *
 */
public class ServiceWolfManager {
	
	/** ArrayList of ServiceGroups */
	private ArrayList<ServiceGroup> serviceGroups;
	/** The current ServiceGroup which is the one that is currently having actions performed on */
	private ServiceGroup currentServiceGroup;
	/** Singleton of ServiceWolfManager */
	private static ServiceWolfManager manager;

	/** 
	 * Constructs a new ServiceWolfManager and creates a new ArrayList of ServiceGroups
	 */
	private ServiceWolfManager() {
		serviceGroups = new ArrayList<ServiceGroup>();
	}
	
	/**
	 * Creates a Singleton of the manager and makes sure there is always one single instance of the ServiceWolfManager
	 * @return instance of the ServiceWolfManager
	 */
	public static ServiceWolfManager getInstance() {
		if (manager == null) {
			manager = new ServiceWolfManager(); 
		}
		return manager;
	}
	
	/**
	 * Saves the ArrayList of ServiceGroups to a file using the ServiceGroupWriter class
	 * @param file file to write the ServiceGroups to 
	 */
	public void saveToFile(String file) {
		if (currentServiceGroup == null || currentServiceGroup.getIncidents().size() == 0) {
			throw new IllegalArgumentException("Unable to save file " + file);
		}

		try {
			ServiceGroupWriter.writeServiceGroupsToFile(file, serviceGroups);
		} catch (IllegalArgumentException e)  {
			throw new IllegalArgumentException("Unable to save file " + file);
		}
	}
	
	/**
	 * Loads ServiceGroups from a file
	 * @param file file to load the ServiceGroups from
	 */
	public void loadFromFile(String file) {
	
		ArrayList<ServiceGroup> loadedServiceGroups = new ArrayList<ServiceGroup>();
		try {
			loadedServiceGroups = ServiceGroupsReader.readServiceGroupsFile(file);
			currentServiceGroup = loadedServiceGroups.get(0);
			for (int i = 0; i < loadedServiceGroups.size(); i++) {
				addServiceGroupToListByName(loadedServiceGroups.get(i));
			}
			currentServiceGroup.setIncidentCounter();
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Unable to read file " + file);
		}
	}
	 
	/**
	 * Returns the Incidents in the currentServiceGroup as a 2d array. Used for display in the ServiceWolf program
	 * @return Incidents of the currentServiceGroup as a 2d array
	 */
	public String[][] getIncidentsAsArray() {
		if (currentServiceGroup == null) {
			return null;
		}
		else {
			String[][] incidentsArray = new String[currentServiceGroup.getIncidents().size()][4];
			
			for (int i = 0; i < currentServiceGroup.getIncidents().size(); i++) {
				for (int j = 0; j < 4; j++) {
					if (j == 0) {
						int k = currentServiceGroup.getIncidents().get(i).getId();
						String id = Integer.toString(k);
						incidentsArray[i][j] = id;
					}
					else if (j == 1) {
						incidentsArray[i][j] = currentServiceGroup.getIncidents().get(i).getState();
					}
					else if (j == 2) {
						incidentsArray[i][j] = currentServiceGroup.getIncidents().get(i).getTitle();
					}
					else {
						incidentsArray[i][j] = currentServiceGroup.getIncidents().get(i).getStatusDetails();
					}
				}
			}
			return incidentsArray;
		}
	}
	
	/**
	 * Returns the Incident with the given id of the currentServiceGroup
	 * @param id id of the Incident to get
	 * @return null if the currentServiceGroup is null. If not null, then the Incident of the given id. 
	 */
	public Incident getIncidentById(int id) {
		if (currentServiceGroup == null) {
			return null;
		}
		else {
			for (int i = 0; i < currentServiceGroup.getIncidents().size(); i++) {
				if (id == currentServiceGroup.getIncidents().get(i).getId()) {
					return currentServiceGroup.getIncidents().get(i);
				}
			}
			return null;
		}
	}
	
	/**
	 * Executes the command on the currentServiceGroups Incident of the given if
	 * @param id id of the incident to perform the command on
	 * @param command command to execute on the Incident
	 */
	public void executeCommand(int id, Command command) {
		if (currentServiceGroup != null) {
			currentServiceGroup.executeCommand(id, command);
		}
	}
	
	/**
	 * Deletes the currentServiceGroups Incident of the given id
	 * @param id id of the Incident to delete
	 */
	public void deleteIncidentById(int id) {
		if (currentServiceGroup != null) {
			for (int i = 0; i < currentServiceGroup.getIncidents().size(); i++) {
				if (id == currentServiceGroup.getIncidents().get(i).getId()) {
					currentServiceGroup.getIncidents().remove(i);
				}
			}
		}
	}
	
	
	/**
	 * Adds an Incident to the currentServiceGroup's ArrayList of Incidents
	 * @param title title of the new Incident
	 * @param caller caller of the new Incident
	 * @param message message of the new Incident
	 */
	public void addIncidentToServiceGroup(String title, String caller, String message) {
		if (currentServiceGroup != null) {
			Incident i = new Incident(title, caller, message);
			
			currentServiceGroup.addIncident(i);
		}
	}

	/**
	 * Loads the ServiceGroup of the given name and makes that ServiceGroup the currentServiceGroup
	 * @param serviceGroupName name of the ServiceGroup to make the currentServiceGroup
	 */
	public void loadServiceGroup(String serviceGroupName) {
		boolean exists = false;
		for (int i = 0; i < serviceGroups.size(); i++) {
			if (serviceGroups.get(i).getServiceGroupName().compareToIgnoreCase(serviceGroupName) == 0) {
				exists = true;
			}
		}
		if (!exists) {
			throw new IllegalArgumentException("Invalid service group name");
		}
		else {
			for (int i = 0; i < serviceGroups.size(); i++) {
				if (serviceGroups.get(i).getServiceGroupName().compareToIgnoreCase(serviceGroupName) == 0) {
					currentServiceGroup = serviceGroups.get(i);
					currentServiceGroup.setIncidentCounter();
				}
			}
		}
	}
	
	/**
	 * Returns the name of the currentServiceGroup
	 * @return name of the currentServiceGroup if currentServiceGroup is not null and returns null if the currentServiceGroup is null
	 */
	public String getServiceGroupName() {
		if (currentServiceGroup == null) {
			return null;
		}
		else {
			return currentServiceGroup.getServiceGroupName();
		}
	}
	
	/**
	 * Returns a String array of ServiceGroups in the order in which they were added
	 * @return String array of ServiceGroups
	 */
	public String[] getServiceGroupList() {
		String[] serviceGroupList = new String[serviceGroups.size()];
		
		for (int i = 0; i < serviceGroups.size(); i++) {
			serviceGroupList[i] = serviceGroups.get(i).getServiceGroupName();
		}
		
		return serviceGroupList;
	}
	
	/**
	 * Clears the ArrayList of ServiceGroups and makes the currentServiceGroup null
	 */
	public void clearServiceGroups() {
		serviceGroups = new ArrayList<ServiceGroup>();
		
		currentServiceGroup = null;
	}
	
	/**
	 * Edits the name of the currentServiceGroup
	 * @param updateName name to update for the ServiceGroup
	 * @throws IllegalArgumentException if the updateName is empty or null
	 */
	public void editServiceGroup(String updateName) {
		if (currentServiceGroup == null) {
			throw new IllegalArgumentException("No service group selected.");
		}
		else {
			if (updateName == null || "".equals(updateName)) {
				throw new IllegalArgumentException("Invalid service group name.");
			}
			checkDuplicateServiceName(updateName);
			deleteServiceGroup();
			addServiceGroup(updateName);
			
			
		}
	}
	
	/**
	 * Adds ServiceGroup to the ArrayList of ServiceGroups in alphabetical order and sorts the list in alphabetical order
	 * @param s ServiceGroup to add to the list
	 */
	private void addServiceGroupToListByName(ServiceGroup s) {
		checkDuplicateServiceName(s.getServiceGroupName());
		
		serviceGroups.add(s);
		
		for (int i = 0; i < serviceGroups.size(); i++) {
			for (int j = serviceGroups.size() - 1; j > i; j--) {
				if (serviceGroups.get(i).getServiceGroupName().compareTo(serviceGroups.get(j).getServiceGroupName()) > 0) {
					ServiceGroup temp = serviceGroups.get(i);
					serviceGroups.set(i, serviceGroups.get(j));
					serviceGroups.set(j, temp);
				}
			}
		}
	}
	
	/**
	 * Adds a ServiceGroup to the list of ServiceGroups using the addServiceGroupToListByName method to ensure that is added in the correct order. Uses the checkDuplicateServiceName to make sure it is not a duplicate ServiceGroup
	 * @param serviceGroupName name of the new ServiceGroup to add
	 * @throws IllegalArgumentException if the name of the ServiceGroup is empty or null
	 */
	public void addServiceGroup(String serviceGroupName) {
		if (serviceGroupName == null || "".equals(serviceGroupName) ) {
			throw new IllegalArgumentException("Invalid service group name.");
		}
		
		else {
			checkDuplicateServiceName(serviceGroupName);
			ServiceGroup s = new ServiceGroup(serviceGroupName);
			addServiceGroupToListByName(s);
			loadServiceGroup(s.getServiceGroupName());
		}
		

		
	}
	
	/**
	 * Checks if the parameter name is already the name of a ServiceGroup in the list of ServiceGroups
	 * @param name name to check if duplicate
	 * @throws IllegalArgumentException if the name is a duplicate name of an existing ServiceGroup 
	 */ 
	private void checkDuplicateServiceName(String name) {
		
		for (int i = 0; i < serviceGroups.size(); i++) {
			if (serviceGroups.get(i).getServiceGroupName().compareToIgnoreCase(name) == 0) {
				throw new IllegalArgumentException("Invalid service group name.");
			}
		}
	}
	
	/**
	 * Deletes the currentServiceGroup and sets the currentServcieGroup to be the first in the list of ServiceGroups if the list is not empty, and sets the currentServiceGroup to null if the list is empty 
	 * @throws IllegalArgumentException if the currentServiceGroup is null
	 */
	public void deleteServiceGroup() {
		
		if (currentServiceGroup == null) {
			throw new IllegalArgumentException("No service group selected.");
		}
		
		else {
			for (int i = 0; i < serviceGroups.size(); i++) {
				if (serviceGroups.get(i).getServiceGroupName().compareToIgnoreCase(currentServiceGroup.getServiceGroupName()) == 0) {
					serviceGroups.remove(i);
				}
			}
			
			if (serviceGroups.size() == 0) {
				currentServiceGroup = null;
			}
			else {
				currentServiceGroup = serviceGroups.get(0);
			}
		}
	}
	
	/**
	 * Resets the manager by making it null
	 */
	public void resetManager() {
		manager = null;
	}

		
}
