package onim.en.empirex.magic.spell.impl;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;

import onim.en.empirex.EmpireX;

public class SpellHarvest extends AbstractSpell {

  @Override
  public String id() {
    return "harvest";
  }

  @Override
  public String getName() {
    return "Harvest";
  }

  @Override
  public String getDescription() {
    return "Click while looking at the crops. that's all";
  }

  @Override
  public int getCost() {
    return 5;
  }

  @Override
  public int getCooltime() {
    return 600;
  }

  @Override
  public boolean activate(Player player) {
    Block block = player.getTargetBlockExact(15);
    return this.harvest(block, 100);
  }

  private boolean harvest(Block block, int power) {
    if (power <= 0)
      return false;

    if (block == null)
      return false;

    if (block.getType() == Material.FARMLAND) {
      block = block.getRelative(BlockFace.UP);
    }

    if (block.getType() == Material.AIR) {
      block = block.getRelative(BlockFace.DOWN);
    }

    if (!(block.getBlockData() instanceof Ageable))
      return false;

    final Block next = block;
    Ageable ageable = (Ageable) next.getBlockData();
    if (ageable.getAge() == ageable.getMaximumAge())
      if (next.breakNaturally()) {

        Bukkit.getScheduler().runTaskLater(EmpireX.instance, task -> {
          harvest(next.getRelative(BlockFace.NORTH), power - 1);
          harvest(next.getRelative(BlockFace.EAST), power - 1);
          harvest(next.getRelative(BlockFace.SOUTH), power - 1);
          harvest(next.getRelative(BlockFace.WEST), power - 1);
        }, 2L);
        Bukkit.getScheduler().runTaskLater(EmpireX.instance, task -> {
          ageable.setAge(0);
          next.setBlockData(ageable);
        }, 20);
      }

    return true;
  }

}
