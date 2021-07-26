package onim.en.empirex.item.entity;

import java.util.Arrays;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.base.Preconditions;

import onim.en.empirex.EmpireX;
import onim.en.empirex.item.CustomItem;
import onim.en.empirex.item.ItemUtil;
import onim.en.empirex.item.interfaces.Shootable;
import onim.en.empirex.util.EntityUtil;
import onim.en.empirex.util.KeyFactory;

public class RapidFire extends CustomItem implements Shootable {

  public RapidFire() {
    super("rapid_fire", "Rapid Fire", Arrays.asList("Fully charged arrows become Rapid Fire"));
  }

  @Override
  public Material getMaterial() {
    return Material.BOW;
  }

  @Override
  public boolean isGlowing() {
    return true;
  }

  @Override
  public boolean onShoot(ProjectileLaunchEvent event) {
    try {
      Preconditions.checkState(event.getEntity() instanceof Arrow);

      Arrow arrowEntity = (Arrow) event.getEntity();

      Preconditions.checkState(arrowEntity.getShooter() instanceof Player);
      Preconditions.checkState(!arrowEntity.isShotFromCrossbow());
      Preconditions.checkState(arrowEntity.isCritical());

      NamespacedKey key_rapid_fire_arrow = KeyFactory.get("rapid_fire_arrow");

      Preconditions
        .checkState(!arrowEntity.getPersistentDataContainer().has(key_rapid_fire_arrow, PersistentDataType.BYTE));

      ItemStack stack = null;
      Player player = (Player) arrowEntity.getShooter();
      PlayerInventory inventory = player.getInventory();

      if (ItemUtil.getPluginItemId(inventory.getItemInMainHand()).contentEquals(this.id())) {
        stack = inventory.getItemInMainHand();
      } else if (ItemUtil.getPluginItemId(inventory.getItemInOffHand()).contentEquals(this.id())) {
        stack = inventory.getItemInOffHand();
      }

      Preconditions.checkNotNull(stack);
      Preconditions.checkState(stack.getType() == Material.BOW);

      new BukkitRunnable() {
        int count = 0;

        @Override
        public void run() {
          if (!decrementArrow(player)) {
            this.cancel();
            return;
          }

          Arrow arrow = player.launchProjectile(Arrow.class);
          arrow.setCritical(true);
          arrow.getPersistentDataContainer().set(key_rapid_fire_arrow, PersistentDataType.BYTE, (byte) count);

          EntityUtil.makeEntityNoImmunity(arrow);

          player.playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1f, 1f);
          count++;
          if (count > 5) {
            this.cancel();
            return;
          }
        }

        private boolean decrementArrow(Player player) {
          if (player.getGameMode() == GameMode.CREATIVE)
            return true;

          PlayerInventory inv = player.getInventory();
          int first = inv.first(Material.ARROW);

          if (first == -1)
            return false;

          ItemStack item = inv.getItem(first);

          if (item.getAmount() == 1) {
            inv.setItem(first, null);
          } else {
            item.setAmount(item.getAmount() - 1);
          }

          return true;
        }

      }.runTaskTimer(EmpireX.instance, 0, 2L);

      return true;
    } catch (Exception e) {
      return false;
    }
  }

}
