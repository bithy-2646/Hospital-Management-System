package HospitalManagement;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class HospitalManagmentSystem extends JFrame {
    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/hospital_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // CardLayout Components (স্ক্রিন পরিবর্তনের জন্য)
    private CardLayout cardLayout;
    private JPanel mainContainer;

    // ১. Login GUI Components
    private JTextField txtUsername;
    private JPasswordField txtAuthPassword;
    private JButton btnLogin;

    // ২. Registration GUI Components
    private JTextField txtName, txtAge;
    private JComboBox<String> cmbGender, cmbDoctor;
    private JButton btnRegister;

    // ৩. Patient List GUI Components
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnBackToReg, btnRefresh;

    public HospitalManagmentSystem() {
        setTitle("Hospital Management System");
        setSize(850, 500); // সব পেজের জন্য স্ট্যান্ডার্ড সাইজ
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // কার্ড লেআউট এবং মেইন কন্টেইনার সেটআপ
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        // ৩টি আলাদা স্ক্রিন/প্যানেল তৈরি করে কন্টেইনারে অ্যাড করা
        createLoginPanel();
        createRegistrationPanel();
        createListPanel();

        add(mainContainer);

        // প্রথম স্ক্রিন হিসেবে লগইন পেজ দেখানো
        cardLayout.show(mainContainer, "LoginScreen");
    }

    // =========================================================================
    // --- প্রথম ধাপ: সিস্টেম লগইন ইন্টারফেস ---
    // =========================================================================
    private void createLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(240, 244, 248)); // হালকা সুন্দর ব্যাকগ্রাউন্ড
        
        JPanel centerBox = new JPanel(new GridBagLayout());
        centerBox.setBackground(Color.WHITE);
        centerBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 220, 230), 1),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("System Login", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        centerBox.add(lblTitle, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0;
        centerBox.add(new JLabel("Username:"), gbc);

        txtUsername = new JTextField();
        txtUsername.setPreferredSize(new Dimension(160, 28));
        gbc.gridx = 1;
        centerBox.add(txtUsername, gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        centerBox.add(new JLabel("Password:"), gbc);

        txtAuthPassword = new JPasswordField();
        txtAuthPassword.setPreferredSize(new Dimension(160, 28));
        gbc.gridx = 1;
        centerBox.add(txtAuthPassword, gbc);

        btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(0, 123, 255)); 
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 13));
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        centerBox.add(btnLogin, gbc);

        // লগইন বাটনের অ্যাকশন
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = txtUsername.getText();
                String pass = new String(txtAuthPassword.getPassword());

                if (user.equals("admin") && pass.equals("1234")) {
                    // লগইন সফল হলে সরাসরি ২য় স্ক্রিন অর্থাৎ 'রেজিস্ট্রেশন ফর্ম'-এ নিয়ে যাবে
                    cardLayout.show(mainContainer, "RegistrationScreen");
                } else {
                    JOptionPane.showMessageDialog(HospitalManagmentSystem.this, 
                            "ভুল ইউজারনেম বা পাসওয়ার্ড!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loginPanel.add(centerBox);
        mainContainer.add(loginPanel, "LoginScreen");
    }

    // =========================================================================
    // --- দ্বিতীয় ধাপ: পেশেন্ট রেজিস্ট্রেশন ইন্টারফেস ---
    // =========================================================================
    private void createRegistrationPanel() {
        JPanel regWrapperPanel = new JPanel(new GridBagLayout());
        regWrapperPanel.setBackground(new Color(240, 244, 248));

        JPanel panelInput = new JPanel(new GridBagLayout());
        panelInput.setBackground(Color.WHITE);
        
        TitledBorder titledBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Register New Patient");
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        panelInput.setBorder(BorderFactory.createCompoundBorder(titledBorder, BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("Arial", Font.PLAIN, 13);
        Dimension fieldSize = new Dimension(180, 30);

        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblName = new JLabel("Patient Name:");
        lblName.setFont(labelFont);
        panelInput.add(lblName, gbc);
        gbc.gridx = 1;
        txtName = new JTextField();
        txtName.setPreferredSize(fieldSize);
        panelInput.add(txtName, gbc);

        // Age
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblAge = new JLabel("Age:");
        lblAge.setFont(labelFont);
        panelInput.add(lblAge, gbc);
        gbc.gridx = 1;
        txtAge = new JTextField();
        txtAge.setPreferredSize(fieldSize);
        panelInput.add(txtAge, gbc);

        // Gender
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblGender = new JLabel("Gender:");
        lblGender.setFont(labelFont);
        panelInput.add(lblGender, gbc);
        gbc.gridx = 1;
        cmbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        cmbGender.setPreferredSize(fieldSize);
        cmbGender.setBackground(Color.WHITE);
        panelInput.add(cmbGender, gbc);

        // Doctor
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblDoctor = new JLabel("Assigned Doctor:");
        lblDoctor.setFont(labelFont);
        panelInput.add(lblDoctor, gbc);
        gbc.gridx = 1;
        cmbDoctor = new JComboBox<>(new String[]{"1: Dr. S. K. Ray", "2: Dr. Asif Iqbal"});
        cmbDoctor.setPreferredSize(fieldSize);
        cmbDoctor.setBackground(Color.WHITE);
        panelInput.add(cmbDoctor, gbc);

        // সেভ/রেজিস্টার বাটন
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 8, 10, 8);

        btnRegister = new JButton("Save & Register");
        btnRegister.setBackground(new Color(40, 167, 69)); // সুন্দর ফ্ল্যাট সবুজ বাটন
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setFont(new Font("Arial", Font.BOLD, 13));
        btnRegister.setPreferredSize(new Dimension(150, 38));
        panelInput.add(btnRegister, gbc);

        // সেভ বাটনের অ্যাকশন লিসেনার
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = txtName.getText();
                String ageStr = txtAge.getText();
                String gender = (String) cmbGender.getSelectedItem();
                String doctorInfo = (String) cmbDoctor.getSelectedItem();

                if (name.isEmpty() || ageStr.isEmpty()) {
                    JOptionPane.showMessageDialog(HospitalManagmentSystem.this, "দয়া করে সব ফিল্ড পূরণ করুন!", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int age = Integer.parseInt(ageStr);
                int doctorId = Integer.parseInt(doctorInfo.split(":")[0]);

                try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                    String sql = "INSERT INTO patients (name, age, gender, doctor_id) VALUES (?, ?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, name);
                    stmt.setInt(2, age);
                    stmt.setString(3, gender);
                    stmt.setInt(4, doctorId);
                    stmt.executeUpdate();

                    JOptionPane.showMessageDialog(HospitalManagmentSystem.this, "পেশেন্ট সফলভাবে রেজিস্টার্ড হয়েছে!");
                    
                    // ইনপুট বক্স খালি করা (যাতে পরের বার ফাঁকা থাকে)
                    txtName.setText("");
                    txtAge.setText("");
                    
                    // ডাটাবেজ থেকে নতুন ডাটা রিফ্রেশ করা
                    loadPatientData();
                    
                    // সেভ হওয়ার সাথে সাথে অটোমেটিক ৩ নম্বর স্ক্রিন 'পেশেন্ট লিস্ট'-এ রিডাইরেক্ট করবে
                    cardLayout.show(mainContainer, "ListScreen");
                    
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(HospitalManagmentSystem.this, "ডাটাবেজ এরর: " + ex.getMessage());
                }
            }
        });

        regWrapperPanel.add(panelInput);
        mainContainer.add(regWrapperPanel, "RegistrationScreen");
    }

    // =========================================================================
    // --- তৃতীয় ধাপ: কারেন্ট পেশেন্ট লিস্ট ইন্টারফেস ---
    // =========================================================================
    private void createListPanel() {
        JPanel listPanel = new JPanel(new BorderLayout(15, 15));
        listPanel.setBackground(new Color(240, 244, 248));
        listPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        TitledBorder tableBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Current Patients List");
        tableBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        
        JPanel panelTableWrapper = new JPanel(new BorderLayout());
        panelTableWrapper.setBackground(Color.WHITE);
        panelTableWrapper.setBorder(BorderFactory.createCompoundBorder(tableBorder, BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        String[] columns = {"ID", "Name", "Age", "Gender", "Doctor"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(230, 235, 240));
        
        JScrollPane scrollPane = new JScrollPane(table);
        panelTableWrapper.add(scrollPane, BorderLayout.CENTER);

        // বটম বাটন প্যানেল
        JPanel panelBottom = new JPanel(new BorderLayout());
        panelBottom.setOpaque(false);

        btnBackToReg = new JButton("<- Register Another Patient");
        btnBackToReg.setBackground(new Color(108, 117, 125)); // ধূসর বাটন
        btnBackToReg.setForeground(Color.WHITE);
        btnBackToReg.setFocusPainted(false);
        btnBackToReg.setPreferredSize(new Dimension(200, 35));

        btnRefresh = new JButton("Refresh Table");
        btnRefresh.setBackground(new Color(0, 123, 255)); // নীল বাটন
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setPreferredSize(new Dimension(120, 35));

        panelBottom.add(btnBackToReg, BorderLayout.WEST);
        panelBottom.add(btnRefresh, BorderLayout.EAST);

        // বাটন একশনস
        btnBackToReg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // আবার নতুন পেশেন্ট এন্ট্রি করতে চাইলে ২ নম্বর রেজিস্ট্রেশন স্ক্রিনে ব্যাক করবে
                cardLayout.show(mainContainer, "RegistrationScreen");
            }
        });

        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPatientData();
            }
        });

        listPanel.add(panelTableWrapper, BorderLayout.CENTER);
        listPanel.add(panelBottom, BorderLayout.SOUTH);

        mainContainer.add(listPanel, "ListScreen");
    }

    // =========================================================================
    // --- ডাটাবেজ থেকে টেবিল ডাটা লোড করার মেথড ---
    // =========================================================================
    private void loadPatientData() {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT p.id, p.name, p.age, p.gender, d.name AS doctor_name " +
                         "FROM patients p JOIN doctors d ON p.doctor_id = d.id";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("doctor_name")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HospitalManagmentSystem().setVisible(true);
            }
        });
    }
}