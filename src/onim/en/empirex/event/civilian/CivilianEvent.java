package onim.en.empirex.event.civilian;

import org.bukkit.event.player.PlayerEvent;

import onim.en.empirex.player.Civilian;

public abstract class CivilianEvent extends PlayerEvent {

  private final Civilian civilian;

  public CivilianEvent(Civilian who) {
    super(who.bukkitPlayer());
    this.civilian = who;
  }

  public final Civilian getCivilian() {
    return civilian;
  }
}
