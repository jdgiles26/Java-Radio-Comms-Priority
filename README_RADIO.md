# WizardMap - Radio Communications Network Simulation

A Java project that simulates **tactical radio communications network routing** using grid-based pathfinding. The system optimizes deployment of radio relay stations, command centers, and personnel positioning across complex terrain. Utilizes custom data structures and Dijkstra's algorithm for efficient route calculation and signal propagation modeling.

## Quick Start

### Multi-Dashboard Control Center
Open the main control center with both simulation modes:
```bash
index.html  # Opens menu with "Shortest Path" and "Hard Knocks Radio Net" tabs
```

### Classic Shortest Path Mode
```bash
java Main input_file0 input_file1 input_file2 output_file
```
Then open `dashboard_shortest_path.html` in a web browser to visualize the network.

### Hard Knocks Radio Net (DPIP Priority System)
```bash
java RadioNetworkPriority
```
Then open `dashboard_radio_priority.html` (or access via `index.html` menu) to visualize tactical priority allocation.

## Military Radio Communications Use Cases

### Core Features

#### 1. Radio Network Architecture
- **Grid Nodes**: Represent geographic waypoints, relay stations, command posts (400×400 coverage area)
- **Node Types**: 
  - Type 0: Standard terrain/passive nodes
  - Type 1: Mobile radio units (vehicles, personnel)
  - Type 2: Fixed command centers
  - Type 3: Hardened relay stations (EMP-resistant)
  - Type 4: Forward observation posts
  - Type 5: High-power repeater stations
  - Type 6: Satellite uplink nodes

- **Travel Times**: Signal propagation delay, link quality (measured in milliseconds), transmission latency

#### 2. Dijkstra's Algorithm for Signal Routing
- Finds optimal communication path between tactical units
- Accounts for variable signal strength across terrain and distance
- Minimizes latency for time-critical operations
- Avoids jammed or unavailable frequencies/channels

#### 3. Mission Planning & Deployment
- **Objectives**: Sequential radio station deployments or reconnaissance positions
- **Start Position**: Command center or mobile command post
- **Reveal Radius**: Limited battlefield visibility (tactical awareness radius)
- **Wizard Options**: Emergency support (air support, satellite relay, reinforcements)

#### 4. Real-World Tactical Scenarios
- **Command & Control (C2)**: Positioning field radios to maintain connectivity across coverage area
- **SIGINT Operations**: Locating enemy radio transmitters by triangulating signals from multiple listening posts
- **Disaster Response**: Establishing emergency communication networks in disaster zones with damaged infrastructure
- **Cross-border Operations**: Finding optimal routes for radio relay teams through complex terrain while avoiding detection
- **Mobile Ad-Hoc Networks**: Establishing temporary communication networks with minimal infrastructure

## System Architecture

### Data Structures (Custom Implementation)
- **MyHash**: O(1) lookup for edge weights and node metadata
- **MyLinkedList**: Efficient sequential processing of node traversals
- **MyPriorityQueue**: Dijkstra's algorithm implementation
- **Grid**: 2D node management with weighted graph edges

### Key Classes
- `Grid.java`: Spatial representation and travel cost management
- `Pathfinder.java`: Dijkstra pathfinding with obstacle avoidance
- `Mission.java`: Multi-objective planning and deployment sequences
- `RadioNetwork.java`: Radio-specific simulation features
- `WizardHelper.java`: Mission support and tactical options

## Radio Communications Dashboard

A web-based visualization panel provides real-time monitoring of:
- **Radio Device Positioning**: Interactive map showing all radio nodes
- **Signal Strength Visualization**: Heat maps indicating coverage areas
- **Network Topology**: Graph representation of possible communication paths
- **Routing Paths**: Dijkstra-computed optimal paths between devices
- **Mission Progress**: Real-time deployment status and objectives completion
- **Device Status**: Online/offline status, signal quality, battery levels
- **Emergency Support**: Activation of wizard options for tactical support

