package onim.en.empirex.item.entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;

import com.google.common.collect.Maps;

import net.kyori.adventure.text.Component;
import onim.en.empirex.EmpireX;
import onim.en.empirex.event.civilian.item.HeldEvent;
import onim.en.empirex.event.civilian.item.HeldEvent.HeldAction;
import onim.en.empirex.item.CustomItem;
import onim.en.empirex.item.interfaces.Heldable;
import onim.en.empirex.item.interfaces.MoveDetectable;
import onim.en.empirex.item.interfaces.ToggleSneakDetectable;

public class RecallCrystal extends CustomItem implements Heldable, MoveDetectable, ToggleSneakDetectable {

  private HashMap<Player, BukkitTask> tasks;

  public RecallCrystal() {
    super("recall_crystal", "Recall Crystal", Arrays
      .asList("アイテムを持って5秒間スニークすることでリコール地点を設定できます", "アイテムを以って10秒間動かずにいることでリコールできます"));
    tasks = Maps.newHashMap();
  }

  @Override
  public ItemStack getItemStack() {
    ItemStack stack = new ItemStack(Material.AMETHYST_SHARD);
    return stack;
  }

  @Override
  public void onHeld(HeldEvent event) {
    Player player = event.getPlayer();

    if (event.getAction() == HeldAction.START) {

      if (player.isSneaking()) {
        this.startUpdate(player, event.getNewItemStack(), player.getLocation());
      } else {
        Location location = getLocationRecall(event.getNewItemStack());

        if (location == null) {
          sendActionBar(player, ChatColor.RED + "リコール地点がありません");
        }

        this.startRecall(player, event.getNewItemStack(), location);
      }
    }

    if (event.getAction() == HeldAction.STOP) {
      this.cancelRecall(player, true);
    }
  }

  @Override
  public void onMove(PlayerMoveEvent event) {
    if (event.hasChangedPosition()) {
      this.cancelRecall(event.getPlayer(), true);
    }
  }

  @Override
  public void onToggleSneak(PlayerToggleSneakEvent event) {
    this.cancelRecall(event.getPlayer(), true);
  }

  private void startRecall(Player player, ItemStack stack, Location location) {
    AtomicInteger count = new AtomicInteger(10);

    BukkitTask task = Bukkit.getScheduler().runTaskTimer(EmpireX.instance, () -> {
      int c = count.get();

      String msg;
      if (c == 0) {
        player.teleport(location);
        this.cancelRecall(player, false);
        msg = "リコールに成功しました!";
      } else {
        msg = String.format("あと %d 秒でリコールします", c);
      }
      sendActionBar(player, ChatColor.YELLOW + msg);

      count.set(c - 1);
    }, 0, 20L);

    tasks.put(player, task);
  }

  private void startUpdate(Player player, ItemStack stack, Location location) {
    AtomicInteger count = new AtomicInteger(5);

    BukkitTask task = Bukkit.getScheduler().runTaskTimer(EmpireX.instance, () -> {
      int c = count.get();

      String msg;
      if (c == 0) {
        this.setLocationRecall(stack, location);
        this.cancelRecall(player, false);
        msg = "リコール地点の更新に成功しました!";
      } else {
        msg = String.format("あと %d秒 でリコール地点を更新します", c);
      }
      sendActionBar(player, ChatColor.YELLOW + msg);
      count.set(c - 1);
    }, 0, 20L);

    tasks.put(player, task);
  }

  private Location getLocationRecall(ItemStack stack) {
    if (stack == null) {
      return null;
    }

    PersistentDataContainer data = stack.getItemMeta().getPersistentDataContainer();
    NamespacedKey key = new NamespacedKey(EmpireX.instance, "recall_location");

    if (data.has(key, PersistentDataType.STRING)) {
      return parseLocationString(data.get(key, PersistentDataType.STRING));
    }

    return null;
  }

  private void setLocationRecall(ItemStack stack, Location location) {
    if (stack == null) {
      return;
    }

    ItemMeta meta = stack.getItemMeta();
    PersistentDataContainer data = meta.getPersistentDataContainer();

    NamespacedKey key = new NamespacedKey(EmpireX.instance, "recall_location");
    data.set(key, PersistentDataType.STRING, toLocationString(location));

    stack.setItemMeta(meta);
  }

  private String toLocationString(Location loc) {
    return String.format("%s:%f,%f,%f", loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ());
  }

  private Location parseLocationString(String str) {
    String[] split = str.split(":");
    if (split.length != 2) {
      return null;
    }

    String worldName = split[0];
    String[] texts = split[1].split(",");

    if (texts.length != 3) {
      return null;
    }

    World world = Bukkit.getWorld(worldName);
    if (world == null) {
      return null;
    }

    double[] coords = new double[3];
    try {
      for (int i = 0; i < 3; i++) {
        coords[i] = Double.parseDouble(texts[i]);
      }
    } catch (NumberFormatException e) {
      return null;
    }

    return new Location(world, coords[0], coords[1], coords[2]);
  }

  private void cancelRecall(Player player, boolean msg) {
    BukkitTask task = tasks.get(player);

    if (task == null) {
      return;
    }

    if (task.isCancelled()) {
      return;
    }

    task.cancel();
    tasks.remove(player);

    if (msg) {
      sendActionBar(player, ChatColor.RED + "キャンセルされました");
    }
  }

  private void sendActionBar(Player player, String text) {
    player.sendActionBar(Component.text(text));
  }
}
