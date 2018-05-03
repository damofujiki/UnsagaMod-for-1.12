package mods.hinasch.unsaga.plugin.jei.magicblend;

import com.google.common.base.Preconditions;

import mods.hinasch.unsaga.element.newele.ElementTable;
import mods.hinasch.unsagamagic.spell.Spell;

public class RecipeBlend {

	final Spell spell;
	final ElementTable table;
	final Spell outputSpell;
	public RecipeBlend(Spell spell,ElementTable table,Spell output){
		Preconditions.checkNotNull(spell);
		Preconditions.checkNotNull(table);
		Preconditions.checkNotNull(output);
		this.spell = spell;
		this.table = table;
		this.outputSpell = output;
	}
	public Spell getInput() {
		return spell;
	}
	public ElementTable getTable() {
		return table;
	}
	public Spell getOutput() {
		return outputSpell;
	}
}
