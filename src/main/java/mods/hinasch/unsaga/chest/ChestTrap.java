package mods.hinasch.unsaga.chest;

import mods.hinasch.lib.iface.INBTWritable;
import mods.hinasch.lib.registry.PropertyElementBase;
import mods.hinasch.lib.util.UtilNBT.RestoreFunc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;


public abstract class ChestTrap extends PropertyElementBase implements INBTWritable{

	public static RestoreFunc<ChestTrap> RESTORE = new RestoreFunc<ChestTrap>(){

		@Override
		public ChestTrap apply(NBTTagCompound input) {
			if(input.hasKey("key")){
				String key = input.getString("key");
				return ChestTraps.getTrap(new ResourceLocation(key));
			}


			return ChestTraps.DUMMY;
		}
	};

	public ChestTrap(Integer number,String name){
		super(new ResourceLocation(name),name);

	}
	public abstract void activate(IChestBehavior chest,EntityPlayer ep);

	@Override
	public Class getParentClass() {
		// TODO 自動生成されたメソッド・スタブ
		return ChestTrap.class;
	}
	@Override
	public void writeToNBT(NBTTagCompound stream) {
		stream.setString("key", this.getKey().getResourcePath());

	}
}
