package mods.hinasch.unsaga.core.inventory;

import mods.hinasch.lib.capability.ISyncCapability;
import mods.hinasch.lib.iface.IRequireInitializing;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface IAccessoryInventory extends IRequireInitializing,ISyncCapability{




	public void setStacks(NonNullList<ItemStack> list);
	public NonNullList<ItemStack> getEquippedList();
	public boolean hasEmptySlot();

}
