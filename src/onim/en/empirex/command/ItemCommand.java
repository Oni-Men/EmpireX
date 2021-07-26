package onim.en.empirex.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import onim.en.empirex.EmpireX;
import onim.en.empirex.gui.WindowRegistry;
import onim.en.empirex.gui.window.ItemShop;
import onim.en.empirex.item.CustomItem;

public class ItemCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command var2, String var3, String[] args) {

    if (!(sender instanceof Player))
      return false;

    Player player = (Player) sender;

    if (args.length == 0) {
      WindowRegistry.subscribeAndOpen(player, new ItemShop());
    } else {
      String itemId = args[0];
      CustomItem itemModel = EmpireX.itemFactory.get(itemId);

      if (itemModel == null)
        return false;

      player.getInventory().addItem(itemModel.createItem());
    }

    return true;
  }

}
