#include <iostream>
#include <string>

// --- The Base Engine: Universal Logic ---
class AnnonVehicleBase {
protected:
    float safe_speed_min;
    float safe_speed_max;
    std::string vehicle_type;

public:
    virtual void processLidar(float distance, float angle) = 0; // Must be defined by vehicle
    virtual void checkEmergency(float g_force, bool is_responsive) = 0;

    void broadcast(std::string msg) {
        // Core Audio Output (4th-Grade)
        std::cout << "[" << vehicle_type << "]: " << msg << std::endl;
    }
};

// --- 1. The Glider Module (Energy & Glide Slope) ---
class AnnonGlider : public AnnonVehicleBase {
public:
    AnnonGlider() { vehicle_type = "GLIDER"; safe_speed_min = 45.0; }

    void processLidar(float distance, float angle) override {
        if (distance < 50.0) broadcast("Terrain approaching. Pull up.");
    }

    void checkEmergency(float g_force, bool is_responsive) override {
        if (g_force > 4.0 && !is_responsive) broadcast("Fail-Deadly: Deploying Chute.");
    }
};

// --- 2. The Boat Module (Draft & Submerged Hazards) ---
class AnnonBoat : public AnnonVehicleBase {
public:
    AnnonBoat() { vehicle_type = "BOAT"; }

    void processLidar(float distance, float angle) override {
        // In a boat, LiDAR looks for docks, buoys, and "Surface Breach"
        if (distance < 10.0) broadcast("Obstacle in water. Turn.");
    }

    void checkEmergency(float g_force, bool is_responsive) override {
        if (!is_responsive) broadcast("Man overboard protocol. Engaging GPS anchor.");
    }
};

// --- 3. The Car Module (Traffic & Pedestrians) ---
class AnnonCar : public AnnonVehicleBase {
public:
    AnnonCar() { vehicle_type = "CAR"; }

    void processLidar(float distance, float angle) override {
        if (distance < 5.0) broadcast("Object ahead. Braking.");
    }

    void checkEmergency(float g_force, bool is_responsive) override {
        if (g_force > 3.0) broadcast("Collision detected. Alerting EMS.");
    }
};

// --- 4. The Handheld/Pedestrian Module (The "Grey Man" Mode) ---
class AnnonHandheld : public AnnonVehicleBase {
public:
    AnnonHandheld() { vehicle_type = "PEDESTRIAN"; }

    void processLidar(float distance, float angle) override {
        if (distance < 2.0) broadcast("Step ahead.");
    }

    void checkEmergency(float g_force, bool is_responsive) override {
        if (g_force > 2.5 && !is_responsive) broadcast("Fall detected. tagging Hall of Records location.");
    }
};
