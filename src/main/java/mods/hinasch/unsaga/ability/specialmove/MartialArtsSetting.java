package mods.hinasch.unsaga.ability.specialmove;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Maps;

import mods.hinasch.lib.util.VecUtil.EnumHorizontalDirection;
import mods.hinasch.unsaga.material.UnsagaWeightType;

public class MartialArtsSetting {

	Map<CommandSetting,Tech> moveDirectionHeavyMap = Maps.newHashMap();


	private static MartialArtsSetting INSTANCE;

	public static MartialArtsSetting instance(){
		if(INSTANCE == null){
			INSTANCE = new MartialArtsSetting();
		}
		return INSTANCE;
	}

	private MartialArtsSetting(){
	}

	public void init(){
//		SpecialMoveRegistry reg = SpecialMoveRegistry.instance();
		moveDirectionHeavyMap.put(new CommandSetting(1,UnsagaWeightType.LIGHT,EnumHorizontalDirection.FRONT,false,false), TechRegistry.AIR_THROW);
		moveDirectionHeavyMap.put(new CommandSetting(2,UnsagaWeightType.LIGHT,EnumHorizontalDirection.BACK,false,false), TechRegistry.CALLBACK);
		moveDirectionHeavyMap.put(new CommandSetting(3,UnsagaWeightType.LIGHT,EnumHorizontalDirection.SIDE,false,false), TechRegistry.CYCLONE);
		moveDirectionHeavyMap.put(new CommandSetting(3,UnsagaWeightType.LIGHT,EnumHorizontalDirection.FRONT,false,true), TechRegistry.THREE_DRAGON);
//		moveDirectionHeavyMap.put(new CommandSetting(1,UnsagaWeightType.LIGHT,EnumHorizontalDirection.BACK,true,false), SpecialMoveRegistry.sinker);
//		moveDirectionHeavyMap.put(new CommandSetting(3,UnsagaWeightType.LIGHT,EnumHorizontalDirection.SIDE,true,false), SpecialMoveRegistry.triangleKick);
		moveDirectionHeavyMap.put(new CommandSetting(3,UnsagaWeightType.LIGHT,EnumHorizontalDirection.FREE,true,false), TechRegistry.TRIANGLE_KICK);

		moveDirectionHeavyMap.put(new CommandSetting(3,UnsagaWeightType.HEAVY,EnumHorizontalDirection.FRONT,false,false), TechRegistry.RAKSHA);
//		moveDirectionHeavyMap.put(new CommandSetting(2,UnsagaWeightType.HEAVY,EnumHorizontalDirection.BACK,false,false), SpecialMoveRegistry.WATERFALL);
		moveDirectionHeavyMap.put(new CommandSetting(1,UnsagaWeightType.HEAVY,EnumHorizontalDirection.BACK,true,false), TechRegistry.SINKER);
		moveDirectionHeavyMap.put(new CommandSetting(2,UnsagaWeightType.HEAVY,EnumHorizontalDirection.NEUTRAL,true,false), TechRegistry.THUNDER_KICK);
		moveDirectionHeavyMap.put(new CommandSetting(3,UnsagaWeightType.HEAVY,EnumHorizontalDirection.FRONT,true,false), TechRegistry.FLYING_KNEE);
	}

	public Optional<Tech> getSpecialMoveFromRegistry(UnsagaWeightType weight,Set<EnumHorizontalDirection> dir,boolean isJumping,boolean isSneak,boolean isSprint){
		return this.moveDirectionHeavyMap.entrySet().stream().filter(in -> in.getKey().isApplicable(weight, dir, isJumping, isSneak,isSprint)).map(in -> in.getValue()).sorted().findFirst();
	}

	public static Optional<Tech> getSpecialMove(UnsagaWeightType weight,Set<EnumHorizontalDirection> dir,boolean isJumping,boolean isSneak,boolean isSprint){
		return instance().getSpecialMoveFromRegistry(weight, dir, isJumping, isSneak,isSprint);
	}

	public static class CommandSetting implements Comparable<CommandSetting>{

		public CommandSetting(int level,UnsagaWeightType weight,EnumHorizontalDirection direction, boolean isJump, boolean isSneak,boolean isSprint) {
			super();
			this.direction = direction;
			this.isJump = isJump;
			this.isSneak = isSneak;
			this.weight = weight;
			this.level = level;
			this.isSprint = isSprint;
		}

		public CommandSetting(int level,UnsagaWeightType weight,EnumHorizontalDirection direction, boolean isJump,boolean isSprint) {
			this(level,weight,direction,isJump,true,isSprint);
		}

		public boolean isApplicable(UnsagaWeightType weight,Set<EnumHorizontalDirection> direction,boolean isJump,boolean isSneak,boolean isSprint){
				if(direction.contains(this.direction) || this.direction==EnumHorizontalDirection.FREE){
					if(isJump==this.isJump && isSneak==this.isSneak && this.isSprint==isSprint){
						return weight==this.weight;
					}
				}

			return false;
		}

		public int getRequireLevel(){
			return this.level;
		}
		final UnsagaWeightType weight;
		final EnumHorizontalDirection direction;
		final boolean isJump;
		final boolean isSneak;
		final int level;
		final boolean isSprint;

		public int getPriority(){
			return this.isSprint ? -1 : 0;
		}
		@Override
		public int compareTo(CommandSetting o) {
			return Integer.compare(this.getPriority(), o.getPriority());
		}


	}
}
