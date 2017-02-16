package com.peffern.pewter;

import com.bioxx.tfc.Blocks.BlockMetalTrapDoor;

import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockCustomMetalTrapDoor extends BlockMetalTrapDoor
{
	public BlockCustomMetalTrapDoor()
	{
		super();
		TFCPewter.setPewterMetalID(metalNames.length);
		String[] m = new String[metalNames.length + 1];
		System.arraycopy(metalNames, 0, m, 0, metalNames.length);
		m[m.length - 1] = "Pewter";
		metalNames = m;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister registerer)
	{
		super.registerBlockIcons(registerer);
		icons[TFCPewter.getPewterMetalID()] = registerer.registerIcon(TFCPewter.MODID + ":" + "metal/" + metalNames[TFCPewter.getPewterMetalID()]+" Trap Door");	}
}
