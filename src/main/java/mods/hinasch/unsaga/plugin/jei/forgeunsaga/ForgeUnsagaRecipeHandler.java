//package mods.hinasch.unsaga.plugin.jei.forgeunsaga;
//
//import mezz.jei.api.recipe.IRecipeHandler;
//import mezz.jei.api.recipe.IRecipeWrapper;
//import mods.hinasch.unsaga.plugin.jei.JEIUnsagaPlugin;
//import mods.hinasch.unsaga.villager.smith.MaterialTransformRegistry.MaterialTransform;
//
//public class ForgeUnsagaRecipeHandler implements IRecipeHandler<MaterialTransform>{
//
//	@Override
//	public Class<MaterialTransform> getRecipeClass() {
//		// TODO 自動生成されたメソッド・スタブ
//		return MaterialTransform.class;
//	}
//
//	@Override
//	public String getRecipeCategoryUid() {
//		// TODO 自動生成されたメソッド・スタブ
//		return JEIUnsagaPlugin.ID_CUSTOMTOOL;
//	}
//
//	@Override
//	public String getRecipeCategoryUid(MaterialTransform recipe) {
//		// TODO 自動生成されたメソッド・スタブ
//		return this.getRecipeCategoryUid();
//	}
//
//	@Override
//	public IRecipeWrapper getRecipeWrapper(MaterialTransform recipe) {
//		// TODO 自動生成されたメソッド・スタブ
//		return new ForgeUnsagaRecipeWrapper(recipe);
//	}
//
//	@Override
//	public boolean isRecipeValid(MaterialTransform recipe) {
//		if(recipe!=null){
//			if(recipe.getBase()!=null && recipe.getSub()!=null && recipe.getTransformed()!=null && recipe.wildcard()!=null){
//				return true;
//			}
//		}
//		return false;
//	}
//
//}
