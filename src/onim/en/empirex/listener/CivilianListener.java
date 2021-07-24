package onim.en.empirex.listener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import onim.en.empirex.event.civilian.CivilianAttackEvent;
import onim.en.empirex.player.Civilian;
import onim.en.empirex.util.MessageMarker;

public class CivilianListener implements Listener {

  @EventHandler
  public void onCivilianAttack(CivilianAttackEvent event) {
    Civilian civilian = event.getCivilian();
    double damage = event.getDamage();

    damage *= Math.log(civilian.getLevel() + Math.E - 1);

    double angle = Math.random() * Math.PI * 2;
    Location loc = event.getDamagee().getLocation().add(Math.cos(angle), 0, Math.sin(angle));

    String msg = ChatColor.LIGHT_PURPLE + String.format("-%.1f", damage);
    MessageMarker.spawn(loc, msg);
    event.setDamage(damage);
  }
}
