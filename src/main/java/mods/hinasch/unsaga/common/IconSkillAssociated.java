package mods.hinasch.unsaga.common;

import mods.hinasch.lib.client.GuiContainerBase;
import mods.hinasch.lib.client.GuiContainerBase.IconButtonDisableable;
import mods.hinasch.unsaga.skillpanel.SkillPanel;
import mods.hinasch.unsaga.skillpanel.SkillPanelAPI;
import net.minecraft.entity.player.EntityPlayer;

public abstract class IconSkillAssociated<T extends GuiContainerBase> extends IconButtonDisableable<T>{

	protected final SkillPanel skill;

	public SkillPanel getAssociatedSkill(){
		return this.skill;
	}

	public IconSkillAssociated(int id, int x, int y, int u, int v, boolean hover,SkillPanel skill) {
		super(id, x, y, u, v, hover);
		this.skill = skill;
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public abstract EntityPlayer getPlayer(T gui);

	public boolean isDisabled(T gui){
		EntityPlayer ep = this.getPlayer(gui);
		return !SkillPanelAPI.hasPanel(ep, skill);
	}
}
