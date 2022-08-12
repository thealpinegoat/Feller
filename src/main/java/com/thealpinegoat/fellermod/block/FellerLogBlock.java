package com.thealpinegoat.fellermod.block;

import com.thealpinegoat.fellermod.FellerMod;
import net.minecraft.src.*;

public class FellerLogBlock extends BlockLog {
    public FellerLogBlock(int i, String name, int[] atlasIndices, float hardness, StepSound stepSound, boolean setMossGrowable) {
        super(i);
        this.setBlockName(name);
        this.atlasIndices = atlasIndices;
        this.setHardness(hardness);
        this.setStepSound(stepSound);
        if (setMossGrowable) {
            this.setMossGrowable();
        }
    }

    private boolean isPartOfTree(World world, int x, int y, int z) {
        return isPartOfTree(world, x, y, z, 1);
    }

    private boolean isPartOfTree(World world, int x, int y, int z, int i) {
        if (i > FellerMod.maxTreeHeight) {
            return false;
        }
        int blockId = world.getBlockId(x, y + 1, z);
        if (Block.blocksList[blockId] instanceof BlockLeavesBase) {
            int leafCount = 0;
            int r = 2;
            for (int m = -r; m <= r; m++) {
                for (int n = -r; n <= r; n++) {
                    for (int o = -r; o <= r; o++) {
                        if ((m | n | o) == 0) {
                            continue;
                        }
                        blockId = world.getBlockId(x + m, y + n, z + o);
                        if (Block.blocksList[blockId] instanceof BlockLeavesBase) {
                            leafCount += 1;
                        }
                    }
                }
            }
            Block block = Block.blocksList[world.getBlockId(x, y, z)];
            if (block != null && leafCount >= FellerMod.idToLeafNumberMap.get(block)) {
                return true;
            }
            return false;
        }
        if (Block.blocksList[blockId] instanceof BlockLog) {
            return isPartOfTree(world, x, y + 1, z, i + 1);
        }
        return false;
    }

    private int breakTree(World world, int x, int y, int z) {
        if (world.isMultiplayerAndNotHost) {
            return 0;
        }
        return breakTree(world, x, y, z, 0);
    }

    private int breakTree(World world, int x, int y, int z, int n) {
        if (n > FellerMod.maxMaxTreeSize || world.isMultiplayerAndNotHost) {
            return n;
        }
        int blockId = world.getBlockId(x, y, z);
        Block block = Block.blocksList[blockId];
        world.setBlockWithNotify(x, y, z, 0);
        if (block != null) {
            block.dropBlockAsItem(world, x, y, z, 0);
        }
        n = n + 1;
        for (int i = -1; i <= 1; i++) {
            for (int j = 0; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    if ((i | j | k) == 0) {
                        continue;
                    }
                    blockId = world.getBlockId(x + i, y + j, z + k);
                    if (Block.blocksList[blockId] instanceof BlockLog) {
                        n = breakTree(world, x + i, y + j, z + k, n);
                    }
                }
            }
        }
        return n;
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int l) {
        super.onBlockDestroyedByPlayer(world, i, j, k, l);
        if (!world.isMultiplayerAndNotHost) {
            EntityPlayer entityPlayer = world.getClosestPlayer(i, j, k, 10);

            if (isPartOfTree(world, i, j, k)) {
                ItemStack heldItem = entityPlayer.getCurrentEquippedItem();
                if (heldItem != null) {
                    int heldItemId = heldItem.itemID;
                    if (Item.itemsList[heldItemId] instanceof ItemToolAxe) {
                        int blocksBroken = breakTree(world, i, j, k);
                        if (heldItem.getItem().isDamagable() && this.blockHardness > 0.0f) {
                            heldItem.damageItem(blocksBroken, null);
                        }
                    }
                }
            }
        }
    }
}
