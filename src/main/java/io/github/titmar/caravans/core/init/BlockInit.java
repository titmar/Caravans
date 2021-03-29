package io.github.titmar.caravans.core.init;

import io.github.titmar.caravans.Caravans;
import io.github.titmar.caravans.common.block.MarketBlock;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			Caravans.MOD_ID);

	public static void init() {
		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	public static final RegistryObject<MarketBlock> MARKET_BLOCK = BLOCKS.register("market_block", MarketBlock::new);

}
