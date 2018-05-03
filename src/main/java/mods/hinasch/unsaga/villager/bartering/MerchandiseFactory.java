package mods.hinasch.unsaga.villager.bartering;

import java.util.Collection;
import java.util.Random;
import java.util.Set;

import mods.hinasch.unsaga.common.tool.ItemFactory;
import mods.hinasch.unsaga.material.UnsagaMaterial;
import mods.hinasch.unsaga.util.ToolCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class MerchandiseFactory extends ItemFactory{

	public MerchandiseFactory(Random rand) {
		super(rand);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public NonNullList<ItemStack> createMerchandises(int amount,int generateLevel,Collection<ToolCategory> availables,Set<UnsagaMaterial> strictMaterials){
		NonNullList<ItemStack> stacks = super.createMerchandises(amount, generateLevel, availables, strictMaterials);
		stacks.forEach(in ->{

			//売品認識タグをつける
			if(MerchandiseCapability.adapter.hasCapability(in)){
				MerchandiseCapability.adapter.getCapability(in).setMerchandise(true);
			}

		});

		return stacks;
	}
}
