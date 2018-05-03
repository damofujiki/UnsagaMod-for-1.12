package mods.hinasch.unsaga.plugin.jei.ingredient;

import java.util.List;

import com.google.common.collect.Lists;

import mezz.jei.api.ingredients.IIngredientRenderer;
import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.lib.client.RenderHelperHS;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.plugin.jei.magicblend.MagicBlendRecipeCategory;
import mods.hinasch.unsagamagic.spell.SpellBlend;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag;

public class SpellIngredientRenderer implements IIngredientRenderer<SpellWrapper>{

	@Override
	public void render(Minecraft minecraft, int xPosition, int yPosition, SpellWrapper ingredient) {
		GlStateManager.enableAlpha();
		ClientHelper.bindTextureToTextureManager(MagicBlendRecipeCategory.RES);
		RenderHelperHS.drawTexturedRect(xPosition, yPosition, ingredient.getSpell().getElement().getMeta()*16, 184, 16, 16);
	}

	@Override
	public List<String> getTooltip(Minecraft minecraft, SpellWrapper ingredient, ITooltipFlag tooltipFlag) {
		List<String> list = Lists.newArrayList();
		if(ingredient.getSpell() instanceof SpellBlend){
			list.add(HSLibs.translateKey("jei."+SpellIngredientHelper.ID+".blended"));
		}else{
			list.add(HSLibs.translateKey("jei."+SpellIngredientHelper.ID));
		}

		list.add(ingredient.getSpell().getLocalizedByFullText());
		return list;
	}

	@Override
	public FontRenderer getFontRenderer(Minecraft minecraft, SpellWrapper ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return minecraft.fontRenderer;
	}

}