**Access**: Open `index.html` in a modern web browser for the command center menu with multiple dashboard options.

### Dashboard Options

#### 1. Shortest Path (Classic Mode)
Traditional grid-based pathfinding with Dijkstra's algorithm. Optimizes routes through terrain with variable costs.
- **File**: `dashboard_shortest_path.html`
- **Use Case**: Mission planning, optimal relay station deployment, terrain-aware routing
- **Features**: Grid visualization, multi-objective sequencing, obstacle avoidance

#### 2. Hard Knocks Radio Net (DPIP Priority Mode)
Dynamic Geospatial Priority allocation with role-based QoS management. Implements tactical spectrum sharing with preemptive interrupt capabilities.
- **File**: `dashboard_radio_priority.html`
- **Use Case**: Combat operations, emergency response, VIP/command prioritization
- **Features**: Tier-based access control, bandwidth throttling, sector locking, active target highlighting

## Input File Formats

### input_file0 (Land Configuration)
```
400 400                  # Grid dimensions (width height)
x y type                 # Node coordinates and type (repeating)
```

**Node Types**:
- 0: Passable terrain
- 1: Mobile radio unit
- 2: Command center
- 3: Hardened relay station
- 4: Forward observation post
- 5: High-power repeater
- 6: Satellite uplink

### input_file1 (Edge Weights - Travel Times/Signal Delays)
```
x1-y1,x2-y2 delay      # Adjacent nodes and propagation delay in milliseconds
```

Example: `0-0,1-0 72553.287576` means the link between (0,0) and (1,0) has 72,553 ms delay.

### input_file2 (Mission Objectives)
```
reveal_radius                        # Tactical awareness radius
start_x start_y                      # Command center location
objective_x objective_y [options]    # Radio stations to deploy
...                                  # (repeating for each objective)
```

**Wizard Options**:
- Option 2: Air support (reduce next travel time by 50%)
- Option 3: Satellite relay (instant communication)
- Option 4: Ground reinforcements (bypass obstacles)
- Option 5: Electronic warfare support
- Option 6: Medical/supply drop

## Running the Simulation

### Standard Mode
```bash
java Main input_file0 input_file1 input_file2 output_file
```

### Visualization Mode
1. Run the Java simulation to generate output
2. Open `dashboard.html` in a modern web browser (Chrome, Firefox, Safari, Edge)
3. Load the output file or use demo data to visualize network topology
4. Monitor real-time device connections and signal routing
5. Interact with the visualization to select devices and paths

### Demo Simulation
1. Click "Load Demo Configuration" in the dashboard
2. Observe the network setup with 10 radio devices
3. Watch the pathfinding algorithm select optimal routes
4. Monitor signal propagation across the network

## Technology Stack
- **Language**: Java 8+
- **Algorithm**: Dijkstra's shortest path
- **Visualization**: HTML5 Canvas + JavaScript
- **Data Structures**: Custom implementations (no stdlib collections)
- **Browser Support**: Chrome 90+, Firefox 88+, Safari 14+, Edge 90+

## Performance Characteristics
- **Grid Size**: 400×400 (160,000 nodes)
- **Pathfinding**: O((V + E) log V) using Dijkstra with custom priority queue
- **Mission Planning**: O(n × pathfinding_cost) where n = number of objectives
- **Dashboard Rendering**: 60 FPS on modern browsers

## Example Output
```
Mission Objective 1: Navigate to (196, 251)
Path: (0,0) -> (1,0) -> (1,1) -> ... -> (196, 251)
Total Delay: 5,432,187 ms (approximately 1.5 hours)
Available Support: Air support, Satellite relay
Status: COMPLETED

Mission Objective 2: Navigate to (74, 131)
Path: (196,251) -> (195,250) -> ... -> (74, 131)
Total Delay: 3,187,456 ms (approximately 53 minutes)
Available Support: Ground reinforcements, Electronic warfare
Status: IN_PROGRESS
```

