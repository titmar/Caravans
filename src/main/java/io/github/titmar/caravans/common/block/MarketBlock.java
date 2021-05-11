package io.github.titmar.caravans.common.block;

import io.github.titmar.caravans.common.tile.MarketTileEntity;
import io.github.titmar.caravans.core.init.TileEntityInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

public class MarketBlock extends Block {

	public MarketBlock() {
		super(Properties.create(Material.WOOD).hardnessAndResistance(2.0f, 1.0f).harvestTool(ToolType.AXE)
				.setRequiresTool().sound(SoundType.WOOD));

	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityInit.MARKET_TILE_ENTITY_TYPE.get().create();
	}

	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		if (worldIn.isRemote) {
			return ActionResultType.SUCCESS;
		} else {
			TileEntity te = worldIn.getTileEntity(pos);
			if (te instanceof MarketTileEntity) {
				NetworkHooks.openGui((ServerPlayerEntity) player, (MarketTileEntity) te, pos);
			}
		}
		return ActionResultType.CONSUME;
	}

	
}
