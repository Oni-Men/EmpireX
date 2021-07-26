package onim.en.empirex.item.entity;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;

import net.md_5.bungee.api.ChatColor;
import onim.en.empirex.item.CustomItem;
import onim.en.empirex.item.interfaces.RightClickable;

public class HyperKineticPositionReverser extends CustomItem implements RightClickable {

  public HyperKineticPositionReverser() {
    super("position_reverser", ChatColor.AQUA + "Hyper-Kinetic Position Reverser", Arrays
      .asList("Right click to swap you with the entity you're looking at."));
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
    Entity entity = getLookingEntity(player);

    if (entity == null) {
      return;
    }

    Location playerlocation = player.getLocation();
    Location entitylocation = entity.getLocation();

    player.teleport(entitylocation);
    entity.teleport(playerlocation);
  }

  private Entity getLookingEntity(Player player) {
    World world = player.getWorld();
    Location location = player.getLocation().add(0, 1, 0);
    RayTraceResult result = world
      .rayTraceEntities(location, location.getDirection(), 16, e -> !e.equals(player));

    if (result == null) {
      return null;
    }

    return result.getHitEntity();
  }
}
