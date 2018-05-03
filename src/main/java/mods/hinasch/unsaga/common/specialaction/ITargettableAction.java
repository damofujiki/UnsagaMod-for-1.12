package mods.hinasch.unsaga.common.specialaction;

import java.util.Optional;

import mods.hinasch.unsaga.status.TargetHolderCapability;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumActionResult;

public interface ITargettableAction<T extends ActionPerformerBase> {

	public abstract EnumActionResult perform(T context,Optional<EntityLivingBase> target);

	public default EnumActionResult selectTarget(T context) {
		Optional<EntityLivingBase> target = Optional.empty();
		switch(context.getTargetType()){
		case OWNER:
			target = Optional.of(context.getPerformer());
			break;
		case POSITION:
			break;
		case TARGET:
			target = TargetHolderCapability.adapter.getCapability(context.getPerformer()).getTarget();
			break;
		default:
			break;


		}
		return this.perform(context, target);
	}
}
