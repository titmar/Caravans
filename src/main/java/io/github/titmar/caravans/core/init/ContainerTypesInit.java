package io.github.titmar.caravans.core.init;

import io.github.titmar.caravans.Caravans;
import io.github.titmar.caravans.common.container.MarketContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerTypesInit {

	public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister
			.create(ForgeRegistries.CONTAINERS, Caravans.MOD_ID);

	public static void init() {
		CONTAINER_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	public static final RegistryObject<ContainerType<MarketContainer>> MARKET_CONTAINER_TYPE = CONTAINER_TYPES
			.register("market", () -> IForgeContainerType.create(MarketContainer::new));
}
