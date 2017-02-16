package com.peffern.pewter;


import com.bioxx.tfc.Core.Metal.MetalRegistry;
import com.bioxx.tfc.Items.ItemIngot;
import com.bioxx.tfc.Items.ItemMetalSheet;
import com.bioxx.tfc.Items.ItemMetalSheet2x;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.bioxx.tfc.TerraFirmaCraft;
import com.bioxx.tfc.Core.Recipes;
import com.bioxx.tfc.Core.Metal.Alloy;
import com.bioxx.tfc.Core.Metal.AlloyManager;
import com.bioxx.tfc.api.HeatIndex;
import com.bioxx.tfc.api.HeatRaw;
import com.bioxx.tfc.api.HeatRegistry;
import com.bioxx.tfc.api.Metal;
import com.bioxx.tfc.api.TFCFluids;
import com.bioxx.tfc.api.TFCItems;
import com.bioxx.tfc.api.Constant.Global;
import com.bioxx.tfc.api.Crafting.AnvilManager;
import com.bioxx.tfc.api.Crafting.AnvilRecipe;
import com.bioxx.tfc.api.Crafting.AnvilReq;
import com.bioxx.tfc.api.Crafting.BarrelManager;
import com.bioxx.tfc.api.Crafting.BarrelRecipe;
import com.bioxx.tfc.api.Crafting.PlanRecipe;
import com.bioxx.tfc.api.Enums.RuleEnum;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;


@Mod(modid = TFCPewter.MODID, name = TFCPewter.MODNAME, version = TFCPewter.VERSION, dependencies = "required-before:" + "terrafirmacraft" + ";")
public class TFCPewter 
{
	
	/** Mod ID String */
	public static final String MODID = "tfcpewter";
	
	/** Mod Name */
	public static final String MODNAME = "TFC Pewter";
	
	/** Mod Version */
	public static final String VERSION = "1.4";
	
	private static int pewterMetalID = 21;
	
	public static Item pewterBowl;
	
	public static Item pewterBowlDirty;
	
	public static Item saladPewterBowl;
	
	public static Item pewterJug;
	
	public static Item pewterUnshaped;
	
	public static Item pewterIngot;
	
	public static Item pewterIngot2X;
	
	public static Item pewterSheet;
	
	public static Item pewterSheet2X;
	
	public static Metal PEWTER;
	
	private static boolean anvilInitialized = false;
	
	public static void setPewterMetalID(int id)
	{
		pewterMetalID = id;
	}
	
