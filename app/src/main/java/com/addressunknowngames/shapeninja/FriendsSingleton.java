//package com.phoenixunknownapps.figurarushextreme;
//
//import android.content.ContentResolver;
//import android.content.Context;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.provider.ContactsContract;
//import android.util.Log;
//
//import com.parse.FindCallback;
//import com.parse.ParseException;
//import com.parse.ParseObject;
//import com.parse.ParseQuery;
//import com.parse.ParseUser;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by matthewferguson on 10/30/15.
// */
//public class FriendsSingleton {
//
//    private static volatile FriendsSingleton instance = null;
//
//    private Map<String, String> friendsMap = new HashMap<String, String>();
//    private List<FriendScorePair> friendHighScores = new ArrayList<FriendScorePair>();
//
//    public static class FriendScorePair {
//        final public ParseObject friend;
//        final public String number;
//
//        public FriendScorePair(final ParseObject friend, final String number) {
//            this.friend = friend;
//            this.number = number;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//
//            FriendScorePair that = (FriendScorePair) o;
//
//            return !(friend != null ? !friend.equals(that.friend) : that.friend != null);
//
//        }
//
//        @Override
//        public int hashCode() {
//            return friend != null ? friend.hashCode() : 0;
//        }
//    }
//
//    private FriendsSingleton() {
//    }
//
//    public static FriendsSingleton getInstance() {
//        if (instance == null) {
//            synchronized (FriendsSingleton.class) {
//                if (instance == null) {
//                    instance = new FriendsSingleton();
//                }
//            }
//        }
//        return instance;
//    }
//
//    public void loadFriendScores(final Context context) {
//
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//
//                String phoneNumber = null;
//                String email = null;
//
//                Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
//                String _ID = ContactsContract.Contacts._ID;
//                String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
//                String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
//
//                Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
//                String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
//                String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
//
//                Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
//                String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
//                String DATA = ContactsContract.CommonDataKinds.Email.DATA;
//
//                ContentResolver contentResolver = context.getContentResolver();
//                Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);
//                if (cursor.getCount() > 0) {
//                    while (cursor.moveToNext()) {
//                        String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
//                        String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
//
//                        int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
//                        if (hasPhoneNumber > 0) {
//                            // Query and loop for every phone number of the contact
//                            Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);
//                            while (phoneCursor.moveToNext()) {
//                                phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
//                                phoneNumber = phoneNumber.replace("+", "").replace("(", "").replace(")", "").replace(" ", "").replace("-", "");
//                                friendsMap.put(phoneNumber, name);
//                                Log.v("MNF", "contact name: " + name + " number: " + phoneNumber);
//                            }
//                            phoneCursor.close();
//
//                            // Query and loop for every email of the contact
//                            Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?", new String[]{contact_id}, null);
//                            while (emailCursor.moveToNext()) {
//                                email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
//                            }
//                            emailCursor.close();
//                        }
//                    }
//                }
//                ParseQuery<ParseUser> friendsQ = ParseUser.getQuery();
//                friendsQ.whereContainedIn("username", friendsMap.keySet());
//                friendsQ.findInBackground(new FindCallback<ParseUser>() {
//                    @Override
//                    public void done(List<ParseUser> objects, ParseException e) {
//                        if (e == null) {
//                            Log.v("MNF", "found " + objects.size() + " friends!");
//
//                            ParseQuery<ParseObject> friendQuery = ParseQuery.getQuery("HighScore");
//                            friendQuery.whereContainedIn("user", objects);
//                            friendQuery.include("user");
//                            friendQuery.orderByDescending("score");
//                            friendQuery.findInBackground(new FindCallback<ParseObject>() {
//                                @Override
//                                public void done(final List<ParseObject> objects, ParseException e) {
//                                    if (e == null) {
//                                        Log.v("MNF", "found " + objects.size() + " friends!");
//                                        for (ParseObject highScore : objects) {
//                                            ParseObject user = (ParseObject) highScore.get("user");
//                                            String contactDisplayName = friendsMap.get(user.getString("username"));
//                                            Log.v("MNF", "username: " + user.getString("username") + " contact name: " + contactDisplayName);
//                                            friendHighScores.add(new FriendScorePair(highScore, contactDisplayName));
//                                        }
//                                    } else {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            });
//                        } else {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        });
//    }
//
//    public List<FriendScorePair> getFriendHighScores() {
//        return new ArrayList<FriendScorePair>(friendHighScores);
//    }
//
//}