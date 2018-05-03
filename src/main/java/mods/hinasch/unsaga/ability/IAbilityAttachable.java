package mods.hinasch.unsaga.ability;

import mods.hinasch.lib.capability.ISyncCapability;
import mods.hinasch.lib.iface.IRequireInitializing;
import net.minecraft.util.NonNullList;

public interface IAbilityAttachable extends IRequireInitializing,ISyncCapability{

	public int getMaxAbilitySize();
	public void setMaxAbilitySize(int size);
	public NonNullList<IAbility> getLearnedAbilities();
	public void setAbility(int index,IAbility ab);
	public void setAbilities(NonNullList<IAbility> list);
	public void removeAbility(IAbility ab);
	public boolean hasAbility(IAbility ab);
	public boolean isAbilityEmpty();
	public void clearAbility(int size);
	@Deprecated
	public void addAbility(IAbility ab);
	public boolean isUniqueItem();
	public NonNullList<IAbility> getLearnableUniqueAbilities();
	public void setLearnableUniqueAbilities(NonNullList<IAbility> list);
	public boolean isAbilityFull();
}
