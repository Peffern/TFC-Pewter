package com.peffern.pewter;


import com.bioxx.tfc.Core.Metal.Alloy.EnumTier;
import com.bioxx.tfc.TerraFirmaCraft;
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
import com.peffern.metals.BaseMetal;
import com.peffern.metals.IMetal;
import com.peffern.metals.Ingredient;
import com.peffern.metals.MetalData;
import com.peffern.metals.MetalsRegistry;

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

/**
 * Mod to make pewter a new alloy so you can make better bowls and jugs
 * @author peffern
 *
 */
@Mod(modid = TFCPewter.MODID, name = TFCPewter.MODNAME, version = TFCPewter.VERSION, dependencies = "required-before:" + "terrafirmacraft" + ";" + "required-after:" + "tfcmetals")
public class TFCPewter 
{
	
	/** Mod ID String */
	public static final String MODID = "tfcpewter";
	
	/** Mod Name */
	public static final String MODNAME = "TFC Pewter";
	
	/** Mod Version */
	public static final String VERSION = "2.1";
	
	/** Plain bowl */
	public static Item pewterBowl;
	
	/** Bowl needs to be washed */
	public static Item pewterBowlDirty;
	
	/** Salad item */
	public static Item saladPewterBowl;
	
	/** Pewter jug item */
	public static Item pewterJug;
	
	/** molten ingot */
	public static Item pewterUnshaped;
	
	/** ingot */
	public static Item pewterIngot;
	
	/** welded ingot */
	public static Item pewterIngot2X;
	
	/** sheet */
	public static Item pewterSheet;
	
	/** welded sheet */
	public static Item pewterSheet2X;
	
	/** Pewter metal instance in the registry */
	public static Metal PEWTER;
	
	/** anvil state */
	private static boolean anvilInitialized = false;
	
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
		//add bucket cleaning registry
		GameRegistry.addShapelessRecipe(new ItemStack(pewterBowl), new ItemStack(pewterBowlDirty), new ItemStack(TFCItems.woodenBucketWater));
		
		pewterJug = new ItemCustomJug().setUnlocalizedName("Metal Jug");
		GameRegistry.registerItem(pewterJug,pewterJug.getUnlocalizedName());
		
		
		//fml event things
		TerraFirmaCraft.PACKET_PIPELINE.registerPacket(InitClientWorldPacket.class);
		
		FMLCommonHandler.instance().bus().register(new PlayerTracker());
		
		FMLCommonHandler.instance().bus().register(new ServerTickHandler());
		
		MinecraftForge.EVENT_BUS.register(new ChunkEventHandler());

		
		IMetal metal = new BaseMetal
		(
			"Pewter",
			"moltenPewter",
			TFCPewter.MODID + ":" + "moltenPewter",
			"pewterIngot",
			TFCPewter.MODID + ":" + "pewterIngot",
			"pewterDoubleIngot",
			TFCPewter.MODID + ":" + "pewterDoubleIngot",
			"pewterSheet",
			TFCPewter.MODID + ":" + "pewterSheet",
			"pewterDoubleSheet",
			TFCPewter.MODID + ":" + "pewterDoubleSheet",
			TFCPewter.MODID + ":" + "metal/Pewter",
			TFCPewter.MODID + ":" + "metal/Pewter Trap Door",
			TFCPewter.MODID,
			"textures/blocks/metal/Pewter.png",
			new Ingredient[]{new Ingredient(Global.TIN, 84.99f, 99.01f), new Ingredient(Global.COPPER, 0.99f, 15.01f)},
			0.17,
			440,
			EnumTier.TierI,
			AnvilReq.BRONZE
		);
		MetalData data = MetalsRegistry.addMetal(metal);
		
		pewterUnshaped = data.unshaped;
		pewterIngot = data.ingot;
		pewterIngot2X = data.ingot2X;
		pewterSheet = data.sheet;
		pewterSheet2X = data.sheet2X;
		PEWTER = data.metal;
		//barrel bowl cleaning recipe
		BarrelManager.getInstance().addRecipe(new BarrelRecipe(new ItemStack(pewterBowlDirty), new FluidStack(TFCFluids.FRESHWATER, 200), new ItemStack(pewterBowl), new FluidStack(TFCFluids.FRESHWATER, 200)).setMinTechLevel(0).setSealedRecipe(false));

		//jug is a fluid container
		FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.FRESHWATER, 1333), new ItemStack(pewterJug, 1, 1), new ItemStack(pewterJug,1, 0));

	
	}
	
	/**
	 * determine anvil status
	 * @return anvil status
	 */
	public static boolean anvilIsInit()
	{
		return anvilInitialized;
	}
	
	/**
	 * Do anvil setup
	 * @param world the world
	 */
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
		
		anvilInitialized = true;
	
	
	
	}
	
	/**
	 * ASM recovery for ItemKnife adding bowls to Food Prep GUI. Checks the given inventory slot for a metal bowl
	 * @param hasBowl previous position of bowl, if one exists
	 * @param i position to currently check
	 * @param player the player
	 */
	public static int evalBowl(int hasBowl, int i, EntityPlayer player)
	{
		if(i < player.inventory.mainInventory.length && (player.inventory.mainInventory[i] != null && player.inventory.mainInventory[i].getItem() == pewterBowl))
			return i;
		else
			return hasBowl;
	}
	
	
}
