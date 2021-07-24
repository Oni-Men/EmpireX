package onim.en.empirex.item.entity;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import onim.en.empirex.item.CustomItem;
import onim.en.empirex.item.interfaces.Fishable;

public class HookShot extends CustomItem implements Fishable {

  public HookShot() {
    super("hook_shot", "Hookshot", Arrays.asList("Hook and Pull. That's All"));
  }

  @Override
  public ItemStack getItemStack() {
    ItemStack stack = new ItemStack(Material.FISHING_ROD);
    ItemMeta meta = stack.getItemMeta();
    meta.addEnchant(Enchantment.DURABILITY, 10, true);
    meta.setUnbreakable(true);
    stack.setItemMeta(meta);
    return stack;
  }

  @Override
  public boolean onFish(PlayerFishEvent event) {
    Player player = event.getPlayer();
    FishHook hook = event.getHook();

    ItemStack stack = player.getInventory()
                            .getItemInMainHand();
    if (!this.isValidItem(stack))
      return false;

    switch (event.getState()) {
      case FISHING:
        hook.setVelocity(hook.getVelocity()
                             .multiply(1.4D));
      case BITE:
      case CAUGHT_FISH:
      case REEL_IN:
        break;
      case IN_GROUND:
      case CAUGHT_ENTITY:
        Vector loc2VecPlayer = player.getLocation()
                                     .toVector();
        Vector loc2VecHook = hook.getLocation()
                                 .toVector();
        Vector multiply = loc2VecHook.subtract(loc2VecPlayer)
                                     .multiply(0.20D);
        multiply.add(new Vector(0, 0.5, 0));
        player.setVelocity(multiply);
        break;
      default:
        break;
    }
    return true;
  }
}
