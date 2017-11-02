package com.assist.tutorial.damian.kotlintestapp

import android.content.Context
import android.util.Log
import android.widget.Toast
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by damian on 31.10.2017.
 */
class Sudoku(private var context: Context,private var count: Int ) {

    private val sudokuMatrix = Array(9,{IntArray(9)})
    private var solvedMatrix = Array(9,{IntArray(9)})


    var countValueChanges: Int =0
    var failedTries: Int = 0

    init {
        initSudokuMatrix()
    }

    fun getInitMatrix():ArrayList<Int>{
        val tempMatrix:ArrayList<Int> = ArrayList()
        for (i in 0..8){
            (0..8).mapTo(tempMatrix) { sudokuMatrix[i][it] }
        }
        return tempMatrix
    }

    fun getSolvedMatrix():ArrayList<Int>{
        val tempMatrix:ArrayList<Int> = ArrayList()
        for (i in 0..8){
            (0..8).mapTo(tempMatrix) { solvedMatrix[i][it] }
        }
        return tempMatrix
    }

    fun checkMe(matrix: ArrayList<Int>?):Boolean{
        val tempMatrix = Array(9,{IntArray(9)})
        for(i in 0 until matrix!!.size){
            tempMatrix[i/9][i%9] = matrix[i]
        }
        solvedMatrix = Array(9,{IntArray(9)})
        System.arraycopy(tempMatrix,0,solvedMatrix,0,9)
        return (findBlankLocation()[0]==-1 && isCorrect())
    }

    private fun isCorrect(): Boolean {
        for (k in 1..9){
            (0..8)
                    .map { i -> (0..8).count { solvedMatrix[i][it]==k } }
                    .filter { it >1 }
                    .forEach { return false }
            (0..8)
                    .map { j -> (0..8).count{solvedMatrix[it][j]==k} }
                    .filter { it >1 }
                    .forEach { return false }
            for (i in 0..6 step 3){
                for (j in 0..6 step 3){
                    var sum=0
                    for (l in i..i+2){
                        for (m in j..j+2){
                            if (solvedMatrix[l][m]==k){
                                sum++
                            }
                            if(sum>1)return false
                        }
                    }
                }
            }
        }
        return true
    }


    fun solveMe(){
        System.arraycopy(sudokuMatrix,0, solvedMatrix,0,9)
        Log.d("SOLVED_MATRIX:  ","-------------------------------------------------------------------------")
        val status = solveSudoku()

        for (i in 0..8){
            var tempString = ""
            for (j in 0..8){
                tempString+=" [ ${solvedMatrix[i].get(j)} ] "
            }
            Log.d("MATRIX-$i :  ",tempString)
        }
        if(status){
            Log.d("MATRIX : ","Solved")
            Toast.makeText(this.context,"Sudoku Solved with $countValueChanges changes and $failedTries failed tries", Toast.LENGTH_SHORT).show()
        }else{
            Log.d("MATRIX : ","NOT Solved")
            Toast.makeText(this.context,"Sudoku Not Solved", Toast.LENGTH_SHORT).show()
        }
    }

    private fun solveSudoku():Boolean {
        val row:Int
        val column:Int
        val blankCell: IntArray = findBlankLocation()
        row=blankCell[0]
        column=blankCell[1]
        if(row==-1){
            // matrix filled
            return true
        }
        for (i in 1..9){
            if(isSafe(row,column,i)){
                solvedMatrix[row][column]=i
                countValueChanges++
                if(solveSudoku()){
                    return true
                }
                failedTries++
                solvedMatrix[row][column] = 0
            }
        }
        return false
    }

    private fun isSafe(row: Int, column: Int, number: Int): Boolean {
        if(!solvedMatrix[row].contains(number) && !numberExistsOnColumn(number,column)
                && !numberExistsOnBox(row - row % 3, column - column % 3, number)){
            return true
        }
        return false
    }

    private fun numberExistsOnBox(row: Int, column: Int, number: Int): Boolean {
        for (i in 0..2){
            (0..2)
                    .filter { solvedMatrix[i+row][it +column] == number }
                    .forEach { return true }
        }
        return false
    }

    private fun numberExistsOnColumn(number: Int,column: Int): Boolean {
        for (i in 0..8){
            if (solvedMatrix[i][column]==number){
                return true
            }
        }
        return false
    }

    private fun findBlankLocation(): IntArray {
        val temp = IntArray(2)
        for (i in 0..8){
            for(j in 0..8){
                if(solvedMatrix[i][j]==0){
                    temp[0]=i
                    temp[1]=j
                    return temp
                }
            }
        }
        temp[0]=-1
        temp[1]=-1
        return temp
    }

    private fun initSudokuMatrix(){
        randomizeSolvedMatrix()
        removeRandomCells()

        for (i in 0..8){
            var tempString = ""
            for (j in 0..8){
                tempString+=" [ ${sudokuMatrix[i].get(j)} ] "
            }
            Log.d("MATRIX:  ",tempString)
        }

    }

    private fun randomizeSolvedMatrix() {
        for (j in 0..8){
            val base = 9
            val random = Random()
            var randomInt = random.nextInt(base + 1)
            while(sudokuMatrix[0].contains(randomInt)){
                randomInt = random.nextInt(base + 1)
            }
            sudokuMatrix[0].set(j,randomInt)
        }
        for (i in 1..8){
            if(i == 3 || i == 6){
                for(j in 0..8){
                    if(j<8){
                        sudokuMatrix[i][j] = sudokuMatrix[i - 1][j + 1]
                    }else{
                        sudokuMatrix[i][j] = sudokuMatrix[i - 1][j - 8]
                    }
                }
            }else {
                for (j in 0..8) {
                    if (j < 6) {
                        sudokuMatrix[i][j] = sudokuMatrix[i - 1][j + 3]
                    } else {
                        sudokuMatrix[i][j] = sudokuMatrix[i - 1][j - 6]
                    }
                }
            }
        }
    }

    private fun removeRandomCells() {
        val base = 8
        for (i in 0..8){
            for(j in 0..count){
                val random = Random()
                val randomInt = random.nextInt(base+1)
                sudokuMatrix[i][randomInt]=0
            }
        }
    }

}