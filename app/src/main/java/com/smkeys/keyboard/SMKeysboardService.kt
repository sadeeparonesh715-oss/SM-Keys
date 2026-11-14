
package com.smkeys.keyboard

import android.inputmethodservice.InputMethodService
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputConnection
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class SMKeyboardService : InputMethodService() {

    private var isSinhala = false

    override fun onCreateInputView(): View {

        val main = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(6, 12, 6, 12)
            setBackgroundColor(Color.rgb(15, 18, 35))
        }

        val title = TextView(this).apply {
            text = "SM keys  •  ${if (isSinhala) "සිංහල" else "English"}"
            textSize = 17f
            gravity = Gravity.CENTER
            setTextColor(Color.WHITE)
            setPadding(5, 12, 5, 12)
        }

        main.addView(title)

        val rows = listOf(
            listOf("q","w","e","r","t","y","u","i","o","p"),
            listOf("a","s","d","f","g","h","j","k","l"),
            listOf("⇧","z","x","c","v","b","n","m","⌫"),
            listOf("?123","🌐","English","Space","↵")
        )

        rows.forEach { row ->
            val line = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
            }

            row.forEach { key ->
                val button = Button(this).apply {
                    text = key
                    textSize = 14f
                    setTextColor(Color.WHITE)
                    background = GradientDrawable().apply {
                        setColor(Color.rgb(42, 48, 70))
                        cornerRadius = 18f
                        setStroke(1, Color.rgb(100, 120, 170))
                    }

                    setOnClickListener {
                        handleKey(key, title)
                    }
                }

                val params = LinearLayout.LayoutParams(
                    0,
                    52.dp,
                    1f
                ).apply {
                    setMargins(2, 3, 2, 3)
                }

                line.addView(button, params)
            }

            main.addView(line)
        }

        return main
    }

    private fun handleKey(key: String, title: TextView) {
        val ic: InputConnection =
            currentInputConnection ?: return

        when (key) {
            "⌫" -> ic.deleteSurroundingText(1, 0)

            "Space" -> ic.commitText(" ", 1)

            "↵" -> ic.commitText("\n", 1)

            "🌐" -> {
                isSinhala = !isSinhala
                title.text =
                    "SM keys  •  ${if (isSinhala) "සිංහල" else "English"}"
            }

            "⇧" -> {}

            else -> ic.commitText(key, 1)
        }
    }

    private val Int.dp: Int
        get() = (this * resources.displayMetrics.density).toInt()
}
