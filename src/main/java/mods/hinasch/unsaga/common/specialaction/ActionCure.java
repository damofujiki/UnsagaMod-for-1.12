package mods.hinasch.unsaga.common.specialaction;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;

import mods.hinasch.lib.particle.ParticleHelper.MovingType;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.common.specialaction.ActionBase.IAction;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumParticleTypes;

public class ActionCure <T extends IActionPerformer> implements IAction<T>{

	final ImmutableSet<Potion> curableEffects;
	boolean cureAllBadEffects = false;

	public ActionCure(Potion... curable){
		if(curable.length<=0){
			this.curableEffects = ImmutableSet.of();
			this.setCureAllBadEffects(true);
		}else{
			this.curableEffects = ImmutableSet.copyOf(curable);
		}

	}
	public ActionCure(Collection<Potion> potions){
		this.curableEffects = ImmutableSet.copyOf(potions);
	}
	public ActionCure setCureAllBadEffects(boolean par1){
		this.cureAllBadEffects = par1;
		return this;
	}
	@Override
	public EnumActionResult apply(T context) {
		Optional<EntityLivingBase> target = context.getTarget();
		if(target.isPresent()){
			context.spawnParticle(MovingType.FLOATING, XYZPos.createFrom(target.get())
					, EnumParticleTypes.VILLAGER_HAPPY, 10, 0.02D);
			if(!this.curableEffects.isEmpty()){
				for(Potion potion:this.curableEffects){
					target.get().removePotionEffect(potion);
				}
			}
			if(this.cureAllBadEffects){
				for(Iterator<PotionEffect> ite=target.get().getActivePotionEffects().iterator();ite.hasNext();){
					PotionEffect effect = ite.next();
					if(effect.getPotion().isBadEffect()){
						target.get().removePotionEffect(effect.getPotion());
					}
				}
			}


			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}

}
