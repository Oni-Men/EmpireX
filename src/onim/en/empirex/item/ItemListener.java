package onim.en.empirex.item;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import com.google.common.base.Strings;

import onim.en.empirex.EmpireX;
import onim.en.empirex.item.interfaces.Droppable;
import onim.en.empirex.item.interfaces.Fishable;
import onim.en.empirex.item.interfaces.Interactable;
import onim.en.empirex.item.interfaces.Shootable;

public class ItemListener implements Listener {

  @EventHandler
  public void on(PlayerDropItemEvent event) {
    ItemStack stack = event.getItemDrop().getItemStack();
    CustomItem customItem = EmpireX.itemFactory.toCustomItem(stack);

    if (customItem instanceof Droppable) {
      ((Droppable) customItem).onDrop(event);
    }
  }

  @EventHandler
  public void on(PlayerFishEvent event) {
    ItemStack stack = event.getPlayer().getActiveItem();
    CustomItem customItem = EmpireX.itemFactory.toCustomItem(stack);

    if (customItem instanceof Fishable) {
      ((Fishable) customItem).onFish(event);
    }
  }

  @EventHandler
  public void on(PlayerInteractEvent event) {
    ItemStack stack = event.getItem();
    CustomItem customItem = EmpireX.itemFactory.toCustomItem(stack);

    if (customItem instanceof Interactable) {
      ((Interactable) customItem).onInteract(event);
    }
  }

  @EventHandler
  public void on(ProjectileLaunchEvent event) {
    ProjectileSource shooter = event.getEntity().getShooter();

    if (!(shooter instanceof Player))
      return;

    ItemStack stack = ((Player) shooter).getActiveItem();
    CustomItem customItem = EmpireX.itemFactory.toCustomItem(stack);

    if (customItem instanceof Shootable) {
      ((Shootable) customItem).onShoot(event);
    }
  }

  @EventHandler
  public void on(CraftItemEvent event) {
    CraftingInventory inventory = event.getInventory();

    for (ItemStack stack : inventory.getMatrix()) {
      String id = ItemUtil.getPluginItemId(stack);

      if (!Strings.isNullOrEmpty(id)) {
        event.setCancelled(true);
      }

    }
    
  }
}
