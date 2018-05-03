package mods.hinasch.unsaga.common.specialaction;

import java.util.EnumSet;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import mods.hinasch.lib.entity.RangedHelper;
import mods.hinasch.lib.entity.RangedHelper.RangedSelector;
import mods.hinasch.unsaga.ability.specialmove.TechInvoker;
import mods.hinasch.unsaga.common.specialaction.ActionBase.IAction;
import mods.hinasch.unsaga.damage.DamageSourceUnsaga;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.Sub;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class ActionRangedAttack<T extends IActionPerformer> implements IAction<T>{

	boolean isAttack = true;
	Function<T,List<AxisAlignedBB>> boundingBoxMaker;
	RangedSelector<T,EntityLivingBase> entitySelector = (self,target)->true;
	SubConsumer<T> subConsumer = (self,target)->{};
	ActionStatusEffect<T> debuffSetter;
	final EnumSet<General> damageType;
	EnumSet<Sub> subDamageType = EnumSet.noneOf(Sub.class);

	public ActionRangedAttack(@Nullable General... damageType) {
		if(damageType==null || damageType.length<=0){
			this.damageType = EnumSet.copyOf(Lists.newArrayList(General.PUNCH));
		}else{
			this.damageType = EnumSet.copyOf(Lists.newArrayList(damageType));
		}

		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public EnumActionResult apply(T context) {
		BiConsumer<RangedHelper<T>,EntityLivingBase> attackConsumer = (self,target)->{
			if(this.isAttack){
				DamageSourceUnsaga ds = DamageSourceUnsaga.create(context.getPerformer(), context.getStrength().lp().amount(), this.damageType);
				if(!this.subDamageType.isEmpty()){
					ds.setSubTypes(subDamageType);
				}
				target.attackEntityFrom(ds, context.getStrength().hp());
				self.setFlag(true);
			}
			if(this.debuffSetter!=null){
//				UnsagaMod.logger.trace(this.getClass().getName(), "called");
				context.setTarget(target);
				EnumActionResult result = this.debuffSetter.apply(context);
				self.setFlag(result ==EnumActionResult.SUCCESS ? true : false);
			}
			this.subConsumer.accept(self, target);
		};
//		context.swingMainHand(true, isRenderSweepParticle);
		boolean flag = RangedHelper.<T>create(context.getWorld(), context.getPerformer(), boundingBoxMaker.apply(context))
		.setSelector(entitySelector).setParent(context).setConsumer(attackConsumer).invoke();

		return flag ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
	}

	public ActionRangedAttack<T> setAttackFlag(boolean par1){
		this.isAttack = par1;
		return this;
	}

	public ActionRangedAttack<T> setSubBehavior(SubConsumer<T> selector){
		this.subConsumer = selector;
		return this;
	}

	public ActionRangedAttack<T> setSubDamageType(Sub... subs){
		this.subDamageType = EnumSet.copyOf(Lists.newArrayList(subs));
		return this;
	}
	public ActionRangedAttack<T> setEntitySelector(RangedSelector<T,EntityLivingBase> selector){
		this.entitySelector = selector;
		return this;
	}
	public ActionRangedAttack<T> setBoundingBoxFactory(Function<T,List<AxisAlignedBB>> boundingBoxMaker){
		this.boundingBoxMaker = boundingBoxMaker;
		return this;
	}

	public ActionRangedAttack<T> setDebuffSetter(ActionStatusEffect<T> action){
		this.debuffSetter = action;
		return this;
	}
	public static class RangeSurroundings<V extends IActionPerformer> implements Function<V,List<AxisAlignedBB>>{

		final double horizontal;
		final double vertical;
		public RangeSurroundings(double horizontal,double vertical){
			this.horizontal = horizontal;
			this.vertical = vertical;
		}
		@Override
		public List<AxisAlignedBB> apply(V t) {
			List<AxisAlignedBB> list = Lists.newArrayList();
			list.add(t.getPerformer().getEntityBoundingBox().expand(horizontal,vertical,horizontal));
			return list;
		}

	}

	public static class RangeSwing implements Function<TechInvoker,List<AxisAlignedBB>>{

		/** 多くするほど当たり判定が細かい*/
		final int resolution;
		public RangeSwing(int resolution){
			this.resolution = resolution;
		}
		@Override
		public List<AxisAlignedBB> apply(TechInvoker context) {
			List<AxisAlignedBB> list = Lists.newArrayList();
			EntityLivingBase el = context.getPerformer();
			int rotatePar = 180 / this.resolution;
			for(int i=0;i<(int)context.getReach();i++){
				Vec3d v1 = el.getLookVec().normalize().scale(i+1);

				for(int r=0;r<resolution+i;r++){
					Vec3d v2 = v1.rotateYaw((float) Math.toRadians(90-(rotatePar*r)));
					AxisAlignedBB bb = el.getEntityBoundingBox().expand(1.5F, 0, 1.5F).offset(v2.x,v2.y,v2.z);
					list.add(bb);
				}
			}


			return list;
		}

	}

	public static interface SubConsumer<T> extends BiConsumer<RangedHelper<T>,EntityLivingBase>{

	}
	public static class PlayerBoundingBox<V extends IActionPerformer> implements Function<V,List<AxisAlignedBB>> {

		final double range;
		public PlayerBoundingBox(double range){
			this.range = range;
		}
		@Override
		public List<AxisAlignedBB> apply(V t) {
			// TODO 自動生成されたメソッド・スタブ
			return Lists.newArrayList(t.getPerformer().getEntityBoundingBox().grow(range));
		}


	}
}
