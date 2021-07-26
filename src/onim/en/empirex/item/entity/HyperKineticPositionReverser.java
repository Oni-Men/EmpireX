package onim.en.empirex.item.entity;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import net.md_5.bungee.api.ChatColor;
import onim.en.empirex.item.CustomItem;
import onim.en.empirex.item.interfaces.RightClickable;

public class HyperKineticPositionReverser extends CustomItem implements RightClickable{

	public HyperKineticPositionReverser() {
		super("position_reverser",ChatColor.AQUA+"Hyper-Kinetic Position Reverser",Arrays.asList("right click to swap you with the entity you're looking at."));
	}

	@Override
	public Material getMaterial() {
		return Material.MUSIC_DISC_CAT;
	}

	@Override
	public boolean isGlowing() {
		return true;
	}

	@Override
	public void onRightClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Entity entity = player.getTargetEntity(16);
		if(entity == null) return;
		Location playerlocation = player.getLocation();
		Location entitylocation = entity.getLocation();
		player.teleport(entitylocation);
		entity.teleport(playerlocation);

	}



}
