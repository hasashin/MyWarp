# MyWarp v1.7.3-modified [1.14]
An simple warp plugin to create warppoints

Modified for bukkit version 1.14 

![Warpsign](https://proxy.spigotmc.org/a5e1c11fb598ab1499e518976d52819a1126dcfa?url=http%3A%2F%2FIMOlli.de%2FMyWarp%2Fimage1.png)
![WarpGUI](https://proxy.spigotmc.org/5b9e121ccae31d914e1a4e0bf4a9024fce4f3098?url=http%3A%2F%2Fwww.ImOlli.de%2FMyWarp%2Fimage2.png)

**Important! If errors occur after updates: Try to delete the configs in the plugin folder.**
 
**MyWarp** is an simple warp plugin to create warppoints. All messages are individual configurable in the messages.yml config.

## How to create an WarpSign:
  1. Place an Sign and write in the second line **"[Warp]"**.
  2. Write in the third line the warpname. (For example "Home").
  3. Click on "Done".
  4. Now, **"[Warp]"** should appear in the second line. If not check if WarpSigns are enabled in the config.yml

## Commands:
	-    /delwarp [Name] | Deletes a warppoint.
	-    /delwarp | Opens an WarpDeleteGUI.
	-    /mywarp [reload] | Basic Command.
	-    /setwarp [Name] | Creates a new warppoint.
	-    /warp [Name] | Warps you to a warppoint.
	-    /warp | Opens an WarpTeleportGUI.
	-    /warps | Show you all warppoints.
	-    /warpgui | Opens an WarpTeleportGUI.
	-    /warpcoins <add|get|remove> <player> <amount> | Shows you an amount of coins from you or an specific player
	     or adds or removes coins from an player

## Permissions:
	-    /delwarp | MyWarp.warp.delete
	-    /mywarp | MyWarp.mywarp
	-    /setwarp | MyWarp.warp.create
	-    /warp | MyWarp.warp.warp
	-    /warps | MyWarp.warps
	-    /warpgui | MyWarp.warp.warp
	-    /warpcoins | MyWarp.warpcoins.basic
	-    /warpcoins <add> | MyWarp.warpcoins.add
	-    /warpcoins <get> | MyWarp.warpcoins.get
	-    /warpcoins <remove> | MyWarp.warpcoins.remove

## Default Permissions:
    -   MyWarp.warp.warp
    -   MyWarp.warps
    -   MyWarp.warpcoins.basic

## Op Permissions:
  	-   MyWarp.warp.create
  	-   MyWarp.warp.remove
  	-   MyWarp.mywarp
  	-   MyWarp.warpcoins.add
  	-   MyWarp.warpcoins.get
  	-   MyWarp.warpcoins.remove

## Other Permissions:
    -   MyWarp.warpcosts.ignore | Gives you the abillity to ignore all warpcosts

## Ultimate Permissions:
  	-   MyWarp.* | Gives you all permissions
  	-   MyWarp.warpcoins.* | Gives you all permissions for the warpcoins command

## ToDo
- [ ] Add MySQL Connection
