package mods.hinasch.unsaga.common.specialaction;

import java.util.List;

import com.google.common.collect.Lists;

import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.common.specialaction.ActionBase.IAction;
import net.minecraft.util.EnumActionResult;

public class ActionList<T extends IActionPerformer> implements IAction<T>{

	List<IAction<T>> actionList = Lists.newArrayList();

	@Override
	public EnumActionResult apply(T context) {
		EnumActionResult result = EnumActionResult.PASS;
		for(IAction<T> act:this.actionList){

			result = act.apply(context);
			UnsagaMod.logger.trace(this.getClass().getName(), result,act);
		}
		return result;
	}

	public ActionList<T> addAction(IAction<T> action){
		this.actionList.add(action);
		return this;
	}
}
