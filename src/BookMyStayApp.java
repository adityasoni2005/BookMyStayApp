import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class BookMyStayApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Hotel Booking System");
        System.out.println("Hotel Booking System v1.0\n");

        // Create room objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Register room availability
        inventory.addRoomType(single.getRoomType(), 5);
        inventory.addRoomType(doubleRoom.getRoomType(), 3);
        inventory.addRoomType(suite.getRoomType(), 2);

        // Display available rooms
        inventory.displayInventory();

        // Take user input
        System.out.println("\nEnter room type to book (Single Room / Double Room / Suite Room): ");
        String roomType = scanner.nextLine();

        System.out.println("Enter number of rooms to book: ");
        int roomsRequested = scanner.nextInt();

        // Process booking
        int available = inventory.getAvailability(roomType);
        if (available >= roomsRequested) {
            inventory.updateAvailability(roomType, -roomsRequested);
            System.out.println("\nBooking successful!");
        } else {
            System.out.println("\nBooking failed. Not enough rooms available.");
        }
        // Display updated inventory
        System.out.println("\nUpdated Room Inventory:");
        inventory.displayInventory();
        scanner.close();
    }
}
class RoomInventory {
    private HashMap<String, Integer> inventory;
    public RoomInventory() {
        inventory = new HashMap<>();
    }
    public void addRoomType(String roomType, int count) {
        inventory.put(roomType, count);
    }
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
    public void updateAvailability(String roomType, int change) {
        int current = getAvailability(roomType);
        inventory.put(roomType, current + change);
    }
    public void displayInventory() {
        System.out.println("----------------------------");
        System.out.println("Current Room Inventory");
        System.out.println("----------------------------");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " rooms available");
        }
        System.out.println("----------------------------");
    }
}
abstract class Room {
    private int beds;
    private int size;
    private double price;

    public Room(int beds, int size, double price) {
        this.beds = beds;
        this.size = size;
        this.price = price;
    }
    public int getBeds() {
        return beds;
    }
    public int getSize() {
        return size;
    }
    public double getPrice() {
        return price;
    }
    public abstract String getRoomType();
}
class SingleRoom extends Room {
    public SingleRoom() {
        super(1, 200, 80.0);
    }
    public String getRoomType() {
        return "Single Room";
    }
}
class DoubleRoom extends Room {
    public DoubleRoom() {
        super(2, 350, 120.0);
    }
    public String getRoomType() {
        return "Double Room";
    }
}
class SuiteRoom extends Room {
    public SuiteRoom() {
        super(3, 600, 250.0);
    }
    public String getRoomType() {
        return "Suite Room";
    }
}