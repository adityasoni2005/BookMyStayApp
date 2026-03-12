import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class BookMyStayApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hotel Booking System v1.0");
        System.out.println("Booking Request Intake Simulation\n");
        BookingQueue bookingQueue = new BookingQueue();
        boolean continueBooking = true;
        while (continueBooking) {
            System.out.println("Enter guest name: ");
            String guestName = scanner.nextLine().trim();
            System.out.println("Enter room type (Single / Double / Suite): ");
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
                    System.out.println("Invalid room type. Try again.");
                    continue; // restart loop for valid input
            }
            System.out.println("Enter number of rooms to book: ");
            int roomsRequested;
            try {
                roomsRequested = Integer.parseInt(scanner.nextLine().trim());
                if (roomsRequested <= 0) {
                    System.out.println("Number of rooms must be positive. Try again.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
                continue;
            }

            // Create reservation and add to queue
            Reservation reservation = new Reservation(guestName, roomType, roomsRequested);
            bookingQueue.addRequest(reservation);

            System.out.println("Do you want to add another booking? (yes/no): ");
            String answer = scanner.nextLine().trim().toLowerCase();
            if (!answer.equals("yes")) {
                continueBooking = false;
            }
        }
        // Display queued requests in arrival order
        bookingQueue.displayQueuedRequests();
        scanner.close();
    }
}
class Reservation {

    private String guestName;
    private String roomType;
    private int roomsRequested;

    public Reservation(String guestName, String roomType, int roomsRequested) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomsRequested = roomsRequested;
    }
    public String getGuestName() {
        return guestName;
    }
    public String getRoomType() {
        return roomType;
    }
    public int getRoomsRequested() {
        return roomsRequested;
    }
    @Override
    public String toString() {
        return "Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Rooms Requested: " + roomsRequested;
    }
}
class BookingQueue {
    private Queue<Reservation> requestQueue;
    public BookingQueue() {
        requestQueue = new LinkedList<>();
    }
    public void addRequest(Reservation reservation) {
        requestQueue.add(reservation);
        System.out.println("Request added to queue: " + reservation.getGuestName());
    }
    public void displayQueuedRequests() {
        System.out.println("\nQueued Booking Requests (Arrival Order):");
        System.out.println("----------------------------------------");
        for (Reservation r : requestQueue) {
            System.out.println(r);
        }
        System.out.println("----------------------------------------");
    }
    public Reservation pollNextRequest() {
        return requestQueue.poll();
    }
    public boolean isEmpty() {
        return requestQueue.isEmpty();
    }
}