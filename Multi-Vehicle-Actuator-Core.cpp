#include <iostream>
#include <algorithm>

// --- Universal Actuator Logic ---
class VehicleActuators {
protected:
    // Helper to map 4th-grade logic (-1.0 to 1.0) to raw hardware PWM (1000ms to 2000ms)
    int mapToPWM(float input) {
        float clamped = std::max(-1.0f, std::min(1.0f, input));
        return static_cast<int>(1500 + (clamped * 500));
    }

public:
    virtual void updateMechanicals(float x_axis, float y_axis) = 0;
};

// --- 1. Glider: Ailerons & Flaps ---
class GliderActuators : public VehicleActuators {
public:
    void updateMechanicals(float roll, float lift_req) override {
        int aileron_pwm = mapToPWM(roll);     // Control banking
        int flap_pwm = mapToPWM(lift_req);   // Control lift/drag
        
        writeToServo(1, aileron_pwm); // Servo 1: Ailerons
        writeToServo(2, flap_pwm);    // Servo 2: Flaps
    }

private:
    void writeToServo(int pin, int pwm) {
        // Direct Hardware I/O to Glider servos
    }
};

// --- 2. Boat: Rudder Control ---
class BoatActuators : public VehicleActuators {
public:
    void updateMechanicals(float rudder_input, float unused) override {
        int rudder_pwm = mapToPWM(rudder_input);
        
        writeToHydraulics(rudder_pwm); // Control the outboard or rudder
    }

private:
    void writeToHydraulics(int pwm) {
        // Signal to the boat's steering actuator
    }
};

// --- 3. Car: Steering Rack ---
class CarActuators : public VehicleActuators {
public:
    void updateMechanicals(float steer_angle, float unused) override {
        // Cars often use a CAN-bus message for Electronic Power Steering (EPS)
        int raw_steer = static_cast<int>(steer_angle * 5000); // Degrees/Torque units
        
        sendToEPS(0x180, raw_steer); 
    }

private:
    void sendToEPS(int id, int val) {
        // Electronic Power Steering handshake
    }
};
