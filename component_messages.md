# Messages list

Messages is a customizable component that displays messages of a
selected conversation and listens for realtime updates.

## How to use

To make the component works correctly, you need to perform several
steps.

### Add `ChatRecyclerView` widget into your xml layout

```
<com.strv.chat.core.core.ui.chat.messages.ChatRecyclerView
    android:id="@+id/rv_chat"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```
### Initialize the component
Call `init()` builder function in `onCreate()` method in order to setup
the component.

**Optional items**:
- `imageLoader: ImageLoader` 
  -   Tells the component how to upload picture's urls to `ImageView`s.
  -   In case that the entity is not defined, images will be empty and
      an error message will be logged.
- `onItemClick: OnClickAction<ChatItemView>`
  -  Having `onClickListener` defined is needed in order to perform an
     action when the user clicks on a message.
- `viewHolderProvider: ChatViewHolderProvider` - a provider of custom implementations of
  ViewHolders
     
```
chatRecyclerView.init {
    imageLoader = loader
}
```

### Get realtime updates
`ChatRecyclerView` is not lifecycle-aware. You must manually configure
when to start observing data and when to stop by calling `onStart()`,
with `conversationId` and `members` provided, and `onStop()` functions.
A recommended way is to start listening in `onStart()` method or as soon
as you receive a list of members and to stop listening in `onStop()`
method.

```
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
- `app:chrv_textMessageTextSize` Custom outgoing text message text size
- `app:chrv_myTextMessageBackground` Custom outgoing text message background
- `app:chrv_myTextMessageBackgroundColor` Custom outgoing text message background
  color
- `app:chrv_myTextMessageCornerRadius` Custom outgoing text message corner radius
- `app:chrv_myTextMessageStrokeColor` Custom outgoing text message stroke color
- `app:chrv_myTextMessageStrokeWidth` Custom outgoing text message stroke width
- `app:chrv_myTextMessageTextColor` Custom outgoing text message text color
- `app:chrv_otherTextMessageBackground` Custom outgoing text message
  backgroud
- `app:chrv_otherTextMessageBackgroundColor` Custom incoming text
  message background color
- `app:chrv_myTextMessageCornerRadius` Custom incoming text message corner
  radius 
- `app:chrv_myTextMessageStrokeColor` Custom incoming text message
  stroke color
- `app:chrv_otherTextMessageStrokeWidth` Custom incoming text message
  stroke width
- `app:chrv_otherTextMessageTextColor` Custom incoming text message text
  color
- `app:chrv_otherTextMessageTextSize` Custom incoming text message text
  size

### Create your own holder
You can define your own holder classes with the help of
`ChatViewHolderProvider` and `ChatVHConfig<T : ChatItemView>`.

```
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
from one of the default `ViewHolder`s in case you are not planning to
rewrite the behaviour from scratch:
- `DefaultHeaderViewHolder` Header item view
- `DefaultMyMessageViewHolder` Outgoing text message item view
- `DefaultOtherMessageViewHolder` Incoming text message item view
- `DefaultMyImageViewHolder` Outgoing image message item view
- `DefaultOtherImageViewHolder` Incoming image message item view

