package ui;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.*;
import service.UserService;
import utils.UIUtil;

public class RegisterPage extends JFrame {
    private final Color PRIMARY_COLOR = new Color(30, 144, 255);
    private final Color TEXT_COLOR = new Color(50, 50, 50);
    private final Font HEADER_FONT = new Font("Arial", Font.BOLD, 24);
    private final Font LABEL_FONT = new Font("Arial", Font.BOLD, 14);
    private final Font INPUT_FONT = new Font("Arial", Font.PLAIN, 14);
    
    private JTextField firstNameField, lastNameField, emailField, phoneField;
    private JPasswordField passwordField, confirmPasswordField;
    private JTextArea addressArea;
    private JComboBox<String> genderCombo;
    private JSpinner dobSpinner;
    private UserService userService;
    
    public RegisterPage() {
        userService = new UserService();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("TBS - Register");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        
        JPanel headerPanel = createHeaderPanel();
        
        JScrollPane scrollPane = new JScrollPane(createFormPanel());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        
        JButton backButton = new JButton("← Back to Login");
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            new LoginPage();
            dispose();
        });
        
        panel.add(backButton, BorderLayout.WEST);
        panel.add(titleLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 50, 40, 50));
        
        JPanel personalInfoPanel = createFormSection("Personal Information",
            createPersonalInfoFields());
        
        JPanel contactInfoPanel = createFormSection("Contact Information",
            createContactInfoFields());
        
        JPanel securityPanel = createFormSection("Account Security",
            createSecurityFields());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 10, 0));
        
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(PRIMARY_COLOR);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);  
        registerButton.setOpaque(true);          
        registerButton.setPreferredSize(new Dimension(150, 40));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        registerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                registerButton.setBackground(new Color(0, 90, 181));  
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                registerButton.setBackground(PRIMARY_COLOR);  
            }
        });
        
        registerButton.addActionListener(e -> handleRegistration());
        
        buttonPanel.add(registerButton);
        
        panel.add(personalInfoPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(contactInfoPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(securityPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(buttonPanel);
        
        return panel;
    }
    
    private JPanel createFormSection(String title, JPanel content) {
        JPanel section = new JPanel(new BorderLayout(0, 10));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 0, 20, 0)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_COLOR);
        
        section.add(titleLabel, BorderLayout.NORTH);
        section.add(content, BorderLayout.CENTER);
        
        return section;
    }
    
    private JPanel createPersonalInfoFields() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 20, 15));
        panel.setBackground(Color.WHITE);
        
        JPanel firstNamePanel = createInputFieldPanel("First Name *", 
            firstNameField = new JTextField());
        
        JPanel lastNamePanel = createInputFieldPanel("Last Name *", 
            lastNameField = new JTextField());
        
        JPanel dobPanel = new JPanel(new BorderLayout(0, 5));
        dobPanel.setBackground(Color.WHITE);
        JLabel dobLabel = new JLabel("Date of Birth *");
        dobLabel.setFont(LABEL_FONT);
        
        SpinnerDateModel dateModel = new SpinnerDateModel();
        dobSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dobSpinner, "yyyy-MM-dd");
        dobSpinner.setEditor(dateEditor);
        dobSpinner.setValue(getDateYearsAgo(18)); 
        dobSpinner.setFont(INPUT_FONT);
        
        dobPanel.add(dobLabel, BorderLayout.NORTH);
        dobPanel.add(dobSpinner, BorderLayout.CENTER);
        
        JPanel genderPanel = new JPanel(new BorderLayout(0, 5));
        genderPanel.setBackground(Color.WHITE);
        JLabel genderLabel = new JLabel("Gender *");
        genderLabel.setFont(LABEL_FONT);
        
        genderCombo = new JComboBox<>(new String[]{"Select Gender", "M", "F"});
        genderCombo.setFont(INPUT_FONT);
        
        genderPanel.add(genderLabel, BorderLayout.NORTH);
        genderPanel.add(genderCombo, BorderLayout.CENTER);
        
        panel.add(firstNamePanel);
        panel.add(lastNamePanel);
        panel.add(dobPanel);
        panel.add(genderPanel);
        
        return panel;
    }
    
    private JPanel createContactInfoFields() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 20, 15));
        panel.setBackground(Color.WHITE);
        
        JPanel emailPanel = createInputFieldPanel("Email *", 
            emailField = new JTextField());
        
        JPanel phonePanel = createInputFieldPanel("Phone Number *", 
            phoneField = new JTextField());
        
        JPanel addressPanel = new JPanel(new BorderLayout(0, 5));
        addressPanel.setBackground(Color.WHITE);
        JLabel addressLabel = new JLabel("Address");
        addressLabel.setFont(LABEL_FONT);
        
        addressArea = new JTextArea(3, 20);
        addressArea.setFont(INPUT_FONT);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        addressArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        JScrollPane addressScroll = new JScrollPane(addressArea);
        addressScroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        addressPanel.add(addressLabel, BorderLayout.NORTH);
        addressPanel.add(addressScroll, BorderLayout.CENTER);
        
        panel.add(emailPanel);
        panel.add(phonePanel);
        panel.add(addressPanel);
        panel.add(new JPanel()); 
        
        return panel;
    }
    
    private JPanel createSecurityFields() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 15));
        panel.setBackground(Color.WHITE);
        
        JPanel passwordPanel = createInputFieldPanel("Password *", 
            passwordField = new JPasswordField());
        
        JPanel confirmPasswordPanel = createInputFieldPanel("Confirm Password *", 
            confirmPasswordField = new JPasswordField());
        
        panel.add(passwordPanel);
        panel.add(confirmPasswordPanel);
        
        return panel;
    }
    
    private JPanel createInputFieldPanel(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(Color.WHITE);
        
        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);
        
        textField.setFont(INPUT_FONT);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 7, 5, 7)
        ));
        
        panel.add(label, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void handleRegistration() {
        try {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            Date dobDate = (Date) dobSpinner.getValue();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dob = sdf.format(dobDate);
            
            String gender = genderCombo.getSelectedItem().toString();
            if (gender.equals("Select Gender")) {
                UIUtil.showErrorMessage(this, "Please select your gender");
                return;
            }
            
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressArea.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            
            if (!password.equals(confirmPassword)) {
                UIUtil.showErrorMessage(this, "Passwords don't match");
                return;
            }
            
            userService.registerUser(firstName, lastName, dob, gender, email, phone, address, password);
            
            UIUtil.showSuccessMessage(this, "Registration successful! Please log in.");
            new LoginPage();
            dispose();
            
        } catch (Exception e) {
            UIUtil.showErrorMessage(this, "Registration failed: " + e.getMessage());
        }
    }
    
    private Date getDateYearsAgo(int years) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -years);
        return cal.getTime();
    }
}