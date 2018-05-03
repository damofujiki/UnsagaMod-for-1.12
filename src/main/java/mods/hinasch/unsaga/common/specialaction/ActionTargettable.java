package mods.hinasch.unsaga.common.specialaction;

import java.util.Optional;

import com.google.common.base.Preconditions;

import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.common.specialaction.ActionBase.IAction;
import mods.hinasch.unsaga.common.specialaction.IActionPerformer.TargetType;
import mods.hinasch.unsaga.status.TargetHolderCapability;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumActionResult;

public class ActionTargettable<T extends IActionPerformer> implements IAction<T>{

	IAction<T> action;
	TargetType strictType;
	public ActionTargettable(IAction<T> action){
		this.action = action;
	}

	public ActionTargettable<T> setStrictType(TargetType type){
		this.strictType = type;
		return this;
	}
	@Override
	public EnumActionResult apply(T context) {
		Optional<EntityLivingBase> target = Optional.empty();
		UnsagaMod.logger.trace(this.getClass().getName(), "targetは"+context.getTargetType());
		Preconditions.checkNotNull(context);
		Preconditions.checkNotNull(context.getTargetType());

		TargetType selector = null;
		if(this.strictType!=null){
			selector = this.strictType;
		}else{
			selector = context.getTargetType();
		}
		switch(selector){
		case OWNER:
			target = Optional.of(context.getPerformer());
			break;
		case POSITION:
			if(context.getTargetCoordinate().isPresent()){
				return this.action.apply(context);
			}
			break;
		case TARGET:
			if(TargetHolderCapability.adapter.hasCapability(context.getPerformer())){
				target = TargetHolderCapability.adapter.getCapability(context.getPerformer()).getTarget();
			}

			break;
		default:
			break;


		}
		if(target.isPresent()){
			context.setTarget(target.get());
			return this.action.apply(context);
		}

		return EnumActionResult.PASS;
	}

	public ActionTargettable<T> setAction(IAction<T> action){
		this.action = action;
		return this;
	}
}
