package mods.hinasch.unsaga.core.potion;

import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;

public class StateSetPos extends PotionUnsaga{

	protected StateSetPos(String name) {
		super(name, false, 0, 0,0);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public static class Effect extends PotionEffect{

		final BlockPos pos;
		public Effect(int durationIn,BlockPos pos) {
			super(UnsagaPotions.SET_POS, durationIn);
			// TODO 自動生成されたコンストラクター・スタ
			this.pos = pos;
		}

		public BlockPos getPos(){
			return this.pos;
		}
	}
}
