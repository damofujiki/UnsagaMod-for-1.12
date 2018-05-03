package mods.hinasch.unsaga.plugin.jei.magicblend;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import mods.hinasch.unsaga.element.FiveElements;
import mods.hinasch.unsaga.plugin.jei.ingredient.ElementPair;
import mods.hinasch.unsaga.plugin.jei.ingredient.SpellWrapper;
import net.minecraft.client.Minecraft;

public class MagicBlendRecipeWrapper implements IRecipeWrapper{

	final RecipeBlend recipe;
	public MagicBlendRecipeWrapper(RecipeBlend recipe){

		this.recipe = recipe;
	}
	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInput(SpellWrapper.class, new SpellWrapper(recipe.getInput()));
		List<ElementPair> list = Lists.newArrayList(FiveElements.Type.values()).stream().map(in -> new ElementPair(in,this.recipe.getTable().getInt(in))).collect(Collectors.toList());
//		for(FiveElements.Type type:FiveElements.Type.values()){
			ingredients.setInputs(ElementPair.class, list);
//		}

		ingredients.setOutput(SpellWrapper.class, new SpellWrapper(recipe.getOutput()));
	}

	public RecipeBlend getRecipe(){
		return this.recipe;
	}


	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		// TODO 自動生成されたメソッド・スタブ
		return Lists.newArrayList();
	}

	@Override
	public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

}
