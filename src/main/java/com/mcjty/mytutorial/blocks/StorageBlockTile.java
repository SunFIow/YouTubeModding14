package com.mcjty.mytutorial.blocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mcjty.mytutorial.setup.Config;
import com.mcjty.mytutorial.setup.Registration;
import com.mcjty.mytutorial.tools.CustomEnergyStorage;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class StorageBlockTile extends TileEntity {

	private CustomEnergyStorage energyStorage = createEnergy();

	// Never create lazy optionals in getCapability. Always place them as fields in the tile entity:
	private LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);

	public StorageBlockTile() {
		super(Registration.STORAGEBLOCK_TILE.get());
	}

	@Override
	public void read(CompoundNBT tag) {
		energyStorage.deserializeNBT(tag.getCompound("energy"));
		super.read(tag);
	}

	@Override
	public CompoundNBT write(CompoundNBT tag) {
		tag.put("energy", energyStorage.serializeNBT());
		return super.write(tag);
	}

	private CustomEnergyStorage createEnergy() {
		return new CustomEnergyStorage(Config.FIRSTBLOCK_MAXPOWER.get(), 100) {
			@Override
			protected void onEnergyChanged() {
				markDirty();
			}
		};
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap.equals(CapabilityEnergy.ENERGY)) {
			return energy.cast();
		}
		return super.getCapability(cap, side);
	}
}
