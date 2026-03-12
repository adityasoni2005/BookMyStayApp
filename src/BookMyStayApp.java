import java.util.*;

// Reservation class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean active;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.active = true;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isActive() {
        return active;
    }

    public void cancel() {
        active = false;
    }

    @Override
    public String toString() {
        return "ReservationID: " + reservationId +
                ", Guest: " + guestName +
                ", RoomType: " + roomType +
                ", RoomID: " + roomId +
                ", Status: " + (active ? "Confirmed" : "Cancelled");
    }
}

// Inventory management
class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 2);
        inventory.put("Suite", 1);
    }

    public boolean isAvailable(String roomType) {
        return inventory.containsKey(roomType) && inventory.get(roomType) > 0;
    }

    public void allocateRoom(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void releaseRoom(String roomType) {
        inventory.put(roomType, inventory.get(roomType) + 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " : " + inventory.get(type));
        }
    }
}

// Booking Service
class BookingService {

    private RoomInventory inventory;
    private Map<String, Reservation> bookingHistory = new HashMap<>();
    private Stack<String> rollbackStack = new Stack<>();

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    // Confirm booking
    public void confirmBooking(String reservationId, String guestName, String roomType) {

        if (!inventory.isAvailable(roomType)) {
            System.out.println("Booking failed. No rooms available for " + roomType);
            return;
        }

        String roomId = roomType.substring(0,1).toUpperCase() + (bookingHistory.size() + 1);

        inventory.allocateRoom(roomType);

        Reservation reservation = new Reservation(reservationId, guestName, roomType, roomId);
        bookingHistory.put(reservationId, reservation);

        rollbackStack.push(roomId);

        System.out.println("Booking confirmed: " + reservation);
    }

    // Cancel booking
    public void cancelBooking(String reservationId) {

        if (!bookingHistory.containsKey(reservationId)) {
            System.out.println("Cancellation failed. Reservation not found.");
            return;
        }

        Reservation reservation = bookingHistory.get(reservationId);

        if (!reservation.isActive()) {
            System.out.println("Cancellation failed. Booking already cancelled.");
            return;
        }

        reservation.cancel();

        String roomType = reservation.getRoomType();

        inventory.releaseRoom(roomType);

        String releasedRoom = rollbackStack.pop();

        System.out.println("Booking cancelled for ReservationID: " + reservationId);
        System.out.println("Room released: " + releasedRoom);
    }

    public void showBookings() {
        System.out.println("\nBooking History:");
        for (Reservation r : bookingHistory.values()) {
            System.out.println(r);
        }
    }
}

// Main Program
public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);

        inventory.displayInventory();

        // Confirm bookings
        bookingService.confirmBooking("R101", "Alice", "Deluxe");
        bookingService.confirmBooking("R102", "Bob", "Suite");

        bookingService.showBookings();
        inventory.displayInventory();

        // Cancel booking
        bookingService.cancelBooking("R102");

        bookingService.showBookings();
        inventory.displayInventory();

        // Invalid cancellation
        bookingService.cancelBooking("R999");
    }
}