package mods.hinasch.unsaga.core.client.gui;

import java.util.List;

import com.google.common.collect.Lists;

import joptsimple.internal.Strings;
import mods.hinasch.lib.client.GuiContainerBase;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.chest.ChestHelper;
import mods.hinasch.unsaga.chest.ChestHelper.BaseDifficulty;
import mods.hinasch.unsaga.chest.IChestBehavior;
import mods.hinasch.unsaga.chest.IChestCapability;
import mods.hinasch.unsaga.chest.ChestTrap;
import mods.hinasch.unsaga.chest.ChestTraps;
import mods.hinasch.unsaga.common.IconSkillAssociated;
import mods.hinasch.unsaga.core.inventory.container.ContainerChestUnsaga;
import mods.hinasch.unsaga.skillpanel.SkillPanel;
import mods.hinasch.unsaga.skillpanel.SkillPanelAPI;
import mods.hinasch.unsaga.skillpanel.SkillPanelRegistry;
import mods.hinasch.unsaga.skillpanel.SkillPanels;
import mods.hinasch.unsaga.util.UnsagaTextFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class GuiChest extends GuiContainerBase{


	//protected ResourceLocation guiPanel = new ResourceLocation(Unsaga.DOMAIN+":textures/gui/box.png");

	//アクセ情報、宝箱情報同期させる必要性
	public static final int OPEN = 1;
	public static final int UNLOCK = 2;
	public static final int DEFUSE = 3;
	public static final int DIVINATION = 4;
	public static final int PENETRATION = 5;
	public static final int UNKNOWN_CHEST = 10;
	public static final int NEEDLE = 20;
	public static final int POISON = 21;
	public static final int EXPLODE = 22;
	public static final int SLIME = 23;
	public IChestCapability theChest;
	public EntityPlayer openPlayer;
	public String message = Strings.EMPTY;

	protected ContainerChestUnsaga container;

	public GuiChest(IChestBehavior chest,EntityPlayer ep) {
		super(new ContainerChestUnsaga(chest,ep));
		this.container = (ContainerChestUnsaga) this.inventorySlots;
		this.theChest = chest.getCapability();
		this.openPlayer = ep;
		// TODO 自動生成されたコンストラクター・スタブ
	}


	@Override
	public void initGui()
	{
		super.initGui();
		int i = width  - xSize >> 1;
		int j = height - ySize >> 1;
		//
		//
		//		// ボタンを追加
		//		// GuiButton(ボタンID, ボタンの始点X, ボタンの始点Y, ボタンの幅, ボタンの高さ, ボタンに表示する文字列)
		//		this.addButton(OPEN, i + 32, j + 16 +(18*1), 30, 19 , Translation.localize("gui.chest.button.open"));
		//		this.addButton(UNLOCK, i + 32, j + 16 +(18*2), 31, 19 ,  Translation.localize("gui.chest.button.unlock"));
		//		this.addButton(DEFUSE, i + 32, j + 16 +(18*3), 30, 19 , Translation.localize("gui.chest.button.defuse"));
		//		this.addButton(DIVINATION, i + 32, j + 16 +(18*4), 30, 19 , Translation.localize("gui.chest.button.divination"));
		//		this.addButton(PENETRATION, i + 32, j + 16 +(18*5), 30, 19 , Translation.localize("gui.chest.button.penetration"));
		int iconYpos = 80;
//		SkillPanelRegistry reg = SkillPanelRegistry.instance();
		this.addIcon(new IconButton(OPEN, 8, iconYpos, 0, 168, true));
		this.addIcon(new IconSkill(DEFUSE, 24*1+8, iconYpos, 16, 168, true,SkillPanels.DEFUSE));
		this.addIcon(new IconSkill(PENETRATION, 24*2+8, iconYpos, 32, 168, true,SkillPanels.SHARP_EYE));
		this.addIcon(new IconSkill(DIVINATION, 24*3+8, iconYpos, 48, 168, true,SkillPanels.FORTUNE));
		this.addIcon(new IconSkill(UNLOCK, 24*4+8, iconYpos, 64, 168, true,SkillPanels.LOCKSMITH));

		this.addIcon(new Icon(UNKNOWN_CHEST, 84, 40, 64, 184, true){
			@Override
			public boolean isVisible(GuiContainerBase gui){
				if(gui instanceof GuiChest){
					GuiChest guiChest = (GuiChest) gui;
					return !guiChest.theChest.hasAnalyzed() && !guiChest.openPlayer.isCreative();
				}
				return false;
			}
		});
		this.addIcon(new IconTrap(NEEDLE, 84, 40, 0, 184, true,ChestTraps.NEEDLE));
		this.addIcon(new IconTrap(POISON, 84+16*1, 40, 16, 184, true,ChestTraps.POISON));
		this.addIcon(new IconTrap(EXPLODE, 84+16*2, 40, 32, 184, true,ChestTraps.EXPLODE));
		this.addIcon(new IconTrap(SLIME, 84+16*3, 40, 48, 184, true,ChestTraps.SLIME));
		//鍵かかっているかどうかも追加する
	}


	public void updateCapability(IChestCapability capa){

		UnsagaMod.logger.trace(this.getClass().getName(), capa.getTraps());
		this.theChest = capa;
	}
	@Override
	protected void drawGuiContainerForegroundLayer(int par1,int par2)
	{
		super.drawGuiContainerForegroundLayer(par1, par2);

		this.fontRenderer.drawString(HSLibs.translateKey("word.chest")+" LV:"+this.theChest.getLevel(), 84, 20, 0x404040);
		this.fontRenderer.drawString(HSLibs.translateKey(this.message), 16, 140, 0x404040);

		//fontRenderer.drawString("Result:"+getSpellStr(), 8, (ySize - 96) + 2, 0x404040);
	}

	@Override
	public String getGuiTextureName(){
		return UnsagaMod.MODID+":textures/gui/container/chest.png";
	}

	@Override
	public String getGuiName(){
		return "";
	}

	@Override
	public void onGuiClosed(){
		super.onGuiClosed();
		//    	ExtendedPlayerData.getData(openPlayer).setInteractingChest(null);
	}


	@Override
	public List<String> getIconHoverText(Icon id){
		List<String> list = Lists.newArrayList();
		SkillPanelRegistry reg = SkillPanelRegistry.instance();
		float prob = 0;
		switch(id.id){
		case OPEN:
			list.add(HSLibs.translateKey("gui.unsaga.chest.open"));
			break;
		case DEFUSE:
			list.add(HSLibs.translateKey("gui.unsaga.chest.defuse"));

			break;
		case UNLOCK:
			list.add(HSLibs.translateKey("gui.unsaga.chest.unlock"));

			break;
		case DIVINATION:
			list.add(HSLibs.translateKey("gui.unsaga.chest.divine"));

			break;
		case SLIME:
			list.add(HSLibs.translateKey("gui.unsaga.chest.trap.slime"));
			break;
		case POISON:
			list.add(HSLibs.translateKey("gui.unsaga.chest.trap.poison"));
			break;
		case NEEDLE:
			list.add(HSLibs.translateKey("gui.unsaga.chest.trap.needle"));
			break;
		case EXPLODE:
			list.add(HSLibs.translateKey("gui.unsaga.chest.trap.explode"));
			break;
		case PENETRATION:
			list.add(HSLibs.translateKey("gui.unsaga.chest.penetrate"));

			break;
		case UNKNOWN_CHEST:
			list.add(HSLibs.translateKey("gui.unsaga.chest.unknown"));
			break;
		}

		if(id instanceof IconSkill){
			if(((IconSkill) id).isDisabled(this)){
				list.add(UnsagaTextFormatting.PROPERTY_LOCKED+HSLibs.translateKey("gui.unsaga.chest.skill.disabled"));
			}else{
				if(SkillPanelAPI.hasPanel(openPlayer, ((IconSkill) id).getAssociatedSkill())){
					list.add(ChestHelper.getInteractionSuccessProb(((IconSkill) id).getBaseDifficulty(), this.theChest,
							SkillPanelAPI.getHighestPanelLevel(openPlayer, ((IconSkill) id).getAssociatedSkill()).getAsInt())*100+"%");
				}

			}
		}
		return list;
	}

	@Override
	public void onPacketFromServer(NBTTagCompound message){
		String str = message.getString("message");
		this.message = str;

	}

	public static class IconSkill extends IconSkillAssociated<GuiChest>{

		public IconSkill(int id, int x, int y, int u, int v, boolean hover, SkillPanel skill) {
			super(id, x, y, u, v, hover, skill);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		public BaseDifficulty getBaseDifficulty(){
			return ChestHelper.getBaseDifficulty(this.getAssociatedSkill());
		}

		@Override
		public EntityPlayer getPlayer(GuiChest gui) {
			// TODO 自動生成されたメソッド・スタブ
			return gui.openPlayer;
		}

	}

	public static class IconTrap extends Icon<GuiChest>{

		final ChestTrap trap;
		public IconTrap(int id, int x, int y, int u, int v, boolean hover,ChestTrap trap) {
			super(id, x, y, u, v, hover);
			this.trap = trap;
			// TODO 自動生成されたコンストラクター・スタブ
		}

		public boolean isVisible(GuiChest gui){
			if(gui instanceof GuiChest){

				IChestCapability capa = gui.theChest;
				if(capa.hasAnalyzed() || gui.openPlayer.isCreative())
				return capa.getTraps().contains(trap);
			}
			return false;
		}


	}
}
