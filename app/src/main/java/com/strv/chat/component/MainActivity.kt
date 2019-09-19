package com.strv.chat.component

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.strv.chat.library.cloudStorage.di.cloudStorageMediaClient
import com.strv.chat.library.domain.Task
import com.strv.chat.library.domain.provider.ConversationProvider
import com.strv.chat.library.domain.provider.MediaProvider
import com.strv.chat.library.domain.provider.MemberModel
import com.strv.chat.library.domain.provider.MemberProvider
import com.strv.chat.library.firestore.di.firestoreChatClient
import com.strv.chat.library.ui.REQUEST_IMAGE_CAPTURE
import com.strv.chat.library.ui.chat.sending.SendWidget
import com.strv.chat.library.ui.chat.messages.ChatRecyclerView
import strv.ktools.logI
import strv.ktools.logMe
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    val firestoreDb by lazy {
        FirebaseFirestore.getInstance()
    }

    val firebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }

    val chatRecyclerView by lazy {
        findViewById<ChatRecyclerView>(R.id.rv_chat)
    }

    val sendWidget by lazy {
        findViewById<SendWidget>(R.id.w_send)
    }

    val progress by lazy {
        findViewById<ProgressBar>(R.id.progress)
    }

    val memberProvider = object : MemberProvider {
        override fun currentUserId(): String = "user-1"

        override fun member(memberId: String): MemberModel {
            if (memberId == "user-1") {
                return user1()
            } else {
                return user2()
            }
        }

    }

    val conversationProvider = object : ConversationProvider {
        override val conversationId: String
            get() = "WWRweQVRkNBRMsxQ0UFh"

    }

    var uri: Uri? = null
    var name: String? = null

    val mediaProvider = object : MediaProvider {


        override fun imageUri(): Uri {
            val fileName = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())


            return imageFile(
                this@MainActivity,
                fileName,
                "Chat_component"
            ).also {
                uri = it
                name = "IMG_${fileName}_.jpg"
            }
        }


    }

    private fun imageFile(context: Context, fileName: String, albumDirectoryName: String): Uri {


// android q: content://media/external/images/media/13662

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_${fileName}_.jpg")

            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$albumDirectoryName")

            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        return requireNotNull(
            context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            ).also {
                it.logMe()
            }
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chatRecyclerView(
            firestoreChatClient(firestoreDb),
            conversationProvider,
            memberProvider
        )

        sendWidget(
            firestoreChatClient(firestoreDb),
            cloudStorageMediaClient(firebaseStorage),
            conversationProvider,
            memberProvider,
            mediaProvider
        )
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, uri!!))
            sendWidget.uploadImage(name!!, bitmap)

            //   val imagesRef = storageRef.child("image")


            //val file = imageFile(this, name!!, "Chat_component")
//            file.logMe()
//            file.lastPathSegment.logMe()

            //  realPathFromUri(file)


            //MediaType.Companion.run { "image/jpeg".toMediaType() }

            /*
            FireStore

            1. getRef - immediately
            2. upload to Ref - get progress
            3. get url
             */


            /*
            S3

            1. getUrl - suspendb
            2. upload to Url - get progress
            3. get Url
             */


//            val fileDescriptor = contentResolver.openFile(uri!!, "r", null)
//            val fileDescriptor2 = contentResolver.openFileDescriptor(uri!!, "r", null)
//
//            fileDescriptor2?.fileDescriptor?



//
//
//            photoRef.putFile(file)
//                .addOnSuccessListener {
//                    "File was uploaded".logMe()
//                }
//                .addOnFailureListener{
//                    "File wasnt uploaded: ${it.localizedMessage}".logMe()
//                }
//                .addOnProgressListener {
//                    it.bytesTransferred.logMe()
//                }
//                .continueWith {
//                    photoRef.downloadUrl
//                }.addOnSuccessListener {
//                    it.result.logMe()
//                    sendWidget.sendImageMessage(it.result.toString())
//                }.addOnFailureListener {
//                    "Error during uri generation: ${it.localizedMessage}".logMe()
//                }


            // sendWidget.sendImageMessage("https://images.unsplash.com/photo-1523895665936-7bfe172b757d?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80")

//            val imageBitmap = data?.extras?.get("data") as Bitmap
//            image.setImageBitmap(imageBitmap)
            // imageView.setImageBitmap(imageBitmap)
        }
    }


    fun user1() = object : MemberModel {
        override val userId: String = "user-1"
        override val userName: String = "John"
        override val userPhotoUrl: String =
            "https://d1uzq9i69r6hkq.cloudfront.net/profile-photos/ccbc2eb4-4902-40b1-9f2f-6c516964c038.jpg"

    }

    fun user2() = object : MemberModel {
        override val userId: String = "user-2"
        override val userName: String = "Camila"
        override val userPhotoUrl: String =
            "https://d1uzq9i69r6hkq.cloudfront.net/profile-photos/d0727450-0984-4108-993f-a6173008264d.jpg"

    }

    override fun onStart() {
        super.onStart()

        chatRecyclerView.onStart()
            .onNext {
                progress.visibility = View.GONE
            }.onError {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }

    override fun onStop() {
        super.onStop()

        chatRecyclerView.onStop()
        sendWidget.onStop()
    }
}
