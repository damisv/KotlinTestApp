package com.assist.tutorial.damian.kotlintestapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.NumberPicker
import kotlinx.android.synthetic.main.activity_third.*
import kotlinx.android.synthetic.main.item.view.*
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.Toast


class SudokuViewActivity : AppCompatActivity() {

    var sudoku: Sudoku? = null
    private var gridAdapter:GridViewCustomAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        val count = intent.getIntExtra(COUNT, 3)
        sudoku = Sudoku(this,count)

         gridAdapter = GridViewCustomAdapter(this, sudoku!!.getInitMatrix())
        grid_view.adapter = gridAdapter
    }

    open class GridViewCustomAdapter(context : Context, sudokuMatrix: ArrayList<Int>) : BaseAdapter() {
        var context: Context? = context
        var matrix: ArrayList<Int>? = sudokuMatrix
        private var inflater:LayoutInflater? = null

        init {
            this.inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        fun getArray(): ArrayList<Int>? {
            return this.matrix
        }


        @SuppressLint("ViewHolder", "InflateParams")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: View? = inflater?.inflate(R.layout.item,null)
            view!!.button.text = matrix?.get(position).toString()
            view.button.textSize = 15f
            if(matrix?.get(position) ==0){
                val gd = GradientDrawable()
                gd.setColor(Color.GRAY)
                view.button.background = gd
            }

            view.button.tag = matrix?.get(position).toString()
            if(matrix?.get(position)==0){
                view.button.setOnClickListener(object: View.OnClickListener {
                    override fun onClick(view: View): Unit {
                        val builder: AlertDialog.Builder
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = context?.let { AlertDialog.Builder(it, android.R.style.Theme_Material_Dialog_Alert) }!!
                        } else {
                            builder = context?.let { AlertDialog.Builder(it) }!!
                        }

                        val numberPicker:NumberPicker = NumberPicker(context)
                        numberPicker.maxValue=9
                        numberPicker.minValue=0
                        numberPicker.value = Integer.parseInt(view.button.tag.toString())
                        val layout = FrameLayout(context)
                        layout.addView(numberPicker, FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.WRAP_CONTENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT,
                                Gravity.CENTER))
                        builder.setTitle("Change value for ${position}")
                                .setView(layout)
                                .setPositiveButton(android.R.string.yes) { dialog, which ->
                                    Log.d("VALUE  ","${numberPicker.value}")
                                    view.button.text = numberPicker.value.toString()
                                    val gd = GradientDrawable()
                                    if(numberPicker.value!=0) {
                                        gd.setColor(Color.BLUE)
                                    }else{
                                        gd.setColor(Color.GRAY)
                                    }
                                    view.button.background = gd
                                    matrix!![position]=numberPicker.value
                                }
                                .setNegativeButton(android.R.string.no) { dialog, which ->
                                    // do nothing
                                }
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show()
                    }
                })
            }
            return view
        }

        override fun getItem(position: Int): Any {
            return matrix!!.get(position)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return matrix!!.size
        }
    }

    fun solveMe(view:View){
        this.sudoku?.solveMe()
        val tempMatrix = sudoku?.getSolvedMatrix()
        this.gridAdapter = GridViewCustomAdapter(this, tempMatrix!!)
        grid_view.adapter=gridAdapter
    }

    fun checkMe(view: View){
        val temp = this.gridAdapter?.getArray()

        if(sudoku!!.checkMe(temp)){
            Toast.makeText(this,"Solved Sudoku",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"This doesn't seem right !",Toast.LENGTH_SHORT).show()
        }
    }
    companion object {
        const val COUNT = "total_count"
    }
}
