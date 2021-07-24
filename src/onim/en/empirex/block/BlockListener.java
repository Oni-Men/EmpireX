package onim.en.empirex.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import onim.en.empirex.block.auto.CutLogAutomatically;

public class BlockListener implements Listener {

  @EventHandler
  public void on(BlockBreakEvent event) {
    CutLogAutomatically.cut(event.getBlock());
  }
}
