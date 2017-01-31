# MyWarp
An simple warp plugin to create warppoints

**Important! If errors occur after updates: Try to delete the configs in the plugin folder.**
 
**MyWarp** is an simple warp plugin to create warppoints. All messages are individual configurable in the messages.yml config.

##How to create an WarpSign:
  1. Place an Sign and write in the second line **"[Warp]"**.
  2. Write in the third line the warpname. (For example "Home").
  3. Click on "Done".
  4. Now, **"[Warp]"** should appear in the second line. If not check if WarpSigns are enabled in the config.yml

##Commands:
	-    /delwarp [Name] | Deletes a warppoint.
	-    /delwarp | Opens an WarpDeleteGUI.
	-    /mywarp [reload] | Basic Command.
	-    /setwarp [Name] | Creates a new warppoint.
	-    /warp [Name] | Warps you to a warppoint.
	-    /warp | Opens an WarpTeleportGUI.
	-    /warps | Show you all warppoints.
	-    /warpgui | Opens an WarpTeleportGUI.

##Permissions
	-    /delwarp | MyWarp.warp.delete
	-    /mywarp | MyWarp.mywarp
	-    /setwarp | MyWarp.warp.create
	-    /warp | MyWarp.warp.warp
	-    /warps | MyWarp.warps
	-    /warpgui | MyWarp.warp.warp

  ###Default Permissions:
    -   MyWarp.warp.warp
    -   MyWarp.warps

  ###Op Permissions:
  	-   MyWarp.warp.create
  	-   MyWarp.warp.remove
  	-   MyWarp.mywarp

  ###Ultimate Permission:
  	-   MyWarp.*

##ToDo
- [ ] Add MySQL Connection
