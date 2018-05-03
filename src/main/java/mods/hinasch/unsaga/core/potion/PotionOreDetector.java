package mods.hinasch.unsaga.core.potion;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;

public class PotionOreDetector extends PotionBuff {

	protected PotionOreDetector(String name, int u, int v) {
		super(name, u, v);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public static class Effect extends PotionEffect{

		public Effect(Potion potionIn, int durationIn,BlockPos origin,List<BlockPos> orePosList) {
			super(potionIn, durationIn);
			if(this.origin.isPresent()){
				this.origin = Optional.of(origin);
			}
			this.orePosList = orePosList;
		}

		Optional<BlockPos> origin = Optional.empty();
		List<BlockPos> orePosList = Lists.newArrayList();

		public Optional<BlockPos> getBasePos(){
			return this.origin;
		}

		public List<BlockPos> getOrePosList(){
			return this.orePosList;
		}
	}
//	public static class Data implements IExtendedPotionData{
//
//		public Data() {
//			// TODO 自動生成されたコンストラクター・スタブ
//		}
//		Optional<BlockPos> origin = Optional.empty();
//		List<BlockPos> orePosList = Lists.newArrayList();
//
//		public Optional<BlockPos> getBasePos(){
//			return this.origin;
//		}
//
//		public List<BlockPos> getOrePosList(){
//			return this.orePosList;
//		}
//
//		public void setBasePos(BlockPos pos){
//			this.origin = Optional.of(pos);
//		}
//
//		public void setOrePosList(List<BlockPos> posList){
//			this.orePosList = posList;
//		}
//		public void clear(){
//			this.origin = Optional.empty();
//			this.orePosList = Lists.newArrayList();
//		}
//
//		@Override
//		public void writeToNBT(NBTTagCompound nbt) {
//			// TODO 自動生成されたメソッド・スタブ
//			if(origin.isPresent()){
//				new XYZPos(origin.get()).writeToNBT(nbt);
//
//			}
//			if(!orePosList.isEmpty()){
//
//				UtilNBT.writeListToNBT(orePosList.stream().map(in -> new XYZPos(in)).collect(Collectors.toList()), nbt, "orePosList");
//			}
//		}
//
//		@Override
//		public void readFromNBT(NBTTagCompound nbt) {
//			// TODO 自動生成されたメソッド・スタブ
//			XYZPos pos = XYZPos.readFromNBT(nbt);
//			if(pos!=null){
//				this.origin = Optional.of(pos);
//			}
//			if(nbt.hasKey("orePosList")){
//				this.orePosList = UtilNBT.readListFromNBT(nbt, "orePosList", XYZPos.RESTORE_TO_BLOCKPOS);
//			}
//		}
//	}

//	@Override
//	public IExtendedPotionData getExtendedPotionData() {
//		// TODO 自動生成されたメソッド・スタブ
//		return new Data();
//	}
}
