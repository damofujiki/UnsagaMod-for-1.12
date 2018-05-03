package mods.hinasch.unsaga.villager.bartering;

import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.function.Predicate;

import com.google.common.collect.Maps;

import mods.hinasch.unsaga.init.UnsagaItems;
import mods.hinasch.unsaga.material.UnsagaMaterials;
import mods.hinasch.unsaga.villager.smith.SmithMaterialRegistry.PredicateBase;
import mods.hinasch.unsaga.villager.smith.SmithMaterialRegistry.PredicateItem;
import mods.hinasch.unsaga.villager.smith.SmithMaterialRegistry.PredicateOre;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.oredict.OreDictionary;

public class BarteringItemRegistry {


	Map<Predicate<ItemStack>,Integer> materialRegistry = Maps.<Predicate<ItemStack>,Integer>newHashMap();

	private static BarteringItemRegistry INSTANCE;

	private BarteringItemRegistry(){

	}
	public static BarteringItemRegistry instance(){
		if(INSTANCE == null){
			INSTANCE = new BarteringItemRegistry();
		}
		return INSTANCE;
	}

	public void register(){
		this.add(Items.EMERALD, 10000);
		this.add(Blocks.STONE, UnsagaMaterials.DEBRIS1.price*4);
		this.add(Blocks.GLASS, 1000);
		this.add(Blocks.WOOL, 200);
		this.add(Items.STRING, 200);
		this.add(Items.WHEAT, 200);
		this.add(Items.COAL, (int)(UnsagaMaterials.OAK.price*1.2F));
		this.add(Items.BEEF, 600);
		this.add(Items.COOKED_BEEF, 400);
		this.add(Items.CHICKEN, 300);
		this.add(Items.COOKED_CHICKEN, 300);
		this.add(Items.BEETROOT, 150);
		this.add(Items.CARROT, 100);
		this.add(Items.BLAZE_ROD,10000);
		this.add(Items.BLAZE_POWDER, 2000);
		this.add(Items.ENDER_PEARL, 10000);
		this.add(Items.LEATHER, 700);
		this.add(Items.POTATO, 60);
		this.add(Items.BAKED_POTATO, 120);
		this.add(Items.FISH, 80);

		this.add(Items.COOKED_FISH, 300);
		this.add(Items.ROTTEN_FLESH, 1);
		this.add(Items.POISONOUS_POTATO, 1);
		this.add(Items.BOAT, (int)(UnsagaMaterials.OAK.price*1.5F));
		this.add(Items.PORKCHOP, 250);
		this.add(Items.COOKED_PORKCHOP, 500);
		this.add(ArmorMaterial.GOLD,UnsagaMaterials.GOLD.price);
		this.add(ToolMaterial.GOLD,UnsagaMaterials.GOLD.price);
		this.add(ArmorMaterial.IRON,UnsagaMaterials.IRON.price);
		this.add(ToolMaterial.IRON,UnsagaMaterials.IRON.price);
		this.add(ArmorMaterial.LEATHER,500);
		this.add("ingotZinc", 1000);
		this.add("ingotBismuth", 1500);
		this.add("ingotBrass", 4000);
		this.add("ingotBronze", 3000);
		this.add("ingotSUS", 6000);
		this.add("record", 10000);
		this.add("ingotTitanium", 6000);
		this.add(UnsagaItems.MUSKET, 1000);

	}

	public OptionalInt find(ItemStack is){
		if(this.materialRegistry.keySet().stream().anyMatch(in -> in.test(is))){
			return this.materialRegistry.keySet().stream().filter(in -> in.test(is)).mapToInt(in -> this.materialRegistry.get(in)).findFirst();
		}
		return OptionalInt.empty();
	}
	public void add(Predicate<ItemStack> item,int price){
		this.materialRegistry.put(item, price);
	}
	public void add(Item item,int price){
		this.materialRegistry.put(new PredicateItem(item,OreDictionary.WILDCARD_VALUE), price);
	}
	public void add(String string,int price){
		this.materialRegistry.put(new PredicateOre(string), price);
	}
	public void add(Block block,int price){
		this.add(block,OreDictionary.WILDCARD_VALUE, price);
	}
	public void add(Block block,int damage,int price){
		this.materialRegistry.put(new PredicateItem(Item.getItemFromBlock(block),damage), price);
	}
	public void add(ToolMaterial tm,int price){
		this.materialRegistry.put(new PredicateToolMaterial(tm), price);
	}

	public void add(ArmorMaterial am,int price){
		this.materialRegistry.put(new PredicateArmorMaterial(am), price);
	}
	public static class PredicateArmorMaterial extends PredicateBase<ArmorMaterial>{

		public PredicateArmorMaterial(ArmorMaterial obj) {
			super(obj);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
		public List<ItemStack> getItemStack() {
			// TODO 自動生成されたメソッド・スタブ
			return null;
		}

		@Override
		public boolean test(ItemStack t) {
			if(t.getItem() instanceof ItemArmor){
				ItemArmor tool = (ItemArmor) t.getItem();
				return tool.getArmorMaterial()==this.object;
			}
			return false;
		}

	}
	public static class PredicateToolMaterial extends PredicateBase<ToolMaterial>{

		public PredicateToolMaterial(ToolMaterial obj) {
			super(obj);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
		public List<ItemStack> getItemStack() {
			// TODO 自動生成されたメソッド・スタブ
			return null;
		}

		@Override
		public boolean test(ItemStack t) {
			if(t.getItem() instanceof ItemTool){
				ItemTool tool = (ItemTool) t.getItem();
				return tool.getToolMaterialName().equals(this.object.toString());
			}
			return false;
		}

	}
}
