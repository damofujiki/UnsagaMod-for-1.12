package mods.hinasch.unsaga.material;

import mods.hinasch.unsaga.common.tool.ComponentUnsagaWeapon;
import mods.hinasch.unsaga.util.ToolCategory;

public interface IUnsagaMaterialSelector {

	public ToolCategory getCategory();
	public ComponentUnsagaWeapon getComponent();
}
