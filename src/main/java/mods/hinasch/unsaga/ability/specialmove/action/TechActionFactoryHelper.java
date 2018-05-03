package mods.hinasch.unsaga.ability.specialmove.action;

import java.util.function.Function;

import org.apache.commons.lang3.tuple.Triple;

import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.particle.ParticleHelper.MovingType;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.ability.specialmove.TechInvoker;
import mods.hinasch.unsaga.ability.specialmove.TechInvoker.InvokeType;
import mods.hinasch.unsaga.common.specialaction.ActionBase.IAction;
import mods.hinasch.unsaga.common.specialaction.ActionCharged;
import mods.hinasch.unsaga.common.specialaction.ActionMelee;
import mods.hinasch.unsaga.common.specialaction.ActionProjectile;
import mods.hinasch.unsaga.common.specialaction.ActionProjectile.ProjectileFunction;
import mods.hinasch.unsaga.common.specialaction.ActionRequireJump;
import mods.hinasch.unsaga.core.entity.mob.EntityRuffleTree;
import mods.hinasch.unsaga.core.potion.StateCombo;
import mods.hinasch.unsaga.core.potion.UnsagaPotions;
import mods.hinasch.unsaga.damage.DamageComponent;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.util.LivingHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;

public class TechActionFactoryHelper {
	public static TechActionBase createProjectile(ProjectileFunction<TechInvoker> func,int chargeTime){
		TechActionBase actionBase = TechActionBase.create(InvokeType.CHARGE);
		ActionProjectile<TechInvoker> projectile = new ActionProjectile<TechInvoker>()
				.setProjectileFunction(func)
				.setShootSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP);
		actionBase.addAction(new ActionCharged(projectile).setChargeThreshold(chargeTime));
		return actionBase;
	}

	/** DamageDelegate用*/
	public static Function<Triple<TechInvoker,EntityLivingBase,DamageComponent>,DamageComponent> getWoodPlantSlayer(){
		return in ->in.getMiddle().getCreatureAttribute()==EntityRuffleTree.PLANT ? DamageComponent.of(in.getRight().hp() * 1.5F, in.getRight().lp()) : in.getRight();
	}

	public static TechActionBase createMultiAttack(int count,EnumParticleTypes particle,boolean cancelHurtResistance,boolean requireJump,General... types){
		TechActionBase base = new TechActionBase(InvokeType.RIGHTCLICK);

		IAction<TechInvoker> action = new ActionMelee(types)
				.setAdditionalBehavior((context,target)->{
					context.playSound(XYZPos.createFrom(target), SoundEvents.ENTITY_IRONGOLEM_HURT, false,0.5F);
					context.spawnParticle(MovingType.FOUNTAIN, new XYZPos(target.getPosition().up()), particle, 15, 0.1D);
					LivingHelper.getCapability(context.getPerformer()).addState(new StateCombo.Effect(0,count-2,context.getStrength(),particle,context.getArtifact().isPresent() ? context.getArtifact().get() : null));
					if(cancelHurtResistance){
						LivingHelper.getCapability(target).addState(new PotionEffect(UnsagaPotions.HURT,ItemUtil.getPotionTime(2),0));
					}

				})
				.setDamageDelegate(in ->{
					float at = (float) in.getLeft().getPerformer().getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
					return DamageComponent.of(at, in.getRight().lp());
				});

		if(requireJump){
			base.addAction(new ActionRequireJump(action));
		}else{
			base.addAction(action);
		}
		return base;
	}
}
