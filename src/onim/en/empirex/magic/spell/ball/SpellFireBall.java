package onim.en.empirex.magic.spell.ball;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;

import onim.en.empirex.util.PackedParticle;

public class SpellFireBall extends SpellAbstractBall {

  public static final PackedParticle<Void> fireTrajectory = new PackedParticle<>(Particle.FLAME, 5, 0.01d),
      fireHit = new PackedParticle<>(Particle.FLAME, 100, 0.1d);

  @Override
  public String getName() {
    return "Fireball";
  }

  @Override
  public String id() {
    return "fire_ball";
  }

  @Override
  public String getDescription() {
    return "Shoot a fireball which ignites the mob when hit.";
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
  public Material getBallMaterial() {
    return Material.MAGMA_CREAM;
  }

  @Override
  public Sound getShotSound() {
    return Sound.ENTITY_BLAZE_SHOOT;
  }

  @Override
  public Sound getHitSound() {
    return Sound.ENTITY_GENERIC_EXPLODE;
  }

  @Override
  public PackedParticle<Void> getTrajectoryParticle() {
    return fireTrajectory;
  }

  @Override
  public PackedParticle<Void> getHitParticle() {
    return fireHit;
  }


  @Override
  public int getLifespanTick() {
    return 60;
  }

  @Override
  public boolean doesGoThrough() {
    return false;
  }

  @Override
  public void onHitEntity(LivingEntity living) {
    living.damage(5);
    living.setFireTicks(40);
  }

}
