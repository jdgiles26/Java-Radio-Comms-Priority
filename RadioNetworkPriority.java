import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

// Main Simulation Class - Dynamic Geospatial Spectrum Allocation & Role-Based Priority Logic
public class RadioNetworkPriority {

    // --- Data Structures ---
    static class Node {
        String id;
        String callSign;
        String role;
        String platoon;
        int x, y;
        double battery;
        int priorityTier; // 0 = Max (Tier-0), 1 = High (Tier-1), 2 = Standard (Tier-2), 3 = Low (Tier-3)
        boolean isActiveTarget;
        double signalStrength;
        double bandwidth;

        public Node(String id, String callSign, String role, String platoon, int x, int y) {
            this.id = id;
            this.callSign = callSign;
            this.role = role;
            this.platoon = platoon;
            this.x = x;
            this.y = y;
            this.battery = 100.0;
            this.priorityTier = 2; // Default to Tier-2
            this.isActiveTarget = false;
            this.signalStrength = 95.0;
            this.bandwidth = 100.0;
        }
    }

    // Connection tracking for optimal routing
    static class Connection {
        String sourceId;
        String targetId;
        double latency;
        boolean isActive;

        public Connection(String sourceId, String targetId, double latency) {
            this.sourceId = sourceId;
            this.targetId = targetId;
            this.latency = latency;
            this.isActive = false;
        }
    }

    // --- Configuration ---
    static List<Node> network = new ArrayList<>();
    static List<Connection> connections = new ArrayList<>();
    static String CURRENT_ACTIVE_PLATOON = "NONE";
    static String CURRENT_COMMAND_SOURCE = "NONE";
    static long SIMULATION_START = System.currentTimeMillis();

    public static void main(String[] args) {
        initializeNetwork();
        
        // --- SIMULATE THE PRIORITY INTERRUPT EVENT ---
        // Scenario: Command calls Platoon A with tactical directive
        System.out.println("[SIMULATION INITIALIZED] Awaiting priority events...\n");
        
        // Event 1: Priority call to Platoon A
        triggerPriorityEvent("COMMAND_ALPHA", "PLATOON_A");
        
        // Generate outputs
        generateTerminalOutput();
        generateJSONData();
        generateConfigurationReport();
    }

    static void initializeNetwork() {
        // High Command - Tier 0 (Always Unrestricted)
        network.add(new Node("1", "COMMAND_ALPHA", "COMMANDER", "HQ", 50, 50));
        network.add(new Node("2", "TAC_COM_1", "TAC_COMMAND", "HQ", 100, 250));

        // Platoon A - Target for Priority (Will become Tier 1)
        network.add(new Node("3", "ALPHA_LEAD", "SQUAD_LEADER", "PLATOON_A", 200, 80));
        network.add(new Node("4", "ALPHA_RTO", "RTO", "PLATOON_A", 220, 85));
        network.add(new Node("4A", "ALPHA_MED", "MEDIC", "PLATOON_A", 210, 75));

        // Platoon B - Secondary (Will be throttled to Tier 3)
        network.add(new Node("5", "BRAVO_LEAD", "SQUAD_LEADER", "PLATOON_B", 300, 120));
        network.add(new Node("6", "BRAVO_RTO", "RTO", "PLATOON_B", 320, 130));

        // Platoon C - Secondary (Will be throttled to Tier 3)
        network.add(new Node("7", "CHARLIE_LEAD", "SQUAD_LEADER", "PLATOON_C", 150, 280));
        network.add(new Node("8", "CHARLIE_RTO", "RTO", "PLATOON_C", 160, 290));
        
        // Relay Network - Tier 1 (Support infrastructure)
        network.add(new Node("9", "RELAY_NORTH", "RELAY", "NET", 150, 100));
        network.add(new Node("10", "RELAY_SOUTH", "RELAY", "NET", 220, 200));
        network.add(new Node("11", "RELAY_EAST", "RELAY", "NET", 350, 150));

        // Initialize connections
        initializeConnections();
    }

    static void initializeConnections() {
        // Command to Tactical Command
        connections.add(new Connection("1", "2", 5.0));
        
        // Command to Relays
        connections.add(new Connection("1", "9", 8.5));
        connections.add(new Connection("1", "10", 12.0));
        
        // Relays to Platoons
        connections.add(new Connection("9", "3", 7.2));
        connections.add(new Connection("9", "4", 7.5));
        connections.add(new Connection("10", "5", 9.8));
        connections.add(new Connection("10", "7", 11.3));
        connections.add(new Connection("11", "5", 6.2));
        connections.add(new Connection("11", "7", 8.9));
    }

