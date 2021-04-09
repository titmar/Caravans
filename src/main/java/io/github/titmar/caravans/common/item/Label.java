package io.github.titmar.caravans.common.item;

import io.github.titmar.caravans.Caravans;
import io.github.titmar.caravans.common.tile.MarketTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
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
			if (nbt.getCompound("marketPos") != null) {
				pos = NBTUtil.readBlockPos(nbt.getCompound("marketPos"));
				playerIn.sendStatusMessage(new StringTextComponent(
						"Holding market information x: " + pos.getX() + " y: " + pos.getY() + " z: " + pos.getZ()),
						true);
				return ActionResult.resultSuccess(stack);
			}
			if (nbt.getCompound("containerPos") != null) {
				pos = NBTUtil.readBlockPos(nbt.getCompound("containerPos"));
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
				switch (this.useOnMarket(stack, te)) {
				case 0:
					context.getPlayer().sendStatusMessage(new StringTextComponent("Max containers reached"), true);
					break;
				case 1:
					context.getPlayer().sendStatusMessage(new StringTextComponent("Successfully registered blocks"),
							true);
					stack.shrink(1);
					return ActionResultType.CONSUME;
				case 2:
					context.getPlayer().sendStatusMessage(new StringTextComponent("Container already registered"),
							true);
					break;
				case 3:
					context.getPlayer().sendStatusMessage(new StringTextComponent("Added information"), true);
					break;
				}
			}
			if (te instanceof ChestTileEntity || te instanceof BarrelTileEntity) {
				switch (this.useOnContainer(stack, te)) {
				case 0:
					context.getPlayer().sendStatusMessage(new StringTextComponent("Max containers reached"), true);
					break;
				case 1:
					context.getPlayer().sendStatusMessage(new StringTextComponent("Successfully registered blocks"),
							true);
					stack.shrink(1);
					return ActionResultType.CONSUME;
				case 2:
					context.getPlayer().sendStatusMessage(new StringTextComponent("Container already registered"),
							true);
					break;
				case 3:
					context.getPlayer().sendStatusMessage(new StringTextComponent("Added information"), true);
					break;
				case 4:
					context.getPlayer().sendStatusMessage(new StringTextComponent("Invalid Market Block"), true);
					break;
				}
			}
		}return ActionResultType.SUCCESS;

	}

	/**
	 * 
	 * @param stack     The stack used
	 * @param container the container to register
	 * @return 0 - list is full, 1 - position added, 2 - position already
	 *         registered, 3 - added information
	 */
	private int useOnMarket(ItemStack stack, TileEntity market) {
		CompoundNBT nbt = stack.getOrCreateTag();
		BlockPos marketPos = market.getPos();

		BlockPos containerPos = nbt.contains("containerPos") ? NBTUtil.readBlockPos(nbt.getCompound("containerPos")) : null;
		
		// If no container data is stored, store market
		if (containerPos == null) {
			nbt.put("marketPos", NBTUtil.writeBlockPos(marketPos));
			stack.setTag(nbt);
			return 3;
		} else {
			stack.setTag(new CompoundNBT());
			return ((MarketTileEntity) market).registerContainer(containerPos);
		}
	}

	/**
	 * 
	 * @param stack     The stack used
	 * @param container the container to register
	 * @return 0 - list is full, 1 - position added, 2 - position already
	 *         registered, 3 - added information
	 */
	private int useOnContainer(ItemStack stack, TileEntity container) {
		CompoundNBT nbt = stack.getOrCreateTag();
		BlockPos containerPos = container.getPos();

		BlockPos marketPos = nbt.contains("marketPos") ? NBTUtil.readBlockPos(nbt.getCompound("marketPos")) : null;

		// If no market data is stored, store container
		if (marketPos == null) {
			nbt.put("containerPos", NBTUtil.writeBlockPos(containerPos));
			stack.setTag(nbt);
			return 3;
		} else {
			stack.setTag(new CompoundNBT());
			TileEntity market = container.getWorld().getTileEntity(marketPos);
			return market != null && market instanceof MarketTileEntity
					? ((MarketTileEntity) market).registerContainer(containerPos)
					: 4;
		}
	}

}
