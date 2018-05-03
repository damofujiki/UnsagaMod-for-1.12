package mods.hinasch.unsaga.core.net.packet;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.lib.network.PacketSyncCapability;
import mods.hinasch.lib.network.PacketUtil;
import mods.hinasch.lib.sync.AsyncUpdateEvent;
import mods.hinasch.lib.util.UtilNBT;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.lp.LifePoint;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class PacketLP implements IMessage{


	NBTTagCompound nbt;
	private UUID uuid;
	public UUID getUuid() {
		return uuid;
	}
	private int lp;
	private int entityid = -1;
	private boolean isPlayer;
	public boolean isPlayer() {
		return isPlayer;
	}
	private boolean isRenderDamagePacket;
	private boolean isRequestPacket;
	public PacketLP(){
		this.isRenderDamagePacket = false;
		this.isRequestPacket = false;
		this.isPlayer = false;
	}

	protected PacketLP(int entityid,int lp){
		this.entityid = entityid;
		this.lp = lp;
		this.isRenderDamagePacket = false;
	}
//	protected PacketLP(UUID entityid,int lp){
//		this.uuid = entityid;
//		this.lp = lp;
//		this.isRenderDamagePacket = false;
//	}
	public static PacketLP createRenderDamagePacket(EntityLivingBase entityid,int lpdamage){
		PacketLP psl = new PacketLP(entityid.getEntityId(),lpdamage);
		psl.isRenderDamagePacket = true;
		return psl;
	}
	public static PacketLP createRequest(EntityLivingBase entityid){

		UnsagaMod.logger.trace("entitjoin", "リクエストパケットを生成");
		if(entityid instanceof EntityPlayer){
			PacketLP psl = new PacketLP(-1,-1);
			psl.isRequestPacket = true;
			psl.isPlayer = true;
			return psl;
		}
		PacketLP psl = new PacketLP(entityid.getEntityId(),-1);
		psl.isRequestPacket = true;
		return psl;

	}


	public static PacketLP create(int entityid,int lp){
		return new PacketLP(entityid,lp);

	}
	public static PacketLP create(EntityLivingBase entityid,int lp){
		return new PacketLP(entityid.getEntityId(),lp);

	}
	@Override
	public void fromBytes(ByteBuf buf) {
		int length = buf.readInt();
		ByteBuf bytes = buf.readBytes(length);
		this.nbt = PacketUtil.bytesToNBT(bytes);
		this.entityid = nbt.getInteger("entityid");
		this.lp = nbt.getByte("lp");
		this.isRenderDamagePacket = nbt.getBoolean("isRender");
		this.isRequestPacket = nbt.getBoolean("isRequest");
		this.isPlayer = nbt.getBoolean("isPlayer");
		if(nbt.hasKey("uuid")){
			this.uuid = nbt.getUniqueId("uuid");
		}

//		this.entityid = buf.readInt();
//		this.lp = buf.readInt();
//
//		this.isRenderDamagePacket = buf.readBoolean();
//		this.isRequestPacket = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		this.nbt = UtilNBT.compound();
		this.nbt.setInteger("entityid", entityid);
		this.nbt.setByte("lp", (byte)lp);
		this.nbt.setBoolean("isRender", this.isRenderDamagePacket);
		this.nbt.setBoolean("isRequest", this.isRequestPacket);
		this.nbt.setBoolean("isPlayer", this.isPlayer);
		if(this.uuid!=null){
			this.nbt.setUniqueId("uuid", uuid);
		}
		byte[] bytes = PacketUtil.nbtToBytes(nbt);
		buf.writeInt(bytes.length);
		buf.writeBytes(bytes);
//
//		buf.writeInt(entityid);
//		buf.writeInt(lp);
//		buf.writeBoolean(isRenderDamagePacket);
//		buf.writeBoolean(isRequestPacket);
	}

	public boolean isRenderRequest(){
		return this.isRenderDamagePacket;
	}
	public int getEntityID(){
		return this.entityid;
	}

	public int getLP(){
		return this.lp;
	}
	public static class PacketLPHandler implements IMessageHandler<PacketLP,IMessage>{

		@Override
		public IMessage onMessage(PacketLP message, MessageContext ctx) {
			EntityPlayer player = null;
			if(ctx.side.isClient()){

				player = ClientHelper.getPlayer();
				if(player!=null){
					World world = player.getEntityWorld();
					Entity entity = world.getEntityByID(message.getEntityID());

					if(entity!=null && LifePoint.adapter.hasCapability((EntityLivingBase) entity)){
						if(message.isRenderRequest()){
							EntityLivingBase living = (EntityLivingBase) entity;

							LifePoint.adapter.getCapability(living).setRenderDamage(message.getLP());
							LifePoint.adapter.getCapability(living).markDirtyRenderDamage(true);
						}else{
							if(entity instanceof EntityLivingBase){
								UnsagaMod.logger.trace("LP sync!"+message.getLP());
								EntityLivingBase living = (EntityLivingBase) entity;
								LifePoint.adapter.getCapability(living).setLifePoint(message.getLP());
							}
						}

					}
				}

			}else{
				//Server Side


				player = ctx.getServerHandler().player;
				World world = player.world;
//				UnsagaMod.logger.trace("LP同期", message.getEntityID());
				Entity entity = null;
				if(message.isPlayer){
					entity = player;
				}else{
					entity = world.getEntityByID(message.getEntityID());
				}
//				entity = world.getEntityByID(message.getEntityID());
//				UnsagaMod.logger.trace("entityid", world.getLoadedEntityList().stream().map(in -> String.valueOf(in.getEntityId())+","+in.getName()).collect(Collectors.toList()));
				if(message.isRequestPacket){
//					AsyncLPInitializer event = new AsyncLPInitializer(world,message.getEntityID(),message.getLP());
//					HSLib.scannerEventPool.addEvent(event);
//					return null;
					UnsagaMod.logger.trace("LP同期", entity,message.getEntityID());
					if(entity instanceof EntityLivingBase && LifePoint.adapter.hasCapability((EntityLivingBase) entity)){

						int lp = LifePoint.adapter.getCapability((EntityLivingBase) entity).getLifePoint();
						UnsagaMod.logger.trace("LP同期リクエストを確認", lp);
						NBTTagCompound nbt = UtilNBT.compound();
						nbt.setInteger("entityid", entity.getEntityId());
						return PacketSyncCapability.create(LifePoint.CAPA, LifePoint.adapter.getCapability(entity),nbt);
//						return PacketLP.create(entity.getEntityId(), lp);

					}else{
						return null;
					}
				}
				if(entity instanceof EntityLivingBase){
					EntityLivingBase living = (EntityLivingBase) entity;
//					ExtendedDataLP data = Unsaga.lpLogicManager.getData(living);
//					ILifePoint capa = LPLogicManager.getCapability(living);
//					capa.setLifePoint(message.getLP());
					LifePoint.adapter.getCapability(living).setLifePoint(message.getLP());
				}
			}
			return null;
		}

	}

	/**
	 * 非同期
	 *
	 */
	public static class AsyncLPInitializer extends AsyncUpdateEvent{

		World world;
		int id;
		int lp;
		boolean finishSync = false;
		public AsyncLPInitializer(World world,int entityid,int lp){
			super(null,"lp");
			UnsagaMod.logger.trace("LP非同期イニシャライザー", entityid);
			this.id = entityid;
			this.lp = lp;
			this.world = world;
		}
		@Override
		public boolean hasFinished() {
			// TODO 自動生成されたメソッド・スタブ
			return finishSync;
		}

		@Override
		public void loop() {
			// TODO 自動生成されたメソッド・スタブ
			if(world.getEntityByID(id)!=null){
				Entity entity = world.getEntityByID(id);
				if(entity instanceof EntityLivingBase){
					UnsagaMod.logger.trace("LP同期します", this.id);
					EntityLivingBase living = (EntityLivingBase) entity;
					UnsagaMod.packetDispatcher.sendToAll(PacketLP.create(living.getEntityId(),lp));
//					if(LPLogicManager.hasCapability(living)){
//						LPLogicManager.getCapability(living).setLifePoint(lp);
						this.finishSync = true;
//					}
				}
			}
		}

	}
}
