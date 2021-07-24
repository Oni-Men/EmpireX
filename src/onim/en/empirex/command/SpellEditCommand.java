package onim.en.empirex.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.base.Preconditions;

import onim.en.empirex.gui.WindowRegistry;
import onim.en.empirex.gui.window.SpellEditor;

public class SpellEditCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command var2, String var3, String[] args) {
    try {
      Preconditions.checkState(sender instanceof Player);

      Player player = (Player) sender;

      SpellEditor spellEditor = new SpellEditor(player.getInventory().getItemInMainHand());
      WindowRegistry.subscribeAndOpen(player, spellEditor);

    } catch (Exception e) {
      return false;
    }

    return true;
  }

}
