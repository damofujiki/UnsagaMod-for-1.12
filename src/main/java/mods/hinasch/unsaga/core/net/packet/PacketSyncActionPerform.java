package mods.hinasch.unsaga.core.net.packet;

import io.netty.buffer.ByteBuf;
import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.lib.iface.IIntSerializable;
import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.network.PacketUtil;
import mods.hinasch.lib.util.ChatHandler;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.util.UtilNBT;
import mods.hinasch.unsaga.common.tool.ItemHasSpecialMove;
import mods.hinasch.unsagamagic.item.ItemSpellBook;
import mods.hinasch.unsagamagic.spell.Spell;
import mods.hinasch.unsagamagic.spell.SpellComponent;
import mods.hinasch.unsagamagic.spell.SpellRegistry;
import mods.hinasch.unsagamagic.spell.action.SpellCaster;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketSyncActionPerform implements IMessage{

	NBTTagCompound nbt;

	public static enum Operation implements IIntSerializable{
		SYNC_SPELL_CAST,SYNC_CHARGED_TECH,SYNC_SPELL_CAST_PLAYER;

		public int getMeta(){
			switch(this){
			case SYNC_CHARGED_TECH:
				return 1;
			case SYNC_SPELL_CAST_PLAYER:
				return 2;
			case SYNC_SPELL_CAST:
				return 3;
			default:
				break;

			}
			return 0;
		}

		public static Operation fromMeta(int meta){
			return HSLibs.fromMeta(Operation.values(), meta);
		}
	}
	public PacketSyncActionPerform(){

	}

	public static PacketSyncActionPerform createSyncSpellCastPacket(){
		NBTTagCompound tag = UtilNBT.compound();
		tag.setInteger("operation", Operation.SYNC_SPELL_CAST_PLAYER.getMeta());
		PacketSyncActionPerform packet = new PacketSyncActionPerform(tag);
		return packet;
	}
	public static PacketSyncActionPerform createSyncSpellCastPacket(int entityid,Spell spell){
		NBTTagCompound tag = UtilNBT.compound();
		tag.setString("spell", spell.getKey().getResourcePath());
		tag.setInteger("entityid", entityid);
		tag.setInteger("operation", Operation.SYNC_SPELL_CAST.getMeta());
		PacketSyncActionPerform packet = new PacketSyncActionPerform(tag);
		return packet;
	}
	public PacketSyncActionPerform(NBTTagCompound data){
		this.nbt = data;
	}


	public static PacketSyncActionPerform createSyncChargedAttackPacket(int timeleft){
		NBTTagCompound tag = UtilNBT.compound();
		tag.setInteger("operation", Operation.SYNC_CHARGED_TECH.getMeta());
		tag.setInteger("timeLeft", timeleft);
		PacketSyncActionPerform packet = new PacketSyncActionPerform(tag);
		return packet;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		int len = buf.readInt();
		ByteBuf bytes = buf.readBytes(len);
		this.nbt = PacketUtil.bytesToNBT(bytes);

	}

	@Override
	public void toBytes(ByteBuf buf) {
		// TODO 自動生成されたメソッド・スタブ
		byte[] bytes = PacketUtil.nbtToBytes(nbt);
		buf.writeInt(bytes.length);
		buf.writeBytes(bytes);
	}

	public NBTTagCompound getData(){
		return this.nbt;
	}


	public static class Handler implements IMessageHandler<PacketSyncActionPerform,IMessage>{

		@Override
		public IMessage onMessage(PacketSyncActionPerform message, MessageContext ctx) {
			if(ctx.side==Side.SERVER){
				World world = ctx.getServerHandler().player.getEntityWorld();
				EntityPlayer ep = ctx.getServerHandler().player;
				NBTTagCompound data = message.getData();
				ItemStack held = ep.getHeldItemMainhand();
				int operation = data.getInteger("operation");
				Operation op = Operation.fromMeta(operation);
				switch(op){
				case SYNC_CHARGED_TECH:
					if(held.getItem() instanceof ItemHasSpecialMove){
						int timeLeft = data.getInteger("timeLeft");
						ItemHasSpecialMove item = (ItemHasSpecialMove) held.getItem();
						item.invokeChargedAttack(held, world, ep, timeLeft);
					}
					break;
				case SYNC_SPELL_CAST_PLAYER:
					if(ItemUtil.isItemStackPresent(held) && held.getItem() instanceof ItemSpellBook){
						ItemSpellBook item = (ItemSpellBook) held.getItem();
						item.processCasting(world, ep, held);
					}
					break;

				default:
					break;

				}



			}else{
				EntityPlayer ep = ClientHelper.getPlayer();
				ItemStack held = ep.getHeldItemMainhand();
				World world = ClientHelper.getWorld();
				NBTTagCompound data = message.getData();
				int operation = data.getInteger("operation");
				Operation op = Operation.fromMeta(operation);
				switch(op){
				case SYNC_CHARGED_TECH:
					if(held.getItem() instanceof ItemHasSpecialMove){
						int timeLeft = data.getInteger("timeLeft");
						ItemHasSpecialMove item = (ItemHasSpecialMove) held.getItem();
						item.invokeChargedAttack(held, world, ep, timeLeft);
					}
					break;
				case SYNC_SPELL_CAST:
					int id = data.getInteger("entityid");
					String spellid = data.getString("spell");
					Spell spell = SpellRegistry.instance().get(spellid);
					if(world.getEntityByID(id) instanceof EntityLivingBase && spell!=null){
						EntityLivingBase casterEntity = (EntityLivingBase) world.getEntityByID(id);
						SpellComponent component = new SpellComponent(spell,1.0F,1.0F,false);
						SpellCaster spellcaster = SpellCaster.ofEnemy(world, casterEntity, component);
						spellcaster.cast();
						ChatHandler.sendChatToPlayer(ep, HSLibs.translateKey("chat.unsaga.enemy.cast.end",casterEntity.getName(), spell.getPropertyName()));
					}
					break;
				default:
					break;

				}
			}
			return null;
		}

	}
}
