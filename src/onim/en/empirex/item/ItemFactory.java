package onim.en.empirex.item;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.inventory.ItemStack;

import com.google.common.base.Strings;

import onim.en.empirex.base.FactoryBase;
import onim.en.empirex.item.entity.HookShot;
import onim.en.empirex.item.entity.HunterCrossbow;
import onim.en.empirex.item.entity.HyperKineticPositionReverser;
import onim.en.empirex.item.entity.Katana;
import onim.en.empirex.item.entity.RapidFire;
import onim.en.empirex.item.entity.RecallCrystal;
import onim.en.empirex.item.entity.SummonShield;
import onim.en.empirex.item.entity.wand.BoneWand;
import onim.en.empirex.item.entity.wand.BronzeWand;
import onim.en.empirex.item.entity.wand.GoldenWand;

public class ItemFactory extends FactoryBase<CustomItem> {

  public CustomItem toCustomItem(ItemStack stack) {
    if (stack == null)
      return null;

    String itemId = ItemUtil.getPluginItemId(stack);

    if (Strings.isNullOrEmpty(itemId))
      return null;

    return get(itemId);
  }

  public List<ItemStack> toStackList() {
    return all().stream().map(i -> i.createItem()).collect(Collectors.toList());
  }

  @Override
  protected void init() {
    set(new HookShot());
    set(new HunterCrossbow());
    set(new BronzeWand());
    set(new BoneWand());
    set(new GoldenWand());
    set(new RapidFire());
    set(new SummonShield());
    set(new Katana());
    set(new RecallCrystal());
    set(new HyperKineticPositionReverser());
  }

  @Override
  protected CustomItem value(String key) {
    return null;
  }

}
