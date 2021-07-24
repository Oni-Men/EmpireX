package onim.en.empirex.player.load;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import onim.en.empirex.EmpireX;
import onim.en.empirex.player.Civilian;
import onim.en.empirex.profession.ProfessionType;

public class CivilianDataLoader {

  private static final NamespacedKey levelKey = new NamespacedKey(EmpireX.instance, "civilian_level");
  private static final NamespacedKey expKey = new NamespacedKey(EmpireX.instance, "civilian_exp");
  private static final NamespacedKey professionTypeKey = new NamespacedKey(EmpireX.instance, "civilian_profession_type");

  private static final NamespacedKey deathsKey = new NamespacedKey(EmpireX.instance, "civilian_deaths");
  private static final NamespacedKey killsKey = new NamespacedKey(EmpireX.instance, "civilian_kills");

  public static Civilian load(Player player) {
    PersistentDataContainer data = player.getPersistentDataContainer();

    Civilian civilian = new Civilian(player);

    civilian.setLevel(data.getOrDefault(levelKey, PersistentDataType.INTEGER, 1));
    civilian.setExp(data.getOrDefault(expKey, PersistentDataType.INTEGER, 0));

    String typeStr = data.get(professionTypeKey, PersistentDataType.STRING);
    if (typeStr != null) {
      ProfessionType type = ProfessionType.valueOf(typeStr);
      civilian.setProfessionType(type);
    }
    
    civilian.setDeathCount(data.getOrDefault(deathsKey, PersistentDataType.INTEGER, 0));
    civilian.setKillStreak(data.getOrDefault(killsKey, PersistentDataType.INTEGER, 0));

    return civilian;
  }

  public static void save(Civilian civilian) {
    Player player = civilian.bukkitPlayer();

    PersistentDataContainer data = player.getPersistentDataContainer();

    data.set(levelKey, PersistentDataType.INTEGER, civilian.getLevel());
    data.set(expKey, PersistentDataType.INTEGER, civilian.getExp());
    data.set(professionTypeKey, PersistentDataType.STRING, civilian.getProfessionType().name());
    data.set(deathsKey, PersistentDataType.INTEGER, civilian.getDeathCount());
    data.set(killsKey, PersistentDataType.INTEGER, civilian.getKillStreak());

  }

}
