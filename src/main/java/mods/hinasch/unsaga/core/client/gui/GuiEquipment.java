package mods.hinasch.unsaga.core.client.gui;

import java.util.EnumSet;
import java.util.List;

import com.google.common.collect.Lists;

import joptsimple.internal.Strings;
import mods.hinasch.lib.client.GuiContainerBase;
import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.misc.XY;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.world.EnvironmentalManager;
import mods.hinasch.lib.world.EnvironmentalManager.EnvironmentalCondition;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.common.IconSkillAssociated;
import mods.hinasch.unsaga.core.advancement.UnsagaUnlockableContentCapability;
import mods.hinasch.unsaga.core.event.old.HealTimerCalculator;
import mods.hinasch.unsaga.core.inventory.container.ContainerEquipment;
import mods.hinasch.unsaga.skillpanel.SkillPanel;
import mods.hinasch.unsaga.skillpanel.SkillPanels;
import mods.hinasch.unsaga.status.UnsagaXPCapability;
import mods.hinasch.unsaga.util.UnsagaTextFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GuiEquipment extends GuiContainerBase{

	protected EntityPlayer player;
	protected World world;
	boolean hasSync = false;
//	boolean hasUnlockedDechiphering = false;

	public static final int BUTTON_OPEN_SKILLS = 1;
	public static final int BUTTON_OPEN_BLEND = 2;
	public static final int BUTTON_USE_MAPSKILL = 3;
	public static final int DECIPHERING_POINT = 10;
	public static final int SKILL_POINT = 11;
	public static final int ROAD_ADVISER = 20;
	public static final int CAVERN_EXPLORER = 21;
	public static final int EASY_REPAIR = 22;
	public static final int WEAPON_FORGE = 23;
	public static final int EAVESDROP = 24;
	public static final int SPELL_BLEND = 25;
	public static final int JUMP = 26;
	public static final int SPEED = 27;

	public GuiEquipment(EntityPlayer player) {
		super(new ContainerEquipment(player.inventory,player));
		this.player = player;
		this.world = player.getEntityWorld();
		// TODO 自動生成されたコンストラクター・スタブ
	}


	@Override
	public void initGui()
	{
		super.initGui();
		int i = width  - xSize >> 1;
		int j = height - ySize >> 1;



		// ボタンを追加
		// GuiButton(ボタンID, ボタンの始点X, ボタンの始点Y, ボタンの幅, ボタンの高さ, ボタンに表示する文字列)
		//buttonList.add(new GuiButton(-2, i + (18*4)+2, j + 16 +(18*2), 30, 20 , "Forging"));
		//this.addButton(BUTTON_OPEN_BLEND,  i + (18*4)+2, j + 16 +(18*0), 30, 20 ,"Blend");
		this.addButton(BUTTON_OPEN_SKILLS,  i + (18*5)+20, j + 8 +(18*0), 50, 20 ,"Skill Panels");

//		SkillPanelRegistry reg = SkillPanelRegistry.instance();
		this.addIcon(new IconCondition(0,8,49,0,168,true));
		this.addIcon(new IconMapSkill(ROAD_ADVISER,8,65,0,200,true,SkillPanels.GUIDE_ROAD));
		this.addIcon(new IconMapSkill(CAVERN_EXPLORER,8+16*1,65,16*1,200,true,SkillPanels.GUIDE_CAVE));
		this.addIcon(new IconMapSkill(SPELL_BLEND,8+16*2,65,16*2,200,true,SkillPanels.MAGIC_BLEND));
		this.addIcon(new IconMapSkill(EASY_REPAIR,8+16*3,65,16*3,200,true,SkillPanels.QUICK_FIX));
		this.addIcon(new IconMapSkill(WEAPON_FORGE,8+16*4,65,16*4,200,true,SkillPanels.TOOL_CUSTOMIZE));
		this.addIcon(new IconMapSkill(EAVESDROP,8+16*5,65,16*5,200,true,SkillPanels.EAVESDROP));
		this.addIcon(new IconMapSkill(SPEED,8+16*6,65,16*6,200,true,SkillPanels.SMART_MOVE));
		this.addIcon(new IconMapSkill(JUMP,8+16*7,65,16*7,200,true,SkillPanels.OBSTACLE_CROSSING));
		this.addIcon(DECIPHERING_POINT, 140, 38, 0, 184, true);
		this.addIcon(SKILL_POINT, 140, 58, 16, 184, true);
	}

	public String getGuiTextureName(){
		return UnsagaMod.MODID+":textures/gui/container/equipment.png";
	}
	public String getGuiName(){
		return "";
	}

	public static boolean isItemStackRepairable(ItemStack is,EntityPlayer ep){
		if(ItemUtil.isItemStackPresent(is) && is.isItemStackDamageable()){
			return ep.experienceLevel >= 1;
		}
		return false;
	}
	@Override
	protected void drawGuiContainerForegroundLayer(int par1,int par2)
	{
		super.drawGuiContainerForegroundLayer(par1, par2);
		XYZPos pos = XYZPos.createFrom(this.player);

		//		if(SkillPanels.hasPanel(world, this.player, UnsagaMod.skillPanels.roadAdviser)){
		//			fontRendererObj.drawString("Spawn Point:"+this.player.getBedLocation(world.provider.getDimension()),8,165,0xffffff);
		//		}

		//		fontRendererObj.drawString(HSLibs.translateKey("condition.environment")+":"+this.getBiomeEnv(world,pos),8,72,0x404040);


	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);
		UnsagaXPCapability.displayAdditionalXP(player, fontRenderer,
				EnumSet.of(UnsagaXPCapability.Type.DECIPHER,UnsagaXPCapability.Type.SKILL),
				this.getWindowStartX() + 160,this.getWindowStartY() + 46,0);
		//    	if(HSLib.configHandler.isDebug()){
		//    		this.drawHoveringText(Lists.newArrayList(String.valueOf(mouseX - this.getWindowsStartX()),String.valueOf(mouseY - this.getWindowsStartY())), mouseX, mouseY);
		//    	}


	}
	protected String getBiomeEnv(World world,XYZPos pos){

		EnvironmentalCondition status = EnvironmentalManager.getCondition(world, pos, world.getBiome(pos),player);
		String debug = Strings.EMPTY;
		if(HSLib.configHandler.isDebug()){
			debug = "%s/HealTime:%d/Temp:%.2f/Humid:%.2f";
			debug = String.format(debug, status.getType().getName(),HealTimerCalculator.calcHealTimer(player),status.getTemp(),status.getHumid());
			//			debug = "/HealTimer:"+HealTimerCalculator.calcHealTimer(player)+"/Temp:"+status.getTemp()+"/Humid:"+status.getHumid();
		}
		return HSLibs.translateKey(status.getType().getName())+debug;

	}

	@Override
	public List<String> getIconHoverText(GuiContainerBase.Icon icon){
		List<String> list = Lists.newArrayList();
		if(icon instanceof IconCondition){
			EnvironmentalCondition status = this.getCondition();
			list.add(HSLibs.translateKey("gui.unsaga.status."+status.getType().getName()));
			list.add("Heal Interval:"+HealTimerCalculator.calcHealTimer(player));
			if(HSLib.configHandler.isDebug()){
				list.add("Temp:"+status.getTemp());
				list.add("Humid:"+status.getHumid());

			}
		}
		if(icon instanceof IconMapSkill){
			list.add(((IconMapSkill) icon).getAssociatedSkill().getLocalized());
			if(icon.id==EASY_REPAIR){
				list.add(UnsagaTextFormatting.SIGNIFICANT+HSLibs.translateKey("gui.unsaga.status.quickFix.info1", 10));
				list.add(UnsagaTextFormatting.SIGNIFICANT+HSLibs.translateKey("gui.unsaga.status.quickFix.info2", 1));
			}
			if(icon.id==EAVESDROP){
				list.add(UnsagaTextFormatting.SIGNIFICANT+HSLibs.translateKey("gui.unsaga.status.eavesdrop.info"));
			}
		}
		if(icon.id==DECIPHERING_POINT){
//			if(this.hasSync){
//				list.add(HSLibs.translateKey("gui.unsaga.status.decipheringPoint"));
//				if(!UnsagaUnlockableCapability.adapter.getCapability(player).hasUnlockedDeciphering()){
//					list.add(UnsagaTextFormatting.SIGNIFICANT+"Get Magic Tablet To Unlock This");
//				}
//			}else{
//				this.hasSync = true;
//				HSLib.core().getPacketDispatcher().sendToServer(PacketSendGuiInfoToClient.request());
//			}
			list.add(HSLibs.translateKey("gui.unsaga.status.decipheringPoint"));
			if(!UnsagaUnlockableContentCapability.adapter.getCapability(player).hasUnlockedDeciphering()){
				list.add(UnsagaTextFormatting.SIGNIFICANT+"Get Magic Tablet To Unlock This");
			}


		}
		if(icon.id==SKILL_POINT){
			list.add(HSLibs.translateKey("gui.unsaga.status.skillPoint"));
		}
		return list;
	}

