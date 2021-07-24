package onim.en.empirex.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public interface WindowInterface {

  public String getTitle();

  public String getId();

  public boolean allowChange();

  public Inventory getWindow();

  public void onClick(InventoryClickEvent event);

  public void onClose(InventoryCloseEvent event);

}