## Tactical Considerations

### Signal Propagation
- **Path Loss**: Represented by variable travel times based on terrain
- **Multipath Fading**: Modeled through edge weight variation
- **Line of Sight**: Grid boundaries and obstacles represent terrain blocking

### Network Reliability
- **Redundant Paths**: Dijkstra finds primary route; secondary routes available
- **Link Quality**: Edge weights represent overall link reliability
- **Fallback Routes**: Mission planning provides alternative paths

### Deployment Strategy
- **Phased Rollout**: Sequential objective completion for staged network deployment
- **Resource Constraints**: Travel times reflect logistical limitations
- **Adaptive Routing**: Dynamic path recalculation as network grows

## Extensions & Customization

### Future Enhancements
- Real-time network status updates
- Frequency allocation and channel management
- Encryption key distribution
- Network traffic simulation
- Threat/jamming modeling
- Multi-objective optimization (cost vs. reliability)

### Custom Scenarios
Modify input files to simulate:
- Urban environments (higher edge weights)
- Rural/remote areas (longer propagation delays)
- Disaster zones (sparse node placement)
- Contested areas (strategic node positioning)




-------dev start terminal output ---------
@jdgiles3 ➜ /workspaces/Java-WizardMap (codespace-laughing-engine-v6rjvgpjp74pcwrrj) $ java RadioNetworkDemo input_file0 input_file1 radio_demo_output.txt 2>&1
═══════════════════════════════════════════════════════════
📡 RADIO COMMUNICATIONS NETWORK SIMULATION DEMO
═══════════════════════════════════════════════════════════
✓ Grid initialized: 400 × 400 (1600 km²)
✓ Network topology loaded: 319200 communication links
✓ RadioNetwork initialized

📍 CONFIGURING RADIO DEVICES:
───────────────────────────────────────────────────────────
  [1] Command Center Alpha (Type: Fixed Command Center) at (50, 50)
  [2] Relay Station 1 (Type: High Power Repeater) at (150, 100)
  [3] Mobile Unit Bravo (Type: Mobile Radio Unit) at (200, 80)
  [4] Forward Observation (Type: Forward Observation Post) at (250, 150)
  [5] Hardened Relay Delta (Type: Hardened Relay Station) at (120, 200)
  [6] Mobile Unit Charlie (Type: Mobile Radio Unit) at (300, 120)
  [7] Satellite Uplink (Type: Satellite Uplink) at (100, 250)
  [8] Relay Station 2 (Type: High Power Repeater) at (220, 200)
  [9] Field Radio East (Type: Mobile Radio Unit) at (320, 180)
  [10] Mobile Unit Foxtrot (Type: Mobile Radio Unit) at (150, 280)

Total Devices: 10

