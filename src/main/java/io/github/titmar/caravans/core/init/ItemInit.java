package io.github.titmar.caravans.core.init;

import io.github.titmar.caravans.Caravans;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Caravans.MOD_ID);

	public static void init() {
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	// BlockItems
	public static final RegistryObject<BlockItem> MARKET_BLOCK = ITEMS.register("market_block",
			() -> new BlockItem(BlockInit.MARKET_BLOCK.get(), new Item.Properties().group(Caravans.TAB)));

}
