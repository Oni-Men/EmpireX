package onim.en.empirex.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import net.kyori.adventure.text.Component;

public class TrashBoxCommand implements CommandExecutor {

  private static final String TRASH_BOX_GUI_TITLE = ChatColor.RED + "Trash Box [CANNOT RESTORE]";

  @Override
  public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {

    if (sender instanceof Player) {

      Inventory trashBox = Bukkit.createInventory(null, 9 * 3, Component.text(TRASH_BOX_GUI_TITLE));

      ((Player) sender).openInventory(trashBox);
    }

    return false;
  }

}