🌐 NETWORK TOPOLOGY:
───────────────────────────────────────────────────────────
Network Topology (10 devices):
  [1] Command Center Alpha at (50,50) - ACTIVE
      -> Connected to [2] Relay Station 1 (Quality: 45.6%)
      -> Connected to [7] Satellite Uplink (Quality: 86.8%)
  [2] Relay Station 1 at (150,100) - ACTIVE
      -> Connected to [1] Command Center Alpha (Quality: 45.6%)
      -> Connected to [3] Mobile Unit Bravo (Quality: 114.8%)
      -> Connected to [4] Forward Observation (Quality: 45.6%)
      -> Connected to [5] Hardened Relay Delta (Quality: 54.5%)
      -> Connected to [7] Satellite Uplink (Quality: 108.3%)
      -> Connected to [8] Relay Station 2 (Quality: 33.4%)
  [3] Mobile Unit Bravo at (200,80) - ACTIVE
      -> Connected to [2] Relay Station 1 (Quality: 114.8%)
      -> Connected to [7] Satellite Uplink (Quality: 90.8%)
      -> Connected to [8] Relay Station 2 (Quality: 33.9%)
  [4] Forward Observation at (250,150) - ACTIVE
      -> Connected to [2] Relay Station 1 (Quality: 45.6%)
      -> Connected to [6] Mobile Unit Charlie (Quality: 48.6%)
      -> Connected to [7] Satellite Uplink (Quality: 98.4%)
      -> Connected to [8] Relay Station 2 (Quality: 109.5%)
      -> Connected to [9] Field Radio East (Quality: 8.6%)
  [5] Hardened Relay Delta at (120,200) - ACTIVE
      -> Connected to [2] Relay Station 1 (Quality: 54.5%)
      -> Connected to [7] Satellite Uplink (Quality: 155.0%)
      -> Connected to [8] Relay Station 2 (Quality: 59.7%)
      -> Connected to [10] Mobile Unit Foxtrot (Quality: 51.6%)
  [6] Mobile Unit Charlie at (300,120) - ACTIVE
      -> Connected to [4] Forward Observation (Quality: 48.6%)
      -> Connected to [7] Satellite Uplink (Quality: 72.3%)
      -> Connected to [8] Relay Station 2 (Quality: 44.0%)
  [7] Satellite Uplink at (100,250) - ACTIVE
      -> Connected to [1] Command Center Alpha (Quality: 86.8%)
      -> Connected to [2] Relay Station 1 (Quality: 108.3%)
      -> Connected to [3] Mobile Unit Bravo (Quality: 90.8%)
      -> Connected to [4] Forward Observation (Quality: 98.4%)
      -> Connected to [5] Hardened Relay Delta (Quality: 155.0%)
      -> Connected to [6] Mobile Unit Charlie (Quality: 72.3%)
      -> Connected to [8] Relay Station 2 (Quality: 120.9%)
      -> Connected to [9] Field Radio East (Quality: 75.8%)
      -> Connected to [10] Mobile Unit Foxtrot (Quality: 153.0%)
  [8] Relay Station 2 at (220,200) - ACTIVE
      -> Connected to [2] Relay Station 1 (Quality: 33.4%)
      -> Connected to [3] Mobile Unit Bravo (Quality: 33.9%)
      -> Connected to [4] Forward Observation (Quality: 109.5%)
      -> Connected to [5] Hardened Relay Delta (Quality: 59.7%)
      -> Connected to [6] Mobile Unit Charlie (Quality: 44.0%)
      -> Connected to [7] Satellite Uplink (Quality: 120.9%)
      -> Connected to [9] Field Radio East (Quality: 57.4%)
      -> Connected to [10] Mobile Unit Foxtrot (Quality: 52.2%)
  [9] Field Radio East at (320,180) - ACTIVE
      -> Connected to [4] Forward Observation (Quality: 8.6%)
      -> Connected to [7] Satellite Uplink (Quality: 75.8%)
      -> Connected to [8] Relay Station 2 (Quality: 57.4%)
  [10] Mobile Unit Foxtrot at (150,280) - ACTIVE
      -> Connected to [5] Hardened Relay Delta (Quality: 51.6%)
      -> Connected to [7] Satellite Uplink (Quality: 153.0%)
      -> Connected to [8] Relay Station 2 (Quality: 52.2%)

📊 NETWORK STATISTICS:
───────────────────────────────────────────────────────────
  Total Devices: 10
  Active Devices: 10
  Active Communication Links: 23
  Average Signal Quality: 74.82%
  Total Network Coverage: 1.20 km

📡 COMMUNICATION SIMULATIONS:
───────────────────────────────────────────────────────────
Transmission 1 (Device 1 → Device 3):
  Result: ✗ FAILED
  Message: No communication path found
  Total Delay: 0.00 ms
  Signal Quality: 0.00%

Transmission 2 (Device 7 → Device 5):
  Result: ✓ SUCCESS
  Message: Message transmitted successfully via 1 hops
  Total Delay: 5.39 ms
  Signal Quality: 155.05%

