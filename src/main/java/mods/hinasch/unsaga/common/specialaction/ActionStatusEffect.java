package mods.hinasch.unsaga.common.specialaction;

import java.util.List;

import com.google.common.collect.ImmutableList;

import mods.hinasch.lib.particle.ParticleHelper.MovingType;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.common.specialaction.ActionBase.IAction;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumParticleTypes;

public abstract class ActionStatusEffect<T extends IActionPerformer> implements IAction<T>{

	protected final ImmutableList<Potion> buffs;
	boolean isDebuff = false;

	/**
	 *
	 * @param isDebuff デバフかどうか。デバフなら自分にかけられない
	 * @param potions
	 */
	public ActionStatusEffect(boolean isDebuff,Potion... potions){
		this.buffs = ImmutableList.copyOf(potions);
		this.isDebuff = isDebuff;
	}
	public ActionStatusEffect(boolean isDebuff,List<Potion> potions){
		this.buffs = ImmutableList.copyOf(potions);
		this.isDebuff = isDebuff;
	}

	public boolean isDebuff(){
		return this.isDebuff;
	}
	@Override
	public EnumActionResult apply(T context) {
//		UnsagaMod.logger.trace(this.getClass().getName(), this.buffs,context.getTarget(),this.decideEffectDuration(context),this.decideEffectLevel(context));
		if(!this.buffs.isEmpty()){
			if(context.getTarget().isPresent()){
				EntityLivingBase target = (EntityLivingBase) context.getTarget().get();
				EnumParticleTypes particle = this.isDebuff ? EnumParticleTypes.SPELL_WITCH : EnumParticleTypes.CRIT_MAGIC;
				context.spawnParticle(MovingType.FLOATING, XYZPos.createFrom(target), particle, 10, 0.02D);
				if(context.getTargetType()==IActionPerformer.TargetType.OWNER && this.isDebuff && target==context.getPerformer()){
					return EnumActionResult.PASS;
				}
//				UnsagaMod.logger.trace(this.getClass().getName(), "called!");
				for(Potion potion:this.buffs){
					target.addPotionEffect(new PotionEffect(potion,this.decideEffectDuration(context),this.decideEffectLevel(context)));
				}
				return EnumActionResult.SUCCESS;
			}
		}
		return EnumActionResult.PASS;
	}


	public abstract int decideEffectLevel(T context);

	public abstract int decideEffectDuration(T context);
//		int sec = ItemUtil.getPotionTime(context.getActionProperty().getDuration());
//		return sec;


}
