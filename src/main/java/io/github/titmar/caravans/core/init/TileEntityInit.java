package io.github.titmar.caravans.core.init;

import io.github.titmar.caravans.Caravans;
import io.github.titmar.caravans.common.tile.MarketTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityInit {

	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister
			.create(ForgeRegistries.TILE_ENTITIES, Caravans.MOD_ID);

	public static void init() {
		TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	public static final RegistryObject<TileEntityType<MarketTileEntity>> MARKET_TILE_ENTITY_TYPE = TILE_ENTITIES
			.register("market", () -> TileEntityType.Builder.create(MarketTileEntity::new, BlockInit.MARKET_BLOCK.get())
					.build(null));
}
