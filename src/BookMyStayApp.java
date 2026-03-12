import java.util.*;
import java.util.concurrent.*;

// Reservation class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "ReservationID: " + reservationId + ", Guest: " + guestName + ", RoomType: " + roomType;
    }
}

// Thread-safe inventory
class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Standard", 5);
        inventory.put("Deluxe", 3);
        inventory.put("Suite", 2);
    }

    // Synchronized allocation
    public synchronized boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);
        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public synchronized void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " : " + inventory.get(type));
        }
    }
}

// Booking service handling concurrent requests
class BookingService implements Runnable {

    private static RoomInventory inventory;
    private static List<Reservation> confirmedBookings = Collections.synchronizedList(new ArrayList<>());
    private String reservationId;
    private String guestName;
    private String roomType;

    public BookingService(RoomInventory inventory, String reservationId, String guestName, String roomType) {
        BookingService.inventory = inventory;
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    @Override
    public void run() {
        if (inventory.allocateRoom(roomType)) {
            Reservation reservation = new Reservation(reservationId, guestName, roomType);
            confirmedBookings.add(reservation);
            System.out.println("Booking confirmed: " + reservation);
        } else {
            System.out.println("Booking failed (no rooms available) for Guest: " + guestName + ", RoomType: " + roomType);
        }
    }

    public static void showBookings() {
        System.out.println("\nConfirmed Bookings:");
        synchronized (confirmedBookings) {
            for (Reservation r : confirmedBookings) {
                System.out.println(r);
            }
        }
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) throws InterruptedException {

        RoomInventory inventory = new RoomInventory();

        // Create threads for multiple booking requests
        Thread t1 = new Thread(new BookingService(inventory, "R101", "Alice", "Deluxe"));
        Thread t2 = new Thread(new BookingService(inventory, "R102", "Bob", "Suite"));
        Thread t3 = new Thread(new BookingService(inventory, "R103", "Charlie", "Deluxe"));
        Thread t4 = new Thread(new BookingService(inventory, "R104", "David", "Suite"));
        Thread t5 = new Thread(new BookingService(inventory, "R105", "Eve", "Standard"));
        Thread t6 = new Thread(new BookingService(inventory, "R106", "Frank", "Deluxe"));
        Thread t7 = new Thread(new BookingService(inventory, "R107", "Grace", "Standard"));

        // Start all threads
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();

        // Wait for all threads to finish
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();
        t6.join();
        t7.join();

        // Show final booking state
        BookingService.showBookings();
        inventory.displayInventory();
    }
}