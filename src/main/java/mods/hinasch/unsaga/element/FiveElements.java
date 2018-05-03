package mods.hinasch.unsaga.element;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import mods.hinasch.lib.iface.IIntSerializable;
import mods.hinasch.lib.particle.ParticleTypeWrapper;
import mods.hinasch.lib.particle.ParticleTypeWrapper.Particles;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.ability.Ability;
import mods.hinasch.unsaga.ability.AbilityRegistry;
import mods.hinasch.unsaga.element.newele.ElementTable;
import mods.hinasch.unsaga.skillpanel.SkillPanels;
import net.minecraft.util.EnumParticleTypes;

public class FiveElements {

	public static enum Type implements IIntSerializable{
		FIRE("fire",0),EARTH("earth",4),WOOD("wood",3),METAL("metal",2)
		,WATER("water",1),FORBIDDEN("forbidden",5);

		private int meta;
		private String name;

		private Type(String name,int meta){
			this.name = name;
			this.meta = meta;
		}

		@Override
		public int getMeta(){
			return this.meta;
		}

		public String getUnlocalized(){
			return "element."+this.name;
		}

		public String getLocalized(){
			return HSLibs.translateKey(getUnlocalized());
		}

		public int getElementColor(){
			switch(this){
			case FIRE:return 0xff0000;
			case EARTH:return 0x8b4513;
			case WOOD:return 0x6b8e23;
			case METAL:return 0xffff00;
			case WATER:return 0x4169e1;
			case FORBIDDEN:return 0x800080;
			}
			return 0xffffff;
		}

		public ParticleTypeWrapper getElementParticle(){
			switch(this){
			case FIRE:return new ParticleTypeWrapper(EnumParticleTypes.FLAME);
			case EARTH:return new ParticleTypeWrapper(Particles.STONE);
			case WOOD:return new ParticleTypeWrapper(Particles.LEAVES);
			case METAL:return new ParticleTypeWrapper(EnumParticleTypes.REDSTONE);
			case WATER:return new ParticleTypeWrapper(Particles.BUBBLE);
			case FORBIDDEN:return new ParticleTypeWrapper(EnumParticleTypes.PORTAL);
			}
			return  new ParticleTypeWrapper(EnumParticleTypes.SPELL);
		}

		public String getSpellIconName(){
			return "spellIcon." + this.name;
		}

		public String getSpellBookIconName(){
			return this.name;
		}

		public ElementTable getElementsFlacture(){
			switch(this){
			case FIRE:return new ElementTable(3,-2,2,-1,-1,-1);
			case EARTH:return new ElementTable(-2,-3,1,1,-1,0);
			case WOOD:return new ElementTable(0,-1,2,-1,2,-3);
			case METAL:return new ElementTable(-1,-1,-1,3,-2,2);
			case WATER:return new ElementTable(1,-1,0,-3,3,-2);
			case FORBIDDEN:return new ElementTable(-1,0,3,0,-2,5);
			}
			return ElementTable.ZERO;
		}
		public Ability getCastableAbility(){
			switch(this){
			case FIRE:return AbilityRegistry.SPELL_FIRE;
			case EARTH:return AbilityRegistry.SPELL_EARTH;
			case WOOD:return AbilityRegistry.SPELL_WOOD;
			case METAL:return AbilityRegistry.SPELL_METAL;
			case WATER:return AbilityRegistry.SPELL_WATER;
			case FORBIDDEN:return AbilityRegistry.SPELL_FORBIDDEN;
			}
			return null;
		}

		public @Nullable mods.hinasch.unsaga.skillpanel.SkillPanel getCastableFamiliar(){
			switch(this){
			case FIRE:return SkillPanels.FAMILIAR_FIRE;
			case EARTH:return SkillPanels.FAMILIAR_EARTH;
			case WOOD:return SkillPanels.FAMILIAR_WOOD;
			case METAL:return SkillPanels.FAMILIAR_METAL;
			case WATER:return SkillPanels.FAMILIAR_WATER;
			case FORBIDDEN:return null;
			}
			return null;
		}
//		public SkillPanel getFamiliar(){
//			switch(this){
//			case FIRE:return SkillPanels.instance().familiarFire;
//			case EARTH:return SkillPanels.instance().familiarEarth;
//			case WOOD:return SkillPanels.instance().familiarWood;
//			case METAL:return SkillPanels.instance().familiarMetal;
//			case WATER:return SkillPanels.instance().familiarWater;
//			case FORBIDDEN:return null;
//			}
//			return null;
//		}

		public static FiveElements.Type fromMeta(int meta){
			return HSLibs.fromMeta(FiveElements.Type.values(), meta);
		}
	}

	public static final EnumSet<Type> VALUES = EnumSet.of(Type.FIRE,Type.EARTH,Type.WOOD,Type.METAL,Type.WATER,Type.FORBIDDEN);

	public static List<String> getSpellIconJsonNames(){
		return VALUES.stream().map(in -> in.getSpellIconName()).collect(Collectors.toList());
	}

	public static List<String> getSpellBookJsonNames(){
		return VALUES.stream().map(in -> in.getSpellBookIconName()).collect(Collectors.toList());
	}
}
