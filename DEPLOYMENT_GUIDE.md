# Radio Network Control Center - Deployment Guide

## Overview

The **Java-WizardMap** project now includes a comprehensive **Radio Network Control Center** with two distinct simulation modes accessible through an intuitive menu-driven interface.

### System Components

```
index.html                          (Main Control Center - Menu Navigation)
├── dashboard_shortest_path.html    (Tab 1: Classic Pathfinding Mode)
├── dashboard_radio_priority.html   (Tab 2: Hard Knocks Priority System)
└── dashboard.html                  (Legacy - still compatible)

RadioNetworkPriority.java           (Priority System Simulator)
network_data.js                     (Auto-generated network state data)
configuration_report.txt            (Priority system configuration)
radio_priority_output.txt           (Simulation output summary)
```

---

## Quick Start

### Method 1: Full Control Center (Recommended)

1. **Start the simulations** (already compiled):
   ```bash
   java RadioNetworkPriority
   ```

2. **Open the Control Center**:
   - Open `index.html` in a web browser
   - Two tabs appear: "Shortest Path" and "Hard Knocks Radio Net"

### Method 2: Direct Access to Individual Dashboards

**Shortest Path Dashboard**:
```bash
# First, generate path data
java Main input_file0 input_file1 input_file2 output_file

# Then open
dashboard_shortest_path.html
```

**Hard Knocks Radio Net**:
```bash
java RadioNetworkPriority
# Then open
dashboard_radio_priority.html
```

---

## Feature Comparison

| Feature | Shortest Path | Hard Knocks Radio Net |
|---------|---------------|----------------------|
| **Algorithm** | Dijkstra's pathfinding | Dynamic Priority (DPIP-V4) |
| **Use Case** | Route optimization | Tactical spectrum allocation |
| **Key Focus** | Minimal cost paths | Role-based QoS management |
| **Visualization** | Network topology & paths | Priority tiers & sector lock |
| **Real-World Scenario** | Logistics, deployment | Combat, emergency response |
| **Dynamic Elements** | Obstacle avoidance | Call-sign based prioritization |
| **Bandwidth Management** | N/A | Tier-based allocation (25%-100%) |
| **Signal Quality** | Path quality | Tier-specific targets (70%-99.8%) |

---

## Control Center Menu Interface

### Header
- **System Status**: Real-time online indicator
- **Protocol Display**: DPIP-V4 (Hard Knocks) or standard (Shortest Path)
- **Clock**: Real-time system clock (HH:MM:SS)
- **Version**: Java-WizardMap v1.0

### Tab Buttons
```
[⬢ Command Center]  [Shortest Path]  [Hard Knocks Radio Net]
```

#### Tab 1: Command Center (Welcome Screen)
- Overview of both simulation modes
- Quick description of use cases
- System status indicators

#### Tab 2: Shortest Path
- Grid-based pathfinding visualization
- Interactive network topology
- Multi-objective mission planning
- Route highlighting and cost calculation

#### Tab 3: Hard Knocks Radio Net
- Priority tier visualization (Tier-0, Tier-1, Tier-3)
- Active sector highlighting
- Real-time bandwidth allocation display
- Tactical sector lock visualization
- Radar sweep animation
- Priority event logging

---

## Hard Knocks Radio Net - Detailed Features

### Network Visualization

**Node Colors & Styling**:
- **White circles** (Tier-0): Command authority (unrestricted)
- **Green circles** (Tier-1): Active sector + relay infrastructure
- **Brown/dim circles** (Tier-3): Throttled secondary units
- **Pulsing rings**: Active target designation

**Connections**:
- **Green animated dashes**: Active priority routes
- **Gray dim lines**: Secondary/inactive routes
- **Latency labels**: Millisecond propagation delays

### Sidebar Information Panels

1. **Network Status**
   - Protocol: DPIP-V4
   - Bandwidth: RE-ROUTED status
   - System operational status

2. **Active Directive**
   - Source: COMMAND_ALPHA
   - Target: PLATOON_A
   - Action: SECTOR LOCK coordinates

