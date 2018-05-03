package mods.hinasch.unsaga.core.client.gui;

import java.util.List;

import com.google.common.collect.Lists;

import mods.hinasch.lib.client.GuiContainerBase;
import mods.hinasch.lib.misc.XY;
import mods.hinasch.lib.util.UtilNBT;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.inventory.container.ContainerSkillPanel;
import mods.hinasch.unsaga.core.inventory.container.HexMatrixAdapter;
import mods.hinasch.unsaga.core.item.misc.skillpanel.SkillPanelCapability;
import mods.hinasch.unsaga.skillpanel.SkillPanelBonus;
import mods.hinasch.unsaga.skillpanel.SkillPanelRegistry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GuiSkillPanel extends GuiContainerBase{

	public static final int BUTTON_DRAW_PANELS = 1;
	public static final int BUTTON_CHANGE_EXP = 2;
	public static final int BUTTON_UNDO = 3;
	protected GuiButton changeButton;
	protected int expToConsume;
	protected EntityPlayer ep;
	SkillPanelRegistry skillPanels = SkillPanelRegistry.instance();
	public GuiSkillPanel(EntityPlayer ep) {
		super(new ContainerSkillPanel(ep));
		this.expToConsume = 5;
		this.ep = ep;
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void initGui(){
		super.initGui();
		int i = width  - xSize >> 1;
		int j = height - ySize >> 1;
		changeButton = this.addButton(BUTTON_CHANGE_EXP,  this.getWindowStartX() + 10,this.getWindowStartY() + 20, 30, 20 ,String.valueOf(getExpToConsume()));
//		this.addButton(BUTTON_DRAW_PANELS,  i + (18*7)+2, j + 30 +(18*0), 30, 20 ,"draw");
//		this.addButton(BUTTON_UNDO,  i + (18*1)+2, j + 30 +(18*0), 30, 20 ,"undo");

		this.addIcon(new IconButton(BUTTON_DRAW_PANELS, 8, 64, 16, 168, true));
		this.addIcon(new IconButton(BUTTON_UNDO, 28, 64, 0, 168, true));
	}
	@Override
	public String getGuiTextureName(){
		return UnsagaMod.MODID+":textures/gui/container/skillpanel.png";
	}

	public List<String> getIconHoverText(Icon icon){
		List<String> list = Lists.newArrayList();
		if(icon.id==BUTTON_DRAW_PANELS){
			list.add("Consume XP to Draw Panels");
		}
		if(icon.id==BUTTON_UNDO){
			list.add("Undo");
		}
		return list;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1,int par2)
	{
		super.drawGuiContainerForegroundLayer(par1, par2);
		ContainerSkillPanel containerSkill = (ContainerSkillPanel) this.container;
		HexMatrixAdapter<ItemStack> matrix = containerSkill.getPanelMatrix();
		for(XY xy:matrix.getJointed()){
			ItemStack is = matrix.getMatrix(xy);
			if(SkillPanelCapability.adapter.hasCapability(is)){
				SkillPanelCapability.adapter.getCapability(is).setJointed(true);
			}
		}

		boolean isLineBonus = false;
		if(!matrix.checkLine().isEmpty()){
//			UnsagaMod.logger.trace("matrix", "line");
			isLineBonus = true;
		}
		float str = SkillPanelBonus.calcAppliedBonus(matrix,SkillPanelBonus.Type.STR,isLineBonus);
		float luck = SkillPanelBonus.calcAppliedBonus(matrix,SkillPanelBonus.Type.LUCK,isLineBonus);
		float health = SkillPanelBonus.calcAppliedBonus(matrix,SkillPanelBonus.Type.HEALTH,isLineBonus);
		float lpstr = SkillPanelBonus.calcAppliedBonus(matrix,SkillPanelBonus.Type.LPSTR,isLineBonus);
		float magic = SkillPanelBonus.calcAppliedBonus(matrix,SkillPanelBonus.Type.MAGIC,isLineBonus);
		float elements = SkillPanelBonus.calcAppliedBonus(matrix,SkillPanelBonus.Type.ELEMENTS,isLineBonus);


//		for(XY xy:matrix.getAll()){
//			ItemStack panel = matrix.getMatrix(xy);
//			if(panel!=null){
//				for(ItemStack aroundStack:matrix.getAroundElements(xy.getX(), xy.getY())){
//					if(aroundStack!=null){
//						if(skillPanels.getData(panel.getItemDamage())==skillPanels.getData(aroundStack.getItemDamage())){
//							if(ItemSkillPanel.adapter.hasCapability(panel) && !ItemSkillPanel.adapter.getCapability(panel).isJointed()){
//								ItemSkillPanel.adapter.getCapability(panel).setJointed(true);
//							}
//						}
//					}
//
//				}
//			}
//
//		}
//		UnsagaMod.logger.trace("bonus", str,luck,health,lpstr,magic);
//		UnsagaXPCapability.displayAdditionalXP(ep, fontRendererObj, EnumSet.of(Type.SKILL));
		String bonusMes = "STR:%.2f HP:%.2f LUCK:%.2f LP.STR:%.2f INT:%.2f ELM:%.2f";
		String formatted = String.format(bonusMes, str,health,luck,lpstr,magic,elements);
		fontRenderer.drawString("Panel Bonus", -60,0,0xffffff);
		fontRenderer.drawString(String.format("STR:%.2f", str), -60,16,0xffffff);
		fontRenderer.drawString(String.format("HP:%.2f", health), -60,16*2,0xffffff);
		fontRenderer.drawString(String.format("LUCK:%.2f", luck), -60,16*3,0xffffff);
		fontRenderer.drawString(String.format("DEX:%.2f", lpstr), -60,16*4,0xffffff);
		fontRenderer.drawString(String.format("INT:%.2f", magic), -60,16*5,0xffffff);
		fontRenderer.drawString(String.format("ELM:%.2f", elements), -60,16*6,0xffffff);
	}
	public int getExpToConsume(){
		return this.expToConsume;
	}
	@Override
	public String getGuiName(){
		return "Skill Panel";
	}

	public void setExpToConsume(int var1){
		this.expToConsume = var1;
	}
	@Override
	public void prePacket(GuiButton par1GuiButton){
		if(par1GuiButton.id==BUTTON_CHANGE_EXP){
			this.setExpToConsume(this.getExpToConsume() + 5);
//			Unsaga.debug(this.expToConsume,this.getClass());


			if(this.getExpToConsume()>30){
				this.setExpToConsume(5);
			}
			this.changeButton.displayString = String.valueOf(getExpToConsume())+"XP";
		}
	}
	@Override
	public NBTTagCompound getSendingArgs(){
		NBTTagCompound nbt = UtilNBT.compound();
		nbt.setInteger("exp", this.getExpToConsume());
//		Byte[] args = new Byte[1];
//		args[0] = (byte)this.getExpToConsume();
		return nbt;
	}
}
