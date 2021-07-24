package onim.en.empirex.magic.spell.ball;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import onim.en.empirex.util.PackedParticle;

public class SpellHealBall extends SpellAbstractBall {

  public static final PackedParticle<Void> healTrajectory, healHit;
  static {
    healTrajectory = new PackedParticle<>(Particle.HEART, 5, 0.01d);
    healHit = new PackedParticle<>(Particle.HEART, 50, 1.0D, 1.0D, 1.0D, 1.0D);
  }

  @Override
  public String id() {
    return "heal_ball";
  }

  @Override
  public String getName() {
    return "Heal";
  }

  @Override
  public String getDescription() {
    return "Shoot an orb. when hit the mob, instantly heals ♡x2 and gives regeneration to it.";
  }

  @Override
  public int getCost() {
    return 10;
  }

  @Override
  public int getCooltime() {
    return 400;
  }

  @Override
  public BlockData getBlockData() {
    return Bukkit.createBlockData(Material.RED_GLAZED_TERRACOTTA);
  }

  @Override
  public Sound getShotSound() {
    return Sound.BLOCK_BEACON_ACTIVATE;
  }

  @Override
  public Sound getHitSound() {
    return Sound.BLOCK_BEACON_POWER_SELECT;
  }

  @Override
  public PackedParticle<?> getTrajectoryParticle() {
    return healTrajectory;
  }

  @Override
  public PackedParticle<?> getHitParticle() {
    return healHit;
  }

  @Override
  public boolean doesGoThrough() {
    return true;
  }

  @Override
  public int getLifespanTick() {
    return 60;
  }

  @Override
  public void onHitEntity(LivingEntity living) {
    if (living instanceof Monster)
      return;

    double maxHealth = living.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
    double nextHealth = living.getHealth() + 4.0D;

    living.setHealth(Math.min(maxHealth, nextHealth));
    living.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 0));
  }

}
