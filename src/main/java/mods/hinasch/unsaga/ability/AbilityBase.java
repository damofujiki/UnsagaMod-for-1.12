package mods.hinasch.unsaga.ability;

import mods.hinasch.lib.registry.PropertyElementBase;
import mods.hinasch.lib.util.UtilNBT.RestoreFunc;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public abstract class AbilityBase extends PropertyElementBase implements IAbility{

	protected int exclusionNum = 0;

	public AbilityBase(String name) {
		super(new ResourceLocation(name), name);
	}

	@Override
	public Class getParentClass() {
		// TODO 自動生成されたメソッド・スタブ
		return AbilityBase.class;
	}

	@Override
	public void writeToNBT(NBTTagCompound stream) {
		stream.setString("id",this.getKey().getResourcePath());

	}

	@Override
	public String toString(){
		return this.getPropertyName();
	}

	public String getUnlocalizedName(){
		return "ability."+this.getPropertyName();
	}
	public static final RestoreFunc<IAbility> FUNC_RESTORE = new RestoreFunc<IAbility>(){

		@Override
		public IAbility apply(NBTTagCompound input) {
			String id = input.getString("id");
			IAbility ability = AbilityRegistry.instance().get(id);
			return ability!=null ? ability : AbilityRegistry.instance().EMPTY;
		}
	};
}
