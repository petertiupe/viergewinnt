import dev.fritz2.binding.RootStore
import dev.fritz2.binding.storeOf
import model.*

private fun getInitialLine(lineNumber: Int): List<FourWinsPosition> {
    val returnList = emptyList<FourWinsPosition>().toMutableList()
    for (x in 1..7) {
        val id = "" + x + lineNumber
            returnList.add(FourWinsPosition(id, x, lineNumber, none))
    }
    return returnList
}

fun getInitialModel(): FourWinsModel {

    val line1 = getInitialLine(1)
    val line2 = getInitialLine(2)
    val line3 = getInitialLine(3)
    val line4 = getInitialLine(4)
    val line5 = getInitialLine(5)
    val line6 = getInitialLine(6)

    return FourWinsModel(line1, line2, line3, line4, line5, line6, blue)
}

