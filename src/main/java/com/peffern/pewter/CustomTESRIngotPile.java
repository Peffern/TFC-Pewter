package com.peffern.pewter;

import org.lwjgl.opengl.GL11;

import com.bioxx.tfc.Reference;
import com.bioxx.tfc.Blocks.BlockIngotPile;
import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.Render.Models.ModelIngotPile;
import com.bioxx.tfc.Render.TESR.TESRIngotPile;
import com.bioxx.tfc.TileEntities.TEIngotPile;
import com.bioxx.tfc.api.TFCBlocks;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public class CustomTESRIngotPile extends TESRIngotPile
{
	private final ModelIngotPile model = new ModelIngotPile();

	@Override
	public void renderTileEntityIngotPileAt(TEIngotPile te, double a, double b, double c, float d)
	{

		Block pile = te.getBlockType();
		if (te.getWorldObj() != null && te.getStackInSlot(0) != null && pile == TFCBlocks.ingotPile)
		{
			int i = ((BlockIngotPile) pile).getStack(te.getWorldObj(), te);
			String dir = (te.type.equals("Pewter")?TFCPewter.MODID:Reference.MOD_ID);
			TFC_Core.bindTexture(new ResourceLocation(dir, "textures/blocks/metal/" + te.type + ".png"));
			GL11.glPushMatrix();
			GL11.glTranslatef((float)a + 0.0F, (float)b + 0F, (float)c + 0.0F); //size
			model.renderIngots(i);
			GL11.glPopMatrix();
		}
	}
}
