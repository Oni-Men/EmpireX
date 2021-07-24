package onim.en.empirex.item.entity;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import onim.en.empirex.item.CustomItem;

public class RecallCrystal extends CustomItem{

	public RecallCrystal() {
		super("recall_crystal", "Recall Crystal");
	}

	@Override
	public ItemStack getItemStack() {
		ItemStack stack = new ItemStack(Material.AMETHYST_SHARD);
		return stack;
	}




}
