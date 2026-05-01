import android.location.Location;
import android.location.LocationListener;

// Annon OS: High-Signal Location Logic
public class AnnonNavigator implements LocationListener {
    
    @Override
    public void onLocationChanged(Location loc) {
        // Only trigger audio if movement > 2 meters to avoid "noise"
        if (loc.getAccuracy() < 5.0) {
            processCoordinates(loc.getLatitude(), loc.getLongitude());
        }
    }

    private void processCoordinates(double lat, double lon) {
        // 4th-Grade Verbal Check
        // "You are on Main Street. The City Hall entrance is 20 feet ahead."
        String guidance = AnnonAI.getVerbalPrompt(lat, lon);
        AnnonAudio.speak(guidance);
    }
}
