package mods.hinasch.unsaga.common.specialaction;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import mods.hinasch.unsaga.common.specialaction.ActionBase.IAction;
import mods.hinasch.unsaga.common.specialaction.option.TargetOptionItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;

public class ActionItem<T extends IActionPerformer> implements IAction<T>{

	protected BiFunction<T,ItemStack,EnumActionResult> function;
	@Override
	public EnumActionResult apply(T context) {

		if(context.getOption() instanceof TargetOptionItem){
			TargetOptionItem option = (TargetOptionItem)context.getOption();
			ItemStack target = option.getStackFromLiving(context.getPerformer());
			if(!target.isEmpty()){
				return this.function.apply(context, target);
			}
		}
		return EnumActionResult.PASS;
	}

	public ActionItem<T> setFunction(BiFunction<T,ItemStack,EnumActionResult> function){
		this.function = function;
		return this;
	}

	public static interface ItemPredicate extends Predicate<ItemStack>{

	}
}
