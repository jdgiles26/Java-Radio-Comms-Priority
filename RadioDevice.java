import java.util.Objects;

/**
 * Represents a radio device in the communications network
 */
public class RadioDevice {
    private int deviceId;
    private String name;
    private int x, y;
    private int type;  // 0-6 representing different radio types
    private String deviceType;
    private double signalStrength;
    private boolean isActive;
    private double batteryLevel;
    private String frequency;
    private MyList<Integer> supportOptions;

    public RadioDevice(int deviceId, String name, int x, int y, int type, String deviceType) {
        this.deviceId = deviceId;
        this.name = name;
        this.x = x;
        this.y = y;
        this.type = type;
        this.deviceType = deviceType;
        this.signalStrength = 95.0;  // dBm
        this.isActive = true;
        this.batteryLevel = 100.0;
        this.frequency = "148.00 MHz";
        this.supportOptions = new MyList<>();
    }

    // Getters and Setters
    public int getDeviceId() { return deviceId; }
    public String getName() { return name; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getType() { return type; }
    public String getDeviceType() { return deviceType; }
    public double getSignalStrength() { return signalStrength; }
    public boolean isActive() { return isActive; }
    public double getBatteryLevel() { return batteryLevel; }
    public String getFrequency() { return frequency; }
    public MyList<Integer> getSupportOptions() { return supportOptions; }

    public void setSignalStrength(double signalStrength) { 
        this.signalStrength = Math.max(-120, Math.min(0, signalStrength)); 
    }
    public void setActive(boolean active) { isActive = active; }
    public void setBatteryLevel(double batteryLevel) { 
        this.batteryLevel = Math.max(0, Math.min(100, batteryLevel)); 
    }
    public void setFrequency(String frequency) { this.frequency = frequency; }

    public void addSupportOption(int option) {
        supportOptions.add(option);
    }

    /**
     * Calculate the coverage radius based on device type and signal strength
     */
    public double getCoverageRadius() {
        switch (type) {
            case 5:  // High-power repeater
                return 150.0;
            case 6:  // Satellite uplink
                return 400.0;  // Global coverage
            case 2:  // Command center
                return 100.0;
            case 3:  // Hardened relay
                return 120.0;
            case 4:  // Forward observation post
                return 80.0;
            case 1:  // Mobile radio unit
                return 50.0;
            default: // Standard terrain
                return 30.0;
        }
    }

    /**
     * Simulate battery drain during transmission
     */
    public void drainBattery(double durationMs) {
        double drainRate = 0.05 * (signalStrength / -50.0);  // Higher signal = faster drain
        batteryLevel = Math.max(0, batteryLevel - (drainRate * durationMs / 1000.0));
    }

    /**
     * Check if device can communicate based on status and battery
     */
    public boolean canCommunicate() {
        return isActive && batteryLevel > 5.0 && signalStrength > -100;
    }

    @Override
    public String toString() {
        return String.format("RadioDevice{id=%d, name='%s', pos=(%d,%d), type=%s, signal=%.1f dBm, battery=%.1f%%, active=%s}",
                deviceId, name, x, y, deviceType, signalStrength, batteryLevel, isActive);
    }
}