Transmission 3 (Device 3 → Device 10):
  Result: ✓ SUCCESS
  Message: Message transmitted successfully via 3 hops
  Total Delay: 28.22 ms
  Signal Quality: 66.80%

---

## Hard Knocks Radio Net - Dynamic Priority System (DPIP-V4)

### Overview

**Hard Knocks Radio Net** implements a **Dynamic Geospatial Spectrum Allocation & Role-Based Priority Logic** system designed for high-contention tactical operations where simultaneous communication demands exceed available bandwidth.

The system employs a **Dynamic Priority Interrupt Protocol (DPIP)** that actively monitors voice traffic for call-sign metadata tags and dynamically reallocates spectrum resources based on command authority and tactical necessity.

### Core Concepts

#### 1. Priority Tier Structure

The network manages devices in four hierarchical tiers:

| Tier | Name | Bandwidth | Key Characteristics | Assignment |
|------|------|-----------|---------------------|------------|
| **Tier-0** | COMMAND | 100% | Unrestricted, pre-emptive interrupt, maximum clarity | COMMANDER, TAC_COMMAND (Static) |
| **Tier-1** | ACTIVE SECTOR | 85-90% | High priority, optimized routing, excellent signal | Target Platoon + RELAY nodes (Dynamic) |
| **Tier-2** | STANDARD | 100% | Default tier, normal operations | Unassigned nodes |
| **Tier-3** | THROTTLED | 25% | Keep-alive only, background sync, minimal throughput | Secondary units during priority lock (Dynamic) |

#### 2. Static Priority Assignment

Certain node roles are **permanently assigned** to fixed priority tiers:

- **COMMANDER nodes**: Always Tier-0 (Unrestricted access)
- **TAC_COMMAND nodes**: Always Tier-0 (Unrestricted access)
- **RELAY nodes**: Always Tier-1 (High priority infrastructure)

#### 3. Dynamic Priority Allocation (Call-Sign Based)

When a **Tier-0 command authority** issues a priority directive, the system executes a **Geospatial Bandwidth Lock**:

1. **Detection**: System detects voice traffic containing target call-sign metadata (e.g., "PLATOON_A")
2. **Verification**: Command authority is verified against role-based access control
3. **Sector Lock**: System identifies geographic cluster associated with target call-sign
4. **Reallocation**: Network topology dynamically re-routes mesh connections to prioritize target location
5. **Throttling**: All secondary units automatically shifted to Tier-3 (25% bandwidth)
6. **Optimization**: Relay infrastructure optimized to support active sector

### Technical Architecture

#### Data Structure

```java
static class Node {
    String id;                  // Unique device identifier
    String callSign;            // Military call-sign (e.g., "ALPHA_LEAD")
    String role;                // Node type (COMMANDER, RTO, RELAY, etc.)
    String platoon;             // Tactical unit designation
    int x, y;                   // Geospatial coordinates
    double battery;             // Power level (%)
    int priorityTier;           // 0-3 (Tier-0 to Tier-3)
    boolean isActiveTarget;     // Flag for sector lock
    double signalStrength;      // Link quality (%)
    double bandwidth;           // Allocated bandwidth (%)
}
```

#### Connection Management

```java
static class Connection {
    String sourceId;            // Source node ID
    String targetId;            // Target node ID
    double latency;             // Propagation delay (ms)
    boolean isActive;           // Is connection part of active sector
}
```

### Operational Scenarios

#### Scenario 1: Standard Operations (No Priority Lock)

```
Network State: NORMAL
├─ Tier-0 (Command): Full bandwidth, unrestricted access
├─ Tier-1 (Relays): High priority infrastructure
├─ Tier-2 (Standard): Normal operational tier
└─ Tier-3 (Secondary): Standard 100% allocation
```

#### Scenario 2: Priority Interrupt (Active Sector Lock)

