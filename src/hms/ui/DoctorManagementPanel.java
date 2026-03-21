package hms.ui;

import hms.model.Doctor;
import hms.service.DoctorService;
import hms.util.ValidationUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class DoctorManagementPanel extends JPanel {
    private DoctorService doctorService;
    private JTable doctorTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton viewButton;
    private JButton refreshButton;

    public DoctorManagementPanel() {
        doctorService = new DoctorService();
        initializeUI();
        loadDoctors();
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
        addButton = new JButton("Add Doctor");
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
        String[] columns = {"ID", "Name", "Specialization", "Availability", "Contact"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        doctorTable = new JTable(tableModel);
        doctorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        doctorTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(doctorTable);

        // Add components to panel
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Add action listeners
        searchButton.addActionListener(e -> searchDoctors());
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchDoctors();
                }
            }
        });

        addButton.addActionListener(e -> showAddDoctorDialog());
        editButton.addActionListener(e -> showEditDoctorDialog());
        deleteButton.addActionListener(e -> deleteDoctor());
        viewButton.addActionListener(e -> viewDoctorDetails());
        refreshButton.addActionListener(e -> loadDoctors());
    }

    private void loadDoctors() {
        // Clear table
        tableModel.setRowCount(0);

        // Load doctors from service
        List<Doctor> doctors = doctorService.getAll();

        // Add doctors to table
        for (Doctor doctor : doctors) {
            Object[] row = {
                    doctor.getId(),
                    doctor.getName(),
                    doctor.getSpecialization(),
                    doctor.getAvailability(),
                    doctor.getContact()
            };
            tableModel.addRow(row);
        }
    }

    private void searchDoctors() {
        String query = searchField.getText().trim();

        // Clear table
        tableModel.setRowCount(0);

        // Search doctors
        List<Doctor> doctors = doctorService.search(query);

        // Add doctors to table
        for (Doctor doctor : doctors) {
            Object[] row = {
                    doctor.getId(),
                    doctor.getName(),
                    doctor.getSpecialization(),
                    doctor.getAvailability(),
                    doctor.getContact()
            };
            tableModel.addRow(row);
        }
    }

    private void showAddDoctorDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Doctor", true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(10, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Generate doctor ID
        String doctorId = doctorService.generateDoctorId();

        JLabel idLabel = new JLabel("Doctor ID:");
        JTextField idField = new JTextField(doctorId);
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

        JLabel specializationLabel = new JLabel("Specialization:");
        JTextField specializationField = new JTextField();

        JLabel qualificationLabel = new JLabel("Qualification:");
        JTextField qualificationField = new JTextField();

        JLabel availabilityLabel = new JLabel("Availability:");
        JTextField availabilityField = new JTextField();

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
        formPanel.add(specializationLabel);
        formPanel.add(specializationField);
        formPanel.add(qualificationLabel);
        formPanel.add(qualificationField);
        formPanel.add(availabilityLabel);
        formPanel.add(availabilityField);

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

                String ageText = ageField.getText().trim();
                if (ageText.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Age cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int age;
                try {
                    age = Integer.parseInt(ageText);
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

                String specialization = specializationField.getText().trim();
                if (specialization.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Specialization cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String availability = availabilityField.getText().trim();
                if (availability.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Availability cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Create doctor object
                Doctor doctor = new Doctor(
                        idField.getText(),
                        name,
                        age,
                        contact,
                        email,
                        addressField.getText().trim(),
                        genderComboBox.getSelectedItem().toString(),
                        specialization,
                        qualificationField.getText().trim(),
                        availability,
                        0.0 // Default consultation fee
                );

                // Save doctor
                if (doctorService.add(doctor)) {
                    JOptionPane.showMessageDialog(dialog, "Doctor added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadDoctors();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to add doctor", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void showEditDoctorDialog() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor to edit", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String doctorId = (String) doctorTable.getValueAt(selectedRow, 0);
        Doctor doctor = doctorService.getById(doctorId);

        if (doctor == null) {
            JOptionPane.showMessageDialog(this, "Doctor not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Doctor", true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(10, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel idLabel = new JLabel("Doctor ID:");
        JTextField idField = new JTextField(doctor.getId());
        idField.setEditable(false);

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(doctor.getName());

        JLabel ageLabel = new JLabel("Age:");
        JTextField ageField = new JTextField(String.valueOf(doctor.getAge()));

        JLabel contactLabel = new JLabel("Contact:");
        JTextField contactField = new JTextField(doctor.getContact());

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(doctor.getEmail());

        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField(doctor.getAddress());

        JLabel genderLabel = new JLabel("Gender:");
        JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderComboBox.setSelectedItem(doctor.getGender());

        JLabel specializationLabel = new JLabel("Specialization:");
        JTextField specializationField = new JTextField(doctor.getSpecialization());

        JLabel qualificationLabel = new JLabel("Qualification:");
        JTextField qualificationField = new JTextField(doctor.getQualification());

        JLabel availabilityLabel = new JLabel("Availability:");
        JTextField availabilityField = new JTextField(doctor.getAvailability());

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
        formPanel.add(specializationLabel);
        formPanel.add(specializationField);
        formPanel.add(qualificationLabel);
        formPanel.add(qualificationField);
        formPanel.add(availabilityLabel);
        formPanel.add(availabilityField);

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

                String ageText = ageField.getText().trim();
                if (ageText.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Age cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int age;
                try {
                    age = Integer.parseInt(ageText);
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

                String specialization = specializationField.getText().trim();
                if (specialization.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Specialization cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String availability = availabilityField.getText().trim();
                if (availability.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Availability cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Update doctor object
                doctor.setName(name);
                doctor.setAge(age);
                doctor.setContact(contact);
                doctor.setEmail(email);
                doctor.setAddress(addressField.getText().trim());
                doctor.setGender(genderComboBox.getSelectedItem().toString());
                doctor.setSpecialization(specialization);
                doctor.setQualification(qualificationField.getText().trim());
                doctor.setAvailability(availability);

                // Save doctor
                if (doctorService.update(doctor)) {
                    JOptionPane.showMessageDialog(dialog, "Doctor updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadDoctors();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to update doctor", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void deleteDoctor() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor to delete", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String doctorId = (String) doctorTable.getValueAt(selectedRow, 0);
        String doctorName = (String) doctorTable.getValueAt(selectedRow, 1);

        int option = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete doctor " + doctorName + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (option == JOptionPane.YES_OPTION) {
            if (doctorService.delete(doctorId)) {
                JOptionPane.showMessageDialog(this, "Doctor deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadDoctors();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete doctor", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewDoctorDetails() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor to view", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String doctorId = (String) doctorTable.getValueAt(selectedRow, 0);
        Doctor doctor = doctorService.getById(doctorId);

        if (doctor == null) {
            JOptionPane.showMessageDialog(this, "Doctor not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Doctor Details", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Doctor details
        JLabel titleLabel = new JLabel("Doctor Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        addDetailRow(infoPanel, "Doctor ID:", doctor.getId());
        addDetailRow(infoPanel, "Name:", doctor.getName());
        addDetailRow(infoPanel, "Age:", String.valueOf(doctor.getAge()));
        addDetailRow(infoPanel, "Gender:", doctor.getGender());
        addDetailRow(infoPanel, "Contact:", doctor.getContact());
        addDetailRow(infoPanel, "Email:", doctor.getEmail());
        addDetailRow(infoPanel, "Address:", doctor.getAddress());
        addDetailRow(infoPanel, "Specialization:", doctor.getSpecialization());
        addDetailRow(infoPanel, "Qualification:", doctor.getQualification());
        addDetailRow(infoPanel, "Availability:", doctor.getAvailability());
        addDetailRow(infoPanel, "Consultation Fee:", String.valueOf(doctor.getConsultationFee()));

        detailsPanel.add(titleLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        detailsPanel.add(infoPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Close");

        buttonPanel.add(closeButton);

        dialog.add(detailsPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        closeButton.addActionListener(e -> dialog.dispose());

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
}
