package mods.hinasch.unsaga.common.specialaction;

import javax.annotation.Nullable;

import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.common.specialaction.ActionProjectile.ProjectileFunction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.SoundEvent;

public interface IProjectileAction<T extends IActionPerformer,V> {
	public ProjectileFunction<T> getProjectileFunction();

	/**
	 *
	 * @param func
	 * @return functionが返すentityは@Nullable
	 */
	public V setProjectileFunction(ProjectileFunction<T> func);
	public V setShootSound(SoundEvent ev);

	public default Entity getProjectile(T context,@Nullable EntityLivingBase target){
		if(this.getProjectileFunction() !=null){
			return this.getProjectileFunction().apply(context,target);
		}
		return null;
	}

	public SoundEvent getShootSound();

	public default EnumActionResult shoot(T context,@Nullable EntityLivingBase target) {
		Entity projectile = this.getProjectile(context,target);
		context.getPerformer().playSound(getShootSound(), 1.0F, 1.0F);
		if(projectile==null){
			return EnumActionResult.PASS;
		}

		UnsagaMod.logger.trace(this.getClass().getName(), WorldHelper.isServer(context.getWorld()));
		WorldHelper.safeSpawn(context.getWorld(), projectile);

		return EnumActionResult.SUCCESS;
	}
}
