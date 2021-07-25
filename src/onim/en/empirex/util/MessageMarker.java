package onim.en.empirex.util;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import onim.en.empirex.EmpireX;

public class MessageMarker {

  public static void removeAllMarkers() {
    NamespacedKey key = KeyFactory.get("message_marker");

    for (World world : Bukkit.getWorlds()) {
      Collection<ArmorStand> armorStands = world.getEntitiesByClass(ArmorStand.class);

      for (ArmorStand stand : armorStands) {

        PersistentDataContainer data = stand.getPersistentDataContainer();

        if (data.has(key, PersistentDataType.BYTE)) {
          stand.remove();
        }
      }
    }
    
  }

  public static void spawn(Location location, String msg) {
    NamespacedKey key = KeyFactory.get("message_marker");
    
    World world = location.getWorld();
    ArmorStand spawn = world.spawn(location.add(0, 1, 0), ArmorStand.class, stand -> {
      stand.setInvisible(true);
      stand.setGravity(false);
      stand.setCollidable(false);
      stand.setCanPickupItems(false);
      stand.setCustomNameVisible(true);
      stand.setCustomName(msg);
      stand.setMarker(true);
      stand.setPersistent(false);

      PersistentDataContainer data = stand.getPersistentDataContainer();
      data.set(key, PersistentDataType.BYTE, Byte.valueOf((byte) 1));
    });
    
    final long startTick = world.getFullTime();
    
    Bukkit.getScheduler().runTaskTimer(EmpireX.instance, (task) -> {
      spawn.teleport(spawn.getLocation().add(0, 0.05, 0));
      
      if (world.getFullTime() - startTick > 20) {
        task.cancel();
        spawn.remove();
      }
    }, 0, 1);
  }

}
