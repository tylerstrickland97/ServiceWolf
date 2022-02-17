package servicewolf.src.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import servicewolf.src.command.Command;
import servicewolf.src.incident.Incident;
import servicewolf.src.manager.ServiceWolfManager;


/**
 * Container for the ServiceWolf that has the menu options for new incident 
 * files, loading existing files, saving files and quitting.
 * Depending on user actions, other JPanels are loaded for the
 * different ways users interact with the UI.
 * 
 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
 */
public class ServiceWolfGUI extends JFrame implements ActionListener {
	
	/** ID number used for object serialization. */
	private static final long serialVersionUID = 1L;
	/** Title for top of GUI. */
	private static final String APP_TITLE = "Service Wolf";
	/** Text for the File Menu. */
	private static final String FILE_MENU_TITLE = "File";
	/** Text for the Load menu item. */
	private static final String LOAD_TITLE = "Load";
	/** Text for the Save menu item. */
	private static final String SAVE_TITLE = "Save";
	/** Text for the Save menu item. */
	private static final String CLEAR_TITLE = "Clear";
	/** Text for the Quit menu item. */
	private static final String QUIT_TITLE = "Quit";
	/** Menu bar for the GUI that contains Menus. */
	private JMenuBar menuBar;
	/** Menu for the GUI. */
	private JMenu menu;
	/** Menu item for loading a file containing Service Groups and their Incidents. */
	private JMenuItem itemLoadServiceGroups;
	/** Menu item for saving the Service Groups and their Incidents. */
	private JMenuItem itemSaveServiceGroups;
	/** Menu item for clearing all Service Groups and their incidents from the system. */
	private JMenuItem itemClearServiceGroups;
	/** Menu item for quitting the program. */
	private JMenuItem itemQuit;
	/** Panel that will contain different views for the application. */
	private JPanel panel;
	/** Constant to identify ServiceGroupPanel for CardLayout. */
	private static final String SERVICE_GROUP_PANEL = "ServiceGroupPanel";
	/** Constant to identify NewPanel for CardLayout. */
	private static final String NEW_PANEL = "NewPanel";
	/** Constant to identify InProgressPanel for CardLayout. */
	private static final String IN_PROGRESS_PANEL = "InProgressPanel";
	/** Constant to identify OnHoldPnale for CardLayout. */
	private static final String ON_HOLD_PANEL = "OnHoldPanel";
	/** Constant to identify ResolvedPanel for CardLayout. */
	private static final String RESOLVED_PANEL = "ResolvedPanel";
	/** Constant to identify CanceledPanel for CardLayout. */
	private static final String CANCELED_PANEL = "CanceledPanel";
	/** Constant to identify CreateIncidentPanel for CardLayout. */
	private static final String CREATE_INCIDENT_PANEL = "CreateIncidentPanel";
	/** ServiceGroup panel - we only need one instance, so it's final. */
	private final ServiceGroupPanel pnlServiceGroup = new ServiceGroupPanel();
	/** New panel - we only need one instance, so it's final. */
	private final NewPanel pnlNew = new NewPanel();
	/** InProgress panel - we only need one instance, so it's final. */
	private final InProgressPanel pnlInProgress = new InProgressPanel();
	/** OnHold panel - we only need one instance, so it's final. */
	private final OnHoldPanel pnlOnHold = new OnHoldPanel();
	/** Resolved panel - we only need one instance, so it's final. */
	private final ResolvedPanel pnlResolved = new ResolvedPanel();
	/** Canceled panel - we only need one instance, so it's final. */
	private final CanceledPanel pnlCanceled = new CanceledPanel();
	/** Add Incident panel - we only need one instance, so it's final. */
	private final AddIncidentPanel pnlAddIncident = new AddIncidentPanel();
	/** Reference to CardLayout for panel.  Stacks all of the panels. */
	private CardLayout cardLayout;
	
	
	/**
	 * Constructs a ServiceWolfGUI object that will contain a JMenuBar and a
	 * JPanel that will hold different possible views of the data in
	 * the ServiceWolf.
	 */
	public ServiceWolfGUI() {
		super();
		
		//Set up general GUI info
		setSize(500, 700);
		setLocation(50, 50);
		setTitle(APP_TITLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setUpMenuBar();
		
		//Create JPanel that will hold rest of GUI information.
		//The JPanel utilizes a CardLayout, which stack several different
		//JPanels.  User actions lead to switching which "Card" is visible.
		panel = new JPanel();
		cardLayout = new CardLayout();
		panel.setLayout(cardLayout);
		panel.add(pnlServiceGroup, SERVICE_GROUP_PANEL);
		panel.add(pnlNew, NEW_PANEL);
		panel.add(pnlInProgress, IN_PROGRESS_PANEL);
		panel.add(pnlOnHold, ON_HOLD_PANEL);
		panel.add(pnlResolved, RESOLVED_PANEL);
		panel.add(pnlCanceled, CANCELED_PANEL);
		panel.add(pnlAddIncident, CREATE_INCIDENT_PANEL);
		cardLayout.show(panel, SERVICE_GROUP_PANEL);
		
		//Add panel to the container
		Container c = getContentPane();
		c.add(panel, BorderLayout.CENTER);
		
		//Set the GUI visible
		setVisible(true);
	}
	
	/**
	 * Makes the GUI Menu bar that contains options working with a file
	 * containing service groups and incidents or for quitting the application.
	 */
	private void setUpMenuBar() {
		//Construct Menu items
		menuBar = new JMenuBar();
		menu = new JMenu(FILE_MENU_TITLE);
		itemLoadServiceGroups = new JMenuItem(LOAD_TITLE);
		itemSaveServiceGroups = new JMenuItem(SAVE_TITLE);
		itemClearServiceGroups = new JMenuItem(CLEAR_TITLE);
		itemQuit = new JMenuItem(QUIT_TITLE);
		itemLoadServiceGroups.addActionListener(this);
		itemSaveServiceGroups.addActionListener(this);
		itemClearServiceGroups.addActionListener(this);
		itemQuit.addActionListener(this);
		
		//Start with save button disabled
		itemSaveServiceGroups.setEnabled(false);
		
		//Build Menu and add to GUI
		menu.add(itemLoadServiceGroups);
		menu.add(itemSaveServiceGroups);
		menu.add(itemClearServiceGroups);
		menu.add(itemQuit);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
	}
	
	/**
	 * Performs an action based on the given ActionEvent.
	 * @param e user event that triggers an action.
	 */
	public void actionPerformed(ActionEvent e) {
		//Use ServiceWolfManager's singleton to create/get the sole instance.
		ServiceWolfManager model = ServiceWolfManager.getInstance();
		if (e.getSource() == itemClearServiceGroups) {
			//Clear service groups from model.
			try {
				model.clearServiceGroups();
				itemSaveServiceGroups.setEnabled(false);
				pnlServiceGroup.updateServiceGroup();
				cardLayout.show(panel, SERVICE_GROUP_PANEL);
				validate();
				repaint();
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, exp.getMessage());
			}
		} else if (e.getSource() == itemLoadServiceGroups) {
			//Load an existing service group and incidents list
			try {
				model.loadFromFile(getFileName(true));
				itemSaveServiceGroups.setEnabled(true);
				pnlServiceGroup.updateServiceGroup();
				cardLayout.show(panel, SERVICE_GROUP_PANEL);
				validate();
				repaint();
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, "Unable to load file.");
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		} else if (e.getSource() == itemSaveServiceGroups) {
			//Save current service group and incidents
			try {
				model.saveToFile(getFileName(false));
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, "Unable to save file.");
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		} else if (e.getSource() == itemQuit) {
			//Quit the program
			try {
				model.saveToFile(getFileName(false));
				System.exit(0);  //Ignore SpotBugs warning here - this is the only place to quit the program!
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, "Unable to save file.");
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		}
	}
	
