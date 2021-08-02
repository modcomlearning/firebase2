package com.mwishen.myapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class PostRecord : AppCompatActivity() {
    //declare views
    private lateinit var progressBar: ProgressBar
    private lateinit var ivImage: ImageView
    private lateinit var etTitle : TextInputEditText
    private lateinit var etDescription: TextInputEditText
    //set up 2 firebase variables
    private lateinit var storageReference: StorageReference
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_record)
        //here
        progressBar = findViewById(R.id.progressbar)
        ivImage = findViewById(R.id.ivImage)
        etTitle = findViewById(R.id.etTitle)
        etDescription = findViewById(R.id.etDescripion)

        ivImage.setOnClickListener{
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryIntent.setType("image/*")
            startActivityForResult(galleryIntent, 1)
        }//end listener

        //here - we create firebase references for image storage and posts
        storageReference = FirebaseStorage.getInstance().reference.child("MODCOM/IMAGES").child("${System.currentTimeMillis()}"+".jpg")
        databaseReference = FirebaseDatabase.getInstance().reference.child("MODCOM/POSTS")
    }//end oncreate

    //after oncreate, we do code to return back the image from gallery to
    //our image view
    var imageUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imageUri = data?.data //get image data and load to imageUri variable
        //set the image to our image view
        ivImage.setImageURI(imageUri)
    }//end here

    //here paste 2 more functions here
    fun postItem(view: View) {
        progressBar.visibility = View.VISIBLE
        val title = etTitle.text.toString()
        val descrition = etDescription.text.toString()
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(descrition)){
            //ready to post
            var imageurl = "default"
            if (imageUri!=null){
                //image  supplied
                val uploadTask: UploadTask = storageReference.putFile(imageUri!!)
                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    storageReference.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        uploadToFirebase(title = title,description= descrition,imageurl = downloadUri.toString())

                    } else {
                        Toast.makeText(this, "Something went wrong while uploading image", Toast.LENGTH_SHORT).show()
                    }
                }

            }else{
                //image not supplied
                progressBar.visibility = View.VISIBLE
                uploadToFirebase(title = title,description= descrition,imageurl = imageurl)
            }
        }
    }//post item end here


    //paste the other function
    private fun uploadToFirebase(title:String, description:String,  imageurl:String){
        val hashMap = HashMap<String, Any>()
        hashMap["title"] = title
        hashMap["description"] = description
        hashMap["timestamp"] = System.currentTimeMillis()
        hashMap["image"]=imageurl
        //random key
        val postid = databaseReference.push().key.toString()
        databaseReference.child(postid).updateChildren(hashMap).addOnCompleteListener{
            if (it.isSuccessful){
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Posted Successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }else{
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Error:${it.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }//end upload to firebase
}//end class