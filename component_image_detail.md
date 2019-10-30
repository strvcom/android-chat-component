# Image detail widget

Image detail widget is a component that displays an image fetched from a given
url.

## How to use

To make the component works correctly, you need to perform following
steps.

### Add `ImageDetailView` widget into your xml layout

```xml
<com.strv.chat.core.core.ui.chat.imageDetail.ImageDetailView
    android:id="@+id/iv_photo"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```
### Initialize the component
Call type-safe `init()` builder function in `onCreate()` method that
allows creating Kotlin-based domain-specific language (DSL) suitable for
configuring the component.

**Optional properties**:
- `url: String`
  - Url of the displayed image that a user wants to display
- `imageLoader: ImageLoader` 
  - Defines the way how to upload picture's urls to `ImageView`s.
     
```kotlin
imageDetail.init {
    imageUrl = imageDetailViewModel.imageUrl
    imageLoader = loader
}
```

