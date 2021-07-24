package onim.en.empirex.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import onim.en.empirex.EmpireX;
import onim.en.empirex.gui.WindowRegistry;
import onim.en.empirex.gui.window.ProfessionSelectGUI;
import onim.en.empirex.profession.ProfessionType;

public class DebugCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command var2, String var3, String[] args) {

    if (args.length == 0) {
      return false;
    }

    if (!(sender instanceof Player)) {
      return false;
    }

    Player player = (Player) sender;
    String sub = args[0];


    switch (sub) {
      case "level":
        if (args.length == 2) {
          int i = Integer.parseInt(args[1]);
          EmpireX.civilianFactory.executeIfPresent(player, c -> {
            c.setLevel(i);
          });
        }
        break;
      case "prof":
        if (args.length == 1) {
          WindowRegistry.subscribeAndOpen(player, new ProfessionSelectGUI());
        } else if (args.length == 2) {
          try {

            ProfessionType type = ProfessionType.valueOf(args[1]);
            EmpireX.civilianFactory.executeIfPresent(player, c -> {
              c.setProfessionType(type);
            });
          } catch (IllegalArgumentException e) {
          }
        }
        break;
    }

    return true;
  }

}
