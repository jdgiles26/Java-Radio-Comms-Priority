/**
 * RadioNetwork: Manages a network of radio devices with pathfinding and communication simulation
 */
public class RadioNetwork {
    private MyList<RadioDevice> devices;
    private Grid grid;
    private long simulationTime;

    public RadioNetwork(Grid grid) {
        this.devices = new MyList<>();
        this.grid = grid;
        this.simulationTime = 0;
    }

    /**
     * Add a radio device to the network
     */
    public void addDevice(RadioDevice device) {
        devices.add(device);
    }

    /**
     * Get a device by ID
     */
    public RadioDevice getDevice(int deviceId) {
        for (int i = 0; i < devices.size(); i++) {
            RadioDevice dev = devices.get(i);
            if (dev.getDeviceId() == deviceId) {
                return dev;
            }
        }
        return null;
    }

    /**
     * Get all devices
     */
    public MyList<RadioDevice> getDevices() {
        return devices;
    }

    /**
     * Calculate distance between two devices
     */
    public double calculateDistance(RadioDevice a, RadioDevice b) {
        int dx = a.getX() - b.getX();
        int dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Check if two devices can communicate (within coverage and line of sight)
     */
    public boolean canCommunicate(RadioDevice a, RadioDevice b) {
        if (!a.canCommunicate() || !b.canCommunicate()) {
            return false;
        }

        double distance = calculateDistance(a, b);
        double coverageRadiusA = a.getCoverageRadius();
        double coverageRadiusB = b.getCoverageRadius();

        return distance <= coverageRadiusA || distance <= coverageRadiusB;
    }

    /**
     * Calculate signal quality between two devices based on distance and terrain
     * Returns value 0-100 (percentage)
     */
    public double calculateSignalQuality(RadioDevice a, RadioDevice b) {
        double distance = calculateDistance(a, b);
        double maxCoverage = Math.max(a.getCoverageRadius(), b.getCoverageRadius());

        // Signal degrades with distance
        double qualityFromDistance = 100.0 * (1.0 - (distance / maxCoverage));
        qualityFromDistance = Math.max(0, Math.min(100, qualityFromDistance));

        // Factor in device signal strengths
        double avgSignalStrength = (a.getSignalStrength() + b.getSignalStrength()) / 2.0;
        double signalFactor = (avgSignalStrength + 120.0) / 120.0;  // Normalize from -120 to 0 dBm

        return qualityFromDistance * signalFactor;
    }

    /**
     * Find optimal communication path using Dijkstra's algorithm
     */
    public MyList<RadioDevice> findCommunicationPath(int fromDeviceId, int toDeviceId) {
        RadioDevice start = getDevice(fromDeviceId);
        RadioDevice end = getDevice(toDeviceId);

        if (start == null || end == null || !start.canCommunicate() || !end.canCommunicate()) {
            return null;
        }

        // Use the grid pathfinder to find the geographic path
        Pathfinder pathfinder = new Pathfinder();
        MyList<Node> geoPath = pathfinder.findShortestPath(grid, start.getX(), start.getY(), end.getX(), end.getY());

        if (geoPath == null) {
            return null;
        }

        // Convert geographic path to device communication path
        MyList<RadioDevice> commPath = new MyList<>();
        commPath.add(start);  // Add starting device
        
        // Find devices along the path
        for (int i = 1; i < geoPath.size() - 1; i++) {
            Node node = geoPath.get(i);
            RadioDevice closestDevice = findClosestDevice(node.getX(), node.getY());
            if (closestDevice != null) {
                boolean alreadyInPath = false;
                for (int j = 0; j < commPath.size(); j++) {
                    if (commPath.get(j).getDeviceId() == closestDevice.getDeviceId()) {
                        alreadyInPath = true;
                        break;
                    }
                }
                if (!alreadyInPath) {
                    commPath.add(closestDevice);
                }
            }
        }

        // Add end device if not already in path
        boolean hasEnd = false;
        for (int j = 0; j < commPath.size(); j++) {
            if (commPath.get(j).getDeviceId() == end.getDeviceId()) {
                hasEnd = true;
                break;
            }
        }
        if (!hasEnd) {
            commPath.add(end);
        }

        return commPath;
    }

    /**
     * Find the closest device to a geographic location
     */
    private RadioDevice findClosestDevice(int x, int y) {
        RadioDevice closest = null;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < devices.size(); i++) {
            RadioDevice dev = devices.get(i);
            double dx = dev.getX() - x;
            double dy = dev.getY() - y;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < minDistance) {
                minDistance = distance;
                closest = dev;
            }
        }

