package mods.hinasch.unsaga.common.tool;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public abstract class ComponentDisplayInfo implements  IComponentDisplayInfo<ComponentDisplayInfo>{

	IPredicateDisplayInfo predicate;
	int priority;


	public ComponentDisplayInfo(int priority,IPredicateDisplayInfo predicate){

		this.predicate =predicate;
		this.priority = priority;

	}
	@Override
	public boolean predicate(ItemStack is, EntityPlayer ep, List dispList, boolean par4) {
		return this.predicate.predicate(is, ep, dispList, par4);
	}
	@Override
	public int compareTo(IComponentDisplayInfo o) {
		if(o instanceof ComponentDisplayInfo){
			return Integer.valueOf(priority).compareTo(((ComponentDisplayInfo)o).priority);
		}
		return o.compareTo(this);
	}


}