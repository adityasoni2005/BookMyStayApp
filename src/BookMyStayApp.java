import java.io.*;
import java.util.*;

// Serializable Reservation class
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    private String reservationId;
    private String guestName;
    private String roomType;
    private boolean active;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.active = true;
    }

    public void cancel() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "ReservationID: " + reservationId +
                ", Guest: " + guestName +
                ", RoomType: " + roomType +
                ", Status: " + (active ? "Confirmed" : "Cancelled");
    }
}

// Serializable Inventory class
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 2);
        inventory.put("Suite", 1);
    }

    public boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
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

    // Getter for inventory map
    public Map<String, Integer> getInventoryMap() {
        return inventory;
    }

    // Restore inventory from another RoomInventory object
    public void restoreInventory(RoomInventory restored) {
        this.inventory.clear();
        this.inventory.putAll(restored.getInventoryMap());
    }
}

// Service to persist and recover state
class PersistenceService {

    private static final String FILE_NAME = "booking_system.dat";

    public static void saveState(RoomInventory inventory, List<Reservation> bookings) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(inventory);
            out.writeObject(bookings);
            System.out.println("\nSystem state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Reservation> loadState(RoomInventory inventory) {
        List<Reservation> bookings = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("\nNo previous system state found. Starting fresh.");
            return bookings;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            RoomInventory restoredInventory = (RoomInventory) in.readObject();
            List<Reservation> restoredBookings = (List<Reservation>) in.readObject();

            // Restore inventory using method
            inventory.restoreInventory(restoredInventory);

            // Restore bookings
            bookings = restoredBookings;
            System.out.println("\nSystem state restored successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading system state: " + e.getMessage());
            System.out.println("Starting with fresh system state.");
        }

        return bookings;
    }
}

// Main Booking System
public class BookMyStayApp{

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        List<Reservation> bookingHistory = PersistenceService.loadState(inventory);

        inventory.displayInventory();

        // Simulate bookings
        Reservation r1 = new Reservation("R101", "Alice", "Deluxe");
        if (inventory.isAvailable(r1.getRoomType())) {
            inventory.allocateRoom(r1.getRoomType());
            bookingHistory.add(r1);
            System.out.println("Booking confirmed: " + r1);
        }

        Reservation r2 = new Reservation("R102", "Bob", "Suite");
        if (inventory.isAvailable(r2.getRoomType())) {
            inventory.allocateRoom(r2.getRoomType());
            bookingHistory.add(r2);
            System.out.println("Booking confirmed: " + r2);
        }

        // Display bookings
        System.out.println("\nBooking History:");
        for (Reservation r : bookingHistory) {
            System.out.println(r);
        }

        inventory.displayInventory();

        // Save state before shutdown
        PersistenceService.saveState(inventory, bookingHistory);

        // Simulate cancellation
        System.out.println("\nCancelling booking R101...");
        for (Reservation r : bookingHistory) {
            if (r.toString().contains("R101") && r.isActive()) {
                r.cancel();
                inventory.releaseRoom(r.getRoomType());
                System.out.println("Booking cancelled: " + r);
            }
        }

        inventory.displayInventory();

        // Save updated state
        PersistenceService.saveState(inventory, bookingHistory);
    }
}