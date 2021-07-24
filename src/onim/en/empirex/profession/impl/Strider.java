package onim.en.empirex.profession.impl;

import java.util.LinkedList;
import java.util.List;

import onim.en.empirex.player.Civilian;
import onim.en.empirex.profession.Profession;
import onim.en.empirex.profession.ProfessionType;

public class Strider extends Profession {

  private static final List<String> desc = new LinkedList<>();
  static {
    desc.add("歩くのが速い");
    desc.add("レベル毎に速さが上昇する");
  }

  public Strider() {
    super(ProfessionType.STRIDER, desc);
  }

  @Override
  public void initialize(Civilian civ) {
    float scale = civ.getLevel();
    float additionalSpeed = Math.min(0.4F, scale / 100F);
    civ.bukkitPlayer().setWalkSpeed(0.2F + additionalSpeed);
  }

  @Override
  public void finalize(Civilian civ) {
    civ.bukkitPlayer().setWalkSpeed(0.2F);
  }

}
