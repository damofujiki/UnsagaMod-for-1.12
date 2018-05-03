package mods.hinasch.unsaga.common.specialaction;

import mods.hinasch.unsaga.common.specialaction.ActionBase.IAction;
import net.minecraft.util.EnumActionResult;

public class ActionRequireJump<T extends IActionPerformer> implements IAction<T>{

	final IAction<T> action;
	public ActionRequireJump(IAction<T> action){
		this.action = action;
	}
	@Override
	public EnumActionResult apply(T t) {
		if(!t.getPerformer().onGround){
			return action.apply(t);
		}
		return EnumActionResult.PASS;
	}

}
