package de.nxll.pitiful.mixin;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiMainMenu.class)
public class MainMenuMixin extends GuiScreen {

    @Inject(method = "initGui", at = @At("RETURN"))
    private void pitiful_initGui(CallbackInfo ci) {
        // Mods button -> Replace with Hacks button
        for (GuiButton guiButton : buttonList) {
            if (guiButton.displayString.equals("Mods")) {
                guiButton.displayString = "Hacks";
            }
        }
    }

}
