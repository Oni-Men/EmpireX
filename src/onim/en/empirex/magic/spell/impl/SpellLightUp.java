package onim.en.empirex.magic.spell.impl;

import java.util.stream.IntStream;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import onim.en.empirex.EmpireX;

public class SpellLightUp extends AbstractSpell {

  @Override
  public String id() {
    return "light_up";
  }

  @Override
  public String getName() {
    return "Light Up";
  }

  @Override
  public String getDescription() {
    return "Light up around block you are looking at.";
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
    Block block = player.getTargetBlockExact(15);

    if (block == null)
      return false;

    IntStream.range(-5, 5).forEach(x -> {
      IntStream.range(-5, 5).forEach(z -> {
        Block relative = block.getRelative(x * 5, 0, z * 5);
        Bukkit.getScheduler().runTaskLater(EmpireX.instance, task -> {
          this.lightUp(relative);
        }, (long) Math.sqrt(x * x + z * z) * 4L);
      });
    });

    return false;
  }

  private void lightUp(Block block) {
    if (block == null)
      return;

    //空気ブロックを見つけるまで上に昇る
    while (!block.isEmpty()) {
      block = block.getRelative(BlockFace.UP);
      //原木は昇らない
      if (block.getType().toString().endsWith("_LOG"))
      return;
    }

    //浮いてるときは、ブロックを見つけるまで降下する
    while (block.getRelative(BlockFace.DOWN).isEmpty()) {
      block = block.getRelative(BlockFace.DOWN);
    }

    //既に明るい際は松明を設置しない
    if (block.getLightFromBlocks() > 10)
      return;

    Block down = block.getRelative(BlockFace.DOWN);
    if (down.isLiquid() || down.isPassable())
      return;

    if (down.getBoundingBox().getVolume() != 1)
      return;

    block.setBlockData(Bukkit.createBlockData(Material.TORCH));
  }

}
