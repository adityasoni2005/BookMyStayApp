import java.util.ArrayList;
import java.util.List;

// Reservation class representing a confirmed booking
class Reservation {
    private String guestName;
    private String roomType;
    private int nights;

    public Reservation(String guestName, String roomType, int nights) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    @Override
    public String toString() {
        return "Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Nights: " + nights;
    }
}

// BookingHistory class that stores confirmed reservations
class BookingHistory {

    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    // Add confirmed reservation to history
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    // Retrieve all reservations
    public List<Reservation> getReservations() {
        return reservations;
    }
}

// Report service for generating booking summaries
class BookingReportService {

    public void generateReport(List<Reservation> reservations) {

        System.out.println("\n--- Booking History Report ---");

        if (reservations.isEmpty()) {
            System.out.println("No bookings available.");
            return;
        }

        int totalBookings = reservations.size();
        int totalNights = 0;

        for (Reservation r : reservations) {
            totalNights += r.getNights();
        }

        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Nights Booked: " + totalNights);

        System.out.println("\nDetailed Reservations:");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulating confirmed bookings
        Reservation r1 = new Reservation("Alice", "Deluxe", 3);
        Reservation r2 = new Reservation("Bob", "Suite", 2);
        Reservation r3 = new Reservation("Charlie", "Standard", 1);

        // Add bookings to history
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Admin retrieves booking history
        System.out.println("=== Stored Booking History ===");
        for (Reservation r : history.getReservations()) {
            System.out.println(r);
        }

        // Admin generates report
        reportService.generateReport(history.getReservations());
    }
}