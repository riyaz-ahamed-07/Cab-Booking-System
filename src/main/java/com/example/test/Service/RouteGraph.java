package com.example.test.Service;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RouteGraph {

    private final List<String> routeStops = Arrays.asList(
            "Kancheepuram",
            "Thandalam",
            "Chembarambakkam",
            "Nazarathpettai",
            "Poonamallee",
            "Koyambedu",
            "Anna Nagar"
    );

    private final Map<String, Map<String, Double>> distances = new HashMap<>();

    private void addEdge(String from, String to, double distance) {
        distances.computeIfAbsent(normalize(from), key -> new HashMap<>())
                .put(normalize(to), distance);
    }

    @PostConstruct
    public void initialize() {
        addEdge("Kancheepuram", "Thandalam", 12.0);
        addEdge("Thandalam", "Chembarambakkam", 10.0);
        addEdge("Chembarambakkam", "Nazarathpettai", 9.0);
        addEdge("Nazarathpettai", "Poonamallee", 6.0);
        addEdge("Poonamallee", "Koyambedu", 8.0);
        addEdge("Koyambedu", "Anna Nagar", 5.0);
    }

    private String normalize(String location) {
        return location == null ? "" : location.trim().replaceAll("\\s+", " ").toLowerCase();
    }

    public double getDistance(String from, String to) {
        if (from == null || to == null) {
            return 0.0;
        }

        String normalizedFrom = normalize(from);
        String normalizedTo = normalize(to);

        if (normalizedFrom.equals(normalizedTo)) {
            return 0.0;
        }

        Map<String, Double> edges = distances.get(normalizedFrom);
        if (edges != null && edges.containsKey(normalizedTo)) {
            return edges.get(normalizedTo);
        }

        Map<String, Double> reverseEdges = distances.get(normalizedTo);
        if (reverseEdges != null && reverseEdges.containsKey(normalizedFrom)) {
            return reverseEdges.get(normalizedFrom);
        }

        int startIndex = indexOfStop(normalizedFrom);
        int endIndex = indexOfStop(normalizedTo);
        if (startIndex >= 0 && endIndex >= 0 && startIndex < endIndex) {
            double total = 0.0;
            for (int i = startIndex; i < endIndex; i++) {
                total += getDistance(routeStops.get(i), routeStops.get(i + 1));
            }
            return total;
        }

        return 14.0;
    }

    public double computeRouteDistance(String pickupLocation, String dropLocation) {
        if (pickupLocation == null || dropLocation == null || pickupLocation.isBlank() || dropLocation.isBlank()) {
            return 5.0;
        }
        double distance = getDistance(pickupLocation, dropLocation);
        return Math.max(distance, 5.0);
    }

    public boolean areRoutesSimilar(String pickupA, String dropA, String pickupB, String dropB) {
        int startA = indexOfStop(normalize(pickupA));
        int endA = indexOfStop(normalize(dropA));
        int startB = indexOfStop(normalize(pickupB));
        int endB = indexOfStop(normalize(dropB));

        if (startA >= 0 && endA >= 0 && startB >= 0 && endB >= 0) {
            return startA < endA && startB < endB && startA <= startB && endA >= endB;
        }

        double pickupDistance = getDistance(pickupA, pickupB);
        double dropDistance = getDistance(dropA, dropB);
        double similarity = 1.0 - ((pickupDistance + dropDistance) / 40.0);
        return similarity >= 0.65;
    }

    public boolean coversPickupDrop(String driverRoute, String pickupLocation, String dropLocation) {
        if (driverRoute == null || driverRoute.isBlank()) {
            return false;
        }

        List<String> route = parseRoute(driverRoute);
        int pickupIndex = indexOfStop(route, pickupLocation);
        int dropIndex = indexOfStop(route, dropLocation);
        return pickupIndex >= 0 && dropIndex >= 0 && pickupIndex < dropIndex;
    }

    public List<String> parseRoute(String route) {
        if (route == null) {
            return new ArrayList<>();
        }
        String normalized = route.replace("->", ",").replace("—", ",");
        String[] stops = normalized.split(",");
        List<String> parsedStops = new ArrayList<>();
        for (String stop : stops) {
            String trimmed = stop.trim();
            if (!trimmed.isEmpty()) {
                parsedStops.add(trimmed);
            }
        }
        return parsedStops;
    }

    private int indexOfStop(String stop) {
        String normalizedStop = normalize(stop);
        for (int i = 0; i < routeStops.size(); i++) {
            if (normalize(routeStops.get(i)).equals(normalizedStop)) {
                return i;
            }
        }
        return -1;
    }

    private int indexOfStop(List<String> route, String stop) {
        String normalizedStop = normalize(stop);
        for (int i = 0; i < route.size(); i++) {
            if (normalize(route.get(i)).equals(normalizedStop)) {
                return i;
            }
        }
        return -1;
    }

    public List<String> getRouteStops() {
        return new ArrayList<>(routeStops);
    }
}
