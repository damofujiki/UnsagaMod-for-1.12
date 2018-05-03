package mods.hinasch.unsaga.plugin.jei.ingredient;

import java.awt.Color;
import java.util.List;

import com.google.common.collect.Lists;

import mezz.jei.api.ingredients.IIngredientHelper;
import mods.hinasch.unsaga.UnsagaMod;

public class SpellIngredientHelper implements IIngredientHelper<SpellWrapper>{

	public static final String ID = UnsagaMod.MODID+".spell";
	@Override
	public List<SpellWrapper> expandSubtypes(List<SpellWrapper> ingredients) {
		// TODO 自動生成されたメソッド・スタブ
		return ingredients;
	}

	@Override
	public SpellWrapper getMatch(Iterable<SpellWrapper> ingredients, SpellWrapper ingredientToMatch) {
		for(SpellWrapper spell:ingredients){
			if(spell.getSpell()==ingredientToMatch.getSpell()){
				return spell;
			}
		}
		return null;
	}

	@Override
	public String getDisplayName(SpellWrapper ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return ingredient.getSpell().getPropertyName();
	}

	@Override
	public String getUniqueId(SpellWrapper ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return ID;
	}

	@Override
	public String getWildcardId(SpellWrapper ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return this.getUniqueId(ingredient);
	}

	@Override
	public String getModId(SpellWrapper ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return UnsagaMod.MODID;
	}

	@Override
	public Iterable<Color> getColors(SpellWrapper ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return Lists.newArrayList();
	}

	@Override
	public String getErrorInfo(SpellWrapper ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return "error";
	}

	@Override
	public String getResourceId(SpellWrapper ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return ingredient.getSpell().getPropertyName();
	}

	@Override
	public SpellWrapper copyIngredient(SpellWrapper ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return new SpellWrapper(ingredient.spell);
	}

}