	public static int getPewterMetalID()
	{
		return pewterMetalID;
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e)
	{
		pewterBowl = new ItemCustomBowl().setUnlocalizedName("Metal Bowl");
		GameRegistry.registerItem(pewterBowl, pewterBowl.getUnlocalizedName());
		
		pewterBowlDirty = new ItemCustomBowl()
		{
			@Override
			public void registerIcons(IIconRegister registerer)
			{
				this.itemIcon = registerer.registerIcon(TFCPewter.MODID + ":" + "Metal Bowl Dirty");
			}
		}.setUnlocalizedName("Metal Bowl Dirty");
		GameRegistry.registerItem(pewterBowlDirty, pewterBowlDirty.getUnlocalizedName());
		
		saladPewterBowl = new ItemCustomSalad().setUnlocalizedName("Clean Salad");
		GameRegistry.registerItem(saladPewterBowl, saladPewterBowl.getUnlocalizedName());
		GameRegistry.addShapelessRecipe(new ItemStack(pewterBowl), new ItemStack(pewterBowlDirty), new ItemStack(TFCItems.woodenBucketWater));
		
		pewterJug = new ItemCustomJug().setUnlocalizedName("Metal Jug");
		GameRegistry.registerItem(pewterJug,pewterJug.getUnlocalizedName());
		
		pewterUnshaped = new ItemCustomMelted().setUnlocalizedName("moltenPewter");
		GameRegistry.registerItem(pewterUnshaped, pewterUnshaped.getUnlocalizedName());
		
		pewterIngot = new ItemIngot()
		{
			@Override
			public void registerIcons(IIconRegister registerer)
			{
				this.itemIcon = registerer.registerIcon(TFCPewter.MODID + ":" + "pewterIngot");
			}
		}.setUnlocalizedName("pewterIngot");
		GameRegistry.registerItem(pewterIngot, pewterIngot.getUnlocalizedName());
		
		pewterIngot2X = new ItemIngot()
		{
			@Override
			public void registerIcons(IIconRegister registerer)
			{
				this.itemIcon = registerer.registerIcon(TFCPewter.MODID + ":" + "pewterDoubleIngot");
			}
		}.setMetal("Pewter", 200).setUnlocalizedName("pewterDoubleIngot");
		GameRegistry.registerItem(pewterIngot2X, pewterIngot2X.getUnlocalizedName());

		pewterSheet = new ItemMetalSheet(pewterMetalID)
		{
			@Override
			public void registerIcons(IIconRegister registerer)
			{
				this.itemIcon = registerer.registerIcon(TFCPewter.MODID + ":" + "pewterSheet");
			}
		}.setMetal("Pewter", 200).setUnlocalizedName("pewterSheet");
		GameRegistry.registerItem(pewterSheet, pewterSheet.getUnlocalizedName());

		pewterSheet2X = new ItemMetalSheet2x(pewterMetalID)
		{
			@Override
			public void registerIcons(IIconRegister registerer)
			{
				this.itemIcon = registerer.registerIcon(TFCPewter.MODID + ":" + "pewterDoubleSheet");
			}
		}.setMetal("Pewter", 400).setUnlocalizedName("pewterDoubleSheet");
		GameRegistry.registerItem(pewterSheet2X, pewterSheet2X.getUnlocalizedName());

		
		PEWTER = new Metal("Pewter", pewterUnshaped, pewterIngot);
		
		MetalRegistry.instance.addMetal(PEWTER, Alloy.EnumTier.TierI);
		
		Alloy Pewter = new Alloy(PEWTER, Alloy.EnumTier.TierI);
		Pewter.addIngred(Global.TIN, 84.99f, 99.01f);
		Pewter.addIngred(Global.COPPER, 0.99f, 15.01f);
		AlloyManager.INSTANCE.addAlloy(Pewter);
		
		TerraFirmaCraft.PACKET_PIPELINE.registerPacket(InitClientWorldPacket.class);
		
		FMLCommonHandler.instance().bus().register(new PlayerTracker());
		
		FMLCommonHandler.instance().bus().register(new ServerTickHandler());
		
		MinecraftForge.EVENT_BUS.register(new ChunkEventHandler());

		HeatRegistry manager = HeatRegistry.getInstance();
		
		HeatRaw PewterRaw = new HeatRaw(0.17, 440);
		
		manager.addIndex(new HeatIndex(new ItemStack(pewterUnshaped,1), PewterRaw,new ItemStack(pewterUnshaped,1)));
		manager.addIndex(new HeatIndex(new ItemStack(pewterIngot,1), PewterRaw,new ItemStack(pewterUnshaped,1)));
		manager.addIndex(new HeatIndex(new ItemStack(pewterIngot2X,1), PewterRaw,new ItemStack(pewterUnshaped,2)));
		manager.addIndex(new HeatIndex(new ItemStack(pewterSheet,1), PewterRaw,new ItemStack(pewterUnshaped,2)));
		manager.addIndex(new HeatIndex(new ItemStack(pewterSheet2X,1), PewterRaw,new ItemStack(pewterUnshaped,4)));
		
		GameRegistry.addShapelessRecipe(new ItemStack(pewterUnshaped, 1, 0), 
				new Object[] {Recipes.getStackNoTemp(new ItemStack(pewterIngot, 1)), new ItemStack(TFCItems.ceramicMold, 1, 1)});

		GameRegistry.addShapelessRecipe(new ItemStack(pewterIngot, 1, 0), 
				new Object[] {Recipes.getStackNoTemp(new ItemStack(pewterUnshaped, 1))});
		
		BarrelManager.getInstance().addRecipe(new BarrelRecipe(new ItemStack(pewterBowlDirty), new FluidStack(TFCFluids.FRESHWATER, 200), new ItemStack(pewterBowl), new FluidStack(TFCFluids.FRESHWATER, 200)).setMinTechLevel(0).setSealedRecipe(false));

		FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.FRESHWATER, 1333), new ItemStack(pewterJug, 1, 1), new ItemStack(pewterJug,1, 0));

	
	}
	
	public static boolean anvilIsInit()
	{
		return anvilInitialized;
	}
	
	public static void anvilInit(World world)
	{
		if(anvilIsInit())
			return;
				
		
		AnvilManager manager = AnvilManager.getInstance();
		AnvilManager.world = world;
		
		//custom anvil plans
		manager.addPlan("metaljug", new PlanRecipe(new RuleEnum[]{RuleEnum.UPSETLAST, RuleEnum.HITSECONDFROMLAST, RuleEnum.DRAWTHIRDFROMLAST}));
		manager.addPlan("metalbowl", new PlanRecipe(new RuleEnum[]{RuleEnum.HITLAST, RuleEnum.BENDSECONDFROMLAST, RuleEnum.SHRINKTHIRDFROMLAST}));
		
		//manufacturing items
		manager.addRecipe(new AnvilRecipe(new ItemStack(pewterSheet2X), null, "metaljug", false, AnvilReq.BRONZE, new ItemStack(pewterJug)).clearRecipeSkills());
		manager.addRecipe(new AnvilRecipe(new ItemStack(pewterSheet), null, "metalbowl", false, AnvilReq.BRONZE, new ItemStack(pewterBowl, 2)).clearRecipeSkills());
		manager.addRecipe(new AnvilRecipe(new ItemStack(pewterIngot2X), null, "sheet", false, AnvilReq.BRONZE, new ItemStack(pewterSheet)));
		
		//double ingot/sheet
		manager.addWeldRecipe(new AnvilRecipe(new ItemStack(pewterIngot),new ItemStack(pewterIngot),AnvilReq.BRONZE, new ItemStack(pewterIngot2X, 1)));
		manager.addWeldRecipe(new AnvilRecipe(new ItemStack(pewterSheet),new ItemStack(pewterSheet),AnvilReq.BRONZE, new ItemStack(pewterSheet2X, 1)));
		
		try
		{
			//pewter sheet trapdoords
			Method addTrapDoor = Recipes.class.getDeclaredMethod("addTrapDoor", Item.class, int.class);
			addTrapDoor.setAccessible(true);
			addTrapDoor.invoke(null, pewterSheet, pewterMetalID);
		}
		catch(InvocationTargetException ex)
		{
			ex.getTargetException().printStackTrace();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		anvilInitialized = true;
	
	
	
	}
	
	/**
	 * ASM recovery for ItemKnife adding bowls to Food Prep GUI. Checks the given inventory slot for a metal bowl
	 */
	public static int evalBowl(int hasBowl, int i, EntityPlayer player)
	{
		if(i < player.inventory.mainInventory.length && (player.inventory.mainInventory[i] != null && player.inventory.mainInventory[i].getItem() == pewterBowl))
			return i;
		else
			return hasBowl;
	}
	
	
}