        return closest;
    }

    /**
     * Simulate transmission of a message along a path
     */
    public TransmissionResult simulateTransmission(int fromDeviceId, int toDeviceId, String message, long durationMs) {
        MyList<RadioDevice> path = findCommunicationPath(fromDeviceId, toDeviceId);

        if (path == null || path.size() < 2) {
            return new TransmissionResult(false, "No communication path found", 0, 0);
        }

        double totalDelay = 0;
        double avgSignalQuality = 0;
        int hopCount = 0;

        // Calculate metrics for each hop
        for (int i = 0; i < path.size() - 1; i++) {
            RadioDevice current = path.get(i);
            RadioDevice next = path.get(i + 1);

            if (!canCommunicate(current, next)) {
                return new TransmissionResult(false, "Link lost at hop " + (i + 1), totalDelay, avgSignalQuality / hopCount);
            }

            double hopDelay = calculateDistance(current, next) * 0.1;  // Simplified delay model
            totalDelay += hopDelay;
            avgSignalQuality += calculateSignalQuality(current, next);
            hopCount++;

            // Drain battery for transmission
            current.drainBattery(durationMs);
        }

        avgSignalQuality /= hopCount;
        
        RadioDevice endpoint = path.get(path.size() - 1);
        endpoint.drainBattery(durationMs);

        simulationTime += (long) totalDelay;

        return new TransmissionResult(
            true,
            "Message transmitted successfully via " + hopCount + " hops",
            totalDelay,
            avgSignalQuality
        );
    }

    /**
     * Get network statistics
     */
    public NetworkStatistics getNetworkStatistics() {
        int activeDevices = 0;
        int totalLinks = 0;
        double avgSignalQuality = 0;
        double totalCoverage = 0;

        // Count active devices and coverage
        for (int i = 0; i < devices.size(); i++) {
            RadioDevice dev = devices.get(i);
            if (dev.canCommunicate()) {
                activeDevices++;
                totalCoverage += dev.getCoverageRadius();
            }
        }

        // Count active links
        for (int i = 0; i < devices.size(); i++) {
            for (int j = i + 1; j < devices.size(); j++) {
                RadioDevice devA = devices.get(i);
                RadioDevice devB = devices.get(j);
                if (canCommunicate(devA, devB)) {
                    totalLinks++;
                    avgSignalQuality += calculateSignalQuality(devA, devB);
                }
            }
        }

        if (totalLinks > 0) {
            avgSignalQuality /= totalLinks;
        }

        return new NetworkStatistics(
            devices.size(),
            activeDevices,
            totalLinks,
            avgSignalQuality,
            totalCoverage,
            simulationTime
        );
    }

    /**
     * Advance simulation time and update device states
     */
    public void advanceTime(long milliseconds) {
        simulationTime += milliseconds;
        
        // Simulate passive battery drain
        for (int i = 0; i < devices.size(); i++) {
            RadioDevice dev = devices.get(i);
            dev.drainBattery(milliseconds / 10.0);  // Passive drain rate
        }
    }

    /**
     * Get network topology as a visual string
     */
    public String getNetworkTopology() {
        StringBuilder sb = new StringBuilder();
        sb.append("Network Topology (").append(devices.size()).append(" devices):\n");

        for (int i = 0; i < devices.size(); i++) {
            RadioDevice dev = devices.get(i);
            sb.append(String.format("  [%d] %s at (%d,%d) - %s\n",
                    dev.getDeviceId(),
                    dev.getName(),
                    dev.getX(),
                    dev.getY(),
                    dev.isActive() ? "ACTIVE" : "OFFLINE"));

            // Show connections
            for (int j = 0; j < devices.size(); j++) {
                if (i == j) continue;
                RadioDevice other = devices.get(j);
                if (canCommunicate(dev, other)) {
                    double quality = calculateSignalQuality(dev, other);
                    sb.append(String.format("      -> Connected to [%d] %s (Quality: %.1f%%)\n",
                            other.getDeviceId(),
                            other.getName(),
                            quality));
                }
            }
        }

        return sb.toString();
    }

    /**
     * Result of a transmission attempt
     */
    public static class TransmissionResult {
        public final boolean success;
        public final String message;
        public final double totalDelay;
        public final double avgSignalQuality;

        public TransmissionResult(boolean success, String message, double totalDelay, double avgSignalQuality) {
            this.success = success;
            this.message = message;
            this.totalDelay = totalDelay;
            this.avgSignalQuality = avgSignalQuality;
        }

        @Override
        public String toString() {
            return String.format("TransmissionResult{success=%s, message='%s', delay=%.2f ms, quality=%.1f%%}",
                    success, message, totalDelay, avgSignalQuality);
        }
    }

    /**
     * Network statistics snapshot
     */
    public static class NetworkStatistics {
        public final int totalDevices;
        public final int activeDevices;
        public final int activeLinks;
        public final double avgSignalQuality;
        public final double totalCoverage;
        public final long simulationTime;

        public NetworkStatistics(int totalDevices, int activeDevices, int activeLinks,
                                 double avgSignalQuality, double totalCoverage, long simulationTime) {
            this.totalDevices = totalDevices;
            this.activeDevices = activeDevices;
            this.activeLinks = activeLinks;
            this.avgSignalQuality = avgSignalQuality;
            this.totalCoverage = totalCoverage;
            this.simulationTime = simulationTime;
        }

        @Override
        public String toString() {
            return String.format("NetworkStatistics{devices=%d/%d, links=%d, quality=%.1f%%, coverage=%.1f}",
                    activeDevices, totalDevices, activeLinks, avgSignalQuality, totalCoverage);
        }
    }
}
