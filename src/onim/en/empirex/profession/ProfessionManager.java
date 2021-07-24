package onim.en.empirex.profession;

import java.util.Collection;
import java.util.HashMap;

import com.google.common.collect.Maps;

import onim.en.empirex.player.Civilian;
import onim.en.empirex.profession.impl.Neet;
import onim.en.empirex.profession.impl.Rikisi;
import onim.en.empirex.profession.impl.SerialKiller;
import onim.en.empirex.profession.impl.Strider;

public class ProfessionManager {

  private static HashMap<ProfessionType, Profession> professions = Maps.newHashMap();
  static {
    registerProfession(new Neet());
    registerProfession(new Rikisi());
    registerProfession(new SerialKiller());
    registerProfession(new Strider());
  }

  public static Collection<Profession> getProfessions() {
    return professions.values();
  }

  public static void registerProfession(Profession profession) {
    if (profession == null) {
      return;
    }
    professions.put(profession.getType(), profession);
  }

  public static Profession getByType(ProfessionType type) {
    if (type == null) {
      return null;
    }
    return professions.get(type);
  }

  public static void initialize(Civilian civilian) {
    Profession profession = getByType(civilian.getProfessionType());

    if (profession == null) {
      return;
    }

    profession.initialize(civilian);
  }

  public static void finalize(Civilian civilian) {
    Profession profession = getByType(civilian.getProfessionType());

    if (profession == null) {
      return;
    }

    profession.finalize(civilian);
  }
}

