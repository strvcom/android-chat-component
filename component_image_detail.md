# Image detail widget

Messages is a component that displays an image fetched from a given
url.

## How to use

To make the component works correctly, you need to perform several
steps.

### Add `ImageDetailView` widget into your xml layout

```xml
<com.strv.chat.core.core.ui.chat.imageDetail.ImageDetailView
    android:id="@+id/iv_photo"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```
### Initialize the component
Call `init()` builder function in `onCreate()` method in order to setup
the component.

**Optional items**:
- `url: String`
  - Url of the displayed image that the user wants to display
- `imageLoader: ImageLoader` 
  -   Defines a way how to upload picture's urls to `ImageView`s.
     
```kotlin
imageDetail.init {
    imageUrl = imageDetailViewModel.imageUrl
    imageLoader = loader
}
```

