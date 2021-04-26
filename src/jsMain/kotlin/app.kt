import dev.fritz2.binding.RootStore
import dev.fritz2.binding.SubStore
import dev.fritz2.binding.storeOf
import dev.fritz2.components.InputFieldComponent
import dev.fritz2.components.inputField
import dev.fritz2.components.stackUp
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.render
import dev.fritz2.dom.values
import kotlinx.coroutines.flow.map
import model.*

const val numberOfColumns: Int = 7
const val numberOfRows: Int = 6

object FourWinsStore : RootStore<FourWinsModel>(getInitialModel(), "fourWinsModel") {

    val addToColumn = handleAndEmit<Int, String> { model, column ->
        val newModel = model.addCoinToModel(column, model.nextChipToAdd)
        val winner = newModel.winner.colour
        println(winner)
        emit("Peter wars")
        newModel
    }
}

object WinnerStore : RootStore<FourWinsWinnerModel>(FourWinsWinnerModel(""), "fourWinsWinnerModel") {
    val setWinner = handle<String> { winnerModel, name ->
        console.log("trying to set Winner to $name")
        winnerModel.copy(name)
    }

    init {
        // handlers have to be connected
        FourWinsStore.addToColumn  handledBy setWinner
    }
}

fun main() {
    // here the Query-Selector - Syntax is used
    val winnerStore = WinnerStore
    val winnerName = winnerStore.sub(L.FourWinsWinnerModel.name)


    render(){
        p {
            +("" + winnerName.data.asText())
        }
    }
    render("#target") {
        getInputForPlayers()
        getField()
    }
}

fun RenderContext.getInputForPlayers() {
    val nextCoin = FourWinsStore.sub(L.FourWinsModel.nextChipToAdd)
    val isBlue  = nextCoin.data.map { it == blue }.map{ if(it) "" else "visibility: hidden" }
    val isYellow = nextCoin.data.map { it == yellow }.map{ if(it) "" else "visibility: hidden" }

    p{
        stackUp {
            items {
                inputField(store = storeOf<String>("Peter wars", "stringStore")){
                    element {
                        changes.values().map { it.length } handledBy FourWinsStore.addToColumn
                    }
                }
            }
        }

    }
    table {
        tr {
            label { +"Player 1: " }
            span("smallcircle") {
                inlineStyle("background-color: ${blue.colour};")
            }
            input {
                placeholder("name of first player")
            }
            span{
                b {
                    inlineStyle(isBlue)
                    +"<-- it's your turn"
                }
            }
        }
        br{

        }
        tr {
            label { +"Player 2: " }
            span("smallcircle") {
                inlineStyle("background-color: ${yellow.colour}")
            }
            input {
                placeholder("name of second player")
            }
            span{
                b {
                    inlineStyle(isYellow)
                    +"<-- it's your turn"
                }

            }
        }
    }


}

fun RenderContext.getField() {
    table {
        this.tableCaption()
        this.tableHead()
        this.tableBody()
    }

}

fun RenderContext.tableCaption() {
    caption("caption") {
        +"Four Wins let's play..."
    }
}

fun RenderContext.tableHead() {

    thead {
        tr {
            for (i in 1..7) {
                th {
                    button(id = "addButton$i") {
                        +"add coin"
                        clicks.map { i } handledBy FourWinsStore.addToColumn
                    }
                }
            }
        }
    }

}

fun RenderContext.tableBody() {
    tbody {
        FourWinsStore.data.map {
            it.getLinesInReverseOrder()
        }.renderEach { line: List<FourWinsPosition> ->
            tr {
                line.forEach { coin ->
                    td {
                        td {
                            span("circle") {
                                inlineStyle("background-color: ${coin.chip.colour}")
                                +("" + coin.xPosition + coin.yPosition)
                            }
                        }
                    }
                }
            }
        }
    }
}




