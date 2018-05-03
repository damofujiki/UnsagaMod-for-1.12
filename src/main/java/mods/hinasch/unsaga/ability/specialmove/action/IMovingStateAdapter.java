package mods.hinasch.unsaga.ability.specialmove.action;

import java.util.List;

import mods.hinasch.unsaga.ability.specialmove.TechInvoker;
import mods.hinasch.unsaga.core.potion.UnsagaPotions;
import mods.hinasch.unsaga.util.LivingHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public interface IMovingStateAdapter{

		public TechInvoker getInvoker();
		public EntityLivingBase getOwner();
		public World getWorld();
		default int getDuration(){
			return LivingHelper.ADAPTER.getCapability(getOwner()).isStateActive(UnsagaPotions.MOVING_STATE) ? LivingHelper.ADAPTER.getCapability(getOwner()).getState(UnsagaPotions.MOVING_STATE).getDuration() : 0;
		}
		default void setCancelHurt(){
			if(!LivingHelper.isStateActive(getOwner(),UnsagaPotions.CANCEL_HURT))LivingHelper.getCapability(getOwner()).addState(new PotionEffect(UnsagaPotions.CANCEL_HURT,this.getDuration(),0,false,false));
		};
		default void setCancelFall(int additional){
			if(!LivingHelper.isStateActive(getOwner(),UnsagaPotions.CANCEL_FALL))LivingHelper.getCapability(getOwner()).addState(new PotionEffect(UnsagaPotions.CANCEL_FALL,this.getDuration()+additional,0,false,false));
		};
		default void expireCancelHurt(){
			LivingHelper.getCapability(getOwner()).removeState(UnsagaPotions.CANCEL_HURT);
//			UnsagaPotions.addRemoveQueue(getOwner(), UnsagaPotions.CANCEL_HURT);
		};
		default void expireCancelFall(){
			LivingHelper.getCapability(getOwner()).removeState(UnsagaPotions.CANCEL_FALL);
//			UnsagaPotions.addRemoveQueue(getOwner(), UnsagaPotions.CANCEL_FALL);
		};
		default List<EntityLivingBase> getCollisionEnemies(){
			return this.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, this.getBoundingBox(1.0D),in->this.getOwner()!=in && IMob.MOB_SELECTOR.apply(in));
		};
		default AxisAlignedBB getBoundingBox(double grow){
			return this.getOwner().getEntityBoundingBox().grow(grow);
		};
		default void expire(){
			this.expireCancelFall();
			this.expireCancelHurt();
			LivingHelper.ADAPTER.getCapability(getOwner()).removeState(UnsagaPotions.MOVING_STATE);
		};

	}