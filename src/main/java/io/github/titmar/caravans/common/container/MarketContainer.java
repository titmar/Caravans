package io.github.titmar.caravans.common.container;

import java.util.Objects;

import io.github.titmar.caravans.common.tile.MarketTileEntity;
import io.github.titmar.caravans.core.init.BlockInit;
import io.github.titmar.caravans.core.init.ContainerTypesInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class MarketContainer extends Container {

	public final MarketTileEntity te;
	private final IWorldPosCallable canInteractWithCallable;
	private BlockPos activeContainer;

	public MarketContainer(final int windowId, final PlayerInventory playerInv, final MarketTileEntity te) {
		super(ContainerTypesInit.MARKET_CONTAINER_TYPE.get(), windowId);
		this.te = te;
		this.canInteractWithCallable = IWorldPosCallable.of(te.getWorld(), te.getPos());
		if (te.getContainers().iterator().hasNext()) {
			BlockPos pos = te.getContainers().iterator().next();
			this.setActiveInventory(pos);
			this.activeContainer = pos;
		}

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

	
	//TODO: Fix bug when instantiated with single chest and switching back and forth
	public void setActiveInventory(BlockPos pos) {
		TileEntity t = this.te.getWorld().getTileEntity(pos);
		if (t == null || !(t instanceof LockableLootTileEntity) || pos.equals(this.activeContainer)) {
			return;
		}
		int startX = 108;
		int startY = 34;
		int row = 0;
		int col = 0;
		LockableLootTileEntity tile = (LockableLootTileEntity) this.te.getWorld().getTileEntity(pos);
		IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
		if (handler != null) {
			this.inventorySlots.clear();
			detectAndSendChanges();
			for (int i = 0; i < handler.getSlots(); i++) {
				this.addSlot(new LockedSlot(handler, i, startX + (col * 18), startY + (row * 18)));
				col++;
				if (col == 9) {
					col = 0;
					row++;
				}
			}
			this.activeContainer = pos;
		}
		
	}

	private class LockedSlot extends SlotItemHandler {

		public LockedSlot(IItemHandler handler, int index, int xPosition, int yPosition) {
			super(handler, index, xPosition, yPosition);

		}

		@Override
		public boolean canTakeStack(PlayerEntity playerIn) {
			return false;
		}

	}

}
