package service;

import db.DBConnection;
import java.sql.*;
import java.util.*;
import model.Booking;
import model.BookingOperations;
import model.Package;

public class PackageService implements BookingOperations {
    public List<Package> getAllPackages() throws SQLException {
        List<Package> packages = new ArrayList<>();
        
        try (Connection con = DBConnection.getConnection()) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Packages");
            
            while (rs.next()) {
                int id = rs.getInt("PackageID");
                String name = rs.getString("PackageDescription");
                String desc = getDestinationName(con, id);
                double price = rs.getDouble("Price");
                
                Package p = new Package(id, name, desc, price);
                packages.add(p);
            }
        }
        
        return packages;
    }
    
    @Override
    public int bookPackage(int userId, int packageId, double amount) throws Exception {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);
            
            int paymentId = getNextPaymentID(con);
            PreparedStatement psPayment = con.prepareStatement(
                "INSERT INTO Payment (PaymentID, Amount, PaymentStatus) VALUES (?, ?, ?)");
            psPayment.setInt(1, paymentId);
            psPayment.setDouble(2, amount);
            psPayment.setInt(3, 1);
            psPayment.executeUpdate();
            
            int bookingId = getNextBookingID(con);
            PreparedStatement psBooking = con.prepareStatement(
                "INSERT INTO Booking (BookingID, BookingStatus, PackageID, PaymentID) VALUES (?, ?, ?, ?)");
            psBooking.setInt(1, bookingId);
            psBooking.setInt(2, 1);
            psBooking.setInt(3, packageId);
            psBooking.setInt(4, paymentId);
            psBooking.executeUpdate();
            
            PreparedStatement psUserBooking = con.prepareStatement(
                "INSERT INTO Users_Booking (UserID, BookingID) VALUES (?, ?)");
            psUserBooking.setInt(1, userId);
            psUserBooking.setInt(2, bookingId);
            psUserBooking.executeUpdate();
            
            con.commit();
            return bookingId;
        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public List<Booking> getUserBookings(int userId) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "SELECT b.* FROM Booking b " +
                "JOIN Users_Booking ub ON b.BookingID = ub.BookingID " +
                "WHERE ub.UserID = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("BookingID");
                int status = rs.getInt("BookingStatus");
                int packageId = rs.getInt("PackageID");
                int paymentId = rs.getInt("PaymentID");
                
                Booking booking = new Booking(id, status, packageId, paymentId);
                bookings.add(booking);
            }
        }
        
        return bookings;
    }
    
    @Override
    public boolean cancelBooking(int bookingId) throws SQLException {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "UPDATE Booking SET BookingStatus = 0 WHERE BookingID = ?");
            ps.setInt(1, bookingId);
            int updated = ps.executeUpdate();
            
            return updated > 0;
        }
    }
    
    public String getDestinationName(Connection con, int packageId) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
            "SELECT d.DestinationName FROM Destinations d " +
            "JOIN PackageInfo pi ON d.DestinationID = pi.DestinationID " +
            "WHERE pi.PackageID = ?");
        ps.setInt(1, packageId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("DestinationName");
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
