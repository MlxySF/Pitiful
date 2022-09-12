package de.nxll.pitiful.features

import de.nxll.pitiful.Pitiful
import de.nxll.pitiful.events.BlockUpdateEvent
import de.nxll.pitiful.utils.BlockUtils
import de.nxll.pitiful.utils.RenderUtils
import de.nxll.pitiful.utils.Utils
import net.minecraft.client.Minecraft
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.init.Blocks
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.awt.Color

class SewerEsp {

    private val isEnabled: Boolean get() = Pitiful.config.sewerEspEnabled
    private val maxDelay: Int get() = Pitiful.config.sewerEspMaxDelay
    private val color: Color get() = Pitiful.config.sewerEspColor

    private var lastAction = -9999L
    private var possibleTreasurePos: BlockPos? = null
    private var treasurePos: BlockPos? = null

    @SubscribeEvent // Check if a chest appeared
    fun onBlockUpdate(event: BlockUpdateEvent) {
        if (!Utils.isInPit) return

        val mc = Minecraft.getMinecraft()

        // Air -> Chest
        if (BlockUtils.isStateAir(event.oldState) && BlockUtils.isStateBlock(event.newState, Blocks.chest)) {
            if (mc.theWorld != null) {
                if (mc.theWorld.totalWorldTime - lastAction <= maxDelay) {
                    treasurePos = event.pos
                    possibleTreasurePos = null
                    lastAction = -9999L

                    val p = mc.thePlayer
                    mc.soundHandler.playSound(
                        PositionedSoundRecord(
                            ResourceLocation("minecraft:note.harp"),
                            1f,
                            2f,
                            p.posX.toFloat(),
                            p.posY.toFloat(),
                            p.posZ.toFloat()
                        )
                    )
                } else {
                    possibleTreasurePos = event.pos
                    lastAction = mc.theWorld.totalWorldTime
                }
            }
        }
        // Chest -> Air
        else if (BlockUtils.isStateBlock(event.oldState, Blocks.chest) && BlockUtils.isStateAir(event.newState)) {
            if (event.pos == treasurePos) {
                treasurePos = null
            }
        }
    }

    @SubscribeEvent // Check for sewer chat messages
    fun onChatRecieved(event: ClientChatReceivedEvent) {
        if (!Utils.isInPit) return

        val mc = Minecraft.getMinecraft()

        if (event.message.unformattedText == "SEWERS! A new treasure spawned somewhere!") {
            if (mc.theWorld != null) {
                if (mc.theWorld.totalWorldTime - lastAction <= maxDelay) {
                    treasurePos = possibleTreasurePos
                    possibleTreasurePos = null
                    lastAction = -9999
                } else {
                    lastAction = mc.theWorld.totalWorldTime
                }
            }
        }
    }

    @SubscribeEvent // Check if out of sewers
    fun onTick(event: TickEvent) {
        if (!Utils.isInPit) {
            lastAction = -9999
            possibleTreasurePos = null
            treasurePos = null
            return
        }

        val mc = Minecraft.getMinecraft()

        if (mc.theWorld != null && mc.thePlayer != null) {
            if (mc.thePlayer.posY > 63) {
                treasurePos = null
            }
        }
    }

    @SubscribeEvent
    fun onRenderWorldLast(event: RenderWorldLastEvent) {
        if (!Utils.isInPit || !isEnabled || treasurePos == null) return

        val viewer = Minecraft.getMinecraft().renderViewEntity
        val viewerX: Double = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * event.partialTicks
        val viewerY: Double = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * event.partialTicks
        val viewerZ: Double = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * event.partialTicks

        val x: Double = treasurePos!!.x - viewerX
        val y: Double = treasurePos!!.y - viewerY
        val z: Double = treasurePos!!.z - viewerZ

        GlStateManager.disableDepth() // Show through walls
        RenderUtils.drawFilledBoundingBox(
            AxisAlignedBB(x - 0.01, y - 0.01, z - 0.01, x + 1.01, y + 1.01, z + 1.01),
            color,
            color.alpha / 255f
        )
        GlStateManager.enableDepth()

    }

}