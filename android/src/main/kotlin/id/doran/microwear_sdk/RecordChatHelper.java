package id.doran.microwear_sdk;

import android.text.TextUtils;
import android.util.Log;

import com.arthenica.ffmpegkit.FFmpegKit;
import com.njj.njjsdk.callback.CallBackManager;
import com.njj.njjsdk.callback.RecordingDataCallback;
import com.njj.njjsdk.entity.RecordingDataEntity;
import com.njj.njjsdk.manger.NjjProtocolHelper;
import com.njj.njjsdk.utils.LogUtil;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    private static final String RECORD_SUFFIX_WAV = ".wav";

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
        File wavFile = new File(context.getExternalFilesDir(null), RECORD + File.separator + name + RECORD_SUFFIX_WAV);

        // Decode OPUS to WAV using FFmpegKit
        OpusUtils.decodeOpusFile(file.getAbsolutePath(),outFile.getAbsolutePath(),s -> {
            Log.i(TAG, "decodeOpusFile: "+s);
            convertPcmToWav(outFile, wavFile, result -> {
                Log.i(TAG, "decodeOpusToWav: " + result);

                // Run Wit.ai speech-to-text in a background thread
                new Thread(() -> {
                    String transcription = convertSpeechToTextWithWitAI(wavFile);
                    if (transcription != null) {
                        Log.i(TAG, "Transcription: " + transcription);

                        // Simulate problem
                        speechRecognitionChatGPT(transcription);
                    } else {
                        Log.e(TAG, "Transcription failed or was not returned.");
                    }
                }).start();
            }, error -> {
                Log.e(TAG, "Error occurred: " + error);
            });
        },null);

    }



