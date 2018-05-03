//package mods.hinasch.unsaga.damage.ranged;
//
//import mods.hinasch.lib.IPostRangeAttack;
//import mods.hinasch.unsaga.ability.wazaeffect.WazaPerformer;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.util.DamageSource;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraft.world.World;
//
//public class RangeExplode extends RangeDamageUnsaga{
//
//
//	public RangeExplode(World world, WazaPerformer parent) {
//		super(world, parent);
//		// TODO 自動生成されたコンストラクター・スタブ
//	}
//
//	@Override
//	public void postRangeAttacked(DamageSource ds, AxisAlignedBB aabb, float damage, EntityLivingBase mob){
//		getHook(this.helper).postRangeAttacked(ds, aabb, damage, mob);
//
//	}
//
//	public static RangeExplode create(World w,WazaPerformer invoker){
//		return new RangeExplode(w,invoker);
//	}
//
//	public static IPostRangeAttack getHook(WazaPerformer parent){
//		return (ds,aabb,damage,mob) ->{
//			if(parent.owner.onGround){
//				parent.world.createExplosion(ds.getEntity(), mob.posX, mob.posY, mob.posZ, 0.6F * (float)parent.getChargeAppliedAttackDamage(false,0)/2, false);
//			}else{
//				parent.world.createExplosion(ds.getEntity(), mob.posX, mob.posY, mob.posZ, 0.6F * (float)parent.getChargeAppliedAttackDamage(false,0)/2, true);
//			}
//		};
//	}
//}
