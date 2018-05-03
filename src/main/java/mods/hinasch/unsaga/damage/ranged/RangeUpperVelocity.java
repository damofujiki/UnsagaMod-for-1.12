//package mods.hinasch.unsaga.damage.ranged;
//
//import mods.hinasch.unsaga.ability.wazaeffect.WazaPerformer;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.util.DamageSource;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraft.world.World;
//
//public class RangeUpperVelocity extends RangeDamageUnsaga{
//
//	public static RangeUpperVelocity create(World world, WazaPerformer parent){
//		return new RangeUpperVelocity(world, parent);
//	}
//	protected RangeUpperVelocity(World world, WazaPerformer parent) {
//		super(world, parent);
//		// TODO 自動生成されたコンストラクター・スタブ
//	}
//
//
//	@Override
//	public void postRangeAttacked(DamageSource ds, AxisAlignedBB aabb, float damage, EntityLivingBase mob){
//		if(mob.onGround){
//			mob.addVelocity(0, 2.0D, 0);
//		}
//
//	}
//}
