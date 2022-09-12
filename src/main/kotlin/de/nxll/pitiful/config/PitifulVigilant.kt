package de.nxll.pitiful.config

import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import java.awt.Color
import java.io.File

class PitifulVigilant : Vigilant(
    file = File("./config/pitiful.toml"), guiTitle = "Pitiful"
) {

    init {
        initialize()
        markDirty()
    }

    @Property(
        category = "ESP",
        subcategory = "Sewers",
        name = "Sewer ESP",
        type = PropertyType.SWITCH
    )
    var sewerEspEnabled: Boolean = true

    @Property(
        category = "ESP",
        subcategory = "Sewers",
        name = "Max Delay",
        description = "Max delay between chat message and cheat appearance for detection",
        type = PropertyType.SLIDER,
        min = 0,
        max = 20
    )
    var sewerEspMaxDelay: Int = 5

    @Property(
        category = "ESP",
        subcategory = "Sewers",
        name = "Color",
        type = PropertyType.COLOR
    )
    var sewerEspColor: Color = Color(255, 155, 0, 175)

}