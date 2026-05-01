import java.util.HashMap;
import java.util.Map;

public class AnnonNavigationManager {

    private Destination currentLock = null;
    private Map<String, Destination> publicRecordCache = new HashMap<>();

    // Destination Object: Lean and Data-Heavy
    public static class Destination {
        String name;
        double lat, lon;
        boolean hasFloorPlan; // From Hall of Records
        String securityLevel; // Private vs Public

        public Destination(String name, double lat, double lon, boolean hasFloorPlan, String securityLevel) {
            this.name = name;
            this.lat = lat;
            this.lon = lon;
            this.hasFloorPlan = hasFloorPlan;
            this.securityLevel = securityLevel;
        }
    }

    // Add a destination to the local cache (keys stored lowercase for lookup)
    public void addDestination(Destination dest) {
        if (dest != null && dest.name != null) {
            publicRecordCache.put(dest.name.toLowerCase(), dest);
        }
    }

    // Set destination via Verbal Command (e.g., "Go to City Hall")
    public void setDestination(String inputName) {
        if (inputName == null) {
            AnnonAudio.speak("No destination provided.");
            return;
        }
        Destination target = publicRecordCache.get(inputName.toLowerCase());

        if (target != null) {
            this.currentLock = target;
            executeLockProtocol();
        } else {
            // 4th Grade Feedback
            AnnonAudio.speak("Target not in memory. Search Public Records?");
        }
    }

    private void executeLockProtocol() {
        // Annon OS Lock: Once the destination is set, the OS prioritizes 
        // navigation interrupts over all other phone tasks.
        AnnonHaptics.confirmLock(); 
        
        String planStatus = currentLock.hasFloorPlan ? "Floor plan loaded." : "No plan. Scanning mode active.";
        AnnonAudio.speak("Heading locked to " + currentLock.name + ". " + planStatus);

        // Wake up the LiDAR and Cameras for real-time hazard detection
        AnnonMobileHub.triggerActiveScan(true);
    }

    public Destination getCurrentLock() {
        return currentLock;
    }
    
    // Clear lock upon arrival or Fail-Deadly event
    public void clearLock() {
        this.currentLock = null;
        AnnonAudio.speak("Destination reached. Standing by.");
    }
}
