package mods.hinasch.unsaga.damage;

import java.util.EnumSet;

import com.google.common.collect.Lists;

import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.status.UnsagaStatus;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttribute;


public class DamageTypeUnsaga {


	public static interface IUnsagaDamageType{

		public IAttribute getAttribute();
		public Enum getEnum();
	}
//	public static General[] getAxeTypes(){
//		return new General[]{General.SWORD,General.PUNCH};
//	}

	public static final General[] AXE_TYPE = {General.SWORD,General.PUNCH};
	public static enum General implements IUnsagaDamageType{
		SWORD("sword"),PUNCH("punch"),SPEAR("pierce"),MAGIC("magic"),NONE("none");

		final String name;
		private General(String name){

			this.name = name;
		}

		public String getName(){
			return this.name;
		}

		@Override
		public String toString(){
			return this.getName();
		}
		@Override
		public IAttribute getAttribute() {
			return UnsagaStatus.GENERALS.get(this);
		}

		@Override
		public Enum getEnum() {
			// TODO 自動生成されたメソッド・スタブ
			return this;
		}


	}
//	private static Map<EnumSet<General>,SoundEvent> attackSoundMap = Maps.newHashMap();
//	static{
//		attackSoundMap.put(EnumSet.of(General.SWORD), SoundEvents.BLOCK_PISTON_EXTEND);
//		attackSoundMap.put(EnumSet.of(General.SPEAR), SoundEvents.ENTITY_ARROW_HIT);
//		attackSoundMap.put(EnumSet.of(General.PUNCH), SoundEvents.BLOCK_SLIME_HIT);
//		attackSoundMap.put(EnumSet.of(General.SWORD,General.PUNCH), SoundEvents.BLOCK_ANVIL_PLACE);
//	}
//
//	/** 今の所スライムの攻撃で使用*/
//	public static SoundEvent getAttackSound(EnumSet<General> other){
//		Optional<SoundEvent> opt = attackSoundMap.entrySet().stream().filter(in -> in.getKey().containsAll(other)).map(in -> in.getValue()).findFirst();
//		return opt.isPresent() ? opt.get() : SoundEvents.ENTITY_PLAYER_HURT;
//	}

	public static enum Sub implements IUnsagaDamageType
	{FIRE("fire"),ELECTRIC("electric"),FREEZE("freeze"),SHOCK("shock"),NONE("none");


		final String name;
		private Sub(String name){

			this.name = name;
		}

		public String getName(){
			return this.name;
		}


		@Override
		public IAttribute getAttribute() {
			return UnsagaStatus.SUBS.get(this);
		}

		@Override
		public Enum getEnum() {
			// TODO 自動生成されたメソッド・スタブ
			return this;
		}
	}


	public static <T extends Enum> float getDamageModifierFromType(T type,EntityLivingBase target,float baseStr){
		return getDamageModifierFromType(EnumSet.copyOf(Lists.newArrayList(type)), target, baseStr);
	}

	/**
	 * 攻撃する側のタイプ
	 *
	 * 攻撃する側のタイプ
	 * @param types
	 * 被害者
	 * @param target
	 * ダメージ
	 * @param baseStr
	 * @return
	 */
	public static <T extends Enum<T>> float getDamageModifierFromType(EnumSet<T> types,EntityLivingBase target,float baseStr){
		float negative = 0.0F;
		float positive = 0.0F;
		float modifier = 0.0F;

		for(T type:types){

			IAttribute attribute = type instanceof General ? UnsagaEntityAttributes.GENERALS.get(type) : UnsagaEntityAttributes.SUBS.get(type);
			if(target.getEntityAttribute(attribute)!=null){
				double value = target.getEntityAttribute(attribute).getAttributeValue();

				if(value<0.0D){
					negative += value;
				}else{
					positive += value;
				}
			}

		}


		if(baseStr + negative<0){
			negative = -baseStr;
		}
		modifier += negative;

		modifier += positive;
		UnsagaMod.logger.trace("base:"+baseStr+" modifier:"+modifier);

		return modifier;
	}
}
