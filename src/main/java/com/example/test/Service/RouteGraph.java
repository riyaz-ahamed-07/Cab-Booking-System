package com.example.test.Service;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class RouteGraph {

    private final Map<String, Map<String, Double>> distances = new HashMap<>();

    private void addEdge(String from, String to, double distance) {
        distances.computeIfAbsent(from.toUpperCase(), key -> new HashMap<>())
                .put(to.toUpperCase(), distance);
    }

    @PostConstruct
    public void initialize() {
        addEdge("A", "B", 8.0);
        addEdge("A", "C", 12.0);
        addEdge("B", "C", 5.0);
        addEdge("B", "D", 10.0);
        addEdge("C", "D", 7.0);
        addEdge("D", "E", 6.0);
        addEdge("A", "E", 18.0);
        addEdge("C", "E", 9.0);
        addEdge("B", "E", 15.0);
    }

    public double getDistance(String from, String to) {
        if (from == null || to == null) {
            return 0.0;
        }

        if (from.equalsIgnoreCase(to)) {
            return 0.0;
        }

        Map<String, Double> edges = distances.get(from.toUpperCase());
        if (edges != null && edges.containsKey(to.toUpperCase())) {
            return edges.get(to.toUpperCase());
        }

        Map<String, Double> reverseEdges = distances.get(to.toUpperCase());
        if (reverseEdges != null && reverseEdges.containsKey(from.toUpperCase())) {
            return reverseEdges.get(from.toUpperCase());
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
        double pickupDistance = getDistance(pickupA, pickupB);
        double dropDistance = getDistance(dropA, dropB);
        double similarity = 1.0 - ((pickupDistance + dropDistance) / 40.0);
        return similarity >= 0.65;
    }
}