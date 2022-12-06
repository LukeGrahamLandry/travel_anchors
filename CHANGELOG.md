## 1.2.13

- build against 1.19.2
- add client config (`minecraft/config/travelstaff-client.json5`) for isElevatorMode
- add server config (`world/serverconfig/travelstaff-synced.json5`) 
  - maxTeleportDistance: how far can you teleport to a travel anchor 
  - shortTeleportDistance: how far can you teleport with just a staff of traveling 
  - staffCooldownTicks: how long must you wait before using the staff of traveling again
  - rangeEnchantScaling: how effective is the range enchantment. range to travel anchor will be multiplied by 1 + (lvl * scaling)
  - maxAngle: maximum angle you can look at the travel anchor and still teleport to it

## 1.2.12

Save the whole block state in TravelAnchorList instead of just the id. 
Fixes bug where sometimes adding or removing a mod would reset the travel anchor's mimic. 
Still compatible with older versions. 
