package mods.hinasch.unsaga.common.specialaction;

import java.util.Optional;

import mods.hinasch.lib.network.PacketUtil;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.common.specialaction.ActionBase.IAction;
import mods.hinasch.unsaga.core.net.packet.PacketClientThunder;
import mods.hinasch.unsaga.damage.AdditionalDamageData;
import mods.hinasch.unsaga.damage.DamageHelper;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.Sub;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;

public class ActionThunder<T extends IActionPerformer> implements IAction<T>{

	public static enum Type{
		EXPLODE,NORMAL;
	}

	final Type type;
	public ActionThunder(Type type){
		this.type = type;
	}
	public EnumActionResult apply(T context) {
		Optional<EntityLivingBase> target = context.getTarget();
		if(context.getTargetType()==IActionPerformer.TargetType.TARGET){
			if(target.isPresent()){
				if(context.getTargetType()==IActionPerformer.TargetType.TARGET){
					XYZPos pos = XYZPos.createFrom(target.get());
					context.playSound(pos, SoundEvents.ENTITY_LIGHTNING_THUNDER, true);

					this.attackTarget(context, target.get());
//					this.callThunderAt(context.getWorld(), pos);
					this.processThunder(context, XYZPos.createFrom(target.get()));

					return EnumActionResult.SUCCESS;
				}

			}
		}
		if(context.getTargetType()==IActionPerformer.TargetType.POSITION){
			if(context.getTargetCoordinate().isPresent()){
				XYZPos pos = new XYZPos((BlockPos) context.getTargetCoordinate().get());
				context.playSound(pos, SoundEvents.ENTITY_LIGHTNING_THUNDER, true);
				this.processThunder(context, pos);
				context.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, HSLibs.getBounding(pos, 2.5D, 2.5D),in -> in!=context.getPerformer())
				.forEach(in ->{
					this.attackTarget(context, in);

				});

				return EnumActionResult.SUCCESS;
			}
		}


		return EnumActionResult.PASS;
	}

	private void processThunder(T context,XYZPos thunderPoint){
		if(WorldHelper.isServer(context.getWorld())){
			UnsagaMod.packetDispatcher.sendToAllAround(PacketClientThunder.create(thunderPoint)
					,PacketUtil.getTargetPointNear(thunderPoint,context.getWorld()));
			if(this.type==Type.EXPLODE){
				WorldHelper.createExplosionSafe(context.getWorld(),context.getPerformer(), thunderPoint, 3, true);
			}
		}
	}
	private void attackTarget(T context,EntityLivingBase target){
		AdditionalDamageData data = new AdditionalDamageData(AdditionalDamageData.getSource(context.getPerformer()),context.getStrength().lp(),General.MAGIC);
		data.setSubTypes(Sub.ELECTRIC);
		target.attackEntityFrom(DamageHelper.create(data),context.getStrength().hp());
	}


}
