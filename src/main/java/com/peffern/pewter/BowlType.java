package com.peffern.pewter;

import java.lang.reflect.Method;
import java.util.Random;

import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.Food.ItemFoodTFC;
import com.bioxx.tfc.Items.ItemTerra;
import com.bioxx.tfc.TileEntities.TEFoodPrep;
import com.bioxx.tfc.api.Food;
import com.bioxx.tfc.api.TFCItems;
import com.bioxx.tfc.api.Constant.Global;
import com.bioxx.tfc.api.Interfaces.IFood;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Enum for ceramic or pewter bowls
 * handles salad stuff
 * @author peffern
 *
 */
public enum BowlType 
{
	/** The TFC Ceramic Bowl */
	CERAMIC(TFCItems.potteryBowl, TFCItems.salad),
	/** The custom Pewter Bowl */
	METAL(TFCPewter.pewterBowl, TFCPewter.saladPewterBowl),
	/** NO bowl */
	NONE(null,null);
	
	private static final float[] saladWeights = new float[]{10,4,4,2};
	
	/** The bowl item for this enum instance */
	private Item item;
	/** the Salad item to create */
	private Item salad;
	
	private BowlType(Item i, Item s)
	{
		item = i;
		salad = s;
	}
	
	public Item getItem()
	{
		return item;
	}
	
	/**
	 * Convenience method to setup item stack
	 * @return the item stack
	 */
	public ItemStack makeSalad()
	{
		if(salad == null)
			return null;
		else
			return new ItemStack(salad, 1);
	}
	
	/**
	 * Actually perform the salad creation method
	 * @param player the player (for skills purposes)
	 * @param te the tile entity food prep
	 */
	public static void createSalad(EntityPlayer player, TEFoodPrep te)
	{
		BowlType bt = BowlType.typeSalad(te);
		if(bt != NONE)//Bread
		{
			ItemStack is = bt.makeSalad();

			float w = 0;
			for(int i = 0; i < 4; i++)
			{
				ItemStack f = te.getStackInSlot(i+1);
				if (f != null && Food.getWeight(f) >= saladWeights[i])
					w += saladWeights[i];
			}
			
			ItemFoodTFC.createTag(is, w);
			Food.setDecayRate(is, 2.0F);

			int[] foodGroups = new int[]{-1,-1,-1,-1};
			if(te.getStackInSlot(1) != null) foodGroups[0] = ((IFood)(te.getStackInSlot(1).getItem())).getFoodID();
			if(te.getStackInSlot(2) != null) foodGroups[1] = ((IFood)(te.getStackInSlot(2).getItem())).getFoodID();
			if(te.getStackInSlot(3) != null) foodGroups[2] = ((IFood)(te.getStackInSlot(3).getItem())).getFoodID();
			if(te.getStackInSlot(4) != null) foodGroups[3] = ((IFood)(te.getStackInSlot(4).getItem())).getFoodID();

			Food.setFoodGroups(is, foodGroups);

			try
			{
				Method m = te.getClass().getDeclaredMethod("getIconSeed", new Class<?>[0]);
				m.setAccessible(true);
				Object result = m.invoke(te, new Object[0]);
				long l = ((Long)result).longValue();
				is.setItemDamage(new Random(l).nextInt(((ItemTerra)TFCItems.salad).metaIcons.length));
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				is.setItemDamage(new Random().nextInt(((ItemTerra)TFCItems.salad).metaIcons.length));
			}
				
			try
			{
				Method m = te.getClass().getDeclaredMethod("combineTastes", NBTTagCompound.class, float[].class, ItemStack[].class);
				m.setAccessible(true);
				m.invoke(te, new Object[]{is.getTagCompound(), saladWeights, new ItemStack[]{te.getStackInSlot(1), te.getStackInSlot(2), te.getStackInSlot(3), te.getStackInSlot(4)}});
			}
			catch(Exception ex)
			{
				combineTastes(is.getTagCompound(), saladWeights, te.getStackInSlot(1), te.getStackInSlot(2), te.getStackInSlot(3), te.getStackInSlot(4));
			}
			
			Food.setMealSkill(is, TFC_Core.getSkillStats(player).getSkillRank(Global.SKILL_COOKING).ordinal());
			te.setInventorySlotContents(6, is);

			te.consumeFoodWeight(saladWeights, te.getStackInSlot(1), te.getStackInSlot(2), te.getStackInSlot(3), te.getStackInSlot(4));

			Item item = bt.getItem();
			
			TFC_Core.getItemInInventory(item, te).stackSize--;
		}
	}
	
	/**
	 * Make sure a salad can be made
	 * @param te the food prep te
	 * @return true if can make salad
	 */
	public static boolean validateSalad(TEFoodPrep te)
	{
		return (typeSalad(te) != NONE);
	}
	
	/** Get the bowl type to use for a salad
	 * 
	 * @param te the current food prep Tile Entity
	 * @return the bowl type instance enum
	 */
	public static BowlType typeSalad(TEFoodPrep te)
	{
		if(te.lastTab == 1)
		{
			if(te.storage[6] != null)
				return NONE;

			if(!te.validateIngreds(te.storage[1],te.storage[2],te.storage[3],te.storage[4]))
				return NONE;

			float weight = 0;
			for(int i = 0; i < 4; i++)
			{
				ItemStack f = te.getStackInSlot(i+1);
				if (f != null && Food.getWeight(f) - Food.getDecay(f) >= saladWeights[i])
				{
					weight += saladWeights[i];
				}
			}

			if(weight < 14)
				return NONE;

			ItemStack bowlStack = TFC_Core.getItemInInventory(TFCItems.potteryBowl, te);
			if(bowlStack != null && bowlStack.getItemDamage() == 1)
			{
				return CERAMIC;
			}
			else
			{
				bowlStack = TFC_Core.getItemInInventory(TFCPewter.pewterBowl, te);
				if(bowlStack != null)
				{
					return METAL;
				}
				else
				{
					return NONE;
				}
			}
		}
		return NONE;
	}
	
	/**
	 * Cooking parameters
	 */
	public static void combineTastes(NBTTagCompound nbt, float[] weights, ItemStack... isArray)
	{
		int tasteSweet = 0;
		int tasteSour = 0;
		int tasteSalty = 0;
		int tasteBitter = 0;
		int tasteUmami = 0;

		for (int i = 0; i < isArray.length; i++)
		{
			float weightMult = 1f;//weights[i] / totalW * 2;
			if(isArray[i] != null)
			{
				tasteSweet += ((IFood)isArray[i].getItem()).getTasteSweet(isArray[i]) * weightMult;
				tasteSour += ((IFood)isArray[i].getItem()).getTasteSour(isArray[i]) * weightMult;
				tasteSalty += ((IFood)isArray[i].getItem()).getTasteSalty(isArray[i]) * weightMult;
				tasteBitter += ((IFood)isArray[i].getItem()).getTasteBitter(isArray[i]) * weightMult;
				tasteUmami += ((IFood)isArray[i].getItem()).getTasteSavory(isArray[i]) * weightMult;
			}
		}
		nbt.setInteger("tasteSweet", tasteSweet);
		nbt.setInteger("tasteSour", tasteSour);
		nbt.setInteger("tasteSalty", tasteSalty);
		nbt.setInteger("tasteBitter", tasteBitter);
		nbt.setInteger("tasteUmami", tasteUmami);
	}
}
