package mods.hinasch.unsaga.plugin.jei.ingredient;

import java.awt.Color;
import java.util.List;

import com.google.common.collect.Lists;

import mezz.jei.api.ingredients.IIngredientHelper;
import mods.hinasch.unsaga.UnsagaMod;

public class ElementIngredientHelper implements IIngredientHelper<ElementPair>{

	public static final String ID = UnsagaMod.MODID+".element";
	@Override
	public List<ElementPair> expandSubtypes(List<ElementPair> ingredients) {
		// TODO 自動生成されたメソッド・スタブ
		return ingredients;
	}

	@Override
	public ElementPair getMatch(Iterable<ElementPair> ingredients, ElementPair ingredientToMatch) {
		for(ElementPair type:ingredients){
			if(type.first()==ingredientToMatch.first()){
				return type;
			}
		}
		return null;
	}

	@Override
	public String getDisplayName(ElementPair ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return ingredient.first().getSpellIconName();
	}

	@Override
	public String getUniqueId(ElementPair ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return ID;
	}

	@Override
	public String getWildcardId(ElementPair ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return this.getUniqueId(ingredient);
	}

	@Override
	public String getModId(ElementPair ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return UnsagaMod.MODID;
	}

	@Override
	public Iterable<Color> getColors(ElementPair ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return Lists.newArrayList();
	}

	@Override
	public String getErrorInfo(ElementPair ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return "error";
	}

	@Override
	public String getResourceId(ElementPair ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return ingredient.first().toString()+ingredient.second().toString();
	}

	@Override
	public ElementPair copyIngredient(ElementPair ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return new ElementPair(ingredient.first(), ingredient.second());
	}

}
