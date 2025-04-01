package co.skyclient.scc.mixins;

import co.skyclient.scc.listeners.GuiListeners;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public class GuiContainerMixin {

    @Unique
    private final GuiContainer scc$gui = (GuiContainer) (Object) this;

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void scc$mouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        GuiListeners.handleAprilFoolsMouse(scc$gui, mouseX, mouseY, mouseButton);
    }
}
