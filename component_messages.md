# Messages list

Messages is a customizable component that displays messages of a
selected conversation and listens for realtime updates.

## How to use

To make the component work correctly, you need to perform the following
steps.

### Add `ChatRecyclerView` widget into your xml layout

```xml
<com.strv.chat.core.core.ui.chat.messages.ChatRecyclerView
    android:id="@+id/rv_chat"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```
### Initialize the component
Call type-safe `init()` builder function in `onCreate()` method of your
Activity that allows creating Kotlin-based domain-specific language
(DSL) suitable for configuring the component.

**Optional properties**:
- `imageLoader: ImageLoader` 
  -   Defines how to upload a picture's url to `ImageView`s.
- `onItemClick: OnClickAction<ChatItemView>`
  -  Defines the action that is performed after a user clicks on a
     message.
- `viewHolderProvider: ChatViewHolderProvider`
  - Allows the adding of custom implementations of `ViewHolders`.
     
```kotlin
chatRecyclerView.init {
    imageLoader = loader
}
```

### Get realtime updates
`ChatRecyclerView` **is not lifecycle-aware**. You have to manually configure
 when to start or stop observing data by calling `onStart()`,
with `conversationId` and `members` provided, and `onStop()` functions
respectively. It is recommended to start listening in `onStart()`
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
- `app:chrv_textMessageTextSize` - custom text message text
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
- `app:chrv_otherTextMessageCornerRadius` - custom incoming text message
  corner radius
- `app:chrv_otherTextMessageStrokeColor` - custom incoming text message
  stroke color
- `app:chrv_otherTextMessageStrokeWidth` - custom incoming text message
  stroke width
- `app:chrv_otherTextMessageTextColor` - custom incoming text message
  text color

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

You can either inherit from the following classes:
- `HeaderViewHolder` - header item view
- `MyTextMessageViewHolder` - outgoing text message item view
- `OtherTextMessageViewHolder` - incoming text message item view 
- `MyImageMessageViewHolder` - outgoing image message item view
- `OtherImageMessageViewHolder` - incoming image message item view

or from one of the default `ViewHolder`s in case you are not planning to
rewrite the behavior from scratch:
- `DefaultHeaderViewHolder` - header item view
- `DefaultMyTextMessageViewHolder` - outgoing text message item view
- `DefaultOtherMessageViewHolder` - incoming text message item view
- `DefaultMyImageMessageViewHolder` - outgoing image message item view
- `DefaultOtherImageMessageViewHolder` - incoming image message item view

