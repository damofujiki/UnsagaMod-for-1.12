package mods.hinasch.unsaga.core.entity.projectile;

import java.util.EnumSet;

import mods.hinasch.unsaga.core.client.particle.ParticleItems;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.Sub;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class EntityIceNeedle extends EntityThrowableUnsaga{

	public EntityIceNeedle(World worldIn) {
		super(worldIn);
		this.setSize(1.0F, 1.8F);
	}

    public EntityIceNeedle(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }
	@Override
	public EnumSet<General> getDamageTypes() {
		// TODO 自動生成されたメソッド・スタブ
		return EnumSet.of(General.PUNCH);
	}

	@Override
	public EnumSet<Sub> getSubDamageTypes(){
		return EnumSet.of(Sub.FREEZE);
	}

	@Override
    protected void onImpact(RayTraceResult result){
		super.onImpact(result);
		if(result.typeOfHit==Type.BLOCK){
			if(world.getBlockState(result.getBlockPos()).getMaterial().getMobilityFlag()!=EnumPushReaction.DESTROY){
				if(this.rand.nextInt(2)==0){
					this.playSound(SoundEvents.BLOCK_GLASS_BREAK, 0.8F, 1.0F+this.rand.nextFloat() * 0.1F);
				}
				this.setDead();
			}
		}


	}

	@Override
	public void setDead()
	{
		for (int i = 0; i < 8; ++i)
		{
			this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, Item.getIdFromItem(ParticleItems.ICE_NEEDLE));
		}

		super.setDead();
	}
}
