package io.github.titmar.caravans.core.network.message;

import java.util.function.Supplier;

import io.github.titmar.caravans.common.container.MarketContainer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class ChangeActiveInventoryMessage {

	public BlockPos pos;

	public ChangeActiveInventoryMessage(BlockPos tile) {
		this.pos = tile;
	}

	public static void encode(ChangeActiveInventoryMessage message, PacketBuffer buffer) {
		buffer.writeBlockPos(message.pos);
	}

	public static ChangeActiveInventoryMessage decode(PacketBuffer buffer) {
		return new ChangeActiveInventoryMessage(buffer.readBlockPos());
	}

	public static void handle(ChangeActiveInventoryMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			ServerPlayerEntity player = context.getSender();
			if(player.openContainer instanceof MarketContainer) {
				
				((MarketContainer)player.openContainer).setActiveInventory(message.pos);
				player.sendContainerToPlayer(player.openContainer);
			}
		});
		context.setPacketHandled(true);
	}
}
