package com.example.expensense.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.SparseArray
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.expensense.R
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import java.io.IOException

class OCRFragment : Fragment() {
    companion object {
        const val REQUEST_IMAGE_CAPTURE = 101
        const val CAMERA_PERMISSION_REQUEST = 102
        const val REQUEST_OPEN_IMAGE = 103
        const val STORAGE_PERMISSION_REQUEST = 104
    }

    private lateinit var bitmapImage : Bitmap
    private lateinit var receiptDataTextView : TextView
    private lateinit var recognizedTextView : TextView
    private lateinit var image : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_o_c_r, container, false)
        receiptDataTextView = view.findViewById<TextView>(R.id.receiptDataTextView)
        recognizedTextView = view.findViewById<TextView>(R.id.text)
        val btnCapture = view.findViewById<LinearLayout>(R.id.lyt_take_picture)
        val btnUpload = view.findViewById<LinearLayout>(R.id.lyt_upload_image)
        image = view.findViewById<ImageView>(R.id.imgPicture)

        btnCapture.setOnClickListener {
            if (isCameraPermissionGranted()) {
                dispatchTakePictureIntent()
            } else {
                requestCameraPermission()
            }
            // You can open the camera or perform any other action
        }

        btnUpload.setOnClickListener {
            // Handle the "Upload Picture" option
            if (isReadStoragePermissionGranted()) {
                openImagePicker()
            } else {
                requestReadStoragePermission()
            }
            // You can trigger the image selection process
        }
        return view
    }

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST
        )
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
    }

    private fun isReadStoragePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestReadStoragePermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_REQUEST
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can now launch the camera.
                dispatchTakePictureIntent()
            } else {
                // Permission denied, handle accordingly.
                // You may want to show a message to the user explaining why you need the camera permission.
            }
        }
        else{
            if (requestCode == STORAGE_PERMISSION_REQUEST) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, you can now open the gallery.
                    openImagePicker()
                } else {
                    // Permission denied, handle accordingly.
                    // You may want to show a message to the user explaining why you need the storage permission.
                }
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_OPEN_IMAGE)
    }

//    fun addData(){
//        if(imgCategory.drawable == null){
//            imageString = null
//        }
//        else{
//            imageString = encodedBitmap.toString()
//        }
//    }

    private fun processReceiptData(recognizedText: String) {
        // You can use regular expressions or custom logic to extract information from the recognized text.

        // Example 1: Extract the total amount from the recognized text using a regular expression.
        val totalAmountPattern = Regex("(?i)total: (\\d+\\.\\d{2})")
        val totalAmountMatch = totalAmountPattern.find(recognizedText)
        val totalAmount = totalAmountMatch?.groupValues?.get(1) ?: "Not found"

        // Example 2: Extract the date from the recognized text using a regular expression.
        val datePattern = Regex("\\d{2}/\\d{2}/\\d{4}")
        val dateMatch = datePattern.find(recognizedText)
        val date = dateMatch?.value ?: "Date not found"

        // Example 3: Extract a list of items from the recognized text.
        val itemsPattern = Regex("(?i)item: ([\\w\\s]+), price: (\\d+\\.\\d{2})")
        val items = itemsPattern.findAll(recognizedText)
        val itemList = mutableListOf<String>()
        for (item in items) {
            val itemName = item.groupValues[1]
            val itemPrice = item.groupValues[2]
            itemList.add("$itemName - $itemPrice")
        }

        // Example 4: You can implement custom logic to process the extracted data further.

        // Display the extracted data or perform any further processing you need.
        val receiptData = "Total Amount: $totalAmount\nDate: $date\nItems:\n${itemList.joinToString("\n")}"
        displayReceiptData(receiptData)
    }

    private fun displayReceiptData(receiptData: String) {
        // You can display the processed receipt data in a TextView or any other UI element.
        receiptDataTextView.text = receiptData
    }
    private fun processImageWithOCR(imageBitmap: Bitmap) {
        val recognizer = TextRecognizer.Builder(requireContext()).build()

        if (!recognizer.isOperational) {
            // Handle text recognition initialization error
            Toast.makeText(context, "Error Occurred!!!", Toast.LENGTH_SHORT).show()
            return
        }

        val frame = Frame.Builder().setBitmap(imageBitmap).build()
        val textBlockSparseArray : SparseArray<TextBlock> = recognizer.detect(frame)
        val stringBuilder : StringBuilder = StringBuilder()

        for (i in 0 until textBlockSparseArray.size()) {
            val textBlock : TextBlock = textBlockSparseArray.valueAt(i)
            stringBuilder.append(textBlock.value)
            stringBuilder.append("\n")
        }
        recognizedTextView.setText(stringBuilder.toString())
        processReceiptData(recognizedTextView.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_OPEN_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val selectedImageUri: Uri? = data.data
                try {
                    val contentResolver = requireContext().contentResolver
                    val inputStream = selectedImageUri?.let { contentResolver.openInputStream(it) }
                    bitmapImage = BitmapFactory.decodeStream(inputStream)
                    image.background = null
                    image.setImageBitmap(bitmapImage)
                    processImageWithOCR(bitmapImage)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                // Do something with the selected image URI, such as displaying it or processing it.
            }
        }
        else{
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
                bitmapImage = data?.extras?.get("data") as Bitmap
                image.background = null
                image.setImageBitmap(bitmapImage)
                processImageWithOCR(bitmapImage)
                // Do something with the captured image, such as displaying it or saving it.
            }
        }
    }
}