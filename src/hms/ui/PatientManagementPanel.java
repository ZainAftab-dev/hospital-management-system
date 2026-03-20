package hms.ui;

import hms.model.Patient;
import hms.service.PatientService;
import hms.util.ValidationUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class PatientManagementPanel extends JPanel {
    private PatientService patientService;
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton viewButton;
    private JButton refreshButton;

    public PatientManagementPanel() {
        patientService = new PatientService();
        initializeUI();
        loadPatients();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create top panel with search and buttons
        JPanel topPanel = new JPanel(new BorderLayout());

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("Search:");
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addButton = new JButton("Add Patient");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        viewButton = new JButton("View Details");
        refreshButton = new JButton("Refresh");

        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(viewButton);
        buttonsPanel.add(refreshButton);

        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(buttonsPanel, BorderLayout.EAST);

        // Create table
        String[] columns = {"ID", "Name", "Age", "Contact", "Disease"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        patientTable = new JTable(tableModel);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        patientTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(patientTable);

        // Add components to panel
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Add action listeners
        searchButton.addActionListener(e -> searchPatients());
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchPatients();
                }
            }
        });

        addButton.addActionListener(e -> showAddPatientDialog());
        editButton.addActionListener(e -> showEditPatientDialog());
        deleteButton.addActionListener(e -> deletePatient());
        viewButton.addActionListener(e -> viewPatientDetails());
        refreshButton.addActionListener(e -> loadPatients());
    }

    private void loadPatients() {
        // Clear table
        tableModel.setRowCount(0);

        // Load patients from service
        List<Patient> patients = patientService.getAll();

        // Add patients to table
        for (Patient patient : patients) {
            Object[] row = {
                    patient.getId(),
                    patient.getName(),
                    patient.getAge(),
                    patient.getContact(),
                    patient.getDisease()
            };
            tableModel.addRow(row);
        }
    }

    private void searchPatients() {
        String query = searchField.getText().trim();

        // Clear table
        tableModel.setRowCount(0);

        // Search patients
        List<Patient> patients = patientService.search(query);

        // Add patients to table
        for (Patient patient : patients) {
            Object[] row = {
                    patient.getId(),
                    patient.getName(),
                    patient.getAge(),
                    patient.getContact(),
                    patient.getDisease()
            };
            tableModel.addRow(row);
        }
    }

    private void showAddPatientDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Patient", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Generate patient ID
        String patientId = patientService.generatePatientId();

        JLabel idLabel = new JLabel("Patient ID:");
        JTextField idField = new JTextField(patientId);
        idField.setEditable(false);

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();

        JLabel ageLabel = new JLabel("Age:");
        JTextField ageField = new JTextField();

        JLabel contactLabel = new JLabel("Contact:");
        JTextField contactField = new JTextField();

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField();

        JLabel genderLabel = new JLabel("Gender:");
        JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});

        JLabel diseaseLabel = new JLabel("Disease/Symptoms:");
        JTextField diseaseField = new JTextField();

        formPanel.add(idLabel);
        formPanel.add(idField);
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(ageLabel);
        formPanel.add(ageField);
        formPanel.add(contactLabel);
        formPanel.add(contactField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(addressLabel);
        formPanel.add(addressField);
        formPanel.add(genderLabel);
        formPanel.add(genderComboBox);
        formPanel.add(diseaseLabel);
        formPanel.add(diseaseField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        saveButton.addActionListener(e -> {
            try {
                // Validate input
                String name = nameField.getText().trim();
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Name cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int age;
                try {
                    age = Integer.parseInt(ageField.getText().trim());
                    if (age <= 0 || age > 120) {
                        JOptionPane.showMessageDialog(dialog, "Age must be between 1 and 120", "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Age must be a number", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String contact = contactField.getText().trim();
                if (contact.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Contact cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String email = emailField.getText().trim();
                if (!email.isEmpty() && !ValidationUtils.isValidEmail(email)) {
                    JOptionPane.showMessageDialog(dialog, "Invalid email format", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String disease = diseaseField.getText().trim();
                if (disease.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Disease/Symptoms cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Create patient object
                String gender = "Other";
                Object selectedGender = genderComboBox.getSelectedItem();
                if (selectedGender != null) {
                    gender = selectedGender.toString();
                }

                Patient patient = new Patient(
                        idField.getText(),
                        name,
                        age,
                        contact,
                        email,
                        addressField.getText().trim(),
                        gender,
                        "", // Blood group
                        "", // Allergies
                        disease
                );

                // Save patient
                if (patientService.add(patient)) {
                    JOptionPane.showMessageDialog(dialog, "Patient added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadPatients();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to add patient", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void showEditPatientDialog() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to edit", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String patientId = (String) patientTable.getValueAt(selectedRow, 0);
        Patient patient = patientService.getById(patientId);

        if (patient == null) {
            JOptionPane.showMessageDialog(this, "Patient not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Patient", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel idLabel = new JLabel("Patient ID:");
        JTextField idField = new JTextField(patient.getId());
        idField.setEditable(false);

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(patient.getName());

        JLabel ageLabel = new JLabel("Age:");
        JTextField ageField = new JTextField(String.valueOf(patient.getAge()));

        JLabel contactLabel = new JLabel("Contact:");
        JTextField contactField = new JTextField(patient.getContact());

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(patient.getEmail());

        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField(patient.getAddress());

        JLabel genderLabel = new JLabel("Gender:");
        JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderComboBox.setSelectedItem(patient.getGender());

        JLabel diseaseLabel = new JLabel("Disease/Symptoms:");
        JTextField diseaseField = new JTextField(patient.getDisease());

        formPanel.add(idLabel);
        formPanel.add(idField);
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(ageLabel);
        formPanel.add(ageField);
        formPanel.add(contactLabel);
        formPanel.add(contactField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(addressLabel);
        formPanel.add(addressField);
        formPanel.add(genderLabel);
        formPanel.add(genderComboBox);
        formPanel.add(diseaseLabel);
        formPanel.add(diseaseField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        saveButton.addActionListener(e -> {
            try {
                // Validate input
                String name = nameField.getText().trim();
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Name cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int age;
                try {
                    age = Integer.parseInt(ageField.getText().trim());
                    if (age <= 0 || age > 120) {
                        JOptionPane.showMessageDialog(dialog, "Age must be between 1 and 120", "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Age must be a number", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String contact = contactField.getText().trim();
                if (contact.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Contact cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String email = emailField.getText().trim();
                if (!email.isEmpty() && !ValidationUtils.isValidEmail(email)) {
                    JOptionPane.showMessageDialog(dialog, "Invalid email format", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String disease = diseaseField.getText().trim();
                if (disease.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Disease/Symptoms cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Update patient object
                String gender = "Other";
                Object selectedGender = genderComboBox.getSelectedItem();
                if (selectedGender != null) {
                    gender = selectedGender.toString();
                }

                patient.setName(name);
                patient.setAge(age);
                patient.setContact(contact);
                patient.setEmail(email);
                patient.setAddress(addressField.getText().trim());
                patient.setGender(gender);
                patient.setDisease(disease);

                // Save patient
                if (patientService.update(patient)) {
                    JOptionPane.showMessageDialog(dialog, "Patient updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadPatients();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to update patient", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void deletePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to delete", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String patientId = (String) patientTable.getValueAt(selectedRow, 0);
        String patientName = (String) patientTable.getValueAt(selectedRow, 1);

        int option = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete patient " + patientName + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (option == JOptionPane.YES_OPTION) {
            if (patientService.delete(patientId)) {
                JOptionPane.showMessageDialog(this, "Patient deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadPatients();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete patient", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewPatientDetails() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to view", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String patientId = (String) patientTable.getValueAt(selectedRow, 0);
        Patient patient = patientService.getById(patientId);

        if (patient == null) {
            JOptionPane.showMessageDialog(this, "Patient not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Patient Details", true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Patient details
        JLabel titleLabel = new JLabel("Patient Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        addDetailRow(infoPanel, "Patient ID:", patient.getId());
        addDetailRow(infoPanel, "Name:", patient.getName());
        addDetailRow(infoPanel, "Age:", String.valueOf(patient.getAge()));
        addDetailRow(infoPanel, "Gender:", patient.getGender());
        addDetailRow(infoPanel, "Contact:", patient.getContact());
        addDetailRow(infoPanel, "Email:", patient.getEmail());
        addDetailRow(infoPanel, "Address:", patient.getAddress());
        addDetailRow(infoPanel, "Blood Group:", patient.getBloodGroup());
        addDetailRow(infoPanel, "Allergies:", patient.getAllergies());
        addDetailRow(infoPanel, "Disease/Symptoms:", patient.getDisease());

        detailsPanel.add(titleLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        detailsPanel.add(infoPanel);

        // Add tabs for medical records, appointments, and billing
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Medical Records", createMedicalRecordsPanel(patient));
        tabbedPane.addTab("Appointments", createAppointmentsPanel(patient));
        tabbedPane.addTab("Billing", createBillingPanel(patient));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Close");
        JButton printButton = new JButton("Print Report");

        buttonPanel.add(printButton);
        buttonPanel.add(closeButton);

        dialog.add(detailsPanel, BorderLayout.NORTH);
        dialog.add(tabbedPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        closeButton.addActionListener(e -> dialog.dispose());
        printButton.addActionListener(e -> generatePatientReport(patient));

        dialog.setVisible(true);
    }

    private void addDetailRow(JPanel panel, String label, String value) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Arial", Font.BOLD, 12));

        JLabel valueComponent = new JLabel(value != null && !value.isEmpty() ? value : "N/A");
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 12));

        panel.add(labelComponent);
        panel.add(valueComponent);
    }

    private JPanel createMedicalRecordsPanel(Patient patient) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create table for medical records
        String[] columns = {"Record ID", "Date", "Doctor", "Diagnosis", "Treatment"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add Record");
        JButton viewButton = new JButton("View Record");

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        addButton.addActionListener(e -> {
            // Show dialog to add medical record
            JOptionPane.showMessageDialog(panel, "Add Medical Record functionality will be implemented here", "Information", JOptionPane.INFORMATION_MESSAGE);
        });

        viewButton.addActionListener(e -> {
            // Show dialog to view medical record details
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(panel, "Please select a record to view", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(panel, "View Medical Record functionality will be implemented here", "Information", JOptionPane.INFORMATION_MESSAGE);
        });

        return panel;
    }

    private JPanel createAppointmentsPanel(Patient patient) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create table for appointments
        String[] columns = {"Appointment ID", "Date", "Time", "Doctor", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Book Appointment");
        JButton viewButton = new JButton("View Details");

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        addButton.addActionListener(e -> {
            // Show dialog to book appointment
            JOptionPane.showMessageDialog(panel, "Book Appointment functionality will be implemented here", "Information", JOptionPane.INFORMATION_MESSAGE);
        });

        viewButton.addActionListener(e -> {
            // Show dialog to view appointment details
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(panel, "Please select an appointment to view", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(panel, "View Appointment functionality will be implemented here", "Information", JOptionPane.INFORMATION_MESSAGE);
        });

        return panel;
    }

    private JPanel createBillingPanel(Patient patient) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create table for billing
        String[] columns = {"Bill ID", "Date", "Amount", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Generate Bill");
        JButton viewButton = new JButton("View Bill");

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        addButton.addActionListener(e -> {
            // Show dialog to generate bill
            JOptionPane.showMessageDialog(panel, "Generate Bill functionality will be implemented here", "Information", JOptionPane.INFORMATION_MESSAGE);
        });

        viewButton.addActionListener(e -> {
            // Show dialog to view bill details
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(panel, "Please select a bill to view", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(panel, "View Bill functionality will be implemented here", "Information", JOptionPane.INFORMATION_MESSAGE);
        });

        return panel;
    }

    private void generatePatientReport(Patient patient) {
        try {
            String outputPath = "reports/patient_" + patient.getId() + ".pdf";
            java.io.File file = hms.util.PDFGenerator.generatePatientReport(patient, null, outputPath);

            if (file != null && file.exists()) {
                int option = JOptionPane.showConfirmDialog(
                        this,
                        "Patient report generated successfully. Do you want to open it?",
                        "Report Generated",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE
                );

                if (option == JOptionPane.YES_OPTION) {
                    // Open the PDF file with the default system viewer
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(file);
                    } else {
                        JOptionPane.showMessageDialog(this, "Cannot open PDF file automatically. File saved at: " + file.getAbsolutePath(), "Information", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to generate patient report", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generating report: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
