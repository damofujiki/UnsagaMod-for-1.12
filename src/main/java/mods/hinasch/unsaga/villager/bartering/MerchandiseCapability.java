package mods.hinasch.unsaga.villager.bartering;

import java.util.OptionalInt;

import mods.hinasch.lib.capability.CapabilityAdapterFactory.ICapabilityAdapterPlan;
import mods.hinasch.lib.capability.CapabilityAdapterFrame;
import mods.hinasch.lib.capability.ComponentCapabilityAdapters;
import mods.hinasch.lib.capability.StorageDummy;
import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.ability.AbilityAPI;
import mods.hinasch.unsaga.material.MaterialItemAssociatings;
import mods.hinasch.unsaga.material.UnsagaMaterialCapability;
import mods.hinasch.unsaga.villager.smith.SmithMaterialRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class MerchandiseCapability {

	@CapabilityInject(IMerchandise.class)
	public static Capability<IMerchandise> CAPA;
	public static final String SYNC_ID = "unsaga.merchandise";

	public static ICapabilityAdapterPlan<IMerchandise> ica = new ICapabilityAdapterPlan(){

		@Override
		public Capability getCapability() {
			// TODO 自動生成されたメソッド・スタブ
			return CAPA;
		}

		@Override
		public Class getCapabilityClass() {
			// TODO 自動生成されたメソッド・スタブ
			return IMerchandise.class;
		}

		@Override
		public Class getDefault() {
			// TODO 自動生成されたメソッド・スタブ
			return DefaultImpl.class;
		}

		@Override
		public IStorage getStorage() {
			// TODO 自動生成されたメソッド・スタブ
			return new StorageDummy();
		}

	};


	public static CapabilityAdapterFrame<IMerchandise> adapterBase = UnsagaMod.capabilityAdapterFactory.create(ica);
	public static ComponentCapabilityAdapters.ItemStack<IMerchandise> adapter = adapterBase.createChildItem(SYNC_ID);

	static{
		adapter.setPredicate(ev -> ev.getObject() instanceof Item);
//		adapter.setRequireSerialize(true);
	}

	public static class DefaultImpl implements IMerchandise{

		boolean isMerchandise = false;
		OptionalInt price = OptionalInt.empty();

		@Override
		public int getPrice(ItemStack stack) {
			if(!this.price.isPresent()){
				if(UnsagaMaterialCapability.adapter.hasCapability(stack)){
					int base = UnsagaMaterialCapability.adapter.getCapability(stack).getMaterial().price;
//					if(stack.getItem() instanceof IUnsagaMaterialSelector){
//						ToolCategory category = ((IUnsagaMaterialSelector)stack.getItem()).getCategory();
//						this.price = OptionalInt.of(base + category.getCategoryPrice(base, stack));
//					}
					this.setPrice((this.getBasePrice(base) + this.applyDamageToPrice(base, stack)));
				}
				if(MaterialItemAssociatings.instance().getMaterialFromStack(stack).isPresent()){
					double amount = MaterialItemAssociatings.instance().getAmoutInDurability(stack).getAsDouble();
					int base = (int)((double)MaterialItemAssociatings.instance().getMaterialFromStack(stack).get().price * amount);
					this.setPrice((this.getBasePrice(base) + this.applyDamageToPrice(base, stack)));
				}
				if(SmithMaterialRegistry.instance().find(stack).isPresent()){
					SmithMaterialRegistry.Info info = SmithMaterialRegistry.instance().find(stack).get();
					int base = (int)((float)info.getMaterial().price * info.getAmount());
					this.setPrice(this.getBasePrice(base) + this.applyDamageToPrice(base, stack));
				}
				if(BarteringItemRegistry.instance().find(stack).isPresent()){
					int base = BarteringItemRegistry.instance().find(stack).getAsInt();
					this.setPrice((this.getBasePrice(base) + this.applyDamageToPrice(base, stack)));
				}


			}
//			UnsagaMod.logger.trace(stack.getDisplayName(), this.price.getAsInt());
			return this.price.isPresent() ? this.price.getAsInt() : 0;
		}

		public void setPrice(int price){
			if(!this.price.isPresent()){
				this.price = OptionalInt.of(price);
			}
		}
		public int getBasePrice(int base){
			return (int)((float)base * BarteringUtil.BASE_PRICE);
		}
		public int applyDamageToPrice(int base,ItemStack is){
			int additional = 0;
			if(AbilityAPI.getAttachedAbilities(is).size()>0){
				additional =(int)(base * (0.05F + 0.15F*(float)AbilityAPI.getAttachedAbilities(is).size()));
//				this.price = OptionalInt.of(this.price.getAsInt()+base);
			}
			if(is.isItemStackDamageable()){
				int durability = ItemUtil.getDurability(is);
				float per = durability / is.getMaxDamage();

				return (int)((float)base * per) + additional;
			}
			return (int)((float)base * BarteringUtil.RAW_MATERIAL_PRICE) + additional;
		}
		@Override
		public boolean isMerchadise() {
			// TODO 自動生成されたメソッド・スタブ
			return this.isMerchandise;
		}

		@Override
		public void setMerchandise(boolean par1) {
			// TODO 自動生成されたメソッド・スタブ
			this.isMerchandise = par1;
		}

		@Override
		public void setPrice(ItemStack stack) {
			if(!this.price.isPresent()){
				if(UnsagaMaterialCapability.adapter.hasCapability(stack)){
					int base = UnsagaMaterialCapability.adapter.getCapability(stack).getMaterial().price;
					this.price = OptionalInt.of((int)((float)base * 0.2F));
				}
			}

		}

		@Override
		public boolean canSell(ItemStack stack) {
			if(UnsagaMaterialCapability.adapter.hasCapability(stack)){
				return true;
			}
			if(MaterialItemAssociatings.instance().getMaterialFromStack(stack).isPresent()){
				return true;
			}
			if(SmithMaterialRegistry.instance().find(stack).isPresent()){
				return true;
			}
			if(BarteringItemRegistry.instance().find(stack).isPresent()){
				return true;
			}
			return false;
		}



	}


	public static void registerAttachEvents(){
		adapter.registerAttachEvent((inst,capa,face,ev)->{
//			if(!inst.getPrice().isPresent()){
//				inst.setPrice(stack);
//			}
		});
	}
}
