package nmhu.edu.sd_bssd4250hw91_camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog

class ViewImageActivity : AppCompatActivity() {

    private lateinit var imagepreview: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        imagepreview = ImageView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            //get extra from intent and turn to Uri (make sure it is lowercase)
            setImageURI(Uri.parse(intent.getStringExtra("filePath")))
        }
        setContentView(imagepreview)
    }


    val saveButton = Button(this).apply {
        text = "Save"
        setOnClickListener {
            MediaScannerConnection.scanFile(applicationContext, arrayOf(filesDir.toString()),
                null, null) }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    val closeButton = Button(this).apply {
        text = "Discard"
        setOnClickListener {
            //finish()
            editBitmap((intent.getStringExtra("filePath")))
        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun editBitmap(filePath: String?){
        val orig = BitmapFactory.decodeFile(filePath)
        val bmp = Bitmap.createScaledBitmap(orig,orig.width/32,
            orig.height/32,true)

        val w = bmp.width
        val h = bmp.height
        var outputString = ""
        for (y in 0..h-1){  //for all the pixels in the bmp
            for (x in 0..w-1) {
                var currColor:Int = (bmp.getColor(x,y).red()*255).toInt()
                currColor += (bmp.getColor(x,y).blue()*255).toInt()
                currColor += (bmp.getColor(x,y).green()*255).toInt()
                currColor /= 3 //average of r,g,b

                if(currColor < 255/4) {
                    outputString += "-"
                } else if (currColor < (255/4)*2){
                    outputString += "+"
                } else if (currColor < (255/4)*3){
                    outputString +="!"
                } else {
                    outputString += "@"
                }
            }
            outputString += "\n"
        }

        val tv= TextView(this).apply{
            text = outputString
            typeface = Typeface.MONOSPACE
        }

        val asciiButton = Button(this).apply{
            text = "Show ASCII Art"
            setOnClickListener {
                Log.d("MACT", outputString)
                (
               AlertDialog.Builder(context).apply {
                    setTitle("ASCII ART")
                    setMessage("Do you want to close or save ASCII art?")
                    setPositiveButton("save") { _, _ ->
                        MediaScannerConnection.scanFile(applicationContext, arrayOf(filesDir.toString()),
                            null, null) }
                    }
                    .setNegativeButton("Discard"){_,_ ->})
               }
            }
        }
    }





