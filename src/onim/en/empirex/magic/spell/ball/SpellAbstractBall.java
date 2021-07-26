package onim.en.empirex.magic.spell.ball;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import onim.en.empirex.EmpireX;
import onim.en.empirex.magic.spell.impl.AbstractSpell;
import onim.en.empirex.util.Memory;
import onim.en.empirex.util.PackedParticle;

public abstract class SpellAbstractBall extends AbstractSpell {

  public static final String SPELL_BALL_ITEM = "spell.ball_item";
  public static final String SPELL_BALL_DUMMY = "spell.ball_dummy";

  @Nonnull
  public abstract Material getBallMaterial();

  @Nullable
  public abstract Sound getShotSound();

  @Nullable
  public abstract Sound getHitSound();

  @Nullable
  public abstract PackedParticle<?> getTrajectoryParticle();

  @Nullable
  public abstract PackedParticle<?> getHitParticle();

  public abstract boolean doesGoThrough();

  public abstract int getLifespanTick();

  public abstract void onHitEntity(LivingEntity living);

  private void onHit(Entity ball, BukkitTask task) {
    ball.remove();
    task.cancel();

    if (this.getHitParticle() != null)
      this.getHitParticle().spawnParticle(ball.getLocation());

    if (this.getHitSound() != null)
      ball.getWorld().playSound(ball.getLocation(), this.getHitSound(), 0.75f, 1.00f);
  }

  @Override
  public boolean activate(Player player) {
    World world = player.getWorld();
    Location location = player.getLocation().add(0, 1, 0);

    Snowball ball = player.launchProjectile(Snowball.class);
    Memory ballMemory = new Memory(ball);
    ballMemory.addTag(SPELL_BALL_DUMMY);

    // Item item = world.dropItem(location, new ItemStack(this.getBallMaterial()));
    // Memory itemMemory = new Memory(item);
    // itemMemory.addTag(SPELL_BALL_ITEM);

    ball.setVelocity(location.getDirection().multiply(2.0D));
    ball.setGravity(false);
    ball.setItem(new ItemStack(this.getBallMaterial()));

    if (this.getShotSound() != null) {
      world.playSound(location, this.getShotSound(), 1, 1);
    }

    Bukkit.getScheduler().runTaskTimer(EmpireX.instance, task -> {
      if (ball.isDead()) {
        this.onHit(ball, task);
      }

      Vector vecA = location.getDirection().normalize();
      Vector vecB = ball.getVelocity().normalize();
      if (hasDirectionChanged(vecA, vecB)) {
        this.onHit(ball, task);
      }

      if (this.getTrajectoryParticle() != null) {
        this.getTrajectoryParticle().spawnParticle(ball.getLocation().add(0, 0, 0));
      }

      List<LivingEntity> nearbyEntities = this.getNearbyLivingsWithoutMe(ball, player);
      if (!nearbyEntities.isEmpty()) {
        nearbyEntities.forEach(e -> {
          this.onHitEntity(e);
          if (!this.doesGoThrough()) {
            this.onHit(ball, task);
          }
        });
      }
      if (ball.getTicksLived() > this.getLifespanTick()) {
        this.onHit(ball, task);
      }
    }, 1L, 1L);

    return true;
  }

  private boolean hasDirectionChanged(Vector a, Vector b) {
    // ブロックへの衝突を検知するため、発射時の進行方向と、並行であるかどうかをチェック。微妙に誤差が発生するのでイコールにはしない
    return Math.abs(1 - Math.abs(a.dot(b))) > 0.01;
  }

  private List<LivingEntity> getNearbyLivingsWithoutMe(Entity ball, Player player) {
    return ball.getNearbyEntities(0.5D, 0.5D, 0.5D)
        .stream()
        .filter(e -> e instanceof LivingEntity)
        .filter(e -> !e.getUniqueId().equals(player.getUniqueId()))
        .map(e -> (LivingEntity) e)
        .collect(Collectors.toList());

  }

}
