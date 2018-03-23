# Image Perspective Transform
Transform the perspective of an image into a reference image perspective.

### How the algorithm works?
* As input the algorithm gets `referenceImage` and `originalImage`
* First it runs a feature detection over both images
* The features are matched, and 4 of them selected for the perspective transformation
    * The 4 features are in the top `n` best matched features and the ones with the longest distance to each other 
* Perspective transformation is performed on the `originalImage`
* Transformed image is returned

### Current State
![Current State](readme/current.jpg)

### Algorithms

* ORB for feature detection
* BFMatcher for matching
* Perspective and warping by opencv

### Example
Here is an illustrated example how the algorithm performs.

#### Reference Image
![Reference Image](data/result/reference.png)

#### Original Image
![Original Image](data/result/original.png)

#### Transformed Image
![Transformed Image](data/result/result.png)

#### Additive Composition
![Additive Composition](data/result/blend.png)

### About
Algorithms by opencv / Florian Bruggisser *(ZHdK 2018)*