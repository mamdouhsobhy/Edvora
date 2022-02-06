package com.example.edvora

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import com.example.edvora.classes_views.DrawingView
import com.example.edvora.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private lateinit var draw:DrawingView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initialize()
        addListenerOnView()

    }

    private fun initialize() {
        draw=findViewById(R.id.main_VW_layout)
    }

    private fun addListenerOnView() {
        binding!!.imgPencil.setOnClickListener {
            sendNumAction(1)
        }
        binding!!.imgArrow.setOnClickListener {
            sendNumAction(2)
        }
        binding!!.imgSquare.setOnClickListener {
            sendNumAction(3)
        }

        binding!!.imgCircle.setOnClickListener {
            sendNumAction(4)
        }
        binding!!.imgColor.setOnClickListener {
            dialogSelectAColor()
        }
        binding!!.mainVWLayout.setOnClickListener {
            binding?.mainSPSelectColor?.isVisible=false
        }

    }

    private fun sendNumAction(num: Int) {
      draw.num(num)
    }

    private fun dialogSelectAColor() {
        //listener for color select
        Log.d("coloSP", "SpectrumPalette")
         binding?.mainSPSelectColor?.isVisible=true
        binding!!.mainSPSelectColor.setOnColorSelectedListener { col ->
            binding?.mainSPSelectColor?.isVisible=false
            binding?.mainVWLayout?.setColor(col)
            binding?.imgColor?.backgroundTintList = ColorStateList.valueOf(col)

        }
    }

}