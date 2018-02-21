# Android image picker
Simplifies the operation to pick image from Gallery/Camera abc

Usage: download this project and 
import module imagepicker to your project and add dependency in your app

![Crop preview](preview/device-2017-12-28-173449-min.png)   ![selection preview](preview/device-2017-12-28-173501-min.png) 

**Features**  
1. Crop   
2. Rotate
3. Saves in specifed directory
4. Compresses image to specified size (defalult size is 500x500)


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


**Credits**

Author: Jinu Jayakumar ( jinuj27@gmail.com)

Any hint, suggestion, improvement or comment will be appreciated


**License**

Copyright 2017 Jinu Jayakumar
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
