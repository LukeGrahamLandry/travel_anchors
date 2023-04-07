# Travel Anchors

Adds the Travel Anchors and Staff of Travelling from EnderIO as a standalone mod.

This project uses multiloader to build the mod for both Forge and Fabric from the same code base. 
Ported from https://www.curseforge.com/minecraft/mc-mods/travel-anchors

## Features

- Shift right click with a Staff of Travelling to do a short teleport forward (can go through blocks)
- **Travel Anchors**
  - Place a Travel Anchor and right click it (while not standing on it) to change its name
  - Put a block in your offhand and right click a Travel Anchor with an empty main hand to disguise it as that block
  - While holding a Staff of Travelling, right click in the direction of a Travel Anchor to teleport to it 
  - Stand on a Travel Anchor and right click with an empty hand in the direction of a another Travel Anchor to teleport to it
  - **Elevator Mode** 
    - Stand on a Travel Anchor and press Jump to teleport to a Travel Anchor directly above you 
    - Stand on a Travel Anchor and press Sneak to teleport to a Travel Anchor directly below you
- **Enchantments**
  - Enchant a weapon or digger item with Teleportation to make it act like a Staff of Travelling
  - Enchant a Staff of Travelling with Range to increase the max range you can teleport to travel anchors
- **Config**
  - `world/serverconfig/travelstaff-synced.json5` (use `/reload` command to refresh without restarting game)
    - maxTeleportDistance: how far can you teleport to a travel anchor
    - shortTeleportDistance: how far can you teleport with just a staff of traveling
    - staffCooldownTicks: how long must you wait before using the staff of traveling again
    - rangeEnchantScaling: how effective is the range enchantment. range to travel anchor will be multiplied by 1 + (lvl * scaling)
    - maxAngle: maximum angle you can look at the travel anchor and still teleport to it
  - `minecraft/config/travelstaff-client.json5`
    - isElevatorMode: while on anchor, press Jump to teleport to an anchor directly above you or Sneak to teleport to an anchor directly below you
