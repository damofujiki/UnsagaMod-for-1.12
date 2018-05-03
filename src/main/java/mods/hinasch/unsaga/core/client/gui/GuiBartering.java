package mods.hinasch.unsaga.core.client.gui;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import mods.hinasch.lib.client.GuiContainerBase;
import mods.hinasch.lib.container.inventory.InventoryHandler;
import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.network.PacketSendGuiInfoToClient;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.util.UtilNBT;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.common.IconSkillAssociated;
import mods.hinasch.unsaga.core.inventory.container.ContainerBartering;
import mods.hinasch.unsaga.skillpanel.SkillPanel;
import mods.hinasch.unsaga.skillpanel.SkillPanelAPI;
import mods.hinasch.unsaga.skillpanel.SkillPanels;
import mods.hinasch.unsaga.util.ToolCategory;
import mods.hinasch.unsaga.util.UnsagaTextFormatting;
import mods.hinasch.unsaga.villager.bartering.BarteringMaterialCategory;
import mods.hinasch.unsaga.villager.bartering.BarteringShopType;
import mods.hinasch.unsaga.villager.bartering.BarteringUtil;
import mods.hinasch.unsaga.villager.bartering.BarteringUtil.PriceDownUpPair;
import mods.hinasch.unsaga.villager.bartering.MerchandiseCapability;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class GuiBartering extends GuiContainerBase{

	//protected final ResourceLocation background = new ResourceLocation(Unsaga.DOMAIN + ":textures/gui/shop.png");
	protected ContainerBartering container;
	protected IMerchant merchant;
	protected EntityPlayer ep;
	protected World world;
	public static final int BUTTON_GET_SECRET = 0;
	public static final int BUTTON_UP_PRICE = 1;
	public static final int BUTTON_DOWN_PRICE = 2;
	public static final int ICON_INFO = 4;
	public static final int BUTTON_BUY = 5;

	boolean hasSync = false;
	int shopLevel = 0;
	int distributionLevel = 0;
	int transactionXP = 0;
	int nextStockTime = 0;
	BarteringShopType type = BarteringShopType.UNKNOWN;
//	private PriceDownUpPair discounts = new PriceDownUpPair(0,0);
	Map<Integer,Boolean> checkBox = Maps.newHashMap();

	public GuiBartering(IMerchant merchant,World world,EntityPlayer ep) {
		super(new ContainerBartering(world,ep,merchant));
		// TODO 自動生成されたコンストラクター・スタブ
		this.merchant = merchant;
		this.world = world;
		this.container = (ContainerBartering)this.inventorySlots;
		this.ep = ep;
		this.addIcon(new Icon(ICON_INFO, -32, 0, 48, 168, true));
		this.addIcon(new IconSkill(BUTTON_DOWN_PRICE, -32, 32, 0, 168, true,SkillPanels.MONGER));
		this.addIcon(new IconSkill(BUTTON_GET_SECRET, -32, 48, 16, 168, true,SkillPanels.ARTISTE));
		this.addIcon(new IconSkill(BUTTON_UP_PRICE, -32, 64, 32, 168, true,SkillPanels.MAHARAJA));
		this.addIcon(new IconButton(BUTTON_BUY, 150, 61, 80, 168, true));
	}

	@Override
	public int MessageYpos(){
		return 100;

	}

	@Override
	public List<String> getIconHoverText(Icon icon){
		List<String> list = Lists.newArrayList();
		if(icon.id==BUTTON_BUY){
			list.add("BUY THEM!");
		}
		if(icon.id==ICON_INFO){
			if(this.hasSync){
				list.add("Shop Level:"+this.shopLevel);
				list.add("Distribution Level:"+this.distributionLevel);
				list.add(String.format("Transaction Point:%d/%d",this.transactionXP,BarteringUtil.calcNextTransactionThreshold(distributionLevel)));
				list.add("Next Stock Interval:"+(24000-this.nextStockTime));
				if(this.type!=BarteringShopType.UNKNOWN){
					List<ToolCategory> category = Lists.newArrayList(type.getAvailableMerchandiseCategory());
					Collections.sort(category);
					String msg1 = Joiner.on(",").join(category.stream().map(in->in.getLocalized()).collect(Collectors.toList()));
					list.add(HSLibs.translateKey("gui.unsaga.bartering.info.category")+":"+msg1);

					List<BarteringMaterialCategory.Type> types = Lists.newArrayList(type.getAvailableTypes());
					Collections.sort(types);
					String msg2 = Joiner.on(",").join(types.stream().map(in->in.getLocalized()).collect(Collectors.toList()));
					list.add(HSLibs.translateKey("gui.unsaga.bartering.info.material")+":"+msg2);
					if(HSLib.isDebug()){
						list.add("type:"+this.type);
					}
				}
			}else{
				this.hasSync = true;
				HSLib.getPacketDispatcher().sendToServer(PacketSendGuiInfoToClient.request());
			}

		}
		if(icon.id==BUTTON_DOWN_PRICE){
			list.add(HSLibs.translateKey("skillPanel.discount.name"));
			list.add(HSLibs.translateKey("gui.unsaga.bartering.discount"));
			if(SkillPanelAPI.hasPanel(ep, SkillPanels.MONGER)){
				list.add(HSLibs.translateKey("gui.unsaga.bartering.available"));
			}else{
				list.add(UnsagaTextFormatting.PROPERTY_LOCKED+HSLibs.translateKey("gui.unsaga.bartering.unavailable"));
			}
		}
		if(icon.id==BUTTON_UP_PRICE){
			list.add(HSLibs.translateKey("skillPanel.gratuity.name"));
			list.add(HSLibs.translateKey("gui.unsaga.bartering.gratuity"));
			if(SkillPanelAPI.hasPanel(ep, SkillPanels.MAHARAJA)){
				list.add(HSLibs.translateKey("gui.unsaga.bartering.available"));
			}else{
				list.add(UnsagaTextFormatting.PROPERTY_LOCKED+HSLibs.translateKey("gui.unsaga.bartering.unavailable"));
			}
		}
		if(icon.id==BUTTON_GET_SECRET){
			list.add(HSLibs.translateKey("skillPanel.luckyFind.name"));
			list.add(HSLibs.translateKey("gui.unsaga.bartering.luckyFind"));
			if(SkillPanelAPI.hasPanel(ep, SkillPanels.ARTISTE)){
				list.add(HSLibs.translateKey("gui.unsaga.bartering.available"));
			}else{
				list.add(UnsagaTextFormatting.PROPERTY_LOCKED+HSLibs.translateKey("gui.unsaga.bartering.unavailable"));
			}
		}
		return list;
	}
	@Override
	protected void drawGuiContainerForegroundLayer(int par1,int par2)
	{


		super.drawGuiContainerForegroundLayer(par1, par2);

		this.checkBox.entrySet().forEach(in ->{
			if(in.getKey()<9 && in.getValue()){
				this.drawTexturedModalRect(7 + in.getKey()  * 18, 8, 0, 184, 18,18);
			}
			if(in.getKey()>=9 && in.getValue()){
				this.drawTexturedModalRect(7 + (in.getKey()-9) * 18, 26, 0, 184, 18,18);
			}
		});

		fontRenderer.drawString(HSLibs.translateKey("gui.unsaga.bartering.amount.sell")+this.container.getSellPrice(),8,170,0xffffff);
		fontRenderer.drawString(HSLibs.translateKey("gui.unsaga.bartering.amount.buy")+this.container.getBuyPrice(),8,186,0xffffff);
//		if(this.container!=null){
//			for(int i=0;i<9;i++){
//				ItemStack is = this.container.getMerchantInventory().getMerchandise(i);
//				if(is!=null){
//					BarteringPriceSupplier.getCapability(is).setMerchandiseItem(true);
//				}
//			}
//		}
//		fontRendererObj.drawString(I18n.translateToLocal("gui.bartering.amount")+this.container.getSellPrice(),8,48,0x404040);
	}

	public void setMerchandiseTag(){
		InventoryHandler.create(this.container.getMerchantInventory()).toStream(0, this.container.getMerchantInventory().getSizeInventory())
		.filter(in -> ItemUtil.isItemStackPresent(in.getStack())).map(in -> in.getStack()).forEach(in ->{
			if(MerchandiseCapability.adapter.hasCapability(in)){
				MerchandiseCapability.adapter.getCapability(in).setMerchandise(true);
			}
		});
	}
	@Override
	public String getGuiTextureName(){
		return UnsagaMod.MODID + ":textures/gui/container/shop.png";
	}

	@Override
	public String getGuiName(){
		return "";
	}

	@Override
	public void initGui()
	{
		super.initGui();
		int i = width  - xSize >> 1;
		int j = height - ySize >> 1;
//		UnsagaMod.packetDispatcher.sendToServer(PacketSyncGui.create(Type.BARTERING_TAGS, null));
//		this.addButton(BUTTON_GET_SECRET, i+7+(0*18), j+85-(18*3), 48, 20 , Translation.localize("gui.bartering.button.secret"));
//		this.addButton(BUTTON_UP_PRICE, i+7+(3*18), j+85-(18*3), 48, 20 , Translation.localize("gui.bartering.button.upprice"));
//		this.addButton(BUTTON_DOWN_PRICE, i+7+(6*18), j+85-(18*3), 48, 20 , Translation.localize("gui.bartering.button.downprice"));
//
//
//		// ボタンを追加
//		// GuiButton(ボタンID, ボタンの始点X, ボタンの始点Y, ボタンの幅, ボタンの高さ, ボタンに表示する文字列)
//		buttonList.add(new GuiButton(DOFORGE, i + (18*5)+2, j + 16 +(18*2), 30, 19 , "Forge!"));
//		buttonList.add(new GuiButton(CATEGORY, i + (18*3)+2, j + 16 +(18*2), 31, 19 , "Category"));
		//for (int k = 0; k < 9; ++k)
		//{
		//	this.addButton(k, i+7+(k*18), j+85-(18*3), 18, 18 , " ");
		//	//this.merchandiseSlot[i] = new SlotMerchandise(this.invMerchant, i+10, 8 + i * 18, 63-(18*3));
		//	//this.addSlotToContainer(new SlotMerchandise(this.invMerchant, i+10, 8 + i * 18, 63-(18*3)));
		//}

	}

	public PriceDownUpPair getDiscount(){
		return this.container.getDiscount();
	}
	@Override
	public void prePacket(GuiButton par1GuiButton){

//		/** 値段上下系パネルの数を調べて返す、おしゃれがあれば加算*/
//		Function panelChecker = new Function<PanelData,Integer>(){
//
//			@Override
//			public Integer apply(PanelData input) {
//				int base = SkillPanels.getHighestLevelPanel(ep.worldObj, ep, input) + 1;
//				if(SkillPanels.hasPanel(ep.worldObj, ep, Unsaga.skillPanels.fashionable)){
//					base += SkillPanels.getHighestLevelPanel(ep.worldObj, ep, Unsaga.skillPanels.fashionable) + 1;
//				}
//				return base;
//			}
//		};
//		switch(par1GuiButton.id){
//		case BUTTON_UP_PRICE:
//			if(SkillPanels.hasPanel(ep.worldObj, ep, Unsaga.skillPanels.gratuity)){
//				this.setPriceUp((Integer) panelChecker.apply(SkillPanels.getInstance().gratuity));
//				Unsaga.debug(this.priceUp,this.getClass());
//			}
//
//
//			break;
//		case BUTTON_DOWN_PRICE:
//			if(SkillPanels.hasPanel(ep.worldObj, ep, Unsaga.skillPanels.discount)){
//				this.setPriceDown((Integer) panelChecker.apply(SkillPanels.getInstance().discount));
//				Unsaga.debug(this.priceDown,this.getClass());
//			}
//
//			break;
//		}
	}


	public void onPacketFromServer(NBTTagCompound message){
		HSLib.logger.trace(this.getGuiName(), "届いてます"+message.getString("test"));
		this.shopLevel = message.getInteger("shopLevel");
		if(message.hasKey("distLV")){
			this.transactionXP = message.getInteger("transXP");
			this.distributionLevel = message.getInteger("distLV");
			int meta = (int)message.getByte("shopType");
			this.nextStockTime = message.getInteger("nextTime");
			this.type = BarteringShopType.fromMeta(meta);
		}
		if(message.hasKey("checkBox")){
			List<ContainerBartering.Entry> list =  UtilNBT.readListFromNBT(message, "checkBox", ContainerBartering.Entry.RESTORE);
			list.stream().forEach(in -> this.checkBox.put(in.num,in.sw));
			this.container.upDateCheckBox(this.checkBox);
		}

//		this.hasSync = true;
	}

	public void prePacket(Icon icon){
		if(icon.id==BUTTON_DOWN_PRICE){
			this.container.setDiscount(BarteringUtil.applyDiscount(ep));
		}
		if(icon.id==BUTTON_UP_PRICE){
			this.container.setDiscount(BarteringUtil.applyGratuity(ep));
		}
	}


	@Override
    public void onGuiClosed()
    {
    	super.onGuiClosed();
    	this.merchant.setCustomer(null);
    }

	public static class IconSkill extends IconSkillAssociated<GuiBartering>{

		public IconSkill(int id, int x, int y, int u, int v, boolean hover, SkillPanel skill) {
			super(id, x, y, u, v, hover, skill);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
		public EntityPlayer getPlayer(GuiBartering gui) {
			// TODO 自動生成されたメソッド・スタブ
			return gui.ep;
		}

	}
}
