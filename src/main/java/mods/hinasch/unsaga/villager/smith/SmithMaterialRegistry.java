package mods.hinasch.unsaga.villager.smith;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.common.LibraryRegistry;
import mods.hinasch.unsaga.init.UnsagaItems;
import mods.hinasch.unsaga.material.UnsagaMaterial;
import mods.hinasch.unsaga.material.UnsagaMaterialRegistry;
import mods.hinasch.unsaga.material.UnsagaMaterials;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class SmithMaterialRegistry extends LibraryRegistry<SmithMaterialRegistry.Info>{

	UnsagaMaterialRegistry reg = UnsagaMaterialRegistry.instance();
//	Map<Predicate<ItemStack>,Info> materialRegistry = Maps.<Predicate<ItemStack>,Info>newHashMap();
	protected static SmithMaterialRegistry INSTANCE;

	public static SmithMaterialRegistry instance(){
		if(INSTANCE == null){
			INSTANCE = new SmithMaterialRegistry();
		}
		return INSTANCE;
	}

	protected SmithMaterialRegistry(){

	}
	public void register(){
		this.add(Items.STICK,UnsagaMaterials.WOOD, 0.125F);
		this.add(Items.BOW, UnsagaMaterials.WOOD, 1.0F);
		this.addBlockAll(Blocks.IRON_BARS,UnsagaMaterials.IRON,1.0F);
		this.add(Items.BUCKET, UnsagaMaterials.IRON, 1.0F);
		this.add(Items.WOODEN_AXE, UnsagaMaterials.WOOD, 1.0F);
		this.add(Items.WOODEN_HOE, UnsagaMaterials.WOOD, 1.0F);
		this.add(Items.WOODEN_PICKAXE, UnsagaMaterials.WOOD, 1.0F);
		this.add(Items.WOODEN_SHOVEL, UnsagaMaterials.WOOD, 1.0F);
		this.add(Items.WOODEN_SWORD, UnsagaMaterials.WOOD, 1.0F);
		this.add(Items.IRON_AXE, UnsagaMaterials.IRON, 1.0F);
		this.add(Items.IRON_HOE, UnsagaMaterials.IRON, 1.0F);
		this.add(Items.IRON_HORSE_ARMOR, UnsagaMaterials.IRON, 1.0F);
		this.add(Items.IRON_PICKAXE, UnsagaMaterials.IRON, 1.0F);
		this.add(Items.IRON_SHOVEL, UnsagaMaterials.IRON, 1.0F);
		this.add(Items.IRON_SWORD, UnsagaMaterials.IRON, 1.0F);
		this.add(Items.IRON_BOOTS, UnsagaMaterials.IRON, 1.0F);
		this.add(Items.IRON_CHESTPLATE, UnsagaMaterials.IRON, 1.0F);
		this.add(Items.IRON_HELMET, UnsagaMaterials.IRON, 1.0F);
		this.add(Items.IRON_LEGGINGS, UnsagaMaterials.IRON, 1.0F);
		this.add(Items.IRON_DOOR, UnsagaMaterials.IRON, 1.0F);
		this.add(Items.STONE_AXE, UnsagaMaterials.STONE, 1.0F);
		this.add(Items.STONE_HOE, UnsagaMaterials.STONE, 1.0F);
		this.add(Items.STONE_PICKAXE, UnsagaMaterials.STONE, 1.0F);
		this.add(Items.STONE_SHOVEL, UnsagaMaterials.STONE, 1.0F);
		this.add(Items.STONE_SWORD, UnsagaMaterials.STONE, 1.0F);
		this.addBlock(Blocks.PLANKS,BlockPlanks.EnumType.OAK.getMetadata(),UnsagaMaterials.OAK,0.25F);
		this.add(Items.OAK_DOOR, UnsagaMaterials.OAK, 0.5F);
		this.addBlock(Blocks.PLANKS,BlockPlanks.EnumType.BIRCH.getMetadata(),UnsagaMaterials.BIRCH,0.25F);
		this.add(Items.BIRCH_DOOR, UnsagaMaterials.BIRCH, 0.5F);
		this.addBlock(Blocks.PLANKS,BlockPlanks.EnumType.SPRUCE.getMetadata(),UnsagaMaterials.SPRUCE,0.25F);
		this.add(Items.SPRUCE_DOOR, UnsagaMaterials.SPRUCE, 0.5F);
		this.addBlock(Blocks.PLANKS,BlockPlanks.EnumType.JUNGLE.getMetadata(),UnsagaMaterials.JUNGLE_WOOD,0.25F);
		this.add(Items.JUNGLE_DOOR, UnsagaMaterials.JUNGLE_WOOD, 0.5F);
		this.addBlock(Blocks.PLANKS,BlockPlanks.EnumType.ACACIA.getMetadata()-4,UnsagaMaterials.ACACIA,0.25F);
		this.add(Items.ACACIA_DOOR, UnsagaMaterials.ACACIA, 0.5F);
		this.addBlock(Blocks.PLANKS,BlockPlanks.EnumType.DARK_OAK.getMetadata()-4,UnsagaMaterials.DARK_OAK,0.25F);
		this.add(Items.DARK_OAK_DOOR, UnsagaMaterials.DARK_OAK, 0.5F);
		this.addBlock(Blocks.LOG,BlockPlanks.EnumType.OAK.getMetadata(),UnsagaMaterials.OAK,1.0F);
		this.addBlock(Blocks.LOG,BlockPlanks.EnumType.BIRCH.getMetadata(),UnsagaMaterials.BIRCH,1.0F);
		this.addBlock(Blocks.LOG,BlockPlanks.EnumType.SPRUCE.getMetadata(),UnsagaMaterials.SPRUCE,1.0F);
		this.addBlock(Blocks.LOG,BlockPlanks.EnumType.JUNGLE.getMetadata(),UnsagaMaterials.JUNGLE_WOOD,1.0F);
		this.addBlock(Blocks.LOG2,BlockPlanks.EnumType.ACACIA.getMetadata(),UnsagaMaterials.ACACIA,1.0F);
		this.addBlock(Blocks.LOG2,BlockPlanks.EnumType.DARK_OAK.getMetadata(),UnsagaMaterials.DARK_OAK,1.0F);
		this.add("ingotIron", UnsagaMaterials.IRON,0.5F);
		this.add("oreIron", UnsagaMaterials.IRON_ORE, 0.5F);
		this.add("ingotCopper", UnsagaMaterials.COPPER, 0.5F);
		this.add("oreCopper", UnsagaMaterials.COPPER_ORE, 0.5F);
		this.add("gemRuby", UnsagaMaterials.RUBY, 0.5F);
		this.add("gemSapphire", UnsagaMaterials.SAPPHIRE,0.5F);
		this.add("ingotDamuscus", UnsagaMaterials.DAMASCUS, 0.5F);
		this.add("ingotOsmium", UnsagaMaterials.OSMIUM, 0.5F);
		this.add("ingotGold", UnsagaMaterials.GOLD, 0.5F);
		this.add("ingotSteel", UnsagaMaterials.STEEL1, 0.5F);
		this.add("ingotBrass", UnsagaMaterials.BRASS, 0.5F);
		this.add("ingotSilver", UnsagaMaterials.SILVER, 0.5F);
		this.add("ingotLead", UnsagaMaterials.LEAD, 0.5F);
		this.add("nuggetGold", UnsagaMaterials.GOLD,0.052F);
		this.add("gemChalcedonyBlue", UnsagaMaterials.CHALCEDONY, 0.5F);
		this.add("gemChalcedonyWhite", UnsagaMaterials.CHALCEDONY, 0.5F);
		this.add("gemChalcedonyRed", UnsagaMaterials.CARNELIAN, 0.5F);
		this.add("gemQuartz", UnsagaMaterials.QUARTZ, 0.5F);
		this.add("gemChalcedonyRed", UnsagaMaterials.CARNELIAN, 0.5F);
		this.add("gemLapis", UnsagaMaterials.LAZULI, 0.5F);
		this.add("gemOpal", UnsagaMaterials.OPAL, 0.5F);
		this.add("gemTopaz", UnsagaMaterials.TOPAZ, 0.5F);
		this.add("oreSerpentine", UnsagaMaterials.SERPENTINE, 0.5F);
		this.addBlockAll(Blocks.COBBLESTONE,UnsagaMaterials.DEBRIS1,0.5F);
		this.add(UnsagaItems.MUSKET,UnsagaMaterials.IRON ,0.5F);
	}

	public List<Predicate<ItemStack>> findByMaterial(UnsagaMaterial m){
		return this.materialRegistry.entrySet().stream().filter(in -> in.getValue().getMaterial()==m).map(in -> in.getKey()).collect(Collectors.toList());
	}

	public List<ItemStack> findItemStacksByMaterial(UnsagaMaterial m){
		return this.findByMaterial(m).stream().filter(in -> in instanceof IGetItemStack).map(in -> (IGetItemStack)in)
		.flatMap(in -> in.getItemStack().stream()).collect(Collectors.toList());
	}
	public Optional<Info> find(@Nullable ItemStack is){
		if(is!=null){
			return this.materialRegistry.entrySet().stream().filter(in -> in.getKey().test(is)).map(in -> in.getValue()).findFirst();
		}
		return Optional.empty();
	}

//	public void add(Predicate<ItemStack> item,UnsagaMaterial m,float f){
//		this.materialRegistry.put(item, new Info(m,f));
//	}
//	public void add(Item item,UnsagaMaterial m,float f){
//		this.materialRegistry.put(new PredicateItem(item,OreDictionary.WILDCARD_VALUE), new Info(m,f));
//	}
//	public void add(String string,UnsagaMaterial m,float f){
//		this.materialRegistry.put(new PredicateOre(string), new Info(m,f));
//	}
//	public void add(Block block,UnsagaMaterial m,float f){
//		this.add(block,OreDictionary.WILDCARD_VALUE, m, f);
//	}
//	public void add(Block block,int damage,UnsagaMaterial m,float f){
//		this.materialRegistry.put(new PredicateItem(Item.getItemFromBlock(block),damage), new Info(m,f));
//	}

	public static class PredicateOre extends PredicateBase<String>{

//		final String ore;

		public PredicateOre(String ore){
			super(ore);
		}
		@Override
		public boolean test(ItemStack t) {
			return HSLibs.getOreNames(t).stream().anyMatch(in -> in.equals(object));
		}
		@Override
		public List<ItemStack> getItemStack() {
			// TODO 自動生成されたメソッド・スタブ
			return OreDictionary.getOres(this.object);
		}

	}
	public static class PredicateItem extends PredicateBase<Item>{


		final int damage;

		public PredicateItem(Item item,int damage){
			super(item);
			this.damage = damage;
		}
		@Override
		public boolean test(ItemStack t) {
			if(damage==OreDictionary.WILDCARD_VALUE){
				return t.getItem() == this.object;
			}
			return t.getItem()==this.object && t.getItemDamage() == damage;
		}
		@Override
		public List<ItemStack> getItemStack() {
			if(damage==OreDictionary.WILDCARD_VALUE){
				return Lists.newArrayList(new ItemStack(object));
			}
			return Lists.newArrayList(new ItemStack(object,1,damage));
		}


	}

	public static class Info{

		final float amount;
		public float getAmount() {
			return amount;
		}

		public UnsagaMaterial getMaterial() {
			return material;
		}

		final UnsagaMaterial material;

		public Info(UnsagaMaterial m,float am){
			this.amount = am;
			this.material = m;
		}
	}

	public static abstract class PredicateBase<T> implements Predicate<ItemStack>,IGetItemStack{

		public final T object;

		public PredicateBase(T obj){
			this.object = obj;
		}



	}
	public static interface IGetItemStack{

		public List<ItemStack> getItemStack();
	}
	@Override
	public Info preRegister(Object... in) {
		// TODO 自動生成されたメソッド・スタブ
		return new Info((UnsagaMaterial)in[0],(Float)in[1]);
	}
}
