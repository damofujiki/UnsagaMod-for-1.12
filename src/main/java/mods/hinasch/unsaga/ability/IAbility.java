package mods.hinasch.unsaga.ability;

import mods.hinasch.lib.iface.INBTWritable;
import mods.hinasch.lib.registry.IPropertyElement;
import net.minecraft.nbt.NBTTagCompound;

public interface IAbility extends INBTWritable,IPropertyElement{

	public String getUnlocalizedName();
	public void writeToNBT(NBTTagCompound stream);
	public Class getParentClass();
	public String getLocalized();
	/** 同時に覚えられるかどうか。同じ数字は覚えられるものから除外する。0で除外しない。*/
	public int getExclusionNumber();

	default boolean isAbilityEmpty(){
		return this==AbilityRegistry.EMPTY ? true : false;
	}
}
