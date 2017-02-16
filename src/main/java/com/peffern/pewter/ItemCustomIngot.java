package com.peffern.pewter;

import com.bioxx.tfc.Items.ItemIngot;

import net.minecraft.client.renderer.texture.IIconRegister;

public class ItemCustomIngot extends ItemIngot
{
	public ItemCustomIngot()
	{
		super();		
	}
	
	@Override
	public void registerIcons(IIconRegister registerer)
	{
		this.itemIcon = registerer.registerIcon(TFCPewter.MODID + ":" + this.getTextureName());
	}
	
	public String getTextureName()
	{
		String unlocalizedName = getUnlocalizedName();
		String textureName = unlocalizedName;
		if(unlocalizedName.indexOf('.') >= 0 && unlocalizedName.indexOf('.') < (unlocalizedName.length() -1))
			textureName = unlocalizedName.substring(unlocalizedName.indexOf('.') + 1);
		return textureName;
	}
}
