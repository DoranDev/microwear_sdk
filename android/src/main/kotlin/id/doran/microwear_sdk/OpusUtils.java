
package id.doran.microwear_sdk;


import androidx.core.util.Consumer;

import com.jieli.jl_audio_decode.callback.OnStateCallback;
import com.jieli.jl_audio_decode.exceptions.OpusException;
import com.jieli.jl_audio_decode.opus.OpusManager;

public class OpusUtils {

    private static final String TAG=OpusUtils.class.getSimpleName();

    public static void decodeOpusFile(String inFilePath, String outFilePath, Consumer<String> consumer, Consumer<String> onError) {
        OpusManager opusManager = null;
        try {
            opusManager = new OpusManager();
        } catch (OpusException e) {
            e.printStackTrace();
            if (onError!=null)
                onError.accept(e.getMessage());
            return;
        }

        OpusManager finalOpusManager = opusManager;
        opusManager.decodeFile(inFilePath, outFilePath, new OnStateCallback() {
            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(String s) {
                finalOpusManager.release();
                if (consumer!=null)
                    consumer.accept(s);
            }

            @Override
            public void onError(int i, String s) {
                finalOpusManager.release();
                if (onError!=null)
                    onError.accept(s);
            }
        });
    }
}
