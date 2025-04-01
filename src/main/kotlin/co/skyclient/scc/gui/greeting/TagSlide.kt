package co.skyclient.scc.gui.greeting

import cc.polyfrost.oneconfig.libs.elementa.components.UIImage
import co.skyclient.scc.gui.greeting.components.GreetingSlide
import cc.polyfrost.oneconfig.libs.elementa.components.UIWrappedText
import cc.polyfrost.oneconfig.libs.elementa.constraints.CenterConstraint
import cc.polyfrost.oneconfig.libs.elementa.constraints.SiblingConstraint
import cc.polyfrost.oneconfig.libs.elementa.dsl.*
import cc.polyfrost.oneconfig.libs.universal.ChatColor
import cc.polyfrost.oneconfig.libs.universal.UDesktop
import cc.polyfrost.oneconfig.libs.universal.UResolution
import co.skyclient.scc.gui.greeting.components.onLeftClick
import java.net.URI

class TagSlide : GreetingSlide<ImportSlide>(ImportSlide::class.java) {

    val text by UIWrappedText("""
        Supporters of our ${ChatColor.BOLD}Patreon${ChatColor.RESET} receive a special tag in-game!
        ${ChatColor.BLUE}Click here to learn more.${ChatColor.RESET}
    """.trimIndent(), centered = true).constrain {
        x = CenterConstraint()
        y = 5.percent()
        width = 85.percent()
        textScale = (2.5).pixels()
    } childOf window

    val image by UIImage.ofResourceCached("/patreon1.png").apply {
        textureMinFilter = UIImage.TextureScalingMode.LINEAR
        textureMagFilter = UIImage.TextureScalingMode.LINEAR
    } constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = ((UResolution.scaledHeight * 50f / 100f) / 272f * 447f).pixels() // Do this manually because Elementa is stupid...
        height = 50.percent()
    } childOf window

    val image2 by UIImage.ofResourceCached("/patreon2.png").apply {
        textureMinFilter = UIImage.TextureScalingMode.LINEAR
        textureMagFilter = UIImage.TextureScalingMode.LINEAR
    } constrain {
        x = CenterConstraint()
        y = SiblingConstraint() + 2.percent()
        width = ((UResolution.scaledHeight * 8f / 100f) / 82f * 995f).pixels() // Do this manually because Elementa is stupid...
        height = 8.percent()
    } childOf window

    init {
        text.onLeftClick { UDesktop.browse(URI.create("https://www.patreon.com/Polyfrost/membership")) }
    }
}