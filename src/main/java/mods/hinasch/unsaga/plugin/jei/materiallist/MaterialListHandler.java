//package mods.hinasch.unsaga.plugin.jei.materiallist;
//
//import mezz.jei.api.recipe.IRecipeWrapper;
//import mezz.jei.api.recipe.IRecipeWrapperFactory;
//import mods.hinasch.unsaga.plugin.jei.JEIUnsagaPlugin;
//
//public class MaterialListHandler implements IRecipeWrapperFactory<MaterialWrapper>{
//
//	@Override
//	public Class<MaterialWrapper> getRecipeClass() {
//		// TODO 自動生成されたメソッド・スタブ
//		return MaterialWrapper.class;
//	}
//
//
//	@Override
//	public String getRecipeCategoryUid(MaterialWrapper recipe) {
//
//		return JEIUnsagaPlugin.ID_MATERIALLIST;
//	}
//
//	@Override
//	public IRecipeWrapper getRecipeWrapper(MaterialWrapper recipe) {
//		// TODO 自動生成されたメソッド・スタブ
//		return new MaterialListWrapper(recipe);
//	}
//
//	@Override
//	public boolean isRecipeValid(MaterialWrapper recipe) {
//		// TODO 自動生成されたメソッド・スタブ
//		return recipe.getMaterial()!=null && !recipe.getStacks().isEmpty();
//	}
//
//}
