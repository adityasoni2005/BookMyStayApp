import java.util.HashMap;
import java.util.Map;

// Custom Exception for invalid booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Inventory class to manage room availability
class RoomInventory {

    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("Standard", 2);
        rooms.put("Deluxe", 2);
        rooms.put("Suite", 1);
    }

    // Validate room type
    public void validateRoomType(String roomType) throws InvalidBookingException {
        if (!rooms.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
    }

    // Check availability
    public void checkAvailability(String roomType) throws InvalidBookingException {
        if (rooms.get(roomType) <= 0) {
            throw new InvalidBookingException("No rooms available for: " + roomType);
        }
    }

    // Book a room safely
    public void bookRoom(String roomType) throws InvalidBookingException {

        validateRoomType(roomType);
        checkAvailability(roomType);

        int remaining = rooms.get(roomType) - 1;

        if (remaining < 0) {
            throw new InvalidBookingException("Inventory cannot become negative.");
        }

        rooms.put(roomType, remaining);
        System.out.println("Booking confirmed for room type: " + roomType);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Room Inventory:");
        for (String type : rooms.keySet()) {
            System.out.println(type + " : " + rooms.get(type));
        }
    }
}

// Booking Service
class BookingService {

    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void requestBooking(String guestName, String roomType) {
        System.out.println("\nGuest " + guestName + " requesting " + roomType + " room.");

        try {
            inventory.bookRoom(roomType);
        } catch (InvalidBookingException e) {
            System.out.println("Booking Failed: " + e.getMessage());
        }
    }
}

// Main Program
public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService(inventory);

        inventory.displayInventory();

        // Valid booking
        service.requestBooking("Alice", "Deluxe");

        // Invalid room type
        service.requestBooking("Bob", "Luxury");

        // Booking until inventory exhausted
        service.requestBooking("Charlie", "Suite");
        service.requestBooking("David", "Suite");

        inventory.displayInventory();
    }
}