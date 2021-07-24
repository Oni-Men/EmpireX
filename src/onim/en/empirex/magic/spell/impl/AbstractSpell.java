package onim.en.empirex.magic.spell.impl;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import onim.en.empirex.item.ItemUtil;
import onim.en.empirex.magic.spell.Spell;

public abstract class AbstractSpell implements Spell {

  @Override
  public ItemStack toItemStack(boolean available) {
    ItemStack stack = new ItemStack(available ? Material.GREEN_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);
    ItemMeta meta = stack.getItemMeta();

    List<Component> lore = new ArrayList<>();
    lore.add(Component.text("#" + id(), TextColor.color(0x333333)));
    lore.add(Component.text("Cost: " + getCost(), TextColor.color(0xffff33)));
    lore.add(Component.text("Cooltime: " + getCooltime(), TextColor.color(0xffff33)));
    lore.add(Component.empty());
    lore.add(Component.text(getDescription(), TextColor.color(0x666666)));

    meta.displayName(Component.text(getName()));
    meta.lore(lore);
    stack.setItemMeta(meta);

    ItemUtil.setUnclickable(stack, true);
    ItemUtil.setUndroppable(stack, true);

    return stack;
  }

}
