package mods.hinasch.unsaga.common.tool;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.item.WeightedRandomStack;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.ability.AbilityAPI;
import mods.hinasch.unsaga.ability.AbilityCapability;
import mods.hinasch.unsaga.init.UnsagaItemRegistry;
import mods.hinasch.unsaga.material.MaterialItemAssociatings;
import mods.hinasch.unsaga.material.RawMaterialRegistry;
import mods.hinasch.unsaga.material.SuitableLists;
import mods.hinasch.unsaga.material.UnsagaMaterial;
import mods.hinasch.unsaga.material.UnsagaMaterialRegistry;
import mods.hinasch.unsaga.material.UnsagaMaterials;
import mods.hinasch.unsaga.util.ToolCategory;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.WeightedRandom;

/**
 * チェストの中身を生成するための基本クラス
 */
public class ItemFactory {
	/** 失敗武器になった場合適合する素材に変わる確率(rand*xx==0)*/
	public static final int FAILED_TRANSFORM = 4;
	public static final int ATTACH_ABILITY = 5;

	public static class WeightedRandomRank extends WeightedRandom.Item{

		public final int number;




		public WeightedRandomRank(int weight,int num) {
			super(weight);
			this.number = num;

		}

	}

	public Random random;
	public ItemFactory(Random rand){
		this.random = rand;
	}
	public NonNullList<ItemStack> createMerchandises(int amount,int generateLevel,final Collection<ToolCategory> availables,final Set<UnsagaMaterial> strictMaterials){


		int minLevel = strictMaterials.stream().mapToInt(in -> in.rank).min().isPresent() ? strictMaterials.stream().mapToInt(in -> in.rank).min().getAsInt() : 3;
		int fixedLevel = minLevel>generateLevel ? minLevel : generateLevel;

		NonNullList<ItemStack> merchandises = ItemUtil.createStackList(amount);
		for(int i=0;i<amount;i++){
//			int rank = random.nextInt(generateLevel);
			List<WeightedRandomStack> weighted = Lists.newArrayList();
			for(int j=0;j<5;j++){
				//取扱素材から選ぶ
				List<UnsagaMaterial> materials = UnsagaMaterialRegistry.instance().getMerchandiseMaterials(0,fixedLevel).stream().filter(in -> strictMaterials.contains(in))
						.collect(Collectors.toList());
				Collections.shuffle(materials, random);
				UnsagaMaterial chosenMaterial = materials.get(0);
				UnsagaMod.logger.trace(this.getClass().getName(), chosenMaterial,materials);
				//取扱カテゴリからひとつ選ぶ
				List<ToolCategory> availablesCopy = Lists.newArrayList(availables);
				Collections.shuffle(availablesCopy,random);


				ToolCategory chosenCategory = availablesCopy.get(0);

				final ItemStack stack = this.orderItem(chosenCategory, chosenMaterial);

//				UnsagaMod.logger.trace(this.getClass().getName(), chosenCategory,availablesCopy);

//				Preconditions.checkNotNull(chosenCategory.getAssociatedItem());
//				UnsagaMod.logger.trace(this.getClass().getName(), chosenCategory.getAssociatedItem());
				weighted.add(new WeightedRandomStack(this.calcWeight(chosenMaterial.rank,fixedLevel),stack));
			}
			ItemStack stack = this.selectRandomItemSafe(weighted, fixedLevel);
//			Preconditions.checkNotNull(stack);
			merchandises.set(i,stack);
		}
//		UnsagaMod.logger.trace("item", merchandises);
		return merchandises;
	}

