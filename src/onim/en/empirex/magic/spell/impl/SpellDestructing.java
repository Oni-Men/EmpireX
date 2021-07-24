package onim.en.empirex.magic.spell.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

public class SpellDestructing extends AbstractSpell {

  private static final HashMap<Player, List<Block>> areaMap = Maps.newHashMap();

  @Override
  public String id() {
    return "destructing";
  }

  @Override
  public String getName() {
    return "Destructing";
  }

  @Override
  public String getDescription() {
    return "";
  }

  @Override
  public int getCost() {
    return 0;
  }

  @Override
  public int getCooltime() {
    return 0;
  }

  @Override
  public boolean activate(Player player) {
    Block block = player.getTargetBlockExact(30);
    if (block == null)
      return false;

    if (!areaMap.containsKey(player)) {
      List<Block> list = new ArrayList<>();
      list.add(block);
      areaMap.put(player, list);
      player.sendBlockChange(block.getLocation(), Bukkit.createBlockData(Material.GOLD_BLOCK));
    } else {
      List<Block> list = areaMap.get(player);
      if (list.size() == 1) {
        list.add(block);
        player.sendBlockChange(block.getLocation(), Bukkit.createBlockData(Material.GOLD_BLOCK));
      } else if (list.size() == 2) {
        list.forEach(b -> {
          player.sendBlockChange(b.getLocation(), b.getBlockData());
        });

        Block first = list.get(0);
        Block second = list.get(1);

        World world = block.getWorld();

        for (int y = Math.min(first.getY(), second.getY()); y <= Math.max(first.getY(), second.getY()); y++) {
          for (int x = Math.min(first.getX(), second.getX()); x <= Math.max(first.getX(), second.getX()); x++) {
            for (int z = Math.min(first.getZ(), second.getZ()); z <= Math.max(first.getZ(), second.getZ()); z++) {
              world.getBlockAt(x, y, z).breakNaturally();
            }
          }
        }

        areaMap.remove(player);
      }
    }

    return true;
  }

}
