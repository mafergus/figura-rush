package com.addressunknowngames.shapeninja.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

/**
 * Created by matthewferguson on 2/28/18.
 */

@IgnoreExtraProperties
public class Quest {
    public final String id;
    public final String title;
    public final String description;

    public Quest() {
        this.id = null;
        this.title = null;
        this.description = null;
    }

    public Quest(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public static Quest fromMap(String id, HashMap<String, Object> questMap) {
        String title = (String)questMap.get("title");
        String description = (String)questMap.get("description");

        return new Quest(id, title, description);
    }
}
