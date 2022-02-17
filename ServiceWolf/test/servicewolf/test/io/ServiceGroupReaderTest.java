package servicewolf.test.io;


import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import servicewolf.src.io.ServiceGroupsReader;
import servicewolf.src.service_group.ServiceGroup;


/**
 * Tests the ServiceGroupReader class
 * @author Tyler Strickland
 */
public class ServiceGroupReaderTest {
	
	
	/**
	 * Tests reading valid and invalid ServiceGroup files
	 */
	@Test
	public void testReadServiceGroupFiles() {
		
		ArrayList<ServiceGroup> serviceGroups = ServiceGroupsReader.readServiceGroupsFile("test-files/incidents2.txt");
		
		assertEquals(1, serviceGroups.size());
		assertEquals("CSC IT", serviceGroups.get(0).getServiceGroupName());

		serviceGroups = ServiceGroupsReader.readServiceGroupsFile("test-files/incidents4.txt");
		assertEquals(0, serviceGroups.size());
		
		
		

	}

}
