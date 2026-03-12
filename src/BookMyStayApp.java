import java.util.*;

// Represents an Add-On Service
class Service {
    private String serviceName;
    private double cost;

    public Service(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }
}

// Manages Add-On Services for reservations
class AddOnServiceManager {

    // Map reservation ID -> List of services
    private Map<String, List<Service>> reservationServices = new HashMap<>();

    // Add service to reservation
    public void addService(String reservationId, Service service) {

        reservationServices.putIfAbsent(reservationId, new ArrayList<>());

        reservationServices.get(reservationId).add(service);

        System.out.println(service.getServiceName() + " added to Reservation " + reservationId);
    }

    // Display services for reservation
    public void showServices(String reservationId) {

        List<Service> services = reservationServices.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        System.out.println("\nServices for Reservation " + reservationId + ":");

        for (Service s : services) {
            System.out.println("- " + s.getServiceName() + " : Rs." + s.getCost());
        }
    }

    // Calculate total add-on cost
    public double calculateTotalCost(String reservationId) {

        double total = 0;

        List<Service> services = reservationServices.get(reservationId);

        if (services != null) {
            for (Service s : services) {
                total += s.getCost();
            }
        }

        return total;
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        AddOnServiceManager manager = new AddOnServiceManager();

        // Example reservation
        String reservationId = "RES101";

        System.out.println("Reservation ID: " + reservationId);

        // Available services
        Service breakfast = new Service("Breakfast", 500);
        Service airportPickup = new Service("Airport Pickup", 1200);
        Service spa = new Service("Spa Access", 2000);
        Service extraBed = new Service("Extra Bed", 800);

        System.out.println("\nAvailable Add-On Services");
        System.out.println("1. Breakfast - Rs.500");
        System.out.println("2. Airport Pickup - Rs.1200");
        System.out.println("3. Spa Access - Rs.2000");
        System.out.println("4. Extra Bed - Rs.800");

        System.out.print("\nEnter number of services you want to add: ");
        int n = sc.nextInt();

        for (int i = 0; i < n; i++) {

            System.out.print("Select service (1-4): ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    manager.addService(reservationId, breakfast);
                    break;
                case 2:
                    manager.addService(reservationId, airportPickup);
                    break;
                case 3:
                    manager.addService(reservationId, spa);
                    break;
                case 4:
                    manager.addService(reservationId, extraBed);
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }

        // Show selected services
        manager.showServices(reservationId);

        // Calculate total cost
        double total = manager.calculateTotalCost(reservationId);

        System.out.println("\nTotal Add-On Cost: Rs." + total);

        sc.close();
    }
}