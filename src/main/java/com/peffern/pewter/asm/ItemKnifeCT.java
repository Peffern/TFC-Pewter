package com.peffern.pewter.asm;

import java.util.ListIterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.launchwrapper.IClassTransformer;

import static org.objectweb.asm.Opcodes.*;

/**
 * Insert the evalBowl call so that when you rc with a knife, it inserts the custom bowls as well as the old ceramic ones.
 * @author peffern
 *
 */
public class ItemKnifeCT implements IClassTransformer
{

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) 
	{
		if(name.equals("com.bioxx.tfc.Items.Tools.ItemKnife"))
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
			if((m.name.equals("onItemUse") || m.name.equals("func_77648_a")) && m.desc.equals("(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;IIIIFFF)Z"))
			{
				ListIterator<AbstractInsnNode> it = m.instructions.iterator();
				//iterate over the bytecode instructions
				while(it.hasNext())
				{
					AbstractInsnNode insn = it.next();
					if(insn instanceof FrameNode)
					{
						FrameNode f = (FrameNode)insn;
						if(f.type == F_APPEND && f.local.size() == 3)
						{
							VarInsnNode var0 = new VarInsnNode(ILOAD, 12);
							VarInsnNode var1 = new VarInsnNode(ILOAD, 13);
							VarInsnNode var2 = new VarInsnNode(ALOAD, 2);
							MethodInsnNode method0 = new MethodInsnNode(INVOKESTATIC, "com/peffern/pewter/TFCPewter", "evalBowl", "(IILnet/minecraft/entity/player/EntityPlayer;)I", false);
							VarInsnNode var3 = new VarInsnNode(ISTORE, 12);
							LabelNode label0 = new LabelNode();
							
							m.instructions.insert(f,var0);
							m.instructions.insert(var0,var1);
							m.instructions.insert(var1,var2);
							m.instructions.insert(var2,method0);
							m.instructions.insert(method0,var3);
							m.instructions.insert(var3,label0);
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
