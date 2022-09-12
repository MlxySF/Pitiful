package de.nxll.pitiful.events

import net.minecraft.block.state.IBlockState
import net.minecraft.util.BlockPos
import net.minecraftforge.fml.common.eventhandler.Event

class BlockUpdateEvent(
    val oldState: IBlockState,
    val newState: IBlockState,
    val pos: BlockPos
) : Event()