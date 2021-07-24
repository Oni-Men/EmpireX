package onim.en.empirex.magic.spell.impl;

import java.util.stream.IntStream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import onim.en.empirex.EmpireX;

public class SpellFangLance extends AbstractSpell {

  @Override
  public String getName() {
    return "Fang - Lance";
  }

  @Override
  public String id() {
    return "fang_attack";
  }

  @Override
  public String getDescription() {
    return "Release Fangs in the direction you are facing";
  }

  @Override
  public int getCost() {
    return 3;
  }

  @Override
  public int getCooltime() {
    return 60;
  }

  @Override
  public boolean activate(Player player) {
    Location location = player.getLocation();
    World world = player.getWorld();
    Vector direction = location.getDirection();

    IntStream.rangeClosed(1, 10).forEach(i -> {
      Bukkit.getScheduler().runTaskLater(EmpireX.instance, task -> {
        double d = Math.atan2(direction.getZ(), direction.getX());
        Location spawnLoc = location.clone().add(Math.cos(d) * 1.25 * i, 0, Math.sin(d) * 1.25 * i);
        world.spawn(spawnLoc, EvokerFangs.class, fang -> fang.setOwner(player));
      }, i);
    });

    return true;
  }

}
