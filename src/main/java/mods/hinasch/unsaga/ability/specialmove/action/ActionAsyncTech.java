package mods.hinasch.unsaga.ability.specialmove.action;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.ability.specialmove.TechInvoker;
import mods.hinasch.unsaga.common.specialaction.ActionBase.IAction;
import mods.hinasch.unsaga.core.potion.StateMovingTech;
import mods.hinasch.unsaga.core.potion.UnsagaPotions;
import mods.hinasch.unsaga.util.LivingHelper;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.SoundEvent;

public class ActionAsyncTech implements IAction<TechInvoker>{

	final Consumer<IMovingStateAdapter> consumer;
	final int time;
	@Nullable SoundEvent sound;
	float pitch = 1.0F;

	public ActionAsyncTech(Consumer<IMovingStateAdapter> consumer,int time){
		this.consumer = consumer;
		this.time = time;
	}
	@Override
	public EnumActionResult apply(TechInvoker t) {
		if(!LivingHelper.isStateActive(t.getPerformer(), UnsagaPotions.MOVING_STATE)){
			if(sound!=null)t.playSound(XYZPos.createFrom(t.getPerformer()), sound, false,pitch);
			PotionEffect state = StateMovingTech.create(time, t,MovingStates.GUST);
			LivingHelper.getCapability(t.getPerformer()).addState(state);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}


	public ActionAsyncTech setStartSound(SoundEvent sound,float pitch){
		this.sound = sound;
		this.pitch = pitch;
		return this;
	}
}
