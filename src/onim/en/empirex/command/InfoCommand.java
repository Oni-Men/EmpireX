package onim.en.empirex.command;

import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import onim.en.empirex.EmpireX;

public class InfoCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command arg1, String label, String[] args) {

    if (!(sender instanceof Player)) {
      return true;
    }

    AtomicReference<Player> target = new AtomicReference<Player>((Player)sender);

    if (args.length != 0) {
      target.set(Bukkit.getPlayer(args[0]));
    }

    EmpireX.civilianFactory.executeIfPresent(target.get(), c -> {
      c.printInfo(target.get());
    });

    return true;
  }

}
