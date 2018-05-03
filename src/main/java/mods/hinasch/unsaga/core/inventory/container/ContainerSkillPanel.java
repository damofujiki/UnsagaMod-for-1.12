package mods.hinasch.unsaga.core.inventory.container;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import mods.hinasch.lib.container.ContainerBase;
import mods.hinasch.lib.container.inventory.InventoryHandler;
import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.network.PacketGuiButtonBaseNew;
import mods.hinasch.lib.network.PacketSyncCapability;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.client.gui.GuiSkillPanel;
import mods.hinasch.unsaga.core.inventory.InventorySkillPanel;
import mods.hinasch.unsaga.core.inventory.slot.SlotSkillPanel;
import mods.hinasch.unsaga.core.item.misc.skillpanel.SkillPanelCapability;
import mods.hinasch.unsaga.core.net.packet.PacketGuiButtonUnsaga;
import mods.hinasch.unsaga.core.world.UnsagaWorldCapability;
import mods.hinasch.unsaga.init.UnsagaGui;
import mods.hinasch.unsaga.skillpanel.ISkillPanelSaver;
import mods.hinasch.unsaga.skillpanel.SkillPanel;
import mods.hinasch.unsaga.skillpanel.SkillPanelBonus;
import mods.hinasch.unsaga.skillpanel.SkillPanelRegistry;
import mods.hinasch.unsaga.skillpanel.SkillPanelRegistry.WeightedRandomPanel;
import mods.hinasch.unsaga.status.UnsagaXPCapability;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class ContainerSkillPanel extends ContainerBase{

	private SkillPanelRegistry skillPanels = SkillPanelRegistry.instance();
	protected InventorySkillPanel invSkillPanel;
	protected InventoryBasic invTemp;
	protected World world;
	protected ItemStack undoPanel;
	protected int slotUndo;
	protected ItemStack undoPanelPlayer;
	protected boolean hasDrawedSkillPanel;
	protected boolean hasSetSkillPanel;
	protected int expToConsume;

	protected static final Map<Integer,Integer[]> modifier_level;
	final MatrixAdapterItemStack matrixPanel;
	final MatrixAdapterElement matrixSpell;


	public HexMatrixAdapter<ItemStack> getPanelMatrix() {
		return matrixPanel;
	}

	public ContainerSkillPanel(EntityPlayer ep) {
		super(ep, new InventorySkillPanel());
		this.world = ep.world;
		this.invSkillPanel = (InventorySkillPanel) this.inv;
		this.invTemp = new InventoryBasic("",false,6);
		this.spreadSlotItems = false;
		this.expToConsume = 5;
		this.hasSetSkillPanel = false;
		this.hasDrawedSkillPanel = false;

		this.matrixPanel = new MatrixAdapterItemStack(inv);
		this.matrixSpell = new MatrixAdapterElement();




//		WorldSaveDataSkillPanel data = WorldSaveDataSkillPanel.get(world);


		NonNullList<ItemStack> panelList = UnsagaWorldCapability.ADAPTER.getCapability(world).getPanels(ep.getUniqueID());
		this.invSkillPanel.applyItemStackList(panelList);

		int index = 0;
		for(int j=0;j<2 ;j++){
			addSlotToContainer(new SlotSkillPanel(this.inv,j , 62 + 9 + j * 18, 17 + index * 18).setPlayerPanel(true));
		}
		index +=1;
		for(int j=0;j<3 ;j++){
			addSlotToContainer(new SlotSkillPanel(this.inv,j + 2, 62 + j * 18, 17 + index * 18).setPlayerPanel(true));
		}
		index +=1;
		for(int j=0;j<2 ;j++){
			addSlotToContainer(new SlotSkillPanel(this.inv,j + 5, 62 + 9 +  j * 18, 17 + index * 18).setPlayerPanel(true));
		}

		for(int col=0;col<3;col++){
			for(int j=0;j<2 ;j++){
				addSlotToContainer(new SlotSkillPanel(this.invTemp, j + (col*2), 53 + 18*4 +  j * 18, 17 + col * 18).setPlayerPanel(false));
			}
		}





	}

	@Override
	public boolean isShowPlayerInv(){
		return this.ep.capabilities.isCreativeMode;
	}
	@Override
	public PacketGuiButtonBaseNew getPacketGuiButton(int guiID, int buttonID,
			NBTTagCompound args) {
		// TODO 自動生成されたメソッド・スタブ
		return PacketGuiButtonUnsaga.create(UnsagaGui.Type.SKILLPANEL, buttonID, args);
	}

	@Override
	public SimpleNetworkWrapper getPacketPipeline() {
		// TODO 自動生成されたメソッド・スタブ
		return UnsagaMod.packetDispatcher;
	}

	@Override
	public int getGuiID() {
		// TODO 自動生成されたメソッド・スタブ
		return UnsagaGui.Type.SKILLPANEL.getMeta();
	}

	@Override
	public void onPacketData() {
		UnsagaMod.logger.trace(this.getClass().getName(),"called");
		switch(this.buttonID){
		case GuiSkillPanel.BUTTON_CHANGE_EXP:
			this.expToConsume = this.argsSent.getInteger("exp");
			break;
		case GuiSkillPanel.BUTTON_DRAW_PANELS:
			if(!this.hasDrawedSkillPanel){
				if(UnsagaXPCapability.hasCapability(ep)){
					int skillPointLevel = UnsagaXPCapability.getCapability(ep).getSkillPoint();
					if(skillPointLevel>=this.expToConsume || this.ep.capabilities.isCreativeMode){
						this.drawSkills();
						this.hasDrawedSkillPanel = true;
					}
				}

			}

			break;
		case GuiSkillPanel.BUTTON_UNDO:
			if(this.hasSetSkillPanel){
				this.undo();
				this.hasSetSkillPanel = false;
			}
			break;

		}

	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);

		this.applyBonus(playerIn);
		for(int i=0;i<7;i++){
			ItemStack panel = this.invSkillPanel.getStackInSlot(i);
			if(!ItemUtil.isItemStackNull(panel)){
				if(SkillPanelCapability.adapter.hasCapability(panel)){
					SkillPanelCapability.adapter.getCapability(panel).setLocked(true);
				}
			}

		}
		playerIn.inventory.setItemStack(ItemStack.EMPTY);
		this.savePanelData(playerIn);
	}

	public void applyBonus(EntityPlayer playerIn){
		boolean lineBonus = !this.getPanelMatrix().checkLine().isEmpty();
		SkillPanelBonus.applyBonus(getPanelMatrix(), playerIn, lineBonus);
	}
	public void savePanelData(EntityPlayer ep){
		UnsagaMod.logger.trace(this.getClass().getName(), "パネルをsaveします");

		ISkillPanelSaver data = UnsagaWorldCapability.ADAPTER.getCapability(world);
		data.dumpData(); //debug
		data.setPanels(ep.getUniqueID(), this.invSkillPanel.getPlayerPanels());
//		this.world.setItemData(WorldSaveDataSkillPanel.KEY, data);
//		data.markDirty();
	}


	public void setUndoInfo(int undoSlot,ItemStack undoStackPlayer,ItemStack undoStackNew){
		this.slotUndo = undoSlot;
		this.undoPanelPlayer = !undoStackPlayer.isEmpty() ?undoStackPlayer.copy() : ItemStack.EMPTY;
		this.undoPanel = !undoStackNew.isEmpty() ?undoStackNew.copy() : ItemStack.EMPTY;
		this.hasSetSkillPanel = true;
	}
	public void undo(){



		this.getSlot(slotUndo).putStack(!undoPanelPlayer.isEmpty()? undoPanelPlayer.copy() : ItemStack.EMPTY);
		if(this.getEmptySlot().isPresent()){
			this.invTemp.setInventorySlotContents(this.getEmptySlot().getAsInt(), !undoPanel.isEmpty()? undoPanel.copy() : ItemStack.EMPTY);
		}

	}

	public void drawSkills(){
		//Collection<SkillPanels.WeightedRandomPanel> weightedPanels = Unsaga.skillPanels.getWeightedRandomPanels();
		int num = this.expToConsume/5;
		final Integer[] modifier = this.modifier_level.get(num-1);


		List<WeightedRandomPanel> availablePanels = skillPanels.makeWeightedRandomPanels().stream()
				.filter(input -> !hasPanel(input.panel,input.level) && modifier[input.level]>0)
				.map(input -> {
					int modifieredWeight = input.itemWeight + modifier[input.level];
					return new WeightedRandomPanel(modifieredWeight,input.panel,input.level);
				}).collect(Collectors.toList());


		InventoryHandler.create(this.invTemp).toStream(0,6).forEach(status ->{
			WeightedRandomPanel drawedPanel = WeightedRandom.getRandomItem(ep.getRNG(), availablePanels);
			ItemStack stackPanel = SkillPanelRegistry.instance().getItemStack(drawedPanel.panel, drawedPanel.level);
//			if(isPanel==null)skillPanels.getData(0).getItemStack(); //?
//			ItemSkillPanel.setLevel(isPanel, drawedLevel);
			status.getParent().setInventorySlotContents(status.getStackNumber(), stackPanel.copy());
			availablePanels.remove(drawedPanel);
		});


		this.consumeExperience();
	}

	public void consumeExperience(){
		if(!this.ep.capabilities.isCreativeMode){
			if(UnsagaXPCapability.hasCapability(ep)){
				int skillLevel = UnsagaXPCapability.getCapability(ep).getSkillPoint();
				if(skillLevel>=this.expToConsume){
					UnsagaXPCapability.getCapability(ep).addSkillPoint(-this.expToConsume);
					if(WorldHelper.isServer(world)){
						HSLib.getPacketDispatcher().sendTo(PacketSyncCapability.create(UnsagaXPCapability.CAPA,UnsagaXPCapability.getCapability(ep)), (EntityPlayerMP) ep);
					}

				}
			}


		}
	}
	public boolean hasPanel(final SkillPanel panelIn,final int levelIn){
		return InventoryHandler.create(inv).toStream(0,7).anyMatch(input -> {
			if(!input.getStack().isEmpty()){
				SkillPanel panel =SkillPanelCapability.adapter.getCapability(input.getStack()).getPanel();
				int lv = SkillPanelCapability.adapter.getCapability(input.getStack()).getLevel();
				if(panelIn==panel && lv==levelIn){
					return true;
				}
				
				
			}
			return false;
		});

//		for(int i=0;i<7;i++){
//			if(this.inv.getStackInSlot(i)!=null){
//				int damage = this.inv.getStackInSlot(i).getItemDamage();
//				int lv = ItemSkillPanel.getLevel(this.inv.getStackInSlot(i));
//				if(data.getNumber()==damage && lv==level){
//					return true;
//				}
//			}
//
//
//		}
//		return false;
	}

	public OptionalInt getEmptySlot(){
		for(int i=0;i<6;i++){
			if(ItemUtil.isItemStackNull(this.invTemp.getStackInSlot(i))){
				return OptionalInt.of(i);
			}
		}
		return OptionalInt.empty();
	}
	@Override
    public ItemStack slotClick(int par1, int dragType, ClickType clickTypeIn, EntityPlayer player)
    {
		if(par1>0){
			Slot slot = this.getSlot(par1);

//			if(slot instanceof SlotSkillPanel){
//				if(!((SlotSkillPanel) slot).isPlayerPanel() && this.getEmptySlot().isPresent()){
//					return null;
//				}
//
//			}

			if(slot instanceof SlotSkillPanel){
				if(!((SlotSkillPanel)slot).isPlayerPanel()){
					if(this.getEmptySlot().isPresent()){
						return ItemStack.EMPTY;
					}
				}
			}

			UnsagaMod.logger.trace("slot", par1);
			ItemStack is = slot.getStack();
			ItemStack catching = player.inventory.getItemStack();
//			this.applyBonus(this.ep);
			if(ItemUtil.isItemStackPresent(catching)){
				this.setUndoInfo(par1, is, catching);
				slot.putStack(catching);
				player.inventory.setItemStack(ItemStack.EMPTY);
				return ItemStack.EMPTY;
			}
			if(slot instanceof SlotSkillPanel){
				if(((SlotSkillPanel) slot).isPlayerPanel()){
					return ItemStack.EMPTY;
				}

			}



		}

		return super.slotClick(par1, dragType, clickTypeIn, player);

	}


	static{
		modifier_level = new HashMap();
		modifier_level.put(0, new Integer[]{40,15,1,0,0});
		modifier_level.put(1, new Integer[]{7,40,10,0,0});
		modifier_level.put(2, new Integer[]{1,7,40,10,0});
		modifier_level.put(3, new Integer[]{0,3,20,40,1});
		modifier_level.put(4, new Integer[]{0,0,5,40,5});
		modifier_level.put(5, new Integer[]{0,0,0,10,30});
	}


}
