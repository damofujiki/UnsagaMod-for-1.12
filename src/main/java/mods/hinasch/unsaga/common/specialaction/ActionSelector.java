package mods.hinasch.unsaga.common.specialaction;

import java.util.Map;

import com.google.common.collect.Maps;

import mods.hinasch.unsaga.ability.specialmove.TechInvoker;
import mods.hinasch.unsaga.ability.specialmove.TechInvoker.InvokeType;
import mods.hinasch.unsaga.common.specialaction.ActionBase.IAction;
import net.minecraft.util.EnumActionResult;

public class ActionSelector implements IAction<TechInvoker>{

	Map<InvokeType,IAction<TechInvoker>> selectableMap = Maps.newHashMap();

	public ActionSelector addAction(InvokeType type,IAction<TechInvoker> action){
		this.selectableMap.put(type, action);
		return this;
	}

	@Override
	public EnumActionResult apply(TechInvoker context) {
		IAction<TechInvoker> action = selectableMap.get(context.getInvokeType());
		if(action!=null){
			return action.apply(context);
		}
		return EnumActionResult.PASS;
	}

}
