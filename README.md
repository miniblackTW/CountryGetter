# CountryGetter
Now, you can see where they are

## Command :
###  /cg info <player>
   - **Description :** Get the info from player's IP
   - **Permission :** `cg.*`

![image](https://imgur.com/2d55VCn.png)

## Events :
###  PlayerJoinEvent
   - **Sets player's display name to** `PlayerName | Country | (VPN/Proxy)`**, only shows on Tab**
   - **Spawns an armor stand named** `Country | (VPN/Proxy)`**, it follows the player**

![image](https://i.imgur.com/1rhl3YV.png)

### PlayerQuitEvent
   - **Removes the armor stand of the player**

### PlayerInteractAtEntityEvent
   - **Stops players from removing the head on the armor stand**

~~### AsyncPlayerChatEvent~~ **(Removed)**
   ~~- **Adds the prefix `Country | PlayerName`**~~
