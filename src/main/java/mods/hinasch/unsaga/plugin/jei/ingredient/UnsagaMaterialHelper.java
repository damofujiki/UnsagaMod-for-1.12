package mods.hinasch.unsaga.plugin.jei.ingredient;

import java.awt.Color;
import java.util.List;

import com.google.common.collect.Lists;

import mezz.jei.api.ingredients.IIngredientHelper;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.material.UnsagaMaterial;

public class UnsagaMaterialHelper implements IIngredientHelper<UnsagaMaterial>{

	public static final String ID = UnsagaMod.MODID+".material";
	@Override
	public List<UnsagaMaterial> expandSubtypes(List<UnsagaMaterial> ingredients) {
		// TODO 自動生成されたメソッド・スタブ
		return ingredients;
	}

	@Override
	public UnsagaMaterial getMatch(Iterable<UnsagaMaterial> ingredients, UnsagaMaterial ingredientToMatch) {
		for(UnsagaMaterial in:ingredients){
			if(in==ingredientToMatch){
				return in;
			}
		}
		return null;
	}

	@Override
	public String getDisplayName(UnsagaMaterial ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return ingredient.getPropertyName();
	}

	@Override
	public String getUniqueId(UnsagaMaterial ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return ID;
	}

	@Override
	public String getWildcardId(UnsagaMaterial ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return this.getUniqueId(ingredient);
	}

	@Override
	public String getModId(UnsagaMaterial ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return UnsagaMod.MODID;
	}

	@Override
	public Iterable<Color> getColors(UnsagaMaterial ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return Lists.newArrayList();
	}

	@Override
	public String getErrorInfo(UnsagaMaterial ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return "error:unsagamod";
	}

	@Override
	public String getResourceId(UnsagaMaterial ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public UnsagaMaterial copyIngredient(UnsagaMaterial ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return ingredient;
	}

}