```
EVENT: Command Authority issues "CONTACT PLATOON_A - IMMEDIATE"

Network State: PRIORITY_LOCK_ACTIVE
├─ Tier-0 (Command): 100% bandwidth - UNRESTRICTED
│  └─ Pre-emptive interrupt capability
│  └─ Signal Quality: 99.8% (Maximum)
├─ Tier-1 (Active Sector): 85% bandwidth - PRIORITY BOOST
│  └─ Target: PLATOON_A location cluster (200, 80)
│  └─ Includes: ALPHA_LEAD, ALPHA_RTO, ALPHA_MED
│  └─ Signal Quality: 99.5% (Excellent)
├─ Tier-1 (Relays): 90% bandwidth - SUPPORT INFRASTRUCTURE
│  └─ Optimized routing to active sector
│  └─ Signal Quality: 98% (Excellent)
└─ Tier-3 (Secondary): 25% bandwidth - THROTTLED
   └─ PLATOON_B, PLATOON_C: Keep-alive connectivity only
   └─ Signal Quality: 70% (Degraded but functional)
```

### Bandwidth Allocation Rules

```
Tier-0:  100% full capacity
         ├─ Pre-emptive interrupt capability
         ├─ Priority voice channels
         └─ Reserved slots for emergency

Tier-1:  85-90% (Active Sector: 85%, Relays: 90%)
         ├─ Optimized routing enabled
         ├─ Enhanced signal processing
         └─ Dedicated bandwidth pools

Tier-2:  100% standard allocation
         └─ Normal operations tier

Tier-3:  25% keep-alive only
         ├─ Background sync heartbeat (5% baseline)
         ├─ Emergency callback (up to 20%)
         └─ Status reporting only (no voice)
```

### Signal Quality Targets

```
Tier-0:  99.8%  Maximum clarity for command authority
Tier-1:  98-99.5%  Excellent quality for active operations
Tier-2:  95%    Standard quality for normal ops
Tier-3:  70%    Degraded but functional for secondary units
```

### Routing Optimization

When a Geospatial Bandwidth Lock is activated:

1. **Route Discovery**: Identifies multi-hop paths from command → target
2. **Latency Minimization**: Selects routes with lowest propagation delay
3. **Link Quality Assessment**: Prioritizes high-quality connections
4. **Relay Optimization**: Ensures relay nodes positioned optimally
5. **Active Marking**: Marks all primary routes as "active" for visualization

Example optimized routing:
```
Primary Route:   COMMAND_ALPHA → RELAY_NORTH → ALPHA_LEAD
                 Latency: 12ms [OPTIMIZED]
                 Quality: 99.5%

Secondary Route: RELAY_SOUTH → BRAVO_LEAD [QUEUED - THROTTLED]
                 Latency: 28ms
                 Quality: 70% (degraded)
```

### Dashboard Integration

The **Hard Knocks Radio Net** dashboard (`dashboard_radio_priority.html`) provides real-time visualization:

#### Visual Elements

- **Tier-0 Nodes**: White circles with maximum glow (Command authority)
- **Tier-1 Nodes**: Green circles with active glow (Priority sector + relays)
- **Tier-3 Nodes**: Brown/dim circles with reduced opacity (Throttled units)
- **Active Connections**: Green animated dashed lines (active sector traffic)
- **Inactive Connections**: Gray dim lines (secondary/throttled units)
- **Target Pulse**: Pulsing ring around active target nodes

#### Sidebar Displays

- **Network Status**: Real-time protocol status and lock status
- **Active Directive**: Current command directive and target
- **Tier Allocation**: Visual tier badges showing current allocation
- **Sector Information**: Target location, allocated bandwidth, signal quality
- **Network Logs**: Real-time event log of priority shifts and optimizations
- **Device Count**: Summary statistics by tier

#### Interactive Features

- **Node Hover**: Displays call-sign and tier information
- **Live Clock**: Real-time timestamp tracking
- **Radar Sweep**: Animated radar animation indicating active monitoring
- **Connection Highlighting**: Auto-highlighting of priority routes

