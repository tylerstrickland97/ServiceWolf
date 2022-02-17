/**
 * 
 */
package servicewolf.src.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

import servicewolf.src.incident.Incident;
import servicewolf.src.service_group.ServiceGroup;


/**
 * Writes ServiceGroups along with their Incidents and their log messages to a file so that they can be saved and modified. 
 * @author Tyler Strickland
 *
 */
public class ServiceGroupWriter {

	/**
	 * Writes ServiceGroups to a file
	 * @param file file to write the ServiceGroups to 
	 * @param serviceGroups ArrayList of ServiceGroups that are to be in the file
	 * @throws FileNotFoundException if the file is unable to be written to.
	 */
	public static void writeServiceGroupsToFile(String file, ArrayList<ServiceGroup> serviceGroups) {
		try {
			PrintStream fileWriter = new PrintStream(new File(file)); 
			ArrayList<Incident> incidents = null;
			for (int i = 0; i < serviceGroups.size(); i++) {
				fileWriter.println("# " + serviceGroups.get(i).getServiceGroupName());
				incidents = serviceGroups.get(i).getIncidents();
				for (int j = 0; j < incidents.size(); j++) {
					fileWriter.print(incidents.get(j).toString());
				}
			}
			fileWriter.close();
		} catch (Exception e) {
			throw new IllegalArgumentException("Unable to write to file " + file);
		}
	}
}
