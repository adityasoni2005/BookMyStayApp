import java.util.*;

class BookMyStayApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hotel Booking System v1.0\n");

        // Create room domain objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Store room catalog
        List<Room> rooms = new ArrayList<>();
        rooms.add(single);
        rooms.add(doubleRoom);
        rooms.add(suite);

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 5);
        inventory.addRoomType("Double Room", 3);
        inventory.addRoomType("Suite Room", 0); // Suite unavailable

        // Create search service
        SearchService searchService = new SearchService(inventory);

        // Take user input for room search
        System.out.println("Enter room type to search (Single / Double / Suite): ");
        String input = scanner.nextLine().trim().toLowerCase();

        String roomType = "";
        switch (input) {
            case "single":
                roomType = "Single Room";
                break;
            case "double":
                roomType = "Double Room";
                break;
            case "suite":
                roomType = "Suite Room";
                break;
            default:
                System.out.println("Invalid room type entered.");
                scanner.close();
                return;
        }
        // Perform read-only search for the requested room
        searchService.searchRoomByType(rooms, roomType);
        scanner.close();
    }
}
class SearchService {
    private RoomInventory inventory;
    public SearchService(RoomInventory inventory) {
        this.inventory = inventory;
    }
    public void searchRoomByType(List<Room> rooms, String roomType) {
        System.out.println("\nSearch Results:");
        System.out.println("----------------------------");
        boolean found = false;
        for (Room room : rooms) {
            if (!room.getRoomType().equals(roomType)) {
                continue; // skip other room types
            }
            int availability = inventory.getAvailability(room.getRoomType());
            if (availability > 0) {
                System.out.println("Room Type: " + room.getRoomType());
                System.out.println("Beds: " + room.getBeds());
                System.out.println("Size: " + room.getSize() + " sq ft");
                System.out.println("Price: $" + room.getPrice());
                System.out.println("Available Rooms: " + availability);
                System.out.println("----------------------------");
                found = true;
            }
        }
        if (!found) {
            System.out.println("Sorry, " + roomType + " is not available at the moment.");
            System.out.println("----------------------------");
        }
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