	/**
	 * Returns a file name generated through interactions with a JFileChooser
	 * object.
	 * @param load true if loading a file, false if saving
	 * @return the file name selected through JFileChooser
	 * @throws IllegalStateException if no file name provided
	 */
	private String getFileName(boolean load) {
		JFileChooser fc = new JFileChooser("./");  //Open JFileChooser to current working directory
		int returnVal = Integer.MIN_VALUE;
		if (load) {
			returnVal = fc.showOpenDialog(this);
		} else {
			returnVal = fc.showSaveDialog(this);
		}
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			//Error or user canceled, either way no file name.
			throw new IllegalStateException();
		}
		File gameFile = fc.getSelectedFile();
		return gameFile.getAbsolutePath();
	}

	/**
	 * Starts the GUI for the ServiceWolf application.
	 * @param args command line arguments
	 */
	public static void main(String [] args) {
		new ServiceWolfGUI();
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * shows the service group including the list of incidents.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class ServiceGroupPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		
		/** Label for selecting current service group */
		private JLabel lblCurrentServiceGroup;
		/** Combo box for ServiceGroup list */
		private JComboBox<String> comboServiceGroupList;
		/** Button to add a service group */
		private JButton btnAddServiceGroup;
		/** Button to edit the selected service group */
		private JButton btnEditServiceGroup;
		/** Button to delete the selected service group */
		private JButton btnDeleteServiceGroup;
		
		/** Button for creating a new incident */
		private JButton btnAdd;
		/** Button for deleting the selected incident in the list */
		private JButton btnDelete;
		/** Button for editing the selected incident in the list */
		private JButton btnEdit;
		/** JTable for displaying the list of incidents */
		private JTable tableIncidents;
		/** TableModel for Incidents */
		private IncidentTableModel tableModel;
		
		/**
		 * Creates the incident list.
		 */
		public ServiceGroupPanel() {
			super(new GridBagLayout());
			
			//Set up JPanel for service groups and incidents
			lblCurrentServiceGroup = new JLabel("Current Service Group");
			comboServiceGroupList = new JComboBox<String>();
			comboServiceGroupList.addActionListener(this);
			btnAddServiceGroup = new JButton("Add Service Group");
			btnAddServiceGroup.addActionListener(this);
			btnEditServiceGroup = new JButton("Edit Service Group");
			btnEditServiceGroup.addActionListener(this);
			btnDeleteServiceGroup = new JButton("Delete Service Group");
			btnDeleteServiceGroup.addActionListener(this);
						
			//Set up the JPanel that will hold action buttons		
			btnAdd = new JButton("Add Incident");
			btnAdd.addActionListener(this);
			btnDelete = new JButton("Delete Incident");
			btnDelete.addActionListener(this);
			btnEdit = new JButton("Edit Incident");
			btnEdit.addActionListener(this);
			
			JPanel pnlServiceGroupSelection = new JPanel();
			pnlServiceGroupSelection.setLayout(new GridLayout(1, 2));
			pnlServiceGroupSelection.add(lblCurrentServiceGroup);
			pnlServiceGroupSelection.add(comboServiceGroupList);
			
			JPanel pnlServiceGroupActions = new JPanel();
			pnlServiceGroupActions.setLayout(new GridLayout(1, 3));
			pnlServiceGroupActions.add(btnAddServiceGroup);
			pnlServiceGroupActions.add(btnEditServiceGroup);
			pnlServiceGroupActions.add(btnDeleteServiceGroup);
			
			JPanel pnlIncidentActions = new JPanel();
			pnlIncidentActions.setLayout(new GridLayout(1, 3));
			pnlIncidentActions.add(btnAdd);
			pnlIncidentActions.add(btnDelete);
			pnlIncidentActions.add(btnEdit);
						
			//Set up table
			tableModel = new IncidentTableModel();
			tableIncidents = new JTable(tableModel);
			tableIncidents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableIncidents.setPreferredScrollableViewportSize(new Dimension(500, 500));
			tableIncidents.setFillsViewportHeight(true);
			
			JScrollPane listScrollPane = new JScrollPane(tableIncidents);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlServiceGroupSelection, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlServiceGroupActions, c);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlIncidentActions, c);
			
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 20;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			c.gridheight = GridBagConstraints.REMAINDER;
			c.gridwidth = GridBagConstraints.REMAINDER;
			add(listScrollPane, c);
			
			updateServiceGroup();
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == comboServiceGroupList) {
				int idx = comboServiceGroupList.getSelectedIndex();
				
				if (idx == -1) {
					updateServiceGroup();
				} else {				
					String serviceGroupName = comboServiceGroupList.getItemAt(idx);
					ServiceWolfManager.getInstance().loadServiceGroup(serviceGroupName);
				}
				updateServiceGroup();
			} else if (e.getSource() == btnAdd) {
				//If the add button is clicked switch to the createIncidentPanel
				pnlAddIncident.setServiceGroupName(ServiceWolfManager.getInstance().getServiceGroupName());
				cardLayout.show(panel,  CREATE_INCIDENT_PANEL);
			} else if (e.getSource() == btnDelete) {
				//If the delete button is clicked, delete the incident
				int row = tableIncidents.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(ServiceWolfGUI.this, "No incident selected");
				} else {
					try {
						int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
						ServiceWolfManager.getInstance().deleteIncidentById(id);
					} catch (NumberFormatException nfe ) {
						JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid id");
					}
				}
				updateServiceGroup();
			} else if (e.getSource() == btnEdit) {
				//If the edit button is clicked, switch panel based on state
				int row = tableIncidents.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(ServiceWolfGUI.this, "No incident selected");
				} else {
					try {
						int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
						String stateName = ServiceWolfManager.getInstance().getIncidentById(id).getState();
						
						if (stateName.equals(Incident.NEW_NAME)) {
							cardLayout.show(panel, NEW_PANEL);
							pnlNew.setInfo(id);
						} else if (stateName.equals(Incident.IN_PROGRESS_NAME)) {
							cardLayout.show(panel, IN_PROGRESS_PANEL);
							pnlInProgress.setInfo(id);
						} else if (stateName.equals(Incident.ON_HOLD_NAME)) {
							cardLayout.show(panel, ON_HOLD_PANEL);
							pnlOnHold.setInfo(id);
						} else if (stateName.equals(Incident.RESOLVED_NAME)) {
							cardLayout.show(panel, RESOLVED_PANEL);
							pnlResolved.setInfo(id);
						} else if (stateName.equals(Incident.CANCELED_NAME)) {
							cardLayout.show(panel, CANCELED_PANEL);
							pnlCanceled.setInfo(id);
						}
					} catch (NumberFormatException nfe) {
						JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid id");
					} catch (NullPointerException npe) {
						JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid id");
					}
				}
				updateServiceGroup();
			} else if (e.getSource() == btnAddServiceGroup) {
				try {
					String serviceGroupName = (String) JOptionPane.showInputDialog(this, "Service Group Name?", "Create New Service Group", JOptionPane.QUESTION_MESSAGE);
					ServiceWolfManager.getInstance().addServiceGroup(serviceGroupName);
					itemSaveServiceGroups.setEnabled(true);
					updateServiceGroup();
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ServiceWolfGUI.this, iae.getMessage());
				}
			} else if (e.getSource() == btnEditServiceGroup) {
				try {
					String serviceGroupName = (String) JOptionPane.showInputDialog(this, "Update Service Group Name?", "Edit Service Group", JOptionPane.QUESTION_MESSAGE, null, null, ServiceWolfManager.getInstance().getServiceGroupName());
					ServiceWolfManager.getInstance().editServiceGroup(serviceGroupName);
					itemSaveServiceGroups.setEnabled(true);
					updateServiceGroup();
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ServiceWolfGUI.this, iae.getMessage());
				}
			} else if (e.getSource() == btnDeleteServiceGroup) {
				try {
					ServiceWolfManager.getInstance().deleteServiceGroup();
					updateServiceGroup();
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ServiceWolfGUI.this, iae.getMessage());
				}
			}
			
			//On all paths, update the GUI!
			ServiceWolfGUI.this.repaint();
			ServiceWolfGUI.this.validate();
		}
		
		public void updateServiceGroup() {
			tableModel.updateData();
			
			String serviceGroupName = ServiceWolfManager.getInstance().getServiceGroupName();
			
			if (serviceGroupName == null) {
				serviceGroupName = "Create a ServiceGroup";
				btnAdd.setEnabled(false);
				btnDelete.setEnabled(false);
				btnEdit.setEnabled(false);
				btnAddServiceGroup.setEnabled(true);
				btnEditServiceGroup.setEnabled(false);
				btnDeleteServiceGroup.setEnabled(false);
			} else {
				btnAdd.setEnabled(true);
				btnDelete.setEnabled(true);
				btnEdit.setEnabled(true);
				btnAddServiceGroup.setEnabled(true);
				btnEditServiceGroup.setEnabled(true);
				btnDeleteServiceGroup.setEnabled(true);
			}
			
			comboServiceGroupList.removeAllItems();
			String [] serviceGroupList = ServiceWolfManager.getInstance().getServiceGroupList();
			for (int i = 0; i < serviceGroupList.length; i++) {
				comboServiceGroupList.addItem(serviceGroupList[i]);
			}
			
			serviceGroupName = ServiceWolfManager.getInstance().getServiceGroupName(); //Get name again
			if (serviceGroupName != null) {
				comboServiceGroupList.setSelectedItem(serviceGroupName);
			} else {
				serviceGroupName = "Create a ServiceGroup";
			}
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Service Group: " + serviceGroupName);
			setBorder(border);
			setToolTipText("Service Group: " + serviceGroupName);
		}
		
		/**
		 * IncidentTableModel is the object underlying the JTable object that displays
		 * the list of Incidents to the user.
		 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
		 */
		private class IncidentTableModel extends AbstractTableModel {
			
			/** ID number used for object serialization. */
			private static final long serialVersionUID = 1L;
			/** Column names for the table */
			private String [] columnNames = {"ID", "State", "Title", "Status Details"};
			/** Data stored in the table */
			private Object [][] data;
			
			/**
			 * Constructs the IncidentTableModel by requesting the latest information
			 * from the IncidentTableModel.
			 */
			public IncidentTableModel() {
				updateData();
			}

			/**
			 * Returns the number of columns in the table.
			 * @return the number of columns in the table.
			 */
			public int getColumnCount() {
				return columnNames.length;
			}

			/**
			 * Returns the number of rows in the table.
			 * @return the number of rows in the table.
			 */
			public int getRowCount() {
				if (data == null) 
					return 0;
				return data.length;
			}
			
			/**
			 * Returns the column name at the given index.
			 * @param col the column index
			 * @return the column name at the given column.
			 */
			public String getColumnName(int col) {
				return columnNames[col];
			}

			/**
			 * Returns the data at the given {row, col} index.
			 * @param row the row index
			 * @param col the column index
			 * @return the data at the given location.
			 */
			public Object getValueAt(int row, int col) {
				if (data == null)
					return null;
				return data[row][col];
			}
			
			/**
			 * Sets the given value to the given {row, col} location.
			 * @param value Object to modify in the data.
			 * @param row the row index
			 * @param col the column index
			 */
			public void setValueAt(Object value, int row, int col) {
				data[row][col] = value;
				fireTableCellUpdated(row, col);
			}
			
			/**
			 * Updates the given model with Incident information from ServiceWolf.
			 */
			private void updateData() {
				ServiceWolfManager m = ServiceWolfManager.getInstance();
				data = m.getIncidentsAsArray();
			}
		}
	}	
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with a New incident.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class NewPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** IncidentPanel that presents the Incident's information to the user */
		private IncidentPanel pnlInfo;
		/** Current Incident's id */
		private int id;
		
		/** Label - message */
		private JLabel lblMessage;
		/** TextField - message */
		private JTextField txtMessage;
		
		/** Label - owner */
		private JLabel lblOwner;
		/** ComboBox - owner */
		private JTextField txtOwner;
		/** Button - Assign w/ owner */
		private JButton btnAssign;
		
		/** Label - cancellation reason */
		private JLabel lblCancellationReason;
		/** ComboBox - cancellation reason */
		private JComboBox<String> comboCancellationReason;
		/** Button - Cancel w/ cancellation reason */
		private JButton btnCancel;
		
		/** Button - return edit */
		private JButton btnReturn;
		
		/**
		 * Constructs the JPanel for editing an Incident in the NewState
		 */
		public NewPanel() {
			
			pnlInfo = new IncidentPanel();	
			
			lblMessage = new JLabel("Message: ");
			txtMessage = new JTextField(25);
			
			lblOwner = new JLabel("Owner Id:");
			txtOwner = new JTextField(25);
			btnAssign = new JButton("Assign");
			
			lblCancellationReason = new JLabel("Cancelation Reason:");
			comboCancellationReason = new JComboBox<String>();
			comboCancellationReason.addItem("Duplicate");
			comboCancellationReason.addItem("Unnecessary");
			comboCancellationReason.addItem("Not an Incident");
			comboCancellationReason.addItem("Caller Canceled");
			btnCancel = new JButton("Cancel");
			
			btnReturn = new JButton("Return");
			
			btnAssign.addActionListener(this);
			btnCancel.addActionListener(this);
			btnReturn.addActionListener(this);
			
			JPanel pnlMessage = new JPanel();
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Incident Log Message");
			pnlMessage.setBorder(border);
			pnlMessage.setToolTipText("Incident Log Message");
			pnlMessage.setLayout(new GridLayout(1, 2));
			pnlMessage.add(lblMessage);
			pnlMessage.add(txtMessage);
			
			JPanel pnlCommands = new JPanel();
			lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());
			
			JPanel pnlAccept = new JPanel();
			pnlAccept.setLayout(new GridLayout(1, 3));
			pnlAccept.add(lblOwner);
			pnlAccept.add(txtOwner);
			pnlAccept.add(btnAssign);
			
			JPanel pnlCancel = new JPanel();
			pnlCancel.setLayout(new GridLayout(1, 3));
			pnlCancel.add(lblCancellationReason);
			pnlCancel.add(comboCancellationReason);
			pnlCancel.add(btnCancel);

			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 1));
			pnlBtnRow.add(btnReturn);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlAccept, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlCancel, c);
			
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlInfo, c);
			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlMessage, c);
			
			c.gridx = 0;
			c.gridy = 8;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);
			
			c.gridx = 0;
			c.gridy = 9;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlBtnRow, c);
			
		}
		
		/**
		 * Set the IncidentInfoPanel with the given incident data.
		 * @param id id of the incident
		 */
		public void setInfo(int id) {
			this.id = id;
			pnlInfo.setIncidentId(this.id);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			
			String message = txtMessage.getText();
			if (e.getSource() != btnReturn && (message == null || message.length() == 0)) {
				JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid information.");
				reset = false;
			} else if (e.getSource() == btnAssign) {				
				String owner = txtOwner.getText();
				//Try a command.  If problem, go back to incident list.
				try {
					Command c = new Command(Command.CommandValue.ASSIGN, owner, message);
					ServiceWolfManager.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid information.");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid state transition");
					reset = false;
				}
			} else if (e.getSource() == btnCancel) {
				int idx = comboCancellationReason.getSelectedIndex();
				
				if (idx == -1) {
					reset = false;
					JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid information.");
				} else {				
					String cancellationReason = comboCancellationReason.getItemAt(idx);
					//Try a command.  If problem, go back to incident list.
					try {
						Command c = new Command(Command.CommandValue.CANCEL, cancellationReason, message);
						ServiceWolfManager.getInstance().executeCommand(id, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid information.");
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid state transition");
						reset = false;
					}
				}
			}
			if (reset) {
				//All buttons lead back to list
				cardLayout.show(panel, SERVICE_GROUP_PANEL);
				pnlServiceGroup.updateServiceGroup();
				ServiceWolfGUI.this.repaint();
				ServiceWolfGUI.this.validate();
				txtMessage.setText("");
				txtOwner.setText("");
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with an incident in the in progress state.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class InProgressPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** IncidentPanel that presents the Incident's information to the user */
		private IncidentPanel pnlInfo;
		/** Current Incident's id */
		private int id;
		
		/** Label - message */
		private JLabel lblMessage;
		/** TextField - message */
		private JTextField txtMessage;
		
		/** Label - on hold reason */
		private JLabel lblOnHoldReason;
		/** ComboBox - on hold reason */
		private JComboBox<String> comboOnHoldReason;
		/** Button - place on hold */
		private JButton btnHold;
		
		/** Label - resolution reason */
		private JLabel lblResolutionReason;
		/** ComboBox - resolution reason */
		private JComboBox<String> comboResolutionReason;
		/** Button - resolve */
		private JButton btnResolve;
		
		/** Label - owner */
		private JLabel lblOwner;
		/** TextField - owner */
		private JTextField txtOwner;
		/** Button - Assign w/ developer */
		private JButton btnAssign;
				
		/** Label - cancellation reason */
		private JLabel lblCancellationReason;
		/** ComboBox - cancellation reason */
		private JComboBox<String> comboCancellationReason;
		/** Button - Cancel w/ rejection reason */
		private JButton btnCancel;
				
		/** Button - cancel edit and return to list */
		private JButton btnReturn;
		
		/**
		 * Constructs the JPanel for editing an Incident in the InProgressState
		 */
		public InProgressPanel() {
			
			pnlInfo = new IncidentPanel();	
			
			lblMessage = new JLabel("Message: ");
			txtMessage = new JTextField(25);
			
			lblOnHoldReason = new JLabel("On Hold Reason:");
			comboOnHoldReason = new JComboBox<String>();
			comboOnHoldReason.addItem("Awaiting Caller");
			comboOnHoldReason.addItem("Awaiting Change");
			comboOnHoldReason.addItem("Awaiting Vendor");
			btnHold = new JButton("Hold");
			
			lblResolutionReason = new JLabel("Resolution Reason:");
			comboResolutionReason = new JComboBox<String>();
			comboResolutionReason.addItem("Permanently Solved");
			comboResolutionReason.addItem("Workaround");
			comboResolutionReason.addItem("Caller Closed");
			btnResolve = new JButton("Resolve");
			
			lblOwner = new JLabel("Owner Id:");
			txtOwner = new JTextField(25);
			btnAssign = new JButton("Assign");
			
			lblCancellationReason = new JLabel("Cancellation Reason:");
			comboCancellationReason = new JComboBox<String>();
			comboCancellationReason.addItem("Duplicate");
			comboCancellationReason.addItem("Unnecessary");
			comboCancellationReason.addItem("Not an Incident");
			comboCancellationReason.addItem("Caller Canceled");
			btnCancel = new JButton("Cancel");			
			
			btnReturn = new JButton("Return");
			
			btnHold.addActionListener(this);
			btnResolve.addActionListener(this);
			btnAssign.addActionListener(this);
			btnCancel.addActionListener(this);
			btnReturn.addActionListener(this);
			
			JPanel pnlMessage = new JPanel();
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Incident Log Message");
			pnlMessage.setBorder(border);
			pnlMessage.setToolTipText("Incident Log Message");
			pnlMessage.setLayout(new GridLayout(1, 2));
			pnlMessage.add(lblMessage);
			pnlMessage.add(txtMessage);
			
			JPanel pnlCommands = new JPanel();
			lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());
			
			JPanel pnlHold = new JPanel();
			pnlHold.setLayout(new GridLayout(1, 3));
			pnlHold.add(lblOnHoldReason);
			pnlHold.add(comboOnHoldReason);
			pnlHold.add(btnHold);
			
			JPanel pnlResolve = new JPanel();
			pnlResolve.setLayout(new GridLayout(1, 3));
			pnlResolve.add(lblResolutionReason);
			pnlResolve.add(comboResolutionReason);
			pnlResolve.add(btnResolve);
			
			JPanel pnlAssign = new JPanel();
			pnlAssign.setLayout(new GridLayout(1, 3));
			pnlAssign.add(lblOwner);
			pnlAssign.add(txtOwner);
			pnlAssign.add(btnAssign);
			
			JPanel pnlCancel = new JPanel();
			pnlCancel.setLayout(new GridLayout(1, 3));
			pnlCancel.add(lblCancellationReason);
			pnlCancel.add(comboCancellationReason);
			pnlCancel.add(btnCancel);

			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 1));
			pnlBtnRow.add(btnReturn);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlHold, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlResolve, c);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlAssign, c);
			
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlCancel, c);			
			
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlInfo, c);
			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlMessage, c);
			
			c.gridx = 0;
			c.gridy = 8;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);
			
			c.gridx = 0;
			c.gridy = 9;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlBtnRow, c);
			
		}
		
		/**
		 * Set the IncidentInfoPanel with the given incident data.
		 * @param id id of the incident
		 */
		public void setInfo(int id) {
			this.id = id;
			pnlInfo.setIncidentId(this.id);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			
			String message = txtMessage.getText();
			if (e.getSource() != btnReturn && (message == null || message.length() == 0)) {
				JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid information.");
				reset = false;
			} else if (e.getSource() == btnHold) {
				int idx = comboOnHoldReason.getSelectedIndex();
				
				if (idx == -1) {
					reset = false;
					JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid information.");
				} else {				
					String onHoldReason = comboOnHoldReason.getItemAt(idx);
					//Try a command.  If problem, go back to incident list.
					try {
						Command c = new Command(Command.CommandValue.HOLD, onHoldReason, message);
						ServiceWolfManager.getInstance().executeCommand(id, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid information.");
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid state transition");
						reset = false;
					}
				}
			} else if (e.getSource() == btnResolve) {
				int idx = comboResolutionReason.getSelectedIndex();
				
				if (idx == -1) {
					reset = false;
					JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid information.");
				} else {				
					String resolutionReason = comboResolutionReason.getItemAt(idx);
					//Try a command.  If problem, go back to incident list.
					try {
						Command c = new Command(Command.CommandValue.RESOLVE, resolutionReason, message);
						ServiceWolfManager.getInstance().executeCommand(id, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid information.");
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid state transition");
						reset = false;
					}
				}
			} else if (e.getSource() == btnAssign) {				
				String developer = txtOwner.getText();
				//Try a command.  If problem, go back to incident list.
				try {
					Command c = new Command(Command.CommandValue.ASSIGN, developer, message);
					ServiceWolfManager.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid information.");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid state transition");
					reset = false;
				}
			} else if (e.getSource() == btnCancel) {
				int idx = comboCancellationReason.getSelectedIndex();
				
				if (idx == -1) {
					reset = false;
					JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid information.");
				} else {				
					String cancellationReason = comboCancellationReason.getItemAt(idx);
					//Try a command.  If problem, go back to incident list.
					try {
						Command c = new Command(Command.CommandValue.CANCEL, cancellationReason, message);
						ServiceWolfManager.getInstance().executeCommand(id, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid information.");
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid state transition");
						reset = false;
					}
				}
			} 
			if (reset) {
				//All buttons lead back to list
				cardLayout.show(panel, SERVICE_GROUP_PANEL);
				pnlServiceGroup.updateServiceGroup();
				ServiceWolfGUI.this.repaint();
				ServiceWolfGUI.this.validate();
				txtMessage.setText("");
				txtOwner.setText("");
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with an incident in the on hold state.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class OnHoldPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** IncidentPanel that presents the Incident's information to the user */
		private IncidentPanel pnlInfo;
		/** Current Incident's id */
		private int id;
		
		/** Label - message */
		private JLabel lblMessage;
		/** TextField - message */
		private JTextField txtMessage;
		
		/** Button - Investigate */
		private JButton btnInvestigate;
		
		/** Button - return */
		private JButton btnReturn;
		
		/**
		 * Constructs the JPanel for editing a Incident in the OnHoldState
		 */
		public OnHoldPanel() {
			pnlInfo = new IncidentPanel();		
			
			lblMessage = new JLabel("Message: ");
			txtMessage = new JTextField(25);
			
			btnInvestigate = new JButton("Investigate");
			btnReturn = new JButton("Return");
			
			btnInvestigate.addActionListener(this);
			btnReturn.addActionListener(this);
			
			JPanel pnlMessage = new JPanel();
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Incident Log Message");
			pnlMessage.setBorder(border);
			pnlMessage.setToolTipText("Incident Log Message");
			pnlMessage.setLayout(new GridLayout(1, 2));
			pnlMessage.add(lblMessage);
			pnlMessage.add(txtMessage);
			
			JPanel pnlCommands = new JPanel();
			lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());


			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 2));
			pnlBtnRow.add(btnReturn);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(btnInvestigate, c);
			
			
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlInfo, c);
			
			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlMessage, c);
			
			c.gridx = 0;
			c.gridy = 8;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);
			
			c.gridx = 0;
			c.gridy = 9;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlBtnRow, c);
			
		}
		
		/**
		 * Set the IncidentInfoPanel with the given incident data.
		 * @param id id of the incident
		 */
		public void setInfo(int id) {
			this.id = id;
			pnlInfo.setIncidentId(this.id);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			String message = txtMessage.getText();
			if (e.getSource() != btnReturn && (message == null || message.length() == 0)) {
				JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid information.");
				reset = false;
			} else if (e.getSource() == btnInvestigate) {
				try {
					Command c = new Command(Command.CommandValue.INVESTIGATE, null, message);
					ServiceWolfManager.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid information.");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid state transition");
					reset = false;
				}
			} 
			if (reset) {
				//All buttons lead back to list
				cardLayout.show(panel, SERVICE_GROUP_PANEL);
				pnlServiceGroup.updateServiceGroup();
				ServiceWolfGUI.this.repaint();
				ServiceWolfGUI.this.validate();
				txtMessage.setText("");
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with an incident in the resolved state.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class ResolvedPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** IncidentPanel that presents the Incident's information to the user */
		private IncidentPanel pnlInfo;
		/** Current Incident's id */
		private int id;
		
		/** Label - message */
		private JLabel lblMessage;
		/** TextField - message */
		private JTextField txtMessage;
		
		/** Button - Reopen */
		private JButton btnReopen;
		
		/** Label - cancellation reason */
		private JLabel lblCancellationReason;
		/** ComboBox - cancellation reason */
		private JComboBox<String> comboCancellationReason;
		/** Button - Cancel w/ rejection reason */
		private JButton btnCancel;
		
		/** Button - return */
		private JButton btnReturn;
		
		/**
		 * Constructs the JPanel for editing a Incident in the ResolvedState
		 */
		public ResolvedPanel() {
			
			pnlInfo = new IncidentPanel();
			
			lblMessage = new JLabel("Message: ");
			txtMessage = new JTextField(25);
			
			btnReopen = new JButton("Reopen");
			
			lblCancellationReason = new JLabel("Cancellation Reason:");
			comboCancellationReason = new JComboBox<String>();
			comboCancellationReason.addItem("Duplicate");
			comboCancellationReason.addItem("Unnecessary");
			comboCancellationReason.addItem("Not an Incident");
			comboCancellationReason.addItem("Caller Canceled");
			btnCancel = new JButton("Cancel");	
			
			btnReturn = new JButton("Return");
			
			btnReopen.addActionListener(this);
			btnCancel.addActionListener(this);
			btnReturn.addActionListener(this);
			
			JPanel pnlMessage = new JPanel();
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Incident Log Message");
			pnlMessage.setBorder(border);
			pnlMessage.setToolTipText("Incident Log Message");
			pnlMessage.setLayout(new GridLayout(1, 2));
			pnlMessage.add(lblMessage);
			pnlMessage.add(txtMessage);
			
			JPanel pnlCommands = new JPanel();
			lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());
			
			JPanel pnlCancel = new JPanel();
			pnlCancel.setLayout(new GridLayout(1, 3));
			pnlCancel.add(lblCancellationReason);
			pnlCancel.add(comboCancellationReason);
			pnlCancel.add(btnCancel);

			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 2));
			pnlBtnRow.add(btnReopen);
			pnlBtnRow.add(btnReturn);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlCancel, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(btnReopen, c);
			
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlInfo, c);
			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlMessage, c);
			
			c.gridx = 0;
			c.gridy = 8;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);
			
			c.gridx = 0;
			c.gridy = 9;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlBtnRow, c);
		}
		
		/**
		 * Set the IncidentInfoPanel with the given incident data.
		 * @param id id of the incident
		 */
		public void setInfo(int id) {
			this.id = id;
			pnlInfo.setIncidentId(this.id);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			String message = txtMessage.getText();
			if (e.getSource() != btnReturn && (message == null || message.length() == 0)) {
				JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid information.");
				reset = false;
			} else if (e.getSource() == btnReopen) {
				try {
					Command c = new Command(Command.CommandValue.REOPEN, null, message);
					ServiceWolfManager.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid information.");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid state transition");
					reset = false;
				}
			} else if (e.getSource() == btnCancel) {
				int idx = comboCancellationReason.getSelectedIndex();
				
				if (idx == -1) {
					reset = false;
					JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid information.");
				} else {				
					String cancellationReason = comboCancellationReason.getItemAt(idx);
					//Try a command.  If problem, go back to incident list.
					try {
						Command c = new Command(Command.CommandValue.CANCEL, cancellationReason, message);
						ServiceWolfManager.getInstance().executeCommand(id, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid information.");
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid state transition");
						reset = false;
					}
				}
			} 
			if (reset) {
				//All buttons lead back to list
				cardLayout.show(panel, SERVICE_GROUP_PANEL);
				pnlServiceGroup.updateServiceGroup();
				ServiceWolfGUI.this.repaint();
				ServiceWolfGUI.this.validate();
				txtMessage.setText("");
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with an incident in the canceled state.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class CanceledPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** IncidentPanel that presents the Incident's information to the user */
		private IncidentPanel pnlInfo;
		/** Current Incident's id */
		private int id;
		
		/** Button - return */
		private JButton btnReturn;
		
		/**
		 * Constructs the JPanel for editing an Incident in the CanceledState
		 */
		public CanceledPanel() {
			
			pnlInfo = new IncidentPanel();		
			
			btnReturn = new JButton("Return");
			
			btnReturn.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());


			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 1));
			pnlBtnRow.add(btnReturn);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlBtnRow, c);
			
			
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlInfo, c);
			
			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);
			
		}
		
		/**
		 * Set the IncidentInfoPanel with the given incident data.
		 * @param id id of the incident
		 */
		public void setInfo(int id) {
			this.id = id;
			pnlInfo.setIncidentId(this.id);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true; 
			if (reset) {
				//All buttons lead back to list
				cardLayout.show(panel, SERVICE_GROUP_PANEL);
				pnlServiceGroup.updateServiceGroup();
				ServiceWolfGUI.this.repaint();
				ServiceWolfGUI.this.validate();
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * shows information about the incident.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class IncidentPanel extends JPanel {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		
		/** Label - title */
		private JLabel lblTitle;
		/** JTextField - title */
		private JTextField txtTitle;
		
		/** Label - caller */
		private JLabel lblCaller;
		/** JTextField - caller */
		private JTextField txtCaller;
		
		/** Label - reopenCount */
		private JLabel lblReopenCount;
		/** JTextField - reopenCount */
		private JTextField txtreopenCount;
				
		/** Label - owner */
		private JLabel lblOwner;
		/** JTextField - owner */
		private JTextField txtOwner;
		
		/** Label - status details */
		private JLabel lblStatusDetails;
		/** JTextField - status details */
		private JTextField txtStatusDetails;
		
		/** Label - Incident log messages */
		private JLabel lblMessages;
		/** JTextArea - Incident log messages */
		private JTextArea txtMessages;
		
		/** 
		 * Construct the panel for the information.
		 */
		public IncidentPanel() {
			super(new GridBagLayout());
			
			lblTitle = new JLabel("Title");
			lblCaller = new JLabel("Caller");
			lblReopenCount = new JLabel("Reopen Count");
			lblOwner = new JLabel("Onwer Id");
			lblStatusDetails = new JLabel("Status Details");
			lblMessages = new JLabel("Incident Log Messages");
			
			txtTitle = new JTextField(15);
			txtCaller = new JTextField(15);
			txtreopenCount = new JTextField(15);
			txtOwner = new JTextField(15);
			txtStatusDetails = new JTextField(15);
			txtMessages = new JTextArea(30, 10);
			
			txtTitle.setEditable(false);
			txtCaller.setEditable(false);
			txtreopenCount.setEditable(false);
			txtOwner.setEditable(false);
			txtStatusDetails.setEditable(false);
			txtMessages.setEditable(false);
			
			JScrollPane scrollMessages = new JScrollPane(txtMessages);
			scrollMessages.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollMessages.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			
			GridBagConstraints c = new GridBagConstraints();
						
			//Row 1 - Title	
			JPanel row1 = new JPanel();
			row1.setLayout(new GridLayout(1, 2));
			row1.add(lblTitle);
			row1.add(txtTitle);
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row1, c);
			
			//Row 2 - Owner
			JPanel row2 = new JPanel();
			row2.setLayout(new GridLayout(1, 2));
			row2.add(lblCaller);
			row2.add(txtCaller);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row2, c);
			
			//Row 3 - Reopen Count
			JPanel row3 = new JPanel();
			row3.setLayout(new GridLayout(1, 2));
			row3.add(lblReopenCount);
			row3.add(txtreopenCount);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row3, c);
			
			//Row 4 - Owner Id
			JPanel row4 = new JPanel();
			row4.setLayout(new GridLayout(1, 2));
			row4.add(lblOwner);
			row4.add(txtOwner);
			
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row4, c);
			
			//Row 5 - Status Details
			JPanel row5 = new JPanel();
			row5.setLayout(new GridLayout(1, 2));
			row5.add(lblStatusDetails);
			row5.add(txtStatusDetails);
			
			c.gridx = 0;
			c.gridy = 4;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row5, c);
			
			//Row 6 - Message label
			JPanel row6 = new JPanel();
			row6.setLayout(new GridLayout(1, 1));
			row6.add(lblMessages);
			
			c.gridx = 0;
			c.gridy = 5;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row6, c);
			
			//Row 7 - Messages
			JPanel row7 = new JPanel();
			row7.setLayout(new GridLayout(1, 1));
			row7.add(scrollMessages);
			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 6;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row7, c);
		}
		
		/**
		 * Adds information about the incident to the display.  
		 * @param id the id for the incident to display information about.
		 */
		public void setIncidentId(int id) {
			//Get the incident from the model
			Incident incident = ServiceWolfManager.getInstance().getIncidentById(id);
			if (incident == null) {
				//If the incident doesn't exist for the given id, show an error message
				JOptionPane.showMessageDialog(ServiceWolfGUI.this, "Invalid id");
				cardLayout.show(panel, SERVICE_GROUP_PANEL);
				ServiceWolfGUI.this.repaint();
				ServiceWolfGUI.this.validate();
			} else {
				//Set the border information with the service group name, incident id, and current state
				String serviceGroupName = ServiceWolfManager.getInstance().getServiceGroupName();
				
				Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
				TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, serviceGroupName + " Incident " + incident.getId() + " - " + incident.getState());
				setBorder(border);
				setToolTipText(serviceGroupName + " Incident " + incident.getId() + " - " + incident.getState());
				
				//Set all of the fields with the information
				txtTitle.setText(incident.getTitle());
				txtCaller.setText(incident.getCaller());
				txtreopenCount.setText("" + incident.getReopenCount());
				txtOwner.setText(incident.getOwner());
				txtStatusDetails.setText(incident.getStatusDetails());
				txtMessages.setText(incident.getIncidentLogMessages());
			}
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * allows for creation of a new incident.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class AddIncidentPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		
		/** Label - service group */
		private JLabel lblServiceGroup;
		/** JTextField - service group */
		private JTextField txtServiceGroup;

		/** Label - title */
		private JLabel lblTitle;
		/** JTextField - title */
		private JTextField txtTitle;
		
		/** Label - caller */
		private JLabel lblCaller;
		/** JTextField - caller */
		private JTextField txtCaller;
		
		/** Label - message */
		private JLabel lblMessage;
		/** JTextField - message */
		private JTextField txtMessage;
		
		/** Button to add an incident */
		private JButton btnAdd;
		/** Button for canceling add action and returning to menu */
		private JButton btnReturn;
		
		/**
		 * Creates the JPanel for adding new incidents to the 
		 * manager.
		 */
		public AddIncidentPanel() {
			super(new GridBagLayout());  
			
			//Construct widgets
			lblServiceGroup = new JLabel("Service Group");
			lblTitle = new JLabel("Title");
			lblCaller = new JLabel("Caller");
			lblMessage = new JLabel("Message");
			
			txtServiceGroup = new JTextField(15);
			txtTitle = new JTextField(15);
			txtCaller = new JTextField(15);
			txtMessage = new JTextField(15);
			
			txtServiceGroup.setText(ServiceWolfManager.getInstance().getServiceGroupName());
			txtServiceGroup.setEditable(false);
			
			btnAdd = new JButton("Add to Service Group");
			btnReturn = new JButton("Return without Adding");
			
			//Adds action listeners
			btnAdd.addActionListener(this);
			btnReturn.addActionListener(this);
			
			GridBagConstraints c = new GridBagConstraints();
			
			//Row 0 - Service Group
			JPanel row0 = new JPanel();
			row0.setLayout(new GridLayout(1, 2));
			row0.add(lblServiceGroup);
			row0.add(txtServiceGroup);
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			c.gridheight = 1;
			c.gridwidth = 1;
			add(row0, c);
			
			//Row 1 - Title
			JPanel row1 = new JPanel();
			row1.setLayout(new GridLayout(1, 2));
			row1.add(lblTitle);
			row1.add(txtTitle);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			c.gridheight = 1;
			c.gridwidth = 1;
			add(row1, c);
			
			//Row 2 - Caller
			JPanel row2 = new JPanel();
			row2.setLayout(new GridLayout(1, 2));
			row2.add(lblCaller);
			row2.add(txtCaller);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			c.gridheight = 1;
			c.gridwidth = 1;
			add(row2, c);
			
			//Row 3 - Message
			JPanel row3 = new JPanel();
			row3.setLayout(new GridLayout(1, 2));
			row3.add(lblMessage);
			row3.add(txtMessage);
			
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			c.gridheight = 1;
			c.gridwidth = 1;
			add(row3, c);
						
			//Row 4 - Buttons
			JPanel row4 = new JPanel();
			row4.setLayout(new GridLayout(1, 2));
			row4.add(btnAdd);
			row4.add(btnReturn);
			
			c.gridx = 0;
			c.gridy = 4;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			c.gridheight = GridBagConstraints.REMAINDER;
			c.gridwidth = GridBagConstraints.REMAINDER;
			add(row4, c);
		}
		
		/**
		 * Sets the service group name
		 * @param serviceGroupName name of the service group to display
		 */
		public void setServiceGroupName(String serviceGroupName) {
			txtServiceGroup.setText(serviceGroupName);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true; //Assume done unless error
			if (e.getSource() == btnAdd) {
				String title = txtTitle.getText();
				String caller = txtCaller.getText();
				String action = txtMessage.getText();
				
				//Get instance of model and add incident
				try {
					ServiceWolfManager.getInstance().addIncidentToServiceGroup(title, caller, action);
				} catch (IllegalArgumentException exp) {
					reset = false;
					JOptionPane.showMessageDialog(ServiceWolfGUI.this, exp.getMessage());
				}
			} 
			if (reset) {
				//All buttons lead to back incident list
				cardLayout.show(panel, SERVICE_GROUP_PANEL);
				pnlServiceGroup.updateServiceGroup();
				ServiceWolfGUI.this.repaint();
				ServiceWolfGUI.this.validate();
				//Reset fields
				txtTitle.setText("");
				txtCaller.setText("");
				txtMessage.setText("");
			}
		}
	}
}
