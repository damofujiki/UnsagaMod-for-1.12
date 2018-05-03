package mods.hinasch.unsaga.common.specialaction;

import java.util.function.Consumer;

import net.minecraft.util.EnumActionResult;

public class ActionEmpty<T extends IActionPerformer> implements ISpecialAction<T>{

	public ActionEmpty() {

	}


	@Override
	public EnumActionResult perform(T context) {
		return EnumActionResult.PASS;
	}

	@Override
	public Consumer<T> getPrePerform() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}


	@Override
	public boolean isBenefical() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}







}
