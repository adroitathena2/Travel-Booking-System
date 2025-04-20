package service;

import db.DBConnection;
import exception.AuthenticationException;
import java.sql.*;
import model.Customer;
import model.User;

public class UserService {
    public User authenticateUser(String email, String password) throws AuthenticationException {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement userCheck = con.prepareStatement("SELECT * FROM Users WHERE Email = ?");
            userCheck.setString(1, email);
            ResultSet userRs = userCheck.executeQuery();
            
            if (!userRs.next()) {
                throw new AuthenticationException("Email not found. Please register.");
            }
            
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM Users WHERE Email = ? AND Password = SHA2(?, 256)");
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return createCustomerFromResultSet(rs);
            }
            
            throw new AuthenticationException("Invalid email or password.");
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AuthenticationException("Database error: " + e.getMessage());
        }
    }
    
    private Customer createCustomerFromResultSet(ResultSet rs) throws SQLException {
        return new Customer(
            rs.getInt("UserID"),
            rs.getString("FirstName") + " " + rs.getString("LastName"),
            rs.getString("Email"),
            rs.getString("Password"),
            rs.getString("Phone"),
            rs.getString("Gender"),
            rs.getString("Address"),
            rs.getString("DOB")
        );
    }

    public void registerUser(String firstName, String lastName, String dob, 
                            String gender, String email, String phone, 
                            String address, String password) throws Exception {
        validateRegistrationInput(firstName, lastName, dob, gender, email, phone, password);
        
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement checkPs = con.prepareStatement(
                "SELECT COUNT(*) FROM Users WHERE Email = ?");
            checkPs.setString(1, email);
            ResultSet rs = checkPs.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                throw new Exception("Email already registered");
            }
            
            int nextId = getNextUserId(con);
            
            int age = calculateAge(dob);
            
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO Users (UserID, FirstName, LastName, DOB, Age, Gender, Email, Phone, Address, Password) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, nextId);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, dob);
            ps.setInt(5, age);
            ps.setString(6, gender);
            ps.setString(7, email);
            ps.setString(8, phone);
            ps.setString(9, address);
            ps.setString(10, password); 
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Database error: " + e.getMessage());
        }
    }
    
    private void validateRegistrationInput(String firstName, String lastName, String dob, 
                                          String gender, String email, String phone, String password) throws Exception {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new Exception("First name is required");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new Exception("Last name is required");
        }
        if (dob == null || dob.trim().isEmpty()) {
            throw new Exception("Date of birth is required");
        }
        if (gender == null || gender.trim().isEmpty()) {
            throw new Exception("Gender is required");
        }
        if (!gender.equalsIgnoreCase("M") && !gender.equalsIgnoreCase("F")) {
            throw new Exception("Gender must be 'M' or 'F'");
        }
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            throw new Exception("Valid email is required");
        }
        if (phone == null || phone.trim().isEmpty() || phone.length() < 10) {
            throw new Exception("Valid phone number is required");
        }
        if (password == null || password.length() < 6) {
            throw new Exception("Password must be at least 6 characters long");
        }
    }
    
    private int getNextUserId(Connection con) throws SQLException {
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT MAX(UserID) as MaxID FROM Users");
        if (rs.next()) {
            return rs.getInt("MaxID") + 1;
        }
        return 1;
    }
    
    private int calculateAge(String dobString) {
        try {
            java.util.Date dob = java.sql.Date.valueOf(dobString);
            java.util.Date now = new java.util.Date();
            long diffInMillies = Math.abs(now.getTime() - dob.getTime());
            long diff = diffInMillies / (1000L * 60 * 60 * 24 * 365);
            return (int) diff;
        } catch (Exception e) {
            return 0;
        }
    }
}
