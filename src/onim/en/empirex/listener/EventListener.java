package onim.en.empirex.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import onim.en.empirex.gui.WindowRegistry;
import onim.en.empirex.item.ItemUtil;
import onim.en.empirex.magic.spell.ball.SpellAbstractBall;
import onim.en.empirex.util.EntityUtil;

public class EventListener implements Listener {

  @EventHandler(priority = EventPriority.LOWEST)
  public void onItemDrop(PlayerDropItemEvent event) {
    if (ItemUtil.isUndroppable(event.getItemDrop().getItemStack())) {
      event.setCancelled(true);
    }
  }

  @SuppressWarnings("deprecation")
  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    if (ItemUtil.isUnclickable(event.getCurrentItem())) {
      event.setCancelled(true);
    }

    String title = event.getView().getTitle();
    WindowRegistry.executeIfPresent(title, w -> w.onClick(event));
    WindowRegistry.update();
  }

  @SuppressWarnings("deprecation")
  @EventHandler
  public void onInventoryClose(InventoryCloseEvent event) {
    String title = event.getView().getTitle();
    WindowRegistry.executeIfPresent(title, w -> w.onClose(event));
    WindowRegistry.update();

  }

  @EventHandler
  public void onBlockChange(EntityChangeBlockEvent event) {
    if (event.getEntity().hasMetadata(SpellAbstractBall.SPELL_BALL_KEY)) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onProjectileLaunch(ProjectileLaunchEvent event) {
    Projectile projectile = event.getEntity();
    ProjectileSource shooter = projectile.getShooter();

    if (shooter instanceof Skeleton) {
      Entity vehicle = ((Skeleton) shooter).getVehicle();
      if (vehicle != null && vehicle.getType() == EntityType.CREEPER) {
        EntityUtil.makeEntityExplodable(projectile, 2F);
      }
    }
  }

  @EventHandler
  public void onProjectileHitEvent(ProjectileHitEvent event) {
    Projectile projectile = event.getEntity();

    if (EntityUtil.isEntityExplodable(projectile)) {
      Location loc = projectile.getLocation();
      float power = EntityUtil.getEntityExplosionPower(projectile);
      loc.getWorld().createExplosion(projectile, power, false, false);
    }

    Entity hitEntity = event.getHitEntity();

    if (hitEntity instanceof LivingEntity) {
      if (EntityUtil.isEntityNoImmunity(projectile)) {
        ((LivingEntity) hitEntity).setNoDamageTicks(0);
      }
    }
  }

  @EventHandler
  public void onEntitySpawn(EntitySpawnEvent event) {

    if (event.getEntityType() == EntityType.CREEPER) {
      if (Math.random() < 0.1) {

        Entity entity = event.getEntity();
        Entity skeleton = entity.getWorld().spawnEntity(entity.getLocation(), EntityType.SKELETON);
        entity.addPassenger(skeleton);
      }
    }

  }

  @EventHandler
  public void onEntityDeath(EntityDeathEvent event) {
    LivingEntity entity = event.getEntity();

    if (entity.getType() == EntityType.SKELETON) {
      Entity vehicle = entity.getVehicle();

      if (vehicle != null && vehicle.getType() == EntityType.CREEPER) {
        event.setDroppedExp(event.getDroppedExp() * 2);
        event.getDrops().add(new ItemStack(Material.ARROW, 4));
      }

    }
  }

  @EventHandler
  public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
    Entity entity = event.getEntity();

    if (entity instanceof Tameable) {

      if (((Tameable) entity).isTamed()) {
        event.setCancelled(true);
        ((Tameable) entity).setNoDamageTicks(10);
        entity.getWorld().playSound(entity.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1f, 1f);
      }

    }
  }
}
