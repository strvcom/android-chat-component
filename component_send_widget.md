# Send widget

Send widget is a customizable component for entering and sending text
and image messages. 

## How to use

To make the component work correctly, you need to perform the following
steps.

### Add `SendWidget` widget into your xml layout

```xml
<com.strv.chat.core.core.ui.chat.sending.SendWidget
    android:id="@+id/w_send"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```
### Initialize the component
Call type-safe `init()` builder function in `onCreate()` method of your
Activity that allows creating Kotlin-based domain-specific language
(DSL) suitable for configuring the component.

Minimum **required properties** that the component needs to be set:
- `conversationId: String` 
  -   `id` of the related conversation
- `newFileProvider: FileProvider`
  - Defines a way of retrieving file uri for saving the camera output that
    is used to send as an image message
     
```kotlin
sendWidget.init {
    conversationId = chatViewModel.conversationId
    newFileProvider = chatViewModel.newFileProvider
}
```

### Send an image message
The Android Camera application encodes the result of an image capture
action to `onActivityResult()`. Thus, it is your responsibility to
handle the `REQUEST_IMAGE_CAPTURE` request code in `onActivityResult()`
method.

In case of successful processing of the image, Send widget API contains the
method `uploadImage(uri: Uri)`, which will **start a service that uploads
the image on the server and shows a notification that notifies about the
progress and the result of the upload**.

## Customization

### Styling via attributes
- `app:sw_backgroundColor` - custom component background color
- `app:sw_sendIconTint` - custom send button icon tint
- `app:sw_messageOptionIconColorNormal` - custom message option icons tint
  in an unselected state
- `app:sw_messageOptionIconColorActivated` - custom message option icons
  tint in a selected state
- `app:sw_sendIcon` - custom send button icon
- `app:sw_textOptionIcon` - custom text-message option button icon
- `app:sw_imageOptionIcon` - custom image-message option button icon
- `app:sw_hint` - custom hint for the input edit text
- `app:sw_imageOptionEnabled` - allow/disallow image messages, `true` by
  default
- `app:sw_inputTextAppearance` - custom message input text appearance from
  the specified style resource

### Notification configuration
- `largeIconRes: Int` - custom large icon resource id that is shown in the ticker
  and notification
- `smallIconProgressRes: Int` - custom small icon resource id to use in the
  notification layouts in a progress state
- `smallIconSuccessRes: Int` - custom small icon resource id to use in the
  notification layouts in a success state
- `smallIconErrorRes: Int` - custom small icon resource id to use in the
  notification layouts in an error state
