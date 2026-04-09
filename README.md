# WizardMap
A Java project that simulates a grid navigation system, where tasks are completed by navigating through nodes, avoiding obstacles, and using custom wizard options. The project utilizes handcrafted data structures to handle grid representation, pathfinding, and mission objectives. Features Dijkstra’s algorithm for efficient route calculation.


The project can be run by typing "Java main input_file0 input_file1 input_file2 output_file" in the code terminal.


Now let me examine the main Java files to understand the implementation better:

Let me also check the Mission and input files to understand the problem better:

Perfect! Now I have a clear understanding. Let me explain what this repository demonstrates and how it could apply to military radio communications:

What This Repository Demonstrates
WizardMap is a grid-based pathfinding and mission optimization system that implements:

Grid Navigation: A 400×400 grid with different node types (obstacles, normal terrain, special locations)
Weighted Graph Pathfinding: Uses Dijkstra's algorithm to find optimal routes between points with variable travel times
Mission Planning: Multiple objectives that must be completed, starting from a base location
Custom Data Structures: Implements hash tables, linked lists, and priority queues from scratch (no standard library)
Visibility/Reveal Radius: Limited information about the terrain until reaching certain points
Real-Life Military Applications
This architecture directly parallels military radio communications and tactical operations:

1. Radio Network Routing & Signal Propagation
Grid nodes = Radio relay stations, command posts, or geographic waypoints
Travel times = Signal propagation delay, link quality (dB loss), or transmission time
Node types = Different radio equipment: standard repeaters (type 0), high-power relays (type 1), mobile units (type 2), jamming-resistant nodes (type 3), etc.
Dijkstra's algorithm = Finding the fastest/most reliable communication path between tactical units, accounting for varying signal strengths across terrain
2. Personnel & Equipment Positioning
Objectives = Strategic locations for deploying personnel, establishing communication hubs, or positioning equipment
Start position = Base of operations or command center
Reveal radius = Limited intelligence about enemy positions or terrain—only observing a 10-unit radius around current position
Mission planning = Optimal sequence to establish communication network across hostile/complex terrain
3. Tactical Movement with Time Constraints
Travel times = Movement cost (terrain difficulty, distance, tactical exposure time)
Multiple waypoints = Sequential deployment of radio crews or reconnaissance teams
Wizard options = Emergency support: satellite uplink (option 2), air support (option 3), reinforcements (option 4)
4. Specific Military Scenarios
Command & Control (C2) Network: Positioning field radios to maintain connectivity across a 400km² area
SIGINT Operations: Locating enemy radio transmitters by triangulating signals from multiple listening posts
Disaster Response: Establishing emergency communication networks in disaster zones with damaged infrastructure
Cross-border Operations: Finding optimal routes for radio relay teams through complex terrain while avoiding detection
The variable travel times (ranging from 3,761 to 98,477 units in the data) represent real factors like terrain difficulty, atmospheric conditions, or risk assessment in tactical scenarios.