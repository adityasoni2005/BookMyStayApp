import java.util.*;

class BookMyStayApp {
    public static void main(String[] args) {
        BookingService bookingService = new BookingService();
        // Initialize inventory
        bookingService.addInventory("Deluxe", 2);
        bookingService.addInventory("Suite", 1);
        // Add booking requests to queue
        bookingService.addBookingRequest(new BookingRequest("John", "Deluxe"));
        bookingService.addBookingRequest(new BookingRequest("Alice", "Deluxe"));
        bookingService.addBookingRequest(new BookingRequest("Bob", "Suite"));
        bookingService.addBookingRequest(new BookingRequest("Tom", "Suite"));
        // Process bookings
        System.out.println(bookingService.processBooking());
        System.out.println(bookingService.processBooking());
        System.out.println(bookingService.processBooking());
        System.out.println(bookingService.processBooking());
    }
}
class BookingService {
    private Queue<BookingRequest> requestQueue = new LinkedList<>();
    private Map<String, Integer> inventory = new HashMap<>();
    private Map<String, Set<String>> assignedRooms = new HashMap<>();
    private Set<String> allocatedRoomIds = new HashSet<>();
    // Add room inventory
    public void addInventory(String roomType, int count) {
        inventory.put(roomType, count);
    }
    // Add booking request
    public void addBookingRequest(BookingRequest request) {
        requestQueue.offer(request);
    }
    // Process booking request
    public synchronized Reservation processBooking() {
        BookingRequest request = requestQueue.poll();
        if (request == null) {
            return null;
        }
        String roomType = request.getRoomType();
        // Check availability
        if (inventory.getOrDefault(roomType, 0) == 0) {
            return new Reservation(
                    request.getGuestName(),
                    roomType,
                    null,
                    "REJECTED"
            );
        }
        // Generate unique room ID
        String roomId = generateRoomId(roomType);
        // Record allocation
        allocatedRoomIds.add(roomId);
        assignedRooms
                .computeIfAbsent(roomType, k -> new HashSet<>())
                .add(roomId);
        // Update inventory
        inventory.put(roomType, inventory.get(roomType) - 1);
        // Confirm reservation
        return new Reservation(
                request.getGuestName(),
                roomType,
                roomId,
                "CONFIRMED"
        );
    }
    // Generate unique room ID
    private String generateRoomId(String roomType) {
        String prefix = roomType.substring(0, 1).toUpperCase();
        String roomId;
        do {
            int number = 100 + new Random().nextInt(900);
            roomId = prefix + number;
        } while (allocatedRoomIds.contains(roomId));
        return roomId;
    }
}
class BookingRequest {
    private String guestName;
    private String roomType;
    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
    public String getGuestName() {
        return guestName;
    }
    public String getRoomType() {
        return roomType;
    }
}

class Reservation {
    private String guestName;
    private String roomType;
    private String roomId;
    private String status;

    public Reservation(String guestName, String roomType, String roomId, String status) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.status = status;
    }
    @Override
    public String toString() {
        return "Reservation{" +
                "guestName='" + guestName + '\'' +
                ", roomType='" + roomType + '\'' +
                ", roomId='" + roomId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}