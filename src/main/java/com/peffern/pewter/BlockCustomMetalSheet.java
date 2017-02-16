package com.peffern.pewter;

import com.bioxx.tfc.Blocks.BlockMetalSheet;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockCustomMetalSheet extends BlockMetalSheet
{
	public BlockCustomMetalSheet()
	{
		super();
		TFCPewter.setPewterMetalID(this.metalNames.length);
		String[] m = new String[this.metalNames.length + 1];
		System.arraycopy(this.metalNames, 0, m, 0, this.metalNames.length);
		m[m.length - 1] = "Pewter";
		this.metalNames = m;
		this.icons = new IIcon[metalNames.length];
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		super.registerBlockIcons(register);
		icons[TFCPewter.getPewterMetalID()] = register.registerIcon(TFCPewter.MODID + ":" + "metal/"+metalNames[TFCPewter.getPewterMetalID()]);
	}
}
