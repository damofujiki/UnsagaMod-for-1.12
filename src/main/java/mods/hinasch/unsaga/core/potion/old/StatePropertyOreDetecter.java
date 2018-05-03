//package mods.hinasch.unsaga.core.potion.old;
//
//import java.util.List;
//import java.util.Optional;
//
//import com.google.common.collect.Lists;
//
//import mods.hinasch.unsaga.core.entity.State;
//import mods.hinasch.unsaga.core.entity.StateProperty;
//import net.minecraft.util.math.BlockPos;
//
//public class StatePropertyOreDetecter extends StateProperty{
//
//	public StatePropertyOreDetecter(String name) {
//		super(name);
//		// TODO 自動生成されたコンストラクター・スタブ
//	}
//
//	@Override
//	public State createState(){
//		return new StateOreDetecter();
//	}
//	public static class StateOreDetecter extends State{
//
//		public StateOreDetecter() {
//			super(false);
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
//	}
//}
