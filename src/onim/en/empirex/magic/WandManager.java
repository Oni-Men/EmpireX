package onim.en.empirex.magic;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Maps;

import onim.en.empirex.magic.impl.ItemWand;

public class WandManager {
  private static HashMap<ItemStack, Wand> wands = Maps.newHashMap();

  public static Wand addWand(ItemStack stack) {
    ItemWand wand = new ItemWand(stack);
  
    wands.put(stack, wand);
    
    return wand;
  }

  public static Wand getWand(ItemStack stack) {
    if (wands.containsKey(stack)) {
      return wands.get(stack);
    }
    return addWand(stack);
  }
}
