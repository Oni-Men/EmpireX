package onim.en.empirex.profession.impl;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;

import onim.en.empirex.player.Civilian;
import onim.en.empirex.profession.Profession;
import onim.en.empirex.profession.ProfessionType;

public class Rikisi extends Profession {

  private static final List<String> desc = new LinkedList<>();
  static {
    desc.add("レベル１で体力がハート2.5多い。4レベル毎にハートが1増える。");
    desc.add("体が重いのでノックバックしにくいらしい");
  }

  public Rikisi() {
    super(ProfessionType.RIKISI, desc);
  }

  @Override
  public void initialize(Civilian civ) {
    this.setMaxHealth(civ, 25 + civ.getLevel() / 2);

    AttributeInstance kbResistance = civ.bukkitPlayer().getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
    float percent = Math.min(100, civ.getLevel()) / 100F;
    kbResistance.setBaseValue(percent);
  }

  @Override
  public void finalize(Civilian civ) {
    this.setMaxHealth(civ, 20);
    civ.bukkitPlayer().getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0);
  }

}
