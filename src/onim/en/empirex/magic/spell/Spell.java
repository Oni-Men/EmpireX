package onim.en.empirex.magic.spell;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.minecraft.nbt.NBTTagCompound;
import onim.en.empirex.EmpireX;
import onim.en.empirex.base.Identifiable;

public interface Spell extends Identifiable {

  public static final String SPELL_ID = "Id";
  public static final String SPELL_Name = "Name";
  public static final String SPELL_AVAILABLE = "Available";

  public String id();

  public String getName();

  public String getDescription();

  public int getCost();

  /**
   *
   * @return tick of cooltime for this spell
   */
  public int getCooltime();

  /**
   *
   * @param player
   * @return 詠唱に成功したらTrueを返す
   */
  public boolean activate(Player player);

  public default ItemStack toItemStack() {
    return this.toItemStack(false);
  }

  public ItemStack toItemStack(boolean available);

  public default NBTTagCompound toNBTTagCompound(boolean available) {
    NBTTagCompound compound = new NBTTagCompound();
    compound.setString(SPELL_ID, this.id());
    compound.setString(SPELL_Name, this.getName());
    compound.setBoolean(SPELL_AVAILABLE, available);

    return compound;
  }

  public static Spell fromNBTTagCompound(NBTTagCompound compound) {
    if (compound == null)
      return null;

    String spellId = compound.getString(SPELL_ID);

    if (spellId.isEmpty())
      return null;

    return EmpireX.spellFactory.get(spellId);
  }
}
