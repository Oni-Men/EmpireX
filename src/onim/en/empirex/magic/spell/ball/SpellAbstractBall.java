package onim.en.empirex.magic.spell.ball;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;

import onim.en.empirex.EmpireX;
import onim.en.empirex.magic.spell.impl.AbstractSpell;
import onim.en.empirex.util.PackedParticle;

public abstract class SpellAbstractBall extends AbstractSpell {

  public static final String SPELL_BALL_KEY = "spell.entity_falling_block";

  @Nonnull
  public abstract BlockData getBlockData();

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

  @Override
  public boolean activate(Player player) {
    World world = player.getWorld();
    Location location = player.getLocation().add(0, 1, 0);

    FallingBlock ball = world.spawnFallingBlock(location, this.getBlockData());
    ball.setVelocity(location.getDirection().multiply(2.0D));
    ball.setGravity(false);
    ball.setDropItem(false);
    ball.setMetadata(SPELL_BALL_KEY, new FixedMetadataValue(EmpireX.instance, "true"));

    if (this.getShotSound() != null) {
      world.playSound(location, this.getShotSound(), 1, 1);
    }

    Consumer<BukkitTask> onHit = task -> {
      ball.remove();
      task.cancel();

      if (this.getHitParticle() != null)
        this.getHitParticle().spawnParticle(ball.getLocation());

      if (this.getHitSound() != null)
        world.playSound(ball.getLocation(), this.getHitSound(), 0.75f, 1.00f);
    };

    Bukkit.getScheduler().runTaskTimer(EmpireX.instance, task -> {
      if (ball.isDead()) {
        onHit.accept(task);
      }

      Vector vecA = location.getDirection().normalize();
      Vector vecB = ball.getVelocity().normalize();
      //ブロックへの衝突を検知するため、発射時の進行方向と、並行であるかどうかをチェック。微妙に誤差が発生するのでイコールにはしない
      if (Math.abs(1 - Math.abs(vecA.dot(vecB))) > 0.01) {
        onHit.accept(task);
      }

      if (this.getTrajectoryParticle() != null) {
        this.getTrajectoryParticle().spawnParticle(ball.getLocation().add(0, 0.5, 0));
      }

      List<LivingEntity> nearbyEntities = this.getNearbyLivingsWithoutMe(ball, player);
      if (!nearbyEntities.isEmpty()) {
        nearbyEntities.forEach(e -> {
          this.onHitEntity(e);
          if (!this.doesGoThrough()) {
            onHit.accept(task);
          }
        });
      }
      if (ball.getTicksLived() > this.getLifespanTick()) {
        onHit.accept(task);
      }
    }, 1L, 1L);

    return true;
  }


  private List<LivingEntity> getNearbyLivingsWithoutMe(FallingBlock ball, Player player) {
    return ball.getNearbyEntities(0.5D, 0.5D, 0.5D)
        .stream()
        .filter(e -> e instanceof LivingEntity)
        .filter(e -> !e.getUniqueId().equals(player.getUniqueId()))
        .map(e -> (LivingEntity) e)
        .collect(Collectors.toList());

  }

}