3. **Tier Allocation**
   - Tier-0: Command (Unrestricted)
   - Tier-1: Active Sector + Relays
   - Tier-3: Secondary Units (Throttled)

4. **Sector Information**
   - Active Platoon designation
   - Geographic location (x, y)
   - Bandwidth allocation %
   - Signal quality %

5. **Network Logs** (Real-time Event Timeline)
   - System initialization events
   - Voice tag detection
   - Authority verification
   - Sector lock initiation
   - Bandwidth reallocation events
   - Optimization completion

6. **Device Count**
   - Total nodes
   - Count per tier (Tier-0, Tier-1, Tier-3)

### Interactive Elements

- **Hover over nodes**: Displays call-sign and tier
- **Click on sectors**: (Extensible for custom interactions)
- **Radar sweep**: Animated background indicating active monitoring
- **Legend**: Visual reference for colors and connection states

---

## Generated Data Files

### network_data.js
Contains the complete network state in JavaScript format:

```javascript
const networkData = [
  {id, callSign, role, platoon, x, y, tier, active, bw, signal},
  ...
];

const connectionData = [
  {source, target, latency, active},
  ...
];

const simulationState = {
  activePlatoon, commandSource, protocol, status, timestamp
};
```

**Usage**: Automatically loaded by dashboard for real-time data visualization.

### radio_priority_output.txt
Summary of the priority interrupt event:
```
SIMULATION_TIMESTAMP=2026-01-15T06:42:39
ACTIVE_PLATOON=PLATOON_A
COMMAND_SOURCE=COMMAND_ALPHA
PROTOCOL=DPIP-V4
STATUS=PRIORITY_LOCK_ACTIVE
NETWORK_STATE=[node data with tiers, bandwidth, signal]
```

### configuration_report.txt
Comprehensive system documentation:
- Tier structure definitions
- Bandwidth allocation rules
- Signal quality targets
- Node inventory with role assignments
- Active routing optimization paths

---

## Architecture

### Three-Tier System

```
┌─────────────────────────────────────────┐
│  WEB BROWSER DASHBOARDS (HTML/CSS/JS)  │
│                                         │
│  - index.html (Menu)                   │
│  - dashboard_*.html (Visualizations)   │
└────────────┬────────────────────────────┘
             │ Loads data from
             ↓
┌─────────────────────────────────────────┐
│  GENERATED DATA FILES                   │
│                                         │
│  - network_data.js (Node & Connection) │
│  - configuration_report.txt            │
│  - radio_priority_output.txt           │
└────────────┬────────────────────────────┘
             │ Generated by
             ↓
┌─────────────────────────────────────────┐
│  JAVA SIMULATION ENGINES                │
│                                         │
│  - RadioNetworkPriority.java            │
│  - Main.java (Pathfinding)              │
│  - Custom Data Structures               │
└─────────────────────────────────────────┘
```

### Data Flow

```
1. User runs: java RadioNetworkPriority
   ↓
2. Simulation executes:
   - Initialize network with 11 nodes
   - Trigger priority event (COMMAND_ALPHA → PLATOON_A)
   - Apply DPIP tier logic
   - Mark active routes
   ↓
3. Output files generated:
   - network_data.js (JavaScript data)
   - configuration_report.txt (Documentation)
   - radio_priority_output.txt (Summary)
   ↓
4. User opens index.html
   ↓
5. Dashboard loads network_data.js
   ↓
6. Visualization renders with:
   - Nodes colored by tier
   - Connections highlighted where active=true
   - Sidebar stats populated from data
   - Real-time clock and radar animation
```

---

## Customization Guide

### Modify Network Configuration

Edit `RadioNetworkPriority.java`:

```java
static void initializeNetwork() {
    // Add command nodes
    network.add(new Node("1", "COMMAND_ALPHA", "COMMANDER", "HQ", 50, 50));
    
    // Add your own nodes
    network.add(new Node("id", "CALLSIGN", "ROLE", "PLATOON", x, y));
    
    // Connections define routes
    connections.add(new Connection("source_id", "target_id", latency_ms));
}
```

