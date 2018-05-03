package mods.hinasch.unsaga.plugin.jei.ingredient;

import mods.hinasch.unsagamagic.spell.Spell;

public class SpellWrapper implements Comparable<SpellWrapper>{

	Spell spell;

	public SpellWrapper(Spell spell){
		this.spell = spell;
	}

	public Spell getSpell(){
		return this.spell;
	}

	@Override
	public int compareTo(SpellWrapper o) {
		// TODO 自動生成されたメソッド・スタブ
		return this.getSpell().compareTo(o.getSpell());
	}
}
