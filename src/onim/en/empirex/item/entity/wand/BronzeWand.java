package onim.en.empirex.item.entity.wand;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import onim.en.empirex.EmpireX;
import onim.en.empirex.magic.spell.Spell;

public class BronzeWand extends AbstractWand {

  public BronzeWand() {
    super("bronze_wand", "Bronze Wand");
  }

  @Override
  public Material getMaterial() {
    return Material.STICK;
  }

  @Override
  public double getCooldown() {
    return 1.5;
  }

  @Override
  public List<Spell> getDefaultSpells() {
    return Arrays.asList(EmpireX.spellFactory.get("fire_ball"));
  }

}
