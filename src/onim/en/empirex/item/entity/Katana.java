package onim.en.empirex.item.entity;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import onim.en.empirex.EmpireX;
import onim.en.empirex.item.CustomItem;
import onim.en.empirex.item.interfaces.LeftClickable;

public class Katana extends CustomItem implements LeftClickable {

  public Katana() {
    super("katana", "Katana", Arrays.asList("Tuyoi ken"));
  }

  @Override
  public ItemStack getItemStack() {
    ItemStack stack = new ItemStack(Material.IRON_SWORD);
    stack.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
    ItemMeta meta = stack.getItemMeta();
    meta.setUnbreakable(true);
    stack.setItemMeta(meta);

    return stack;
  }

  @Override
  public int getCooltime() {
    return 0;
  }

  @Override
  public void onLeftClick(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    ItemStack stack = player.getInventory().getItemInMainHand();

    if (!this.isValidItem(stack))
      return;

    if (player.getAttackCooldown() != 1.0f)
      return;
      
    World world = player.getWorld();
    Location location = player.getLocation().add(0, 0.5, 0);

    world.playSound(location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);

    IntStream.range(0, 5).forEach(i -> {
      Bukkit.getScheduler().runTaskLater(EmpireX.instance, task -> {
        Location add = location.clone().add(location.getDirection().multiply(i * 2 + 1));
        world.getNearbyEntities(add, 2, 2, 2).stream()
            .filter(e -> !e.equals(player))
            .filter(e -> e instanceof LivingEntity)
            .map(e -> (LivingEntity) e)
            .forEach(l -> l.damage(5, player));

        world.spawnParticle(Particle.SWEEP_ATTACK, add, 1);
      }, i);
    });

  }

}
