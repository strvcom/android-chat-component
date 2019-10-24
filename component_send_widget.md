# Send widget

Send widget is a customizable component for entering and sending text
and image messages. 

## How to use

To make the component works correctly, you need to perform several
steps.

### Add `SendWidget` widget into your xml layout

```
<com.strv.chat.core.core.ui.chat.sending.SendWidget
    android:id="@+id/w_send"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```
### Initialize the component
Call `init()` builder function in `onCreate()` method in order to setup
the component.

Minimum **required items** that the component needs are:
- `conversationId: String` 
  -   `id` of the related conversation
- `newFileProvider: FileProvider`
  - an interface responsible for retrieving file Uri for saving a camera
    output that is used to send an image message
     
```
sendWidget.init {
    conversationId = chatViewModel.conversationId
    newFileProvider = chatViewModel.newFileProvider
}
```

### Send image message
The Android Camera application encodes result of the image capture
action to `onActivityResult()`. Thus is your responsibility to handle
`REQUEST_IMAGE_CAPTURE` request code in `onActivityResult()` method.

In case of successful processing of the image, Send widget API contains
method `uploadImage(uri: Uri)` which will start a foreground service
that uploads the image on the server and notify the user about the
result.

## Customization

### Styling via attributes
- `app:sw_backgroundColor` Custom component background color
- `sw_sendIconTint` Custom sendIcon tint


### Notification configuration
- `largeIconRes: Int` Set the large icon that is shown in the ticker and
  notification
- `smallIconProgressRes: Int` Set the small icon to use in the
  notification layouts in a progress state
- `smallIconSuccessRes: Int` Set the small icon to use in the
  notification layouts in a success state
- `smallIconErrorRes: Int` Set the small icon to use in the notification
  layouts in an error state