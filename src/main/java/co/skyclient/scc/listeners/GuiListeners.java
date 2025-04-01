/*
 * SkyclientCosmetics - Cool cosmetics for a mod installer Skyclient!
 * Copyright (C) koxx12-dev [2021 - 2021]
 *
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found under this url
 * https://github.com/koxx12-dev/Skyclient-Cosmetics
 *
 * If you have a private concern, please contact me on
 * Discord: Koxx12#8061
 */

package co.skyclient.scc.listeners;

import cc.polyfrost.oneconfig.libs.universal.UDesktop;
import cc.polyfrost.oneconfig.renderer.NanoVGHelper;
import co.skyclient.scc.SkyclientCosmetics;
import co.skyclient.scc.config.Settings;
import co.skyclient.scc.gui.SkyClientMainMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.net.URI;

public class GuiListeners {

    @SubscribeEvent
    public void onGuiInitPost(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.gui instanceof GuiIngameMenu) {
            int x = event.gui.width - 132;
            int y = 3;
            event.buttonList.add(new GuiButton(2666487, x, y, 130, 20, "SkyClient Cosmetics"));
        }
    }

    @SubscribeEvent
    public void onGuiAction(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (event.gui instanceof GuiIngameMenu && event.button.id == 2666487) {
            SkyclientCosmetics.config.openGui();
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && SkyclientCosmetics.config.enabled && Settings.customMainMenu) {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu && !(Minecraft.getMinecraft().currentScreen instanceof SkyClientMainMenu)) {
                Minecraft.getMinecraft().displayGuiScreen(new SkyClientMainMenu());
            }
        }
    }

    @SubscribeEvent
    public void onGuiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (event.gui instanceof GuiContainer) {
            handleAprilFoolsDraw(event.gui);
        }
    }

    public static void handleAprilFoolsDraw(GuiScreen gui) {
        if (SkyclientCosmetics.aprilTrollUntil == 0 || System.currentTimeMillis() > SkyclientCosmetics.aprilTrollUntil) return;
        NanoVGHelper.INSTANCE.setupAndDraw(true, (vg) -> NanoVGHelper.INSTANCE.drawImage(vg, "/SKYCLIENTCOSMETICS AD.png", gui.width - (821 * 3f / 4f / 4f), 0, 821 * 3f / 4f / 4f, 1080 * 3f / 4f / 4f, GuiListeners.class));
    }

    public static void handleAprilFoolsMouse(GuiScreen gui, int mouseX, int mouseY, int mouseButton) {
        if (SkyclientCosmetics.aprilTrollUntil == 0 || System.currentTimeMillis() > SkyclientCosmetics.aprilTrollUntil) return;
        if (mouseButton == 0 &&
                mouseX >= gui.width - (821 * 3f / 4f / 4f) &&
                mouseX <= gui.width
                && mouseY >= 0 &&
                mouseY <= (1080 * 3f / 4f / 4f)) {
            UDesktop.browse(URI.create("https://www.patreon.com/Polyfrost/membership"));
        }
    }
}