package mods.hinasch.unsaga.minsaga;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import mods.hinasch.lib.capability.CapabilityAdapterFactory.CapabilityAdapterPlanImpl;
import mods.hinasch.lib.capability.CapabilityAdapterFrame;
import mods.hinasch.lib.capability.CapabilityStorage;
import mods.hinasch.lib.capability.ComponentCapabilityAdapters;
import mods.hinasch.lib.iface.INBTWritable;
import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.util.UtilNBT;
import mods.hinasch.lib.util.UtilNBT.RestoreFunc;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.minsaga.MinsagaForging.Ability;
import mods.hinasch.unsaga.minsaga.MinsagaForging.ArmorModifier;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class MinsagaForgeCapability {

	/** 最大改造回数*/
	public static final int MAX_LAYER_COUNT = 4;
	public static class DefaultImpl implements IMinsagaForge{

		NonNullList<MaterialLayer> layers = NonNullList.withSize(10, MaterialLayer.EMPTY);




		@Override
		public void addMaterialLayer(MinsagaMaterial material) {

			int index = this.getCurrentLayerIndex();
			/** フィッティングが終わってないなら現在の改造中のレイヤー、
			 * でなければ次のレイヤー*/
			if(this.getCurrentLayer().hasFinishedFitting()){
				index ++;
			}
			/** 改造限界の場合はMAX-1番目（一番最後）を上書き*/
			if(this.getLayerCount()>=MAX_LAYER_COUNT){
				index = this.getLayerCount() - 1;
			}
			MaterialLayer newForge = new MaterialLayer();
			newForge.setMaterial(material);
			newForge.setFittingProgress(0);
			newForge.setMaxFittingProgress(PROGRESS_BASE * (this.getLayerCount()+1));
			this.layers.set(index, newForge);


		}


		/** 改造されたレイヤー数を返す。*/
		@Override
		public int getLayerCount() {
			return (int) this.layers.stream().filter(in ->!in.isEmptyLayer()).count();
		}

		@Override
		public List<MaterialLayer> getMaterialLayers() {
			// TODO 自動生成されたメソッド・スタブ
			return ImmutableList.copyOf(this.layers);
		}

		@Override
		public boolean hasForged() {
			return this.getLayerCount() > 0;
		}

		@Deprecated
		@Override
		public void removeUnfinished() {
			if(this.layers!=null && !this.layers.isEmpty()){
				this.layers.removeIf((forge)->!forge.hasFinishedFitting());
			}

		}

		@Override
		public void setLayerList(NonNullList<MaterialLayer> list) {
			this.layers = list;
		}

		/** 改造されたレイヤーの一番最後（のインデックス）を返す。
		 * (鉄・鉄・なし・なし)なら1*/
		public int getCurrentLayerIndex(){
			int index = 0;
			for(int i=0;i<this.layers.size();i++){
				if(!this.layers.get(i).isEmptyLayer()){
					index = i;
				}
			}
			return index;
		}
		@Override
		public MaterialLayer getCurrentLayer() {

			return this.layers.get(this.getCurrentLayerIndex());
		}

		@Override
		public float getAttackModifier() {
			if(this.hasForged()){
				return (float) this.getMaterialLayers().stream().mapToDouble(in -> in.getMaterial().getAttackModifier()).sum();
			}
			return 0;
		}

		@Override
		public ArmorModifier getArmorModifier() {
			if(!this.hasForged()){
				return ArmorModifier.ZERO;
			}
			float melee = (float) this.getMaterialLayers().stream().mapToDouble(in -> in.getMaterial().getArmorModifier().melee()).sum();
			float magic = (float) this.getMaterialLayers().stream().mapToDouble(in -> in.getMaterial().getArmorModifier().magic()).sum();
			return new ArmorModifier(melee,magic);
		}

		@Override
		public float getEfficiencyModifier() {
			if(this.hasForged()){
				return (float) this.getMaterialLayers().stream().filter(in -> !in.isEmptyLayer()).mapToDouble(in -> in.getMaterial().getEfficiencyModifier()).sum();
			}
			return 0;
		}

		@Override
		public int getCostModifier() {
			if(this.hasForged()){
				return this.getMaterialLayers().stream().filter(in -> !in.isEmptyLayer()).mapToInt(in -> in.getMaterial().getCostModifier()).sum();
			}
			return 0;
		}

		@Override
		public int getDurabilityModifier() {
			if(this.hasForged()){
				return this.getMaterialLayers().stream().filter(in -> !in.isEmptyLayer()).mapToInt(in -> in.getMaterial().getDurabilityModifier()).sum();
			}
			return 0;
		}

		@Override
		public List<Ability> getAbilities() {
			// TODO 自動生成されたメソッド・スタブ
			return this.getMaterialLayers().stream().flatMap(in -> {
				List<Ability> list = Lists.newArrayList();
				if(in.getMaterial().hasAbilities()){
					list.addAll(in.getMaterial().getAbilities());
				}
				return list.stream();
			}).collect(Collectors.toList());
		}

		@Override
		public int getWeightModifier() {
			if(this.hasForged()){
				return this.getMaterialLayers().stream().filter(in -> !in.isEmptyLayer()).mapToInt(in -> in.getMaterial().getWeightModifier()).sum();
			}
			return 0;
		}






	}
	public static interface IStatusModifier{
		public float getAttackModifier();
		public ArmorModifier getArmorModifier();
		public float getEfficiencyModifier();
		public int getCostModifier();
		public int getDurabilityModifier();
		public int getWeightModifier();
	}
	public static class MaterialLayer implements INBTWritable{

		public static final MaterialLayer EMPTY = new MaterialLayer();
		public static RestoreFunc<MaterialLayer> RESTORE_FUNC = (input)->{
			MaterialLayer at = new MaterialLayer();
			at.setMaterial(MinsagaForging.instance().getMaterial(input.getString("material")));
			at.setFittingProgress(input.getInteger("progress"));
			at.setMaxFittingProgress(input.getInteger("max"));

			return at;
		};
		private MinsagaMaterial forgedMaterial = MinsagaForging.EMPTY;
		private int maxFitCount = 0;
		private int fittingCount = 0;
		public int getFittingProgress() {
			return fittingCount;
		}

		public boolean isEmptyLayer(){
			return this==MaterialLayer.EMPTY;
		}
		public boolean hasFinishedFitting(){
			return this.getFittingProgress() >= this.getMaxFittingProgress();
		}
		public MinsagaMaterial getMaterial() {
			return forgedMaterial;
		}
		public int getMaxFittingProgress() {
			return maxFitCount;
		}

		public void setFittingProgress(int suitCount) {
			this.fittingCount = suitCount;
		}
		public void setMaterial(MinsagaMaterial forgedMaterial) {
			this.forgedMaterial = forgedMaterial;
		}
		public void setMaxFittingProgress(int maxSuitCount) {
			this.maxFitCount = maxSuitCount;
		}



		@Override
		public void writeToNBT(NBTTagCompound stream) {
			stream.setString("material", this.forgedMaterial.getPropertyName());
			stream.setInteger("progress", this.getFittingProgress());
			stream.setInteger("max", this.getMaxFittingProgress());

		}

	}
	public static interface IMinsagaForge extends IStatusModifier{

		public List<MinsagaForging.Ability> getAbilities();
		public float getAttackModifier();
		public ArmorModifier getArmorModifier();
		public float getEfficiencyModifier();
		public MaterialLayer getCurrentLayer();
		public void addMaterialLayer(MinsagaMaterial material);
		public int getLayerCount();
		public List<MaterialLayer> getMaterialLayers();
		public boolean hasForged();
		public void removeUnfinished();
		public void setLayerList(NonNullList<MaterialLayer> list);
	}

	public static class Storage extends CapabilityStorage<IMinsagaForge>{

		@Override
		public void readNBT(NBTTagCompound comp, Capability<IMinsagaForge> capability, IMinsagaForge instance,
				EnumFacing side) {
			if(comp.hasKey("forgeAttribute")){
				List<MaterialLayer> list = UtilNBT.readListFromNBT(comp, "forgeAttribute", MaterialLayer.RESTORE_FUNC);
				instance.setLayerList(ItemUtil.toNonNull(list, MaterialLayer.EMPTY));
			}

		}

		@Override
		public void writeNBT(NBTTagCompound comp, Capability<IMinsagaForge> capability, IMinsagaForge instance,
				EnumFacing side) {
			if(instance.getMaterialLayers()!=null && !instance.getMaterialLayers().isEmpty()){
				UtilNBT.writeListToNBT(instance.getMaterialLayers(), comp, "forgeAttribute");
			}

		}

	}
	/**改造を重ねるごとに50*levelする */
	public static final int PROGRESS_BASE = 50;
	public static final int EXP_BASE = 5;

	@CapabilityInject(IMinsagaForge.class)
	public static Capability<IMinsagaForge> CAPA;

		public static CapabilityAdapterFrame<IMinsagaForge> BUILDER = UnsagaMod.capabilityAdapterFactory.create(
				new CapabilityAdapterPlanImpl(()->CAPA,()->IMinsagaForge.class,()->DefaultImpl.class,Storage::new));

		public static ComponentCapabilityAdapters.ItemStack<IMinsagaForge> ADAPTER = BUILDER.createChildItem("minsaga_forge");
		static{
			ADAPTER.setPredicate((ev)->HSLibs.cast(ev, in -> in.getItem().isRepairable()));
			ADAPTER.setRequireSerialize(true);
		}


}
