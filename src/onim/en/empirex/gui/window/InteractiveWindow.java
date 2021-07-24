package onim.en.empirex.gui.window;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import onim.en.empirex.gui.WindowInterface;
import onim.en.empirex.gui.WindowRegistry;

public abstract class InteractiveWindow implements WindowInterface {

  @Override
  public boolean allowChange() {
    return false;
  }

  @Override
  public void onClick(InventoryClickEvent event) {
    event.setCancelled(!this.allowChange());
  }

  @Override
  public void onClose(InventoryCloseEvent event) {
    WindowRegistry.unsubscribe(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;

    if (!(obj instanceof WindowInterface))
      return false;

    WindowInterface window = (WindowInterface) obj;

    if (!window.getTitle().contentEquals(this.getTitle()))
      return false;

    if (!window.getId().contentEquals(this.getId()))
      return false;

    return true;
  }

  protected boolean isTopInventory(int rowSlot, int topInventorySize) {
    if (rowSlot < topInventorySize)
      return true;
    return false;
  }
}
