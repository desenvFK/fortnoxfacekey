
# Anyvision android Facekey  
  
Facekey SDK - Android integration guide and example  
  
  
## Getting Started  
  
* Add a repository link to your repositories in the project level gradle build file:  
```  
allprojects {  
     repositories {  
         maven {  
             credentials {  
                     username facekey_username  
                     password facekey_password  
             }  
             url "https://anyvision.jfrog.io/anyvision/gradle-release"  
         }  
     }  
}  
```  
* Include the following dependency in your app gradle build file: **implementation 'com.anyvision.facekey.facekey:3.1.0'**  
* Add the following lines to your gradle.properties:  
```  
facekey_username=<your supplied username>  
facekey_password=<your supplied password>  
```  
## Usage  
* First Call the **Facekey.initialize(String baseUrl)**.  
* Alternatively you can call  **Facekey.initialize(String baseUrl,int timeout)** where 'timeout' is in milliseconds and defines the timeout of the request to the API. The default value is 40000 and the maximum value is 120000.  
* Also, you can call  **Facekey.initialize(String baseUrl,int timeout, float threshold)**
   where threshold is the threshold value for the face detection. The default value is 0.35. Please note that the SDK will always override the server value.
* Photograph the user's identification any way you want.  
* Create a camera activity and add **com.anyvision.facekey.liveness.LivenessView** in your layout. Do not obscure this view with other views. This will hurt performance.  
* In your camera activity use the view as follows:  
```  
public class CameraPreviewSurface extends Activity implements ILivenessListener {  
  
    private LivenessView livenessVies;  
   
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        livenessView = findViewById(R.id.liveness_view);  
    }  
      
    @Override  
    protected void onStop() {  
        super.onStop();  
        if (livenessView != null){  
            livenessView.release();  
        }  
    }  
      
    @Override  
    public void onFrameCallback(eEvent event) {  
        switch (event){  
            case noFace:  
                <handle no face event>  
                break;  
            case tooManyFaces:  
                <handle too many faces event>  
                break;  
            case faceTooClose:  
                <handle face too close event>  
                break;  
            case faceTooFar:  
                <handle face too far event>  
                break;  
            case faceRightSize:  
                recordVideo();  
                livenessView.startPattern(this);  
                break;  
        }  
    }  
      
    private void onFrame(Bitmap frame){  
        livenessView.onFrame(bitmap,rotation);  
    }  
      
    private void onVideoRecordingStopped(){  
        livenessView.stopPattern();  
    }  
}  
```  
If onFailure is returned a throwable will be returned containing the message and error code.  
You can parse the Json as shown in the example:  
```  
            JSONObject jsonObject = new JSONObject(errorJson);  
            errorArray.add(jsonObject.getString("message"));  
            errorArray.add(jsonObject.getString("code"));  
```              
You can set your custom message according these error codes:  
| Code | Meaning |  |  
| :---         |     :---     |         :--- |  
|ERR_FACEKEY_NO_FACE |   Could not find any faces in the given image|  
|ERR_FACEKEY_TOO_MANY_FACES |  More than one face detected in the image|  
|ERR_FACEKEY_TIMEOUT|  Timeout, no detections in the video|  
|ERR_AV_MISSING_PARAMS|    Missing Parameters in the network call|  
|ERR_AV_DUP_SUSPECT_NAME|  A suspect with this name already exists  
|ERR_FACEKEY_NOT_CONSISTENT|   Too many movements, face not stable.  
|ERR_FACEKEY_NOT_ALIVE|         The person is not alive  
|ERR_FACEKEY_NO_MATCH|    The face and picture don't match  
  
  
* Pass frames from your camera to the ***livenessView*** object and the camera rotation.The default rotation is 0. It will detect faces in those frames and return a ***onFrameCallback***.  
* If the face is the right size, you can start recording a video while the livenessView is showing a color pattern on the screen. The recommended video length is 3 seconds.  
* You can set the color pattern to be transparent. Call **livenessView.setOpacity(true)**. Default value is **false**. 
* Call the **livenessView.stopPattern()** method to stop showing the color pattern after you've finished recording the video.  
* Once you have the video call **Facekey.registerUser(File image, File video, String id, String externalId, Boolean doSaveUser, final IRegisterListener listener)**. The ***image*** and ***video*** are the ones you recorded earlier. The ***id*** and ***externalId*** are ids for your use. ***doSaveUser*** indicated whether you want to save the user in the system or not. The result returns through the ****listener***.  
* Examples for implementing Camera1 and Camera2 are in ***LivenessActivityCamera1*** and ***LivenessActivityCamera2*** respectively.  
* Starting of version 3.0.2 the import of **com.anyvision.facekey.listeners.ILivenessListener.eEvent**  
has changed to **com.anyvision.facekey.listeners.eEvent** .  
* Starting of version 3.1.0 We added code that change the screen brightness  to the maximum. In order to support it you need to ask the user to turn on this dangerous permission (not a runtime permission).  
* Since server version 1.20.2 - When using Facekey.registerUser() call, 'id' field should be unique. if it's already exists you will get this error:
|ERR_AV_DUP_SUSPECT_NAME|  A suspect with this name already exists  
  
***Please note*** This is a basic demo of the FaceKey sdk. Please note that you can alter your app as required.

* Important note - apps that target Android 8.1 (API level 27) or lower. Starting with Android 9 (API level 28), cleartext support is disabled by default.
in order to fix "IOException java.io.IOException: Cleartext HTTP traffic" you need to use https protocol or do as written in: https://developer.android.com/training/articles/security-config#CleartextTrafficPermitted