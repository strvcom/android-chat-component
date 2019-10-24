# Conversation list

Conversation list is a customizable component that displays
conversations of the user and listens for realtime updates.

## How to use

To make the component works correctly, you need to perform several
steps.

### Add `ConversationRecyclerView` widget into your xml layout

```
<com.strv.chat.core.core.ui.conversation.ConversationRecyclerView
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
- `onItemClick: OnClickAction<ConversationItemView>`
  -  Having `onClickListener` defined is needed in order to perform an
     action when the user clicks on a conversation.
- `viewHolderProvider: ConversationViewHolderProvider` - a provider of
  custom implementations of ViewHolders
     
```
conversationRecyclerView.init {
    imageLoader = loader
    onConversationClick = { conversation ->
        openChat(conversation.id, conversation.otherMemberIds)
    }
}   
```

### Get realtime updates
`ConversationRecyclerView` is not lifecycle-aware. You must manually
configure when to start observing data and when to stop by calling
`onStart()` and `onStop()` functions. A recommended way is to start
listening in `onStart()` method and to stop listening in `onStop()`
method.

```
override fun onStart() {
    super.onStart()

    conversationRecyclerView.onStart()
        .onError { error ->
            Log.e(TAG, error.localizedMessage ?: "Unknown error", error)
        }.onNext {
            Log.d("TAG", "Conversation list has been changed")
        }
}

override fun onStop() {
    super.onStop()

    conversationRecyclerView.onStop()
}
```

## Customization

### Styling via attributes
- `app:crv_titleTextSize` Custom title text size
- `app:crv_titleTextColor` Custom title text color
- `app:crv_titleTextStyle` Custom title fond style
- `app:crv_messageTextSize` Custom message text size
- `app:crv_messageTextColor` Custom message text color
- `app:crv_messageTextStyle` Custom title fond style


### Create your own holder
You can define your own holder classes with the help of
`ConversationViewHolderProvider` and `ConversationVHConfig`.

```
val vhConfig = ConversationVHConfig(
    layoutId = R.layout.item_conversation,
    constructor = { parent, _ ->
        MyConversationViewHolder(
            parent
        )
    })

val viewHolderProvider = ConversationViewHolderProvider(
    conversationVHConfig = vhConfig
)

conversationRecyclerView.viewHolderProvider = viewHolderProvider
```

You can either inherit from `ConversationViewHolder` class or from the
default `DefaultConversationViewHolder` in case you are not planning to
rewrite the behaviour from scratch.