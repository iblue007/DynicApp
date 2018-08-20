package com.felink.corelib.kitset.once;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

class AsyncSharedPreferenceLoader {

    private final Context context;

    private final AsyncTask<String, Void, SharedPreferences> asyncTask = new AsyncTask<String, Void, SharedPreferences>() {
        @Override
        protected SharedPreferences doInBackground(String... names) {
            return context.getSharedPreferences(names[0], Context.MODE_PRIVATE);
        }
    };

    AsyncSharedPreferenceLoader(Context context, String name) {
        this.context = context;
        asyncTask.execute(name);
    }

    SharedPreferences get() {
        try {
            return asyncTask.get();
        } catch (Exception e) {
            return null;
        }
    }
}
