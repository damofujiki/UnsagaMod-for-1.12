package mods.hinasch.unsaga.plugin.jei.ingredient;

import java.util.List;

import com.google.common.collect.Lists;

import mezz.jei.api.ingredients.IIngredientRenderer;
import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.lib.client.RenderHelperHS;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.plugin.jei.magicblend.MagicBlendRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag;

public class ElementIngredientRenderer implements IIngredientRenderer<ElementPair>{


	@Override
	public void render(Minecraft minecraft, int xPosition, int yPosition, ElementPair ingredient) {
		GlStateManager.enableAlpha();
		ClientHelper.bindTextureToTextureManager(MagicBlendRecipeCategory.RES);
		RenderHelperHS.drawTexturedRect(xPosition, yPosition, 16*ingredient.first().getMeta(), 184, 16, 16);
		String text = String.valueOf(ingredient.second());
//		this.getFontRenderer(minecraft, ingredient).FONT_HEIGHT = 200;
		this.getFontRenderer(minecraft, ingredient).drawStringWithShadow(text, xPosition+7, yPosition+16, 0xffffff);
//		this.getFontRenderer(minecraft, ingredient).FONT_HEIGHT = 9;
		GlStateManager.disableAlpha();
	}

	@Override
	public List<String> getTooltip(Minecraft minecraft, ElementPair ingredient, ITooltipFlag tooltipFlag) {
		// TODO 自動生成されたメソッド・スタブ
		List<String> list = Lists.newArrayList();
		list.add(HSLibs.translateKey("jei."+ElementIngredientHelper.ID)+":"+ingredient.first().getLocalized());
		list.add(ingredient.second()+" or more");
		return list;
	}

	@Override
	public FontRenderer getFontRenderer(Minecraft minecraft, ElementPair ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return minecraft.fontRenderer;
	}

}
