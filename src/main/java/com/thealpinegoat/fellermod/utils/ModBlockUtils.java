package com.thealpinegoat.fellermod.utils;

import net.minecraft.src.Block;

import java.lang.reflect.Constructor;

public class ModBlockUtils {
    public static <T extends Block> T OverrideBlock(Block oldBlock, Class<T> newBlockClazz) {
        return OverrideBlockWithParameters(oldBlock, newBlockClazz, new Object[]{});
    }

    public static <T extends Block> T OverrideBlockWithParameters(Block oldBlock, Class<T> newBlockClazz,
                                                                  Object[] extraParams) {
        Object[] parameters = new Object[extraParams.length + 1];
        parameters[0] = oldBlock.blockID;
        System.arraycopy(extraParams, 0, parameters, 1, extraParams.length);

        Class<?>[] parameterTypes = new Class[extraParams.length + 1];
        parameterTypes[0] = Integer.class;
        for (int i = 0; i < extraParams.length; i++) {
            parameterTypes[i + 1] = extraParams[i].getClass();
        }

        Block.blocksList[oldBlock.blockID] = null;

        try {
            Constructor<T> constructor = newBlockClazz.getDeclaredConstructor(parameterTypes);
            T newBlock = constructor.newInstance(parameters);
            CopyOldBlockToNew(oldBlock, newBlock);
            return newBlock;
        } catch (Exception e) {
            return null;
        }
    }

    public static void CopyOldBlockToNew(Block oldBlock, Block newBlock) {
        Block.blocksList[oldBlock.blockID] = newBlock;
        newBlock.setBlockName(oldBlock.getBlockName(0).substring(5));
        newBlock.atlasIndices = oldBlock.atlasIndices;
        newBlock.stepSound = oldBlock.stepSound;
    }
}
