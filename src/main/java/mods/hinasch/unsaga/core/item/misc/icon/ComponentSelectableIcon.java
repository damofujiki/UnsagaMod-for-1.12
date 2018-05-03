package mods.hinasch.unsaga.core.item.misc.icon;

import mods.hinasch.lib.capability.CapabilityAdapterFactory.ICapabilityAdapterPlan;
import mods.hinasch.lib.capability.CapabilityAdapterFrame;
import mods.hinasch.lib.capability.ComponentCapabilityAdapters;
import mods.hinasch.lib.iface.IIconItem;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.util.Statics;
import mods.hinasch.lib.util.UtilNBT;
import mods.hinasch.unsaga.UnsagaMod;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ComponentSelectableIcon {

	@CapabilityInject(INegaPosi.class)
	public static Capability<INegaPosi> CAPA;

//	public static Predicate<AttachCapabilitiesEvent> predicate = ev -> HSLibs.cast(ev, in -> in.g() instanceof IIconItem)t
	public static ICapabilityAdapterPlan<INegaPosi> capabilityAdapter = new ICapabilityAdapterPlan(){

		@Override
		public Class getCapabilityClass() {
			// TODO 自動生成されたメソッド・スタブ
			return INegaPosi.class;
		}

		@Override
		public IStorage getStorage() {
			// TODO 自動生成されたメソッド・スタブ
			return new Storage();
		}

		@Override
		public Class getDefault() {
			// TODO 自動生成されたメソッド・スタブ
			return DefaultImpl.class;
		}

		@Override
		public Capability getCapability() {
			// TODO 自動生成されたメソッド・スタブ
			return CAPA;
		}
	};
	public static CapabilityAdapterFrame<INegaPosi> adapterBase = UnsagaMod.capabilityAdapterFactory.create(capabilityAdapter);
	public static ComponentCapabilityAdapters.ItemStack<INegaPosi> adapter = (ComponentCapabilityAdapters.ItemStack<INegaPosi>) adapterBase.createChildItem("iconItem");
	static{
		adapter.setPredicate(ev -> HSLibs.cast(ev, in -> in.getItem() instanceof IIconItem)).setRequireSerialize(true);;
	}

	public static final int COLOR_DARKER = 0x555555;
//	protected static final String KEY = "nega_posi";

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass)
    {
    	if(stack.hasCapability(CAPA, null)){
    		return stack.getCapability(CAPA, null).isNegative() ? COLOR_DARKER : Statics.COLOR_NONE;
    	}

    	return Statics.COLOR_NONE;
    }

    public static void setNegative(ItemStack is,boolean par1){
    	if(is!=null && is.hasCapability(CAPA, null)){
    		UnsagaMod.logger.trace("icon", par1);
    		is.getCapability(CAPA, null).setNegative(par1);
    	}
    }

    public static boolean isNegative(ItemStack is){
    	if(is!=null && is.hasCapability(CAPA, null)){
    		return is.getCapability(CAPA, null).isNegative();
    	}
    	return false;
    }

//    public static void setNegative(ItemStack is,boolean par1){
//    	UtilNBT.initNBTIfNotInit(is);
//    	UtilNBT.setFreeTag(is, KEY, par1);
//    }
//
//    public static boolean isNegative(ItemStack is){
//    	if(UtilNBT.hasKey(is, KEY)){
//    		return UtilNBT.readFreeTagBool(is, KEY);
//    	}
//    	return false;
//    }

    public static interface INegaPosi{
    	public boolean isNegative();
    	public void setNegative(boolean par1);
    }

    public static class DefaultImpl implements INegaPosi{

    	boolean isNegative = false;
		@Override
		public boolean isNegative() {
			// TODO 自動生成されたメソッド・スタブ
			return this.isNegative;
		}

		@Override
		public void setNegative(boolean par1) {
			// TODO 自動生成されたメソッド・スタブ
			this.isNegative = par1;
		}

    }

    public static class Storage implements IStorage<INegaPosi>{

		@Override
		public NBTBase writeNBT(Capability<INegaPosi> capability, INegaPosi instance, EnumFacing side) {
			NBTTagCompound tag = UtilNBT.compound();
			tag.setBoolean("negative", instance.isNegative());
			return tag;
		}

		@Override
		public void readNBT(Capability<INegaPosi> capability, INegaPosi instance, EnumFacing side, NBTBase nbt) {
			// TODO 自動生成されたメソッド・スタブ
			if(nbt instanceof NBTTagCompound){
				instance.setNegative(((NBTTagCompound) nbt).getBoolean("negative"));
			}
		}

    }


//    public static class AttachEvent{
//
//    	@SubscribeEvent
//    	public void onConstruct(AttachCapabilitiesEvent.Item e){
//
//    		if(e.getItem() instanceof IIconItem){
//    			e.addCapability(new ResourceLocation(UnsagaMod.MODID,"selectableIcon"), new ICapabilitySerializable<NBTBase>(){
//
//    				INegaPosi inst = CAPA.getDefaultInstance();
//					@Override
//					public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
//						// TODO 自動生成されたメソッド・スタブ
//						return CAPA==capability && CAPA!=null;
//					}
//
//					@Override
//					public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
//						// TODO 自動生成されたメソッド・スタブ
//						return (CAPA==capability && CAPA!=null)? (T)inst : null;
//					}
//
//					@Override
//					public NBTBase serializeNBT() {
//						// TODO 自動生成されたメソッド・スタブ
//						return CAPA.getStorage().writeNBT(CAPA, inst, null);
//					}
//
//					@Override
//					public void deserializeNBT(NBTBase nbt) {
//						// TODO 自動生成されたメソッド・スタブ
//						CAPA.getStorage().readNBT(CAPA, inst, null, nbt);
//					}}
//    			);
//    		}
//    	}
//    }
}
