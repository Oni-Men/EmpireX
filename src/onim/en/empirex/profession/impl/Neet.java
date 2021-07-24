package onim.en.empirex.profession.impl;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import onim.en.empirex.player.Civilian;
import onim.en.empirex.profession.Profession;
import onim.en.empirex.profession.ProfessionType;

public class Neet extends Profession {

  private static final List<String> desc = new LinkedList<>();
  static {
    desc.add("ニートなので体力がない。あと力も弱い。");
    desc.add("親のすねをかじっているので常に満腹状態である");
  }

  public Neet() {
    super(ProfessionType.NEET, desc);
  }

  @Override
  public void initialize(Civilian civ) {
    setMaxHealth(civ, 10);

    PotionEffect effect = new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, true, true);
    civ.bukkitPlayer().addPotionEffect(effect);
    civ.bukkitPlayer().setExhaustion(0);
    civ.bukkitPlayer().setFoodLevel(20);
  }

  @Override
  public void finalize(Civilian civ) {
    setMaxHealth(civ, 20);

    Player player = civ.bukkitPlayer();

    PotionEffect weakness = player.getPotionEffect(PotionEffectType.WEAKNESS);

    if (weakness != null && weakness.getAmplifier() == 0) {
      civ.bukkitPlayer().removePotionEffect(PotionEffectType.WEAKNESS);
    }
  }


}
