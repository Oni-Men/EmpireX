package onim.en.empirex.player;

import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.google.common.base.Strings;

import onim.en.empirex.EmpireX;

public class ExpUtil {

  public static void playLevelUpAnimation(Player player, int oldLevel, int newLevel) {

    Bukkit.getScheduler().runTask(EmpireX.instance, () -> {
      player.sendTitle(ChatColor.GREEN + Integer.toString(oldLevel), "", 5, 20, 0);
    });

    AtomicInteger count = new AtomicInteger(0);

    Bukkit.getScheduler().runTaskTimer(EmpireX.instance, task -> {
      char[] chars = Strings.repeat(ChatColor.DARK_GRAY + ">", 5).toCharArray();
      chars[count.getAndAdd(1) * 3 + 1] = ChatColor.YELLOW.getChar();
      player.sendTitle(new String(chars), "", 0, 4, 0);

      if (count.get() >= 5) {
        task.cancel();
      }
    }, 25, 2);

    Bukkit.getScheduler().runTaskLater(EmpireX.instance, () -> {
      player.sendTitle(ChatColor.GREEN + Integer.toString(newLevel), "", 5, 20, 10);
    }, 35);
  }

}
