package model;

import java.sql.SQLException;
import java.util.List;

public interface BookingOperations {
    int bookPackage(int userId, int packageId, double amount) throws Exception;
    List<Booking> getUserBookings(int userId) throws SQLException;
    boolean cancelBooking(int bookingId) throws SQLException;
}
