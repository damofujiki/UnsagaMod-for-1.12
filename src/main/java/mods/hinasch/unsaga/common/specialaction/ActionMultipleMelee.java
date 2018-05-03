package mods.hinasch.unsaga.common.specialaction;

import javax.annotation.Nonnull;

import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.ability.specialmove.TechInvoker;
import mods.hinasch.unsaga.core.potion.UnsagaPotions;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.damage.DamageComponent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public abstract class ActionMultipleMelee extends ActionMelee implements IActionMultipleMelee{

	public ActionMultipleMelee(@Nonnull General... attributes) {
		super(attributes);
		this.setAdditionalBehavior((inveoker,target)->{
			Potion allowMultipleAttack = UnsagaPotions.instance().ALLOW_MULTIPLE_ATTACK;
			if(target.isPotionActive(allowMultipleAttack)){


				target.setVelocity(0,0,0);
				target.addVelocity(0, 1, 0);
				inveoker.getPerformer().addVelocity(0, 1, 0);
				int attackNum = target.getActivePotionEffect(allowMultipleAttack).getAmplifier();



				attackNum --;
				target.removePotionEffect(allowMultipleAttack);
				if(attackNum>0){
					inveoker.playSound(XYZPos.createFrom(target), SoundEvents.ENTITY_IRONGOLEM_HURT, false);
					target.addPotionEffect(new PotionEffect(allowMultipleAttack,ItemUtil.getPotionTime(2),attackNum));
				}else{
					inveoker.playSound(XYZPos.createFrom(target), SoundEvents.ENTITY_GENERIC_EXPLODE, false);
				}

			}else{
				target.setVelocity(0,0,0);
				target.addVelocity(0, 1, 0);
				inveoker.getPerformer().addVelocity(0, 1, 0);
				target.addPotionEffect(new PotionEffect(allowMultipleAttack,ItemUtil.getPotionTime(2),3));
			}
//			if(EntityStateCapability.adapter.hasCapability(target)){
//				target.setVelocity(0,0,0);
//				target.addVelocity(0, 1, 0);
//				inveoker.getPerformer().addVelocity(0, 1, 0);
//
//				StateSpecialMove state = (StateSpecialMove) EntityStateCapability.adapter.getCapability(target).getState(StateRegistry.instance().stateSpecialMove);
//				state.setMultipleAttackComponent(new MultipleAttackComponent(inveoker, this.getMultipleAttackAllowingTime(), this.getMultipleMeleeNumber()));
//			}
		});
	}

	@Override
	public DamageComponent getDamage(TechInvoker context, EntityLivingBase target, DamageComponent base) {
		if(target.isPotionActive(UnsagaPotions.instance().ALLOW_MULTIPLE_ATTACK)){
			int num = target.getActivePotionEffect(UnsagaPotions.instance().ALLOW_MULTIPLE_ATTACK).getAmplifier();
			if(num==1){
				return DamageComponent.of(base.hp()+2.0F, base.lp());
			}
		}
		return base;
	}
	public static class MultipleAttackComponent{

		final TechInvoker invoker;
		final int allowedComboTimeMax;
		int allowedComboTime = 0;
		int attackNumber = 0;
		public MultipleAttackComponent(TechInvoker invoker, int allowingTime, int attackNumber) {
			super();
			this.invoker = invoker;
			this.allowedComboTimeMax = allowingTime;
			this.allowedComboTime = allowingTime;
			this.attackNumber = attackNumber;
		}

		public TechInvoker getInvoker(){
			return this.invoker;

		}

		public boolean isMultipleAttackAllowing(){
			return this.getAllowingTime()>0 && this.getAttackNumber()>0;
		}
		public int getAllowingTime(){
			return this.allowedComboTime;
		}

		public int getAttackNumber(){
			return this.attackNumber;
		}

		public void resetComboTime(){
			this.allowedComboTime = this.allowedComboTimeMax;
		}
		public void reduceComboTime(){
			if(this.allowedComboTime>0){
				this.allowedComboTime --;
				UnsagaMod.logger.trace("combo", this.allowedComboTime);
			}

		}

		public void reduceAttackNumber(){
			if(this.attackNumber>0){
				this.attackNumber --;
			}
		}
	}
}
