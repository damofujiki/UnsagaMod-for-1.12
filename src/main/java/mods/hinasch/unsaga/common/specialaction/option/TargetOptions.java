package mods.hinasch.unsaga.common.specialaction.option;

public class TargetOptions {

	public static IOption NONE = new TargetOption("none");
	public static IOption SELF = new TargetOption("self");
	public static IOption TARGET = new TargetOption("target");
	public static IOption OFF_HAND = new TargetOptionItem("off_hand");
	public static IOption EQUIPPED = new TargetOptionItem("equipped");
	public static IOption HEAD = new TargetOptionItem("head");
	public static IOption BODY = new TargetOptionItem("body");
	public static IOption FEET = new TargetOptionItem("feet");
	public static IOption LEGS = new TargetOptionItem("legs");
	public static IOption POSITION = new TargetOption("position");
	public static IOption IGNITABLE = new TargetOption("ignitable");
	public static IOption SERVANT_MELEE = new TargetOption("servant_melee");
	public static IOption SERVANT_BOW = new TargetOption("servant_bow");
}
