package mods.hinasch.unsaga.common;

import java.util.Map;
import java.util.function.Predicate;

import com.google.common.collect.Maps;

import mods.hinasch.unsaga.villager.bartering.BarteringItemRegistry.PredicateArmorMaterial;
import mods.hinasch.unsaga.villager.bartering.BarteringItemRegistry.PredicateToolMaterial;
import mods.hinasch.unsaga.villager.smith.SmithMaterialRegistry.PredicateItem;
import mods.hinasch.unsaga.villager.smith.SmithMaterialRegistry.PredicateOre;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public abstract class LibraryRegistry<V> {

	protected Map<Predicate<ItemStack>,V> materialRegistry = Maps.<Predicate<ItemStack>,V>newHashMap();

	public abstract V preRegister(Object... in);

	public void add(Predicate<ItemStack> item,Object... price){
		this.materialRegistry.put(item, this.preRegister(price));
	}
	public void add(Item item,Object... price){
		this.materialRegistry.put(new PredicateItem(item,OreDictionary.WILDCARD_VALUE), this.preRegister(price));
	}
	public void add(String string,Object... price){
		this.materialRegistry.put(new PredicateOre(string), this.preRegister(price));
	}
	public void addBlockAll(Block block,Object... price){
		this.addBlock(block,OreDictionary.WILDCARD_VALUE, price);
	}
	public void addBlock(Block block,int damage,Object... price){
		this.materialRegistry.put(new PredicateItem(Item.getItemFromBlock(block),damage), this.preRegister(price));
	}
	public void add(ToolMaterial tm,Object... price){
		this.materialRegistry.put(new PredicateToolMaterial(tm), this.preRegister(price));
	}

	public void add(ArmorMaterial am,Object... price){
		this.materialRegistry.put(new PredicateArmorMaterial(am), this.preRegister(price));
	}
}