//	@Override
//	public void onPacketFromServer(NBTTagCompound message){
//		this.hasUnlockedDechiphering = message.getBoolean("isUnlockDecipher");
////		super.onPacketFromServer(message);
//	}
	public EnvironmentalCondition getCondition(){
		XYZPos pos = XYZPos.createFrom(player);
		EnvironmentalCondition status = EnvironmentalManager.getCondition(world, pos, world.getBiome(pos),player);
		return status;
	}

	@Override
	public void prePacket(Icon icon){
		if(icon.id==EASY_REPAIR){
			if(GuiEquipment.isItemStackRepairable(this.player.getHeldItemMainhand(), this.player)){
				this.player.experienceLevel -= 1;
			}
		}
	}
	public static class IconCondition extends GuiContainerBase.Icon{

		public IconCondition(int id,int x, int y, int u, int v, boolean hover) {
			super(id,x, y, u, v, hover);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
		public XY getUV(GuiContainerBase gui){
			if(gui instanceof GuiEquipment){
				GuiEquipment eqGui = (GuiEquipment) gui;
				EnvironmentalCondition status = eqGui.getCondition();
				//				UnsagaMod.logger.trace("tes", status.getType());
				return new XY(u+16*status.getType().getIconNumber(),v);
			}
			return new XY(0,168);
		}
	}

	public static class IconMapSkill extends IconSkillAssociated<GuiEquipment>{

		public IconMapSkill(int id, int x, int y, int u, int v, boolean hover, SkillPanel skill) {
			super(id, x, y, u, v, hover, skill);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
		public EntityPlayer getPlayer(GuiEquipment gui) {
			// TODO 自動生成されたメソッド・スタブ
			return gui.player;
		}

	}


}
