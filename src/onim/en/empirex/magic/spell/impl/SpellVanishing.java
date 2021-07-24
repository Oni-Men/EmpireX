package onim.en.empirex.magic.spell.impl;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.google.common.collect.Lists;

import onim.en.empirex.EmpireX;

public class SpellVanishing extends AbstractSpell {

  private static final List<PotionEffect> effects = Lists.newArrayList();
  static {
    effects.add(new PotionEffect(PotionEffectType.INVISIBILITY, 80, 0));
    effects.add(new PotionEffect(PotionEffectType.SPEED, 80, 2));
  }

  @Override
  public String getName() {
    return "Vanishing";
  }

  @Override
  public String id() {
    return "vanishing";
  }

  @Override
  public String getDescription() {
    return "Vanish myself.";
  }

  @Override
  public int getCost() {
    return 10;
  }

  @Override
  public int getCooltime() {
    return 60;
  }

  @Override
  public boolean activate(Player player) {
    Bukkit.getOnlinePlayers().forEach(p -> {
      p.hidePlayer(EmpireX.instance, player);
    });

    World world = player.getWorld();
    Location location = player.getLocation();
    world.spawnParticle(Particle.PORTAL, location.add(0, 1, 0), 100);
    world.playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
    player.playSound(location, Sound.BLOCK_CONDUIT_AMBIENT, 128, 1);
    player.addPotionEffects(effects);

    Bukkit.getScheduler().runTaskLater(EmpireX.instance, task -> {
      world.spawnParticle(Particle.REVERSE_PORTAL, player.getLocation().add(0, 1, 0), 100);
      world.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
      Bukkit.getOnlinePlayers().forEach(p -> {
        p.showPlayer(EmpireX.instance, player);
      });
    }, 80);
    return true;
  }
}