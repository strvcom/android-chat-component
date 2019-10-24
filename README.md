# Chat component

Chat component SDK is designed to simplify the development of chat functionality. It has a customizable solution that is easily extendable.

#### Technical details
  
- **Multiple backend support**
  [Firestore](https://firebase.google.com/docs/firestore/),
  [Firebase](https://firebase.google.com/docs/database) (under
  development)
- **Cloud Storage support using Cloud Firestore**
- **Kotlin**
- **API Level 21+** Compatible with 85% of Android devices

#### Features
- Hook up to the selected backed support
- Ready-to-use UI
  - A list of conversations
  - An infinite list of messages with lazy loading
  - Sending widget
  - Image detail widget
- Various types of messages
  - Text message
  - Image message (picking an image from camera or gallery)
- Notification about the state of image upload
- Show timestamp on tap
- Customizable UI

#### UI components
To implement all of the features above you can use the following
components:
- Conversations list
- Messages list
- Message input
- Image detail

## Adding the Chat component SDK to your project

``` //todo change later
 implementation 'com.strv.chat.component::chat-component-core:1.0.0'
```
The base module you should use **if you want to have completely custom
backend implementation**. You will be forced to implement all the
required interfaces by yourself.

#### Firestore

``` //todo change later
 implementation 'com.strv.chat.component::chat-component-firestore:1.0.0'
```
The module you should use **if you want to use the default Cloud
Firestore implementation** for your backend.

##### Firestore structure
The database structure required for using the default Firestore implementation is described in [Firestore docs](https://github.com/strvcom/android-research-chat-component/blob/master/firestore.md).

#### Firebase Realtime Database

``` //todo change later
 implementation 'com.strv.chat.component::chat-component-firebase:1.0.0'
```

The module you should use **if you want to use the default Realtime
Database implementation** for your backend.


#### Firebase Cloud Storage

```//todo change later implementation
'com.strv.chat.component::chat-component-storage:1.0.0'
```

The module you should use **if you want to use the default Firebase
Cloud implementation** as your media storage.

### Initializing Chat component 

Use `ChatComponent.init()` function to configure and initialize the
component in your application.

```kotlin
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // initialize Chat component
        ChatComponent.init(
            //application context
            this,
            //configuration
            Configuration(
                chatClient,
                conversationClient,
                memberClient,
                mediaClient,
                //notification configuration - for a complete list of available attributes see //todo link
                serviceConfig(CHANNEL_ID)
            )
        )
    }
}
```

## Task interface
`Task` API is widely used in SDK to represents asynchronous method
calls.

There are three implementations of Task API
- `Task<Result, Error>` Represents a single result of an asynchronous
  call that returns `<Result>` type in case of success and `<Error>` in
  case of an error
- `ProgressTask<Result, Error>` Represents a single result of an
  asynchronous call that periodically notify about the progress and
  returns `<Result>` type in case of success and `<Error>` in case of an
  error
- `ObservableTask<Result, Error>` Represents a stream with real time
  updates of the result of type `<Result>`. Returns `<Error>` when an
  error occurs.

#### Handling task results

#### `Task<Result, Error>` 

To be notified when the task succeeds succeeds, call `onSuccess`: 
```
task.onSuccess { result ->
    Log.d("TAG", "Task completed successfully with result: $result")
}
```

To be notified when the task fails, call `onError`:
```
task.onError { error ->
    Log.e("TAG", "Task failed with an exception: ${error.localizedMessage ?: "Unknown error"}")
}
```

#### `ProgressTask<Result, Error>` 

To be notified when the task succeeds succeeds, call `onSuccess`: 
```
progressTask.onSuccess { result ->
    Log.d("TAG", "Task completed successfully with result: $result")
}
```

To be notified when the task fails, call `onError`:
```
progressTask.onError { error ->
    Log.e("TAG", "Task failed with an exception: ${error.localizedMessage ?: "Unknown error"}")
}
```

To be periodically notified about the progress, call `onProgress`:
```
progressTask.onProgress { progress ->
    Log.d("TAG", "Task progress is: $progress")
}
```

#### `ObservableTask<Result, Error>` 

To subscribe to the source od data, call `onNext`" 
```
observableTask.onNext { result ->
    Log.d("TAG", "Task has a new result: $result")
}
```

To be notified when the task fails, call `onError`:
```
observableTask.onError { error ->
    Log.e("TAG", "Task failed with an exception: ${error.localizedMessage ?: "Unknown error"}")
}
```

### Create your own task
You can convert any existing callback-based API to the Task API via 
- `task`
- `observableTask`
- `progressTask` 

functions:

#### `Task<Result, Error>` 
```
interface Callback<T> {
    fun onSuccess(result: T)
    fun onError(e: Exception)
}

fun <T> taskCallback(block: (Callback<T>) -> Unit): Task<T, Throwable> = task<T, Throwable> {
    block(object : Callback<T> {
        override fun onSuccess(result: T) {
            invokeSuccess(result)
        }

        override fun onError(e: Exception) {
            invokeError(e)
        }
    })
}
```
#### `ProgressTask<Result, Error>` 
```
fun subscribe(): ObservableTask<List<Entity>, Throwable> =
    observableTask<List<Entity>, Throwable>(::unsubscribe) {
        listenerRegistration =
            queryAource.addSnapshotListener { result, exception ->
                if (exception != null) {
                    invokeError(exception)
                } else {
                    val list = arrayListOf<Entity>()

                    result?.mapTo(list) { snapShot ->
                        snapShot.toObject(clazz).also { item ->
                            item.id = snapShot.id
                        }
                    }

                    invokeNext(list)
                }
            }
    }
```
#### `ObservableTask<Result, Error>` 
```
fun uploadImage(bitmap: Bitmap, uploadUrl: String, contentType: String) =
    progressTask<DownloadUrl, Throwable> {
        val metadata = storageMetadata {
            this.contentType = contentType
        }

        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos)

        val imagePath = path(firebaseStorage, uploadUrl)

        imagePath.putBytes(bos.toByteArray(), metadata)
            .addOnSuccessListener() {
                logD("File was uploaded")
            }.addOnFailureListener {
                invokeError(it)
            }.addOnProgressListener { snapshot ->
                invokeProgress((100.0 * snapshot.bytesTransferred / snapshot.totalByteCount).toInt())
            }.continueWithTask {
                imagePath.downloadUrl
            }.addOnSuccessListener { uri ->
                invokeSuccess(uri)
            }.addOnFailureListener {
                invokeError(it)
            }
    }
```

### Task operators
Task operators were created to provide users smooth possibility to
transform task data. You can find the list of supported operators here.
//todo link