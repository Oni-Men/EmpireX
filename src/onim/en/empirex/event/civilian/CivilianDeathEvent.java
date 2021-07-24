package onim.en.empirex.event.civilian;

import org.bukkit.event.HandlerList;

import onim.en.empirex.player.Civilian;

public class CivilianDeathEvent extends CivilianEvent {
  private static final HandlerList handlers = new HandlerList();

  public CivilianDeathEvent(Civilian who) {
    super(who);
  }

  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
}
