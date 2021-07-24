package onim.en.empirex.magic.spell.impl;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.google.common.collect.Maps;

import onim.en.empirex.EmpireX;

public class SpellGrabbing extends AbstractSpell {

  public static final HashMap<UUID, GrabbingData> grabbingMap = Maps.newHashMap();

  @Override
  public String id() {
    return "grabbing";
  }

  @Override
  public String getName() {
    return "Grab";
  }

  @Override
  public String getDescription() {
    return "Click to grab the mob you are looking at. click again to release it.";
  }

  @Override
  public int getCost() {
    return 1;
  }

  @Override
  public int getCooltime() {
    return 600;
  }

  @Override
  public boolean activate(Player player) {
    if (grabbingMap.containsKey(player.getUniqueId())) {
      grabbingMap.get(player.getUniqueId()).cancel();
      grabbingMap.remove(player.getUniqueId());
      return true;
    }

    Location location = player.getLocation();
    Vector direction = location.getDirection();
    double maxDistance = 30;
    double raySize = 1;
    Predicate<Entity> filter = e -> !e.equals(player);
    RayTraceResult result = player.getWorld().rayTraceEntities(location, direction, maxDistance, raySize, filter);

    if (result == null)
      return false;

    Entity entity = result.getHitEntity();

    GrabbingData grabbingData = new GrabbingData(player, entity);
    grabbingData.start();

    grabbingMap.put(player.getUniqueId(), grabbingData);
    return false;
  }

  static class GrabbingData {
    public final Player grabber;
    public final Entity grabee;
    private BukkitTask task;

    public GrabbingData(Player grabber, Entity grabee) {
      this.grabber = grabber;
      this.grabee = grabee;
    }

    public void start() {
      Location grabberLoc = this.grabber.getLocation();
      Location grabeeLoc = this.grabee.getLocation();
      double distance = grabberLoc.distance(grabeeLoc);

      this.task = Bukkit.getScheduler().runTaskTimer(EmpireX.instance, () -> {
        if (!this.grabber.getWorld().equals(this.grabee.getWorld())) {
          this.cancel();
        }

        if (this.grabee.isDead() || this.grabber.isDead())
          this.cancel();

        if (!this.grabber.isOnline())
          this.cancel();

        Location currentGrabberLocation = this.grabber.getLocation();
        Vector currentGrabberDirection = currentGrabberLocation.getDirection();
        Location nextGrabeeLocation = currentGrabberLocation.add(currentGrabberDirection.multiply(distance));
        nextGrabeeLocation.add(0,  1,  0).setDirection(this.grabee.getLocation().getDirection());
        this.grabee.teleport(nextGrabeeLocation, TeleportCause.PLUGIN);
        this.grabee.setFallDistance(0f);
        this.grabee.setGlowing(true);
      }, 0L, 1L);
    }

    public void cancel() {
      if (this.task != null) {
        this.task.cancel();
        this.grabee.setGlowing(false);
      }
    }

    public boolean isCancelled() {
      return this.task.isCancelled();
    }

  }
}
