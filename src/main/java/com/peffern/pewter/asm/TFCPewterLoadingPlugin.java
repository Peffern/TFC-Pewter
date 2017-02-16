package com.peffern.pewter.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.TransformerExclusions({"com.peffern.pewter"})
public class TFCPewterLoadingPlugin implements IFMLLoadingPlugin
{
	@Override
	public String[] getASMTransformerClass() 
	{
		return new String[]{ItemKnifeCT.class.getName(), TEFoodPrepCT.class.getName(), ClientProxyCT.class.getName(), BlockSetupCT.class.getName(), RecipesCT.class.getName()};
	}

	@Override
	public String getModContainerClass() 
	{
		return null;
	}

	@Override
	public String getSetupClass() 
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) 
	{
		
	}

	@Override
	public String getAccessTransformerClass() 
	{
		return null;
	}
}
