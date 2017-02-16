package com.peffern.pewter.asm;

import java.util.ListIterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.launchwrapper.IClassTransformer;

import static org.objectweb.asm.Opcodes.*;

public class RecipesCT implements IClassTransformer
{

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) 
	{
		if(name.equals("com.bioxx.tfc.Core.Recipes"))
			return asmify(basicClass);
		else
			return basicClass;
	}
	
	private byte[] asmify(byte[] bytes)
	{
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);
		for(MethodNode m : classNode.methods)
		{
			if((m.name.equals("addTrapDoor")) && m.desc.equals("(Lnet/minecraft/item/Item;I)V"))
			{
				ListIterator<AbstractInsnNode> it = m.instructions.iterator();
				//iterate over the bytecode instructions
				while(it.hasNext())
				{
					AbstractInsnNode insn = it.next();
					if(insn instanceof InsnNode)
					{
						InsnNode theInsn = (InsnNode)insn;
						if(theInsn.getOpcode() == RETURN)
						{
							AbstractInsnNode previous = theInsn.getPrevious();
							
							InsnList insns = new InsnList();
							
							insns.add(new VarInsnNode(ALOAD, 2));
							insns.add(new TypeInsnNode(NEW, "com/bioxx/tfc/api/Crafting/AnvilRecipe"));
							insns.add(new InsnNode(DUP));
							insns.add(new TypeInsnNode(NEW, "net/minecraft/item/ItemStack"));
							insns.add(new InsnNode(DUP));
							insns.add(new VarInsnNode(ALOAD, 0));
							insns.add(new MethodInsnNode(INVOKESPECIAL, "net/minecraft/item/ItemStack", "<init>", "(Lnet/minecraft/item/Item;)V", false));
							insns.add(new TypeInsnNode(NEW, "net/minecraft/item/ItemStack"));
							insns.add(new InsnNode(DUP));
							insns.add(new FieldInsnNode(GETSTATIC, "com/peffern/pewter/TFCPewter", "pewterIngot", "Lnet/minecraft/item/Item;"));
							insns.add(new MethodInsnNode(INVOKESPECIAL, "net/minecraft/item/ItemStack", "<init>", "(Lnet/minecraft/item/Item;)V", false));
							insns.add(new LdcInsnNode("trapdoor"));
							insns.add(new FieldInsnNode(GETSTATIC, "com/bioxx/tfc/api/Crafting/AnvilReq", "COPPER", "Lcom/bioxx/tfc/api/Crafting/AnvilReq;"));
							insns.add(new TypeInsnNode(NEW, "net/minecraft/item/ItemStack"));
							insns.add(new InsnNode(DUP));
							insns.add(new FieldInsnNode(GETSTATIC, "com/bioxx/tfc/api/TFCBlocks", "metalTrapDoor", "Lnet/minecraft/block/Block;"));
							insns.add(new InsnNode(ICONST_1));
							insns.add(new VarInsnNode(ILOAD, 1));
							insns.add(new MethodInsnNode(INVOKESTATIC, "com/peffern/pewter/TFCPewter", "getPewterMetalID", "()I", false));
							insns.add(new InsnNode(ICONST_5));
							insns.add(new InsnNode(ISHL));
							insns.add(new InsnNode(IADD));
							insns.add(new MethodInsnNode(INVOKESPECIAL, "net/minecraft/item/ItemStack", "<init>", "(Lnet/minecraft/block/Block;II)V", false));
							insns.add(new MethodInsnNode(INVOKESPECIAL, "com/bioxx/tfc/api/Crafting/AnvilRecipe", "<init>", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;Ljava/lang/String;Lcom/bioxx/tfc/api/Crafting/AnvilReq;Lnet/minecraft/item/ItemStack;)V", false));
							insns.add(new MethodInsnNode(INVOKEVIRTUAL, "com/bioxx/tfc/api/Crafting/AnvilManager", "addRecipe", "(Lcom/bioxx/tfc/api/Crafting/AnvilRecipe;)V", false));
							
							m.instructions.insert(previous, insns);							
						}
					}
				}
			}
		}
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
	}

}
