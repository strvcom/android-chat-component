# Chat component

Chat component SDK is designed to simplify the development of a chat
functionality. It has a customizable easily extendable solution.

#### Technical details
  
- **Multiple backend support**
  - [Firestore](https://firebase.google.com/docs/firestore/)
  - [Firebase](https://firebase.google.com/docs/database) (under 
    development)
- **Cloud Storage support**
  - [Cloud Firestore](https://firebase.google.com/docs/firestore)
- **Kotlin-first**
- **API Level 21+** (compatible with 85% of Android devices)

#### Features
- **Hook up to the selected backed support**
- **Ready-to-use and customizable UI**
  - [Conversations list](https://github.com/strvcom/android-research-chat-component/blob/feature/readme/component_conversations.md)
  - [Messages list](https://github.com/strvcom/android-research-chat-component/blob/feature/readme/component_messages.md)
  - [Sending widget](https://github.com/strvcom/android-research-chat-component/blob/feature/readme/component_send_widget.md)
  - [Image detail widget](https://github.com/strvcom/android-research-chat-component/blob/feature/readme/component_image_detail.md)
- **Various types of messages**
  - Text message
  - Image message (from camera or gallery)
- **Notification about the state of an image upload**
- **Tap to show the timestamp of the sent message**

## Adding the Chat component SDK to your project

``` //todo change later
 implementation 'com.strv.chat.component::chat-component-core:1.0.0'
```
You should use the core module **if you want to have custom backend
implementation**. In this case, you will be forced to implement all the
required interfaces by yourself.

#### Firestore

``` //todo change later
 implementation 'com.strv.chat.component::chat-component-firestore:1.0.0'
```
You should use firestore module **if you want to use the default [Cloud
Firestore](https://firebase.google.com/docs/firestore/) implementation**
for your backend.

##### Firestore structure
The database structure required for using the default Firestore
implementation, is described in
[Firestore docs](https://github.com/strvcom/android-research-chat-component/blob/master/firestore.md).

#### Firebase Realtime Database

``` //todo change later
 implementation 'com.strv.chat.component::chat-component-firebase:1.0.0'
```

You should use firebase module **if you want to use the default
[Firebase Realtime Database](https://firebase.google.com/docs/database)
implementation** for your backend.


#### Firebase Cloud Storage

```//todo change later implementation
'com.strv.chat.component::chat-component-storage:1.0.0'
```

You should use storage module **if you want to use the default [Firebase
Cloud](https://firebase.google.com/docs/firestore) implementation** as
your media storage.

### Initializing Chat component 

Use `ChatComponent.init()` function to configure and initialize the Chat
component SDK in your application.

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
`Task` API is widely used in Chat component SDK to represent a **promise
that computation will be done**. It is a wrapper around a **result of
an asynchronous call**.

There are three kinds of Task:
- `Task<Result, Error>` 
  - Represents a **single result of an asynchronous call** that returns
    `<Result>` type in case of success and `<Error>` in case of
    error.
- `ProgressTask<Result, Error>`
  - Represents a **single result of an asynchronous call and
    periodically notifies about progress of the call**. It returns
    `<Result>` type in case of success and `<Error>` in case of error.
- `ObservableTask<Result, Error>`
  - Represents a **stream with real time updates** of the result of type
    `<Result>`. Returns `<Error>` when an error occurs.

#### Handling task results

#### `Task<Result, Error>` 

To be notified when the task **succeeds**, call `onSuccess`: 
```kotlin
task.onSuccess { result ->
    Log.d("TAG", "Task completed successfully with a result: $result")
}
```

To be notified when the task **fails**, call `onError`:
```kotlin
task.onError { error ->
    Log.e("TAG", "Task failed with an exception: ${error.localizedMessage ?: "Unknown error"}")
}
```

#### `ProgressTask<Result, Error>` 

To be notified when the task **succeeds**, call `onSuccess`: 
```kotlin
progressTask.onSuccess { result ->
    Log.d("TAG", "Task completed successfully with a result: $result")
}
```

To be notified when the task **fails**, call `onError`:
```kotlin
progressTask.onError { error ->
    Log.e("TAG", "Task failed with an exception: ${error.localizedMessage ?: "Unknown error"}")
}
```

To be repeatedly notified about the **progress**, call `onProgress`:
```kotlin
progressTask.onProgress { progress ->
    Log.d("TAG", "Task progress is: $progress")
}
```

#### `ObservableTask<Result, Error>` 

To **subscribe** to the source of data, call `onNext`" 
```kotlin
observableTask.onNext { result ->
    Log.d("TAG", "Task has a new result: $result")
}
```

To be notified when the task **fails**, call `onError`:
```kotlin
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
```kotlin
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
```kotlin
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
```kotlin
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
Task operators were created to provide to the users smooth possibility
to **transform task data**. You can find the list of the supported
operators
[here](https://github.com/strvcom/android-research-chat-component/blob/feature/readme/chat-component-core/src/main/java/com/strv/chat/core/domain/task/operators.kt).