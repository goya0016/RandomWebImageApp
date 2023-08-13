//Name: nipun goyal
//Date: 08/11/2020

package com.example.randomwebimageapp


// region imports
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.io.File

// endregion
/*
* Created by Student Name on October 23, 2020
*/
class GlideImage {

    private val randomSiteIdentifier = "<RANDOM>" // note < and > are illegal url characters
    private val appScreenWidth = "<appscreenwidth>"
    private val appScreenHeight = "<appscreenheight>"

    private var width = 300
    private var height = 400


    // region list of image url

    private val listOfImageUrls = mutableListOf<String>(
            "https://loremflickr.com/${appScreenWidth}/${appScreenHeight}$randomSiteIdentifier",
            "https://source.unsplash.com/collection/630950/${appScreenWidth}x${appScreenHeight}$randomSiteIdentifier",
            "https://www.placebear.com/${appScreenWidth}/${appScreenHeight}",
            "https://www.placecage.com/g/${appScreenWidth}/${appScreenHeight}",
            "https://www.stevensegallery.com/c/${appScreenWidth}/${appScreenHeight}",
            "https://placeimg.com/${appScreenWidth}/${appScreenHeight}/any",
            "https://picsum.photos/${appScreenWidth}/${appScreenHeight}$randomSiteIdentifier",
            "https://www.fillmurray.com/${appScreenWidth}/${appScreenHeight}",
            "https://placebeard.it/${appScreenWidth}/${appScreenHeight}",
            "https://keywordimg.com/${appScreenWidth}x${appScreenHeight}/random",
            "https://www.placecage.com/${appScreenWidth}/${appScreenHeight}"


    )

    //endregion

    //region properties
    private var listCounter = 0

    var lastURL = ""
        private set

    var diskCacheStrategy = DiskCacheStrategy.ALL

    private val displayInfo = DisplayInfo()

    val sharedPreference = SharedPreference()
    //endregion

    // region loadGlideImage function

    fun loadGlideImage(
            imageView: ImageView,
            context: Activity,
            progressBar: ProgressBar,
            url: String = getRandomImageURL()
    ) {
        progressBar.visibility = View.VISIBLE

        val portrait = displayInfo.isPortrait

        width = displayInfo.realWidth
        height = displayInfo.realHeight

        if(portrait){
            //swap width and height if portrait
          width = height.also { height = width }
        }
        width /= 4
        height /= 4
        //Log.d("urldata" ,url)

        var updatedurl = url

        if (url.contains(randomSiteIdentifier)){
            diskCacheStrategy = DiskCacheStrategy.NONE
            updatedurl = url.replace(randomSiteIdentifier,"")
        }else{
            diskCacheStrategy = DiskCacheStrategy.ALL
            updatedurl = updatedurl.replace(appScreenWidth,width.toString())
            updatedurl = updatedurl.replace(appScreenHeight,height.toString())
            Log.d("urldata" ,updatedurl)
        }

        Glide.with(context)
                .load(updatedurl)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        progressBar.visibility = View.GONE
                        context.toast("Glide Load Failed:$updatedurl")
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        progressBar.visibility = View.GONE
                        // context.toast("Glide Load success")
                        imageView.setImageDrawable(resource)
                        lastURL = updatedurl
                        sharedPreference.save(context.getString(R.string.last_url_key),lastURL)
                        return false
                    }
                })
                .diskCacheStrategy(diskCacheStrategy)
                .into(imageView)


    }
    //endregion

    // region getRandomImageURL function
    private fun getRandomImageURL(): String {

        lastURL = listOfImageUrls[listCounter]
        listCounter++
        if (listCounter == listOfImageUrls.size) {
            listOfImageUrls.shuffle()
            listCounter = 0
        }
        return lastURL
    }

    //endregion

    // region init function
    init {
        listOfImageUrls.shuffle()

    }

    //endregion

    fun emptyCache(context: Context) {
        val asyncGlide = AsyncGlide(context)
        asyncGlide.execute()
    }

    //region loadImageFromInternalStorage
    fun loadImageFromInternalStorage(
            imageView: ImageView,
            context: Context
    ){
        var filePath = "${context.filesDir}${File.separator}${context.getString(R.string.last_image_file_name)}"
        Glide.with(context)
                .load(File(filePath))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView)

    }
    //endregion

    // region toast
    // Extension method
    fun Context.toast(message: String) {
        Toast.makeText(TheApp.context, message, Toast.LENGTH_SHORT).show()
    }
    //endregion

    private inner class AsyncGlide(val context: Context) : AsyncTask<Any, Any, Any>() {
        override fun doInBackground(vararg params: Any?): Any? {
            Glide.get(context).clearDiskCache()
            return null
        }

        override fun onPostExecute(result: Any?) {
            super.onPostExecute(result)
            context.toast("Image cache deleted")
        }


    }
}