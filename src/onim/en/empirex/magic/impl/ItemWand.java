package onim.en.empirex.magic.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import onim.en.empirex.EmpireX;
import onim.en.empirex.magic.Wand;
import onim.en.empirex.magic.spell.Spell;
import onim.en.empirex.util.KeyFactory;

public class ItemWand implements Wand {

  public enum WandDataKey {
      ACTIVE_SPELL("wand.active_spell"),
      SPELL_LIST("wand.spell_list");

    private final String key;

    WandDataKey(String key) {
      this.key = key;
    }

    public NamespacedKey key() {
      return KeyFactory.get(key);
    }

  }

  private final ItemStack stack;

  private ItemMeta meta;

  private PersistentDataContainer data;

  public ItemWand(ItemStack stack) {
    this.stack = stack;
    this.meta = stack.getItemMeta();
    this.data = meta.getPersistentDataContainer();
  }

  private List<String> getSpells() {
    if (data.has(WandDataKey.SPELL_LIST.key(), PersistentDataType.STRING)) {
      String[] split = data.get(WandDataKey.SPELL_LIST.key(), PersistentDataType.STRING).split(",");
      return new ArrayList<>(Arrays.asList(split));
    }
    return new ArrayList<>();
  }

  private int getActiveSpellIndex() {
    if (data.has(WandDataKey.ACTIVE_SPELL.key(), PersistentDataType.INTEGER)) {
      return data.get(WandDataKey.ACTIVE_SPELL.key(), PersistentDataType.INTEGER);
    }
    return 0;
  }

  private void setSpells(List<String> spells) {
    String str = String.join(",", spells);
    data.set(WandDataKey.SPELL_LIST.key(), PersistentDataType.STRING, str);
    stack.setItemMeta(meta);
  }

  @Nullable
  @Override
  public Spell getActiveSpell() {
    int activeSpell = getActiveSpellIndex();
    List<String> spells = this.getSpells();

    if (spells.isEmpty()) {
      return null;
    }

    String id = spells.get(activeSpell % spells.size());
    return EmpireX.spellFactory.get(id);
  }

  @Override
  public void setActiveSpell(Spell spell) {
    if (spell == null) {
      return;
    }

    List<String> spells = this.getSpells();
    int index = spells.indexOf(spell.id());
    data.set(WandDataKey.ACTIVE_SPELL.key(), PersistentDataType.INTEGER, index);
  }

  @Override
  public void castSpell(Player player) {
    Spell spell = getActiveSpell();

    if (spell != null) {
      spell.activate(player);
    }
  }

  @Override
  public void addSpell(Spell spell) {
    if (spell == null) {
      return;
    }

    List<String> spells = this.getSpells();
    spells.add(spell.id());
    this.setSpells(spells);
  }

  @Override
  public void removeSpell(Spell spell) {
    if (spell == null) {
      return;
    }

    List<String> spells = this.getSpells();
    if (spells.remove(spell.id())) {
      this.setSpells(spells);
    }
  }

  @Override
  public boolean hasSpell(Spell spell) {
    if (spell == null) {
      return false;
    }

    List<String> spells = this.getSpells();
    return spells.contains(spell.id());
  }

  @Override
  public void addSpells(Collection<Spell> spells) {
    List<String> list = this.getSpells();

    for (Spell spell : spells) {
      list.add(spell.id());
    }

    this.setSpells(list);
  }

  @Override
  public void nextSpell() {
    this.changeSpell(1);
  }

  @Override
  public void previousSpell() {
    this.changeSpell(-1);
  }

  @Override
  public ItemStack getItemStack() {
    return this.stack;
  }


  private void changeSpell(int amount) {
    int lengthSpell = this.getSpells().size();

    if (lengthSpell == 0) {
      return;
    }

    int activeSpell = getActiveSpellIndex();
    int nextSpell = (activeSpell + amount) % lengthSpell;
    data.set(WandDataKey.ACTIVE_SPELL.key(), PersistentDataType.INTEGER, nextSpell);
    stack.setItemMeta(meta);
  }

}
