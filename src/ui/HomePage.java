package ui;

import db.DBConnection;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import model.Customer;
import model.Package;

public class HomePage extends JFrame {
    private Customer customer;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private final Color PRIMARY_COLOR = new Color(30, 144, 255);  
    private final Color SECONDARY_COLOR = new Color(240, 248, 255);  
    private final Color ACCENT_COLOR = new Color(255, 140, 0);  
    private final Font HEADING_FONT = new Font("Arial", Font.BOLD, 18);
    private final Font REGULAR_FONT = new Font("Arial", Font.PLAIN, 14);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
    
    public HomePage(Customer customer) {
        this.customer = customer;
        setTitle("Welcome, " + customer.getName());
        setLayout(new BorderLayout(10, 10));
        
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel packagesPanel = createPackagesPanel();
        mainPanel.add(packagesPanel, "PACKAGES");
        
        JPanel bookingsPanel = createBookingsPanel();
        mainPanel.add(bookingsPanel, "BOOKINGS");
        
        add(mainPanel, BorderLayout.CENTER);
        
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
        
        cardLayout.show(mainPanel, "PACKAGES");
        
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel welcomeLabel = new JLabel("Welcome to Travel Booking System");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        welcomeLabel.setForeground(Color.WHITE);
        
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        navPanel.setOpaque(false);
        
        JButton myBookingsBtn = new JButton("My Bookings");
        myBookingsBtn.setBackground(new Color(0, 102, 204));
        myBookingsBtn.setForeground(Color.WHITE);
        myBookingsBtn.setFont(new Font("Arial", Font.BOLD, 12));
        myBookingsBtn.setOpaque(true);
        myBookingsBtn.setBorderPainted(false);
        myBookingsBtn.setFocusPainted(false);
        myBookingsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        myBookingsBtn.addActionListener(e -> {
            mainPanel.remove(mainPanel.getComponent(1)); 
            JPanel newBookingsPanel = createBookingsPanel();
            mainPanel.add(newBookingsPanel, "BOOKINGS");
            cardLayout.show(mainPanel, "BOOKINGS");
        });
        
        myBookingsBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                myBookingsBtn.setBackground(new Color(0, 70, 140));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                myBookingsBtn.setBackground(new Color(0, 102, 204));
            }
        });
        
        JButton homeBtn = new JButton("Home");
        homeBtn.setBackground(new Color(0, 102, 204));
        homeBtn.setForeground(Color.WHITE);
        homeBtn.setFont(new Font("Arial", Font.BOLD, 12));
        homeBtn.setOpaque(true);
        homeBtn.setBorderPainted(false);
        homeBtn.setFocusPainted(false);
        homeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeBtn.addActionListener(e -> cardLayout.show(mainPanel, "PACKAGES"));
        
        homeBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                homeBtn.setBackground(new Color(0, 70, 140));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                homeBtn.setBackground(new Color(0, 102, 204));
            }
        });
        
        JLabel userLabel = new JLabel("Logged in as: " + customer.getName());
        userLabel.setFont(REGULAR_FONT);
        userLabel.setForeground(Color.WHITE);
        
        navPanel.add(homeBtn);
        navPanel.add(myBookingsBtn);
        navPanel.add(userLabel);
        
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(navPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createPackagesPanel() {
        JPanel wrapperPanel = new JPanel(new BorderLayout(10, 10));
        wrapperPanel.setBackground(SECONDARY_COLOR);
        wrapperPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Available Travel Packages");
        titleLabel.setFont(HEADING_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        
        JPanel packagesContainer = new JPanel();
        packagesContainer.setLayout(new BoxLayout(packagesContainer, BoxLayout.Y_AXIS));
        packagesContainer.setBackground(SECONDARY_COLOR);
        
        List<Package> packages = getPackagesFromDatabase();
        
        for (Package p : packages) {
            JPanel packageCard = createPackageCard(p);
            packagesContainer.add(packageCard);
            packagesContainer.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        
        JScrollPane scrollPane = new JScrollPane(packagesContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        wrapperPanel.add(titleLabel, BorderLayout.NORTH);
        wrapperPanel.add(scrollPane, BorderLayout.CENTER);
        
        return wrapperPanel;
    }
    
    private JPanel createPackageCard(Package p) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(p.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(PRIMARY_COLOR);
        
        JLabel descLabel = new JLabel("Destination: " + p.getDescription());
        descLabel.setFont(REGULAR_FONT);
        
        JLabel priceLabel = new JLabel(String.format("Price: ₹%.2f", p.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        priceLabel.setForeground(ACCENT_COLOR);
        
        detailsPanel.add(nameLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detailsPanel.add(descLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detailsPanel.add(priceLabel);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton detailsButton = new JButton("View Details");
        detailsButton.setFont(REGULAR_FONT);
        detailsButton.setBackground(PRIMARY_COLOR);
        detailsButton.setForeground(Color.WHITE);
        detailsButton.setOpaque(true);
        detailsButton.setFocusPainted(false);
        detailsButton.setBorderPainted(false);
        detailsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        
        detailsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                detailsButton.setBackground(new Color(0, 90, 181)); 
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                detailsButton.setBackground(PRIMARY_COLOR);
            }
        });
        
        detailsButton.addActionListener(e -> {
            JPanel detailView = createPackageDetailView(p);
            mainPanel.add(detailView, "DETAIL_" + p.getId());
            cardLayout.show(mainPanel, "DETAIL_" + p.getId());
        });
        
        buttonPanel.add(detailsButton);
        
        card.add(detailsPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);
        
        return card;
    }
    
    private JPanel createPackageDetailView(Package p) {
        JPanel detailPanel = new JPanel(new BorderLayout(20, 20));
        detailPanel.setBackground(SECONDARY_COLOR);
        detailPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        JButton backButton = new JButton("← Back to Packages");
        backButton.setFont(REGULAR_FONT);
        backButton.setForeground(new Color(0, 102, 204));
        backButton.setBackground(SECONDARY_COLOR);
        backButton.setOpaque(false);  
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setText("<html><u>← Back to Packages</u></html>");
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setText("← Back to Packages");
            }
        });
        
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "PACKAGES"));
        
        JLabel packageTitle = new JLabel(p.getName());
        packageTitle.setFont(new Font("Arial", Font.BOLD, 24));
        packageTitle.setForeground(PRIMARY_COLOR);
        packageTitle.setAlignmentX(Component.LEFT_ALIGNMENT); 
        
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(packageTitle, BorderLayout.SOUTH);
        
        
        JSplitPane contentPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        contentPane.setDividerLocation(600);
        contentPane.setEnabled(false); 
        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        leftPanel.setAlignmentX(Component.LEFT_ALIGNMENT); 
        
        PackageDetails details = getPackageDetails(p.getId());
        
        JPanel destinationSection = createDetailSection("Destination", p.getDescription());
        JPanel dateSection = createDetailSection("Travel Period", 
            dateFormat.format(details.startDate) + " to " + dateFormat.format(details.endDate) + 
            " (" + details.duration + " days)");
        JPanel hotelSection = createDetailSection("Accommodation", 
            details.hotelName + " - " + details.hotelLocation + 
            (details.mealsProvided ? " (Meals Included)" : ""));
        JPanel transportSection = createDetailSection("Transportation", 
            details.transportType + " - " + details.transportName + 
            "\nDeparture: " + details.departureLocation + " at " + details.departureTime +
            "\nArrival: " + details.arrivalLocation + " at " + details.arrivalTime);
        JPanel internationalSection = details.isInternational ? 
            createDetailSection("International Travel", 
                "VISA Required: " + (details.visaRequired ? "Yes" : "No") +
                "\nEmbassy Contact: " + details.embassyContact) : null;
        
        JPanel priceSection = createDetailSection("Price Details", 
            String.format("Package Price: ₹%.2f\nIncluded: Accommodation, Transportation", p.getPrice()));
        
        leftPanel.add(destinationSection);
        leftPanel.add(dateSection);
        leftPanel.add(hotelSection);
        leftPanel.add(transportSection);
        if (internationalSection != null) {
            leftPanel.add(internationalSection);
        }
        leftPanel.add(priceSection);
        
        leftPanel.add(Box.createVerticalGlue());
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(new Color(245, 245, 245));
        rightPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel bookingTitle = new JLabel("Book this Package");
        bookingTitle.setFont(new Font("Arial", Font.BOLD, 20));
        bookingTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pricePanel.setOpaque(false);
        
        JLabel priceLabel = new JLabel(String.format("₹%.2f", p.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 24));
        priceLabel.setForeground(ACCENT_COLOR);
        
        pricePanel.add(priceLabel);
        
        JButton bookButton = new JButton("Book Now");
        bookButton.setFont(new Font("Arial", Font.BOLD, 16));
        bookButton.setBackground(new Color(0, 102, 204));
        bookButton.setForeground(Color.WHITE);
        bookButton.setOpaque(true);
        bookButton.setBorderPainted(false);
        bookButton.setFocusPainted(false);
        bookButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        bookButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bookButton.setBackground(new Color(0, 70, 140));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bookButton.setBackground(new Color(0, 102, 204));
            }
        });
        
        bookButton.addActionListener(e -> bookPackage(p));
        
        rightPanel.add(bookingTitle);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        rightPanel.add(pricePanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        rightPanel.add(bookButton);
        
        JScrollPane leftScrollPane = new JScrollPane(leftPanel);
        leftScrollPane.setBorder(null);
        leftScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        contentPane.setLeftComponent(leftScrollPane);
        contentPane.setRightComponent(rightPanel);
        
        detailPanel.add(topPanel, BorderLayout.NORTH);
        detailPanel.add(contentPane, BorderLayout.CENTER);
        
        return detailPanel;
    }
    
    private JPanel createDetailSection(String title, String content) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(new EmptyBorder(10, 0, 20, 0));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT); 
        
        JTextArea contentArea = new JTextArea(content);
        contentArea.setFont(REGULAR_FONT);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setOpaque(false);
        contentArea.setBorder(null);
        contentArea.setAlignmentX(Component.LEFT_ALIGNMENT); 
        
        section.add(titleLabel);
        section.add(Box.createRigidArea(new Dimension(0, 5)));
        section.add(contentArea);
        
        return section;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(PRIMARY_COLOR);
        footerPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        JLabel copyrightLabel = new JLabel("© 2025 Travel Booking System");
        copyrightLabel.setForeground(Color.WHITE);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(0, 102, 204));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.setOpaque(true);  
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setBorderPainted(false);
        
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(0, 70, 140)); 
                logoutButton.setForeground(new Color(180, 0, 0)); 
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(0, 102, 204));
                logoutButton.setForeground(Color.WHITE);
            }
        });
        
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginPage();
        });
        
        footerPanel.add(copyrightLabel, BorderLayout.WEST);
        footerPanel.add(logoutButton, BorderLayout.EAST);
        
        return footerPanel;
    }
    
    private JPanel createBookingsPanel() {
        JPanel wrapperPanel = new JPanel(new BorderLayout(10, 10));
        wrapperPanel.setBackground(SECONDARY_COLOR);
        wrapperPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("My Bookings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        
        JPanel bookingsContainer = new JPanel();
        bookingsContainer.setLayout(new BoxLayout(bookingsContainer, BoxLayout.Y_AXIS));
        bookingsContainer.setBackground(SECONDARY_COLOR);
        
        List<BookingInfo> bookings = getUserBookings();
        
        if (bookings.isEmpty()) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setBackground(Color.WHITE);
            emptyPanel.setBorder(new EmptyBorder(50, 20, 50, 20));
            
            JLabel emptyLabel = new JLabel("You don't have any bookings yet");
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            emptyLabel.setForeground(Color.GRAY);
            
            emptyPanel.add(emptyLabel);
            bookingsContainer.add(emptyPanel);
        } else {
            for (BookingInfo booking : bookings) {
                JPanel bookingCard = createBookingCard(booking);
                bookingsContainer.add(bookingCard);
                bookingsContainer.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(bookingsContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        wrapperPanel.add(titleLabel, BorderLayout.NORTH);
        wrapperPanel.add(scrollPane, BorderLayout.CENTER);
        
        return wrapperPanel;
    }
    
    private JPanel createBookingCard(BookingInfo booking) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.WHITE);
        
        JLabel bookingIdLabel = new JLabel("Booking ID: #" + booking.bookingId);
        bookingIdLabel.setFont(new Font("Arial", Font.BOLD, 16));
        bookingIdLabel.setForeground(PRIMARY_COLOR);
        
        JLabel packageLabel = new JLabel("Package: " + booking.packageName);
        packageLabel.setFont(REGULAR_FONT);
        
        JLabel destinationLabel = new JLabel("Destination: " + booking.destination);
        destinationLabel.setFont(REGULAR_FONT);
        
        JLabel dateLabel = new JLabel("Travel Period: " + booking.travelDates);
        dateLabel.setFont(REGULAR_FONT);
        
        JLabel priceLabel = new JLabel(String.format("Price: ₹%.2f", booking.price));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        priceLabel.setForeground(ACCENT_COLOR);
        
        JLabel statusLabel = new JLabel("Status: " + (booking.status ? "Confirmed" : "Cancelled"));
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setForeground(booking.status ? new Color(0, 128, 0) : Color.RED);
        
        detailsPanel.add(bookingIdLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        detailsPanel.add(packageLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detailsPanel.add(destinationLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detailsPanel.add(dateLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detailsPanel.add(priceLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        detailsPanel.add(statusLabel);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        if (booking.status) { 
            JButton cancelButton = new JButton("Cancel Booking");
            cancelButton.setFont(REGULAR_FONT);
            cancelButton.setBackground(new Color(204, 0, 0));
            cancelButton.setForeground(Color.WHITE);
            cancelButton.setOpaque(true);
            cancelButton.setBorderPainted(false);
            cancelButton.setFocusPainted(false);
            cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    cancelButton.setBackground(new Color(153, 0, 0));
                }
                
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    cancelButton.setBackground(new Color(204, 0, 0));
                }
            });
            
            cancelButton.addActionListener(e -> cancelBooking(booking.bookingId));
            buttonPanel.add(cancelButton);
        }
        
        card.add(detailsPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);
        
        return card;
    }
    
    private class BookingInfo {
        int bookingId;
        int packageId;
        String packageName;
        String destination;
        String travelDates;
        double price;
        boolean status; 
    }
    
    private List<BookingInfo> getUserBookings() {
        List<BookingInfo> bookings = new ArrayList<>();
        
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "SELECT b.BookingID, b.BookingStatus, b.PackageID, p.PackageDescription, " +
                "p.Price, p.StartDate, p.EndDate, d.DestinationName " +
                "FROM Booking b " +
                "JOIN Users_Booking ub ON b.BookingID = ub.BookingID " +
                "JOIN Packages p ON b.PackageID = p.PackageID " +
                "LEFT JOIN PackageInfo pi ON p.PackageID = pi.PackageID " +
                "LEFT JOIN Destinations d ON pi.DestinationID = d.DestinationID " +
                "WHERE ub.UserID = ? " +
                "ORDER BY b.BookingID DESC"
            );
            ps.setInt(1, customer.getId());
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                BookingInfo info = new BookingInfo();
                info.bookingId = rs.getInt("BookingID");
                info.packageId = rs.getInt("PackageID");
                info.packageName = rs.getString("PackageDescription");
                
                String destination = rs.getString("DestinationName");
                info.destination = (destination != null) ? destination : "Unknown Destination";
                
                info.price = rs.getDouble("Price");
                info.status = rs.getInt("BookingStatus") == 1;
                
                Date startDate = rs.getDate("StartDate");
                Date endDate = rs.getDate("EndDate");
                
                if (startDate != null && endDate != null) {
                    info.travelDates = dateFormat.format(startDate) + " to " + dateFormat.format(endDate);
                } else {
                    info.travelDates = "Dates not available";
                }
                
                bookings.add(info);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading bookings: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        
        return bookings;
    }
    
    private void cancelBooking(int bookingId) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to cancel this booking?\nThis action cannot be undone.",
            "Confirm Cancellation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "UPDATE Booking SET BookingStatus = 0 WHERE BookingID = ?"
            );
            ps.setInt(1, bookingId);
            int updated = ps.executeUpdate();
            
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, 
                    "Booking #" + bookingId + " has been cancelled successfully.",
                    "Booking Cancelled", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                JOptionPane.showMessageDialog(this,
                    "Refund has been initiated for cancelled booking.",
                    "Refund Initiated",
                    JOptionPane.INFORMATION_MESSAGE);
                
                mainPanel.remove(mainPanel.getComponent(1)); 
                JPanel newBookingsPanel = createBookingsPanel();
                mainPanel.add(newBookingsPanel, "BOOKINGS");
                cardLayout.show(mainPanel, "BOOKINGS");
            } else {
                JOptionPane.showMessageDialog(this,
                    "Could not cancel booking. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error cancelling booking: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private List<Package> getPackagesFromDatabase() {
        List<Package> packages = new ArrayList<>();
        
        try (Connection con = DBConnection.getConnection()) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Packages");
            
            while (rs.next()) {
                int id = rs.getInt("PackageID");
                String name = rs.getString("PackageDescription");
                String desc = GetDestinationName(con, id);
                double price = rs.getDouble("Price");
                
                Package p = new Package(id, name, desc, price);
                packages.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading packages: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        
        return packages;
    }
    
    private void bookPackage(Package p) {
        try (Connection con = DBConnection.getConnection()) {
            int paymentId = getNextPaymentID(con);
            
            PreparedStatement psPayment = con.prepareStatement(
                "INSERT INTO Payment (PaymentID, Amount, PaymentStatus) VALUES (?, ?, ?)");
            psPayment.setInt(1, paymentId);
            psPayment.setDouble(2, p.getPrice());
            psPayment.setInt(3, 1); 
            psPayment.executeUpdate();
            
            int bookingId = getNextBookingID(con);
            
            PreparedStatement psBooking = con.prepareStatement(
                "INSERT INTO Booking (BookingID, BookingStatus, PackageID, PaymentID) VALUES (?, ?, ?, ?)");
            psBooking.setInt(1, bookingId);
            psBooking.setInt(2, 1); 
            psBooking.setInt(3, p.getId());
            psBooking.setInt(4, paymentId);
            psBooking.executeUpdate();
            
            PreparedStatement psUserBooking = con.prepareStatement(
                "INSERT INTO Users_Booking (UserID, BookingID) VALUES (?, ?)");
            psUserBooking.setInt(1, customer.getId());
            psUserBooking.setInt(2, bookingId);
            psUserBooking.executeUpdate();
            
            JOptionPane.showMessageDialog(this, 
                "<html><h2>Booking Confirmed!</h2><p>Your booking for <b>" + p.getName() + "</b> has been confirmed.</p>" +
                "<p>Booking ID: <b>" + bookingId + "</b></p>" +
                "<p>Thank you for choosing our services!</p></html>", 
                "Booking Successful", JOptionPane.INFORMATION_MESSAGE);
            
            mainPanel.remove(mainPanel.getComponent(1)); 
            JPanel newBookingsPanel = createBookingsPanel();
            mainPanel.add(newBookingsPanel, "BOOKINGS");    
                
            cardLayout.show(mainPanel, "PACKAGES");
            
        } catch (Exception ex) { 
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error booking package: " + ex.getMessage(), 
                "Booking Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private class PackageDetails {
        java.util.Date startDate, endDate;
        int duration;
        String hotelName, hotelLocation;
        boolean mealsProvided, acAvailable;
        String transportType, transportName;
        String departureLocation, arrivalLocation;
        String departureTime, arrivalTime;
        boolean isInternational, visaRequired;
        String embassyContact;
    }
    
    private PackageDetails getPackageDetails(int packageId) {
        PackageDetails details = new PackageDetails();
        
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement psPackage = con.prepareStatement(
                "SELECT StartDate, EndDate FROM Packages WHERE PackageID = ?");
            psPackage.setInt(1, packageId);
            ResultSet rsPackage = psPackage.executeQuery();
            
            if (rsPackage.next()) {
                details.startDate = rsPackage.getDate("StartDate");
                details.endDate = rsPackage.getDate("EndDate");
                
                long diff = details.endDate.getTime() - details.startDate.getTime();
                details.duration = (int) (diff / (1000 * 60 * 60 * 24)) + 1; 
            }
            
            PreparedStatement psInfo = con.prepareStatement(
                "SELECT pi.DestinationID, h.HotelName, h.HotelLocation, h.areMealsProvided, h.ACAvailability, " +
                "t.TransportID, t.ArrivalTime, t.ArrivalLocation, t.DepartureTime, t.DepartureLocation " +
                "FROM PackageInfo pi " +
                "JOIN Hotels h ON pi.HotelID = h.HotelID " +
                "JOIN Transport t ON pi.TransportID = t.TransportID " +
                "WHERE pi.PackageID = ?");
            psInfo.setInt(1, packageId);
            ResultSet rsInfo = psInfo.executeQuery();
            
            if (rsInfo.next()) {
                details.hotelName = rsInfo.getString("HotelName");
                details.hotelLocation = rsInfo.getString("HotelLocation");
                details.mealsProvided = rsInfo.getInt("areMealsProvided") == 1;
                details.acAvailable = rsInfo.getInt("ACAvailability") == 1;
                
                details.arrivalTime = rsInfo.getString("ArrivalTime");
                details.arrivalLocation = rsInfo.getString("ArrivalLocation");
                details.departureTime = rsInfo.getString("DepartureTime");
                details.departureLocation = rsInfo.getString("DepartureLocation");
                
                int destinationId = rsInfo.getInt("DestinationID");
                String transportId = rsInfo.getString("TransportID");
                
                PreparedStatement psDest = con.prepareStatement(
                    "SELECT isInternational FROM Destinations WHERE DestinationID = ?");
                psDest.setInt(1, destinationId);
                ResultSet rsDest = psDest.executeQuery();
                
                if (rsDest.next()) {
                    details.isInternational = rsDest.getInt("isInternational") == 1;
                    
                    if (details.isInternational) {
                        PreparedStatement psInt = con.prepareStatement(
                            "SELECT VISAReqd, EmbassyContact FROM International WHERE DestinationID = ?");
                        psInt.setInt(1, destinationId);
                        ResultSet rsInt = psInt.executeQuery();
                        
                        if (rsInt.next()) {
                            details.visaRequired = rsInt.getInt("VISAReqd") == 1;
                            details.embassyContact = rsInt.getString("EmbassyContact");
                        }
                    }
                }
                
                String[] transportTypes = {"Flight", "Train", "Bus", "Ship"};
                for (String type : transportTypes) {
                    PreparedStatement psTransport = con.prepareStatement(
                        "SELECT * FROM " + type + " WHERE TransportID = ?");
                    psTransport.setString(1, transportId);
                    ResultSet rsTransport = psTransport.executeQuery();
                    
                    if (rsTransport.next()) {
                        details.transportType = type;
                        switch (type) {
                            case "Flight":
                                details.transportName = rsTransport.getString("AirlineName");
                                break;
                            case "Train":
                                details.transportName = rsTransport.getString("TrainName");
                                break;
                            case "Bus":
                                details.transportName = "Bus" + (rsTransport.getInt("is_AC") == 1 ? " (AC)" : "");
                                break;
                            case "Ship":
                                details.transportName = rsTransport.getString("CruiseName");
                                break;
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return details;
    }
    
    private String GetDestinationName(Connection con, int packageId) {
        try {
            PreparedStatement ps = con.prepareStatement(
                "SELECT d.DestinationName FROM Destinations d " +
                "JOIN PackageInfo pi ON d.DestinationID = pi.DestinationID " +
                "WHERE pi.PackageID = ?");
            ps.setInt(1, packageId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("DestinationName");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown Destination";
    }
    
    private int getNextPaymentID(Connection con) throws SQLException {
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT MAX(PaymentID) as MaxID FROM Payment");
        if (rs.next()) {
            return rs.getInt("MaxID") + 1;
        }
        return 2600; 
    }
    
    private int getNextBookingID(Connection con) throws SQLException {
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT MAX(BookingID) as MaxID FROM Booking");
        if (rs.next()) {
            return rs.getInt("MaxID") + 1;
        }
        return 2600; 
    }
}