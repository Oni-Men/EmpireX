package onim.en.empirex.event.civilian;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

import onim.en.empirex.player.Civilian;

@SuppressWarnings("deprecation")
public class CivilianAttackEvent extends CivilianEvent {
  private static final HandlerList handlers = new HandlerList();

  private final EntityDamageByEntityEvent event;

  public CivilianAttackEvent(Civilian who, EntityDamageByEntityEvent event) {
    super(who);
    this.event = event;
  }

  public Entity getDamagee() {
    return event.getEntity();
  }

  public double getDamage() {
    return event.getDamage();
  }

  public double getDamage(DamageModifier modifier) {
    return event.getDamage(modifier);
  }

  public void setDamage(double damage) {
    event.setDamage(damage);
  }

  public void setDamage(DamageModifier type, double damage) {
    event.setDamage(type, damage);
  }

  public DamageCause getCause() {
    return event.getCause();
  }

  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
}
