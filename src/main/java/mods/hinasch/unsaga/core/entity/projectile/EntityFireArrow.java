//package mods.hinasch.unsaga.core.entity.projectile;
//
//import java.util.Set;
//
//import com.google.common.collect.Sets;
//
//import mods.hinasch.lib.entity.EntityThrowableBase;
//import mods.hinasch.lib.particle.ParticleHelper;
//import mods.hinasch.lib.world.XYZPos;
//import mods.hinasch.unsaga.damage.DamageSourceUnsaga;
//import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
//import mods.hinasch.unsaga.damage.DamageTypeUnsaga.Sub;
//import mods.hinasch.unsagamagic.spell.SpellRegistry;
//import net.minecraft.block.Block;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.init.Blocks;
//import net.minecraft.util.DamageSource;
//import net.minecraft.util.EnumParticleTypes;
//import net.minecraft.util.math.RayTraceResult;
//import net.minecraft.world.World;
//
//public class EntityFireArrow extends EntityThrowableBase{
//
//	protected boolean isAmplified = false;
//
//	protected static final Set<Block> DESTROYABLE = Sets.newHashSet(Blocks.VINE,Blocks.WEB,Blocks.ICE,Blocks.FROSTED_ICE);
//	public EntityFireArrow(World par1World) {
//		super(par1World);
//
//	}
//
//	public EntityFireArrow(World par1World, EntityLivingBase par2EntityLiving)
//	{
//		super(par1World,par2EntityLiving);
//
//
//	}
//
//
//
//	boolean checked = false;
//	@Override
//	public void onUpdate(){
//
//		super.onUpdate();
//		if(this.ticksExisted %2 ==0){
//			ParticleHelper.MovingType.FLOATING.spawnParticle(world, XYZPos.createFrom(this), EnumParticleTypes.FLAME, this.world.rand, 10, 0.03D);
//		}
//
//		if(this.isInWater()){
//			this.setDead();
//		}
//		if(this.isInLava() && !this.isAmplified){
//			this.isAmplified = true;
//			this.setDamage(this.getDamage()*1.4F);
//
//		}
//
//
//	}
//
//
//
//	@Override
//	public DamageSource getDamageSource(RayTraceResult result) {
//		// TODO 自動生成されたメソッド・スタブ
//		return DamageSourceUnsaga.createProjectile(this.getThrower(), this, this.getDamage(), General.MAGIC).setSubTypes(Sub.FIRE).setStrLPHurt(SpellRegistry.instance().FIRE_ARROW.getStrength().lp().amount());
//	}
//
//	@Override
//	public Set<Block> getDestroyableBlocks(){
//		return DESTROYABLE;
//	}
//
//}
