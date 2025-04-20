package ui;

import exception.AuthenticationException;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.Customer;
import model.User;
import service.UserService;
import utils.UIUtil;

public class LoginPage extends JFrame {
    private final Color PRIMARY_COLOR = new Color(30, 144, 255);
    private final Color TEXT_COLOR = new Color(50, 50, 50);
    private final Font HEADER_FONT = new Font("Arial", Font.BOLD, 24);
    private final Font LABEL_FONT = new Font("Arial", Font.BOLD, 14);
    private final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginBtn;
    private JButton registerBtn;
    private UserService userService;
    
    public LoginPage() {
        userService = new UserService();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("TBS - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        
        JPanel imagePanel = createImagePanel();
        
        JPanel loginPanel = createLoginPanel();
        
        mainPanel.add(imagePanel);
        mainPanel.add(loginPanel);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private JPanel createImagePanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                Color color1 = new Color(30, 144, 255);
                Color color2 = new Color(0, 65, 106);
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel("Travel Booking System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subTitleLabel = new JLabel("Your gateway to unforgettable journeys");
        subTitleLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        subTitleLabel.setForeground(Color.WHITE);
        subTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(Box.createVerticalGlue());
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(subTitleLabel);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(50, 40, 50, 40));
        
        JLabel headerLabel = new JLabel("Sign In");
        headerLabel.setFont(HEADER_FONT);
        headerLabel.setForeground(TEXT_COLOR);
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 1, 0, 20));
        formPanel.setBackground(Color.WHITE);
        formPanel.setMaximumSize(new Dimension(400, 200));
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel emailPanel = new JPanel(new BorderLayout());
        emailPanel.setBackground(Color.WHITE);
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(LABEL_FONT);
        emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY),
            BorderFactory.createEmptyBorder(5, 0, 5, 0)
        ));
        emailPanel.add(emailLabel, BorderLayout.NORTH);
        emailPanel.add(emailField, BorderLayout.CENTER);
        
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setBackground(Color.WHITE);
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(LABEL_FONT);
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY),
            BorderFactory.createEmptyBorder(5, 0, 5, 0)
        ));
        passwordPanel.add(passwordLabel, BorderLayout.NORTH);
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(Color.WHITE);
        
        loginBtn = new JButton("Login");
        loginBtn.setFont(BUTTON_FONT);
        loginBtn.setBackground(PRIMARY_COLOR);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setOpaque(true);
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(400, 40));
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        loginBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginBtn.setBackground(new Color(0, 90, 181));  
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginBtn.setBackground(PRIMARY_COLOR);
            }
        });
        
        registerBtn = new JButton("Create an Account");
        registerBtn.setFont(new Font("Arial", Font.BOLD, 12));
        registerBtn.setBackground(new Color(230, 230, 230));
        registerBtn.setForeground(PRIMARY_COLOR);
        registerBtn.setBorderPainted(false);
        registerBtn.setFocusPainted(false);
        registerBtn.setOpaque(true);
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerBtn.setMaximumSize(new Dimension(400, 30));
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        registerBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                registerBtn.setBackground(new Color(220, 220, 220));
                registerBtn.setForeground(new Color(0, 70, 140));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                registerBtn.setBackground(new Color(230, 230, 230));
                registerBtn.setForeground(PRIMARY_COLOR);
            }
        });
        
        buttonsPanel.add(loginBtn);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonsPanel.add(registerBtn);
        
        formPanel.add(emailPanel);
        formPanel.add(passwordPanel);
        formPanel.add(buttonsPanel);
        
        panel.add(headerLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));
        panel.add(formPanel);
        panel.add(Box.createVerticalGlue());
        
        loginBtn.addActionListener(e -> handleLogin());
        registerBtn.addActionListener(e -> {
            new RegisterPage();
            dispose();
        });
        
        return panel;
    }
    
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (email.isEmpty() || password.isEmpty()) {
            UIUtil.showErrorMessage(this, "Please enter both email and password");
            return;
        }
        
        try {
            User user = userService.authenticateUser(email, password);
            
            if (user instanceof Customer) {
                new HomePage((Customer) user);
                dispose();
            } else {
                UIUtil.showErrorMessage(this, "Unknown user type");
            }
        } catch (AuthenticationException e) {
            UIUtil.showErrorMessage(this, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            UIUtil.showErrorMessage(this, "Error during login: " + e.getMessage());
        }
    }
}