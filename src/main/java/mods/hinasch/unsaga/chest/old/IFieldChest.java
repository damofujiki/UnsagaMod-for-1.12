package mods.hinasch.unsaga.chest.old;

import mods.hinasch.lib.iface.INBTWritable;
import mods.hinasch.unsaga.chest.old.FieldChestInfo.Type;
import net.minecraft.util.math.BlockPos;

public interface IFieldChest extends INBTWritable{
	public BlockPos getPos();
	public Type getType();
	public boolean isScheduledUnloaded();
	public boolean isSaved();
	public void setScheduledUnload(boolean par1);
	public void setSaved(boolean par1);
}
