package mods.hinasch.unsaga.core.potion.old;

import com.google.common.collect.ImmutableList;

import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.Sub;

public class ShieldPropertyRegistry {

	public final ShieldProperty water = new ShieldProperty(in ->in.WATER_SHIELD ,0.0F, 0.0F, "textures/entity/magicshield/water.png")
			.setBlockableBehavior(in ->in.getSubTypes().contains(Sub.FIRE));
	public final ShieldProperty fire = new ShieldProperty(in -> in.SELF_BURNING,0.0F, 0.0F, "textures/entity/magicshield/fire.png")
			.setBlockableBehavior(dsu ->dsu.getSubTypes().contains(Sub.FIRE));
	public final ShieldProperty leaf = new ShieldProperty(in -> in.LEAF_SHIELD,0.0F, 0.5F, "textures/entity/magicshield/leaf.png")
			.setBlockableBehavior(dsu ->!dsu.isUnblockable() && !dsu.isMagicDamage() && !dsu.getDamageTypeUnsaga().contains(General.MAGIC));
	public final ShieldProperty missile = new ShieldProperty(in -> in.MISSILE_GUARD,0.0F,1.0F, "textures/entity/magicshield/missile.png")
			.setBlockableBehavior(dsu ->dsu.getDamageTypeUnsaga().contains(General.SPEAR) && !dsu.isUnblockable());
	public final ShieldProperty aegis = new ShieldProperty(in -> in.AEGIES_SHIELD,0.0F, 0.35F, "textures/entity/magicshield/aegis.png")
			.setBlockableBehavior(dsu->!dsu.isDamageAbsolute());

	public final ImmutableList<ShieldProperty> shields = ImmutableList.of(water,fire,leaf,missile,aegis);

	private static ShieldPropertyRegistry INSTANCE;
	public static ShieldPropertyRegistry instance(){
		if(INSTANCE == null){
			INSTANCE = new ShieldPropertyRegistry();
		}
		return INSTANCE;
	}

	private ShieldPropertyRegistry(){

	}
}
