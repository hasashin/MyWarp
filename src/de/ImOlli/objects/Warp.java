package de.ImOlli.objects;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Warp {
	
	private String name;
	private Location loc;
	private String creator;
	
	public Warp(String name, Location loc, String creator) {
		this.name = name;
		this.loc = loc;
		this.creator = creator;
	}
	
	public void teleport(Player p){
		p.teleport(loc);
	}

	public String getName(){
		return name;
	}
	
	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public String getCreator() {
		return creator;
	} 
}
