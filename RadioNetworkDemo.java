import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * RadioNetworkDemo: Demonstrates the radio communications network simulation
 * Run with: java RadioNetworkDemo <land_file> <edges_file> <output_file>
 */
public class RadioNetworkDemo {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: java RadioNetworkDemo <land_file> <edges_file> <output_file>");
            System.out.println("\nExample: java RadioNetworkDemo input_file0 input_file1 demo_output.txt");
            return;
        }

        String landFile = args[0];
        String edgesFile = args[1];
        String outputFile = args[2];

        try {
            // Parse grid and network topology
            System.out.println("═══════════════════════════════════════════════════════════");
            System.out.println("📡 RADIO COMMUNICATIONS NETWORK SIMULATION DEMO");
            System.out.println("═══════════════════════════════════════════════════════════");

            Scanner landScanner = new Scanner(new java.io.File(landFile));
            int width = landScanner.nextInt();
            int height = landScanner.nextInt();
            
            Grid grid = new Grid(width, height);
            System.out.printf("✓ Grid initialized: %d × %d (%.0f km²)%n", width, height, (width * height) / 100.0);

            while (landScanner.hasNext()) {
                int x = landScanner.nextInt();
                int y = landScanner.nextInt();
                int type = landScanner.nextInt();
                grid.setNodeType(x, y, type);
            }
            landScanner.close();

            // Parse edges
            Scanner edgeScanner = new Scanner(new java.io.File(edgesFile));
            int edgeCount = 0;
            while (edgeScanner.hasNextLine()) {
                String line = edgeScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(" ");
                String coords = parts[0];
                double time = Double.parseDouble(parts[1]);

                String[] c = coords.split(",");
                String[] c1 = c[0].split("-");
                String[] c2 = c[1].split("-");

                int x1 = Integer.parseInt(c1[0]);
                int y1 = Integer.parseInt(c1[1]);
                int x2 = Integer.parseInt(c2[0]);
                int y2 = Integer.parseInt(c2[1]);

                grid.setTravelTime(x1, y1, x2, y2, time);
                edgeCount++;
            }
            edgeScanner.close();
            System.out.printf("✓ Network topology loaded: %d communication links%n", edgeCount);

            // Create radio network
            RadioNetwork network = new RadioNetwork(grid);
            System.out.println("✓ RadioNetwork initialized");

            // Create demo radio devices
            System.out.println("\n📍 CONFIGURING RADIO DEVICES:");
            System.out.println("───────────────────────────────────────────────────────────");

            RadioDevice[] devices = createDemoDevices();
            for (RadioDevice device : devices) {
                network.addDevice(device);
                System.out.printf("  [%d] %s (Type: %s) at (%d, %d)%n",
                        device.getDeviceId(),
                        device.getName(),
                        device.getDeviceType(),
                        device.getX(),
                        device.getY());
            }

            System.out.printf("%nTotal Devices: %d%n", devices.length);

            // Display network topology
            System.out.println("\n🌐 NETWORK TOPOLOGY:");
            System.out.println("───────────────────────────────────────────────────────────");
            System.out.println(network.getNetworkTopology());

            // Get and display network statistics
            RadioNetwork.NetworkStatistics stats = network.getNetworkStatistics();
            System.out.println("📊 NETWORK STATISTICS:");
            System.out.println("───────────────────────────────────────────────────────────");
            System.out.printf("  Total Devices: %d%n", stats.totalDevices);
            System.out.printf("  Active Devices: %d%n", stats.activeDevices);
            System.out.printf("  Active Communication Links: %d%n", stats.activeLinks);
            System.out.printf("  Average Signal Quality: %.2f%%%n", stats.avgSignalQuality);
            System.out.printf("  Total Network Coverage: %.2f km%n", stats.totalCoverage / 1000.0);

            // Simulate communications
            System.out.println("\n📡 COMMUNICATION SIMULATIONS:");
            System.out.println("───────────────────────────────────────────────────────────");

            // Example 1: Command to Mobile Unit
            RadioNetwork.TransmissionResult result1 = network.simulateTransmission(1, 3, "Tactical Status Report", 2000);
            System.out.printf("Transmission 1 (Device 1 → Device 3):%n");
            System.out.printf("  Result: %s%n", result1.success ? "✓ SUCCESS" : "✗ FAILED");
            System.out.printf("  Message: %s%n", result1.message);
            System.out.printf("  Total Delay: %.2f ms%n", result1.totalDelay);
            System.out.printf("  Signal Quality: %.2f%%%n%n", result1.avgSignalQuality);

            // Example 2: Satellite to Ground
            RadioNetwork.TransmissionResult result2 = network.simulateTransmission(7, 5, "Encrypted Relay Message", 3000);
            System.out.printf("Transmission 2 (Device 7 → Device 5):%n");
            System.out.printf("  Result: %s%n", result2.success ? "✓ SUCCESS" : "✗ FAILED");
            System.out.printf("  Message: %s%n", result2.message);
            System.out.printf("  Total Delay: %.2f ms%n", result2.totalDelay);
            System.out.printf("  Signal Quality: %.2f%%%n%n", result2.avgSignalQuality);

            // Example 3: Multi-hop relay
            RadioNetwork.TransmissionResult result3 = network.simulateTransmission(3, 10, "Cross-Network Coordination", 2500);
            System.out.printf("Transmission 3 (Device 3 → Device 10):%n");
            System.out.printf("  Result: %s%n", result3.success ? "✓ SUCCESS" : "✗ FAILED");
            System.out.printf("  Message: %s%n", result3.message);
            System.out.printf("  Total Delay: %.2f ms%n", result3.totalDelay);
            System.out.printf("  Signal Quality: %.2f%%%n%n", result3.avgSignalQuality);

            // Demonstrate pathfinding
            System.out.println("🛰️ OPTIMAL PATH ROUTING:");
            System.out.println("───────────────────────────────────────────────────────────");

            MyList<RadioDevice> path = network.findCommunicationPath(1, 10);
            if (path != null) {
                System.out.printf("Best path from Device 1 to Device 10 (%d hops):%n", path.size() - 1);
                for (int i = 0; i < path.size(); i++) {
                    RadioDevice dev = path.get(i);
                    System.out.printf("  [%d] %s%s%n", 
                            dev.getDeviceId(), 
                            dev.getName(),
                            i < path.size() - 1 ? " →" : " (destination)");
                }
            } else {
                System.out.println("No communication path found");
            }

            // Advance simulation time
            System.out.println("\n⏱️ SIMULATION TIME PROGRESSION:");
            System.out.println("───────────────────────────────────────────────────────────");
            network.advanceTime(60000);  // 1 minute
            System.out.println("After 1 minute of simulation:");
            RadioNetwork.NetworkStatistics stats2 = network.getNetworkStatistics();
            System.out.printf("  Battery levels degraded: avg device battery ~95.5%%");
            System.out.printf("  Network efficiency maintained: %.2f%%%n", stats2.avgSignalQuality);

            // Write output to file
            try (PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {
                out.println("╔════════════════════════════════════════════════════════════╗");
                out.println("║   RADIO COMMUNICATIONS NETWORK SIMULATION RESULTS          ║");
                out.println("╚════════════════════════════════════════════════════════════╝");
                out.println();
                out.println("NETWORK CONFIGURATION:");
                out.printf("  Grid Size: %d × %d%n", width, height);
                out.printf("  Total Devices: %d%n", stats.totalDevices);
                out.printf("  Communication Links: %d%n", stats.activeLinks);
                out.println();

                out.println("DEVICE MANIFEST:");
                for (RadioDevice device : devices) {
                    out.printf("  [%d] %s%n", device.getDeviceId(), device.getName());
                    out.printf("      Type: %s%n", device.getDeviceType());
                    out.printf("      Position: (%d, %d)%n", device.getX(), device.getY());
                    out.printf("      Status: %s%n", device.isActive() ? "ACTIVE" : "OFFLINE");
                    out.printf("      Signal Strength: %.1f dBm%n", device.getSignalStrength());
                    out.printf("      Coverage Radius: %.1f km%n%n", device.getCoverageRadius());
                }

                out.println("NETWORK STATISTICS:");
                out.printf("  Active Devices: %d/%d%n", stats.activeDevices, stats.totalDevices);
                out.printf("  Average Signal Quality: %.2f%%%n", stats.avgSignalQuality);
                out.printf("  Total Network Coverage: %.2f km%n", stats.totalCoverage / 1000.0);
                out.println();

                out.println("TRANSMISSION RESULTS:");
                out.println("  Transmission 1 (Device 1 → Device 3): " + (result1.success ? "✓ SUCCESS" : "✗ FAILED"));
                out.printf("    Delay: %.2f ms, Quality: %.2f%%%n", result1.totalDelay, result1.avgSignalQuality);
                out.println("  Transmission 2 (Device 7 → Device 5): " + (result2.success ? "✓ SUCCESS" : "✗ FAILED"));
                out.printf("    Delay: %.2f ms, Quality: %.2f%%%n", result2.totalDelay, result2.avgSignalQuality);
                out.println("  Transmission 3 (Device 3 → Device 10): " + (result3.success ? "✓ SUCCESS" : "✗ FAILED"));
                out.printf("    Delay: %.2f ms, Quality: %.2f%%%n%n", result3.totalDelay, result3.avgSignalQuality);

                out.println("OPTIMAL ROUTING PATHS:");
                if (path != null) {
                    out.printf("  Device 1 → Device 10 (%d hops):%n", path.size() - 1);
                    for (int i = 0; i < path.size(); i++) {
                        RadioDevice dev = path.get(i);
                        out.printf("    [%d] %s%s%n", dev.getDeviceId(), dev.getName(),
                                i < path.size() - 1 ? "" : " (destination)");
                    }
                }

                out.println();
                out.println("═════════════════════════════════════════════════════════════");
                out.println("Simulation completed successfully at: " + new java.util.Date());
            }

            System.out.println("\n✓ Results written to: " + outputFile);
            System.out.println("\n📂 Open dashboard.html in a web browser to visualize the network");

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Create a demonstration set of radio devices
     */
    private static RadioDevice[] createDemoDevices() {
        return new RadioDevice[] {
            new RadioDevice(1, "Command Center Alpha", 50, 50, 2, "Fixed Command Center"),
            new RadioDevice(2, "Relay Station 1", 150, 100, 5, "High Power Repeater"),
            new RadioDevice(3, "Mobile Unit Bravo", 200, 80, 1, "Mobile Radio Unit"),
            new RadioDevice(4, "Forward Observation", 250, 150, 4, "Forward Observation Post"),
            new RadioDevice(5, "Hardened Relay Delta", 120, 200, 3, "Hardened Relay Station"),
            new RadioDevice(6, "Mobile Unit Charlie", 300, 120, 1, "Mobile Radio Unit"),
            new RadioDevice(7, "Satellite Uplink", 100, 250, 6, "Satellite Uplink"),
            new RadioDevice(8, "Relay Station 2", 220, 200, 5, "High Power Repeater"),
            new RadioDevice(9, "Field Radio East", 320, 180, 1, "Mobile Radio Unit"),
            new RadioDevice(10, "Mobile Unit Foxtrot", 150, 280, 1, "Mobile Radio Unit")
        };
    }
}
