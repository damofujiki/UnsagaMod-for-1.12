package mods.hinasch.unsaga.core.net.packet;

import com.google.common.base.Predicate;

import io.netty.buffer.ByteBuf;
import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.sync.AsyncUpdateEvent;
import mods.hinasch.lib.world.XYZPos;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketClientThunder implements IMessage{

	protected XYZPos pos;
	public PacketClientThunder(){

	}


	protected PacketClientThunder(XYZPos pos){
		this.pos = pos;
	}

	public static PacketClientThunder create(XYZPos pos){
		return new PacketClientThunder(pos);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = XYZPos.readFromBuffer(buf);

	}

	@Override
	public void toBytes(ByteBuf buf) {
		this.pos.writeToBuffer(buf);

	}

	public XYZPos getPos(){
		return this.pos;
	}


	public static class Handler implements IMessageHandler<PacketClientThunder,IMessage>{



		@Override
		public IMessage onMessage(PacketClientThunder mes,
				MessageContext ctx) {
			if(ctx.side==Side.CLIENT){
				XYZPos pos = mes.getPos();
				EntityPlayer clientPlayer = Minecraft.getMinecraft().player;
				if(clientPlayer.world.getEntities(EntityWeatherEffect.class, new Predicate(){

					@Override
					public boolean apply(Object input) {
						return input instanceof EntityLightningBolt;
					}}
				).size()==0){
					SafeLightningBolt event = new SafeLightningBolt(clientPlayer,pos);
					HSLib.addAsyncEvent(event.getSender(), event);
//					HSLib.core().events.scannerEventPool.addEvent(event);
//					clientPlayer.worldObj.spawnEntityInWorld(bolt);

//					Unsaga.debug("PacketClient",bolt,pos);
				}


			}

			return null;
		}

	}
	public static class SafeLightningBolt extends AsyncUpdateEvent{

		boolean hasSpawn =false;
		XYZPos pos;

		public SafeLightningBolt(EntityLivingBase sender,XYZPos pos){
			super(sender, "lightningbolt");
			this.pos = pos;
		}
		@Override
		public boolean hasFinished() {
			// TODO 自動生成されたメソッド・スタブ
			return hasSpawn;
		}

		@Override
		public void loop() {
			EntityLightningBolt bolt = new EntityLightningBolt(ClientHelper.getWorldClient(),pos.getX(),pos.getY(),pos.getZ(),false);
			ClientHelper.getWorldClient().spawnEntity(bolt);
			this.hasSpawn = true;

		}

	}
}
