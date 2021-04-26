package model

import dev.fritz2.lenses.Lenses

@Lenses
data class Chip(val colour: String)

val none = Chip("none")
val blue = Chip("blue")
val yellow = Chip("yellow")

@Lenses
data class FourWinsPosition(val id: String, val xPosition: Int, val yPosition: Int, val chip: Chip = none)

@Lenses
data class FourWinsModel(val line1: List<FourWinsPosition>,
                         val line2: List<FourWinsPosition>,
                         val line3: List<FourWinsPosition>,
                         val line4: List<FourWinsPosition>,
                         val line5: List<FourWinsPosition>,
                         val line6: List<FourWinsPosition>,
                         val nextChipToAdd: Chip = none,
                         val playerOne: String = "",
                         val playerTwo: String = "",
                         val winner: Chip = none
                         ) {


    fun getLines(): List<List<FourWinsPosition>> {
        val listToReturn = emptyList<List<FourWinsPosition>>().toMutableList()
        listToReturn.add(line1)
        listToReturn.add(line2)
        listToReturn.add(line3)
        listToReturn.add(line4)
        listToReturn.add(line5)
        listToReturn.add(line6)
        return listToReturn
    }

    fun getLinesInReverseOrder(): List<List<FourWinsPosition>> = getLines().reversed()

    private fun getColumn(columnNo: Int): List<FourWinsPosition> {
        val returnList = emptyList<FourWinsPosition>().toMutableList()
        returnList.addAll(line1.filter { position ->
            position.xPosition == columnNo
        })
        returnList.addAll(line2.filter { position ->
            position.xPosition == columnNo
        })
        returnList.addAll(line3.filter { position ->
            position.xPosition == columnNo
        })
        returnList.addAll(line4.filter { position ->
            position.xPosition == columnNo
        })
        returnList.addAll(line5.filter { position ->
            position.xPosition == columnNo
        })
        returnList.addAll(line6.filter { position ->
            position.xPosition == columnNo
        })
        return returnList
    }

    private fun getLine(lineNo: Int): List<FourWinsPosition> {
        val lineToReturn =  when(lineNo) {
            1 -> line1
            2 -> line2
            3 -> line3
            4 -> line4
            5 -> line5
            6 -> line6
            else -> throw Exception("No Valid Line given for getLine")
        }
        return lineToReturn
    }

    fun addCoinToModel(columnNo: Int, chipToAdd: Chip): FourWinsModel {
            val winner = getWinner()
            println("And the winner is: ${getWinner()}")
            if(winner == none) {
                val column: List<FourWinsPosition> = getColumn(columnNo)
                val firstEmptyChipInColumm: FourWinsPosition = column.first { it.chip == none }
                println("Spalte: $columnNo")
                println("Gesetzt werden soll: ${firstEmptyChipInColumm.xPosition}, ${firstEmptyChipInColumm.yPosition}")
                val lineNo = firstEmptyChipInColumm.yPosition
                println("Zeile $lineNo")
                val line = getLine(lineNo)

                val lineToReplace = line.map {
                    if (it.xPosition == firstEmptyChipInColumm.xPosition && it.yPosition == lineNo) {
                        FourWinsPosition(it.id, it.xPosition, it.yPosition, chipToAdd)
                    } else {
                        it
                    }
                }
                return replaceLineInModel(lineNo, lineToReplace).setNextChipToAdd().setWinner(getWinner() )
            } else {
                return this
            }
    }

    private fun setNextChipToAdd(): FourWinsModel {
        return if(nextChipToAdd == blue){
           this.copy(nextChipToAdd = yellow)
        } else {
            this.copy(nextChipToAdd = blue)
        }
    }

    private fun setWinner(chip: Chip): FourWinsModel {
        return this.copy(winner = chip)
    }

    private fun replaceLineInModel(lineNo: Int, lineToSet: List<FourWinsPosition>): FourWinsModel {
        return when(lineNo) {
            1 -> this.copy(line1 = lineToSet)
            2 -> this.copy(line2 = lineToSet)
            3 -> this.copy(line3 = lineToSet)
            4 -> this.copy(line4 = lineToSet)
            5 -> this.copy(line5 = lineToSet)
            6 -> this.copy(line6 = lineToSet)
            else -> throw Exception("wrong Line Number")
        }
    }


    private fun getWinner(): Chip  {
        val winnercolumn = checkForColumnWinner()
        val winnerLine = checkForLineWinner()
        val winnerChip = listOf(winnerLine, winnercolumn)
        try {
            return winnerChip.first() { it != none }
        } catch(ex: Exception) {
            return none
        }
    }

    private fun checkForColumnWinner(): Chip {
        for(i in 1..7){
            val stringRepresntation: String = getColumn(i).map { if(it.chip == blue) {
                    "1"
                } else if(it.chip == yellow) {
                    "2"
                } else {
                    "0"
                }
            }.fold(""){ a, b -> a + b}
            if (stringRepresntation.contains("1111")) return blue
            if (stringRepresntation.contains("2222")) return yellow
        }
        return none
    }

    private fun checkForLineWinner(): Chip {

        for(i in 1..6){
            val stringRepresntation: String = getLine(i).map { if(it.chip == blue) {
                    "1"
                } else if(it.chip == yellow) {
                    "2"
                } else {
                    "0"
                }
            }.fold(""){ a, b -> a + b}
            if (stringRepresntation.contains("1111")) return blue
            if (stringRepresntation.contains("2222")) return yellow
        }
        return none
    }

}

@Lenses
data class FourWinsWinnerModel(val name: String)




