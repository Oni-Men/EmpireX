package onim.en.empirex.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExhaustionEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.projectiles.ProjectileSource;

import onim.en.empirex.EmpireX;
import onim.en.empirex.event.civilian.CivilianAttackEvent;
import onim.en.empirex.profession.Profession;
import onim.en.empirex.profession.ProfessionManager;
import onim.en.empirex.profession.ProfessionType;

public class PlayerListener implements Listener {

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    try {
      EmpireX.civilianFactory.load(event.getPlayer());
      EmpireX.civilianFactory.executeIfPresent(event.getPlayer(), c -> {
        c.printInfo(event.getPlayer());
      });
    } catch (RuntimeException e) {
      event.getPlayer().sendMessage(e.getMessage());
    }
  }

  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent event) {
    EmpireX.civilianFactory.unload(event.getPlayer());
  }

  @EventHandler
  public void onPlayerDead(PlayerDeathEvent event) {
    EmpireX.civilianFactory.executeIfPresent(event.getEntity(), c -> {
      c.incrementDeath();
      c.setLevel(c.getLevel() / 2);
    });

    event.setKeepLevel(true);
    event.setDroppedExp(0);
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onPlayerAttack(EntityDamageByEntityEvent event) {
    Entity damager = event.getDamager();
    Player player = null;

    if (damager instanceof Player) {
      player = (Player) damager;
    }

    if (damager instanceof AbstractArrow) {
      ProjectileSource shooter = ((AbstractArrow) damager).getShooter();

      if (shooter instanceof Player) {
        player = (Player) shooter;
      }
    }

    if (player == null) {
      return;
    }

    EmpireX.civilianFactory.executeIfPresent(player, c -> {
      CivilianAttackEvent attackEvent = new CivilianAttackEvent(c, event);
      Bukkit.getPluginManager().callEvent(attackEvent);
    });
  }

  @EventHandler
  public void onPlayerKillEntity(EntityDeathEvent event) {
    Player killer = event.getEntity().getKiller();

    if (killer == null) {
      return;
    }

    EmpireX.civilianFactory.executeIfPresent(killer, c -> {
      c.incrementKillStreak();

      if (c.getProfessionType() == ProfessionType.SERIAL_KILLER) {
        Profession profession = ProfessionManager.getByType(ProfessionType.SERIAL_KILLER);
        profession.onKillEntity(c);
      }
    });
  }

  @EventHandler
  public void onPlayerExhausted(EntityExhaustionEvent event) {
    if (event.getEntityType() != EntityType.PLAYER)
      return;

    HumanEntity entity = event.getEntity();

    EmpireX.civilianFactory.executeIfPresent((Player) entity, c -> {
      if (c.getProfessionType() == ProfessionType.NEET) {
        event.setCancelled(true);
        entity.setExhaustion(0);
      }
    });
  }

  @EventHandler
  public void onExpChange(PlayerExpChangeEvent event) {
    int amount = event.getAmount();

    EmpireX.civilianFactory.executeIfPresent(event.getPlayer(), c -> {
      c.addExp(amount);
    });
    event.setAmount(0);
  }

}
