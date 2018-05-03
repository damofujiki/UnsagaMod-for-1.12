package mods.hinasch.unsaga.common.specialaction;

import java.util.EnumSet;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Triple;

import com.google.common.collect.Lists;

import mods.hinasch.unsaga.ability.specialmove.TechInvoker;
import mods.hinasch.unsaga.common.specialaction.ActionBase.IAction;
import mods.hinasch.unsaga.damage.DamageComponent;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.Sub;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumActionResult;

public class ActionMelee implements IAction<TechInvoker>,ISimpleMelee<TechInvoker>{


	Function<Triple<TechInvoker,EntityLivingBase,DamageComponent>,DamageComponent> damageDelegate = in -> in.getRight();
	BiConsumer<TechInvoker,EntityLivingBase> consumer;
	EnumSet<General> attributes;
	EnumSet<Sub> subAttributes = EnumSet.noneOf(Sub.class);
	float knockbackStrength = 0;

	public ActionMelee(@Nonnull General... attributes) {
		this.attributes = EnumSet.copyOf(Lists.newArrayList(attributes));
	}

	public ActionMelee setSubAttributes(EnumSet<Sub> sub){
		this.subAttributes = sub;
		return this;
	}
	public ActionMelee setAdditionalBehavior(BiConsumer<TechInvoker,EntityLivingBase> consumer){
		this.consumer = consumer;
		return this;
	}

	@Override
	public EnumActionResult apply(TechInvoker t) {
		// TODO 自動生成されたメソッド・スタブ
		return this.performSimpleAttack(t);
	}

	public ActionMelee setKnockbackStrength(float knockback){
		this.knockbackStrength = knockback;
		return this;
	}
	@Override
	public EnumSet<General> getAttributes() {
		// TODO 自動生成されたメソッド・スタブ
		return attributes;
	}

	@Override
	public EnumSet<Sub> getSubAttributes() {
		// TODO 自動生成されたメソッド・スタブ
		return subAttributes;
	}



	@Override
	public BiConsumer<TechInvoker, EntityLivingBase> getAdditionalBehavior() {
		// TODO 自動生成されたメソッド・スタブ
		return consumer;
	}

	@Override
	public DamageComponent getDamage(TechInvoker context,EntityLivingBase target, DamageComponent base) {
		// TODO 自動生成されたメソッド・スタブ
		return damageDelegate.apply(Triple.of(context, target, base));
	}

	@Override
	public float getReach(TechInvoker context) {
		// TODO 自動生成されたメソッド・スタブ
		return context.getReach();
	}

	@Override
	public float getKnockbackStrength() {
		// TODO 自動生成されたメソッド・スタブ
		return this.knockbackStrength;
	}

	public ActionMelee setDamageDelegate(Function<Triple<TechInvoker,EntityLivingBase,DamageComponent>,DamageComponent> damageDelegate){
		this.damageDelegate = damageDelegate;
		return this;
	}
//	@Override
//	public float getLPDamage(SpecialMoveInvoker context, EntityLivingBase target, float base) {
//		// TODO 自動生成されたメソッド・スタブ
//		return base;
//	}



}
