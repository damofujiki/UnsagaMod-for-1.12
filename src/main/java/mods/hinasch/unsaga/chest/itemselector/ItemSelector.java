package mods.hinasch.unsaga.chest.itemselector;

import java.util.List;
import java.util.Random;

import mods.hinasch.lib.item.WeightedRandomStack;

public abstract class ItemSelector {

	public int weight;

	public ItemSelector(int weight){
		this.weight = weight;
	}

	public abstract List<WeightedRandomStack> getItems(int level,Random rand);


}
