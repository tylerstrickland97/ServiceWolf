/**
 * 
 */
package servicewolf.src.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import servicewolf.src.incident.Incident;
import servicewolf.src.service_group.ServiceGroup;


/**
 * Reads a file and creates a new ArrayList of ServiceGroups from the given information. Breaks a file into multiple pieces and creates new Incidents and incident logs for each service g
 * group using the information in the file. 
 * @author Tyler Strickland
 *
 */
public class ServiceGroupsReader {

	/**
	 * Creates an ArrayList of ServiceGroups by processing a file and using the information within the file to construct new ServiceGroups that contain incidents and incident logs. 
	 * @param file to read for processing
	 * @return ArrayList of ServiceGroup objects 
	 * @throws FileNotFoundException if the file cannot be read. 
	 */
	public static ArrayList<ServiceGroup> readServiceGroupsFile(String file) {
		
		String s = "";
		String groups = "";
		ArrayList<ServiceGroup> serviceGroups = new ArrayList<ServiceGroup>();
		try {
			Scanner fileScanner = new Scanner(new FileInputStream(file));
			while (fileScanner.hasNext()) {
				s += fileScanner.nextLine() + '\n';
			}
			Scanner groupScanner = new Scanner(s);
			groupScanner.useDelimiter("\\r?\\n?[#]");
			try {
			while (groupScanner.hasNext() ) {
				ServiceGroup group = processServiceGroup(groupScanner.next());
				if (group == null) {
					groupScanner.close();
					serviceGroups = new ArrayList<ServiceGroup>();
					return serviceGroups;
				}
				else {
					serviceGroups.add(group);
				}
			}
			groupScanner.close();
			} catch (IllegalArgumentException e) {
				groupScanner.close();
				return null;
			}
			System.out.print(groups);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Cannot load file " + file);
		}
		return serviceGroups;
	}
	
	/**
	 * Processes a line of the file and returns a ServiceGroup given the information in the line. 
	 * @param line line to process for ServiceGroup information. 
	 * @return ServiceGroup using information passed in the line
	 * @throws IllegalArgumentException in the ServiceGroup cannot be created
	 */
	private static ServiceGroup processServiceGroup(String line) {
		
		Scanner scan = new Scanner(line);
		String name = scan.nextLine();
		name = name.trim();
		ServiceGroup s = new ServiceGroup(name);
		scan.useDelimiter("\\r?\\n?[*]");
		try {
			while (scan.hasNext()) {
				Incident i = processIncident(scan.next());
				s.addIncident(i);
			}
		} catch (IllegalArgumentException e) {
			scan.close();
			return null;
		}
		scan.close();
		return s;
	}
	
	/** 
	 * Creates a new Incident using information from a line that is passed in
	 * @param line line to create the Incident from using the information in the line
	 * @return an Incident with data from the line parameter. 
	 * @throws IllegalArgumentException if the Incident cannot be created. 
	 */
	private static Incident processIncident(String line) {
		Scanner scan = new Scanner(line);
		String incident = scan.nextLine();
		scan.close();
		incident = incident.trim();

		
		Scanner incidentScanner = new Scanner(incident);
		incidentScanner.useDelimiter(",");
		int id = 0;
		String state = "";
		String title = "";
		String caller = "";
		int reopenCount = 0;
		String owner = "";
		String statusDetails = "";
		ArrayList<String> incidentLog = new ArrayList<String>();
		 
		try {
			id = incidentScanner.nextInt();
			state = incidentScanner.next();
			state = state.trim();
			title = incidentScanner.next();
			title = title.trim();
			caller = incidentScanner.next();
			caller = caller.trim();
			reopenCount = incidentScanner.nextInt();
			owner = incidentScanner.next();
			owner = owner.trim();
			statusDetails = incidentScanner.next();
			statusDetails = statusDetails.trim();
			incidentScanner.close();
		} catch (NoSuchElementException e) {
			incidentScanner.close();
			throw new IllegalArgumentException("Incident cannot be created");
		}
		
		Scanner logScanner = new Scanner(line);
		logScanner.nextLine();
		String message = "";
		logScanner.useDelimiter("\\r?\\n?[-]");

		while (logScanner.hasNext()) {
			message = logScanner.next();
			message = message.trim();
			incidentLog.add(message);
		}
		logScanner.close();

		System.out.println();
		Incident i = new Incident(id, state, title, caller, reopenCount, owner, statusDetails, incidentLog);
		return i;
		
	}
}
