//package mods.hinasch.unsaga.damage.ranged;
//
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.util.DamageSource;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraft.world.World;
//
//public class RangeKnockBack extends RangeDamageUnsaga{
//
//	public double knockbackStr;
//	public float lpdamage;
//
//	public RangeKnockBack(World world,double knock) {
//		super(world);
//		this.knockbackStr = knock;
//	}
//
//	public void setLPDamage(float f){
//		this.lpdamage = f;
//	}
//
//	@Override
//	public void doRangedAttack(DamageSource ds, AxisAlignedBB aabb, float damage, EntityLivingBase mob){
//		mob.knockBack(mob, 0, 2.0D*knockbackStr, 1.0D*knockbackStr);
//	}
//
//
//}
