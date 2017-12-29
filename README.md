# Android image picker
Simplifies the operation to pick image from Gallery/Camera

Usage:
import module imagepicker to your project and add dependency in your app **build.gradle**
```gradle
compile project(':imagepicker')
```


**Features**  
1. Crop   
2. Rotate
3. Saves in specifed directory
4. Scales image (defalult size is 500x500)


```java
PickImage.getInstance()
.setFolderName(fioldeName)//folder name to save in disk
.useStorageApi(isStorageApi)// if set true uses storage api from enabeled above ktikat devices
.setCompressWidthHeight(width,height)//size that you want to resize
.useGallery(isGallery)//pass true if you want to take image from gallery else opens up camera
.setIcon(R.drawable.send)//drawable resource if you want to change send button
.build(context)
```

The above code will return intent you need to call.
```java
 startActivityForResult(intent,requestCode)
```

Result is passed through onActivityResult().
```java 
onActivityResult(int requestCode,int resultCode,Intent data)
```
use ```data.getData()``` to get uri of saved file

**Screenshots**

![Crop preview](preview/device-2017-12-28-173449-min.png)   ![selection preview](preview/device-2017-12-28-173501-min.png)   
