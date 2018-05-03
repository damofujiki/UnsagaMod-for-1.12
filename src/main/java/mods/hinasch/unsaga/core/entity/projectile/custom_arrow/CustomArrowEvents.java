package mods.hinasch.unsaga.core.entity.projectile.custom_arrow;

import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.entity.EntityArrowUpdateEvent;
import mods.hinasch.lib.network.PacketUtil;
import mods.hinasch.lib.particle.PacketParticle;
import mods.hinasch.lib.util.UtilNBT;
import mods.hinasch.lib.world.WorldHelper;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CustomArrowEvents {

	@SubscribeEvent
	public void onArrowImpact(ProjectileImpactEvent.Arrow e){
//		if(e.getRayTraceResult().typeOfHit==Type.BLOCK){
//			if(CustomArrowCapability.ADAPTER.getCapability(e.getArrow()).getArrowType()==StateArrow.Type.SHADOW_STITCH){
//				World world = e.getArrow().getEntityWorld();
//				world.getEntitiesWithinAABB(EntityLivingBase.class,e.getArrow().getEntityBoundingBox().grow(3D, 2D, 3D),in ->in != e.getArrow().shootingEntity)
//				.forEach(in ->{
//					in.addPotionEffect(new PotionEffect(UnsagaPotions.STUN,ItemUtil.getPotionTime(8),0));
//				});
//			}
//
//		}

		if(CustomArrowCapability.ADAPTER.hasCapability(e.getArrow())){
			CustomArrowCapability.ADAPTER.getCapability(e.getArrow()).getArrowType().onImpact(e);
		}
//		if(e.getRayTraceResult().typeOfHit==Type.ENTITY){
//			if(CustomArrowCapability.ADAPTER.hasCapability(e.getArrow())){
//				ICustomArrow instance = CustomArrowCapability.ADAPTER.getCapability(e.getArrow());
//				if(instance.getArrowType()!=StateArrow.Type.NONE){
//
//					Entity entity = e.getRayTraceResult().entityHit;
//					if(entity instanceof EntityLivingBase){
//						entity.attackEntityFrom(DamageHelper.create(instance.getArrowType().getDamageSource(e.getArrow().shootingEntity, e.getArrow())));
//					}
//				}
//
//			}
//		}
	}

	@SubscribeEvent
	public void onArrowUpdate(EntityArrowUpdateEvent e){
		EntityArrow arrow = e.getArrow();
		if(CustomArrowCapability.ADAPTER.hasCapability(arrow) && WorldHelper.isServer(arrow.world)){

			if(arrow.ticksExisted % 3 == 0){
				EnumParticleTypes type = CustomArrowCapability.ADAPTER.getCapability(arrow).getArrowType().getParticle();
//				UnsagaMod.logger.trace(this.getClass().getName(), type);
//				Field inGround = ReflectionHelper.findField(EntityArrow.class, "field_146051_au","inGround","field_174854_a");
//				boolean flag = true;
//				try {
//					flag = inGround.getBoolean(arrow);
//				} catch (IllegalArgumentException e1) {
//					// TODO 自動生成された catch ブロック
//					e1.printStackTrace();
//				} catch (IllegalAccessException e1) {
//					// TODO 自動生成された catch ブロック
//					e1.printStackTrace();
//				}
				NBTTagCompound nbt = UtilNBT.compound();
				arrow.writeEntityToNBT(nbt);
				boolean inGround = nbt.getBoolean("inGround");
				if(type!=null && !inGround){
					HSLib.getPacketDispatcher().sendToAllAround(PacketParticle.toEntity(type, arrow, 15), PacketUtil.getTargetPointNear(arrow,300D));
//					ParticleHelper.MovingType.DIVERGE.spawnParticle(arrow.getEntityWorld(), XYZPos.createFrom(arrow), type, arrow.world.rand, 15, 0.1D);
				}

			}

		}
	}


	public static void onEntityJoin(EntityJoinWorldEvent e){
//		if(WorldHelper.isServer(e.getWorld())){
//			return;
//		}
//		if(e.getEntity() instanceof EntityArrow){
//			if(CustomArrowCapability.ADAPTER.hasCapability(e.getEntity())){
//				ICustomArrow instance = CustomArrowCapability.ADAPTER.getCapability(e.getEntity());
//				if(instance.getArrowType()!=Type.NONE){
//					NBTTagCompound args = UtilNBT.compound();
//					args.setInteger("entityid", e.getEntity().getEntityId());
//					HSLib.getPacketDispatcher().sendToServer(PacketSyncCapability.createRequest(CustomArrowCapability.CAPA, instance,args));
//				}
//
//			}
//		}
	}
//
//	@SubscribeEvent
//	public void arrowParticleEvent(LivingUpdateEvent e){
//		World world = e.getEntityLiving().getEntityWorld();
//		if(WorldHelper.isServer(world)){
//			return;
//		}
//		world.getEntitiesWithinAABB(EntityArrow.class, e.getEntityLiving().getEntityBoundingBox().grow(50D))
//		.forEach(in ->{
//			if(in.ticksExisted % 12 == 0){
//				StateArrow.Type type = CustomArrowCapability.ADAPTER.getCapability(in).getArrowType();
//				EnumParticleTypes particle = type.getParticle();
//				ParticleHelper.MovingType.DIVERGE.spawnParticle(world, XYZPos.createFrom(in), particle, world.rand, 10, 0.2D);
//			}
//		});
//	}
}
