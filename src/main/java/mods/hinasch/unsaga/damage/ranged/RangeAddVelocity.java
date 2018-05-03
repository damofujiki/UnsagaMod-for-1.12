//package mods.hinasch.unsaga.damage.ranged;
//
//import mods.hinasch.lib.IPostRangeAttack;
//import mods.hinasch.unsaga.ability.wazaeffect.WazaPerformer;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.util.DamageSource;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraft.world.World;
//
//public class RangeAddVelocity extends RangeDamageUnsaga{
//
//	public static RangeAddVelocity create(World world,WazaPerformer parent){
//		return new RangeAddVelocity(world,parent);
//	}
//	public RangeAddVelocity(World world, WazaPerformer parent) {
//		super(world, parent);
//		// TODO 自動生成されたコンストラクター・スタブ
//	}
//
//	@Override
//	public void postRangeAttacked(DamageSource ds, AxisAlignedBB aabb, float damage, EntityLivingBase mob){
//
//		getHook(this.helper.owner).postRangeAttacked(ds, aabb, damage, mob);;
//
//	}
//
//	public static IPostRangeAttack getHook(EntityLivingBase owner){
//		return (ds,aabb,damage,mob)->{
//			double d0 = owner.posX - mob.posX;
//			double d1;
//
//			for (d1 = owner.posZ - mob.posZ; d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D)
//			{
//				d0 = (Math.random() - Math.random()) ;
//			}
//			mob.knockBack(mob, 0, d0, d1);
//			mob.setVelocity(mob.motionX*2,mob.motionY*2,mob.motionZ*2);
//		};
//	}
//}
