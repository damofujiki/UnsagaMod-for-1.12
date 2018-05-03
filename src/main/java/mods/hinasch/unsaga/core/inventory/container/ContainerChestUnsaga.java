package mods.hinasch.unsaga.core.inventory.container;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import com.google.common.collect.Lists;

import joptsimple.internal.Strings;
import mods.hinasch.lib.container.ContainerBase;
import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.network.PacketSendGuiInfoToClient;
import mods.hinasch.lib.util.ChatHandler;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.util.UtilNBT;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.ability.AbilityRegistry;
import mods.hinasch.unsaga.chest.ChestHelper;
import mods.hinasch.unsaga.chest.IChestBehavior;
import mods.hinasch.unsaga.chest.ChestTrap;
import mods.hinasch.unsaga.chest.ChestTraps;
import mods.hinasch.unsaga.common.tool.ItemFactory;
import mods.hinasch.unsaga.core.advancement.UnsagaTriggers;
import mods.hinasch.unsaga.core.client.gui.GuiChest;
import mods.hinasch.unsaga.core.net.packet.PacketGuiButtonUnsaga;
import mods.hinasch.unsaga.core.net.packet.PacketSyncSkillPanel;
import mods.hinasch.unsaga.init.UnsagaGui;
import mods.hinasch.unsaga.skillpanel.SkillPanel;
import mods.hinasch.unsaga.skillpanel.SkillPanelAPI;
import mods.hinasch.unsaga.skillpanel.SkillPanels;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class ContainerChestUnsaga extends ContainerBase{

	public IChestBehavior chest;
	//	public ChestInteractionAgent chestBehaviorAgent;
	protected EntityPlayer openPlayer;
	protected boolean setClose;

	protected AbilityRegistry abilities;
	//protected int id;

	String message = Strings.EMPTY;
	World world;
	protected InventoryBasic dummy;

	ItemFactory itemFactory;

	public ContainerChestUnsaga(IChestBehavior chest, EntityPlayer ep) {
		super(ep, null);
//		this.abilities = UnsagaMod.abilities;
		this.openPlayer = ep;
		this.chest = chest;
		this.chest.getCapability().setOpeningPlayer(openPlayer);
		this.setClose = false;
		this.dummy = new InventoryBasic("chest",false,10);

		//		this.chestBehaviorAgent = new ChestInteractionAgent(chest,chest.getParent());

		this.world = this.openPlayer.getEntityWorld();
		this.spreadSlotItems = false;

		this.itemFactory = new ItemFactory(this.world.rand);
		//		this.attachIcons(dummy, openPlayer, ChestSkill.SKILLS,new XY(28,29));


		//		ep.addStat(UnsagaModCore.instance().achievements.firstChest, 1);
		//		ListHelper.stream(ChestSkill.mapSkills).forEach(new Consumer<ChestSkill>(){
		//
		//			@Override
		//			public void accept(ChestSkill input) {
		//				addSlotToContainer(new SlotIcon(dummy, input.getNumber(), 28+(18*input.getNumber()), 29));
		//				ItemStack is = input.getIcon();
		//				if(!input.panelChecker.apply(openPlayer)){
		//					ComponentSelectableIcon.setNegative(is, true);
		//				}
		//				dummy.setInventorySlotContents(input.getNumber(), is);
		//			}}
		//		);

		if(WorldHelper.isServer(this.ep.getEntityWorld())){
			UnsagaMod.packetDispatcher.sendTo(PacketSyncSkillPanel.create(openPlayer),(EntityPlayerMP) this.openPlayer);
			UnsagaTriggers.OPEN_GUI_CHEST.trigger((EntityPlayerMP) ep);
		}

//		this.ep.addStat(UnsagaAchievementRegistry.instance().firstChest);


	}


	public void activateTraps(){
		this.ep.closeScreen();
		boolean flag = false;
		Queue<ChestTrap> queue = new ArrayBlockingQueue(10);
		this.chest.getCapability().getTraps().forEach(in -> queue.offer(in));
		for(int i=0;i<queue.size();i++){
			ChestTrap trap = queue.poll();
			trap.activate(chest, openPlayer);
			if(trap==ChestTraps.NEEDLE){
				if(this.ep.getEntityWorld().rand.nextInt(2)==0){
					queue.offer(trap);
				}
			}
		}
		this.chest.getCapability().setTraps(Lists.newArrayList(queue));



	}
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		if(this.chest==null){
			return false;
		}
		//		if(this.chest.getStatus(EnumProperty.OPENED)){
		//			return false;
		//		}
		if(this.openPlayer.openContainer==this){
			if(this.setClose){
				return false;
			}
			return this.openPlayer==entityplayer;
		}

		if(this.chest.getCapability().getOpeningPlayer().isPresent()){
			return this.chest.getCapability().getOpeningPlayer().get()==entityplayer;
		}
		return false;

	}

	//
	//	@Deprecated
	//	@Override
	//	public void  onPacketData() {
	//
	//		Predicate skillChecker = new Predicate<PanelData>(){
	//
	//			@Override
	//			public boolean apply(PanelData input) {
	//				if(SkillPanels.hasPanel(ep.worldObj, ep, input)){
	//					return true;
	//				}
	//				ChatHandler.sendChatToPlayer(openPlayer, Translation.localize("msg.has.noability"));
	//				return false;
	//			}
	//		};
	//		openPlayer.closeScreen();
	//		switch(buttonID){
	//		case GuiChest.OPEN:
	//			this.chest.activateChest(this.openPlayer);
	//			break;
	//		case GuiChest.DEFUSE:
	//			if(skillChecker.apply(SkillPanels.getInstance().defuse)){
	//				this.chestBehaviorAgent.tryDefuse(openPlayer);
	//			}
	//			break;
	//		case GuiChest.DIVINATION:
	//			if(skillChecker.apply(SkillPanels.getInstance().divination)){
	//				this.chestBehaviorAgent.divination(openPlayer);
	//			}
	//			break;
	//		case GuiChest.UNLOCK:
	//			if(skillChecker.apply(SkillPanels.getInstance().unlock)){
	//				this.chestBehaviorAgent.tryUnlock(openPlayer);
	//			}
	//			break;
	//		case GuiChest.PENETRATION:
	//			if(skillChecker.apply(SkillPanels.getInstance().penetration)){
	//				this.chestBehaviorAgent.tryPenetration(openPlayer);
	//			}
	//		}
	//
	//	}
