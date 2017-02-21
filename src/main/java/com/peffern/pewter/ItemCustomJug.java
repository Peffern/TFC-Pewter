package com.peffern.pewter;

import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.Core.TFC_Time;
import com.bioxx.tfc.Core.Player.FoodStatsTFC;
import com.bioxx.tfc.Items.ItemTerra;
import com.bioxx.tfc.api.Enums.EnumSize;
import com.bioxx.tfc.api.Enums.EnumWeight;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

/**
 * Custom jug item
 * @author peffern
 *
 */
public class ItemCustomJug extends ItemTerra
{
	/** texture when empty */
	private IIcon emptyIcon;
	/** texture when full */
	private IIcon waterIcon;
	
	public ItemCustomJug()
	{
		super();
		this.stackable = false;
		this.metaNames = new String[]{"Metal Jug", "Water Jug"};
		setSize(EnumSize.SMALL);
		setWeight(EnumWeight.LIGHT);
	}
	
	@Override
	public void registerIcons(IIconRegister registerer)
	{
		this.emptyIcon = registerer.registerIcon(TFCPewter.MODID + ":" + "Metal Jug");
		this.waterIcon = registerer.registerIcon(TFCPewter.MODID + ":" + "Water Jug");
		this.itemIcon = emptyIcon;
	}
	
	@Override
	public IIcon getIconFromDamage(int damage)
	{
		if(damage == 0)
			return this.emptyIcon;
		else if(damage == 1)
			return this.waterIcon;
		
		return this.itemIcon;
	}
	
	@Override
	public ItemStack onEaten(ItemStack is, World world, EntityPlayer player)
	{
		if (!world.isRemote)
		{
			if(is.getItemDamage() == 1)
			{
				TFC_Core.getPlayerFoodStats(player).restoreWater(player, 32000);
			}
			
			is.setItemDamage(0);
				
		}
		return is;
	}
	
	/**
	 * How long it takes to use or consume an item
	 */
	@Override
	public int getMaxItemUseDuration(ItemStack is)
	{
		return 32;
	}
	
	/**
	 * returns the action that specifies what animation to play when the items is being used
	 */
	@Override
	public EnumAction getItemUseAction(ItemStack is)
	{
		return EnumAction.drink;
	}
	
	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer entity)
	{
		MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, entity, true);
		FoodStatsTFC fs = TFC_Core.getPlayerFoodStats(entity);
		Boolean canDrink = fs.getMaxWater(entity) - 500 > fs.waterLevel;

		boolean playNote = false;
		float lookAngle = 0;
		if(mop == null)
		{
			if(is.getItemDamage() > 0 && canDrink)
			{
				entity.setItemInUse(is, this.getMaxItemUseDuration(is));
			}
			else if(is.getItemDamage() == 0){
				Vec3 lookVec = entity.getLookVec();
				lookAngle = (float)(lookVec.yCoord/2d);
				if(!is.hasTagCompound()){
					playNote = true;
					is.stackTagCompound = new NBTTagCompound();
					is.stackTagCompound.setLong("blowTime", TFC_Time.getTotalTicks());
				}
				else if(is.stackTagCompound.hasKey("blowTime") &&	
							is.stackTagCompound.getLong("blowTime") + 10 < TFC_Time.getTotalTicks())
				{
					playNote = true;
					is.stackTagCompound.setLong("blowTime", TFC_Time.getTotalTicks());
				}
				else if(!is.stackTagCompound.hasKey("blowTime")){
					playNote = true;
					is.stackTagCompound.setLong("blowTime", TFC_Time.getTotalTicks());
				}
			}
		}
		else
		{
			if(mop.typeOfHit == MovingObjectType.BLOCK)
			{
				int x = mop.blockX;
				int y = mop.blockY;
				int z = mop.blockZ;
				
				// Handle flowing water
				int flowX = x;
				int flowY = y;
				int flowZ = z;
				switch(mop.sideHit)
				{
				case 0:
					flowY = y - 1;
					break;
				case 1:
					flowY = y + 1;
					break;
				case 2:
					flowZ = z - 1;
					break;
				case 3:
					flowZ = z + 1;
					break;
				case 4:
					flowX = x - 1;
					break;
				case 5:
					flowX = x + 1;
					break;
				}

				if (!entity.isSneaking() && !world.isRemote && 
						TFC_Core.isFreshWater(world.getBlock(x, y, z)) || TFC_Core.isFreshWater(world.getBlock(flowX, flowY, flowZ)))
				{
					if(is.getItemDamage() == 0)
					{
						is.setItemDamage(1);
						playNote = false;
					}
					else
					{
						if(canDrink)
						{
							entity.setItemInUse(is, this.getMaxItemUseDuration(is));
						}
					}
				}
				else
				{
					if(is.getItemDamage() == 1 && canDrink)
					{
						entity.setItemInUse(is, this.getMaxItemUseDuration(is));
					}
					else if(is.getItemDamage() == 0){
						Vec3 lookVec = entity.getLookVec();
						lookAngle = (float)(lookVec.yCoord/2d);
						if(!is.hasTagCompound()){
							is.stackTagCompound = new NBTTagCompound();
							is.stackTagCompound.setLong("blowTime", TFC_Time.getTotalTicks());
						}
						else if(is.stackTagCompound.hasKey("blowTime") &&	
									is.stackTagCompound.getLong("blowTime") + 10 < TFC_Time.getTotalTicks())
						{
							is.stackTagCompound.setLong("blowTime", TFC_Time.getTotalTicks());
						}
						else if(!is.stackTagCompound.hasKey("blowTime")){
							playNote = true;
							is.stackTagCompound.setLong("blowTime", TFC_Time.getTotalTicks());
						}
					}
				}

				if(!world.canMineBlock(entity, x, y, z))
				{
					return is;
				}

				if(!entity.canPlayerEdit(x, y, z, mop.sideHit, is))
				{
					return is;
				}
			}
		}
		if(playNote){
			entity.playSound(TFCPewter.MODID + ":" + "item.jug.blow", 1.0f, 1.0f + lookAngle);
		}
		return is;
	}
}
