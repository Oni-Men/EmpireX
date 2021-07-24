package onim.en.empirex.magic.spell.ball;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import onim.en.empirex.util.PackedParticle;

public class SpellFrozenBall extends SpellAbstractBall {

  public static final PackedParticle<BlockData> frozenTrajectory = new PackedParticle<>(Particle.BLOCK_DUST, 10,
      Bukkit.createBlockData(Material.ICE)),
      frozenHit = new PackedParticle<BlockData>(Particle.BLOCK_CRACK, 100, 5d, Bukkit.createBlockData(Material.ICE));

  @Override
  public String id() {
    return "frozen_ball";
  }

  @Override
  public String getName() {
    return "Ice Crystal";
  }

  @Override
  public String getDescription() {
    return "Shoot an ice crystal which gives slow when hit.";
  }

  @Override
  public int getCost() {
    return 1;
  }

  @Override
  public int getCooltime() {
    return 20;
  }

  @Override
  public BlockData getBlockData() {
    return Bukkit.createBlockData(Material.ICE);
  }

  @Override
  public Sound getShotSound() {
    return Sound.ENTITY_GHAST_SHOOT;
  }

  @Override
  public Sound getHitSound() {
    return Sound.ENTITY_ZOMBIE_VILLAGER_CURE;
  }

  @Override
  public PackedParticle<BlockData> getTrajectoryParticle() {
    return frozenTrajectory;
  }

  @Override
  public PackedParticle<BlockData> getHitParticle() {
    return frozenHit;
  }

  @Override
  public boolean doesGoThrough() {
    return false;
  }

  @Override
  public int getLifespanTick() {
    return 60;
  }

  @Override
  public void onHitEntity(LivingEntity living) {
    living.damage(5);
    living.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 3));
    living.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 80, 3));
  }
}