### Running Hard Knocks Radio Net

```bash
# Compile the system
javac RadioNetworkPriority.java

# Run the simulation
java RadioNetworkPriority

# Output files generated:
# - radio_priority_output.txt        : Terminal output summary
# - network_data.js                  : Data for dashboard visualization
# - configuration_report.txt         : Detailed configuration specs
```

### Generated Output

#### radio_priority_output.txt
Terminal-friendly summary of priority event and network state changes.

#### network_data.js
JavaScript data file containing:
- `networkData[]`: Array of all nodes with tier assignments
- `connectionData[]`: Array of all connections with active status
- `simulationState`: Current DPIP state and active platoon

#### configuration_report.txt
Comprehensive documentation of:
- Tier structure definitions
- Bandwidth allocation rules
- Signal quality targets
- Complete node inventory with assignments
- Active routing paths and latencies

### Real-World Applications

#### 1. Combat Operations
**Scenario**: Squad under fire requests immediate support
- Command center issues "PRIORITY PLATOON_A"
- System allocates 85% bandwidth to pinned-down squad
- All other units throttled to keep-alive status
- Direct routing established for immediate casualty evacuation coordination

#### 2. Emergency Response
**Scenario**: Disaster zone with multiple rescue teams
- Incident commander designates primary rescue team as active sector
- Secondary teams maintain keep-alive connectivity
- All command authority broadcasts reach active sector with minimal latency
- Re-prioritization automatic when active team completes objective

#### 3. VIP Protection Detail
**Scenario**: Protected person moving through high-threat area
- Security team designated as active sector
- Perimeter teams throttled to background monitoring only
- Any priority from command authority immediately re-routes to protection detail
- Real-time tactical updates guarantee delivery within 12ms latency

#### 4. Multi-Jurisdiction Operations
**Scenario**: Law enforcement coordinating with national guard and fire
- Incident commander (command authority) maintains Tier-0
- Lead tactical team designated as active sector
- Supporting units and logistics teams in Tier-3
- Priority can shift to different team based on mission phase

### Configuration & Customization

To modify Hard Knocks behavior, edit `RadioNetworkPriority.java`:

```java
// Modify network initialization
static void initializeNetwork() {
    // Add/remove nodes
    network.add(new Node("id", "CALLSIGN", "ROLE", "PLATOON", x, y));
}

// Modify priority event
static void triggerPriorityEvent(String sourceCallSign, String targetPlatoon) {
    // Custom priority logic here
}

// Modify tier assignments
if (n.role.equals("COMMANDER")) n.priorityTier = 0;  // Static
else if (n.platoon.equals(targetPlatoon)) n.priorityTier = 1;  // Dynamic
else n.priorityTier = 3;  // Secondary
```

---

🛰️ OPTIMAL PATH ROUTING:
───────────────────────────────────────────────────────────
No communication path found

⏱️ SIMULATION TIME PROGRESSION:
───────────────────────────────────────────────────────────
After 1 minute of simulation:
  Battery levels degraded: avg device battery ~95.5%  Network efficiency maintained: 74.82%

✓ Results written to: radio_demo_output.txt

📂 Open dashboard.html in a web browser to visualize the network
@jdgiles3 ➜ /workspaces/Java-WizardMap (codespace-laughing-engine-v6rjvgpjp74pcwrrj) $ 

-------------------------------
How to Run This
Java: Save Part 2 as RadioNetworkPriority.java in your src or root folder. Compile and run it:

Bash

javac RadioNetworkPriority.java
java RadioNetworkPriority
Dashboard: Save Part 3 as dashboard.html in the same folder.

View: Run the Java code to see the terminal output, then open dashboard.html in your browser. The dashboard visually highlights "Platoon A" in green (Active Priority) and dims Platoons B and C, demonstrating the "Sector Lock" logic you requested.