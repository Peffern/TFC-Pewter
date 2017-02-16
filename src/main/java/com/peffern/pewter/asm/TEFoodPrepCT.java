package com.peffern.pewter.asm;

import java.util.ArrayList;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.launchwrapper.IClassTransformer;

import static org.objectweb.asm.Opcodes.*;

public class TEFoodPrepCT implements IClassTransformer
{
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) 
	{
		if(name.equals("com.bioxx.tfc.TileEntities.TEFoodPrep"))
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
			if((m.name.equals("validateSalad")) && m.desc.equals("()Z"))
			{
				m.instructions = new InsnList();
				LabelNode l0 = new LabelNode();
				m.instructions.add(l0);
				m.instructions.add(new VarInsnNode(ALOAD, 0));
				m.instructions.add(new MethodInsnNode(INVOKESTATIC, "com/peffern/pewter/BowlType", "validateSalad", "(Lcom/bioxx/tfc/TileEntities/TEFoodPrep;)Z", false));
				m.instructions.add(new InsnNode(IRETURN));
				LabelNode l1 = new LabelNode();
				m.instructions.add(l1);
				m.localVariables = new ArrayList<LocalVariableNode>();
				m.localVariables.add(new LocalVariableNode("this", "Lcom/bioxx/tfc/TileEntities/TEFoodPrep;", null, l0, l1, 0));
				m.maxLocals = 1;
				m.maxStack = 1;
			}
			
			if((m.name.equals("createSalad")) && m.desc.equals("(Lnet/minecraft/entity/player/EntityPlayer;)V"))
			{
				m.instructions = new InsnList();
				LabelNode l0 = new LabelNode();
				m.instructions.add(l0);
				m.instructions.add(new VarInsnNode(ALOAD, 1));
				m.instructions.add(new VarInsnNode(ALOAD, 0));
				m.instructions.add(new MethodInsnNode(INVOKESTATIC, "com/peffern/pewter/BowlType", "createSalad", "(Lnet/minecraft/entity/player/EntityPlayer;Lcom/bioxx/tfc/TileEntities/TEFoodPrep;)V", false));
				LabelNode l1 = new LabelNode();
				m.instructions.add(l1);
				m.instructions.add(new InsnNode(RETURN));
				LabelNode l2 = new LabelNode();
				m.instructions.add(l2);
				m.localVariables = new ArrayList<LocalVariableNode>();
				m.localVariables.add(new LocalVariableNode("this", "Lcom/bioxx/tfc/TileEntities/TEFoodPrep;", null, l0, l2, 0));
				m.localVariables.add(new LocalVariableNode("player", "Lnet/minecraft/entity/player/EntityPlayer;", null, l0, l2, 1));
				m.maxLocals = 2;
				m.maxStack = 2;
				
			}
		}
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
	}
}
