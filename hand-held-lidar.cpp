#include <iostream>
#include <vector>
#include <cmath>

// Annon OS Module: Proximity Audio Feedback
// Logic: 4th Grade Level (Close = Fast, Far = Slow)

class LidarProximitySystem {
private:
    const float CRITICAL_ZONE = 0.5; // 0.5 meters (Immediate Stop)
    const float WARNING_ZONE = 3.0;  // 3.0 meters (Start Pinging)
    
public:
    // This function takes the raw Point Cloud from the Annon Mobile hub
    void processLidarData(const std::vector<float>& pointCloudDistances) {
        float closestObject = 100.0; // Default to clear path

        // Find the single closest point in the path
        for (float distance : pointCloudDistances) {
            if (distance < closestObject) {
                closestObject = distance;
            }
        }

        executeFeedback(closestObject);
    }

    void executeFeedback(float distance) {
        if (distance <= CRITICAL_ZONE) {
            // Constant tone + Seat vibration (Left/Right/Center)
            triggerHaptics("MAX");
            playTone("CONSTANT");
            std::cout << "ALERT: STOP" << std::endl;
        } 
        else if (distance <= WARNING_ZONE) {
            // Calculate pulse frequency based on distance
            // Closer = Higher frequency pulse
            float pulseRate = mapDistanceToFrequency(distance);
            triggerHaptics("PULSE", pulseRate);
            playTone("BEEP", pulseRate);
            std::cout << "Object at: " << distance << "m. Pinging..." << std::endl;
        }
        else {
            // Path is clear. Keep the OS quiet.
            stopAllFeedback();
        }
    }

private:
    float mapDistanceToFrequency(float dist) {
        // Simple 4th grade math: Invert the distance
        // Result is a pulse delay in milliseconds
        return (dist / WARNING_ZONE) * 500; 
    }

    void triggerHaptics(std::string intensity, float rate = 0) {
        // Handshake with Annon Mobile to vibrate seat bolsters
    }

    void playTone(std::string type, float frequency = 0) {
        // Send spatial audio to earbuds
    }

    void stopAllFeedback() {
        // Maintain silence - Grey Man Protocol
    }
};
