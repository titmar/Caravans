package io.github.titmar.caravans.common.tile;

import java.util.HashSet;

import io.github.titmar.caravans.Caravans;
import io.github.titmar.caravans.common.container.MarketContainer;
import io.github.titmar.caravans.core.init.TileEntityInit;
import io.github.titmar.caravans.core.network.CaravansNetwork;
import io.github.titmar.caravans.core.network.message.UpdateMarketMessage;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.BarrelTileEntity;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;

public class MarketTileEntity extends LockableLootTileEntity {

	private HashSet<BlockPos> containers = new HashSet<BlockPos>();
	private int maxContainers;

	public MarketTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
		this.maxContainers = 10;
	}

	public MarketTileEntity() {
		this(TileEntityInit.MARKET_TILE_ENTITY_TYPE.get());
	}

	@Override
	public int getSizeInventory() {
		return 0;
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
	}

	@Override
	protected void setItems(NonNullList<ItemStack> itemsIn) {

	}

	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("container." + Caravans.MOD_ID + ".market");
	}

	@Override
	protected Container createMenu(int id, PlayerInventory player) {
		return new MarketContainer(id, player, this);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		ListNBT nbt = new ListNBT();
		for (BlockPos pos : containers) {
			nbt.add(NBTUtil.writeBlockPos(pos));
		}
		compound.put("containerArray", nbt);
		return compound;
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		HashSet<BlockPos> list = new HashSet<BlockPos>();
		ListNBT nbt = compound.getList("containerArray", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < nbt.size(); i++) {
			list.add(NBTUtil.readBlockPos(nbt.getCompound(i)));
		}
		this.containers = list;
	}
	
	@Override
	public CompoundNBT getUpdateTag() {
		return this.write(super.getUpdateTag());
	}

	/**
	 * 
	 * @param pos The position of the container to register
	 * @return 0 - list is full, 1 - position added, 2 - position already registered
	 */
	public int registerContainer(BlockPos pos) {
		this.validateContainers();
		if (containers.size() == this.maxContainers) {
			return 0;
		}
		if (containers.add(pos)) {
			this.world.notifyBlockUpdate(this.pos, getBlockState(), getBlockState(), 2);
			this.markDirty();
			return 1;
		}
		return 2;
	}

	/*
	 * Returns the list of registered containers after validating them
	 */
	public HashSet<BlockPos> getContainers() {
		this.validateContainers();
		return this.containers;
	}

	/*
	 * checks if the list of containers is up-to-date and syncs it
	 */
	public void validateContainers() {
		if(world.isRemote) {
			//sync server with client when opening gui
			CaravansNetwork.CHANNEL.sendToServer(new UpdateMarketMessage(this.getUpdateTag(), pos));
		}
		if (this.containers.removeIf((pos) -> (this.world.getTileEntity(pos) == null)
				&& !(this.world.getTileEntity(pos) instanceof ChestTileEntity)
				&& !(this.world.getTileEntity(pos) instanceof BarrelTileEntity))) {
			this.markDirty();
			this.world.notifyBlockUpdate(this.pos, getBlockState(), getBlockState(), 2);
		}
		
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		nbt = this.write(nbt);
		return new SUpdateTileEntityPacket(this.getPos(), -1, nbt);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.read(world.getBlockState(pkt.getPos()), pkt.getNbtCompound());
	}	
}
