package onim.en.empirex.block.auto;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import onim.en.empirex.EmpireX;

public class CutLogAutomatically {

  public static void cut(Block block) {
    cut(block, true);
  }

  public static void cut(Block block, boolean first) {
    if (!isLog(block) && !(isLeaves(block) && !first)) {
      return;
    }

    boolean destroyed = block.breakNaturally();
    if (destroyed) {
      Bukkit.getScheduler().runTaskLater(EmpireX.instance, () -> {
        Block up = block.getRelative(BlockFace.UP);
        cut(up, false);
        cut(up.getRelative(BlockFace.NORTH), false);
        cut(up.getRelative(BlockFace.EAST), false);
        cut(up.getRelative(BlockFace.WEST), false);
        cut(up.getRelative(BlockFace.SOUTH), false);
        cut(up.getRelative(BlockFace.DOWN), false);
      }, 4L);
    }
  }

  private static boolean isLog(Block block) {
    switch (block.getType()) {
      case ACACIA_LOG:
      case DARK_OAK_LOG:
      case BIRCH_LOG:
      case JUNGLE_LOG:
      case OAK_LOG:
      case SPRUCE_LOG:
        return true;
      default:
        return false;
    }
  }

  private static boolean isLeaves(Block block) {
    switch (block.getType()) {
      case ACACIA_LEAVES:
      case BIRCH_LEAVES:
      case DARK_OAK_LEAVES:
      case JUNGLE_LEAVES:
      case OAK_LEAVES:
      case SPRUCE_LEAVES:
        return true;
      default:
        return false;
    }
  }
}
