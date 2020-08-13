package com.model.basemodel.custom_view_study

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * author ：king
 * date : 2020/8/13 11:02
 * description :
 */
class CustomView : View {
    var paint: Paint = Paint()
    constructor(context: Context) : super(context) {
        initPaint()
    }
    constructor(context: Context, attrs: AttributeSet?) : super(
        context,
        attrs
    ){
        initPaint()
    }

    /**
     * 设置画笔
     */
    private fun initPaint(){
        paint.color = Color.RED
        paint.strokeWidth = 50f
        paint.style = Paint.Style.FILL_AND_STROKE
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(50f,50f,200f,50f,paint)
    }
}