	private @Nonnull ItemStack selectRandomItemSafe(List<WeightedRandomStack> weighted,int fixedLevel){
		ItemStack stack = WeightedRandom.getRandomItem(random, weighted).is;

//		Preconditions.checkArgument(materials.isEmpty(),rank);


		//ランダムでダメージを与える
		if(this.random.nextInt(10)==0){
			this.applyRandomDamageItemStack(stack);
		}



		if(stack.isEmpty()){
			UnsagaMod.logger.warn("[Warning]Item is null!!", fixedLevel);
			stack = RawMaterialRegistry.instance().debris1.getItemStack(1);
		}
		return stack;
	}
	private ItemStack orderItem(final ToolCategory chosenCategory,final UnsagaMaterial chosenMaterial){
		//生素材の場合
//		UnsagaMaterial finalMaterial = chosenMaterial;

		if(chosenCategory==ToolCategory.RAW_MATERIAL){
			if(MaterialItemAssociatings.instance().getAssociatedStack(chosenMaterial).isEmpty()){
				return new ItemStack(Items.FEATHER);
			}else{
				return MaterialItemAssociatings.instance().getAssociatedStack(chosenMaterial);
			}
		}else{
			Supplier<UnsagaMaterial> finalMaterialSupplier = ()->{
				if(SuitableLists.instance().getSuitables(chosenCategory)!=null){
					//出来損ない武器になってしまう場合、確率で他の適合する素材になる
					if(!SuitableLists.instance().getSuitables(chosenCategory).contains(chosenMaterial) && random.nextInt(FAILED_TRANSFORM)==0){
						List<UnsagaMaterial> list = Lists.newArrayList(SuitableLists.instance().getSuitableMerchadises(chosenCategory));
						Collections.shuffle(list,random);
						return list.isEmpty() ? UnsagaMaterials.FEATHER : list.get(0);
					}

				}
				return chosenMaterial;
			};

			ItemStack stack = UnsagaItemRegistry.createStack(chosenCategory.getAssociatedItem(), finalMaterialSupplier.get(),0);
			if(random.nextInt(ATTACH_ABILITY)==0){
				this.putRandomAbility(stack, random);
			}
			return stack;
		}

	}
	private void putRandomAbility(ItemStack stack,Random rand){
		if(AbilityCapability.adapter.hasCapability(stack)){
			if(AbilityAPI.existLearnableAbility(stack)){
				for(int i=0;i<rand.nextInt(4);i++){
					AbilityAPI.learnRandomAbility(rand, stack);
				}

			}
		}
	}
	private int calcWeight(int rank,int generateLevel){
		if(rank<=6){
			return 100 - rank*rank;
		}
		int b = generateLevel - 9;
		b = b < 0 ? 0 : b;
		int a = 11 - rank + (b*3);
		return a<1 ? 1 : a;
	}

	protected void applyRandomDamageItemStack(ItemStack is){
		if(is.isItemStackDamageable()){
			int maxd = is.getMaxDamage();
			int damage = this.random.nextInt(maxd);
			is.setItemDamage(damage);
		}
	}

//	private int drawRank(int generateLevel,Random random){
//		List<WeightedRandomRank> list = this.prepareWeightedList(generateLevel);
//		WeightedRandomRank w = WeightedRandom.getRandomItem(random, list);
//		return w.number;
//	}
//	private List<WeightedRandomRank> prepareWeightedList(int generateLevel){
//		List<WeightedRandomRank> list = Lists.newArrayList();
//		int a = generateLevel <=2 ? 2 : generateLevel;
//		generateLevel = generateLevel >= 20 ? 20 : generateLevel;
//		for(int i=0;i<a;i++){
//			list.add(this.getWeighted(i, generateLevel));
//		}
//		return list;
//	}
//	private WeightedRandomRank getWeighted(int requireRank,int generateLevel){
//		if(generateLevel<requireRank){
//			return new WeightedRandomRank(1,requireRank);
//		}
//		if(requireRank<=6){
//			int a = -10 * requireRank + 100;
//			return new WeightedRandomRank(a,requireRank);
//		}
//		int b = generateLevel - 9;
//		b = b < 0 ? 0 : b;
//		int a = - 5 * (requireRank -7) + (10 + b * 2);
//		a = a < 1 ? 1 : a;
//		return new WeightedRandomRank(a,requireRank);
//	}
}
