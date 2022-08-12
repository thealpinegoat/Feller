package com.thealpinegoat.fellermod;

import bta.Mod;
import com.thealpinegoat.fellermod.block.FellerLogBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FellerMod implements Mod {
    public static final String MOD_ID = "feller";
    private final static Logger log = Logger.getLogger(MOD_ID);

    public static Map<Block, Integer> idToLeafNumberMap;
    // Maximum height of a tree (in blocks up from starting point)
    public static int maxTreeHeight;
    public static int maxMaxTreeSize;
    private static Minecraft _minecraft;
    private static FellerLogBlock fellLogOak;
    private static FellerLogBlock fellLogPine;
    private static FellerLogBlock fellLogBirch;
    private static FellerLogBlock fellLogCherry;
    private static FellerLogBlock fellLogEucalyptus;
    private static FellerLogBlock fellLogOakMossy;

    public static Minecraft get_minecraft() {
        return _minecraft;
    }

    private static FellerLogBlock overrideLogBlock(Block block) {
        Block.blocksList[block.blockID] = null; // Remove old block reference.
        FellerLogBlock fellerLogBlock = new FellerLogBlock(block.blockID, block.getBlockName(0).substring(5), block.atlasIndices, block.getHardness(), block.stepSound, block.mossGrowable);
        Block.blocksList[fellerLogBlock.blockID] = fellerLogBlock;
        return fellerLogBlock;
    }

    private static void overrideLogBlocks() {
        log.info("Overriding existing log blocks.");
        fellLogOak = overrideLogBlock(Block.logOak);
        fellLogPine = overrideLogBlock(Block.logPine);
        fellLogBirch = overrideLogBlock(Block.logBirch);
        fellLogCherry = overrideLogBlock(Block.logCherry);
        fellLogEucalyptus = overrideLogBlock(Block.logEucalyptus);
        fellLogOakMossy = overrideLogBlock(Block.logOakMossy);
        log.info("Override complete.");
    }

    private static void initTreeLeafMap() {
        log.info("Initialising tree data.");
        idToLeafNumberMap = new HashMap<Block, Integer>();
        idToLeafNumberMap.put(fellLogOak, 40);
        idToLeafNumberMap.put(fellLogPine, 8);
        idToLeafNumberMap.put(fellLogBirch, 40);
        idToLeafNumberMap.put(fellLogCherry, 20);
        idToLeafNumberMap.put(fellLogEucalyptus, 8);
        idToLeafNumberMap.put(fellLogOakMossy, 25);
        log.info("Initialised tree data.");
    }

    @Override
    public void init(Minecraft minecraft) {
        _minecraft = minecraft;
        maxTreeHeight = 25;
        maxMaxTreeSize = 150;

        overrideLogBlocks();
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
