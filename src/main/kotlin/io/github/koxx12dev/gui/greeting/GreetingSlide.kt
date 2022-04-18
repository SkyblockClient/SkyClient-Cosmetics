package io.github.koxx12dev.gui.greeting

import gg.essential.api.utils.Multithreading
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.UIComponent
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.UIText
import gg.essential.elementa.components.Window
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ConstraintType
import gg.essential.elementa.constraints.HeightConstraint
import gg.essential.elementa.constraints.WidthConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.constraints.resolution.ConstraintVisitor
import gg.essential.elementa.dsl.*
import gg.essential.elementa.state.BasicState
import gg.essential.elementa.state.State
import gg.essential.elementa.utils.withAlpha
import gg.essential.universal.GuiScale
import gg.essential.vigilance.gui.settings.ButtonComponent
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import java.awt.Color

open class GreetingSlide<T : GuiScreen>(val nextGui: Class<T>, val onClick: () -> Unit = {}) : WindowScreen(version = ElementaVersion.V1, drawDefaultBackground = false, restoreCurrentGuiOnClose = false, newGuiScale = GuiScale.scaleForScreenSize().ordinal) {
    init {
        if (previousScale == Int.MIN_VALUE) {
            previousScale = Minecraft.getMinecraft().gameSettings.guiScale
        }
    }
    private val background by UIBlock(Color(16, 16, 16)) constrain {
        x = 0.pixels()
        y = 0.pixels()
        width = 100.percent()
        height = 100.percent()
    } childOf window

    val blackbar by UIBlock(color = TRANSPARENT_BLACK.withAlpha(128)) constrain {
        x = 0.pixels()
        y = 90.percent()
        width = 100.percent()
        height = 10.percent()
    } childOf window

    private val skyclientLogo by UIImage.ofResource("/assets/scc/skyclient_logo.png") constrain {
        x = 2.pixels()
        y = CenterConstraint()
        height = HeightRelativeConstraint(0.75f)
        width = HeightRelativeConstraint(0.75f)
    } childOf blackbar

    override fun updateGuiScale() {
        newGuiScale = GuiScale.scaleForScreenSize().ordinal
        super.updateGuiScale()
    }

    private val skyclientText by UIText("SkyClient") constrain {
        x = 2.pixels(alignOpposite = true)
        y = CenterConstraint()
        color = Color.CYAN.toConstraint()
    } childOf blackbar

    private val fade by UIBlock(Color.BLACK) constrain {
        x = 0.pixels()
        y = 0.pixels()
        width = 100.percent()
        height = 100.percent()
    } childOf window

    private val button by ButtonComponent("Next") {
        setButtonFloat()
        displayNextScreen()
    } constrain {
        x = CenterConstraint()
        y = CenterConstraint()
    } childOf blackbar

    fun hideNextButton() {
        button.hide(true)
    }

    protected fun displayNextScreen() {
        fade.setFloating(true)
        fade.animate {
            setColorAnimation(Animations.LINEAR, 0.5f, Color(16, 16, 16).toConstraint())
        }
        Multithreading.runAsync {
            while (fade.getColor().alpha != 255) { ; }
            Minecraft.getMinecraft().addScheduledTask {
                onClick()
                displayScreen(nextGui.getConstructor().newInstance())
            }
        }
    }

    protected open fun setButtonFloat() {
        button.setFloating(true)
    }

    init {
        Window.enqueueRenderOperation {
            fade.setFloating(true)
            fade.animate {
                setColorAnimation(Animations.LINEAR, 0.5f, TRANSPARENT_BLACK.toConstraint())
            }
            Multithreading.runAsync {
                while (true) {
                    if (fade.getColor().alpha == 0) {
                        Window.enqueueRenderOperation {
                            fade.setFloating(false)
                            setButtonFloat()
                        }
                        break
                    } else {
                        Thread.sleep(50)
                    }
                }
            }
        }
    }

    class HeightRelativeConstraint(value: Float) : WidthConstraint, HeightConstraint {
        override var cachedValue = 0f
        override var recalculate = true
        override var constrainTo: UIComponent? = null

        private var valueState: State<Float> = BasicState(value)

        var value: Float
            get() = valueState.get()
            set(value) { valueState.set(value) }

        override fun getWidthImpl(component: UIComponent): Float {
            return (constrainTo ?: component.parent).getHeight() * valueState.get()
        }

        override fun getHeightImpl(component: UIComponent): Float {
            return (constrainTo ?: component.parent).getHeight() * valueState.get()
        }

        override fun visitImpl(visitor: ConstraintVisitor, type: ConstraintType) {

        }
    }

    companion object {
        val TRANSPARENT_BLACK = Color(0, 0, 0, 0)
        var previousScale = Int.MIN_VALUE
    }
}