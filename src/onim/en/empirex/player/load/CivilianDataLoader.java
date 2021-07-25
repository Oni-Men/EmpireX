package onim.en.empirex.player.load;

import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import onim.en.empirex.player.Civilian;
import onim.en.empirex.profession.ProfessionType;
import onim.en.empirex.util.KeyFactory;

public class CivilianDataLoader {

  private static final String key_level = "civilian_level";
  private static final String key_exp = "civilian_exp";
  private static final String key_profession = "civilian_profession";
  private static final String key_death_count = "civilian_deaths";
  private static final String key_kill_count = "civilian_kills";


  public static Civilian load(Player player) {
    PersistentDataContainer data = player.getPersistentDataContainer();

    Civilian civilian = new Civilian(player);

    civilian.setLevel(data.getOrDefault(KeyFactory.get(key_level), PersistentDataType.INTEGER, 1));
    civilian.setExp(data.getOrDefault(KeyFactory.get(key_exp), PersistentDataType.INTEGER, 0));

    String typeStr = data.get(KeyFactory.get(key_profession), PersistentDataType.STRING);
    if (typeStr != null) {
      ProfessionType type = ProfessionType.valueOf(typeStr);
      civilian.setProfessionType(type);
    }
    
    civilian.setDeathCount(data.getOrDefault(KeyFactory.get(key_death_count), PersistentDataType.INTEGER, 0));
    civilian.setKillStreak(data.getOrDefault(KeyFactory.get(key_kill_count), PersistentDataType.INTEGER, 0));

    return civilian;
  }

  public static void save(Civilian civilian) {
    Player player = civilian.bukkitPlayer();

    PersistentDataContainer data = player.getPersistentDataContainer();

    data.set(KeyFactory.get(key_level), PersistentDataType.INTEGER, civilian.getLevel());
    data.set(KeyFactory.get(key_exp), PersistentDataType.INTEGER, civilian.getExp());
    data.set(KeyFactory.get(key_profession), PersistentDataType.STRING, civilian.getProfessionType().name());
    data.set(KeyFactory.get(key_death_count), PersistentDataType.INTEGER, civilian.getDeathCount());
    data.set(KeyFactory.get(key_kill_count), PersistentDataType.INTEGER, civilian.getKillStreak());

  }

}
