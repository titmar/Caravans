package io.github.titmar.caravans.common.item;

import io.github.titmar.caravans.Caravans;
import io.github.titmar.caravans.common.tile.MarketTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.BarrelTileEntity;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class Label extends Item {

	public Label() {
		super(new Item.Properties().group(Caravans.TAB));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		if (stack.hasTag()) {
			CompoundNBT nbt = stack.getTag();
			BlockPos pos;
			if (nbt.getBoolean("marketInit")) {
				pos = new BlockPos(nbt.getInt("marketX"), nbt.getInt("marketY"), nbt.getInt("marketZ"));
				playerIn.sendStatusMessage(new StringTextComponent(
						"Holding market information x: " + pos.getX() + " y: " + pos.getY() + " z: " + pos.getZ()),
						true);
				return ActionResult.resultSuccess(stack);
			}
			if (nbt.getBoolean("containerInit")) {
				pos = new BlockPos(nbt.getInt("containerX"), nbt.getInt("containerY"), nbt.getInt("containerZ"));
				playerIn.sendStatusMessage(new StringTextComponent(
						"Holding container information x: " + pos.getX() + " y: " + pos.getY() + " z: " + pos.getZ()),
						true);
				return ActionResult.resultSuccess(stack);
			}

		}
		playerIn.sendStatusMessage(new StringTextComponent("No information gathered yet"), true);
		return ActionResult.resultSuccess(stack);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		if (!world.isRemote) {
			BlockPos pos = context.getPos();
			TileEntity te = world.getTileEntity(pos);
			ItemStack stack = context.getPlayer().getHeldItem(context.getHand());

			if (te instanceof MarketTileEntity) {
				int use = this.useOnMarket(stack, te);
				if (use == 1) {
					context.getPlayer().sendStatusMessage(new StringTextComponent(
							"Registered Market Block at x: " + pos.getX() + " y: " + pos.getY() + " z: " + pos.getZ()),
							true);
					stack.shrink(1);
					return ActionResultType.CONSUME;
				}
				if (use == 2) {
					context.getPlayer().sendStatusMessage(new StringTextComponent("Max containers reached ore container already registered"), true);
				}

			}
			if (te instanceof ChestTileEntity || te instanceof BarrelTileEntity) {
				int use = this.useOnContainer(stack, te);
				if (use == 1) {
					context.getPlayer().sendStatusMessage(new StringTextComponent("Registered Container Block at x: "
							+ pos.getX() + " y: " + pos.getY() + " z: " + pos.getZ()), true);
					stack.shrink(1);
					return ActionResultType.CONSUME;
				}
				if (use == 2) {
					context.getPlayer().sendStatusMessage(new StringTextComponent("Max containers reached or container already registered"), true);
				}
			}

		}

		return ActionResultType.SUCCESS;
	}

	private int useOnMarket(ItemStack stack, TileEntity tile) {
		CompoundNBT nbt = stack.getTag() != null ? stack.getTag() : new CompoundNBT();
		BlockPos pos = tile.getPos();

		// If no container data is stored, store market
		if (!nbt.getBoolean("containerInit")) {
			nbt.putInt("marketX", pos.getX());
			nbt.putInt("marketY", pos.getY());
			nbt.putInt("marketZ", pos.getZ());
			nbt.putBoolean("marketInit", true);
			stack.setTag(nbt);
			return 0;
		} else {
			pos = new BlockPos(nbt.getInt("containerX"), nbt.getInt("containerY"), nbt.getInt("containerZ"));
			nbt.putBoolean("marketInit", false);
			nbt.putBoolean("containerInit", false);
			stack.setTag(nbt);
			return ((MarketTileEntity) tile).registerContainer(pos) ? 1 : 2;
		}

	}

	/*
	 * return 0 - information added return 1 - registered block successfully return
	 * 2 - block could not be registered
	 */
	private int useOnContainer(ItemStack stack, TileEntity tile) {
		CompoundNBT nbt = stack.getTag() != null ? stack.getTag() : new CompoundNBT();
		BlockPos pos = tile.getPos();

		// If no market data is stored, store container
		if (!nbt.getBoolean("marketInit")) {
			nbt.putInt("containerX", pos.getX());
			nbt.putInt("containerY", pos.getY());
			nbt.putInt("containerZ", pos.getZ());
			nbt.putBoolean("containerInit", true);
			stack.setTag(nbt);
			return 0;
		} else {
			pos = new BlockPos(nbt.getInt("marketX"), nbt.getInt("marketY"), nbt.getInt("marketZ"));
			nbt.putBoolean("marketInit", false);
			nbt.putBoolean("containerInit", false);
			stack.setTag(nbt);
			return ((MarketTileEntity) tile.getWorld().getTileEntity(pos)).registerContainer(tile.getPos()) ? 1 : 2;
		}
	}

}
