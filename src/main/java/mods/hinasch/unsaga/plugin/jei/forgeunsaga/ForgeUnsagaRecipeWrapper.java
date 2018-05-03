package mods.hinasch.unsaga.plugin.jei.forgeunsaga;

import java.util.List;

import com.google.common.collect.Lists;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import mods.hinasch.lib.misc.XY;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.material.UnsagaMaterial;
import mods.hinasch.unsaga.villager.smith.MaterialTransformRegistry.MaterialTransform;
import mods.hinasch.unsaga.villager.smith.MaterialTransformRegistry.WILDCARD;
import net.minecraft.client.Minecraft;

public class ForgeUnsagaRecipeWrapper implements IRecipeWrapper{

	private final MaterialTransform recipe;
	private UnsagaMaterial base;
	private List<UnsagaMaterial> subs;
	private UnsagaMaterial output;
	public ForgeUnsagaRecipeWrapper(MaterialTransform recipe){
		this.recipe = recipe;
		this.base = recipe.getBase();
		this.subs = Lists.newArrayList();
		if(recipe.wildcard()!=WILDCARD.NONE){
			this.subs.addAll(recipe.wildcard().getMaterials());
		}else{
			this.subs.add(recipe.getSub());
		}
		this.output = recipe.getTransformed();
//		ItemStack is = MaterialItemAssociatedRegistry.instance().getAssociatedStack(recipe.getBase());
//		inputsBase = Lists.newArrayList(is);
//		List<ItemStack> list = SmithMaterialRegistry.instance().findItemStacksByMaterial(recipe.getBase());
//		if(!list.isEmpty()){
//			inputsBase.addAll(list);
//		}
//
//		ItemStack sub = MaterialItemAssociatedRegistry.instance().getAssociatedStack(recipe.getSub());
//		inputsSub = Lists.newArrayList(sub);
//		List<ItemStack> listSub = SmithMaterialRegistry.instance().findItemStacksByMaterial(recipe.getSub());
//		if(!listSub.isEmpty()){
//			inputsSub.addAll(listSub);
//		}
//
//
//
//		ItemStack output = MaterialItemAssociatedRegistry.instance().getAssociatedStack(recipe.getTransformed());
//		this.outputs = Lists.newArrayList(output);
//		List<ItemStack> outputList = SmithMaterialRegistry.instance().findItemStacksByMaterial(recipe.getTransformed());
//		if(!outputList.isEmpty()){
//			this.outputs.addAll(outputList);
//		}

	}
	@Override
	public void getIngredients(IIngredients ingredients) {
		List<List<UnsagaMaterial>> list = Lists.newArrayList();
		list.add(Lists.newArrayList(this.base));
		list.add(subs);
		ingredients.setInputLists(UnsagaMaterial.class,list);
		ingredients.setOutput(UnsagaMaterial.class, output);
	}


	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		// TODO 自動生成されたメソッド・スタブ

	}



	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		List<String> list = Lists.newArrayList();
		if(HSLibs.isXYIn(XY.of(mouseX, mouseY), XY.of(82, 26), XY.of(104, 42))){
			list.add((int)(this.recipe.getProb()*100F)+"%");
		}
		return list;
	}

	@Override
	public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {

		return false;
	}



}
