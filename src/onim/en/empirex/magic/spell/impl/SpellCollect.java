package onim.en.empirex.magic.spell.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SpellCollect extends AbstractSpell {

  @Override
  public String id() {
    return "collect";
  }

  @Override
  public String getName() {
    return "Collect";
  }

  @Override
  public String getDescription() {
    return "Collect nearby items.";
  }

  @Override
  public int getCost() {
    return 3;
  }

  @Override
  public int getCooltime() {
    return 600;
  }

  @Override
  public boolean activate(Player player) {
    List<Item> items = player.getNearbyEntities(20.0D, 20.0D, 20.0D).stream().map(e -> {
      if (e instanceof Item)
        return (Item) e;
      return null;
    }).filter(e -> e != null).collect(Collectors.toList());

    if (items.isEmpty())
      return false;

    items.forEach(item -> {
      Vector origin = player.getLocation().toVector();
      Vector target = item.getLocation().toVector();

      Vector velocity = origin.subtract(target).normalize().setY(0.5d)
          .multiply(Math.sqrt(0.2 * origin.distance(target)) / 10);

      item.setVelocity(velocity);
    });

    return false;
  }

}
