package com.peffern.pewter.asm;

import java.util.ListIterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import net.minecraft.launchwrapper.IClassTransformer;

public class BlockSetupCT implements IClassTransformer
{

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) 
	{
		if(name.equals("com.bioxx.tfc.BlockSetup"))
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
			if((m.name.equals("loadBlocks")) && m.desc.equals("()V"))
			{
				ListIterator<AbstractInsnNode> it = m.instructions.iterator();
				//iterate over the bytecode instructions
				while(it.hasNext())
				{
					AbstractInsnNode insn = it.next();
					if(insn instanceof TypeInsnNode)
					{
						TypeInsnNode tinsn = (TypeInsnNode)insn;
						if(tinsn.desc.equals("com/bioxx/tfc/Blocks/BlockMetalSheet"))
						{
							TypeInsnNode newTinsn = new TypeInsnNode(tinsn.getOpcode(), "com/peffern/pewter/BlockCustomMetalSheet");
							m.instructions.insert(tinsn, newTinsn);
							m.instructions.remove(tinsn);
						}
						else if(tinsn.desc.equals("com/bioxx/tfc/Blocks/BlockMetalTrapDoor"))
						{
							TypeInsnNode newTinsn = new TypeInsnNode(tinsn.getOpcode(), "com/peffern/pewter/BlockCustomMetalTrapDoor");
							m.instructions.insert(tinsn, newTinsn);
							m.instructions.remove(tinsn);
						}
					}
					else if(insn instanceof MethodInsnNode)
					{
						MethodInsnNode minsn = (MethodInsnNode)insn;
						if(minsn.owner.equals("com/bioxx/tfc/Blocks/BlockMetalSheet"))
						{
							MethodInsnNode newMinsn = new MethodInsnNode(minsn.getOpcode(), "com/peffern/pewter/BlockCustomMetalSheet", minsn.name, minsn.desc, minsn.itf);
							m.instructions.insert(minsn, newMinsn);
							m.instructions.remove(minsn);
						}
						else if(minsn.owner.equals("com/bioxx/tfc/Blocks/BlockMetalTrapDoor"))
						{
							MethodInsnNode newMinsn = new MethodInsnNode(minsn.getOpcode(), "com/peffern/pewter/BlockCustomMetalTrapDoor", minsn.name, minsn.desc, minsn.itf);
							m.instructions.insert(minsn, newMinsn);
							m.instructions.remove(minsn);
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
