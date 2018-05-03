package mods.hinasch.unsaga.chest;

import java.util.Optional;

import javax.annotation.Nullable;

import mods.hinasch.lib.iface.INBTWritable;
import mods.hinasch.lib.util.UtilNBT.RestoreFunc;
import mods.hinasch.lib.world.XYZPos;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class ChunkChestInfo implements INBTWritable{

	public static RestoreFunc<ChunkChestInfo> RESTORE_FUC = in ->{
		ChunkChestInfo info = new ChunkChestInfo();
		BlockPos pos = XYZPos.RESTORE_TO_BLOCKPOS.apply(in);
		if(pos!=null){
			info.setChestPos(pos);
		}
		int type = in.getInteger("chestType");
		info.setFieldChestType(FieldChestType.fromMeta(type));
		info.setHasInitialized(in.getBoolean("initialized"));
		return info;
	};
	FieldChestType fieldChestType = FieldChestType.NORMAL;
	Optional<BlockPos> blockPos = Optional.empty();
	boolean hasInitialized = false;
	public FieldChestType getFieldChestType() {
		return fieldChestType;
	}
	public void setFieldChestType(FieldChestType fieldChestType) {
		this.fieldChestType = fieldChestType;
	}
	public Optional<BlockPos> getChestPos() {
		return blockPos;
	}
	public void setChestPos(@Nullable BlockPos blockPos) {
		if(blockPos!=null){
			this.blockPos = Optional.of(blockPos);
		}else{
			this.blockPos = Optional.empty();
		}

	}
	public boolean hasInitialized() {
		return hasInitialized;
	}
	public void setHasInitialized(boolean hasInitialized) {
		this.hasInitialized = hasInitialized;
	}
	@Override
	public void writeToNBT(NBTTagCompound stream) {
		// TODO 自動生成されたメソッド・スタブ
		stream.setInteger("chestType", this.getFieldChestType().getMeta());
		if(this.getChestPos().isPresent()){
			XYZPos pos = new XYZPos(this.getChestPos().get());
			pos.writeToNBT(stream);
		}
		stream.setBoolean("initialized", hasInitialized);
	}


}
