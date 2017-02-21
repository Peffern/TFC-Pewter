package com.peffern.pewter;

import java.util.List;

import com.bioxx.tfc.Food.ItemMeal;
import com.bioxx.tfc.Food.ItemSalad;
import com.bioxx.tfc.Render.Item.FoodItemRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

/** Custom Salad Item 
 *	@author peffern 
 */
public class ItemCustomSalad extends ItemMeal
{
	
	public ItemCustomSalad()
	{
		super();
		this.hasSubtypes = true;
		this.metaNames = new String[]{"Salad0","Salad1","Salad2","Salad3"};
		
	}
	
	@Override
	public void registerIcons(IIconRegister registerer)
	{
		
		metaIcons = new IIcon[metaNames.length];
		for(int i = 0; i < metaNames.length; i++)
		{
			metaIcons[i] = registerer.registerIcon(TFCPewter.MODID + ":" + this.textureFolder + metaNames[i]);
		}
		
		//This will prevent NullPointerException errors with other mods like NEI
		this.itemIcon = metaIcons[0];
		MinecraftForgeClient.registerItemRenderer(this, new FoodItemRenderer());
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(Item item, CreativeTabs tabs, List list)
	{
		list.add(createTag(new ItemStack(this, 1)));
	}
	
	public static ItemStack createTag(ItemStack is)
	{
		return ItemSalad.createTag(is);
	}
	
	
	
	@Override
	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
	{
		ItemStack is = super.onEaten(stack, world, player);
		
		if (is.stackSize == 0)
		{
			//give player item
			boolean added = player.inventory.addItemStackToInventory(new ItemStack(TFCPewter.pewterBowlDirty));
			if (!added)
			{
				//failed to add, so drop
				return new ItemStack(TFCPewter.pewterBowlDirty);
			}
		}
		
		return is;
	}
}
