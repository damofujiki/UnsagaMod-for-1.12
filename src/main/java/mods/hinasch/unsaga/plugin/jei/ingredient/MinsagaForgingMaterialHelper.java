package mods.hinasch.unsaga.plugin.jei.ingredient;

import java.awt.Color;
import java.util.List;

import mezz.jei.api.ingredients.IIngredientHelper;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.minsaga.MinsagaMaterial;

public class MinsagaForgingMaterialHelper implements IIngredientHelper<MinsagaMaterial>{

	public static final String ID = UnsagaMod.MODID+".materialMinsaga";


	@Override
	public List<MinsagaMaterial> expandSubtypes(List<MinsagaMaterial> ingredients) {
		// TODO 自動生成されたメソッド・スタブ
		return ingredients;
	}

	@Override
	public MinsagaMaterial getMatch(Iterable<MinsagaMaterial> ingredients, MinsagaMaterial ingredientToMatch) {
		for(MinsagaMaterial in:ingredients){
			if(in==ingredientToMatch){
				return in;
			}
		}
		return null;
	}

	@Override
	public String getDisplayName(MinsagaMaterial ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return "material."+ingredient.getPropertyName();
	}

	@Override
	public String getUniqueId(MinsagaMaterial ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return ingredient.getPropertyName();
	}

	@Override
	public String getWildcardId(MinsagaMaterial ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return this.getUniqueId(ingredient);
	}

	@Override
	public String getModId(MinsagaMaterial ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return UnsagaMod.MODID;
	}

	@Override
	public Iterable<Color> getColors(MinsagaMaterial ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public String getErrorInfo(MinsagaMaterial ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public String getResourceId(MinsagaMaterial ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return ingredient.getPropertyName();
	}

	@Override
	public MinsagaMaterial copyIngredient(MinsagaMaterial ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return ingredient;
	}

}
