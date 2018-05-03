package mods.hinasch.unsaga.common.specialaction;

import java.util.function.BiFunction;

import javax.annotation.Nullable;

import mods.hinasch.unsaga.common.specialaction.ActionBase.IAction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.SoundEvent;

public class ActionProjectile<T extends IActionPerformer> implements IAction<T>,IProjectileAction<T,ActionProjectile>{

	ProjectileFunction<T> func;
	SoundEvent shootSound;

	@Override
	public EnumActionResult apply(T context) {
		// TODO 自動生成されたメソッド・スタブ
		return this.shoot(context, null);
	}

	@Override
	public ProjectileFunction<T> getProjectileFunction() {
		// TODO 自動生成されたメソッド・スタブ
		return this.func;
	}



	@Override
	public SoundEvent getShootSound() {
		// TODO 自動生成されたメソッド・スタブ
		return shootSound;
	}

	@Override
	public ActionProjectile setProjectileFunction(ProjectileFunction<T> func) {
		this.func = func;
		return this;
	}

	@Override
	public ActionProjectile setShootSound(SoundEvent ev) {
		this.shootSound = ev;
		return this;
	}

	public static interface ProjectileFunction<J extends IActionPerformer> extends BiFunction<J,EntityLivingBase,Entity>{

		@Override
		public @Nullable Entity apply(J t, EntityLivingBase u);

	}

	public static interface IProjectileTech<T>{

		public void init(T invoker,EntityLivingBase target);
	}
}
