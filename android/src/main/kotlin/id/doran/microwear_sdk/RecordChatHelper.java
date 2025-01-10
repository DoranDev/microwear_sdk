package id.doran.microwear_sdk;

import android.text.TextUtils;
import android.util.Log;

import com.njj.njjsdk.callback.CallBackManager;
import com.njj.njjsdk.callback.RecordingDataCallback;
import com.njj.njjsdk.entity.RecordingDataEntity;
import com.njj.njjsdk.manger.NjjProtocolHelper;
import com.njj.njjsdk.utils.LogUtil;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.*;

public class RecordChatHelper {

    private static final String TAG = RecordChatHelper.class.getSimpleName();
    private static volatile RecordChatHelper instance;

    private int type;
    private String lang1Key;
    private String lang2Str;

    private FileOutputStream mOutputStream;

    private static final String RECORD = "record";

    private static final String RECORD_SUFFIX_OPUS = ".opus";
    private static final String RECORD_SUFFIX_PCM = ".pcm";

    private String name;

    private RecordChatHelper(Context context) {
        this.context = context.getApplicationContext(); // Gunakan applicationContext untuk menghindari memory leak
    }

    private Context context;

    public static synchronized RecordChatHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (RecordChatHelper.class) {
                if (instance == null) {
                    instance = new RecordChatHelper(context);
                }
            }
        }
        return instance;
    }

    public void registerRecordingDataCallback() {
        CallBackManager.getInstance().registerRecordingDataCallback(recordingDataCallback);
    }

    public void unregisterRecordingDataCallback() {
        CallBackManager.getInstance().unregisterRecordingDataCallback(recordingDataCallback);
    }

    private final RecordingDataCallback.ICallBack recordingDataCallback = new RecordingDataCallback.ICallBack() {
        @Override
        public void onReceiveData(RecordingDataEntity recordingDataEntity) {
            int recordingType = recordingDataEntity.getType();
            if (recordingType == 0) {
                type = 0;
                // 0 support
                NjjProtocolHelper.getInstance().sendIsSupport(0);
            } else if (recordingType == 5) { // Voice translation

            } else if (recordingType == 1) {
                if (recordingDataEntity.getStatus() == 0) {
                    // Start recording
                    name = System.currentTimeMillis() + "_recordChat";
                    String fileName = name + RECORD_SUFFIX_OPUS;
                    saveFilePrepare(fileName);
                    LogUtil.e("File save path: " + name);
                } else if (recordingDataEntity.getStatus() == 1) {
                    LogUtil.e("End recording");
                    closeFileStream();
                    if (type == 0) {
                        // Voice recognition
                        speechRecognition(recordingDataEntity.getSession() == 0);
                    } else if (type == 5) {
                        // Voice translation

                    }
                }
            } else if (recordingType == 2) {
                saveData(recordingDataEntity.getVoiceData());
            }
        }
    };

    private void replyEmpty() {
        try {
            byte[] nullBytes = "".getBytes("unicode");
            NjjProtocolHelper.getInstance().sendTranslateContent(nullBytes);
            NjjProtocolHelper.getInstance().sendSpeechRecognitionContent(nullBytes, 0, 1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void speechRecognition(boolean newSession) {
        File file = new File(context.getExternalCacheDir(), RECORD + File.separator + name + RECORD_SUFFIX_OPUS);
        File outFile = new File(context.getExternalCacheDir(), RECORD + File.separator + name + RECORD_SUFFIX_PCM);
        OpusUtils.decodeOpusFile(file.getAbsolutePath(), outFile.getAbsolutePath(), s -> {
            Log.i(TAG, "speechRecognition: " + s);

            // Run Wit.ai speech-to-text in a background thread
            new Thread(() -> {
                String transcription = convertSpeechToTextWithWitAI(outFile);
                if (transcription != null) {
                    Log.i(TAG, "Transcription: " + transcription);

                    // Simulate problem
                    speechRecognitionChatGPT(transcription);
                } else {
                    Log.e(TAG, "Transcription failed or was not returned.");
                }
            }).start();
        }, null);
    }

    // Method to send audio to Wit.ai
    private String convertSpeechToTextWithWitAI(File audioFile) {
        OkHttpClient client = new OkHttpClient();
        String token = "Bearer SF4DSTOT3ISXDPOC46MU2HP7TRZPN4NW";

        try {
            // Create a request body for the PCM audio file
            RequestBody requestBody = RequestBody.create(
                    audioFile,
                    MediaType.parse("audio/raw")
            );

            // Build the HTTP request to Wit.ai
            Request request = new Request.Builder()
                    .url("https://api.wit.ai/speech?v=20250110")
                    .header("Authorization", token)
                    .header("Content-Type", "audio/raw")
                    .post(requestBody)
                    .build();

            // Execute the request synchronously
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonResponse = response.body().string();
                Log.i(TAG, "Wit.ai Response: " + jsonResponse);

                // Extract transcription
                JSONObject jsonObject = new JSONObject(jsonResponse);
                return jsonObject.optString("text", null);
            } else {
                Log.e(TAG, "Wit.ai API Response Error: " + response.code());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error sending audio to Wit.ai", e);
            gptErrorTip("Error sending audio");
        }
        return null; // Return null in case of an error
    }

    // Method to send audio to Wit.ai
    private String sendTextToAI(String question) {
        OkHttpClient client = new OkHttpClient();
        String token = "x45rRwduZ10T4mqE73fTyMny6aNhmQ8W2lhvE7ot40065";

        try {
            // Build the HTTP request to Wit.ai
            Request request = new Request.Builder()
                    .url("http://connect.dorangadget.com/api/chatbot?text=${question}&provider=gemini")
                    .header("token", token)
                    .get()
                    .build();

            // Execute the request synchronously
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonResponse = response.body().string();
                Log.i(TAG, "AI Response: " + jsonResponse);

                // Extract transcription
                JSONObject jsonObject = new JSONObject(jsonResponse);
                return jsonObject.optString("pesan", null);
            } else {
                Log.e(TAG, "AI API Response Error: " + response.code());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error sending text to JETE AI", e);
            gptErrorTip("Error sending text to AI");
        }
        return null; // Return null in case of an error
    }

    private void speechRecognitionChatGPT(String problem) {
        // Reply even if the input is empty
        if (TextUtils.isEmpty(problem)) {
            replyEmpty();
            return;
        }

        // Reply with the result of voice recognition on the phone
        try {
            byte[] bytes = problem.getBytes("unicode");
            // Send a maximum of 200 bytes
            if (bytes.length > 200) {
                bytes = Arrays.copyOf(bytes, 200);
            }
            NjjProtocolHelper.getInstance().sendTranslateContent(bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //call ChatGPT to get a reply.
        String reply = sendTextToAI(problem);
        try {
            sendSpeechRecognitionContent(reply);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void gptErrorTip(String errMessage) {
        try {
            sendSpeechRecognitionContent(errMessage);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void saveData(byte[] data) {
        if (mOutputStream != null) {
            try {
                mOutputStream.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveFilePrepare(String name) {
        // Close the file stream if open
        String outputFilePath = getNamePathAndCreate(name);
        try {
            mOutputStream = new FileOutputStream(outputFilePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getNamePathAndCreate(String name) {
        String outputFilePath = FileUtil.createCacheFilePath(context, RECORD) + File.separator + name;
        return outputFilePath;
    }

    private void closeFileStream() {
        if (mOutputStream != null) {
            try {
                mOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mOutputStream = null;
        }
    }

    public void sendSpeechRecognitionContent(String result) throws UnsupportedEncodingException {
        byte[] resultBytes = result.getBytes("unicode");
        List<byte[]> byteArray = splitByteArray(resultBytes);
        for (int i = 0; i < byteArray.size(); i++) {
            byte[] bytes = byteArray.get(i);
            NjjProtocolHelper.getInstance().sendSpeechRecognitionContent(bytes, i, byteArray.size());
        }
    }

    public List<byte[]> splitByteArray(byte[] input) {
        List<byte[]> result = new ArrayList<>();

        int length = input.length;
        int chunkSize = 200;

        for (int i = 0; i < 3 && i * chunkSize < length; i++) {
            int remainingLength = length - i * chunkSize;
            int currentChunkSize = Math.min(chunkSize, remainingLength);
            byte[] chunk = new byte[currentChunkSize];
            System.arraycopy(input, i * chunkSize, chunk, 0, currentChunkSize);
            result.add(chunk);
        }

        return result;
    }
}
