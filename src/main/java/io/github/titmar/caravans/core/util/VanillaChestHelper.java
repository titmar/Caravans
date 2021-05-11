package io.github.titmar.caravans.core.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class VanillaChestHelper {

	/*
	 * Checks whether the given position is a double chest and if so returns the
	 * coordinates of the second part
	 */
	public static BlockPos isGetDoubleChest(World world, BlockPos pos) {
		BlockState chest = world.getBlockState(pos);
		if (!(chest.getBlock() instanceof ChestBlock))
			return null;
		IItemHandler handler = world.getTileEntity(pos).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
				.orElse(null);
		if (handler == null)
			return null;
		if (handler.getSlots() != 54) {
			return null;
		}
		Vector3f dir = ChestBlock.getDirectionToAttached(chest).toVector3f();

		return VanillaChestHelper.addDirectionToPos(dir, pos);
	}

	private static BlockPos addDirectionToPos(Vector3f dir, BlockPos pos) {
		int x = (int) (pos.getX() + dir.getX());
		int y = (int) (pos.getY() + dir.getY());
		int z = (int) (pos.getZ() + dir.getZ());

		return new BlockPos(x, y, z);
	}
}
