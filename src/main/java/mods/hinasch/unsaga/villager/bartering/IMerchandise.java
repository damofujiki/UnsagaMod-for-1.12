package mods.hinasch.unsaga.villager.bartering;

import net.minecraft.item.ItemStack;

public interface IMerchandise {

	public void setPrice(ItemStack stack);
	public boolean canSell(ItemStack stack);
	public int getPrice(ItemStack stack);
	public boolean isMerchadise();
	public void setMerchandise(boolean par1);
}
