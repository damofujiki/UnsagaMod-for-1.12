package mods.hinasch.unsaga.common.specialaction;

import mods.hinasch.unsaga.ability.specialmove.TechInvoker;
import net.minecraft.entity.EntityLivingBase;

public interface IActionMultipleMelee{

	public int getMultipleMeleeNumber();
	public void attack(EntityLivingBase ev,TechInvoker invoker);
	public int getMultipleAttackAllowingTime();
}