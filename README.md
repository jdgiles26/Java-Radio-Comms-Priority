# WizardMap

WizardMap is a Java project that simulates a grid navigation system for completing tasks by navigating through nodes, avoiding obstacles, and using custom wizard options. It employs handcrafted data structures for grid representation, pathfinding, and mission objectives, utilizing Dijkstra’s algorithm for efficient route calculation.

To run the project, use the command: Java main input_file0 input_file1 input_file2 output_file in the code terminal.

## Key Components:

1. **Grid Navigation**: Operates on a 400×400 grid with various node types, including obstacles, normal terrain, and special locations.

2. **Weighted Graph Pathfinding**: Implements Dijkstra’s algorithm to determine optimal routes between points with variable travel times.

3. **Mission Planning**: Involves multiple objectives that must be completed, starting from a base location.

4. **Custom Data Structures**: Features hash tables, linked lists, and priority queues implemented from scratch without using the standard library.

5. **Visibility/Reveal Radius**: Limits information about the terrain until certain points are reached.

## Military Applications:

WizardMap’s architecture is applicable to military radio communications and tactical operations in several ways:

1. **Radio Network Routing & Signal Propagation**:
   - Grid nodes represent radio relay stations, command posts, or geographic waypoints.
   - Travel times correspond to signal propagation delay, link quality, or transmission time.
   - Node types include different radio equipment, such as standard repeaters, high-power relays, mobile units, and jamming-resistant nodes.
   - Dijkstra’s algorithm is used to find the fastest or most reliable communication path between tactical units, considering varying signal strengths.

2. **Personnel & Equipment Positioning**:
   - Objectives involve strategic locations for deploying personnel, establishing communication hubs, or positioning equipment.
   - The start position is the base of operations or command center.
   - The reveal radius represents limited intelligence about enemy positions or terrain, observing only a 10-unit radius around the current position.
   - Mission planning focuses on the optimal sequence to establish a communication network across hostile or complex terrain.

3. **Tactical Movement with Time Constraints**:
   - Travel times denote movement costs, such as terrain difficulty, distance, and tactical exposure time.
   - Multiple waypoints allow for sequential deployment of radio crews or reconnaissance teams.
   - Wizard options provide emergency support, like satellite uplink, air support, or reinforcements.

4. **Specific Military Scenarios**:
   - **Command & Control (C2) Network**: Positions field radios to maintain connectivity across a 400km² area.
   - **SIGINT Operations**: Locates enemy radio transmitters by triangulating signals from multiple listening posts.
   - **Disaster Response**: Establishes emergency communication networks in disaster zones with damaged infrastructure.
   - **Cross-border Operations**: Finds optimal routes for radio relay teams through complex terrain while avoiding detection.

The variable travel times, ranging from 3,761 to 98,477 units, represent real factors like terrain difficulty, atmospheric conditions, or risk assessment in tactical scenarios.
