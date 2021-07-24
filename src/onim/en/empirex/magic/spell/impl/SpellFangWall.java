package onim.en.empirex.magic.spell.impl;

import java.util.stream.IntStream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.Player;

import net.minecraft.util.MathHelper;
import onim.en.empirex.EmpireX;

public class SpellFangWall extends AbstractSpell {

  @Override
  public String getName() {
    return "Fang - Wall";
  }

  @Override
  public String id() {
    return "fang_wall";
  }

  @Override
  public String getDescription() {
    return "Summon the fangs around you";
  }

  @Override
  public int getCost() {
    return 4;
  }

  @Override
  public int getCooltime() {
    return 60;
  }

  @Override
  public boolean activate(Player player) {
    Location location = player.getLocation();
    World world = player.getWorld();

    Bukkit.getScheduler().runTaskLater(EmpireX.instance, task -> {
      IntStream.range(0, 5).forEach(i -> {
        float d = (float) i * (float) Math.PI * 0.4F;
        Location spawnLoc = location.clone().add(MathHelper.cos(d) * 1.5, 0, MathHelper.sin(d) * 1.5);
        spawnLoc.setYaw(d * (180 / (float) Math.PI));
        world.spawn(spawnLoc, EvokerFangs.class, fang -> fang.setOwner(player));
      });
    }, 0);
    Bukkit.getScheduler().runTaskLater(EmpireX.instance, task -> {
      IntStream.range(0, 8).forEach(i -> {
        float d = (float) i * (float) Math.PI * 2.0F / 8.0F + 1.25f;
        Location spawnLoc = location.clone().add(MathHelper.cos(d) * 2.5, 0, MathHelper.sin(d) * 2.5);
        spawnLoc.setYaw(d * (180 / (float) Math.PI));
        world.spawn(spawnLoc, EvokerFangs.class, fang -> fang.setOwner(player));
      });
    }, 5);

    return true;
  }

}
