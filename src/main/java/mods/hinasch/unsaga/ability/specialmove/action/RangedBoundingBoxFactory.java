package mods.hinasch.unsaga.ability.specialmove.action;

import java.util.List;
import java.util.function.Function;

import com.google.common.collect.Lists;

import mods.hinasch.unsaga.ability.specialmove.TechInvoker;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

/**
 *
 * 技での特殊な複雑？な当たり判定の生成用
 *
 */
public class RangedBoundingBoxFactory {
	/**
	 *
	 *単順に自分の当たり判定を広げたもの
	 *
	 */
	public static class RangeSurroundings implements Function<TechInvoker,List<AxisAlignedBB>>{

		final double horizontal;
		final double vertical;
		public RangeSurroundings(double horizontal,double vertical){
			this.horizontal = horizontal;
			this.vertical = vertical;
		}
		@Override
		public List<AxisAlignedBB> apply(TechInvoker t) {
			List<AxisAlignedBB> list = Lists.newArrayList();
			list.add(t.getPerformer().getEntityBoundingBox().grow(horizontal,vertical,horizontal));
			return list;
		}

	}


	/**
	 *
	 * 扇形の当たり判定。resolutionを上げるほど細かい（扇に近い）判定になる
	 *
	 */
	public static class RangeSwing implements Function<TechInvoker,List<AxisAlignedBB>>{

		/** 多くするほど当たり判定が細かい*/
		final int resolution;
		public RangeSwing(int resolution){
			this.resolution = resolution;
		}
		@Override
		public List<AxisAlignedBB> apply(TechInvoker context) {
			List<AxisAlignedBB> list = Lists.newArrayList();
			EntityLivingBase el = context.getPerformer();
			int rotatePar = 180 / this.resolution;
			for(int i=0;i<(int)context.getReach();i++){
				Vec3d v1 = el.getLookVec().normalize().scale(i+1);

				for(int r=0;r<resolution+i;r++){
					Vec3d v2 = v1.rotateYaw((float) Math.toRadians(90-(rotatePar*r)));
					AxisAlignedBB bb = el.getEntityBoundingBox().grow(1.5F, 0, 1.5F).offset(v2.x,v2.y,v2.z);
					list.add(bb);
				}
			}


			return list;
		}

	}
}
