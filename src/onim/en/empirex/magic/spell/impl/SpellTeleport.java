package onim.en.empirex.magic.spell.impl;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class SpellTeleport extends AbstractSpell {

  @Override
  public String getName() {
    return "Teleport";
  }

  @Override
  public String id() {
    return "teleport";
  }

  @Override
  public String getDescription() {
    return "Teleport to the block you are looking at. (max distance is 30)";
  }

  @Override
  public int getCost() {
    return 5;
  }

  @Override
  public int getCooltime() {
    return 40;
  }

  @Override
  public boolean activate(Player player) {
    Location currentLocation = player.getLocation();
    Block targetBlock = player.getTargetBlockExact(30, FluidCollisionMode.NEVER);
    if (targetBlock == null)
      return false;

    Location targetLocation = targetBlock.getLocation();
    targetLocation.setDirection(currentLocation.getDirection());

    if (!targetBlock.isPassable()) {
      targetLocation.add(0, 1, 0);
    }

    boolean ok = player.teleport(targetLocation, TeleportCause.PLUGIN);

    if (ok) {
      World world = player.getWorld();
      world.spawnParticle(Particle.PORTAL, currentLocation, 200, 0d, 1d, 0d);
      world.spawnParticle(Particle.REVERSE_PORTAL, targetLocation, 200, 0d, 1d, 0d);
      world.playSound(targetLocation, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.AMBIENT, 1, 0.5f);
      return true;
    }

    return false;
  }

}
