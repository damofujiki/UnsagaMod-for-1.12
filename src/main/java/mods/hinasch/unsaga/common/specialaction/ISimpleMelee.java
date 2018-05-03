package mods.hinasch.unsaga.common.specialaction;

import java.util.EnumSet;
import java.util.List;
import java.util.function.BiConsumer;

import com.google.common.collect.Lists;

import mods.hinasch.lib.util.VecUtil;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.damage.AdditionalDamageData;
import mods.hinasch.unsaga.damage.DamageComponent;
import mods.hinasch.unsaga.damage.DamageHelper;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.Sub;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public interface ISimpleMelee<T extends IActionPerformer> {

	public EnumSet<General> getAttributes();
	public EnumSet<Sub> getSubAttributes();
	public DamageComponent getDamage(T context,EntityLivingBase target,DamageComponent base);
//	public LPStrPair getLPDamage(T context,EntityLivingBase target,LPStrPair base);

	/**
	 * ターゲット攻撃後の作用
	 * @return
	 */
	public BiConsumer<T,EntityLivingBase> getAdditionalBehavior();
	public float getReach(T context);
	public float getKnockbackStrength();

	public default EnumActionResult performSimpleAttack(T context) {

		context.playSound(XYZPos.createFrom(context.getPerformer()), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, false);
		context.getPerformer().swingArm(EnumHand.MAIN_HAND);

		List<EntityLivingBase> list = Lists.newArrayList();
		for(int i=0;i<this.getReach(context);i++){
			Vec3d lookvec = context.getPerformer().getLookVec().normalize().scale(1.0D * (i+1));
			AxisAlignedBB bb = context.getPerformer().getEntityBoundingBox().offset(lookvec.x,lookvec.y,lookvec.z);
			context.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, bb,in ->in!=context.getPerformer()).forEach(in ->{
				list.add(in);

			});
		}
		if(!list.isEmpty()){
			EntityLivingBase el = list.get(0);
//			el.attackEntityFrom(DamageSourceUnsaga.create(context.getPerformer(), this.getLPDamage(context, el, context.getModifiedStrength().lp()), this.getAttributes()).setSubTypes(getSubAttributes()),this.getDamage(context,el,context.getModifiedStrength().hp()));
			DamageComponent damage = this.getDamage(context, el, context.getStrength());
			el.attackEntityFrom(DamageHelper.create(new AdditionalDamageData(AdditionalDamageData.getSource(context.getPerformer()),damage.lp(),this.getAttributes())),damage.hp());
			if(this.getKnockbackStrength()>0){
				VecUtil.knockback(context.getPerformer(), el, getKnockbackStrength(), 1.0D);
			}
			if(this.getAdditionalBehavior()!=null){
				this.getAdditionalBehavior().accept(context, el);
			}
			return EnumActionResult.SUCCESS;
		}
//		list.forEach(in ->{
//
//		});
		//		AxisAlignedBB bb = context.getPerformer().getEntityBoundingBox().expandXyz(1.0F).offset(lookvec.xCoord,0,lookvec.zCoord);
		//		context.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, bb,in ->in!=context.getPerformer())
		//		.forEach(in ->{
		//			in.attackEntityFrom(DamageSourceUnsaga.create(context.getPerformer(), context.getActionProperty().getStrength().lp(), this.attributes), context.getModifiedStrength());
		//			if(this.consumer!=null){
		//				this.consumer.accept(context, in);
		//			}
		//		});
		return EnumActionResult.PASS;
	}
}