//	public boolean playerHasAbility(Ability ab) {
//		// TODO 自動生成されたメソッド・スタブ
//		return AbilityHelper.getAbilityAmounts(openPlayer).get(ab)>0;
//	}

	public void defuse(EnumActionResult result){
		if(this.chest.getCapability().hasDefused()){
			this.message = "gui.unsaga.chest.success.hasDefused";
			this.syncMessage();
			return;
		}
		switch(result){
		case FAIL:
			this.openPlayer.getFoodStats().addExhaustion(0.025F);
			this.message = "gui.unsaga.chest.failed2";
			break;
		case PASS:
			this.message = "gui.unsaga.chest.failed";
			break;
		case SUCCESS:
			this.playSoundFromServer(XYZPos.createFrom(openPlayer), SoundEvents.BLOCK_PISTON_CONTRACT, ep);
			this.message = "gui.unsaga.chest.success.defuse";
			this.chest.getCapability().setTraps(Lists.newArrayList());
			break;
		default:
			break;

		}
		this.openPlayer.getFoodStats().addExhaustion(0.025F);
		this.syncMessage();
		this.syncChest();
	}

	public void divine(EnumActionResult result,Random rand){
		int up = 0;
		switch(result){
		case FAIL:
			up = -100;
			ChatHandler.sendChatToPlayer(ep, HSLibs.translateKey("gui.unsaga.chest.failed2"));
			this.activateTraps();
			break;
		case PASS:
			up = -rand.nextInt(10)+1;
			this.message = "gui.unsaga.chest.failed";
			break;
		case SUCCESS:
			this.playSoundFromServer(XYZPos.createFrom(openPlayer), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, ep);
			this.message = this.chest.getCapability().getTreasureType().getMessage();
			up = rand.nextInt(10)+1;

			break;
		default:
			break;

		}
		int level = chest.getCapability().getLevel() + up;
		level = MathHelper.clamp(level, 1, 100);
		chest.getCapability().setLevel(level);
		this.openPlayer.getFoodStats().addExhaustion(0.025F);
		this.syncMessage();
		this.syncChest();
	}

	@Override
	public int getGuiID() {
		// TODO 自動生成されたメソッド・スタブ
		return UnsagaGui.Type.CHEST.getMeta();
	}

	@Override
	public PacketGuiButtonUnsaga getPacketGuiButton(int guiID, int buttonID,
			NBTTagCompound args) {
		// TODO 自動生成されたメソッド・スタブ
		return PacketGuiButtonUnsaga.create(UnsagaGui.Type.fromMeta(guiID),buttonID,args);
	}
	@Override
	public SimpleNetworkWrapper getPacketPipeline() {
		// TODO 自動生成されたメソッド・スタブ
		return UnsagaMod.packetDispatcher;
	}

	@Override
	public PacketSendGuiInfoToClient getSyncPacketToClient(EntityPlayer ep){
		NBTTagCompound tag = UtilNBT.compound();
		tag.setString("message", this.message);
		return PacketSendGuiInfoToClient.create(tag);
	}

	//	private void onClickMapSkill(int num) {
	//		if(WorldHelper.isClient(this.openPlayer.worldObj)){
	//			return;
	//		}
	//		openPlayer.closeScreen();
	//		ChestSkill skill = ChestSkill.SKILLS.get(num);
	//		skill.onClicked(openPlayer,this);
	//	}


	//	public IUnsagaChest getOpeningChestCore(){
	//		return this.chest;
	//	}
	//
	//	public ChestInteractionAgent getOpeningChestHelper(){
	//		return this.chestBehaviorAgent;
	//	}

	@Override
	public boolean isShowPlayerInv(){
		return false;
	}


	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{

		super.onContainerClosed(par1EntityPlayer);

		if(WorldHelper.isServer(this.ep.getEntityWorld())){
			//			if(chest.getParent().first!=null){
			//				((EntityUnsagaChest)chest.getParent().first).sync();
			//			}
			//			if(chest.getParent().second!=null){
			//				((TileEntityUnsagaChest)chest.getParent().second).sync();
			//			}
		}
		this.chest.getCapability().setOpeningPlayer(null);


	}
	@Override
	public void onPacketData() {
		int id = this.buttonID;
		Random rand = this.ep.getRNG();
		switch(id){
		case GuiChest.OPEN:
			this.tryOpening();
			break;
		case GuiChest.UNLOCK:
			if(SkillPanelAPI.hasPanel(ep, SkillPanels.LOCKSMITH)){
				SkillPanel skill = SkillPanels.LOCKSMITH;
				EnumActionResult result = ChestHelper.tryInteraction(openPlayer, skill, rand, chest.getCapability(), SkillPanelAPI.getHighestPanelLevel(openPlayer, skill).getAsInt());
				this.unlock(result);
			}
			break;
		case GuiChest.DIVINATION:
			if(SkillPanelAPI.hasPanel(ep, SkillPanels.FORTUNE)){
				SkillPanel skill = SkillPanels.FORTUNE;
				EnumActionResult result = ChestHelper.tryInteraction(openPlayer, skill, rand, chest.getCapability(), SkillPanelAPI.getHighestPanelLevel(openPlayer, skill).getAsInt());
				this.divine(result,rand);
			}
			break;
		case GuiChest.PENETRATION:
			if(SkillPanelAPI.hasPanel(ep, SkillPanels.SHARP_EYE)){
				SkillPanel skill = SkillPanels.SHARP_EYE;
				EnumActionResult result = ChestHelper.tryInteraction(openPlayer, skill, rand, chest.getCapability(), SkillPanelAPI.getHighestPanelLevel(openPlayer, skill).getAsInt());
				this.penetrate(result);
			}
			break;
		case GuiChest.DEFUSE:
			if(SkillPanelAPI.hasPanel(ep, SkillPanels.DEFUSE)){
				SkillPanel skill = SkillPanels.DEFUSE;
				EnumActionResult result = ChestHelper.tryInteraction(openPlayer, skill, rand, chest.getCapability(), SkillPanelAPI.getHighestPanelLevel(openPlayer, skill).getAsInt());
				this.defuse(result);
			}
			break;
		}


	}
