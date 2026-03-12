public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Booking System");
        System.out.println("Hotel Booking System v1.0\n");

        // Creating room objects using polymorphism
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        // Availability variables (static representation)
        int singleRoomAvailable = 5;
        int doubleRoomAvailable = 3;
        int suiteRoomAvailable = 2;

        // Display room details
        displayRoom(singleRoom, singleRoomAvailable);
        displayRoom(doubleRoom, doubleRoomAvailable);
        displayRoom(suiteRoom, suiteRoomAvailable);
    }
    public static void displayRoom(Room room, int availability) {
        System.out.println("Room Type: " + room.getRoomType());
        System.out.println("Beds: " + room.getBeds());
        System.out.println("Size: " + room.getSize() + " sq ft");
        System.out.println("Price: $" + room.getPrice());
        System.out.println("Available Rooms: " + availability);
        System.out.println("-----------------------------------");
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
    @Override
    public String getRoomType() {
        return "Single Room";
    }
}
class DoubleRoom extends Room {
    public DoubleRoom() {
        super(2, 350, 120.0);
    }
    @Override
    public String getRoomType() {
        return "Double Room";
    }
}
class SuiteRoom extends Room {
    public SuiteRoom() {
        super(3, 600, 250.0);
    }
    @Override
    public String getRoomType() {
        return "Suite Room";
    }
}