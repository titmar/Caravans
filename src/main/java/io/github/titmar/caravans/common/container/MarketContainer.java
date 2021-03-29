package io.github.titmar.caravans.common.container;

import java.util.Objects;

import io.github.titmar.caravans.common.tile.MarketTileEntity;
import io.github.titmar.caravans.core.init.BlockInit;
import io.github.titmar.caravans.core.init.ContainerTypesInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;

public class MarketContainer extends Container {

	public final MarketTileEntity te;
	private final IWorldPosCallable canInteractWithCallable;

	public MarketContainer(final int windowId, final PlayerInventory playerInv, final MarketTileEntity te) {
		super(ContainerTypesInit.MARKET_CONTAINER_TYPE.get(), windowId);
		this.te = te;
		this.canInteractWithCallable = IWorldPosCallable.of(te.getWorld(), te.getPos());
	}

	public MarketContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data) {
		this(windowId, playerInv, getTileEntity(playerInv, data));
	}

	private static MarketTileEntity getTileEntity(final PlayerInventory playerInv, final PacketBuffer data) {
		Objects.requireNonNull(playerInv, "Player Inventory cannot be null");
		Objects.requireNonNull(data, "Data cannot be null");
		final TileEntity te = playerInv.player.world.getTileEntity(data.readBlockPos());
		if (te instanceof MarketTileEntity) {
			return (MarketTileEntity) te;
		}
		throw new IllegalStateException("Tile Entity is not correct!");
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return isWithinUsableDistance(canInteractWithCallable, playerIn, BlockInit.MARKET_BLOCK.get());
	}

}
