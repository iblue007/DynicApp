package com.felink.corelib.kitset.once;

import android.content.Context;
import android.content.SharedPreferences;

import com.felink.corelib.kitset.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class PersistedMap {

    private static final String DELIMITER = ",";

    private SharedPreferences preferences;
    private final Map<String, List<Long>> map = new ConcurrentHashMap<String, List<Long>>();
    private final AsyncSharedPreferenceLoader preferenceLoader;

    PersistedMap(Context context, String mapName) {
        String preferencesName = "PersistedMap".concat(mapName);
        preferenceLoader = new AsyncSharedPreferenceLoader(context, preferencesName);
    }

    private void waitForLoad() {
        if (preferences != null) {
            return;
        }

        preferences = preferenceLoader.get();
        if (preferences == null) {
            return;
        }
        Map<String, ?> allPreferences = preferences.getAll();

        for (String key : allPreferences.keySet()) {

            List<Long> values;
            try {
                values = stringToList(preferences.getString(key, null));
            } catch (ClassCastException exception) {
                values = loadFromLegacyStorageFormat(key);
            }

            map.put(key, values);
        }
    }

    private List<Long> loadFromLegacyStorageFormat(String key) {
        long value = preferences.getLong(key, -1);
        List<Long> values = new ArrayList<Long>(1);
        values.add(value);

        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, listToString(values));
            spCommit(editor);
        }

        return values;
    }

    List<Long> get(String tag) {
        waitForLoad();

        List<Long> longs = map.get(tag);
        if (longs == null) {
            return Collections.emptyList();
        }
        return longs;
    }

    void put(String tag, long timeSeen) {
        waitForLoad();

        List<Long> lastSeenTimeStamps = map.get(tag);
        if (lastSeenTimeStamps == null) {
            lastSeenTimeStamps = new ArrayList<Long>(1);
        }
        lastSeenTimeStamps.add(timeSeen);

        map.put(tag, lastSeenTimeStamps);
        if (preferences != null) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString(tag, listToString(lastSeenTimeStamps));
            spCommit(edit);
        }
    }

    void remove(String tag) {
        waitForLoad();

        map.remove(tag);
        if (preferences != null) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.remove(tag);
            spCommit(edit);
        }
    }

    public void clear() {
        waitForLoad();

        map.clear();
        if (preferences != null) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.clear();
            spCommit(edit);
        }
    }

    private String listToString(List<Long> list) {
        StringBuilder stringBuilder = new StringBuilder();
        String loopDelimiter = "";

        for (Long l : list) {
            stringBuilder.append(loopDelimiter);
            stringBuilder.append(l);

            loopDelimiter = DELIMITER;
        }

        return stringBuilder.toString();
    }

    private List<Long> stringToList(String stringList) {
        if (StringUtil.isEmpty(stringList)) {
            return Collections.emptyList();
        }

        String[] strings = stringList.split(DELIMITER);
        List<Long> list = new ArrayList<Long>(strings.length);

        for (String stringLong : strings) {
            list.add(Long.parseLong(stringLong));
        }

        return list;
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
