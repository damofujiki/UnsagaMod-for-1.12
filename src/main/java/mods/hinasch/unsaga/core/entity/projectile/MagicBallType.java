package mods.hinasch.unsaga.core.entity.projectile;

import java.util.EnumSet;

import javax.annotation.Nullable;

import com.mojang.realmsclient.util.Pair;

import mods.hinasch.lib.iface.IIntSerializable;
import mods.hinasch.lib.particle.ParticleHelper;
import mods.hinasch.lib.particle.ParticleHelper.MovingType;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.core.client.particle.ParticleItems;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.Sub;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

//	public static ParticleMagicBallBreak particleFactory = new ParticleMagicBallBreak();
public enum MagicBallType implements IIntSerializable{
	NONE(0),FIREBALL(1),BUBBLE(2),THUNDER_CRAP(3),BLASTER(4),BOULDER(5),ACID(6);

	public static MagicBallType fromMeta(int meta){
		return HSLibs.fromMeta(MagicBallType.values(), meta);
	}
	int meta;

	private MagicBallType(int meta){
		this.meta = meta;
	}

	public General getDamageType(){
		switch(this){
		case BLASTER:
			break;
		case BUBBLE:
			return General.PUNCH;
		case FIREBALL:
			break;
		case NONE:
			break;
		case THUNDER_CRAP:
			break;
		default:
			break;

		}
		return General.MAGIC;
	}
	@Override
	public int getMeta() {
		// TODO 自動生成されたメソッド・スタブ
		return this.meta;
	}


	public @Nullable EnumParticleTypes getParticle(){
		if(this==FIREBALL){
			return EnumParticleTypes.FLAME;
		}
		if(this==BLASTER){
			return EnumParticleTypes.PORTAL;
		}
		return null;
	}
	public Pair<Integer,ParticleHelper.MovingType> getParticleSetting(){
		if(this==FIREBALL){
			return Pair.of(15,MovingType.DIVERGE);
		}
		if(this==BLASTER){
			return Pair.of(30,MovingType.FLOATING);
		}
		return null;
	}
	public EnumSet<Sub> getSubDamageTypes(){
		switch(this){
		case BLASTER:
			break;
		case BUBBLE:
			return EnumSet.of(Sub.FREEZE);
		case FIREBALL:
			return EnumSet.of(Sub.FIRE);
		case NONE:
			break;
		case THUNDER_CRAP:
			return EnumSet.of(Sub.ELECTRIC);
		case ACID:
			return EnumSet.of(Sub.SHOCK);
		default:
			break;

		}
		return EnumSet.noneOf(Sub.class);
	}
	public @Nullable Item getTextureItem(){
		switch(this){
		case BOULDER:
			return ParticleItems.BOULDER;
		case BLASTER:
			break;
		case BUBBLE:
			return ParticleItems.BUBBLE_BALL;
		case FIREBALL:
			return ParticleItems.FIRE_BALL;
		case NONE:
			break;
		case THUNDER_CRAP:
			return ParticleItems.THUNDER_CRAP;
		case ACID:
			return ParticleItems.THUNDER_CRAP;
		default:
			break;

		}
		return null;
	}

	public boolean isDestroyable(World world,BlockPos pos,IBlockState state){
		switch(this){
		case BLASTER:
			break;
		case BOULDER:
			if(state.getMaterial()==Material.GLASS && state.getBlockHardness(world, pos)<=2.0F){
				return true;
			}
			if((state.getMaterial()==Material.ICE || state.getMaterial()==Material.PACKED_ICE)&& state.getBlockHardness(world, pos)<=2.0F){
				return true;
			}
			break;
		case BUBBLE:
			if(state.getMaterial()==Material.FIRE){
				return true;
			}
			break;
		case FIREBALL:
			if(state.getMaterial()==Material.VINE || state.getMaterial()==Material.WEB){
				return true;
			}
			if((state.getMaterial()==Material.ICE || state.getMaterial()==Material.PACKED_ICE)&& state.getBlockHardness(world, pos)<=2.0F){
				return true;
			}
			break;
		case NONE:
			break;
		case THUNDER_CRAP:
			break;
		default:
			break;

		}
		return false;
	}
	public boolean isExtinguish(IBlockState state){
		if(this==FIREBALL){
			if((state.getMaterial()==Material.ICE || state.getMaterial()==Material.PACKED_ICE)){
				return true;
			}
		}
		if(this==BUBBLE){
			if(state.getMaterial()==Material.FIRE){
				return true;
			}
		}
		return false;
	}

	public boolean onDead(){
		return false;
	}
}