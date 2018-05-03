package mods.hinasch.unsaga.damage;

import com.mojang.realmsclient.util.Pair;

import mods.hinasch.unsaga.lp.LPAttribute;

public class DamageComponent extends Pair<Float,LPAttribute>{

	protected DamageComponent(Float first, LPAttribute second) {
		super(first, second);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public float hp(){
		return this.first();
	}

	public LPAttribute lp(){
		return this.second();
	}
	public static DamageComponent of(float hp,LPAttribute lp){
		return new DamageComponent(hp,lp);

	}
	public static DamageComponent of(float hp,float lp){
		return new DamageComponent(hp,new LPAttribute(lp,1));

	}
	public static final DamageComponent ZERO = DamageComponent.of(0, new LPAttribute(0.0F,0));
}
