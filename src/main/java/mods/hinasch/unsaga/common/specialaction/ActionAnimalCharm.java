package mods.hinasch.unsaga.common.specialaction;

import java.util.Optional;

import mods.hinasch.lib.particle.ParticleHelper.MovingType;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.common.specialaction.ActionBase.IAction;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumParticleTypes;

public class ActionAnimalCharm<T extends IActionPerformer> implements IAction<T>{

	@Override
	public EnumActionResult apply(T context) {
		Optional<EntityLivingBase> target = context.getTarget();
		if(target.isPresent() && context.getPerformer() instanceof EntityPlayer){


			context.spawnParticle(MovingType.FLOATING, XYZPos.createFrom(target.get())
					, EnumParticleTypes.HEART, 10, 0.02D);


			if(target.get() instanceof EntityTameable){
				EntityTameable tameable = (EntityTameable) target.get();
				if(!tameable.isTamed()){
					tameable.setTamed(true);
					tameable.setOwnerId(context.getPerformer().getUniqueID());
				}
				return EnumActionResult.SUCCESS;
			}

			if(target.get() instanceof EntityHorse){
				EntityHorse horse = (EntityHorse) target.get();
				if(!horse.isTame()){
					horse.setTamedBy((EntityPlayer) context.getPerformer());
					context.getWorld().setEntityState(horse, (byte)7);
					return EnumActionResult.SUCCESS;
				}
			}else{
				if(target.get() instanceof EntityAnimal){
					EntityAnimal animal = (EntityAnimal) target.get();
					animal.setInLove((EntityPlayer) context.getPerformer());
					return EnumActionResult.SUCCESS;
				}
			}
			if(target.get() instanceof EntityCreature){
				EntityCreature creature = (EntityCreature)target.get();
				creature.setRevengeTarget(null);
				return EnumActionResult.SUCCESS;
			}
		}

		return EnumActionResult.PASS;
	}

}
