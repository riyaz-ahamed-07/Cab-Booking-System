package com.example.test.cli;

import com.example.test.Entity.Driver;
import com.example.test.Entity.RideRequest;
import com.example.test.Entity.Rider;
import com.example.test.Entity.Trip;
import com.example.test.Service.DriverService;
import com.example.test.Service.RidePoolingService;
import com.example.test.Service.RideRequestService;
import com.example.test.Service.RiderService;
import com.example.test.Service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class MainCLI implements CommandLineRunner {

    private final RiderService riderService;
    private final DriverService driverService;
    private final RideRequestService rideRequestService;
    private final RidePoolingService ridePoolingService;
    private final TripService tripService;

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void run(String... args) {
        boolean running = true;

        while (running) {
            System.out.println("\n===== ONLINE CAB POOLING SYSTEM =====");
            System.out.println("1. Rider");
            System.out.println("2. Driver");
            System.out.println("3. Exit");
            System.out.print("Choose your role: ");

            int role = parseInt(scanner.nextLine());
            switch (role) {
                case 1:
                    riderFlow();
                    break;
                case 2:
                    driverFlow();
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("Please enter 1, 2, or 3.");
            }
        }
    }

    private void riderFlow() {
        boolean menu = true;
        while (menu) {
            System.out.println("\n--- RIDER MENU ---");
            System.out.println("1. Register Rider");
            System.out.println("2. Create Ride Request");
            System.out.println("3. Match Ride Request");
            System.out.println("4. View Pending Requests");
            System.out.println("5. Back");
            System.out.print("Choose: ");

            int option = parseInt(scanner.nextLine());
            switch (option) {
                case 1:
                    registerRider();
                    break;
                case 2:
                    createRideRequest();
                    break;
                case 3:
                    matchRideRequest();
                    break;
                case 4:
                    viewPendingRideRequests();
                    break;
                case 5:
                    menu = false;
                    break;
                default:
                    System.out.println("Choose 1-5.");
            }
        }
    }

    private void driverFlow() {
        boolean menu = true;
        while (menu) {
            System.out.println("\n--- DRIVER MENU ---");
            System.out.println("1. Register Driver");
            System.out.println("2. View Pooled Trips");
            System.out.println("3. Complete Trip");
            System.out.println("4. Back");
            System.out.print("Choose: ");

            int option = parseInt(scanner.nextLine());
            switch (option) {
                case 1:
                    registerDriver();
                    break;
                case 2:
                    viewPooledTrips();
                    break;
                case 3:
                    completeTrip();
                    break;
                case 4:
                    menu = false;
                    break;
                default:
                    System.out.println("Choose 1-4.");
            }
        }
    }

    private void registerRider() {
        Rider rider = new Rider();
        System.out.print("Name: ");
        rider.setRiderName(scanner.nextLine());
        System.out.print("Phone: ");
        rider.setPhoneNumber(scanner.nextLine());
        System.out.print("Email: ");
        rider.setEmail(scanner.nextLine());
        System.out.print("Pickup: ");
        rider.setPickupLocation(scanner.nextLine());
        System.out.print("Drop: ");
        rider.setDropLocation(scanner.nextLine());

        riderService.addRider(rider);
        System.out.println("Rider registered.");
    }

    private void registerDriver() {
        Driver driver = new Driver();
        System.out.print("Name: ");
        driver.setDriverName(scanner.nextLine());
        System.out.print("Phone: ");
        driver.setPhoneNumber(scanner.nextLine());
        System.out.print("Vehicle number: ");
        driver.setVehicleNumber(scanner.nextLine());
        System.out.print("Route (start -> end): ");
        driver.setVehicleType(scanner.nextLine());
        System.out.print("Capacity: ");
        driver.setCapacity(parseInt(scanner.nextLine()));

        driverService.addDriver(driver);
        System.out.println("Driver registered.");
    }

    private void createRideRequest() {
        List<Rider> riders = riderService.getAllRiders();
        if (riders.isEmpty()) {
            System.out.println("Add a rider first.");
            return;
        }
        riders.forEach(r -> System.out.println(r.getRiderId() + ": " + r.getRiderName()));
        System.out.print("Rider ID: ");
        Long riderId = Long.valueOf(scanner.nextLine());
        Rider rider = riders.stream().filter(r -> r.getRiderId().equals(riderId)).findFirst().orElse(null);
        if (rider == null) {
            System.out.println("Rider not found.");
            return;
        }

        RideRequest request = new RideRequest();
        request.setRider(rider);
        System.out.print("Pickup: ");
        request.setPickupLocation(scanner.nextLine());
        System.out.print("Drop: ");
        request.setDropLocation(scanner.nextLine());

        rideRequestService.addRideRequest(request);
        System.out.println("Ride request created.");
    }

    private void matchRideRequest() {
        System.out.print("Request ID to match: ");
        Long requestId = Long.valueOf(scanner.nextLine());
        try {
            Trip trip = ridePoolingService.matchAndCreatePool(requestId);
            System.out.println("Matched trip: " + trip);
        } catch (Exception e) {
            System.out.println("Match failed: " + e.getMessage());
        }
    }

    private void viewPendingRideRequests() {
        rideRequestService.getPendingRequests().forEach(System.out::println);
    }

    private void viewPooledTrips() {
        tripService.getAllTrips().forEach(System.out::println);
    }

    private void completeTrip() {
        System.out.print("Trip ID to complete: ");
        Long tripId = Long.valueOf(scanner.nextLine());
        try {
            Trip trip = tripService.completeTrip(tripId);
            System.out.println("Completed: " + trip);
        } catch (Exception e) {
            System.out.println("Complete failed: " + e.getMessage());
        }
    }

    private int parseInt(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (Exception e) {
            return -1;
        }
    }
}
