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

public class ClientProxyCT implements IClassTransformer
{
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) 
	{
		if(name.equals("com.bioxx.tfc.ClientProxy"))
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
			if((m.name.equals("registerTileEntities")) && m.desc.equals("(Z)V"))
			{
				ListIterator<AbstractInsnNode> it = m.instructions.iterator();
				//iterate over the bytecode instructions
				while(it.hasNext())
				{
					AbstractInsnNode insn = it.next();
					if(insn instanceof TypeInsnNode)
					{
						TypeInsnNode tinsn = (TypeInsnNode)insn;
						if(tinsn.desc.equals("com/bioxx/tfc/Render/TESR/TESRIngotPile"))
						{
							TypeInsnNode newTinsn = new TypeInsnNode(tinsn.getOpcode(), "com/peffern/pewter/CustomTESRIngotPile");
							m.instructions.insert(tinsn,newTinsn);
							m.instructions.remove(tinsn);
						}
					}
					else if(insn instanceof MethodInsnNode)
					{
						MethodInsnNode minsn = (MethodInsnNode)insn;
						if(minsn.owner.equals("com/bioxx/tfc/Render/TESR/TESRIngotPile"))
						{
							MethodInsnNode newMinsn = new MethodInsnNode(minsn.getOpcode(), "com/peffern/pewter/CustomTESRIngotPile", minsn.name, minsn.desc, minsn.itf);
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
