# Augmented Reality Senior Project (Uses [GPUImage for Android](https://github.com/CyberAgent/android-gpuimage))

The goal of the project is to have an application which aids those with impaired vision and is suitable for classroom (specifically whiteboard) use.
This project uses [GPUImage for Android](https://github.com/CyberAgent/android-gpuimage) as a large framework for the application, with several modifications:

- Split screen for headset use
- Modifications to various filters in order to make the application useful for people with visual impairments
- Zoom functionality
- Removal of photo-taking capability
- Bug fixes

See the section "TODO" for future goals.

**NOTE:** This application is meant to be used with a headset. This application has only been tested using the Android Galaxy Note 3


## Requirements
* Android 2.2 or higher (OpenGL ES 2.0)
* Android NDK

## Usage

### Gradle dependency

```groovy
repositories {
    mavenCentral()
}

dependencies {
    compile 'jp.co.cyberagent.android.gpuimage:gpuimage-library:1.3.0'
}
```

### Sample Code
With preview:

```java
@Override
public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity);

    Uri imageUri = ...;
    mGPUImage = new GPUImage(this);
    mGPUImage.setGLSurfaceView((GLSurfaceView) findViewById(R.id.surfaceView));
    mGPUImage.setImage(imageUri); // this loads image on the current thread, should be run in a thread
    mGPUImage.setFilter(new GPUImageSepiaFilter());
}
```

Without preview:

```java
Uri imageUri = ...;
mGPUImage = new GPUImage(context);
mGPUImage.setFilter(new GPUImageSobelEdgeDetection());
mGPUImage.setImage(imageUri);
mGPUImage.saveToPictures("GPUImage", "ImageWithFilter.jpg", null);
```

### Gradle
Make sure that you run the clean target when using maven.

```groovy
gradle clean assemble
```

## TODO
The following goals are planned for the application:
- [x] Add a working dilation filter to the sobel edge detection filter to increase text size
- [ ] Human testing (need to go through ethics board first)
- [ ] Add pinch zoom


## License
    Copyright 2012 CyberAgent, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
