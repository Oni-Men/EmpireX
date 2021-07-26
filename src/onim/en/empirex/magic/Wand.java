package onim.en.empirex.magic;

import java.util.Collection;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import onim.en.empirex.magic.spell.Spell;

public interface Wand {

  public Spell getActiveSpell();

  public void setActiveSpell(Spell spell);

  public boolean castSpell(Player player);

  public void addSpell(Spell spell);

  public void removeSpell(Spell spell);

  public boolean hasSpell(Spell spell);

  public void addSpells(Collection<Spell> spells);

  public void nextSpell();

  public void previousSpell();

  public ItemStack getItemStack();
}
