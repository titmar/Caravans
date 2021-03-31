package io.github.titmar.caravans.common.tile;

import java.util.ArrayList;

import io.github.titmar.caravans.Caravans;
import io.github.titmar.caravans.common.container.MarketContainer;
import io.github.titmar.caravans.core.init.TileEntityInit;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;

public class MarketTileEntity extends LockableLootTileEntity {

	private ArrayList<BlockPos> containers = new ArrayList<BlockPos>();
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
		return new TranslationTextComponent("container." + Caravans.MOD_ID + "market");
	}

	@Override
	protected Container createMenu(int id, PlayerInventory player) {
		return new MarketContainer(id, player, this);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		ListNBT nbt = new ListNBT();
		for(BlockPos pos : containers) {
			nbt.add(NBTUtil.writeBlockPos(pos));
		}
		compound.put("containerArray", nbt);
		return compound;
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		ArrayList<BlockPos> list = new ArrayList<BlockPos>();
		ListNBT nbt = compound.getList("containerArray", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i < nbt.size(); i++) {
			list.add(NBTUtil.readBlockPos(nbt.getCompound(i)));
		}
		this.containers = list;
	}

	/*
	 * Add a container
	 */
	public boolean registerContainer(BlockPos pos) {
		if (containers.size() >= this.maxContainers)
			return false;
		if (!containers.contains(pos)) {
			containers.add(pos);
			return true;
		}
		return false;
	}

	/*
	 * Returns the list of registered containers
	 */
	public ArrayList<BlockPos> getContainers() {
		this.validateContainers();
		return this.containers;
	}

	private void validateContainers() {
		this.containers.removeIf((pos) -> (this.world.getTileEntity(pos) == null));
	}
}
