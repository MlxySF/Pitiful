package de.nxll.pitiful.mixin;

import de.nxll.pitiful.events.BlockUpdateEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Chunk.class)
public abstract class ChunkMixin {

    @Shadow
    public abstract IBlockState getBlockState(BlockPos pos);

    private IBlockState previousState = null;

    @Inject(method = "setBlockState", at = @At("HEAD"))
    public void pitiful_setBlockState_HEAD(BlockPos pos, IBlockState state, CallbackInfoReturnable<IBlockState> cir) {
        previousState = getBlockState(pos);
    }

    @Inject(method = "setBlockState", at = @At("RETURN"))
    public void pitiful_setBlockState_RETURN(BlockPos pos, IBlockState state, CallbackInfoReturnable<IBlockState> cir) {
        if (previousState != null) {
            MinecraftForge.EVENT_BUS.post(new BlockUpdateEvent(previousState, state, pos));
            previousState = null;
        }
    }

}
