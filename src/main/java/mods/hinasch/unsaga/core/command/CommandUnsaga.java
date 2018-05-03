package mods.hinasch.unsaga.core.command;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Joiner;

import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.lib.entity.ModifierHelper;
import mods.hinasch.lib.util.ChatHandler;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.entity.passive.EntityEffectSpawner;
import mods.hinasch.unsaga.core.net.packet.PacketDebugPos;
import mods.hinasch.unsaga.core.world.UnsagaWorldCapability;
import mods.hinasch.unsaga.core.world.WorldStructureInfo;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.Sub;
import mods.hinasch.unsaga.status.UnsagaStatus;
import mods.hinasch.unsaga.villager.UnsagaVillagerCapability;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandUnsaga extends CommandBase{

	@Override
	public String getName() {
		// TODO 自動生成されたメソッド・スタブ
		return "unsagamod";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO 自動生成されたメソッド・スタブ
		return "/unsagamod delete";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length>=1){
			String str = args[0];

//			if(str.equals("mods")){
//				if(sender instanceof EntityPlayer){
//					EntityPlayer ep = (EntityPlayer) sender;
//					ChatHandler.sendChatToPlayer(ep, "Loaded Heat And Climate Mod:"+HSLib.plugin().isLoadedHAC());
//				}
//
//			}
			if(str.equals("delete")){
				if(sender instanceof EntityPlayer){
					EntityPlayer ep = (EntityPlayer) sender;
					ep.world.getEntitiesWithinAABB(EntityLivingBase.class, ep.getEntityBoundingBox().expand(30.0D, 30.0D, 30.0D))
					.forEach(living ->{
						if(living!=ep){
							living.setDead();
						}

					});
				}
			}
			if(str.equals("fountain")){

				if(sender instanceof EntityPlayer){
					int dens = Integer.valueOf(args[1]);
					double speed = Double.valueOf(args[2]);

					EntityEffectSpawner en = new EntityEffectSpawner(ClientHelper.getWorld());
					XYZPos p = XYZPos.createFrom((Entity) sender);
					en.setPosition(p.dx,p.dy,p.dz);
					en.setFountainStatus(dens, speed);
					ClientHelper.getWorld().spawnEntity(en);
				}

			}
//			if(str.equals("togglespark")){
//				boolean b = UnsagaMod.configHandler.isAlwaysSparkling();
//				UnsagaMod.configHandler.enableAlwaysSparkling(!b);
//				ChatHandler.sendChatToPlayer((EntityPlayer) sender, "Toggled Always Spark Mode.");
//			}

			if(str.equals("pos")){

				int bank = Integer.valueOf(args[1]);
					double x = Double.valueOf(args[2]);
					double y = Double.valueOf(args[3]);
					double z = Double.valueOf(args[4]);

					XYZPos pos = new XYZPos(x,y,z);
					UnsagaMod.packetDispatcher.sendTo(new PacketDebugPos(bank,pos), (EntityPlayerMP) sender);

			}
			if(str.equals("status")){
				StringBuilder builder = new StringBuilder("");
				if(sender instanceof EntityPlayer){
					EntityPlayer ep = (EntityPlayer) sender;
					double strmodifier = ep.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
					builder.append("Attack:"+strmodifier+"/");
					ModifierHelper.dumpModifiers(SharedMonsterAttributes.ATTACK_DAMAGE, ep);
					for(General gen:General.values()){
						double at = ep.getEntityAttribute(UnsagaStatus.GENERALS.get(gen)).getAttributeValue();
						builder.append(gen.getName()+":"+at+"/");
						ModifierHelper.dumpModifiers(UnsagaStatus.GENERALS.get(gen), ep);
					}
					for(Sub sub:Sub.values()){
						if(sub!=Sub.NONE){
							double at = ep.getEntityAttribute(UnsagaStatus.SUBS.get(sub)).getAttributeValue();
							builder.append(sub.getName()+":"+at+"/");
						}
						ModifierHelper.dumpModifiers(UnsagaStatus.SUBS.get(sub), ep);
					}
					ChatHandler.sendChatToPlayer(ep,builder.toString());
				}
			}
			if(str.equals("stock")){
				if(sender instanceof EntityPlayer){
					EntityPlayer ep = (EntityPlayer) sender;
					ep.world.getEntitiesWithinAABB(EntityVillager.class, ep.getEntityBoundingBox().expand(30.0D, 30.0D, 30.0D))
					.forEach(living ->{
						if(UnsagaVillagerCapability.ADAPTER.hasCapability(living)){
							long recent = UnsagaVillagerCapability.ADAPTER.getCapability(living).getRecentStockedTime();
							UnsagaVillagerCapability.ADAPTER.getCapability(living).setStockedTime(recent - 24000);
						}

					});
				}
			}
			if(str.equals("structure")){
				if(sender instanceof EntityPlayer){
					EntityPlayer ep = (EntityPlayer) sender;
					List<XYZPos> pos = UnsagaWorldCapability.ADAPTER.getCapability(ep.getEntityWorld()).getWorldStructureInfo().getCoords(WorldStructureInfo.MERCHANT_HOUSE)
							.stream().map(in -> new XYZPos(in.getX() << 4,in.getY() <<4,in.getZ() <<4)).collect(Collectors.toList());
					String mes = pos.isEmpty() ? "empty" :Joiner.on(',').join(pos);
					ChatHandler.sendChatToPlayer(ep, mes);
				}
			}
//			if(str.equals("distribution") && args.length>2){
//				int level = this.parseInt(args[1],0,999);
//				if(sender instanceof EntityPlayer){
//					EntityPlayer ep = (EntityPlayer) sender;
//					ep.worldObj.getEntitiesWithinAABB(EntityVillager.class, ep.getEntityBoundingBox().expand(30.0D, 30.0D, 30.0D))
//					.forEach(villager ->{
//						if(UnsagaVillager.hasCapability(villager)){
//							UnsagaVillager.getCapability(villager).setDistributionLevel(level);
//							ChatHandler.sendChatToPlayer(ep, "set distributionLevel to "+level);
//						}
//					});
//				}
//			}
//			if(str.equals("checkevents")){
//
//				final String type = args[1];
//
//				String message = new Supplier<String>(){
//
//					@Override
//					public String get() {
//						if(type.equals("update")){
//							String eventsUpdate = "update events:";
//
//							for(ILivingUpdateEvent ev:HSLibEvents.livingUpdate.getEvents()){
//
//								eventsUpdate += ev.getName()+",";
//
//							}
//							return eventsUpdate;
//						}
//						if(type.equals("hurt")){
//							String eventsHurt = "hurt events pre:";
//							for(ILivingHurtEvent ev:HSLibEvents.livingHurt.getEventsPre()){
//
//								eventsHurt += ev.getName()+",";
//
//							}
//							eventsHurt += ">>";
//							for(ILivingHurtEvent ev:HSLibEvents.livingHurt.getEventsMiddle()){
//
//								eventsHurt += ev.getName()+",";
//
//							}
//							eventsHurt += ">>";
//							for(ILivingHurtEvent ev:HSLibEvents.livingHurt.getEventsPost()){
//
//								eventsHurt += ev.getName()+",";
//
//							}
//							return eventsHurt;
//						}
//						return "";
//					}
//				}.get();
//
//
//
//
//				if(sender instanceof EntityPlayer){
//					EntityPlayer ep  = (EntityPlayer) sender;
//					ChatHandler.sendChatToPlayer(ep, message);
//				}
//			}
//			if(str.equals("info")){
//				UnsagaMod.logger.trace(this.getClass().getName(), "called");
//				if(sender instanceof EntityLivingBase){
//					EntityLivingBase living = (EntityLivingBase) sender;
//					String message = "HealAmount:"+AbilityHelper.getHealWeight((EntityPlayer) living);
//					List<EntityVillager> list = living.worldObj.getEntitiesWithinAABB(EntityVillager.class, living.getEntityBoundingBox().expand(50D, 50D, 50D));
//					if(!list.isEmpty()){
//						OptionalInt dismax = list.stream().mapToInt(input ->{
//							if(UnsagaVillager.hasCapability(input)){
//								return UnsagaVillager.getCapability(input).getDistributionLevel();
//							}
//							return 0;
//						}
//						).max();
//
//						if(dismax.isPresent()){
//							message += " DistributionLevel:"+dismax.getAsInt();
//						}
//					}
//
//					if(sender instanceof EntityPlayer){
//						EntityPlayer ep  = (EntityPlayer) sender;
//						ChatHandler.sendChatToPlayer(ep, message);
//					}
//				}
//
//			}
		}

	}

}
