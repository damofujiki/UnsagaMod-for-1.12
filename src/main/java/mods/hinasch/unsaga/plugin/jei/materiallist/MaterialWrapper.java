package mods.hinasch.unsaga.plugin.jei.materiallist;

import java.util.List;

import com.google.common.collect.Lists;

import mods.hinasch.unsaga.material.MaterialItemAssociatings;
import mods.hinasch.unsaga.material.UnsagaMaterial;
import mods.hinasch.unsaga.minsaga.MinsagaMaterial;
import mods.hinasch.unsaga.villager.smith.SmithMaterialRegistry;
import mods.hinasch.unsaga.villager.smith.SmithMaterialRegistry.IGetItemStack;
import net.minecraft.item.ItemStack;

public class MaterialWrapper<T> implements Comparable<MaterialWrapper>{


	final public T material;
//	final public MinsagaForging.Material materialMinsaga;
	public List<ItemStack> itemStack;

	public MaterialWrapper(T mate){
		this.material = mate;
		if(mate instanceof UnsagaMaterial){
			UnsagaMaterial m = (UnsagaMaterial) mate;
			this.itemStack = Lists.newArrayList();
			ItemStack is = MaterialItemAssociatings.instance().getAssociatedStack(m);
			if(is!=null){
				this.itemStack.add(is);
			}
			if(!SmithMaterialRegistry.instance().findItemStacksByMaterial(m).isEmpty()){
				this.itemStack.addAll(SmithMaterialRegistry.instance().findItemStacksByMaterial(m));
			}
		}
		if(mate instanceof MinsagaMaterial){
			MinsagaMaterial m = (MinsagaMaterial) mate;
			if(m.checker() instanceof IGetItemStack){
				this.itemStack = ((IGetItemStack)m.checker()).getItemStack();
			}else{
				this.itemStack = Lists.newArrayList();
			}
		}

	}

	public T getMaterial(){
		return this.material;

	}


	public List<ItemStack> getStacks(){
		return this.itemStack;
	}

	@Override
	public int compareTo(MaterialWrapper o) {
		if(this.material instanceof UnsagaMaterial && o.getMaterial() instanceof UnsagaMaterial){
			return ((UnsagaMaterial)this.material).compareTo((UnsagaMaterial) o.getMaterial());
		}
		if(this.material instanceof UnsagaMaterial && o.getMaterial() instanceof MinsagaMaterial){
			return -1;
		}
		if(o.getMaterial() instanceof UnsagaMaterial && this.getMaterial() instanceof MinsagaMaterial){
			return 1;
		}
		return 0;
	}
}
