package onim.en.empirex.profession;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;

import net.kyori.adventure.text.Component;
import onim.en.empirex.player.Civilian;
import onim.en.empirex.util.ComponentsBuilder;

public abstract class Profession {

  private ProfessionType type;

  private List<Component> description;

  public Profession() {
    this(ProfessionType.NEET, new ArrayList<>());
  }

  public Profession(ProfessionType type, List<String> desc) {
    this.type = type;
    this.description = new ComponentsBuilder().appendTexts(desc).get();
  }


  public ProfessionType getType() {
    return this.type;
  }

  public abstract void initialize(Civilian civ);

  public abstract void finalize(Civilian civ);

  public List<Component> description() {
    return new ArrayList<>(this.description);
  }

  protected void setMaxHealth(Civilian civ, double maxHealth) {
    AttributeInstance genericMaxHealth = civ.bukkitPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH);
    genericMaxHealth.setBaseValue(maxHealth);
  }

  public void onKillEntity(Civilian civ) {}
}
