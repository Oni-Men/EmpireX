package onim.en.empirex.player;

import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import onim.en.empirex.base.FactoryBase;
import onim.en.empirex.player.load.CivilianDataLoader;

public class CivilianFactory extends FactoryBase<Civilian> {


  public void load(Player player) throws RuntimeException {
    if (has(player.getUniqueId().toString())) {
      throw new RuntimeException("既にデータが存在します。再度ログインしてください。");
    }

    Civilian load = CivilianDataLoader.load(player);

    if (load == null) {
      load = new Civilian(player);
    }

    set(load);
  }

  public void unload(Player player) throws RuntimeException {
    Civilian remove = remove(player.getUniqueId().toString());

    if (remove != null) {
      CivilianDataLoader.save(remove);
    }

  }

  public void loadOnlines() {
    for (Player player : Bukkit.getOnlinePlayers()) {
      load(player);
    }
  }

  public void unloadOnlines() {
    for (Player player : Bukkit.getOnlinePlayers()) {
      unload(player);
    }
  }

  public void executeIfPresent(Player player, Consumer<Civilian> executor) {
    executeIfPresent(player.getUniqueId().toString(), executor);
  }

  @Override
  protected void init() {
  }

  @Override
  protected Civilian value(String key) {
    return null;
  }
}
