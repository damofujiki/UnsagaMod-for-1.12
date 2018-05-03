package mods.hinasch.unsaga.ability;

public class AbilityNaturalHeal extends Ability{

	int healValue;
	public AbilityNaturalHeal(String name) {
		super(name);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public Ability setHealAmount(int value){
		this.healValue = value;
		return this;
	}

	public int getHealAmount(){
		return this.healValue;
	}

}
