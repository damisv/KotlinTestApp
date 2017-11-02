package com.assist.tutorial.damian.kotlintestapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun countMinus(view: View){
        var counter:Int = Integer.parseInt(textView.text.toString())
        if (counter>0){
            counter--
        }
        textView.text = counter.toString()
    }

    fun countPlus(view:View){
        var counter:Int = Integer.parseInt(textView.text.toString())
        counter++
        textView.text = counter.toString()
    }

    fun randomMatrix(view:View){
        val randomIntent = Intent(this, SudokuViewActivity::class.java)
        val counter = Integer.parseInt(textView.text.toString())
        randomIntent.putExtra(SudokuViewActivity.COUNT,counter)
        startActivity(randomIntent)
    }


}
