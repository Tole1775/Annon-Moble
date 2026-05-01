#include <iostream>
#include <algorithm>

// --- Abstract Control Class ---
class AnnonController {
protected:
    float current_throttle = 0.0f; // 0.0 to 1.0
    float current_steering = 0.0f; // -1.0 (Left) to 1.0 (Right)

public:
    virtual void setSteering(float input) = 0;
    virtual void setThrottle(float input) = 0;
    virtual void emergencyStop() = 0;

    // Constraints to keep the "Blind Pilot" within safe physical limits
    float clamp(float val, float min, float max) {
        return std::max(min, std::min(max, val));
    }
};

// --- Car Control: Steering Rack & Electronic Throttle ---
class CarInterface : public AnnonController {
public:
    void setSteering(float input) override {
        // Cars have a specific turning radius/lock-to-lock
        current_steering = clamp(input, -1.0f, 1.0f);
        sendToCANBus(0x102, current_steering); // Steering Address
    }

    void setThrottle(float input) override {
        current_throttle = clamp(input, 0.0f, 0.8f); // Limit to 80% for safety
        sendToCANBus(0x101, current_throttle); // Throttle Address
    }

    void emergencyStop() override {
        sendToCANBus(0x101, 0.0f); // Zero Throttle
        sendToCANBus(0x103, 1.0f); // Max Braking Force
    }

private:
    void sendToCANBus(int address, float value) {
        // Direct hardware write to the car's computer
    }
};

// --- Boat Control: Rudder & Outboard Trim ---
class BoatInterface : public AnnonController {
public:
    void setSteering(float input) override {
        // Boats have "Prop Walk" and slow response times
        current_steering = clamp(input, -1.0f, 1.0f);
        applyServoPWM(9, current_steering); // Pin 9: Rudder Servo
    }

    void setThrottle(float input) override {
        current_throttle = clamp(input, 0.0f, 1.0f);
        applyServoPWM(10, current_throttle); // Pin 10: Engine Throttle
    }

    void emergencyStop() override {
        applyServoPWM(10, 0.0f);   // Neutral
        applyServoPWM(11, -1.0f);  // Reverse Thrust (Braking on water)
    }

private:
    void applyServoPWM(int pin, float value) {
        // Direct PWM signal to the boat's mechanical actuators
    }
};
