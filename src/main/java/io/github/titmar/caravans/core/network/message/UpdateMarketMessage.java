package io.github.titmar.caravans.core.network.message;

import java.util.function.Supplier;

import io.github.titmar.caravans.common.tile.MarketTileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class UpdateMarketMessage {

	public CompoundNBT nbt;
	public BlockPos position;

	public UpdateMarketMessage(CompoundNBT nbt, BlockPos pos) {
		this.nbt = nbt;
		this.position = pos;
	}

	public static void encode(UpdateMarketMessage message, PacketBuffer buffer) {
		buffer.writeCompoundTag(message.nbt);
		buffer.writeBlockPos(message.position);
	}

	public static UpdateMarketMessage decode(PacketBuffer buffer) {
		return new UpdateMarketMessage(buffer.readCompoundTag(), buffer.readBlockPos());
	}

	public static void handle(UpdateMarketMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			World world = context.getSender().getEntityWorld();
			TileEntity te = world.getTileEntity(message.position);
			if(te instanceof MarketTileEntity) {
				((MarketTileEntity)te).validateContainers();
			}
		});
		context.setPacketHandled(true);
	}

}
