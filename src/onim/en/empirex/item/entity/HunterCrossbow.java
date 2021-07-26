package onim.en.empirex.item.entity;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.util.Vector;

import com.google.common.base.Preconditions;

import onim.en.empirex.item.CustomItem;
import onim.en.empirex.item.ItemUtil;
import onim.en.empirex.item.interfaces.Shootable;

public class HunterCrossbow extends CustomItem implements Shootable {

  public HunterCrossbow() {
    super("crossbow", "Hunter's Crossbow",
        Arrays.asList("The Arrow shot is very penetrative", "and goes straight throught."));
  }

  @Override
  public Material getMaterial() {
    return Material.CROSSBOW;
  }

  @Override
  public boolean isGlowing() {
    return true;
  }

  @Override
  public boolean onShoot(ProjectileLaunchEvent event) {
    try {
      Preconditions.checkState(event.getEntity() instanceof Arrow);
      Preconditions.checkState(event.getEntity().getShooter() instanceof Player);
    } catch (Exception e) {
      return false;
    }

    Arrow arrowEntity = (Arrow) event.getEntity();
    Player shooter = (Player) arrowEntity.getShooter();

    if (!arrowEntity.isShotFromCrossbow())
      return false;

    PlayerInventory inventory = shooter.getInventory();
    ItemStack stack = null;

    if (ItemUtil.getPluginItemId(inventory.getItemInMainHand()).contentEquals(this.id())) {
      stack = inventory.getItemInMainHand();
    } else if (ItemUtil.getPluginItemId(inventory.getItemInOffHand()).contentEquals(this.id())) {
      stack = inventory.getItemInOffHand();
    }

    if (stack == null)
      return false;

    if (!(stack.getItemMeta() instanceof CrossbowMeta))
      return false;

    Vector velocity = shooter.getLocation().getDirection();
    arrowEntity.setVelocity(velocity.multiply(arrowEntity.getVelocity().length() * 3D));
    arrowEntity.setGravity(false);
    arrowEntity.setKnockbackStrength(2);
    arrowEntity.setPierceLevel(5);
    return true;
  }

}