//    private void convertPcmToWav(File pcmFile, File wavFile, OnFinishCallback callback, OnErrorCallback errorCallback) {
//        // Ensure PCM file exists
//        if (!pcmFile.exists()) {
//            Log.e(TAG, "PCM file not found");
//            errorCallback.onError("PCM file not found.");
//            return;
//        }
//
//        // Use more explicit FFmpeg parameters
//        String command = "-f s16le -ar 44100 -ac 2 -i " + pcmFile.getAbsolutePath() +
//                " -acodec pcm_s16le -ar 16000 " + // Convert to 16kHz for better STT
//                wavFile.getAbsolutePath();
//
//        // Execute FFmpegKit command
//        FFmpegKit.executeAsync(command, session -> {
//            if (session.getReturnCode().isSuccess()) {
//                Log.i(TAG, "PCM to WAV conversion completed.");
//                callback.onFinish("PCM to WAV conversion completed.");
//            } else {
//                Log.e(TAG, "Error converting PCM to WAV: " + session.getFailStackTrace());
//                String errorMessage = session.getFailStackTrace();
//                errorCallback.onError("Error converting PCM to WAV: " + errorMessage);
//            }
//        });
//    }

    private void convertPcmToWav(File pcmFile, File wavFile, OnFinishCallback callback, OnErrorCallback errorCallback) {
        // Validasi file PCM
        if (pcmFile == null || !pcmFile.exists() || !pcmFile.isFile()) {
            Log.e(TAG, "PCM file not found or is invalid.");
            if (errorCallback != null) {
                errorCallback.onError("PCM file not found or is invalid.");
            }
            return;
        }

        // Proses konversi manual ke WAV seperti pada kode sebelumnya
        try {
//            int channelCount = 2; // Contoh
//            int sampleRate = 44100; // Contoh
//            int bitsPerSample = 16; // Contoh

            int channelCount = 1;  // Mono
            int sampleRate = 16000; // 16kHz (bisa 44100 Hz juga, tergantung kebutuhan)
            int bitsPerSample = 16; // 16-bit audio

            // Konversi PCM ke WAV
            int inputSize = (int) pcmFile.length();
            try (FileInputStream pcmInputStream = new FileInputStream(pcmFile);
                 FileOutputStream wavOutputStream = new FileOutputStream(wavFile)) {

                writeWavHeader(wavOutputStream, inputSize, channelCount, sampleRate, bitsPerSample);
                copy(pcmInputStream, wavOutputStream);
            }

            if (callback != null) {
                callback.onFinish("PCM to WAV conversion completed successfully.");
            }
        } catch (IOException e) {
            Log.e(TAG, "Error during PCM to WAV conversion.", e);
            if (errorCallback != null) {
                errorCallback.onError("Error during PCM to WAV conversion: " + e.getMessage());
            }
        }
    }

    // Menulis header WAV
    private void writeWavHeader(OutputStream output, int inputSize, int channelCount, int sampleRate, int bitsPerSample) throws IOException {
        // Ukuran data WAV
        int byteRate = sampleRate * channelCount * bitsPerSample / 8;
        int blockAlign = channelCount * bitsPerSample / 8;
        int dataSize = inputSize;
        int totalSize = 36 + dataSize;

        // RIFF Header
        writeString(output, "RIFF");
        writeInt(output, totalSize);
        writeString(output, "WAVE");

        // Format Subchunk
        writeString(output, "fmt ");
        writeInt(output, 16); // Subchunk size
        writeShort(output, (short) 1); // Audio format (1 = PCM)
        writeShort(output, (short) channelCount);
        writeInt(output, sampleRate);
        writeInt(output, byteRate);
        writeShort(output, (short) blockAlign);
        writeShort(output, (short) bitsPerSample);

        // Data Subchunk
        writeString(output, "data");
        writeInt(output, dataSize);
    }

    // Menulis string ke output
    private void writeString(OutputStream output, String data) throws IOException {
        for (int i = 0; i < data.length(); i++) {
            output.write(data.charAt(i));
        }
    }

    // Menulis integer dalam format little-endian
    private void writeInt(OutputStream output, int data) throws IOException {
        output.write(data & 0xFF);
        output.write((data >> 8) & 0xFF);
        output.write((data >> 16) & 0xFF);
        output.write((data >> 24) & 0xFF);
    }

    // Menulis short dalam format little-endian
    private void writeShort(OutputStream output, short data) throws IOException {
        output.write(data & 0xFF);
        output.write((data >> 8) & 0xFF);
    }

    // Menyalin data dari InputStream ke OutputStream
    private void copy(InputStream source, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = source.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    // Callback interfaces for success and error
    @FunctionalInterface
    interface OnFinishCallback {
        void onFinish(String message);
    }

    @FunctionalInterface
    interface OnErrorCallback {
        void onError(String errorMessage);
    }

    private String convertSpeechToTextWithWitAI(File audioFile) {
        OkHttpClient client = new OkHttpClient();
        String token = "Bearer SF4DSTOT3ISXDPOC46MU2HP7TRZPN4NW";
        String TAG = "WitAI";

        try {
            // Debug: Periksa file audio
            if (!audioFile.exists() || audioFile.length() == 0) {
                Log.e(TAG, "Audio file is missing or empty.");
                return null;
            }
            Log.d(TAG, "Audio file path: " + audioFile.getAbsolutePath());
            Log.d(TAG, "Audio file size: " + audioFile.length() + " bytes");

            // Create a request body for the PCM audio file
            RequestBody requestBody = RequestBody.create(
                    audioFile,
                    MediaType.parse("audio/wav")
            );

            // Build the HTTP request to Wit.ai
            Request request = new Request.Builder()
                    .url("https://api.wit.ai/speech?v=20250110&verbose=true")
                    .header("Authorization", token)
                    .header("Content-Type", "audio/wav")
                    .post(requestBody)
                    .build();

            Log.d(TAG, "Request headers: " + request.headers().toString());

            // Execute the request synchronously
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                String jsonResponse = response.body().string();
                Log.i(TAG, "Wit.ai Response: " + jsonResponse);

                // Parse the JSON response to get the transcription text
                String finalTranscription = getFinalTranscription(jsonResponse);
                if (finalTranscription != null && !finalTranscription.isEmpty()) {
                    return finalTranscription; // Return the transcription text
                } else {
                    Log.e(TAG, "Transcription failed or was not returned.");
                }
            } else {
                if (response.body() != null) {
                    String errorResponse = response.body().string();
                    Log.e(TAG, "Wit.ai Error Response: " + errorResponse);
                } else {
                    Log.e(TAG, "Response body is null.");
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error sending audio to Wit.ai", e);
        }
        return null; // Return null in case of an error
    }
    private String getFinalTranscription(String jsonResponse) {
        try {
            // Parse JSON response as a JSONObject
            JSONObject jsonObject = new JSONObject(jsonResponse);

            // Periksa apakah ini adalah transkripsi final
            if ("FINAL_TRANSCRIPTION".equals(jsonObject.optString("type"))) {
                return jsonObject.optString("text", ""); // Ambil teks final
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing JSON response", e);
        }
        return null; // Return null jika tidak ada transkripsi final ditemukan
    }
    // Method to send audio to Wit.ai
    private String sendTextToAI(String question) {
        OkHttpClient client = new OkHttpClient();
        String token = "x45rRwduZ10T4mqE73fTyMny6aNhmQ8W2lhvE7ot40065";

        try {
            // Build the HTTP request to Wit.ai
            Request request = new Request.Builder()
                    .url("https://api.jeteconnect.id/api/chatbot?text=${question}&provider=gemini")
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
