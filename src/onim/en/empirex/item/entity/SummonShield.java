package onim.en.empirex.item.entity;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.stream.IntStream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ArmorStand.LockType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import net.minecraft.util.MathHelper;
import onim.en.empirex.EmpireX;
import onim.en.empirex.item.CustomItem;
import onim.en.empirex.item.interfaces.Interactable;

public class SummonShield extends CustomItem implements Interactable {

  public SummonShield() {
    super("summon_shield", "Summon Shield", Arrays.asList("Summon shields around you"));
  }

  @Override
  public ItemStack getItemStack() {
    ItemStack stack = new ItemStack(Material.SHIELD);
    stack.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
    return stack;
  }

  @Override
  public int getCooltime() {
    return 10;
  }

  @Override
  public boolean onInteract(PlayerInteractEvent event) {
    ItemStack stack = event.getPlayer().getInventory().getItemInMainHand();

    if (!this.isValidItem(stack))
      return false;

    if (this.isCooldown(stack)) {
      return false;
    }

    switch (event.getAction()) {
      case RIGHT_CLICK_AIR:
      case RIGHT_CLICK_BLOCK:
        this.summonShields(event.getPlayer());
        this.setCooldown(stack);
        break;
      default:
        return false;
    }
    return true;
  }

  private void summonShields(Player player) {
    World world = player.getWorld();

    IntStream.range(0, 6).mapToObj(i -> new AbstractMap.SimpleEntry<>(i,
        world.spawn(player.getLocation(), ArmorStand.class, armorStand -> {
          armorStand.setVisible(false);
          armorStand.setInvulnerable(true);
          armorStand.addEquipmentLock(EquipmentSlot.HAND, LockType.REMOVING_OR_CHANGING);
          armorStand.getEquipment().setItemInMainHand(new ItemStack(Material.SHIELD));
          armorStand.setRightArmPose(new EulerAngle(300, 0, 0));
          armorStand.teleport(this.calculate(player.getLocation(), i, 0));
        }))).forEach(e -> {
          final ArmorStand armorStand = e.getValue();
          Bukkit.getScheduler().runTaskTimer(EmpireX.instance, task -> {
            if (armorStand.getTicksLived() > 200) {
              armorStand.remove();
              task.cancel();
            }

            armorStand.setVelocity(player.getVelocity());
            armorStand.teleport(
                this.calculate(player.getLocation(), e.getKey(), armorStand.getTicksLived()));
          }, 0L, 1L);
        });
  }

  private Location calculate(Location origin, int i, int rotation) {
    float radians = (float) Math.toRadians(i * 60 + rotation);
    float x = MathHelper.cos(radians);
    float z = MathHelper.sin(radians);
    Location location = origin.clone().add(x, 0, z);
    location.setYaw(180 + i * 60 + rotation);

    return location;
  }
}
