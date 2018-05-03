package mods.hinasch.unsaga.core.item.misc.skillpanel;

import mods.hinasch.unsaga.skillpanel.SkillPanel;

public interface ISkillPanel {

	public SkillPanel getPanel();
	public int getLevel();
	public void setPanel(SkillPanel panel);
	public void setLevel(int level);
	public boolean hasJointed();
	public boolean hasLocked();
	public void setJointed(boolean par1);
	public void setLocked(boolean par1);
}
