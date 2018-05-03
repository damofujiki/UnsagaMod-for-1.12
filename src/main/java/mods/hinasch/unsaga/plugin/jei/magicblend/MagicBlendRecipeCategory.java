package mods.hinasch.unsaga.plugin.jei.magicblend;

import java.util.List;

import com.google.common.collect.Lists;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiIngredientGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.element.FiveElements;
import mods.hinasch.unsaga.plugin.jei.JEIUnsagaPlugin;
import mods.hinasch.unsaga.plugin.jei.ingredient.ElementIngredientRenderer;
import mods.hinasch.unsaga.plugin.jei.ingredient.ElementPair;
import mods.hinasch.unsaga.plugin.jei.ingredient.SpellIngredientRenderer;
import mods.hinasch.unsaga.plugin.jei.ingredient.SpellWrapper;
import mods.hinasch.unsagamagic.spell.Spell;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;




public class MagicBlendRecipeCategory implements IRecipeCategory<MagicBlendRecipeWrapper>,ITooltipCallback<Spell>{



	public static final ResourceLocation RES = new ResourceLocation(UnsagaMod.MODID,"textures/gui/container/recipe_magic.png");
    private final String title;
    private final IDrawable background;
    private final IDrawable icon;
	public MagicBlendRecipeCategory(IGuiHelper guiHelper){
		this.title = HSLibs.translateKey("jei."+this.getUid()+".title");

		this.background = guiHelper.createDrawable(RES, 8, 8, 160, 56);
		this.icon = guiHelper.createDrawable(RES, 0, 168, 16, 16);
	}


	@Override
	public void drawExtras(Minecraft minecraft) {
		// TODO 自動生成されたメソッド・スタブ
		ClientHelper.fontRenderer().drawStringWithShadow("Base Spell", 0, 0, 0xffffff);
		ClientHelper.fontRenderer().drawStringWithShadow("Required Elements", 0, 26, 0xffffff);
	}



	public IDrawable getBackground() {
		// TODO 自動生成されたメソッド・スタブ
		return background;
	}

	@Override
	public IDrawable getIcon() {
		// TODO 自動生成されたメソッド・スタブ
		return icon;
	}

	@Override
	public String getTitle() {
		// TODO 自動生成されたメソッド・スタブ
		return title;
	}

	@Override
	public List getTooltipStrings(int mouseX, int mouseY) {
		// TODO 自動生成されたメソッド・スタブ
		return Lists.newArrayList();
	}

	@Override
	public String getUid() {
		// TODO 自動生成されたメソッド・スタブ
		return JEIUnsagaPlugin.ID_MAGICBLEND;
	}



	@Override
	public void setRecipe(IRecipeLayout recipeLayout, MagicBlendRecipeWrapper recipeWrapper, IIngredients ingredients) {
		RecipeBlend recipe = recipeWrapper.getRecipe();
		IGuiIngredientGroup<SpellWrapper> guiSpell = recipeLayout.getIngredientsGroup(SpellWrapper.class);
		IGuiIngredientGroup<ElementPair> guiElm = recipeLayout.getIngredientsGroup(ElementPair.class);

		guiSpell.init(0, true, new SpellIngredientRenderer(), 0, 10, 16, 16, 0, 0);
		guiSpell.set(0, ingredients.getInputs(SpellWrapper.class).get(0));

		int index = 0;

		for(FiveElements.Type type:FiveElements.Type.values()){
			guiElm.init(1+index, true, new ElementIngredientRenderer(), (18*index), 38, 16, 16, 0, 0);
			guiElm.set(1+index,ingredients.getInputs(ElementPair.class).get(index));
			index ++;
		}

		guiSpell.init(10, false, new SpellIngredientRenderer(), 135, 28, 16, 16, 0, 0);
		guiSpell.set(10,ingredients.getOutputs(SpellWrapper.class).get(0));
	}
	@Override
	public void onTooltip(int slotIndex, boolean input, Spell ingredient, List<String> tooltip) {
		if(slotIndex==0){
			tooltip.add("Base");
		}

	}


	@Override
	public String getModName() {
		// TODO 自動生成されたメソッド・スタブ
		return UnsagaMod.MODID;
	}

}
