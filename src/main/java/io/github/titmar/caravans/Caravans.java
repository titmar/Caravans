package io.github.titmar.caravans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.titmar.caravans.core.init.BlockInit;
import io.github.titmar.caravans.core.init.ContainerTypesInit;
import io.github.titmar.caravans.core.init.ItemInit;
import io.github.titmar.caravans.core.init.TileEntityInit;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("caravans")
public class Caravans {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "caravans";
	public static final ItemGroup TAB = new ItemGroup("caravansTab") {

		@Override
		public ItemStack createIcon() {
			return new ItemStack(Blocks.DIRT);
		}
	};

	public Caravans() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		MinecraftForge.EVENT_BUS.register(this);

		BlockInit.init();
		ItemInit.init();
		TileEntityInit.init();
		ContainerTypesInit.init();
	}

	private void setup(final FMLCommonSetupEvent event) {

	}
}
