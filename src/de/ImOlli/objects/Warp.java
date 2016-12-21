package de.ImOlli.objects;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Warp {
	
	private String name;
	private Location location;
	private String creator;
	
	public Warp(String name, Location loc, String creator) {
		this.name = name;
		this.location = loc;
		this.creator = creator;
	}
	
	public void teleport(Player p){
		p.teleport(location);
	}

	public String getName(){
		return name;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location loc) {
		this.location = loc;
	}

	public String getCreator() {
		return creator;
	} 
}
