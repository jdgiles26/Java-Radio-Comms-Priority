// Auto-generated Radio Network Data
// Timestamp: 2026-01-15T06:42:39.330857

const networkData = [
  {id:'1', callSign:'COMMAND_ALPHA', role:'COMMANDER', platoon:'HQ', x:50, y:50, tier:0, active:false, bw:100.0, signal:99.8},
  {id:'2', callSign:'TAC_COM_1', role:'TAC_COMMAND', platoon:'HQ', x:100, y:250, tier:0, active:false, bw:100.0, signal:99.8},
  {id:'3', callSign:'ALPHA_LEAD', role:'SQUAD_LEADER', platoon:'PLATOON_A', x:200, y:80, tier:1, active:true, bw:85.0, signal:99.5},
  {id:'4', callSign:'ALPHA_RTO', role:'RTO', platoon:'PLATOON_A', x:220, y:85, tier:1, active:true, bw:85.0, signal:99.5},
  {id:'4A', callSign:'ALPHA_MED', role:'MEDIC', platoon:'PLATOON_A', x:210, y:75, tier:1, active:true, bw:85.0, signal:99.5},
  {id:'5', callSign:'BRAVO_LEAD', role:'SQUAD_LEADER', platoon:'PLATOON_B', x:300, y:120, tier:3, active:false, bw:25.0, signal:70.0},
  {id:'6', callSign:'BRAVO_RTO', role:'RTO', platoon:'PLATOON_B', x:320, y:130, tier:3, active:false, bw:25.0, signal:70.0},
  {id:'7', callSign:'CHARLIE_LEAD', role:'SQUAD_LEADER', platoon:'PLATOON_C', x:150, y:280, tier:3, active:false, bw:25.0, signal:70.0},
  {id:'8', callSign:'CHARLIE_RTO', role:'RTO', platoon:'PLATOON_C', x:160, y:290, tier:3, active:false, bw:25.0, signal:70.0},
  {id:'9', callSign:'RELAY_NORTH', role:'RELAY', platoon:'NET', x:150, y:100, tier:1, active:false, bw:90.0, signal:98.0},
  {id:'10', callSign:'RELAY_SOUTH', role:'RELAY', platoon:'NET', x:220, y:200, tier:1, active:false, bw:90.0, signal:98.0},
  {id:'11', callSign:'RELAY_EAST', role:'RELAY', platoon:'NET', x:350, y:150, tier:1, active:false, bw:90.0, signal:98.0}
];

const connectionData = [
  {source:'1', target:'2', latency:5.0, active:false},
  {source:'1', target:'9', latency:8.5, active:true},
  {source:'1', target:'10', latency:12.0, active:true},
  {source:'9', target:'3', latency:7.2, active:true},
  {source:'9', target:'4', latency:7.5, active:true},
  {source:'10', target:'5', latency:9.8, active:false},
  {source:'10', target:'7', latency:11.3, active:false},
  {source:'11', target:'5', latency:6.2, active:false},
  {source:'11', target:'7', latency:8.9, active:false}
];

const simulationState = {
  activePlatoon: 'PLATOON_A',
  commandSource: 'COMMAND_ALPHA',
  protocol: 'DPIP-V4',
  status: 'PRIORITY_LOCK_ACTIVE',
  timestamp: '2026-01-15T06:42:39.341831'
};
