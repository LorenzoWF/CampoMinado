package com.game.campominado

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlin.random.Random
import kotlin.collections.ArrayList as ArrayList
import android.support.v7.app.AlertDialog;

class MainActivity : AppCompatActivity() {

    //I - INITIAL | X - BOMB | Numbers...
    var positions = Array(8) {Array(8) {"I"} };
    var fixedExcludesButtons = arrayListOf(19, 20, 29, 30, 39, 40, 49, 50, 59, 60, 69, 70, 79, 80, 89)
    val fixedExcludesPositions = arrayListOf(18, 28, 38, 48, 58, 68, 78, 88)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnNewGame = findViewById<Button>(R.id.btnNewGame)
        btnNewGame.setOnClickListener({
            initGame()
            btnNewGame.visibility = View.INVISIBLE
        })

        val builder = AlertDialog.Builder(this)

        for (x in 0 .. 7) {
            for (y in 0..7) {
                val resID = resources.getIdentifier(
                    "btn" + (x + 1).toString() + (y + 1).toString(),
                    "id", packageName
                )

                val button = findViewById<Button>(resID)
                button.visibility = View.INVISIBLE
                button.setOnClickListener {
                    if (positions[x][y] == "X") {
                        markAllBombs()
                        endGame()

                        with(builder)
                        {
                            setTitle("FIM DE JOGO")
                            setMessage("VOCÊ PERDEU")
                            show()
                        }
                    } else {
                        when(positions[x][y]) {
                            "1" -> button.setTextColor(Color.BLUE)
                            "2" -> button.setTextColor(Color.RED)
                            "3" -> button.setTextColor(Color.GREEN)
                            "4" -> button.setTextColor(Color.MAGENTA)
                            "5" -> button.setTextColor(Color.YELLOW)
                            "6" -> button.setTextColor(Color.CYAN)
                            else -> button.setTextColor(Color.GRAY)
                        }
                        markPosition(x, y)
                        if (checkEndGame() == true) {
                            endGame()

                            with(builder)
                            {
                                setTitle("FIM DE JOGO")
                                setMessage("VOCÊ GANHOU")
                                show()
                            }
                        }
                    }
                }
            }
        }
    }

    fun initGame(): Unit {

        for (x in 0 .. 7) {
            for (y in 0..7) {
                positions[x][y] = "I"
            }
        }

        fixedExcludesButtons = arrayListOf(19, 20, 29, 30, 39, 40, 49, 50, 59, 60, 69, 70, 79, 80, 89)
        var number: Int


        var z = 0
        while (z < 10) {
            number = generateRandom(11, 49, fixedExcludesButtons)
            fixedExcludesButtons.add(number)
            initBomb(number)
            z++
        }

        initNumbers()

        for (x in 0 .. 7) {
            for (y in 0..7) {
                val resID = resources.getIdentifier(
                    "btn" + (x + 1).toString() + (y + 1).toString(),
                    "id", packageName
                )

                val button = findViewById<Button>(resID)
                button.setText("")
                button.isEnabled = true
                button.setTextColor(Color.BLACK)
                button.visibility = View.VISIBLE
            }
        }
    }

    fun checkEndGame() : Boolean {
        for (x in 0 .. 7) {
            for (y in 0..7) {
                if (positions[x][y] != "X") {
                    val resID = resources.getIdentifier(
                        "btn" + (x + 1).toString() + (y + 1).toString(),
                        "id", packageName
                    )

                    val button = findViewById<Button>(resID)

                    if (button.text.toString() == "") {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    fun endGame() : Unit {
        val btnNewGame = findViewById<Button>(R.id.btnNewGame)
        btnNewGame.visibility = View.VISIBLE

        for (x in 0 .. 7) {
            for (y in 0..7) {
                val resID = resources.getIdentifier(
                    "btn" + (x + 1).toString() + (y + 1).toString(),
                    "id", packageName
                )

                val button = findViewById<Button>(resID)
                button.isEnabled = false
            }
        }
    }




    fun generateRandom(start: Int, end: Int, excludeRows: ArrayList<Int>): Int {
        val rand = Random
        var random = rand.nextInt(start, end)
        while (excludeRows.contains(random)) {
            random += 1
            if (random == end) {
                random = start
            }
        }
        return random
    }

    fun initBomb(position: Int): Unit {
        val x : Int = (position / 10) - 1
        val y : Int = (position % 10) - 1
        positions[x][y] = "X"
    }

    fun markAllBombs() : Unit {
        for (x in 0 .. 7) {
            for (y in 0..7) {
                if (positions[x][y] == "X") {
                    markPosition(x, y)
                }
            }
        }
    }

    fun markPosition(x : Int, y : Int) : Unit {
        val resID = resources.getIdentifier(
            "btn" + (x + 1).toString() + (y + 1).toString(),
            "id", packageName
        )

        val button = findViewById<Button>(resID)
        button.text = positions[x][y].toString()
    }

    fun checkBomb(checkX : Int, checkY : Int) : Boolean {
        if (checkX == -1 || checkY == -1 || checkX == 8 || checkY == 8) {
            return false
        }
        val position = (checkX.toString() + checkY.toString()).toInt()
        if (!fixedExcludesPositions.contains(position)) {

            if (positions[checkX][checkY] == "X") {
                return true
            }
        }
        return false
    }

    fun initNumbers() : Unit {

        var countBombs : Int

        for (x in 0 .. 7) {
            for (y in 0..7) {
                if (positions[x][y] != "X") {
                    countBombs = 0

                    //CHECK LEFT x - 1
                    if (checkBomb(x - 1, y) == true) {
                        countBombs += 1
                    }

                    //CHECK RIGHT x + 1
                    if (checkBomb(x + 1, y) == true) {
                        countBombs += 1
                    }

                    //CHECK UP y - 1
                    if (checkBomb(x, y - 1) == true) {
                        countBombs += 1
                    }

                    //CHECK DOWN y + 1
                    if (checkBomb(x, y + 1) == true) {
                        countBombs += 1
                    }

                    //CHECK LEFT UP x - 1 & y + 1
                    if (checkBomb(x - 1, y + 1) == true) {
                        countBombs += 1
                    }

                    //CHECK LEFT DOWN x - 1 & y - 1
                    if (checkBomb(x - 1, y - 1) == true) {
                        countBombs += 1
                    }

                    //CHECK RIGHT UP x + 1 & y + 1
                    if (checkBomb(x + 1, y + 1) == true) {
                        countBombs += 1
                    }

                    //CHECK RIGHT DOWN x + 1 & y - 1
                    if (checkBomb(x + 1, y - 1) == true) {
                        countBombs += 1
                    }

                    if (countBombs == 0) {
                        positions[x][y] = "X"
                    }
                }
            }
        }


        for (x in 0 .. 7) {
            for (y in 0 .. 7) {
                if (positions[x][y] != "X") {
                    countBombs = 0

                    //CHECK LEFT x - 1
                    if (checkBomb(x - 1, y) == true) {
                        countBombs += 1
                    }

                    //CHECK RIGHT x + 1
                    if (checkBomb(x + 1, y) == true) {
                        countBombs += 1
                    }

                    //CHECK UP y - 1
                    if (checkBomb(x, y - 1) == true) {
                        countBombs += 1
                    }

                    //CHECK DOWN y + 1
                    if (checkBomb(x, y + 1) == true) {
                        countBombs += 1
                    }

                    //CHECK LEFT UP x - 1 & y + 1
                    if (checkBomb(x - 1, y + 1) == true) {
                        countBombs += 1
                    }

                    //CHECK LEFT DOWN x - 1 & y - 1
                    if (checkBomb(x - 1, y - 1) == true) {
                        countBombs += 1
                    }

                    //CHECK RIGHT UP x + 1 & y + 1
                    if (checkBomb(x + 1, y + 1) == true) {
                        countBombs += 1
                    }

                    //CHECK RIGHT DOWN x + 1 & y - 1
                    if (checkBomb(x + 1, y - 1) == true) {
                        countBombs += 1
                    }

                    positions[x][y] = countBombs.toString()
                }
            }
        }
    }
}
