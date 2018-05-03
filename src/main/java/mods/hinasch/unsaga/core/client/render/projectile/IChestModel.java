package mods.hinasch.unsaga.core.client.render.projectile;

import mods.hinasch.unsaga.chest.FieldChestType;

public interface IChestModel {

	public float getPrevLidAngle();

	public float getLidAngle() ;

	public boolean isOpened();

	public FieldChestType getVisibilityType();

	public void setVisiblityType(FieldChestType type);
}
