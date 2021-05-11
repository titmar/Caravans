package io.github.titmar.caravans.core.network.message;

import java.util.function.Supplier;

import io.github.titmar.caravans.common.tile.MarketTileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class UpdateMarketContainerListMessage {

	public BlockPos position;

	public UpdateMarketContainerListMessage(BlockPos pos) {
		this.position = pos;
	}

	public static void encode(UpdateMarketContainerListMessage message, PacketBuffer buffer) {
		buffer.writeBlockPos(message.position);
	}

	public static UpdateMarketContainerListMessage decode(PacketBuffer buffer) {
		return new UpdateMarketContainerListMessage(buffer.readBlockPos());
	}

	public static void handle(UpdateMarketContainerListMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			World world = context.getSender().getEntityWorld();
			TileEntity te = world.getTileEntity(message.position);
			if (te instanceof MarketTileEntity) {
				((MarketTileEntity) te).validateContainers();
			}
		});
		context.setPacketHandled(true);
	}

}