//
//	@Override
//	public void onSlotClick(int rawSlotNumber,int containerSlotNumber,int clickButton,ClickType par3,EntityPlayer ep){
//		ItemStack is = this.getSlot(rawSlotNumber).getStack();
//		if(is!=null && is.getItem() instanceof ItemIconMapSkill){
//			this.playClickSound();
//			int num =  is.getItemDamage();
//			if(!ComponentSelectableIcon.isNegative(is)){
//				//				this.onClickMapSkill(num);
//			}
//
//		}
//
//	}

	public void openChest(){
		if(this.chest.getCapability().hasLocked()){
			ChatHandler.sendChatToPlayer(openPlayer, HSLibs.translateKey("gui.unsaga.chest.locked"));

		}else{
			if(this.chest.getCapability().hasMagicLocked()){
				ChatHandler.sendChatToPlayer(openPlayer,HSLibs.translateKey("gui.unsaga.chest.magicLocked"));

			}else{
				this.chest.getCapability().setOpened(true);
				ItemStack treasure = this.chest.getCapability().getTreasureType().createTreasure(this.world.rand, this.chest.getCapability().getLevel(), itemFactory);
				ItemUtil.dropItem(world, treasure, this.chest.getChestPosition());
				ChatHandler.sendChatToPlayer(openPlayer, HSLibs.translateKey("gui.unsaga.chest.opening"));
				ep.closeScreen();

			}
		}
	}

	public void penetrate(EnumActionResult result){
		if(this.chest.getCapability().hasAnalyzed()){
			this.message = "gui.unsaga.chest.success.hasAnalyzed";
			this.syncMessage();
			return;
		}
		switch(result){
		case FAIL:
			ChatHandler.sendChatToPlayer(ep, HSLibs.translateKey("gui.unsaga.chest.failed2"));
			this.activateTraps();
			break;
		case PASS:
			this.message = "gui.unsaga.chest.failed";
			break;
		case SUCCESS:
			this.playSoundFromServer(XYZPos.createFrom(openPlayer), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, ep);
			this.message = "gui.unsaga.chest.success.penetrate";
			this.chest.getCapability().setAnalyzed(true);
			break;
		default:
			break;

		}
		this.openPlayer.getFoodStats().addExhaustion(0.025F);
		this.syncMessage();
		this.syncChest();
	}

	public void syncChest(){
		this.chest.sync(openPlayer);

	}

	public void syncMessage(){
		if(!this.message.isEmpty()){
			if(WorldHelper.isServer(this.openPlayer.getEntityWorld())){
				HSLib.getPacketDispatcher().sendTo(this.getSyncPacketToClient(openPlayer), (EntityPlayerMP) this.openPlayer);
			}
		}

	}
	public void tryOpening(){
		this.activateTraps();
		this.openChest();

	}
	public void unlock(EnumActionResult result){
		if(!this.chest.getCapability().hasLocked()){
			this.message = "gui.unsaga.chest.success.hasUnlocked";
			this.syncMessage();
			return;
		}
		switch(result){
		case FAIL:
			ChatHandler.sendChatToPlayer(ep, HSLibs.translateKey("gui.unsaga.chest.failed2"));
			this.activateTraps();
			break;
		case PASS:
			this.message = "gui.unsaga.chest.failed";
			break;
		case SUCCESS:

			this.playSoundFromServer(XYZPos.createFrom(openPlayer), SoundEvents.BLOCK_PISTON_CONTRACT, ep);
			this.message = "gui.unsaga.chest.success.unlock";
			this.chest.getCapability().setLocked(false);
			break;
		default:
			break;

		}
		this.openPlayer.getFoodStats().addExhaustion(0.025F);
		this.syncMessage();
		this.syncChest();
	}


}
