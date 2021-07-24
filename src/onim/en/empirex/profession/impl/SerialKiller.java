package onim.en.empirex.profession.impl;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import onim.en.empirex.player.Civilian;
import onim.en.empirex.profession.Profession;
import onim.en.empirex.profession.ProfessionType;

public class SerialKiller extends Profession {

  private static final List<String> desc = new LinkedList<>();
  static {
    desc.add("キルすると15秒間、攻撃力が上昇する。");
    desc.add("連続キルで、上昇する攻撃力が増える。");
  }

  public SerialKiller() {
    super(ProfessionType.SERIAL_KILLER, desc);
  }

  @Override
  public void initialize(Civilian civ) {

  }

  @Override
  public void finalize(Civilian civ) {

  }

  @Override
  public void onKillEntity(Civilian civ) {
    Player player = civ.bukkitPlayer();
    PotionEffect strength = player.getPotionEffect(PotionEffectType.INCREASE_DAMAGE);

    int amplifier = 1;

    if (strength != null) {
      amplifier = strength.getAmplifier();
    }

    if ((civ.getKillStreak() >>> amplifier & 1) == 1) {
      amplifier++;
    }

    if (amplifier > 5) {
      amplifier = 5;
    }

    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 15, amplifier, true, true));
  }

}
