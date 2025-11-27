package com.example.musicstorehn.network;

// File: app/src/main/java/com/uth/musicstorehn/network/UploadRequestBody.java

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class UploadRequestBody extends RequestBody {
    private File file;
    private String contentType;
    private UploadCallback callback;

    public interface UploadCallback {
        void onProgressUpdate(int percentage);
    }

    public UploadRequestBody(File file, String contentType, UploadCallback callback) {
        this.file = file;
        this.contentType = contentType;
        this.callback = callback;
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse(contentType);
    }

    @Override
    public long contentLength() {
        return file.length();
    }

    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
        long fileLength = file.length();
        byte[] buffer = new byte[4096];
        FileInputStream in = new FileInputStream(file);
        long uploaded = 0;

        try {
            int read;
            Handler handler = new Handler(Looper.getMainLooper());
            while ((read = in.read(buffer)) != -1) {
                uploaded += read;
                sink.write(buffer, 0, read);

                final long finalUploaded = uploaded;
                handler.post(() -> {
                    if (callback != null) {
                        int percentage = (int) (100 * finalUploaded / fileLength);
                        callback.onProgressUpdate(percentage);
                    }
                });
            }
        } finally {
            in.close();
        }
    }
}
