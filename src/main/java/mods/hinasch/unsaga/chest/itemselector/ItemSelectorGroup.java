//package mods.hinasch.unsaga.chest.itemselector;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import com.google.common.collect.Lists;
//
//import mods.hinasch.lib.WeightedRandomItem;
//import mods.hinasch.lib.item.WeightedRandomStack;
//import mods.hinasch.unsaga.UnsagaMod;
//import mods.hinasch.unsaga.ability.AbilityHelper;
//import mods.hinasch.unsaga.capability.IUnsagaPropertyItem;
//import mods.hinasch.unsaga.init.MiscItemRegistry;
//import mods.hinasch.unsaga.init.UnsagaItems.EnumItemSelector;
//import mods.hinasch.unsaga.util.ToolCategory;
//import mods.hinasch.unsaga.init.UnsagaMaterials;
//import mods.hinasch.unsagamagic.item.ItemTabletOld;
//import net.minecraft.init.Items;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.math.MathHelper;
//
//public class ItemSelectorGroup {
//
//	public static final int RANKMAX = 9;
//	public static final int WEIGHT_MIN = 3;
//
//	/**
//	 * 素材から選択
//	 */
//	public static ItemSelector selectorMaterial = new ItemSelector(8){
//
//
//		@Override
//		public List<WeightedRandomStack> getItems(final int level,Random rand) {
//			List<WeightedRandomStack> weightedList = UnsagaMaterials.getMaterialItemExchangeMap().getKeys().stream()
//					.filter(materialIn -> UnsagaMaterials.materialToItem.containsKey(materialIn))
//					.filter(materialIn -> materialIn.rank<=RANKMAX)
//					.map(materialIn -> {
//						ItemStack is = UnsagaMaterials.materialToItem.getObject(materialIn).asItemStack();
//
//							return new WeightedRandomStack(calcWeightFromRank(materialIn.rank,level),is);
//
////						return new WeightedRandomStack(calcWeightFromRank(1),UnsagaMod.miscItems.debris.getItemStack(1));
//					}).collect(Collectors.toList());
//
//
//			//			for(Iterator<UnsagaMaterial> ite=Unsaga.materials.getAllMaterialValues().iterator();ite.hasNext();){
//			//				UnsagaMaterial us = ite.next();
//			//				if(us.getAssociatedItemStack().isPresent()){
//			//					ItemStack is = us.getAssociatedItemStack().get();
//			//					if(us.rank<level/10+2){
//			//						items.add(new WeightedRandomItemStack(calcWeightFromRank(us.rank),is));
//			//					}
//			//
//			//				}
//			//			}
//			if(weightedList.isEmpty()){
//				weightedList.add(new WeightedRandomStack(60,MiscItemRegistry.instance().debris.getItemStack(1)));
//			}
//			return weightedList;
//		}
//
//
//
//
//	};
//
//	/**
//	 * 全ツールから
//	 */
//	public static ItemSelector selectorTool = new ItemSelector(6){
//
//		@Override
//		public List<WeightedRandomStack> getItems(final int level,final Random rand) {
//			List<WeightedRandomStack> items = Stream.generate(() -> {
//				int min = 0;
//				int max = level/10 + 3;
//
//				ItemStack is = UnsagaMod.items.getRandomTool(rand,min,max , EnumItemSelector.ALL);
//
//				if(AbilityHelper.hasCapability(is)){
//					IUnsagaPropertyItem capa = AbilityHelper.getCapability(is);
//					return new WeightedRandomStack(calcWeightFromRank(capa.getUnsagaMaterial().getRank(),level),is);
//				}
//				ItemStack def = UnsagaMod.items.getItemStack(ToolCategory.SWORD, UnsagaMod.materials.stone, 1, 0);
//				return new WeightedRandomStack(calcWeightFromRank(UnsagaMod.materials.stone.rank,level),def);
//			}).limit(15).collect(Collectors.toList());
////			List<WeightedRandomStack> items = IntStream.range(0, 15).mapToObj(num ->{
////				int min = 0;
////				int max = level/10 + 3;
////
////				ItemStack is = UnsagaMod.items.getRandomTool(rand,min,max , EnumItemSelector.ALL);
////
////				if(AbilityHelperNew.hasCapability(is)){
////					IUnsagaPropertyItem capa = AbilityHelperNew.getCapability(is);
////					return new WeightedRandomStack(calcWeightFromRank(capa.getUnsagaMaterial().getRank()),is);
////				}
////				return new WeightedRandomStack(calcWeightFromRank(1),UnsagaMod.miscItems.debris.getItemStack(1));
////			}).collect(Collectors.toList());
//
//
//			if(items.isEmpty()){
//				ItemStack stack = new ItemStack(Items.STONE_SWORD,1);//Unsaga.items.getFailedWeapon(rand, ToolCategory.SWORD,null);
//				items.add(new WeightedRandomStack(60,stack));
//			}
//			return items;
//		}
//
//	};
//
//	public static ItemSelector selectorMagicTool = new ItemSelector(6){
//
//		@Override
//		public List<WeightedRandomStack> getItems(final int level,final Random rand) {
//
//			List<WeightedRandomStack> items = Stream.generate(() ->{
//				ItemStack is = UnsagaMod.items.getRandomTool(rand,0,100 , EnumItemSelector.MAGIC);
//
//				if(AbilityHelper.hasCapability(is)){
//					IUnsagaPropertyItem capa = AbilityHelper.getCapability(is);
//					return new WeightedRandomStack(calcWeightFromRank(capa.getUnsagaMaterial().getRank(),level),is);
//				}
//				return new WeightedRandomStack(calcWeightFromRank(1,level),UnsagaMod.items.getItemStack(ToolCategory.KNIFE, UnsagaMod.materials.carnelian,1,0));
//			}).limit(15).collect(Collectors.toList());
////			items = IntStream.range(0, 15).map(new Function<Integer,WeightedRandomStack>(){
////
////				@Override
////				public WeightedRandomStack apply(Integer num) {
////					ItemStack is = UnsagaMod.items.getRandomTool(rand,0,100 , EnumItemSelector.MAGIC);
////
////					if(AbilityHelperNew.hasCapability(is)){
////						IUnsagaPropertyItem capa = AbilityHelperNew.getCapability(is);
////						return new WeightedRandomStack(calcWeightFromRank(capa.getUnsagaMaterial().getRank()),is);
////					}
////					return new WeightedRandomStack(calcWeightFromRank(1),UnsagaMod.miscItems.debris.getItemStack(1));
////				}}).getList();
//
//			if(items.isEmpty()){
//				ItemStack stack = new ItemStack(Items.STONE_SWORD,1);//Unsaga.items.getFailedWeapon(rand, ToolCategory.SWORD,null);
//				items.add(new WeightedRandomStack(60,stack));
//			}
//			return items;
//		}
//
//	};
//
//
//	public static int calcWeightFromRank(int rank,int level){
//		int initial = RANKMAX + 1;
//		float picth = (110-MathHelper.clamp_int(level, 1, 100))*0.1F;
//		float var2 = (float)(rank-initial);
//		var2 *= var2;
//		float weight = picth*var2+WEIGHT_MIN;
//		//補正
//		if(level<15){
//			if(rank>5){
//				weight -= 15.0D;
//			}
//		}
//		if(level>30){
//			if(rank<=1){
//				weight -= 5.0D;
//			}
//		}
//		if(level>60){
//			if(rank<=1){
//				weight -= 5.0D;
//			}
//		}
//		if(level>90){
//			if(rank<=1){
//				weight -= 5.0D;
//			}
//		}
//		return MathHelper.clamp_int((int)weight, WEIGHT_MIN, 100);
//	}
//
//	/**
//	 * 金塊・タブレットから選択。宝箱用
//	 */
//	public static ItemSelector selectorOtherItem = new ItemSelector(5){
//
//		@Override
//		public List<WeightedRandomStack> getItems(int level, Random rand) {
//			List<WeightedRandomStack> items = new ArrayList();
//			items.add(new WeightedRandomStack(60,new ItemStack(Items.GOLD_NUGGET,1)));
//			items.add(new WeightedRandomStack(15,new ItemStack(UnsagaMod.items.musket,1)));
//			ItemStack tablet = ItemTabletOld.getRandomTablet(rand);//TabletHelper.getRandomMagicTablet(rand);
//			items.add(new WeightedRandomStack(5,tablet));
//			return items;
//		}
//
//	};
//
//
//	/** チェスト用のアイテムセレクターを詰めたリスト*/
//	public static List<ItemSelector> chestItemSelectorList = Lists.newArrayList(selectorMaterial,selectorTool,selectorOtherItem);
//
//	public static List<WeightedRandomSelector> toWeighted(List<ItemSelector> listIn){
//		return listIn.stream().map(selectorIn -> new WeightedRandomSelector(selectorIn.weight,selectorIn)).collect(Collectors.toList());
//	}
//	public static class WeightedRandomSelector extends WeightedRandomItem<ItemSelector>{
//
//		public WeightedRandomSelector(int itemWeightIn, ItemSelector item) {
//			super(itemWeightIn, item);
//			// TODO 自動生成されたコンストラクター・スタブ
//		}
//
//
//	}
//
//
//}
