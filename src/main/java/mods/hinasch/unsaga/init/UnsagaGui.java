package mods.hinasch.unsaga.init;

import com.google.common.collect.Lists;

import mods.hinasch.lib.capability.VillagerHelper;
import mods.hinasch.lib.client.GuiTextMenu;
import mods.hinasch.lib.client.IGuiAttribute;
import mods.hinasch.lib.container.ContainerTextMenu;
import mods.hinasch.lib.iface.IModBase;
import mods.hinasch.lib.network.PacketOpenGui;
import mods.hinasch.lib.util.ChatHandler;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.chest.ChestCapability;
import mods.hinasch.unsaga.chest.IChestBehavior;
import mods.hinasch.unsaga.core.client.gui.GuiBartering;
import mods.hinasch.unsaga.core.client.gui.GuiChest;
import mods.hinasch.unsaga.core.client.gui.GuiEquipment;
import mods.hinasch.unsaga.core.client.gui.GuiSkillPanel;
import mods.hinasch.unsaga.core.client.gui.GuiToolCustomizeMinsaga;
import mods.hinasch.unsaga.core.client.gui.GuiBlacksmithUnsaga;
import mods.hinasch.unsaga.core.client.gui.GuiUnsagaMap;
import mods.hinasch.unsaga.core.inventory.container.ContainerBartering;
import mods.hinasch.unsaga.core.inventory.container.ContainerChestUnsaga;
import mods.hinasch.unsaga.core.inventory.container.ContainerEquipment;
import mods.hinasch.unsaga.core.inventory.container.ContainerSkillPanel;
import mods.hinasch.unsaga.core.inventory.container.ContainerToolCustomizeMinsaga;
import mods.hinasch.unsaga.core.inventory.container.ContainerBlacksmithUnsaga;
import mods.hinasch.unsaga.core.inventory.container.ContainerUnsagaMap;
import mods.hinasch.unsaga.util.LivingHelper;
import mods.hinasch.unsagamagic.client.gui.GuiBlender;
import mods.hinasch.unsagamagic.client.gui.GuiSpellBookBinder;
import mods.hinasch.unsagamagic.client.gui.GuiTabletDeciphering;
import mods.hinasch.unsagamagic.inventory.container.ContainerBlender;
import mods.hinasch.unsagamagic.inventory.container.ContainerSpellBookBinder;
import mods.hinasch.unsagamagic.inventory.container.ContainerTabletDeciphering;
import mods.hinasch.unsagamagic.tileentity.TileEntityDecipheringTable;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.NpcMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UnsagaGui {

	public static enum Type implements IGuiAttribute{
		EQUIPMENT(0),SKILLPANEL(1),BLENDING(2)
		,TABLET(3),SMITH(4),BARTERING(5),CHEST(6),BINDER(7)
		,SMITH_MINSAGA(8),MANUAL_ABILITY(9),MAP_FIELD(10),MAP_DUNGEON(11);

		private int meta;

		public int getMeta() {
			return meta;
		}

		private Type(int meta) {
			this.meta = meta;
		}

		public static Type fromMeta(int meta){
			return HSLibs.fromMeta(Type.values(), meta);
		}

		@Override
		public Container getContainer(World world,EntityPlayer player,XYZPos pos){
			switch(this){
			case EQUIPMENT:
				return new ContainerEquipment(player.inventory,player);
			case SKILLPANEL:
				return new ContainerSkillPanel(player);
			case BLENDING:
				return new ContainerBlender(player,world);
			case TABLET:

				if(world.getTileEntity(pos) instanceof TileEntityDecipheringTable){
					return new ContainerTabletDeciphering(player,world,pos,(TileEntityDecipheringTable) world.getTileEntity(pos));
				}

				break;
			case SMITH:
				if(LivingHelper.ADAPTER.hasCapability(player) && LivingHelper.ADAPTER.getCapability(player).getMerchant().isPresent()){
					return new ContainerBlacksmithUnsaga((IMerchant) LivingHelper.ADAPTER.getCapability(player).getMerchant().get(), world, player);
				}
				break;
			case BARTERING:
				if(LivingHelper.ADAPTER.hasCapability(player) && LivingHelper.ADAPTER.getCapability(player).getMerchant().isPresent()){
					return new ContainerBartering(world, player, (IMerchant) LivingHelper.ADAPTER.getCapability(player).getMerchant().get());
				}
				break;
			case CHEST:
				if(LivingHelper.ADAPTER.hasCapability(player) && LivingHelper.ADAPTER.getCapability(player).getChest().isPresent()){
					Entity chest = LivingHelper.ADAPTER.getCapability(player).getChest().get();
					if(ChestCapability.adapterEntity.hasCapability(chest)){
						return new ContainerChestUnsaga((IChestBehavior) chest,player);
					}
				}
//				if(ChestBehavior.interactionCache.containsKey(player)){
//					if(ChestBehavior.interactionCache.get(player).first==EnumInteractionType.ENTITY){
//						Entity entity = ChestBehavior.interactionCache.get(player).second;
//						if(entity!=null && ChestBehavior.hasCapability(entity)){
//							return new ContainerChestUnsaga(ChestCapability.adapterEntity.getCapability(entity),player);
//						}
//					}
//					if(ChestBehavior.interactionCache.get(player).first==EnumInteractionType.TILEENTITY){
//						TileEntity te = world.getTileEntity(pos);
//						if(te!=null && ChestBehavior.hasCapability(te)){
//							return new ContainerChestUnsaga(ChestCapability.adapterTE.getCapability(te),player);
//						}
//					}
//				}
				break;
			case BINDER:
				if(player.getHeldItemMainhand()!=null){
					return new ContainerSpellBookBinder(player, player.getHeldItemMainhand());
				}
				break;
			case SMITH_MINSAGA:
				return new ContainerToolCustomizeMinsaga(player);
			case MANUAL_ABILITY:
				return new ContainerTextMenu(player);
			case MAP_FIELD:
			case MAP_DUNGEON:
				return new ContainerUnsagaMap(player);

			default:

				break;

			}
			return null;
		}

		@Override
		public GuiContainer getGui(World world,EntityPlayer player,XYZPos pos){
			switch(this){
			case EQUIPMENT:
				return new GuiEquipment(player);
			case SKILLPANEL:
				return new GuiSkillPanel(player);
			case BLENDING:
				return new GuiBlender(player, world);
			case TABLET:
				if(world.getTileEntity(pos) instanceof TileEntityDecipheringTable){
					return new GuiTabletDeciphering(player,world,pos,(TileEntityDecipheringTable) world.getTileEntity(pos));
				}
				break;
			case SMITH:
				return new GuiBlacksmithUnsaga(new NpcMerchant(player,ChatHandler.createChatComponent("smith")),world,player);
			case BARTERING:
				return new GuiBartering(new NpcMerchant(player,ChatHandler.createChatComponent("merchant")), world, player);
			case CHEST:
				if(LivingHelper.ADAPTER.hasCapability(player) && LivingHelper.ADAPTER.getCapability(player).getChest().isPresent()){
					Entity chest = LivingHelper.ADAPTER.getCapability(player).getChest().get();
					if(ChestCapability.adapterEntity.hasCapability(chest)){
						return new GuiChest((IChestBehavior) chest,player);
					}
				}
//				if(ChestBehavior.interactionCache.containsKey(player)){
//					if(ChestBehavior.interactionCache.get(player).first==EnumInteractionType.ENTITY){
//						Entity entity = ChestBehavior.interactionCache.get(player).second;
//						if(entity!=null && ChestBehavior.hasCapability(entity)){
//							return new GuiChest(ChestBehavior.getCapability(entity),player);
//						}
//					}
//					if(ChestBehavior.interactionCache.get(player).first==EnumInteractionType.TILEENTITY){
//						TileEntity te = world.getTileEntity(pos);
//						if(te!=null && ChestBehavior.hasCapability(te)){
//							return new GuiChest(ChestBehavior.getCapability(te),player);
//						}
//					}
//				}
				break;
			case BINDER:
				if(player.getHeldItemMainhand()!=null){
					return new GuiSpellBookBinder(player, player.getHeldItemMainhand());
				}
				break;
			case SMITH_MINSAGA:
				return new GuiToolCustomizeMinsaga(world,player);
			case MANUAL_ABILITY:
//				ITextMenuAdapter adapter = new AbilityTextMenuAdapter();
				return new GuiTextMenu(player, null);
			case MAP_FIELD:
				return new GuiUnsagaMap(player,GuiUnsagaMap.Type.FIELD);
			case MAP_DUNGEON:
				return new GuiUnsagaMap(player,GuiUnsagaMap.Type.DUNGEON);
			default:
				break;

			}

			return null;
		}



		public void onGuiOpen(PacketOpenGui pgo,MessageContext ctx,EntityPlayer ep ){
			if(Lists.newArrayList(Type.SMITH,Type.BARTERING).contains(fromMeta(pgo.getGuiNumber()))){
				Entity entity = ep.world.getEntityByID(pgo.getComp().getInteger("villager"));
				if(VillagerHelper.hasCustomerCapability(ep) && entity instanceof EntityVillager){
					VillagerHelper.getCustomerCapability(ep).setMerchant((EntityVillager) entity);
				}
			}
		}

		@Override
		public IModBase getMod() {
			// TODO 自動生成されたメソッド・スタブ
			return UnsagaMod.instance;
		}
	}



}
