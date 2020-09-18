package ru.skillbranch.skillarticles.markdown.spans

import android.graphics.*
import android.text.style.ReplacementSpan
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.annotation.VisibleForTesting
import ru.skillbranch.skillarticles.markdown.Element


class BlockCodeSpan(
    @ColorInt
    private val textColor: Int,
    @ColorInt
    private val bgColor: Int,
    @Px
    private val cornerRadius: Float,
    @Px
    private val padding: Float,
    private val type: Element.BlockCode.Type
) : ReplacementSpan() {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var rect = RectF()
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var path = Path()

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) = when(type){
        Element.BlockCode.Type.END -> forEnd(top, canvas, bottom, paint)
        Element.BlockCode.Type.MIDDLE -> forMiddle(top, canvas, bottom, paint)
        Element.BlockCode.Type.SINGLE -> forSingle(top, canvas, bottom, paint, text, x, y)
        Element.BlockCode.Type.START -> forStart(top, canvas, bottom, paint)
    }

    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
//        text as Spannable
//        val start = text.getSpanStart(this)
//        val end = text.getSpanEnd(this)
//        if (start == -1 ||end ==  -1) return 0
//        return end - start
        return 0
    }

    private fun forSingle(top: Int, canvas: Canvas, bottom: Int, paint: Paint, text: CharSequence, x: Float, y: Int){
        paint.forBackground {
            canvas.drawRoundRect(
                RectF(
                    0f,
                    top + padding,
                    canvas.width.toFloat(),
                    bottom - padding
                ),
                cornerRadius,
                cornerRadius,
                paint
            )
        }
        paint.forText {
            canvas.drawText(text, 0, text.length, x + padding, y.toFloat(), paint)
        }
    }

    private fun forStart(top: Int, canvas: Canvas, bottom: Int, paint: Paint){
        path.reset()
        path.addRoundRect(
            RectF(
                0f,
                top + padding,
                canvas.width.toFloat(),
                bottom.toFloat()
            ),
            floatArrayOf(
                cornerRadius, cornerRadius, // Top left radius in px
                cornerRadius, cornerRadius, // Top right radius in px
                0f, 0f, // Bottom right radius in px
                0f, 0f // Bottom left radius in px
            ),
            Path.Direction.CW
        )
        canvas.drawPath(path, paint)
    }

    private fun forMiddle(top: Int, canvas: Canvas, bottom: Int, paint: Paint){
        paint.forBackground {
            canvas.drawRect(
                RectF(
                    0f,
                    top.toFloat(),
                    canvas.width.toFloat(),
                    bottom.toFloat()
                ),
                paint
            )
        }
    }

    private fun forEnd(top: Int, canvas: Canvas, bottom: Int, paint: Paint){
        paint.forBackground {
            path.reset()
            path.addRoundRect(
                RectF(
                    0f,
                    top.toFloat(),
                    canvas.width.toFloat(),
                    bottom - padding
                ),
                floatArrayOf(
                    0f, 0f,
                    0f, 0f,
                    cornerRadius, cornerRadius,
                    cornerRadius, cornerRadius
                ),
                Path.Direction.CW
            )
            canvas.drawPath(path, paint)
        }
    }

    private inline fun Paint.forBackground(block: () -> Unit) {
        val oldColor = color
        val oldStyle = style

        color = bgColor
        style = Paint.Style.FILL

        block()

        color = oldColor
        style = oldStyle
    }

    private inline fun Paint.forText(block: () -> Unit) {
        val oldColor = color
        val oldStyle = typeface?.style ?: 0
        val oldFont = typeface
        val oldSize = textSize

        color = textColor
        typeface = Typeface.create(Typeface.MONOSPACE, oldStyle)
        textSize *= 0.85f

        block()

        color = oldColor
        typeface = oldFont
        textSize = oldSize
    }

}
