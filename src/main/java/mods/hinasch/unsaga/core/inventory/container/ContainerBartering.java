package mods.hinasch.unsaga.core.inventory.container;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;

import mods.hinasch.lib.container.ContainerBase;
import mods.hinasch.lib.container.inventory.InventoryHandler;
import mods.hinasch.lib.container.inventory.InventoryHandler.TransferStackLogic;
import mods.hinasch.lib.container.inventory.SlotPlayer;
import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.iface.INBTWritable;
import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.network.PacketGuiButtonBaseNew;
import mods.hinasch.lib.network.PacketSendGuiInfoToClient;
import mods.hinasch.lib.util.SoundAndSFX;
import mods.hinasch.lib.util.UtilNBT;
import mods.hinasch.lib.util.UtilNBT.RestoreFunc;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.advancement.UnsagaTriggers;
import mods.hinasch.unsaga.core.client.gui.GuiBartering;
import mods.hinasch.unsaga.core.inventory.InventoryMerchant;
import mods.hinasch.unsaga.core.inventory.slot.SlotMerchant;
import mods.hinasch.unsaga.core.net.packet.PacketGuiButtonUnsaga;
import mods.hinasch.unsaga.core.net.packet.PacketSyncSkillPanel;
import mods.hinasch.unsaga.init.UnsagaGui;
import mods.hinasch.unsaga.skillpanel.SkillPanelAPI;
import mods.hinasch.unsaga.skillpanel.SkillPanels;
import mods.hinasch.unsaga.util.LivingHelper;
import mods.hinasch.unsaga.villager.UnsagaVillagerCapability;
import mods.hinasch.unsaga.villager.bartering.BarteringUtil;
import mods.hinasch.unsaga.villager.bartering.BarteringUtil.PriceDownUpPair;
import mods.hinasch.unsaga.villager.bartering.MerchandiseCapability;
import mods.hinasch.unsaga.villager.bartering.MerchantBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class ContainerBartering extends ContainerBase{

	public static class Entry implements INBTWritable{

		public static RestoreFunc<Entry> RESTORE = new RestoreFunc<Entry>(){

			@Override
			public Entry apply(NBTTagCompound input) {
				int num = (int)input.getByte("num");
				boolean sw = input.getBoolean("sw");
				return new Entry(num,sw);
			}
		};
		public final int num;

		public final boolean sw;
		public Entry(int num,boolean sw){
			this.num = num;
			this.sw = sw;
		}

		@Override
		public void writeToNBT(NBTTagCompound stream) {
			// TODO 自動生成されたメソッド・スタブ
			stream.setByte("num", (byte)this.num);
			stream.setBoolean("sw", this.sw);
		}
	}
	static final int MERCHANDISE_SLOT_START = 36;
	protected World worldobj;
	protected EntityPlayer theCustomer;
	protected IInventory invEp;
	protected IMerchant theMerchant;
	protected SlotMerchant[] merchandiseSlot;

	protected IInventory dummyInv;
//	protected BarteringProcessor barteringProcessor;

	protected PriceDownUpPair discounts;
//	protected int isPriceDown;
//	protected int isPriceUp;

	protected IInventory invSell;

	@Deprecated
	protected IInventory invResult;

	protected InventoryMerchant invMerchant;

	protected MerchantBehavior behavior;

	protected byte selected;

	protected int shopLevel = 0;

	protected InventoryHandler.TransferStackLogic transferLogic;


	Map<Integer,Boolean> checkBox = Maps.newHashMap();
	public ContainerBartering(World world,EntityPlayer ep,IMerchant merchant){

		super(ep, new InventoryMerchant(18,ep,merchant));
		this.worldobj = world;
		this.theCustomer = ep;
		this.invEp = ep.inventory;
		this.theMerchant = merchant;
		this.theMerchant.setCustomer(theCustomer);
		this.invMerchant = (InventoryMerchant) this.inv;
		this.merchandiseSlot = new SlotMerchant[9];
		this.dummyInv = new InventoryBasic("",false,10); //インベントリの連携はいらないがないとダメ…？
		this.spreadSlotItems = false;
		this.invSell = new InventoryMerchant(7,ep,merchant);
		this.invResult = new InventoryMerchant(1,ep,merchant);

		this.discounts = new PriceDownUpPair(0,0);
		this.transferLogic = new TransferStackLogic(invEp, invSell, in -> (in instanceof SlotMerchant.PlayerSell));
		this.transferLogic.setSelfSlotIdentifier(in -> in instanceof SlotPlayer);

		for(int j=0;j<2;j++){
			for (int i = 0; i < 9; ++i)
			{
				this.checkBox.put(i+(9*j), false);
				this.addSlotToContainer(new SlotMerchant.MerchantSell(this.invMerchant, i + (j*9), 8 + i * 18, (j*18)+63-(18*3)));
			}



		}

		for (int i = 0; i < this.invSell.getSizeInventory(); ++i)
		{
			this.addSlotToContainer(new SlotMerchant.PlayerSell(this.invSell, i , 8 + i * 18, 62));
		}
		//		this.addSlotToContainer(new SlotMerchant.Result(this.invResult, 0, 8 + 8 * 18, 62));

		//this.addSlotToContainer(new Slot(this.invMerchant,30,152,42));



		if(this.theMerchant instanceof EntityVillager){
			EntityVillager villager = (EntityVillager) this.theMerchant;
			this.behavior = new MerchantBehavior(world, villager);



			if(WorldHelper.isServer(world)){
				this.shopLevel = this.behavior.calcShopLevel(villager);

				if(this.behavior.hasComeUpdateTime()){
					this.behavior.resetDisplayedSecretMerchandise();
					this.behavior.updateMerchandises(this.ep);
				}



				if(UnsagaVillagerCapability.ADAPTER.hasCapability(villager)){
					NonNullList<ItemStack> merchandises = UnsagaVillagerCapability.ADAPTER.getCapability(villager).getMerchandises();
					NonNullList<ItemStack> secrets = UnsagaVillagerCapability.ADAPTER.getCapability(villager).getSecretMerchandises();
//					merchandises.setToInventory(0, this.invMerchant);
//					secrets.setToInventory(9, this.invMerchant);
					ItemUtil.transferToInventory(merchandises, invMerchant, 0);
					ItemUtil.transferToInventory(secrets, invMerchant, 9);
				}
			}



		}


		if(this.ep instanceof EntityPlayerMP){
			//			NBTTagCompound comp = new NBTTagCompound();
			//			comp.setString("test", "てすと！");
			//			HSLib.core().getPacketDispatcher().sendTo(PacketSendGuiInfoToClient.create(comp), (EntityPlayerMP) this.ep);
			UnsagaMod.packetDispatcher.sendTo(PacketSyncSkillPanel.create(ep), (EntityPlayerMP) ep);
		}

		//		this.attachIcons(dummyInv, theCustomer, ShopSkill.SHOP_SKILLS,new XY(28,29));
		//		ListHelper.stream(ShopSkill.shopSkills).forEach(new Consumer<ShopSkill>(){
		//
		//			@Override
		//			public void accept(ShopSkill input) {
		//				addSlotToContainer(new SlotIcon(dummyInv, input.getNumber(), 28+(18*input.getNumber()), 29));
		//				ItemStack is = input.getIcon();
		//				if(!input.panelChecker.apply(theCustomer)){
		//					ComponentSelectableIcon.setNegative(is, true);
		//				}
		//				dummyInv.setInventorySlotContents(input.getNumber(), is);
		//			}}
		//		);


		//		this.ep.addStat(UnsagaModCore.instance().achievements.openVillager);

	}
//	/**
//	 * 目利きを使った時。掘り出しものを発見する
//	 */
//	public void addSecretMerchandise(){
//		if(UnsagaVillager.hasCapability((EntityVillager) this.theMerchant)){
//			IUnsagaVillager capa = UnsagaVillager.getCapability((EntityVillager) this.theMerchant);
//			if(!capa.hasFoundSecret()){
//				Random rand = this.theCustomer.getRNG();
//				ItemStack newMerchandise = capa.getSecretMerchandise(0);
//				//				this.invMerchant.setMerchandise(8, newMerchandise);
//				capa.setFoundSecret(true);
//				NBTTagCompound comp = UtilNBT.compound();
//				newMerchandise.writeToNBT(comp);
//				UnsagaMod.packetDispatcher.sendTo(PacketSyncGui.create(PacketSyncGui.Type.BARTERING_SECRET, comp),(EntityPlayerMP) this.ep);
//			}
//		}
//		//		ExtendedMerchantData data = ExtendedMerchantData.getData((EntityVillager) this.theMerchant);
//		//		if(data.hasFoundSecret())return;
//		//		Random rand = new Random();
//		//		ItemStack newMerchandise = data.getSecretMerchandise(0);
//		//		this.invMerchant.setMerchandise(8, newMerchandise);
//		////		this.invMerchant.markDirty();
//		//
//		//		data.setFoundSecret(true);
//		//		PacketSyncGui psg = new PacketSyncGui(PacketSyncGui.SYNC_BARTERING_SECRET,newMerchandise);
//		//		Unsaga.packetDispatcher.sendTo(psg,(EntityPlayerMP) this.ep);
//		//		if(this.invMerchant.getHelper()!=null){
//		//			Unsaga.debug("addsecret",this.worldobj.isRemote);
//		//			int ad = (SkillPanels.getHighestLevelOfPanel(worldobj, ep, Unsaga.skillPanels.fashionable) +1) + (SkillPanels.getHighestLevelOfPanel(worldobj, ep, Unsaga.skillPanels.luckyFind)+1);
//		//			int repu = ExtendedMerchantData.getData((EntityVillager) this.theMerchant).getDistributionLevel() + 5 +ad;
//		//			EnumMerchantType type = MerchantHelper.merchantTypeSelector.apply((EntityVillager) this.theMerchant);
//		//			ItemStack is = MerchandiseInfo.getRandomMerchandise(rand,repu,type);
//		//			MerchandiseInfo.setBuyPriceTag(is,MerchandiseInfo.getPrice(is)*3);
//		//			this.invMerchant.setMerchandise(8, is);
//		//			data.setFoundSecret(true);
//		//		}
//
//	}

	public boolean canBuy(){
		boolean flag = InventoryHandler.create(invSell).toStream(0, this.invSell.getSizeInventory()).anyMatch(in -> ItemUtil.isItemStackPresent(in.getStack()));
		boolean flag2 = InventoryHandler.create(invSell).toStream(0, this.invSell.getSizeInventory()).filter(in -> ItemUtil.isItemStackPresent(in.getStack()))
				.allMatch(in -> {
					if(MerchandiseCapability.adapter.hasCapability(in.getStack())){
						return MerchandiseCapability.adapter.getCapability(in.getStack()).getPrice(in.getStack())>0;
					}
					return false;
					});
		boolean flag3 = this.checkBox.entrySet().stream().filter(in -> in.getValue())
				.anyMatch(in -> ItemUtil.isItemStackPresent(this.invMerchant.getStackInSlot(in.getKey())));
		return flag && flag2 && flag3;
	}
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		// TODO 自動生成されたメソッド・スタブ
		return this.theCustomer!=null && this.theCustomer == entityplayer;
	}

	public void cleanBarteringInv(){
		//		InventoryHandler.create(this.invMerchant).forEach(status ->{
		//
		//			int num = status.getStackNumber();
		//			if(invMerchant.getBartering(num)!=null){
		//				invMerchant.setBartering(num, null);
		//			}
		//		}, new Range(0,this.invMerchant.getSizeBarteringInv()));
		//		InventoryHandler.create(this.invMerchant).forEach(new Consumer<InventoryStatus>(){
		//
		//			@Override
		//			public void accept(InventoryStatus input) {
		//				InventoryMerchantUnsaga inv = (InventoryMerchantUnsaga) input.getParent();
		//				int num = input.getStackNumber();
		//				if(inv.getBartering(num)!=null){
		//					inv.setBartering(num, null);
		//					//					if(BarteringProcessor.checkSellingPosibility(inv.getBartering(num))){
		//					//						inv.setBartering(num, null);
		//					//					}
		//				}
		//			}
		//		}, new Range(0,this.invMerchant.getSizeBarteringInv()));

	}



	public int getBuyPrice(){
		//		this.checkBox.entrySet().stream().forEach(in -> UnsagaMod.logger.trace("stream", in));
		return this.checkBox.entrySet().stream().filter(in -> in.getValue()).map(in -> this.invMerchant.getStackInSlot(in.getKey()))
				.filter(in -> ItemUtil.isItemStackPresent(in)).mapToInt(in -> {
					if(MerchandiseCapability.adapter.hasCapability(in)){
						return BarteringUtil.getDiscountPrice(MerchandiseCapability.adapter.getCapability(in).getPrice(in)*2, this.discounts);
					}
					return 0;
				}).sum();
	}

	public PriceDownUpPair getDiscount(){
		return this.discounts;
	}

	@Override
	public int getGuiID() {
		// TODO 自動生成されたメソッド・スタブ
		return UnsagaGui.Type.BARTERING.getMeta();
	}

	public InventoryMerchant getMerchantInventory(){
		return this.invMerchant;//this.invMerchant;
	}
	@Override
	public PacketGuiButtonBaseNew getPacketGuiButton(int guiID, int buttonID,
			NBTTagCompound args) {
		//		args.setByte("button", this.selected);
		return PacketGuiButtonUnsaga.create(UnsagaGui.Type.fromMeta(guiID), buttonID, args);
	}



	@Override
	public SimpleNetworkWrapper getPacketPipeline() {
		// TODO 自動生成されたメソッド・スタブ
		return UnsagaMod.packetDispatcher;
	}

	public int getSellPrice(){

		return InventoryHandler.create(this.invSell).toStream(0, this.invSell.getSizeInventory())
				.mapToInt(in -> {
					if(in.getStack()!=null && MerchandiseCapability.adapter.hasCapability(in.getStack())){
						return MerchandiseCapability.adapter.getCapability(in.getStack()).getPrice(in.getStack()) * in.getStack().getCount();
					}
					return 0;
				}).sum();
	}

	public PacketSendGuiInfoToClient getSyncPacketToClient(EntityPlayer ep){
		NBTTagCompound comp = UtilNBT.compound();
		comp.setInteger("shopLevel", this.shopLevel);
		if(this.theMerchant instanceof EntityVillager){
			EntityVillager villager = (EntityVillager) this.theMerchant;
			if(UnsagaVillagerCapability.ADAPTER.hasCapability(villager)){
				comp.setInteger("distLV", UnsagaVillagerCapability.ADAPTER.getCapability(villager).getDistributionLevel());
				comp.setInteger("transXP", UnsagaVillagerCapability.ADAPTER.getCapability(villager).getTransactionPoint());
				comp.setByte("shopType", (byte)UnsagaVillagerCapability.ADAPTER.getCapability(villager).getShopType().getMeta());
				comp.setInteger("nextTime", (int)(this.worldobj.getTotalWorldTime() - UnsagaVillagerCapability.ADAPTER.getCapability(villager).getRecentStockedTime()));

			}

		}
		List<ContainerBartering.Entry> list = this.checkBox.entrySet().stream().map(in -> new ContainerBartering.Entry(in.getKey(),in.getValue())).collect(Collectors.toList());
		UtilNBT.writeListToNBT(list, comp, "checkBox");
		return PacketSendGuiInfoToClient.create(comp);
	}

	public World getWorld(){
		return this.worldobj;
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{
		super.onContainerClosed(par1EntityPlayer);
		if(this.theMerchant instanceof EntityVillager && UnsagaVillagerCapability.ADAPTER.hasCapability((Entity) this.theMerchant)){
			EntityVillager villager = (EntityVillager) this.theMerchant;
			UnsagaVillagerCapability.ADAPTER.getCapability(villager).setMerchandises(InventoryHandler.create(invMerchant).crop(0, 9));
			UnsagaVillagerCapability.ADAPTER.getCapability(villager).setSecretMerchandises(InventoryHandler.create(invMerchant).crop(9, 9));
		}
		if(LivingHelper.ADAPTER.hasCapability(ep)){
			LivingHelper.ADAPTER.getCapability(ep).setMerchant(Optional.empty());
		}
		this.invMerchant.closeInventory(par1EntityPlayer);

		this.theMerchant.setCustomer((EntityPlayer)null);

		if(WorldHelper.isServer(this.worldobj)){
			InventoryHandler.create(this.invSell).toStream(0, this.invSell.getSizeInventory())
			.forEach(in -> ItemUtil.dropItem(in.getStack(), ep));
		}
		//		if(WorldHelper.isServer(worldobj)){
		//			ItemUtil.dropItem(par1EntityPlayer, 0.1F, IntStream.range(0, invMerchant.getSizeBarteringInv())
		//					.mapToObj(num -> invMerchant.removeStackFromSlot(num)));
		//
		////			ItemStack is = this.invMerchant.removeStackFromSlot(this.invMerchant.RESULT);
		////			if(is!=null){
		////				par1EntityPlayer.entityDropItem(is,0.1F);
		////			}
		//		}
	}

	@Override
	public void onPacketData() {

		this.selected = (byte)this.buttonID;
		UnsagaMod.logger.trace(this.getClass().getName(), this.buttonID);
		if(this.buttonID == GuiBartering.BUTTON_BUY){
			if(this.getBuyPrice()<=this.getSellPrice() && this.canBuy()){
				this.processBuying();
			}
		}

		switch(this.selected){
		case GuiBartering.BUTTON_DOWN_PRICE:
			this.discounts = BarteringUtil.applyDiscount(ep);
			break;
		case GuiBartering.BUTTON_UP_PRICE:
			this.discounts = BarteringUtil.applyGratuity(ep);
			break;
		case GuiBartering.BUTTON_GET_SECRET:
			if(!this.behavior.hasDisplayedSecrets()){
				int level = SkillPanelAPI.getHighestPanelLevel(ep, SkillPanels.ARTISTE).getAsInt();
				NonNullList<ItemStack> secrets = this.behavior.createSecretMerchandises(level);
				if(!secrets.isEmpty()){
					for(int i=0;i<secrets.size();i++){
						Slot slot = this.getSlotFromInventory(invMerchant, 9+i);
						slot.putStack(secrets.get(i));
						this.detectAndSendChanges();
					}
				}

//				secrets.setToInventory(9, this.invMerchant);
			}
			break;
		}
		//		Unsaga.debug(this.selected,this.getClass().getName());
		//		if(this.invMerchant.getMerchandise(this.selected)!=null){
		//
		//			this.progressBuying(this.selected);
		//		//		}
		//		PacketSyncGui psg = null;
		//		switch(this.selected){
		//		case GuiBartering.BUTTON_GET_SECRET:
		//			if(SkillPanels.hasPanel(this.worldobj, this.ep, Unsaga.skillPanels.luckyFind)){
		//				this.addSecretMerchandise();
		//			}else{
		//				this.showNoAbilityMessage();
		//			}
		//
		//			break;
		//		case GuiBartering.BUTTON_UP_PRICE:
		//			if(SkillPanels.hasPanel(ep.worldObj, ep, Unsaga.skillPanels.gratuity)){
		//				this.isPriceUp = new Supplier<Integer>(){
		//
		//					@Override
		//					public Integer get() {
		//						int base = SkillPanels.getHighestLevelPanel(ep.worldObj,ep, Unsaga.skillPanels.gratuity) + 1;
		//						if(SkillPanels.hasPanel(worldobj, ep, Unsaga.skillPanels.fashionable)){
		//							base += SkillPanels.getHighestLevelPanel(ep.worldObj, ep, Unsaga.skillPanels.fashionable) + 1;
		//						}
		//						return base;
		//					}
		//				}.get();
		//
		//				Unsaga.debug(this.isPriceUp,this.getClass());
		//				this.isPriceDown = 0;
		//				psg = new PacketSyncGui(PacketSyncGui.SYNC_BARTERINGGUI,this.isPriceDown,this.isPriceUp);
		//				Unsaga.packetDispatcher.sendTo(psg, (EntityPlayerMP) this.ep);
		//			}else{
		//				this.showNoAbilityMessage();
		//			}
		//
		//			break;
		//		case GuiBartering.BUTTON_DOWN_PRICE:
		//			if(SkillPanels.hasPanel(ep.worldObj, ep, Unsaga.skillPanels.discount)){
		//				this.isPriceDown = new Supplier<Integer>(){
		//
		//					@Override
		//					public Integer get() {
		//						int base = SkillPanels.getHighestLevelPanel(ep.worldObj,ep, Unsaga.skillPanels.discount) + 1;
		//						if(SkillPanels.hasPanel(worldobj, ep, Unsaga.skillPanels.fashionable)){
		//							base += SkillPanels.getHighestLevelPanel(ep.worldObj, ep, Unsaga.skillPanels.fashionable) + 1;
		//						}
		//						return base;
		//					}
		//				}.get();
		//
		//				Unsaga.debug(this.isPriceDown,this.getClass());
		//				this.isPriceUp = 0;
		//				psg = new PacketSyncGui(PacketSyncGui.SYNC_BARTERINGGUI,this.isPriceDown,this.isPriceUp);
		//				Unsaga.packetDispatcher.sendTo(psg, (EntityPlayerMP) this.ep);
		//			}else{
		//				this.showNoAbilityMessage();
		//			}
		//
		//			break;
		//		}


	}




	public synchronized void processBuying(){
		this.behavior.addTransactionPoint(this.getBuyPrice());
//		UnsagaMod.logger.splitter("1");
		this.checkBox.entrySet().stream().filter(in -> in.getValue()).map(in -> this.invMerchant.getStackInSlot(in.getKey()))
		.filter(in -> ItemUtil.isItemStackPresent(in)).map(in -> in.copy())
		.forEach(in -> ItemUtil.dropItem(in, ep));
//		UnsagaMod.logger.splitter("2");
		this.checkBox.entrySet().stream().filter(in -> in.getValue()).forEach(in -> this.invMerchant.setInventorySlotContents(in.getKey(),null));
		InventoryHandler.create(this.invSell).fill(null, 0, 7);
//		UnsagaMod.logger.splitter("3");

		UnsagaTriggers.OPEN_GUI_BARTERING.trigger((EntityPlayerMP) ep);
//		this.ep.addStat(UnsagaAchievementRegistry.instance().bartering1);
	}

	@Deprecated
	public void progressBuying(int invNum){
		//		BarteringProcessor transaction = this.invMerchant.getBehaviorAgent().newTransaction(theCustomer, this)
		//				.setBuyingItem(this.invMerchant.getMerchandise(invNum));
		//		if(transaction.canBuy()){
		//			ItemStack bought = transaction.getStackBuying().copy();
		//			transaction.finishBuying(invNum,bought);
		//			if(WorldHelper.isServer(worldobj)){
		//				XYZPos xyz = XYZPos.createFrom((Entity) this.theMerchant);
		//
		//				transaction.increaseDistributionPoint(this.isPriceUp);
		//			}
		//
		//			this.cleanBarteringInv();
		//		}
		//			Unsaga.debug(this.worldobj.getTotalWorldTime());


	}
//	public void sendPriceSyncPacketToClient(){
//		if(WorldHelper.isServer(ep.world)){
//			//			PacketSyncGui psg = new PacketSyncGui(PacketSyncGui.SYNC_BARTERINGGUI,this.isPriceDown,this.isPriceUp);
////			UnsagaMod.logger.trace(this.getClass().getName(), this.isPriceDown,this.isPriceUp);
//			NBTTagCompound comp = UtilNBT.compound();
//			comp.setInteger("priceUp", this.discounts.priceUp());
//			comp.setInteger("priceDown", this.discounts.priceDown());
//			UnsagaMod.packetDispatcher.sendTo(PacketSyncGui.create(PacketSyncGui.Type.BARTERING_PRICE,comp), (EntityPlayerMP) this.ep);
//		}
//
//	}

	public void setDiscount(PriceDownUpPair p){
		this.discounts = p;
	}
	public void setMerchandiseTag(){
		//		this.invMerchant.setMerchandiseTag();
	}


	@Override
	public ItemStack slotClick(int par1, int dragType, ClickType clickTypeIn, EntityPlayer player)
	{


//		UnsagaMod.logger.trace("slot", par1);
		if(par1>=MERCHANDISE_SLOT_START && MERCHANDISE_SLOT_START+18>par1 ){
			if(WorldHelper.isClient(this.ep.world)){
				SoundAndSFX.playPositionedSoundRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F);
			}
			if(player instanceof EntityPlayerMP){
				boolean b = this.checkBox.get(par1-36);
				this.checkBox.put(par1-36, !b);
				HSLib.getPacketDispatcher().sendTo(this.getSyncPacketToClient(player), (EntityPlayerMP) player);
			}

		}



		//		if(par1>=36 && par1<=44){
		//			//			Unsaga.debug(par1,par2,par3,this.getClass().getName());
		//			if(this.getSlot(par1).getStack()!=null){
		//				this.playClickSound();
		//				this.progressBuying(par1-36);
		//			}
		//
		//		}

		return super.slotClick(par1, dragType, clickTypeIn, player);


	}
	public void syncSecretSlot(ItemStack is){
		//		this.invMerchant.setMerchandise(8, is);
	}

	@Override
	public ItemStack transferStackInSlot(Slot slot)
	{
		return this.transferLogic.transferStackInSlot(slot);
//		InventoryHandler hinvEp = InventoryHandler.create(this.invEp);
//		//		InventoryHandler hinvSm = InventoryHandler.create(this.invMerchant);
//		InventoryHandler hinvSell = InventoryHandler.create(this.invSell);
//		if(slot instanceof SlotMerchant.PlayerSell){
//			if(!slot.getStack().isEmpty() && hinvEp.getFirstMergeableOrEmptySlot(slot.getStack()).isPresent()){
//				hinvEp.merge(hinvEp.getFirstMergeableOrEmptySlot(slot.getStack()).getAsInt(), slot);
//			}
//
//		}
//
//		if(!(slot instanceof SlotMerchant.PlayerSell && slot instanceof SlotMerchant.MerchantSell)){
//			if(slot.getStack()!=null && hinvSell.getFirstMergeableOrEmptySlot(slot.getStack()).isPresent()){
//				hinvSell.merge(hinvSell.getFirstMergeableOrEmptySlot(slot.getStack()).getAsInt(), slot);
//			}
//		}
//		return super.transferStackInSlot(slot);
	}

	/**クライアント側 */
	public void upDateCheckBox(Map<Integer,Boolean> map){
		this.checkBox = map;
	}
}
