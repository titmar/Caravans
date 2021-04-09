package io.github.titmar.caravans.core.network;

import io.github.titmar.caravans.Caravans;
import io.github.titmar.caravans.core.network.message.UpdateMarketMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class CaravansNetwork {

	public static final String NETWORK_VERSION = "0.1.0";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(Caravans.MOD_ID, "network"), () -> NETWORK_VERSION,
			version -> version.equals(NETWORK_VERSION), version -> version.equals(NETWORK_VERSION));
	
	
	public static void init() {
		CHANNEL.registerMessage(0, UpdateMarketMessage.class, UpdateMarketMessage::encode, UpdateMarketMessage::decode, UpdateMarketMessage::handle);
	}

}
