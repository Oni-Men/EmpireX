package onim.en.empirex;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import onim.en.empirex.block.BlockListener;
import onim.en.empirex.command.DebugCommand;
import onim.en.empirex.command.InfoCommand;
import onim.en.empirex.command.ItemCommand;
import onim.en.empirex.command.SpellEditCommand;
import onim.en.empirex.command.TrashBoxCommand;
import onim.en.empirex.item.ItemFactory;
import onim.en.empirex.item.ItemListener;
import onim.en.empirex.listener.CivilianListener;
import onim.en.empirex.listener.EventListener;
import onim.en.empirex.listener.PlayerListener;
import onim.en.empirex.magic.spell.SpellFactory;
import onim.en.empirex.player.CivilianFactory;
import onim.en.empirex.player.routine.ForceRegenerationWorker;
import onim.en.empirex.util.MessageMarker;
import onim.en.empirex.util.Ticks;

public class EmpireX extends JavaPlugin {

  public static JavaPlugin instance = null;

  public static SpellFactory spellFactory;
  public static ItemFactory itemFactory;
  public static CivilianFactory civilianFactory;

  @Override
  public void onEnable() {
    instance = this;

    this.registerListeners();
    this.initFactory();

    this.getCommand("items").setExecutor(new ItemCommand());
    this.getCommand("trashbox").setExecutor(new TrashBoxCommand());
    this.getCommand("editspell").setExecutor(new SpellEditCommand());

    this.getCommand("debug").setExecutor(new DebugCommand());
    this.getCommand("info").setExecutor(new InfoCommand());

    civilianFactory.loadOnlines();

    this.startWorkers();
  }

  @Override
  public void onDisable() {
    civilianFactory.unloadOnlines();
    MessageMarker.removeAllMarkers();
  }

  private void initFactory() {
    spellFactory = new SpellFactory();
    itemFactory = new ItemFactory();
    civilianFactory = new CivilianFactory();
  }

  private void registerListeners() {
    PluginManager pluginManager = this.getServer().getPluginManager();
    pluginManager.registerEvents(new EventListener(), instance);
    pluginManager.registerEvents(new PlayerListener(), instance);
    pluginManager.registerEvents(new ItemListener(), instance);
    pluginManager.registerEvents(new BlockListener(), instance);
    pluginManager.registerEvents(new CivilianListener(), instance);
  }

  private void startWorkers() {
    Bukkit.getScheduler().runTaskTimer(this, new ForceRegenerationWorker(), 0, Ticks.sec(5));

  }
}
