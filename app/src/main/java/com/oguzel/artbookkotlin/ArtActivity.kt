package com.oguzel.artbookkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.oguzel.artbookkotlin.databinding.ActivityArtBinding

class ArtActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArtBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.saveButton.setOnClickListener { }
        binding.selectImageView.setOnClickListener { }

        fun saveButtonClicked() {

        }

        fun selectImageClicked() {

        }


    }
}