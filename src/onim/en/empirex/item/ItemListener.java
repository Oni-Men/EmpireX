package onim.en.empirex.item;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.projectiles.ProjectileSource;

import com.google.common.base.Strings;

import onim.en.empirex.EmpireX;
import onim.en.empirex.event.civilian.item.HeldEvent;
import onim.en.empirex.event.civilian.item.HeldEvent.HeldAction;
import onim.en.empirex.item.interfaces.Droppable;
import onim.en.empirex.item.interfaces.Fishable;
import onim.en.empirex.item.interfaces.Heldable;
import onim.en.empirex.item.interfaces.LeftClickable;
import onim.en.empirex.item.interfaces.MoveDetectable;
import onim.en.empirex.item.interfaces.RightClickable;
import onim.en.empirex.item.interfaces.Shootable;
import onim.en.empirex.item.interfaces.ToggleSneakDetectable;

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

    switch (event.getAction()) {
      case LEFT_CLICK_AIR:
      case LEFT_CLICK_BLOCK:
        if (customItem instanceof LeftClickable) {
          ((LeftClickable) customItem).onLeftClick(event);
        }
        break;
      case RIGHT_CLICK_AIR:
      case RIGHT_CLICK_BLOCK:
        if (customItem instanceof RightClickable) {
          ((RightClickable) customItem).onRightClick(event);
        }
        break;
      default:
        break;
    }
  }

  @EventHandler
  public void on(ProjectileLaunchEvent event) {
    ProjectileSource shooter = event.getEntity().getShooter();

    if (!(shooter instanceof Player))
      return;

    Player player = (Player) shooter;
    PlayerInventory inventory = player.getInventory();

    ItemStack stack = inventory.getItemInMainHand();
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

  @EventHandler
  public void on(PlayerItemHeldEvent event) {
    Player player = event.getPlayer();
    PlayerInventory inventory = player.getInventory();

    ItemStack newItem = inventory.getItem(event.getNewSlot());
    ItemStack prevItem = inventory.getItem(event.getPreviousSlot());

    CustomItem newCustomItem = EmpireX.itemFactory.toCustomItem(newItem);
    CustomItem prevCustomItem = EmpireX.itemFactory.toCustomItem(prevItem);

    EmpireX.civilianFactory.executeIfPresent(player, c -> {
      if (newCustomItem instanceof Heldable) {
        ((Heldable) newCustomItem).onHeld(new HeldEvent(c, HeldAction.START, newItem, prevItem));
      }

      if (prevCustomItem instanceof Heldable) {
        ((Heldable) prevCustomItem).onHeld(new HeldEvent(c, HeldAction.STOP, newItem, prevItem));
      }
    });

  }
  
  @EventHandler
  public void on(PlayerMoveEvent event) {
    Player player = event.getPlayer();
    ItemStack stack = player.getInventory().getItemInMainHand();
    CustomItem customItem = EmpireX.itemFactory.toCustomItem(stack);

    if (customItem instanceof MoveDetectable) {
      ((MoveDetectable) customItem).onMove(event);
    }
  }

  @EventHandler
  public void on(PlayerToggleSneakEvent event) {
    Player player = event.getPlayer();
    ItemStack stack = player.getInventory().getItemInMainHand();
    CustomItem customItem = EmpireX.itemFactory.toCustomItem(stack);

    if (customItem instanceof ToggleSneakDetectable) {
      ((ToggleSneakDetectable) customItem).onToggleSneak(event);
    }
  }
}
