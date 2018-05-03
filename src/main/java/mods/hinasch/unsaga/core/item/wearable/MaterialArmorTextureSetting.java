package mods.hinasch.unsaga.core.item.wearable;

import java.util.Set;

import mods.hinasch.unsaga.core.item.wearable.ItemArmorUnsaga.ArmorTexture;
import mods.hinasch.unsaga.material.UnsagaMaterial;
import mods.hinasch.unsaga.material.UnsagaMaterialRegistry;
import mods.hinasch.unsaga.material.UnsagaMaterials;
import mods.hinasch.unsaga.util.ToolCategory;

public class MaterialArmorTextureSetting {

	static UnsagaMaterialRegistry m = UnsagaMaterialRegistry.instance();

	public static enum RenderSize{
		NORMAL,THIN;
	}
	public static final ArmorTexture THIN_ARMOR = new ArmorTexture("armor","armor2").setRenderSize(RenderSize.THIN);
	public static final ArmorTexture FEATHER = new ArmorTexture("feather","armor2").setRenderSize(RenderSize.THIN);
	public static final ArmorTexture FUR = new ArmorTexture("fur","armor2");
	public static final ArmorTexture WOOD = new ArmorTexture("wood","armor2");
	public static final ArmorTexture BOOTS = new ArmorTexture("fur","armor2").setRenderSize(RenderSize.THIN);
	public static final ArmorTexture CIRCLET = new ArmorTexture("circlet","armor2").setRenderSize(RenderSize.THIN);
	public static final ArmorTexture HEADBAND = new ArmorTexture("headband","armor2").setRenderSize(RenderSize.THIN);
	public static final ArmorTexture MASK = new ArmorTexture("mask","armor2");
	public static final ArmorTexture TRANSPARENT = new ArmorTexture("nothing","nothing");
	public static void register(){
		UnsagaMaterials.SILK.addArmorTexture(ToolCategory.HELMET,CIRCLET);
		UnsagaMaterials.VELVET.addArmorTexture(ToolCategory.HELMET,CIRCLET);
		UnsagaMaterials.COTTON.addArmorTexture(ToolCategory.HELMET,CIRCLET);
		UnsagaMaterials.LIVE_SILK.addArmorTexture(ToolCategory.HELMET,CIRCLET);
		UnsagaMaterials.SILVER.addArmorTexture(ToolCategory.HELMET,CIRCLET);
		UnsagaMaterials.DIAMOND.addArmorTexture(ToolCategory.HELMET,CIRCLET);
		UnsagaMaterials.SAPPHIRE.addArmorTexture(ToolCategory.HELMET,CIRCLET);
		UnsagaMaterials.RUBY.addArmorTexture(ToolCategory.HELMET,CIRCLET);
		UnsagaMaterials.OBSIDIAN.addArmorTexture(ToolCategory.HELMET,MASK);
		UnsagaMaterials.SILK.addArmorTexture(ToolCategory.LEGGINS,TRANSPARENT);
		UnsagaMaterials.VELVET.addArmorTexture(ToolCategory.LEGGINS,TRANSPARENT);
		UnsagaMaterials.COTTON.addArmorTexture(ToolCategory.LEGGINS,TRANSPARENT);
		UnsagaMaterials.LIVE_SILK.addArmorTexture(ToolCategory.LEGGINS,TRANSPARENT);
		UnsagaMaterials.HYDRA_LEATHER.addArmorTexture(ToolCategory.HELMET,CIRCLET);
		UnsagaMaterials.CROCODILE_LEATHER.addArmorTexture(ToolCategory.HELMET,HEADBAND);
		UnsagaMaterials.SNAKE_LEATHER.addArmorTexture(ToolCategory.HELMET,HEADBAND);
		UnsagaMaterials.FUR.addArmorTexture(ToolCategory.HELMET, FUR);
		UnsagaMaterials.FEATHER.addArmorTexture(ToolCategory.HELMET, FEATHER);
		UnsagaMaterials.CROCODILE_LEATHER.addArmorTexture(ToolCategory.BOOTS,BOOTS);
		UnsagaMaterials.SNAKE_LEATHER.addArmorTexture(ToolCategory.BOOTS,BOOTS);
		UnsagaMaterials.FUR.addArmorTexture(ToolCategory.BOOTS,BOOTS);
		UnsagaMaterials.HYDRA_LEATHER.addArmorTexture(ToolCategory.BOOTS,BOOTS);
		addToCategory(m.materials_cloth,ToolCategory.ARMOR,THIN_ARMOR);
		addToCategory(m.materials_wood,ToolCategory.ARMOR,WOOD);
		addToCategory(m.materials_wood,ToolCategory.ARMOR,WOOD);
		addToCategory(m.materials_wood,ToolCategory.HELMET,WOOD);
		addToCategory(m.materials_wood,ToolCategory.BOOTS,WOOD);
	}

	public static void addToCategory(Set<UnsagaMaterial> cate,ToolCategory tool,ArmorTexture tex){
		for(UnsagaMaterial m:cate){
			m.addArmorTexture(tool, tex);
		}
	}
}
