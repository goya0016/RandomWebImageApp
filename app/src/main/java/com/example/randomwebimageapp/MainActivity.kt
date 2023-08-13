
//Name: nipun goyal
//Date: 08/11/2020



package com.example.randomwebimageapp

//region imports
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.gesture.Gesture
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.GestureDetectorCompat
import com.example.randomwebimageapp.databinding.ActivityMainBinding
//endregion

class MainActivity : AppCompatActivity(),
GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    //region properties
    private val glideImage = GlideImage()
  private lateinit var binding : ActivityMainBinding
    //setContentView(binding.root)

    private var gestureDetector: GestureDetectorCompat? = null

    private var showingSystemUI = true

    // for runtime permissions, can be any positive int value
    private val requestCode = 13
    //endregion

    //region onCreate function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

         binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val internetConnected = InternetConnected(this)

        if(!internetConnected.checkNetworkConnectivity()){
            AlertDialog.Builder(this)
                .setTitle(R.string.message_title)
                .setMessage(R.string.message_text)
                .setNegativeButton(R.string.quit){_,_ -> finishAffinity()}
                .show()
        }else{
            setupPermissions()
            glideImage.emptyCache(this)

            val sharedPreference=SharedPreference()

            gestureDetector = GestureDetectorCompat(this,this)
            gestureDetector?.setOnDoubleTapListener(this)

//            binding.getImageButton.setOnClickListener(){
//                glideImage.loadGlideImage(binding.imageView1,this,binding.progressBar)
//            }
//            binding.getImageButton.callOnClick()
            glideImage.loadGlideImage(binding.imageView1,this,binding.progressBar)
        }
    }
    //endregion

    override fun onPause() {
        super.onPause()
        if (binding.imageView1.drawable != null){
            val bitmap = binding.imageView1.drawable.toBitmap()
            val asyncStorageIO= AsyncStorageIO(bitmap,true)
            asyncStorageIO.execute()
        }
    }

    //region functions
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        this.gestureDetector?.onTouchEvent(event)
        return super.onTouchEvent(event)
    }


    fun Context.toast(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
    //endregion

    //region gesture event methods
    override fun onDown(e: MotionEvent?): Boolean {
        return  true
    }

    override fun onShowPress(e: MotionEvent?) {
        //return  true
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return  true
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        return  true
    }

    override fun onLongPress(e: MotionEvent?) {
glideImage.emptyCache(this)
        this.toast("image cache deleted")
    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        if(e1.x < e2.x){
            glideImage.loadGlideImage(binding.imageView1,this,binding.progressBar)
        }

        return  true
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        return  true
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        showingSystemUI = if(showingSystemUI){
            hideSystemUI()
            false
        }else{
            showingSystemUI
            true
        }
        return  true
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        return  true
    }
    //endregion

    //region hide/show ui
    private fun hideSystemUI(){
window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
        )
    }

    private fun showSystemUI(){
        window.decorView.systemUiVisibility = (
                     View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )

    }
    //endregion

    // region Permissions
    private fun setupPermissions(){
        val permission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            this.toast("Permission denied!")
            makeRequest()
        } else {
            this.toast("Permission Already Granted...")
        }
    }

    private fun makeRequest(){
        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                requestCode
        )
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            this.requestCode -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    this.toast("Permission denied by user!")
                } else {
                    this.toast("Permission granted by user!")
                }
            }
        }
    }
//endregion
}