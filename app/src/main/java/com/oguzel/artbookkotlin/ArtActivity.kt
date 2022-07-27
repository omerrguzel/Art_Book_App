package com.oguzel.artbookkotlin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import com.google.android.material.snackbar.Snackbar
import com.oguzel.artbookkotlin.databinding.ActivityArtBinding
import java.lang.Exception

class ArtActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArtBinding
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedBitmap : Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.saveButton.setOnClickListener { }
        binding.selectImageView.setOnClickListener {
            selectImageClicked(view)
        }

        registerLauncher()


    }
    fun saveButtonClicked() {

    }

    fun selectImageClicked(view: View) {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                //rationale
                Snackbar.make(view,"Permission needed for gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission",
                    View.OnClickListener {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }).show()
            }else {
                //request permission
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

        }else {
            val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            //intent
            activityResultLauncher.launch(intentToGallery)

        }
    }

    private fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->
                if(result.resultCode == RESULT_OK){
                    val intentFromResult = result.data
                    if(intentFromResult != null){
                        val imageData = intentFromResult.data
//                        binding.selectImageView.setImageURI(imageData)
                        if(imageData != null) {
                            try {
                                if(Build.VERSION.SDK_INT >= 28) {
                                    val source = ImageDecoder.createSource(this@ArtActivity.contentResolver,imageData)
                                    selectedBitmap = ImageDecoder.decodeBitmap(source)
                                    binding.selectImageView.setImageBitmap(selectedBitmap)
                                } else {
                                    selectedBitmap = MediaStore.Images.Media.getBitmap(contentResolver,imageData)
                                    binding.selectImageView.setImageBitmap(selectedBitmap)
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            })
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission(),
            ActivityResultCallback { success ->
                if(success){
                    //permission granted
                    val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)
                }else{
                    //permission denied
                    Toast.makeText(this@ArtActivity,"Permission denied!",Toast.LENGTH_LONG).show()
                }
            })

        }

}