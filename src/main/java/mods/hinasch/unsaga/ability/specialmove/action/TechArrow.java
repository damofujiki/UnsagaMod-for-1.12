package mods.hinasch.unsaga.ability.specialmove.action;

import mods.hinasch.lib.particle.ParticleHelper.MovingType;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.ability.specialmove.TechInvoker;
import mods.hinasch.unsaga.ability.specialmove.TechInvoker.InvokeType;
import mods.hinasch.unsaga.core.entity.projectile.custom_arrow.SpecialArrowType;
import mods.hinasch.unsaga.core.entity.projectile.custom_arrow.CustomArrowCapability;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.EnumActionResult;

public class TechArrow extends TechActionBase{

	SpecialArrowType type;
	public TechArrow() {
		super(InvokeType.BOW);
		this.addAction(new IAction<TechInvoker>(){

			@Override
			public EnumActionResult apply(TechInvoker context) {
				float f = context.getChargedTime() * 0.01F;
				EntityArrow arrow = context.getArrowComponent().get().arrowEntity;
//				EntityCustomArrow arrow = new EntityCustomArrow(context.getWorld(),context.getPerformer());
				arrow.shoot(context.getPerformer(), context.getPerformer().rotationPitch, context.getPerformer().rotationYaw, 0.0F, f * 3.0F, 1.0F);
//				ItemStack arrowStack = context.getArrowComponent().get().arrowStack.copy();
//				arrowStack.setCount(1);
//				arrow.setArrowStack(arrowStack);
//				arrow.pickupStatus = context.getArrowComponent().get().arrowEntity.pickupStatus;
				arrow.setDamage(arrow.getDamage()+context.getStrengthHP());
				if(CustomArrowCapability.ADAPTER.hasCapability(arrow)){
					CustomArrowCapability.ADAPTER.getCapability(arrow).setArrowType(type);
					CustomArrowCapability.ADAPTER.getCapability(arrow).setArrowLPStrength(context.getActionStrength().lp());
				}
				context.spawnParticle(MovingType.DIVERGE, XYZPos.createFrom(context.getPerformer()), type.getParticle(), 10, 0.05D);
				if(WorldHelper.isServer(context.getWorld())){
					context.getWorld().spawnEntity(arrow);

				}
				return EnumActionResult.SUCCESS;
			}}
		);
	}

	public SpecialArrowType getType(){
		return type;
	}

	public TechArrow setArrowType(SpecialArrowType type){
		this.type = type;
		return this;
	}



}
