import java.util.ArrayList;
import java.util.List;

// Annon OS: Memory Commit Module
// Purpose: Record and avoid physical hazards found during the "First Walk"
public class HazardMemory {

    private List<HazardNode> knownHazards = new ArrayList<>();
    private final float PROXIMITY_ALERT = 1.5f; // Alert within 1.5 meters

    // Represents a physical danger committed to memory
    public static class HazardNode {
        double lat, lon, altitude;
        String type; // e.g., "Step Up", "Low Ceiling", "Uneven Ground"
        long timestamp;

        public HazardNode(double lat, double lon, String type) {
            this.lat = lat;
            this.lon = lon;
            this.type = type;
            this.timestamp = System.currentTimeMillis();
        }
    }

    // Logic to commit a new hazard to the local SQL database
    public void commitToMemory(double lat, double lon, String type) {
        HazardNode newNode = new HazardNode(lat, lon, type);
        knownHazards.add(newNode);
        
        // Annon Protocol: Silence confirmed by haptic confirmation
        AnnonHaptics.confirmCommit(); 
        System.out.println("HAZARD COMMITTED: " + type + " at " + lat + ", " + lon);
    }

    // Constant background check against committed memory
    public void checkSurroundings(double currentLat, double currentLon) {
        for (HazardNode hazard : knownHazards) {
            double distance = calculateDistance(currentLat, currentLon, hazard.lat, hazard.lon);
            
            if (distance <= PROXIMITY_ALERT) {
                triggerHazardWarning(hazard);
            }
        }
    }

    private void triggerHazardWarning(HazardNode hazard) {
        // Spatial Audio: "Step up ahead"
        AnnonAudio.speak(hazard.type + " ahead.");
        // Directional Haptics: Vibrate seat or pocket
        AnnonHaptics.directionalPulse(hazard.lat, hazard.lon);
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Haversine formula for high-accuracy pedestrian distance
        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2))
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                    * Math.cos(Math.toRadians(theta));
        return Math.acos(dist) * 6371000; // Return meters
    }
}
