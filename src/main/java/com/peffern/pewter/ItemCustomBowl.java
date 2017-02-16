package com.peffern.pewter;

import com.bioxx.tfc.Core.TFCTabs;
import com.bioxx.tfc.Items.ItemTerra;
import com.bioxx.tfc.api.Enums.EnumSize;
import com.bioxx.tfc.api.Enums.EnumWeight;

import net.minecraft.client.renderer.texture.IIconRegister;

public class ItemCustomBowl extends ItemTerra
{
	
	public ItemCustomBowl()
	{
		super();
		this.hasSubtypes = false;
		this.setMaxDamage(0);
		this.setFolder("pottery/");
		setCreativeTab(TFCTabs.TFC_POTTERY);
		this.setWeight(EnumWeight.MEDIUM);
		this.setSize(EnumSize.SMALL);
	}
	
	@Override
	public void registerIcons(IIconRegister registerer)
	{
		this.itemIcon = registerer.registerIcon(TFCPewter.MODID + ":" + "Metal Bowl");
	}
	
	
}
