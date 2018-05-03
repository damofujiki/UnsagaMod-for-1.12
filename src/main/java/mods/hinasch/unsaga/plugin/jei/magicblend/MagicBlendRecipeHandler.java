//package mods.hinasch.unsaga.plugin.jei.magicblend;
//
//import mezz.jei.api.recipe.IRecipeHandler;
//import mezz.jei.api.recipe.IRecipeWrapper;
//import mods.hinasch.unsaga.UnsagaMod;
//import mods.hinasch.unsaga.plugin.jei.JEIUnsagaPlugin;
//
//public class MagicBlendRecipeHandler implements IRecipeHandler<RecipeBlend>{
//
//	@Override
//	public Class<RecipeBlend> getRecipeClass() {
//		// TODO 自動生成されたメソッド・スタブ
//		return RecipeBlend.class;
//	}
//
//	@Override
//	public String getRecipeCategoryUid() {
//		// TODO 自動生成されたメソッド・スタブ
//		return JEIUnsagaPlugin.ID_MAGICBLEND;
//	}
//
//	@Override
//	public String getRecipeCategoryUid(RecipeBlend recipe) {
//		// TODO 自動生成されたメソッド・スタブ
//		return this.getRecipeCategoryUid();
//	}
//
//	@Override
//	public IRecipeWrapper getRecipeWrapper(RecipeBlend recipe) {
//		// TODO 自動生成されたメソッド・スタブ
//		return new MagicBlendRecipeWrapper(recipe);
//	}
//
//	@Override
//	public boolean isRecipeValid(RecipeBlend recipe) {
//		// TODO 自動生成されたメソッド・スタブ
//		return recipe.getInput()!=null && recipe.getOutput()!=null && recipe.getTable()!=null;
//	}
//
//}
