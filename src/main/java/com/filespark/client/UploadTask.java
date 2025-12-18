package com.filespark.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import javafx.concurrent.Task;
import java.util.concurrent.Flow;

public class UploadTask extends Task<Void>{
    
    private final File file;
    private final String presignedUrl;
    private final String mime;
    private final int kiloByte = 1024;
    private static final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

    public UploadTask(File file, String presignedUrl, String mime) {

        this.file = file;
        this.presignedUrl = presignedUrl;
        this.mime = mime;

    }

    @Override
    protected Void call() throws Exception {

        long fileBytes = file.length();

        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(presignedUrl)).header("Content-Type", mime).PUT(HttpRequest.BodyPublishers.ofFile(file.toPath())).build();
        HttpResponse<Void> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.discarding());

        // 200 : ok 204: success no content
        if(response.statusCode() != 200 && response.statusCode() != 204) {

            throw new IOException("upload task failed: HTTP " + response.statusCode());

        }

        updateMessage(file.getName() + " uploaded!");
        updateProgress(fileBytes, fileBytes);
        return null;

    }

    // @todo: we need file progress
    private HttpRequest.BodyPublisher streamFileProgress(File file, long totalBytes){

        return new HttpRequest.BodyPublisher() {

            @Override
            public long contentLength() {

                return file.length();

            }

            @Override
            public void subscribe (Flow.Subscriber<? super ByteBuffer> subscriber){

                subscriber.onSubscribe(new Flow.Subscription() {
                    
                    private long uploaded = 0;
                    private volatile boolean cancelled = false;
                    private FileInputStream inputStream;

                    {

                        try { inputStream = new FileInputStream(file); }
                        catch (IOException exception) { subscriber.onError(exception); }

                    }

                    @Override
                    public void request (long n) {

                        if (this.cancelled || n <= 0) return;
                        
                        try {

                            byte[] buffer = new byte[32 * kiloByte];
                            int read;
                            while (n-- > 0 && !cancelled && (read = inputStream.read(buffer)) != -1){

                                uploaded += read;
                                subscriber.onNext(ByteBuffer.wrap(buffer, 0, read));
                                updateProgress(uploaded, totalBytes);
                                updateMessage(String.format("%d / %d bytes", uploaded, totalBytes));

                                if (isCancelled()) {

                                    cancel();
                                    return;

                                }

                            }

                            if (!cancelled && inputStream.read() == -1) subscriber.onComplete();

                        }
                        catch (IOException exception){

                            subscriber.onError(exception);
                        
                        }
                        
                    }

                    @Override
                    public void cancel() {

                        this.cancelled = true;
                        try { 

                            if (inputStream != null) inputStream.close();

                        }
                        catch (IOException exception) {}

                    }

                });

            }


        };
    
    }

}
