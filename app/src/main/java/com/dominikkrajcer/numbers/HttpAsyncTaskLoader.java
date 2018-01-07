package com.dominikkrajcer.numbers;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by Dominik on 05.01.2018.
 */

public class HttpAsyncTaskLoader extends AsyncTaskLoader<String> {

    private String url;

    public HttpAsyncTaskLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public String loadInBackground() {
        HttpHandler httpHandler = new HttpHandler();
        return httpHandler.makeServiceCall(url);
    }
}
