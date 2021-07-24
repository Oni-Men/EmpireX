package onim.en.empirex.util;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import onim.en.empirex.EmpireX;

public class EntityUtil {

  private static NamespacedKey getKeyExplodable() {
    return new NamespacedKey(EmpireX.instance, "explodable");
  }

  public static void makeEntityExplodable(Entity entity, float power) {
    PersistentDataContainer data = entity.getPersistentDataContainer();
    data.set(getKeyExplodable(), PersistentDataType.FLOAT, power);
  }

  public static boolean isEntityExplodable(Entity entity) {
    PersistentDataContainer data = entity.getPersistentDataContainer();
    return data.has(getKeyExplodable(), PersistentDataType.FLOAT);
  }

  public static float getEntityExplosionPower(Entity entity) {
    PersistentDataContainer data = entity.getPersistentDataContainer();

    if (isEntityExplodable(entity)) {
      Float power = data.get(getKeyExplodable(), PersistentDataType.FLOAT);
      return power;
    }

    return 0F;
  }
}
