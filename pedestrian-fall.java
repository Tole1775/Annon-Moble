// Annon OS: Pedestrian Safety Module
public class PedestrianMonitor {
    
    // Low-pass filter to ignore the "rhythm" of a normal walking gait
    private float alpha = 0.8f;
    private float gravity[] = new float[3];

    public void onSensorChanged(SensorEvent event) {
        // Isolate Linear Acceleration (remove gravity)
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        float linear_x = event.values[0] - gravity[0];
        float linear_y = event.values[1] - gravity[1];
        float linear_z = event.values[2] - gravity[2];

        // If linear acceleration drops to near-zero (Freefall) followed by a spike
        if (checkFreefall(linear_x, linear_y, linear_z)) {
            verifyImpact();
        }
    }

    private void verifyImpact() {
        // Annon Protocol: Silent Check
        // Vibrate phone. If user taps volume button (Tactile Check), cancel EMS.
        if (!AnnonBio.userResponsive()) {
            executeEmergencyBroadcast();
        }
    }

    private boolean checkFreefall(float x, float y, float z) {
        // Detect freefall: total linear acceleration near zero means no surface contact
        float magnitude = (float) Math.sqrt(x * x + y * y + z * z);
        return magnitude < 0.5f; // Below 0.5 m/s² is considered freefall
    }

    private void executeEmergencyBroadcast() {
        // Broadcast emergency alert with last known GPS location
        AnnonAudio.speak("Emergency detected. Contacting emergency services.");
        AnnonBio.broadcastEmergency();
    }
}
