package mods.hinasch.unsaga.core.inventory.container;

import mods.hinasch.lib.container.ContainerBase;
import mods.hinasch.lib.container.inventory.InventoryHandler;
import mods.hinasch.lib.entity.RangedHelper;
import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.network.PacketGuiButtonBaseNew;
import mods.hinasch.lib.util.ChatHandler;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.ability.EventRefleshAbilityModifier;
import mods.hinasch.unsaga.core.advancement.UnsagaTriggers;
import mods.hinasch.unsaga.core.client.gui.GuiEquipment;
import mods.hinasch.unsaga.core.inventory.AccessorySlotCapability;
import mods.hinasch.unsaga.core.inventory.IAccessoryInventory;
import mods.hinasch.unsaga.core.inventory.InventoryEquipment;
import mods.hinasch.unsaga.core.inventory.slot.SlotAccessory;
import mods.hinasch.unsaga.core.item.wearable.ItemAccessoryUnsaga;
import mods.hinasch.unsaga.core.net.packet.PacketGuiButtonUnsaga;
import mods.hinasch.unsaga.core.potion.UnsagaPotions;
import mods.hinasch.unsaga.core.world.chunk.IUnsagaChunk;
import mods.hinasch.unsaga.core.world.chunk.UnsagaChunkCapability;
import mods.hinasch.unsaga.init.UnsagaGui;
import mods.hinasch.unsaga.skillpanel.SkillPanel;
import mods.hinasch.unsaga.skillpanel.SkillPanelAPI;
import mods.hinasch.unsaga.skillpanel.SkillPanels;
import mods.hinasch.unsaga.status.UnsagaXPCapability;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class ContainerEquipment extends ContainerBase{


	protected EntityPlayer player;
	protected InventoryEquipment invEquipment;
	protected InventoryBasic dummy;
	protected IInventory invPlayer;

	//	public static final String KEY = "unsaga.equipment";


	public ContainerEquipment(InventoryPlayer playerIn, EntityPlayer ep)
	{
		super(ep,null);
		this.player = ep;
		this.invPlayer = playerIn;
		this.invEquipment = new InventoryEquipment(ep);
		this.inv = this.invEquipment;
		this.dummy = new InventoryBasic("dummy",false,10);

		if(WorldHelper.isServer(ep.getEntityWorld()))UnsagaTriggers.OPEN_GUI_UNSAGA.trigger((EntityPlayerMP) ep);
//		ep.addStat(UnsagaModCore.instance().achievements.openInv, 1);

		if(AccessorySlotCapability.adapter.hasCapability(ep)){
			this.loadAndSetAccessories(AccessorySlotCapability.adapter.getCapability(ep));
		}


		this.addSlotToContainer(new SlotAccessory(this.invEquipment, 0, 28, 53-(18*2)));
		this.addSlotToContainer(new SlotAccessory(this.invEquipment, 1, 28+(18*2)-8, 53-(18*2)));

		//
		//		if(WorldHelper.isServer(this.ep.getEntityWorld())){
		//			UnsagaMod.packetDispatcher.sendTo(PacketSyncSkillPanel.create(this.player), (EntityPlayerMP) player);
		//		}

		//		World w = ep.getEntityWorld();
		//		XYZPos pos = XYZPos.createFrom(ep);
		//		EnvironmentalCondition condition = EnvironmentalManager.getCondition(w, pos, w.getBiome(pos), ep);
		//		this.addSlotToContainer(new SlotIcon(this.dummy, 2,8,64));
		//		dummy.setInventorySlotContents(2, ItemIconCondition.getIcon(condition));
		//		this.addSlotToContainer(new SlotTablet(this.invEquipment,2, 28, 53));
		//this.addSlotToContainer(new SlotMerchantResult(par1InventoryPlayer.player, par2IMerchant, this.merchantInventory, 2, 120, 53));
		//int i;

		//		this.attachIcons(dummy, player, MapSkill.MAP_SKILLS,new XY(28,53),input ->((MapSkill)input).getIconFromPlayer(player));
		//		ListHelper.stream(MapSkill.mapSkills).forEach(new Consumer<MapSkill>(){
		//
		//			@Override
		//			public void accept(MapSkill input) {
		//				addSlotToContainer(new SlotIcon(dummy, input.getNumber(), 28+(18*input.getNumber()), 53));
		//				ItemStack is = input.getIconFromPlayer(player);
		//				if(!input.panelChecker.apply(player)){
		//					ComponentSelectableIcon.setNegative(is, true);
		//				}
		//				dummy.setInventorySlotContents(input.getNumber(), is);
		//			}}
		//		);

		UnsagaXPCapability.syncAdditionalXP(ep);
	}

	protected void loadAndSetAccessories(IAccessoryInventory data){
		this.invEquipment.setInventorySlotContents(0, data.getEquippedList().get(0));
		this.invEquipment.setInventorySlotContents(1, data.getEquippedList().get(1));
		//		this.invEquipment.setInventorySlotContents(2, data.getTablet());
	}
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		// TODO 自動生成されたメソッド・スタブ
		return this.player.openContainer != player.inventoryContainer;
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{
		this.invEquipment.closeInventory(par1EntityPlayer);
		EventRefleshAbilityModifier.refresh(par1EntityPlayer);
		return;
	}

	@Override
	public PacketGuiButtonBaseNew getPacketGuiButton(int guiID, int buttonID,
			NBTTagCompound args) {
		// TODO 自動生成されたメソッド・スタブ

		return PacketGuiButtonUnsaga.create(UnsagaGui.Type.fromMeta(guiID), buttonID, args);//PacketGuiButtonUnsaga.create(UnsagaGui.Type.EQUIPMENT, buttonID, args);
	}

	@Override
	public SimpleNetworkWrapper getPacketPipeline() {
		// TODO 自動生成されたメソッド・スタブ
		return UnsagaMod.packetDispatcher;
	}

	@Override
	public int getGuiID() {
		// TODO 自動生成されたメソッド・スタブ
		return UnsagaGui.Type.EQUIPMENT.getMeta();
	}

	@Override
    public void onCraftMatrixChanged(IInventory inventoryIn)
    {
        super.onCraftMatrixChanged(inventoryIn);
        EventRefleshAbilityModifier.refresh(player);

    }

	@Override
	public void onPacketData() {


		XYZPos p = XYZPos.createFrom(player);
		switch(this.buttonID){
		case GuiEquipment.BUTTON_OPEN_SKILLS:
			UnsagaMod.logger.trace("open skills", "called");
			p = XYZPos.createFrom(player);
			this.player.closeScreen();
			HSLibs.openGui(player, UnsagaMod.instance, UnsagaGui.Type.SKILLPANEL.getMeta(), this.player.world, p);

			break;
			//		case GuiEquipment.BUTTON_OPEN_BLEND:
			//			if(SkillPanels.hasPanel(this.ep.worldObj, ep, UnsagaMod.skillPanels.spellBlend)){
			//				p = XYZPos.createFrom(player);
			//				this.player.closeScreen();
			//				HSLibs.openGui(player, UnsagaMod.instance, UnsagaGui.Type.BLENDING.getMeta(), this.player.worldObj, p);
			//			}
			//			UnsagaMod.logger.trace("open blends", "called");
			//			break;
		case GuiEquipment.ROAD_ADVISER:
			HSLibs.openGui(player, UnsagaMod.instance, UnsagaGui.Type.MAP_FIELD.getMeta(), this.player.world, p);
			break;
		case GuiEquipment.CAVERN_EXPLORER:
			HSLibs.openGui(player, UnsagaMod.instance, UnsagaGui.Type.MAP_DUNGEON.getMeta(), this.player.world, p);
			break;
		case GuiEquipment.SPELL_BLEND:
			HSLibs.openGui(player, UnsagaMod.instance, UnsagaGui.Type.BLENDING.getMeta(), this.player.world, p);
			break;
		case GuiEquipment.WEAPON_FORGE:
			HSLibs.openGui(player, UnsagaMod.instance, UnsagaGui.Type.SMITH_MINSAGA.getMeta(), this.player.world, p);
			break;
		case GuiEquipment.EASY_REPAIR:
			ItemStack is = ep.getHeldItemMainhand();
			if(GuiEquipment.isItemStackRepairable(is, ep)){
				ep.experienceLevel -= 1;
				this.playSoundFromServer(XYZPos.createFrom(ep), SoundEvents.BLOCK_ANVIL_USE, player);
				int repair = -10 - 5 * (SkillPanelAPI.getHighestPanelLevel(ep, SkillPanels.QUICK_FIX).getAsInt()-1);
				is.damageItem(repair, ep);
				ChatHandler.sendChatToPlayer(ep, String.format("%s is repaired %d damage.", is.getDisplayName(),-repair));
				if(is.getItemDamage()<0){
					is.setItemDamage(0);
				}

			}

			break;
		case GuiEquipment.EAVESDROP:
			ep.closeScreen();
			this.playSoundFromServer(XYZPos.createFrom(ep), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, player);
			ep.getFoodStats().addExhaustion(0.5F);
			RangedHelper.create(ep.getEntityWorld(), ep, 30).setSelector((self,in) -> in!=ep)
			.setConsumer((self,in)->{
				in.addPotionEffect(new PotionEffect(MobEffects.GLOWING,ItemUtil.getPotionTime(5),0));
			}).invoke();
			break;
		case GuiEquipment.JUMP:
			ep.closeScreen();
			this.playSoundFromServer(XYZPos.createFrom(ep), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, player);
			ep.getFoodStats().addExhaustion(0.5F);
			if(WorldHelper.isServer(ep.getEntityWorld())){
				ep.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST,ItemUtil.getPotionTime(10),SkillPanelAPI.getHighestPanelLevel(ep, SkillPanels.OBSTACLE_CROSSING).getAsInt()));
			}


			break;
		case GuiEquipment.SPEED:
			ep.closeScreen();
			this.playSoundFromServer(XYZPos.createFrom(ep), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, player);
			ep.getFoodStats().addExhaustion(0.5F);
			if(WorldHelper.isServer(ep.getEntityWorld())){
				ep.addPotionEffect(new PotionEffect(MobEffects.SPEED,this.calcPotioinTime(SkillPanels.SMART_MOVE),0));
				ep.addPotionEffect(new PotionEffect(UnsagaPotions.instance().SILENT_MOVE,this.calcPotioinTime(SkillPanels.SMART_MOVE),0));
			}

			break;
		}

	}

	private void processChestAppear(){
		World world = ep.getEntityWorld();
		Chunk chunk = world.getChunkFromBlockCoords(ep.getPosition());
		IUnsagaChunk instance = UnsagaChunkCapability.ADAPTER.getCapability(chunk);
//		if(instance.getChunkChestInfo().)
	}
	private int calcPotioinTime(SkillPanel panel){
		int level = SkillPanelAPI.getHighestPanelLevel(ep, panel).getAsInt();
		return ItemUtil.getPotionTime(20 * level);
	}

	@Override
	public ItemStack transferStackInSlot(Slot slot)
	{
		InventoryHandler hinvEp = InventoryHandler.create(this.invPlayer);
		InventoryHandler hinvBlender = InventoryHandler.create(this.invEquipment);

		if((slot instanceof SlotAccessory)){
			//		if((slot instanceof SlotAccessory) || (slot instanceof SlotTablet)){
			if(slot.getStack()!=null && hinvEp.getFirstEmptySlotNum().isPresent()){
				hinvEp.swapFirstEmptySlot(slot);
			}
		}else{
			if(slot.getStack()!=null && hinvBlender.getFirstEmptySlotNum().isPresent()){
				ItemStack is = slot.getStack();
				if((is.getItem() instanceof ItemAccessoryUnsaga) && hinvBlender.getEmptySlots(0,1).isPresent()){
					hinvBlender.mergeSlot(slot,hinvBlender.getEmptySlots(0,1).get());
				}
				//				if((is.getItem() instanceof ItemTablet) && hinvBlender.getEmptySlots(2).isPresent()){
				//					hinvBlender.mergeSlot(slot, hinvBlender.getEmptySlots(2).get());
				//				}

			}
		}
		return super.transferStackInSlot(slot);
	}

	//	@Override
	//	public void onSlotClick(int rawSlotNumber,int containerSlotNumber,int clickButton,ClickType clickTypeIn,EntityPlayer ep){
	//		ItemStack is = this.getSlot(rawSlotNumber).getStack();
	//		UnsagaMod.logger.trace("slot1", ComponentSelectableIcon.isNegative(is));
	//		if(is!=null && is.getItem() instanceof ItemIconMapSkill){
	//			this.playClickSound();
	//			int num =  is.getItemDamage();
	//			if(!ComponentSelectableIcon.isNegative(is)){
	//				this.onClickMapSkill(num);
	//			}
	//
	//		}
	//
	//	}


//	@Override
//	public PacketSendGuiInfoToClient getSyncPacketToClient(EntityPlayer ep){
//		boolean hasStat = false;
//		if(ep instanceof EntityPlayerMP){
//			UnsagaTriggers.OPEN_GUI_BARTERING.
//			hasStat = ((EntityPlayerMP)ep).getAdvancements().(UnsagaMod.core.achievements.getTablet);
//		}
//		NBTTagCompound nbt = UtilNBT.compound();
//		nbt.setBoolean("isUnlockDecipher", hasStat);
//		return PacketSendGuiInfoToClient.create(nbt);
//	}
}
