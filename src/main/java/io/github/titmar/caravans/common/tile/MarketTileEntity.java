package io.github.titmar.caravans.common.tile;

import io.github.titmar.caravans.Caravans;
import io.github.titmar.caravans.common.container.MarketContainer;
import io.github.titmar.caravans.core.init.TileEntityInit;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class MarketTileEntity extends LockableLootTileEntity {

	public MarketTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
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
		if (!this.checkLootAndRead(compound)) {
			// TODO: save data
		}
		return compound;
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
		if (this.checkLootAndRead(nbt)) {
			// TODO: load data
		}
	}
}
