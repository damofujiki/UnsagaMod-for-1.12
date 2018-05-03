package mods.hinasch.unsaga.common.tool;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IPredicateDisplayInfo{

	public boolean predicate(ItemStack is, EntityPlayer ep, List dispList, boolean par4) ;
}