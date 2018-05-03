package mods.hinasch.unsaga.common.specialaction;

import java.util.function.Consumer;

import net.minecraft.util.EnumActionResult;

public interface ISpecialAction<T extends IActionPerformer> {

	public EnumActionResult perform(T context);
	/** 基本的にAIで使う*/
	public boolean isBenefical();
	public Consumer<T> getPrePerform();
}
