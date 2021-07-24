package onim.en.empirex.item;

import org.bukkit.ChatColor;

import net.kyori.adventure.text.Component;

public enum ItemTag {
  UNIQUE("Unique"),
  UNDROPPABLE("Undroppable"),
  UNCLICKABLE("Unclickable");

  private final String name;
  private final String toString;
  private ItemTag(String name) {
    this.name = name;
    this.toString = ChatColor.YELLOW + this.name;
  }

  public String toString() {
    return this.toString;
  }

  public Component toComponent() {
    return Component.text(this.toString);
  }
}
