package io.github.titmar.caravans.datagen.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CapabilitySerializer implements IStorage<ILabelData> {

	@Override
	public INBT writeNBT(Capability<ILabelData> capability, ILabelData instance, Direction side) {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt("marketX", instance.getMarket().getX());
		nbt.putInt("marketY", instance.getMarket().getY());
		nbt.putInt("marketZ", instance.getMarket().getZ());
		
		nbt.putInt("containerX", instance.getContainer().getX());
		nbt.putInt("containerY", instance.getContainer().getY());
		nbt.putInt("containerZ", instance.getContainer().getZ());
		return nbt;
	}

	@Override
	public void readNBT(Capability<ILabelData> capability, ILabelData instance, Direction side, INBT nbt) {
		int x = ((CompoundNBT) nbt).getInt("marketX");
		int y = ((CompoundNBT) nbt).getInt("marketY");
		int z = ((CompoundNBT) nbt).getInt("marketZ");
		instance.setMarket(new BlockPos(x, y, z));
		
		x = ((CompoundNBT) nbt).getInt("containerX");
		y = ((CompoundNBT) nbt).getInt("containerY");
		z = ((CompoundNBT) nbt).getInt("containerZ");
		instance.setContainer(new BlockPos(x, y, z));
		
	}

}
