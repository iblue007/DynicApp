package com.felink.corelib.kitset.once;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class PersistedSet {

    private static final String STRING_SET_KEY = "PersistedSetValues";
    private static final String DELIMITER = ",";

    private SharedPreferences preferences;
    private Set<String> set = new HashSet<String>();

    private final AsyncSharedPreferenceLoader preferenceLoader;

    PersistedSet(Context context, String setName) {
        String preferencesName = "PersistedSet".concat(setName);
        preferenceLoader = new AsyncSharedPreferenceLoader(context, preferencesName);
    }

    private void waitForLoad() {
        if (preferences == null) {
            preferences = preferenceLoader.get();

            if (preferences == null) {
                return;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                set = preferences.getStringSet(STRING_SET_KEY, new HashSet<String>());
            } else {
                String setString = preferences.getString(STRING_SET_KEY, null);
                set = new HashSet<String>(StringToStringSet(setString));
            }
        }
    }

    void put(String tag) {
        waitForLoad();

        set.add(tag);
        updatePreferences();
    }

    boolean contains(String tag) {
        waitForLoad();

        return set.contains(tag);
    }

    void remove(String tag) {
        waitForLoad();

        set.remove(tag);
        updatePreferences();
    }

    public void clear() {
        waitForLoad();

        set.clear();
        updatePreferences();
    }

    private void updatePreferences() {
        SharedPreferences.Editor edit = preferences.edit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            edit.putStringSet(STRING_SET_KEY, set);
        } else {
            edit.putString(STRING_SET_KEY, StringSetToString(set));
        }
        spCommit(edit);
    }

    private String StringSetToString(Set<String> set) {
        StringBuilder stringBuilder = new StringBuilder();
        String loopDelimiter = "";

        for (String s : set) {
            stringBuilder.append(loopDelimiter);
            stringBuilder.append(s);

            loopDelimiter = DELIMITER;
        }

        return stringBuilder.toString();
    }

    /**
     * @param setString not null
     * @return
     */
    private Set<String> StringToStringSet(String setString) {
        if (setString == null) {
            return new HashSet<String>();
        }

        return new HashSet<String>(Arrays.asList(setString.split(DELIMITER)));
    }

    private void spCommit(SharedPreferences.Editor editor) {
        if (editor != null) {
            editor.commit();
//            if(Build.VERSION.SDK_INT < 9){
//                editor.commit();
//            } else {
//                editor.apply();
//            }
        }
    }
}