### Change Priority Target

```java
static void main(String[] args) {
    initializeNetwork();
    
    // Change target platoon/call-sign
    triggerPriorityEvent("COMMAND_ALPHA", "PLATOON_B");  // Target B instead of A
}
```

### Adjust Bandwidth Allocation

```java
// In triggerPriorityEvent():
else if (n.platoon.equals(targetPlatoon)) {
    n.priorityTier = 1;
    n.bandwidth = 85.0;  // Change from 85% to desired value
}
```

### Customize Tier Logic

Modify the tier assignment conditions:

```java
// Static: Command stays Tier-0
if (n.role.equals("COMMANDER")) {
    n.priorityTier = 0;
    n.bandwidth = 100.0;
}

// Dynamic: Target platoon becomes Tier-1
else if (n.platoon.equals(targetPlatoon)) {
    n.priorityTier = 1;
    n.bandwidth = 85.0;
}

// Add custom logic for specific roles
else if (n.role.equals("VIP_PROTECTION")) {
    n.priorityTier = 1;  // Always high priority
}
```

---

## Troubleshooting

### "Cannot find network_data.js" (Dashboard shows defaults)
- **Cause**: RadioNetworkPriority.java hasn't been run yet
- **Solution**: Execute `java RadioNetworkPriority` first

### Dashboard appears blank or misaligned
- **Cause**: Browser caching old version
- **Solution**: Hard refresh (Ctrl+Shift+R or Cmd+Shift+R)

### Nodes not appearing in correct positions
- **Cause**: SCALE or OFFSET values don't match your display
- **Solution**: Adjust in dashboard HTML:
  ```javascript
  const SCALE = 2.0;        // Adjust zoom level
  const OFFSET_X = 330;     // Adjust horizontal
  const OFFSET_Y = 100;     // Adjust vertical
  ```

### Compilation errors in RadioNetworkPriority.java
- **Solution**: Ensure Java 8+ is installed
  ```bash
  javac -version  # Check version
  javac RadioNetworkPriority.java  # Recompile
  ```

---

## System Requirements

- **Java**: 8 or higher
- **Browser**: Modern browser with ES6 support (Chrome 51+, Firefox 54+, Safari 10+, Edge 15+)
- **Display**: 1280x720 minimum recommended
- **Network**: No internet required (fully local)

---

## Files Manifest

### Source Code
- `RadioNetworkPriority.java` (22 KB) - DPIP simulation engine
- `Main.java` - Dijkstra pathfinding engine
- Supporting classes: `Grid.java`, `Pathfinder.java`, `Mission.java`, etc.

### Web Interface
- `index.html` (8.1 KB) - Control center with menu
- `dashboard_shortest_path.html` (32 KB) - Pathfinding visualization
- `dashboard_radio_priority.html` (22 KB) - Priority system visualization
- `dashboard.html` (32 KB) - Legacy (alternative access)

### Generated Data
- `network_data.js` (2.3 KB) - Network state data
- `configuration_report.txt` (3.5 KB) - System documentation
- `radio_priority_output.txt` (1.4 KB) - Simulation summary

### Documentation
- `README_RADIO.md` - Comprehensive feature documentation
- `DEPLOYMENT_GUIDE.md` - This file

---

## Version History

### v1.0 (Current)
- ✅ Shortest Path mode (Grid-based Dijkstra pathfinding)
- ✅ Hard Knocks Radio Net mode (DPIP-V4 priority system)
- ✅ Unified control center menu interface
- ✅ Real-time data visualization dashboards
- ✅ Role-based QoS management
- ✅ Dynamic sector locking with call-sign detection

---

## Support & Development

For questions or feature requests, refer to the project documentation or examine the source code comments for implementation details.

### Key Implementation Files
- `RadioNetworkPriority.java` - Core priority logic
- `dashboard_radio_priority.html` - Frontend visualization
- `network_data.js` - Data interface between Java and web

---

**Last Updated**: January 15, 2026  
**Status**: Production Ready
