package mods.hinasch.unsaga.skillpanel;

import java.util.Map;
import java.util.UUID;

import mods.hinasch.lib.capability.ISyncCapability;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface ISkillPanelSaver extends ISyncCapability{
	public NonNullList<ItemStack> getPanels(UUID uuid);
	public void setPanels(UUID uuid,NonNullList<ItemStack> list);
	public Map<UUID,NonNullList<ItemStack>> getRawPanelMap();
	public void dumpData();
	public void clearData();
}
