package onim.en.empirex.item.entity.wand;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import onim.en.empirex.EmpireX;
import onim.en.empirex.magic.spell.Spell;

public class BoneWand extends AbstractWand {

  public BoneWand() {
    super("bone_wand", "Bone Wand");
  }

  @Override
  public Material getMaterial() {
    return Material.BONE;
  }

  @Override
  public double getCooldown() {
    return 0.5;
  }

  @Override
  public List<Spell> getDefaultSpells() {
    return Arrays.asList(EmpireX.spellFactory.get("frozen_ball"));
  }

}
