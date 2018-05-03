package mods.hinasch.unsaga.chest.old;

import mods.hinasch.lib.iface.IIntSerializable;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.util.UtilNBT.RestoreFunc;
import mods.hinasch.lib.world.XYZPos;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class FieldChestInfo implements IFieldChest{

	public enum Type implements IIntSerializable{FIELD(1),CAVE(2),STRUCTURE(3),EXPIRED(4),NONE(0);

		private final int meta;

		private Type(int meta){
			this.meta = meta;
		}
		@Override
		public int getMeta() {
			// TODO 自動生成されたメソッド・スタブ
			return meta;
		}

		public static Type fromMeta(int meta){
			return HSLibs.fromMeta(Type.values(), meta);
		}



	};
	public FieldChestInfo(BlockPos position, Type type) {
		super();
		this.position = position;
		this.type = type;
	}

	public static RestoreFunc<IFieldChest> FUNC = in ->{
		int meta = in.getByte("type");
		XYZPos pos = XYZPos.readFromNBT(in);
		return new FieldChestInfo(pos,Type.fromMeta(meta));
	};
	final BlockPos position;
	final Type type;
	boolean isUnloaded = false;
	boolean isSaved = false;
	@Override
	public BlockPos getPos() {
		// TODO 自動生成されたメソッド・スタブ
		return this.position;
	}
	@Override
	public Type getType() {
		// TODO 自動生成されたメソッド・スタブ
		return this.type;
	}
	@Override
	public void writeToNBT(NBTTagCompound stream) {
		// TODO 自動生成されたメソッド・スタブ
		stream.setByte("type", (byte)this.getType().getMeta());
		XYZPos pos = new XYZPos(this.position);
		pos.writeToNBT(stream);
	}
	@Override
	public boolean isScheduledUnloaded() {
		// TODO 自動生成されたメソッド・スタブ
		return this.isUnloaded;
	}
	@Override
	public boolean isSaved() {
		// TODO 自動生成されたメソッド・スタブ
		return this.isSaved;
	}
	@Override
	public void setScheduledUnload(boolean par1) {
		// TODO 自動生成されたメソッド・スタブ
		this.isUnloaded = par1;
	}
	@Override
	public void setSaved(boolean par1) {
		// TODO 自動生成されたメソッド・スタブ
		this.isSaved = par1;
	}



}
