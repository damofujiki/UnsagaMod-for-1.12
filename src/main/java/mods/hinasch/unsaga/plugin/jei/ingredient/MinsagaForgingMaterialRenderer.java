package mods.hinasch.unsaga.plugin.jei.ingredient;

import java.util.List;
import java.util.OptionalInt;

import com.google.common.collect.Lists;

import mezz.jei.api.ingredients.IIngredientRenderer;
import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.lib.client.RenderHelperHS;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.minsaga.MinsagaMaterial;
import mods.hinasch.unsaga.minsaga.MinsagaUtil;
import mods.hinasch.unsaga.plugin.jei.forgeunsaga.ForgeUnsagaRecipeCategory;
import mods.hinasch.unsaga.villager.smith.SmithMaterialRegistry.IGetItemStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;

public class MinsagaForgingMaterialRenderer implements IIngredientRenderer<MinsagaMaterial>{


	@Override
	public void render(Minecraft minecraft, int xPosition, int yPosition, MinsagaMaterial ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		ItemStack s = null;
		if(ingredient.checker() instanceof IGetItemStack){
			List<ItemStack> list = ((IGetItemStack)ingredient.checker()).getItemStack();
			if(!list.isEmpty()){
				s = list.get(0);
			}
		}


//		if(s== null){
//			s = new ItemStack(Items.APPLE);
//		}

		if(s==null){
			GlStateManager.enableAlpha();
			ClientHelper.bindTextureToTextureManager(ForgeUnsagaRecipeCategory.RES);
			RenderHelperHS.drawTexturedRect(xPosition, yPosition, 16, 168, 16, 16);

			return;
		}
		if (s != null) {
			RenderHelper.enableGUIStandardItemLighting();
			FontRenderer font = getFontRenderer(minecraft, ingredient);
			minecraft.getRenderItem().renderItemAndEffectIntoGUI(null, s, xPosition, yPosition);
			minecraft.getRenderItem().renderItemOverlayIntoGUI(font, s, xPosition, yPosition, null);
			GlStateManager.disableBlend();
			RenderHelper.disableStandardItemLighting();
		}

	}

	@Override
	public List<String> getTooltip(Minecraft minecraft, MinsagaMaterial ingredient, ITooltipFlag tooltipFlag) {
		List<String> list = Lists.newArrayList();
		list.add(HSLibs.translateKey("jei."+MinsagaForgingMaterialHelper.ID));
		list.add(ingredient.getLocalized());
		list.addAll(MinsagaUtil.getModifierStrings(ingredient, OptionalInt.of(ingredient.getRepairDamage())));
		return list;
	}

	@Override
	public FontRenderer getFontRenderer(Minecraft minecraft, MinsagaMaterial ingredient) {
		// TODO 自動生成されたメソッド・スタブ
		return minecraft.fontRenderer;
	}


}
