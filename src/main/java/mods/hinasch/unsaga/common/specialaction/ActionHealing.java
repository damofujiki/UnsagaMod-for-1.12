package mods.hinasch.unsaga.common.specialaction;

import java.util.Optional;

import mods.hinasch.lib.particle.ParticleHelper.MovingType;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.common.specialaction.ActionBase.IAction;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumParticleTypes;

public class ActionHealing<T extends IActionPerformer> implements IAction<T>{

	@Override
	public EnumActionResult apply(T context) {
		float healAmount = context.getStrength().hp();
		Optional<EntityLivingBase> target = context.getTarget();
		if(target.isPresent()){
			context.spawnParticle(MovingType.FLOATING, XYZPos.createFrom(target.get())
					, EnumParticleTypes.VILLAGER_HAPPY, 10, 0.02D);
			if(healAmount>0.0F){
				target.get().heal(healAmount);
				context.broadCastMessage(
						HSLibs.translateKey("spell.unsaga.msg.healed",target.get().getName(),(int)healAmount));
			}

			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}


}
