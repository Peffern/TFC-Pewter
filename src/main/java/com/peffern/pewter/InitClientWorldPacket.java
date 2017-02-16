package com.peffern.pewter;

import com.bioxx.tfc.Handlers.Network.AbstractPacket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public class InitClientWorldPacket extends AbstractPacket
{
	@Override
	public void handleClientSide(EntityPlayer player) 
	{
		TFCPewter.anvilInit(player.worldObj); 
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) 
	{
		
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) 
	{
		
	}

	@Override
	public void handleServerSide(EntityPlayer player) 
	{

	}
}
