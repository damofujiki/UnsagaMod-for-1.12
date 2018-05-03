package mods.hinasch.unsaga.plugin.jei.forgeunsaga;

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
import mods.hinasch.unsaga.material.UnsagaMaterial;
import mods.hinasch.unsaga.plugin.jei.JEIUnsagaPlugin;
import mods.hinasch.unsaga.plugin.jei.ingredient.UnsagaMaterialRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class ForgeUnsagaRecipeCategory implements IRecipeCategory<ForgeUnsagaRecipeWrapper>,ITooltipCallback<UnsagaMaterial>{

	private final String title;
    private final IDrawable background;
    private final IDrawable icon;
    public static final ResourceLocation RES = new ResourceLocation(UnsagaMod.MODID,"textures/gui/container/recipe.png");
	public ForgeUnsagaRecipeCategory(IGuiHelper guiHelper){
		this.title = HSLibs.translateKey("jei."+this.getUid()+".title");

		this.background = guiHelper.createDrawable(RES, 8, 8, 160, 56);
		this.icon = guiHelper.createDrawable(RES, 0, 168, 16, 16);
	}
	@Override
	public String getUid() {
		// TODO 自動生成されたメソッド・スタブ
		return JEIUnsagaPlugin.ID_CUSTOMTOOL;
	}

	@Override
	public String getTitle() {
		// TODO 自動生成されたメソッド・スタブ
		return title;
	}

	@Override
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
	public void drawExtras(Minecraft minecraft) {
		// TODO 自動生成されたメソッド・スタブ
		ClientHelper.fontRenderer().drawStringWithShadow("Base", 15, 8, 0xffffff);
		ClientHelper.fontRenderer().drawStringWithShadow("Sub", 51, 8, 0xffffff);
	}



	@Override
	public void setRecipe(IRecipeLayout recipeLayout, ForgeUnsagaRecipeWrapper recipeWrapper, IIngredients ingredients) {
		// TODO 自動生成されたメソッド・スタブ

		UnsagaMaterial base = ingredients.getInputs(UnsagaMaterial.class).get(0).get(0);
		List<UnsagaMaterial> subs = ingredients.getInputs(UnsagaMaterial.class).get(1);
		UnsagaMaterial output = ingredients.getOutputs(UnsagaMaterial.class).get(0).get(0);
		IGuiIngredientGroup<UnsagaMaterial> gui = recipeLayout.getIngredientsGroup(UnsagaMaterial.class);
		gui.init(0, true,new UnsagaMaterialRenderer(), 15, 25,16,16,0,0);
		gui.set(0, base);

		gui.init(1, true,new UnsagaMaterialRenderer(), 51, 25,16,16,0,0);
		gui.set(1, subs);

		gui.init(2, true,new UnsagaMaterialRenderer(), 122, 25,16,16,0,0);
		gui.set(2,output);

		gui.addTooltipCallback(this);
	}

	@Override
	public List getTooltipStrings(int mouseX, int mouseY) {
		// TODO 自動生成されたメソッド・スタブ
		return Lists.newArrayList();
	}

	@Override
	public void onTooltip(int slotIndex, boolean input, UnsagaMaterial ingredient, List<String> tooltip) {
		// TODO 自動生成されたメソッド・スタブ
		if(slotIndex==0){
			tooltip.add("Base Material");

		}
		if(slotIndex==1){
			tooltip.add("Sub Material");

		}

	}
	@Override
	public String getModName() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
}
