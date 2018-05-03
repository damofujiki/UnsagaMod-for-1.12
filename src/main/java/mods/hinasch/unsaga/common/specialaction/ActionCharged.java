package mods.hinasch.unsaga.common.specialaction;

import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.ability.specialmove.TechInvoker;
import mods.hinasch.unsaga.common.specialaction.ActionBase.IAction;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import net.minecraft.util.EnumActionResult;

public class ActionCharged<T extends IAction<TechInvoker>> implements IAction<TechInvoker>{

	int threshold = 20;

	T action;

	public ActionCharged(T act){
		this.action = act;
	}
	@Override
	public EnumActionResult apply(TechInvoker context) {
		UnsagaMod.logger.trace(this.getClass().getName(), context.getChargedTime());
		if(context.getChargedTime()>=this.getChargeThreshold()){

			return this.action.apply(context);
		}
		return EnumActionResult.PASS;
	}


	public int getChargeThreshold() {
		// TODO 自動生成されたメソッド・スタブ
		return this.threshold;
	}

	public T getAction(){
		return this.action;
	}

	public ActionCharged setAction(T action){
		this.action = action;
		return this;
	}

	public ActionCharged setChargeThreshold(int time) {
		this.threshold = time;
		return this;
	}

	public static ActionCharged simpleChargedMelee(float knockback,General... general){
		ActionMelee melee = new ActionMelee(general);
		melee.setKnockbackStrength(knockback);
		ActionCharged rt = new ActionCharged(melee);
		return rt;
	}
}
