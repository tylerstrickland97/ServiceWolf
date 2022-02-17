/**
 * 
 */
package servicewolf.src.service_group;


import java.util.ArrayList;

import servicewolf.src.command.Command;
import servicewolf.src.incident.Incident;



/**
 * This class represents a ServiceGroup in ServiceWolf. It is responsible for constructing ServiceGroups, returning their names, adding incidents to ServiceGroups, 
 * returning those incidents, and executing commands. 
 * @author Tyler Strickland
 *
 */
public class ServiceGroup {
	
	/** ArrayList of incidents that a ServiceGroup will have */
	private ArrayList<Incident> incidents;
	/** Name of the ServiceGroup */
	private String serviceGroupName;
	
	/** 
	 * Constructs a new ServiceGroup and creates a new ArrayList of incidents for that ServiceGroup
	 * @param serviceGroupName name of the ServiceGroup
	 */
	public ServiceGroup(String serviceGroupName) {
		setServiceGroupName(serviceGroupName);
		incidents = new ArrayList<Incident>();
		
	}
	
	/**
	 * Sets the incident counter for the ServiceGroup
	 */
	public void setIncidentCounter() {
		if (incidents.size() == 0) {
			Incident.setCounter(1);
		}
		else {
			int max = 0;
			for (int i = 0; i < incidents.size(); i++) {
				if (incidents.get(i).getId() > max) {
					max = incidents.get(i).getId();
				}
			}
			Incident.setCounter(max + 1);
			
		}
	}
	
	/**
	 * Sets the name of the ServiceGroup
	 * @param serviceGroupName name of the ServiceGroup to set
	 * @throws IllegalArgumentException if the service group name is empty or null
	 */
	public void setServiceGroupName(String serviceGroupName) {
		if ("".equals(serviceGroupName) || serviceGroupName == null) {
			throw new IllegalArgumentException("Invalid service group name.");
		}
		this.serviceGroupName = serviceGroupName;
	}
	
	/**
	 * Returns the name of the ServiceGroup
	 * @return ServiceGroup name String
	 */
	public String getServiceGroupName() {
		return serviceGroupName;
	}
	
	/**
	 * Adds an Incident to the ServiceGroups ArrayList of incidents. 
	 * @param incident Incident to add to the ServiceGroup
	 * @throws IllegalArgumentException if the Incident id matches the id of an Incident that is already a part of the ServiceGroup
	 */
	public void addIncident(Incident incident) {
		for (int i = 0; i < incidents.size(); i++) {
			if (incident.getId() == incidents.get(i).getId()) {
				throw new IllegalArgumentException("Incident cannot be created.");
			}
		}
		incidents.add(incident);
		setIncidentCounter();
		
		for (int i = 0; i < incidents.size(); i++) {
			for (int j = incidents.size() - 1; j > i; j--) {
				if (incidents.get(i).getId() > incidents.get(j).getId()) {
					Incident temp = incidents.get(i);
					incidents.set(i, incidents.get(j));
					incidents.set(j, temp);
				}
			}
		}
	
	}
	
	/**
	 * Returns the ArrayList of Incidents
	 * @return the ServiceGroup's incidents as an ArrayList
	 */
	public ArrayList<Incident> getIncidents() {
		return incidents;
	}
	
	/**
	 * Goes through the ServiceGroups ArrayList of Incidents and returns the Incident with the desired id
	 * @param id id of the Incident to return
	 * @return the Incident of the given id, or null if the ServiceGroup does not have the Incident of the given id
	 */
	public Incident getIncidentById(int id) {
		
		for (int i = 0; i < incidents.size(); i++) {
			if (incidents.get(i).getId() == id) {
				return incidents.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Executes a command on an Incident in the ArrayList of Incidents
	 * @param id id of the Incident to perform the Command on
	 * @param command command to execute on the Incident
	 */
	public void executeCommand(int id, Command command) {
		
		for (int i = 0; i < incidents.size(); i++) {
			if (incidents.get(i).getId() == id) {
				incidents.get(i).update(command);
			}
		}
	}
	
	/**
	 * Deletes an Incident in a ServiceGroup
	 * @param id id of the ServiceGroup to delete
	 */
	public void deleteIncidentById(int id) {
		for (int i = 0; i < incidents.size(); i++) {
			if (incidents.get(i).getId() == id) {
				incidents.remove(i);
			}
		}
	}
	
	
}