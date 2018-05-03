package mods.hinasch.unsaga.ability;

import mods.hinasch.unsaga.damage.AdditionalDamageTypes;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.Sub;

public class AbilityShield extends Ability{

	final AdditionalDamageTypes blockables;
	final float baseValue;

	public AbilityShield(String name,AdditionalDamageTypes blockable,float baseValue) {
		super(name);
		// TODO 自動生成されたコンストラクター・スタブ
		this.blockables = blockable;
		this.baseValue = baseValue;
	}
	public AbilityShield(String name,float value,Sub... subs) {
		this(name,new AdditionalDamageTypes(General.MAGIC).setSubTypes(subs),value);
	}

	public AbilityShield(String name,General... gens) {
		this(name,new AdditionalDamageTypes(gens),1.0F);
	}
	public AdditionalDamageTypes getBlockableTypes(){
		return this.blockables;
	}

	public float getBlockableValue(){
		return this.baseValue;
	}
}
