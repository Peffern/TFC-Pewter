package com.peffern.pewter;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;

/**
 * When the server starts setup the anvil recipes
 * @author peffern
 *
 */
public class ServerTickHandler 
{
    @SubscribeEvent 
    public void onServerWorldTick(WorldTickEvent e) 
    { 
        if (e.phase == Phase.START) 
        { 
        	if (e.world.provider.dimensionId == 0)
        		TFCPewter.anvilInit(e.world); 
        } 
		else if(e.phase == Phase.END)
		{

		}
    } 
}