package mods.hinasch.unsaga.core.item.misc;

import mods.hinasch.lib.util.Statics;
import mods.hinasch.unsaga.material.RawMaterialRegistry;
import mods.hinasch.unsaga.material.RawMaterialRegistry.RawMaterial;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemRawMaterialNew extends Item implements IItemColor{

	final RawMaterial material;

	public ItemRawMaterialNew(RawMaterial material){
		this.material = material;
		this.setUnlocalizedName(material.getPropertyName());
	}

	public RawMaterial getMaterial(){
		return this.material;
	}
	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex) {
		if(RawMaterialRegistry.instance().getPropertyFromStack(stack)!=null){
			RawMaterial prop = RawMaterialRegistry.instance().getPropertyFromStack(stack);
			if(prop.isItemColored()){
				return prop.getAssociatedMaterial().getMaterialColor();
			}
		}
		return Statics.COLOR_NONE;
	}


}
