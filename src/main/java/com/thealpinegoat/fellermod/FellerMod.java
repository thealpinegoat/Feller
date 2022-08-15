package com.thealpinegoat.fellermod;

import bta.Mod;
import com.thealpinegoat.fellermod.block.FellerLogBlock;
import com.thealpinegoat.fellermod.utils.ModBlockUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FellerMod implements Mod {
    public static final String MOD_ID = "feller";
    public static final int maxTreeHeight = 25;
    public static final int maxMaxTreeSize = 128;
    public static final Map<Integer, Integer> idToLeafNumberMap = new HashMap<>();
    public static final Map<Integer, Block> modBlocksMap = new HashMap<>();
    public final static Logger log = Logger.getLogger(MOD_ID);

    private static void OverrideLogBlocks() {
        String[] logNames = new String[]{"oak", "pine", "birch", "cherry", "oak.mossy"};
        String blockName;
        Block oldBlock, newBlock;
        for (String logName : logNames) {
            blockName = String.format("tile.log.%s", logName);
            if (!Block.nameToIdMap.containsKey(blockName)) {
                log.warning(String.format("Name -> ID map does not contain a value for %s, skipping.", blockName));
                continue;
            }
            oldBlock = Block.blocksList[Block.nameToIdMap.get(blockName)];
            newBlock = ModBlockUtils.OverrideBlock(oldBlock, FellerLogBlock.class);
            if (newBlock == null) {
                log.severe(String.format("Failed to override block: " + "'%s'!", blockName));
            }
            modBlocksMap.put(oldBlock.blockID, newBlock);
        }
    }

    private static void initTreeLeafMap() {
        idToLeafNumberMap.put(Block.logOak.blockID, 40);
        idToLeafNumberMap.put(Block.logPine.blockID, 8);
        idToLeafNumberMap.put(Block.logBirch.blockID, 40);
        idToLeafNumberMap.put(Block.logCherry.blockID, 20);
        idToLeafNumberMap.put(Block.logEucalyptus.blockID, 8);
        idToLeafNumberMap.put(Block.logOakMossy.blockID, 25);
    }

    @Override
    public void init(Minecraft minecraft) {
        OverrideLogBlocks();
        initTreeLeafMap();
        log.log(Level.INFO, "Initialised.");
    }

    @Override
    public void update() {

    }

    @Override
    public void tick() {

    }
}
