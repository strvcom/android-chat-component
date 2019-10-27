# Messages list

Messages is a customizable component that displays messages of a
selected conversation and listens for realtime updates.

## How to use

To make the component works correctly, you need to perform following
steps.

### Add `ChatRecyclerView` widget into your xml layout

```xml
<com.strv.chat.core.core.ui.chat.messages.ChatRecyclerView
    android:id="@+id/rv_chat"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```
### Initialize the component
Call type-safe `init()` builder function in `onCreate()` method that
allows creating Kotlin-based domain-specific language (DSL) suitable for
configuring the component.

**Optional properties**:
- `imageLoader: ImageLoader` 
  -   Defines a way how to upload picture's urls to `ImageView`s.
- `onItemClick: OnClickAction<ChatItemView>`
  -  Defines an action that is performed after a user clicks on a
     message.
- `viewHolderProvider: ChatViewHolderProvider`
  - Allows to add a custom implementations of `ViewHolders`.
     
```kotlin
chatRecyclerView.init {
    imageLoader = loader
}
```

### Get realtime updates
`ChatRecyclerView` **is not lifecycle-aware**. You have to configure
manually when to start or stop observing data by calling `onStart()`,
with `conversationId` and `members` provided, and `onStop()` functions
respectively. A recommended way is to start listening in `onStart()`
method or as soon as you receive a `conversationId` and a list of
members and to stop listening in `onStop()` method of your
activity/fragment.

```kotlin
override fun onStart() {
    super.onStart()
        
    chatRecyclerView.onStart(chatViewModel.conversationId, otherMembers)
        .onNext {
            Log.d("TAG", "Message list has been changed")
        }.onError {
            Log.e(TAG, error.localizedMessage ?: "Unknown error", error)
        }
}

override fun onStop() {
    super.onStop()

    chatRecyclerView.onStop()
}
```

## Customization

### Styling via attributes
- `app:chrv_textMessageTextSize` - custom outgoing text message text
  size
- `app:chrv_myTextMessageBackground` - custom outgoing text message
  background
- `app:chrv_myTextMessageBackgroundColor` - custom outgoing text message
  background color
- `app:chrv_myTextMessageCornerRadius` - custom outgoing text message
  corner radius
- `app:chrv_myTextMessageStrokeColor` - custom outgoing text message
  stroke color
- `app:chrv_myTextMessageStrokeWidth` - custom outgoing text message
  stroke width
- `app:chrv_myTextMessageTextColor` - custom outgoing text message text
  color
- `app:chrv_otherTextMessageBackground` - custom outgoing text message
  backgroud
- `app:chrv_otherTextMessageBackgroundColor` - custom incoming text
  message background color
- `app:chrv_myTextMessageCornerRadius` - custom incoming text message
  corner radius
- `app:chrv_myTextMessageStrokeColor` - custom incoming text message
  stroke color
- `app:chrv_otherTextMessageStrokeWidth` - custom incoming text message
  stroke width
- `app:chrv_otherTextMessageTextColor` - custom incoming text message
  text color
- `app:chrv_otherTextMessageTextSize` - custom incoming text message
  text size

### Create your own holder
You can define your own holder classes with the help of
`ChatViewHolderProvider` and `ChatVHConfig<T : ChatItemView>`.

```kotlin
val incomingTextMessageVHConfig =
    ChatVHConfig(
        layoutId = ChatViewType.MY_TEXT_MESSAGE.id,
        constructor = { parent, _ ->
            DefaultMyMessageViewHolder(parent)
        }
    )

val outgoingTextMessageVHConfig =
    ChatVHConfig(
        layoutId = ChatViewType.OTHER_TEXT_MESSAGE.id,
        constructor = { parent, _ ->
            DefaultOtherMessageViewHolder(
                parent      
            )
        }
    )

val viewHolderProvider = ChatViewHolderProvider(
    myTextMessageConfig = incomingTextMessageVHConfig,
    otherTextMessageConfig = outgoingTextMessageVHConfig
)

chatRecyclerView.viewHolderProvider = viewHolderProvider
```

You can either inherit from `ChatViewHolder<T : ChatItemView>` class or
from one of the default `ViewHolder`s in case if you are not planning to
rewrite the behaviour from scratch:
- `DefaultHeaderViewHolder` - header item view
- `DefaultMyMessageViewHolder` - outgoing text message item view
- `DefaultOtherMessageViewHolder` - incoming text message item view
- `DefaultMyImageViewHolder` - outgoing image message item view
- `DefaultOtherImageViewHolder` - incoming image message item view

