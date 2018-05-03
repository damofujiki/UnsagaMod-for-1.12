//package mods.hinasch.unsaga.plugin.jei;
//
//import java.util.List;
//
//import com.google.common.collect.Lists;
//
//import mezz.jei.api.IJeiHelpers;
//import mezz.jei.api.IJeiRuntime;
//import mezz.jei.api.IModPlugin;
//import mezz.jei.api.IModRegistry;
//import mezz.jei.api.JEIPlugin;
//import mods.hinasch.unsaga.init.UnsagaMaterial;
//import mods.hinasch.unsaga.villager.smith.MaterialTransformer;
//import mods.hinasch.unsagamagic.spell.SpellRegistry;
//
//@JEIPlugin
//public class UnsagaJEIPlugin implements IModPlugin{
//
//
//	private IJeiHelpers helper;
//	@Override
//	public void onRuntimeAvailable(IJeiRuntime paramIJeiRuntime) {
//		// TODO 自動生成されたメソッド・スタブ
//
//	}
//
//	@Override
//	public void register(IModRegistry registry) {
//		helper = registry.getJeiHelpers();
//		registry.addRecipeCategories(new MaterialTransformCategory(helper.getGuiHelper()),new SpellBlendRecipeCategory(helper.getGuiHelper()));
//		registry.addRecipeHandlers(new MaterialTransformRecipeHandler(),new SpellBlendRecipeHandler());
//		List<MaterialTransformRecipe> list = Lists.newArrayList();
//		MaterialTransformer.getAllRecipes().stream().forEach(transIn -> {
//
//			if(transIn.existRecipeItems()){
//				UnsagaMaterial base = transIn.getRequired().getBase();
//				UnsagaMaterial sub = transIn.getRequired().getSub();
//				UnsagaMaterial output = transIn.getTransformResult();
//				sub.getRecipeDisplayItemStacks().forEach(stackIn ->{
//					list.add(new MaterialTransformRecipe(base.getRecipeDisplayItemStack(),stackIn,output.getRecipeDisplayItemStack(),transIn.getProbability()));
//				});
//
//			}
//		});
//		List<SpellBlendRecipe> list2 = Lists.newArrayList();
//		SpellRegistry.instance().getAllBlendsMap().stream().forEach(spellBlendIn -> {
//			spellBlendIn.getRequireMap().entrySet().forEach(entryIn ->{
//				list2.add(new SpellBlendRecipe(entryIn.getKey(),entryIn.getValue(),spellBlendIn));
//			});
//		});
//
//		registry.addRecipes(list);
//		registry.addRecipes(list2);
//	}
//
//}
