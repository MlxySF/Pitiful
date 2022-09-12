package de.nxll.pitiful.utils

import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks

object BlockUtils {
    fun isStateBlock(state: IBlockState, block: Block): Boolean {
        return state.block.defaultState == block.defaultState
    }

    fun isStateAir(state: IBlockState): Boolean {
        return isStateBlock(state, Blocks.air)
    }
}