package servicewolf.test.io;


import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.Test;

import servicewolf.src.incident.Incident;
import servicewolf.src.io.ServiceGroupWriter;
import servicewolf.src.service_group.ServiceGroup;


/**
 * Tests the ServiceGroupWriter class
 * @author Tyler Strickland
 *
 */
public class ServiceGroupWriterTest {

	/**
	 * Tests writing ServiceGroups and their Incidents to files
	 */
	@Test
	public void testWriteServiceGroupsToFile() {
		ArrayList<ServiceGroup> serviceGroups = new ArrayList<ServiceGroup>();
		ServiceGroup s1 = new ServiceGroup("CSC IT");
		ArrayList<String> log1 = new ArrayList<String>();
		log1.add("When I go to wolfware.ncsu.edu, I get a 500 error");
		Incident i1 = new Incident(3, "New", "Moodle down", "sesmith5", 0, "Unowned", "No Status", log1);
		ArrayList<String> log2 = new ArrayList<String>();
		log2.add("Jenkins requires VPN to access.  Please open to general access.");
		log2.add("Assigned to C. Gurley");
		Incident i2 = new Incident(9, "In Progress", "Jenkins behind firewall", "sesmith5", 0, "cgurley", "No Status", log2);
		ArrayList<String> log3 = new ArrayList<String>();
		log3.add("Please set up Jenkins VMs for Spring 2021 semester.");
		log3.add("Assigned to C. Gurley");
		log3.add("Set up test VM. Awaiting verification from caller.");
		log3.add("VM works great, please deploy the rest.");
		log3.add("VMs deployed. Marked resolved.");
		log3.add("One of the VMs has the wrong version of Checkstyle installed.");
		log3.add("Updated version of Checkstyle.");
		Incident i3 = new Incident(4, "Resolved", "Set up Jenkins VMs", "sesmith5", 1, "cgurley", "Permanently Solved", log3);
		ArrayList<String> log4 = new ArrayList<String>();
		log4.add("Set up piazza for Spring 2021");
		log4.add("Canceled; not an NC State IT service");
		Incident i4 = new Incident(2, "Canceled", "Piazza", "sesmith5", 0, "Unowned", "Not an Incident", log4);
		s1.addIncident(i4);
		s1.addIncident(i1);
		s1.addIncident(i3);
		s1.addIncident(i2);
		assertEquals(4, s1.getIncidents().size());
		serviceGroups.add(s1);

		
		try {
			ServiceGroupWriter.writeServiceGroupsToFile("test-files/actual_incidents2.txt", serviceGroups);
		} catch (IllegalArgumentException e) {
			fail("Cannot write ServiceGroups to file");
		}
		
		checkFiles("test-files/expected_incidents2.txt", "test-files/actual_incidents2.txt");
		
	
	}
	
	/**
	 * Helper method to compare two files for the same contents
	 * @param expFile expected output
	 * @param actFile actual output
	 */
	private void checkFiles(String expFile, String actFile) {
		try (Scanner expScanner = new Scanner(new FileInputStream(expFile));
			 Scanner actScanner = new Scanner(new FileInputStream(actFile));) {
			
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