    static void triggerPriorityEvent(String sourceCallSign, String targetPlatoon) {
        CURRENT_ACTIVE_PLATOON = targetPlatoon;
        CURRENT_COMMAND_SOURCE = sourceCallSign;
        
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║        PRIORITY INTERRUPT PROTOCOL (DPIP) ACTIVATED      ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println("  SOURCE CALLSIGN: " + sourceCallSign);
        System.out.println("  TARGET SECTOR:   " + targetPlatoon);
        System.out.println("  TIMESTAMP:       " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")));
        System.out.println();
        
        // Apply priority logic to all nodes
        for (Node n : network) {
            // Reset Base
            n.isActiveTarget = false;
            n.signalStrength = 95.0;
            n.bandwidth = 100.0;
            
            // Logic 1: Command & TAC always Tier 0 (UNRESTRICTED)
            if (n.role.equals("COMMANDER") || n.role.equals("TAC_COMMAND")) {
                n.priorityTier = 0;
                n.signalStrength = 99.8;
                n.bandwidth = 100.0;
            }
            // Logic 2: Target Platoon gets Tier 1 (HIGH PRIORITY)
            else if (n.platoon.equals(targetPlatoon)) {
                n.priorityTier = 1;
                n.isActiveTarget = true;
                n.signalStrength = 99.5;
                n.bandwidth = 85.0; // Gets 85% of available bandwidth
            }
            // Logic 3: Relays stay Tier 1 (HIGH - Critical infrastructure)
            else if (n.role.equals("RELAY")) {
                n.priorityTier = 1;
                n.signalStrength = 98.0;
                n.bandwidth = 90.0;
            }
            // Logic 4: Secondary Platoons throttled to Tier 3 (LOW)
            else if (n.platoon.startsWith("PLATOON_") && !n.platoon.equals(targetPlatoon)) {
                n.priorityTier = 3;
                n.isActiveTarget = false;
                n.signalStrength = 70.0;
                n.bandwidth = 25.0; // Throttled to 25% - keep-alive only
            }
        }

        // Update connections to reflect active sector
        markActiveRoutes();
    }

    static void markActiveRoutes() {
        // Reset all connections
        for (Connection c : connections) {
            c.isActive = false;
        }

        // Mark routes that service the active platoon as "Active"
        for (Connection c : connections) {
            Node source = getNodeById(c.sourceId);
            Node target = getNodeById(c.targetId);

            if (source != null && target != null) {
                // Connection is active if it connects Command->Relay or Relay->ActivePlatoon
                if ((source.priorityTier == 0 && target.priorityTier == 1) ||
                    (source.priorityTier == 1 && target.platoon.equals(CURRENT_ACTIVE_PLATOON))) {
                    c.isActive = true;
                }
            }
        }
    }

    static Node getNodeById(String id) {
        for (Node n : network) {
            if (n.id.equals(id)) return n;
        }
        return null;
    }

    static void generateTerminalOutput() {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("📡 RADIO COMMUNICATIONS NETWORK SIMULATION - PRIORITY UPDATE");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("✓ Protocol: Dynamic Geospatial Priority (DPIP-V4) Active");
        System.out.println("✓ Monitor: VOICE_CHANNEL_01 [SECURE/ENCRYPTED]");
        System.out.println("✓ Timestamp: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        
        System.out.println("\n⚠️  PRIORITY INTERRUPT DETECTED");
        System.out.println("───────────────────────────────────────────────────────────────");
        System.out.println("  SOURCE:      " + CURRENT_COMMAND_SOURCE + " [TIER-0 - COMMAND]");
        System.out.println("  DIRECTIVE:   \"CONTACT " + CURRENT_ACTIVE_PLATOON + " - IMMEDIATE\"");
        System.out.println("  ACTION:      GEOSPATIAL BANDWIDTH LOCK INITIATED");
        System.out.println("  RESULT:      Sector Lock → Primary Platoon (" + CURRENT_ACTIVE_PLATOON + ")");

        System.out.println("\n📍 NETWORK TOPOLOGY STATUS BY PRIORITY TIER:");
        System.out.println("───────────────────────────────────────────────────────────────");
        
        // Print Tier 0 (Command)
        System.out.println("  [TIER-0] COMMAND AUTHORITY - STATUS: UNRESTRICTED");
        System.out.println("  ├─ 100% Bandwidth Available");
        System.out.println("  ├─ Pre-emptive Interrupt Capability");
        System.out.println("  └─ Signal Quality: MAXIMUM");
        printNodesByRole("COMMANDER");
        printNodesByRole("TAC_COMMAND");

        // Print Tier 1 (Active Platoon + Relays)
        System.out.println("\n  [TIER-1] ACTIVE SECTOR - STATUS: PRIORITY BOOST");
        System.out.println("  ├─ 85% Bandwidth Allocated");
        System.out.println("  ├─ Optimized Routing Enabled");
        System.out.println("  └─ Signal Quality: EXCELLENT");
        printNodesByPlatoon(CURRENT_ACTIVE_PLATOON);
        System.out.println("  ├─ Relay Nodes (Support Infrastructure):");
        printNodesByRole("RELAY");

        // Print Tier 3 (Secondary Units)
        System.out.println("\n  [TIER-3] SECONDARY UNITS - STATUS: THROTTLED");
        System.out.println("  ├─ 25% Bandwidth Allocated (Keep-Alive)");
        System.out.println("  ├─ Background Sync Only");
        System.out.println("  └─ Signal Quality: REDUCED");
        printNodesByPriority(3);

        System.out.println("\n🌐 OPTIMIZED ROUTING PATHS:");
        System.out.println("───────────────────────────────────────────────────────────────");
        System.out.println("  Primary Route:   COMMAND_ALPHA → RELAY_NORTH → ALPHA_LEAD");
        System.out.println("  Latency:         12ms [OPTIMIZED]");
        System.out.println("  Secondary Route: RELAY_SOUTH → BRAVO_LEAD (QUEUED)");
        System.out.println("  Latency:         28ms [QUEUED - THROTTLED]");
        
        System.out.println("\n✓ STATIC TIER ASSIGNMENTS (Permanent):");
        System.out.println("  • COMMANDER nodes:    Tier-0 (Unrestricted)");
        System.out.println("  • TAC_COMMAND nodes:  Tier-0 (Unrestricted)");
        System.out.println("  • RELAY nodes:        Tier-1 (High Priority - Infrastructure)");
        
        System.out.println("\n✓ DYNAMIC TIER ASSIGNMENTS (Call-Sign Based):");
        System.out.println("  • Target Platoon:     Tier-1 (Active Sector)");
        System.out.println("  • Secondary Platoons: Tier-3 (Throttled)");
        
        System.out.println("\n✓ Configuration saved to:");
        System.out.println("  → radio_priority_output.txt");
        System.out.println("  → network_data.js (Dashboard)");
        System.out.println("  → configuration_report.txt");
        System.out.println("\n📂 DASHBOARD: Open dashboard.html to view Live Tactical Map");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        // Save to file
        try (FileWriter writer = new FileWriter("radio_priority_output.txt")) {
            writer.write("═══════════════════════════════════════════════════════════════\n");
            writer.write("RADIO NETWORK PRIORITY SIMULATION OUTPUT\n");
            writer.write("═══════════════════════════════════════════════════════════════\n\n");
            writer.write("SIMULATION_TIMESTAMP=" + LocalDateTime.now() + "\n");
            writer.write("ACTIVE_PLATOON=" + CURRENT_ACTIVE_PLATOON + "\n");
            writer.write("COMMAND_SOURCE=" + CURRENT_COMMAND_SOURCE + "\n");
            writer.write("PROTOCOL=DPIP-V4\n");
            writer.write("STATUS=PRIORITY_LOCK_ACTIVE\n\n");
            
            writer.write("NETWORK_STATE:\n");
            for (Node n : network) {
                writer.write(String.format("  %s,%s,%s,%s,%d,%d,Tier-%d,Bw:%.0f%%,Signal:%.1f%%\n",
                    n.id, n.callSign, n.role, n.platoon, n.x, n.y, 
                    n.priorityTier, n.bandwidth, n.signalStrength));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void printNodesByRole(String role) {
        for (Node n : network) {
            if (n.role.equals(role)) {
                String marker = n.isActiveTarget ? " ◆ " : " • ";
                System.out.printf("  %s[%s] %-15s | Tier:%d | Bw:%.0f%% | Signal:%.1f%%\n", 
                    marker, n.id, n.callSign, n.priorityTier, n.bandwidth, n.signalStrength);
            }
        }
    }
    
    static void printNodesByPlatoon(String platoon) {
        for (Node n : network) {
            if (n.platoon.equals(platoon)) {
                String marker = n.isActiveTarget ? " ◆ " : " • ";
                System.out.printf("  %s[%s] %-15s | Tier:%d | Bw:%.0f%% | Signal:%.1f%%\n", 
                    marker, n.id, n.callSign, n.priorityTier, n.bandwidth, n.signalStrength);
            }
        }
    }

    static void printNodesByPriority(int tier) {
        for (Node n : network) {
            if (n.priorityTier == tier) {
                System.out.printf("  • [%s] %-15s | Tier:%d | Bw:%.0f%% | Signal:%.1f%%\n", 
                    n.id, n.callSign, n.priorityTier, n.bandwidth, n.signalStrength);
            }
        }
    }

    // Generate JavaScript data file for dashboard visualization
    static void generateJSONData() {
        try (FileWriter writer = new FileWriter("network_data.js")) {
            writer.write("// Auto-generated Radio Network Data\n");
            writer.write("// Timestamp: " + LocalDateTime.now() + "\n\n");
            writer.write("const networkData = " + nodesToJson() + ";\n\n");
            writer.write("const connectionData = " + connectionsToJson() + ";\n\n");
            writer.write("const simulationState = " + simulationStateToJson() + ";\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String nodesToJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < network.size(); i++) {
            Node n = network.get(i);
            sb.append(String.format(
                "  {id:'%s', callSign:'%s', role:'%s', platoon:'%s', x:%d, y:%d, tier:%d, active:%b, bw:%.1f, signal:%.1f}",
                n.id, n.callSign, n.role, n.platoon, n.x, n.y, n.priorityTier, n.isActiveTarget, n.bandwidth, n.signalStrength
            ));
            if (i < network.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");
        return sb.toString();
    }

    static String connectionsToJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < connections.size(); i++) {
            Connection c = connections.get(i);
            sb.append(String.format(
                "  {source:'%s', target:'%s', latency:%.1f, active:%b}",
                c.sourceId, c.targetId, c.latency, c.isActive
            ));
            if (i < connections.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");
        return sb.toString();
    }

    static String simulationStateToJson() {
        return String.format("{\n" +
            "  activePlatoon: '%s',\n" +
            "  commandSource: '%s',\n" +
            "  protocol: 'DPIP-V4',\n" +
            "  status: 'PRIORITY_LOCK_ACTIVE',\n" +
            "  timestamp: '%s'\n" +
            "}", CURRENT_ACTIVE_PLATOON, CURRENT_COMMAND_SOURCE, LocalDateTime.now());
    }

    static void generateConfigurationReport() {
        try (FileWriter writer = new FileWriter("configuration_report.txt")) {
            writer.write("╔════════════════════════════════════════════════════════════════╗\n");
            writer.write("║   DYNAMIC GEOSPATIAL SPECTRUM ALLOCATION - CONFIG REPORT      ║\n");
            writer.write("╚════════════════════════════════════════════════════════════════╝\n\n");
            
            writer.write("1. TIER STRUCTURE\n");
            writer.write("─────────────────────────────────────────────────────────────\n");
            writer.write("   Tier-0 (UNRESTRICTED):     COMMANDER, TAC_COMMAND nodes\n");
            writer.write("   Tier-1 (HIGH PRIORITY):    Active Sector + RELAY nodes\n");
            writer.write("   Tier-2 (STANDARD):         Default tier assignment\n");
            writer.write("   Tier-3 (THROTTLED):        Secondary units (Keep-alive only)\n\n");
            
            writer.write("2. BANDWIDTH ALLOCATION RULES\n");
            writer.write("─────────────────────────────────────────────────────────────\n");
            writer.write("   Tier-0:  100% (Full capacity, pre-emptive interrupt)\n");
            writer.write("   Tier-1:  85-90% (Active sector optimization)\n");
            writer.write("   Tier-2:  100% (Standard operations)\n");
            writer.write("   Tier-3:  25% (Background sync, minimal throughput)\n\n");
            
            writer.write("3. SIGNAL QUALITY TARGETS\n");
            writer.write("─────────────────────────────────────────────────────────────\n");
            writer.write("   Tier-0:  99.8% (Maximum clarity)\n");
            writer.write("   Tier-1:  98-99.5% (Excellent quality)\n");
            writer.write("   Tier-3:  70% (Degraded but functional)\n\n");
            
            writer.write("4. ACTIVE CONFIGURATION\n");
            writer.write("─────────────────────────────────────────────────────────────\n");
            writer.write("   Active Platoon: " + CURRENT_ACTIVE_PLATOON + "\n");
            writer.write("   Command Source: " + CURRENT_COMMAND_SOURCE + "\n");
            writer.write("   Sector Lock: ENABLED\n");
            writer.write("   Geospatial Focus: (200, 80) - ALPHA_LEAD position\n");
            writer.write("   Relay Support: RELAY_NORTH, RELAY_SOUTH, RELAY_EAST\n\n");
            
            writer.write("5. NODE INVENTORY\n");
            writer.write("─────────────────────────────────────────────────────────────\n");
            for (Node n : network) {
                writer.write(String.format("   [%s] %s | Role: %-12s | Platoon: %-10s | Tier: %d\n",
                    n.id, n.callSign, n.role, n.platoon, n.priorityTier));
            }
            
            writer.write("\n6. ROUTING OPTIMIZATION\n");
            writer.write("─────────────────────────────────────────────────────────────\n");
            for (Connection c : connections) {
                if (c.isActive) {
                    Node src = getNodeById(c.sourceId);
                    Node tgt = getNodeById(c.targetId);
                    writer.write(String.format("   ACTIVE: %s → %s (Latency: %.1fms)\n",
                        src.callSign, tgt.callSign, c.latency));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
