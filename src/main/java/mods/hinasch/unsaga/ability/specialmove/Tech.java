package mods.hinasch.unsaga.ability.specialmove;

import java.util.OptionalInt;

import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.ability.AbilityBase;
import mods.hinasch.unsaga.ability.specialmove.TechInvoker.InvokeType;
import mods.hinasch.unsaga.ability.specialmove.action.TechActionBase;
import mods.hinasch.unsaga.damage.DamageComponent;
import mods.hinasch.unsaga.lp.LPAttribute;

/**
 *
 * またはSpecialMove。
 *
 */
public class Tech extends AbilityBase{


	public static final TechActionBase EMPTY = new TechActionBase(InvokeType.BOW);

	int cost = 1;
	boolean isRequireTarget = false;
	DamageComponent str = DamageComponent.ZERO;
	OptionalInt coolingTime = OptionalInt.empty();
	public Tech(String name) {
		super(name);
		this.exclusionNum = 1;
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public TechActionBase getAction(){
		if(TechRegistry.instance().getAssociatedAction(this)!=null){
			return TechRegistry.instance().getAssociatedAction(this);
		}
		return EMPTY;
	}

	public Tech setRequireTarget(boolean par1){
		this.isRequireTarget = par1;
		return this;
	}
	public boolean isRequireTarget(){
		return this.isRequireTarget;
	}
	public OptionalInt getCoolingTime(){
		return this.coolingTime;
	}

	public int getCost() {
		// TODO 自動生成されたメソッド・スタブ
		return cost;
	}
	@Override
	public String getLocalized() {
		return HSLibs.translateKey("ability."+this.getPropertyName());
	}
	public DamageComponent getStrength(){
		return this.str;
	}

	public Tech setCoolingTime(int cool){
		this.coolingTime = OptionalInt.of(cool);
		return this;
	}

	public Tech setCost(int cost) {
		this.cost = cost;
		return this;
	}

	public Tech setStrength(float hp,float lp){
		this.str = DamageComponent.of(hp, new LPAttribute(lp, 1));
		return this;
	}

	@Override
	public int getExclusionNumber() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}
}
