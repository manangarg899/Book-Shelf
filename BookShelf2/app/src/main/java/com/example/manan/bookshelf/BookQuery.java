package com.example.manan.bookshelf;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manan on 14-02-2017.
 */

public final class BookQuery {

    public static List<Book> fetchBookData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("Log message", "Problem making the HTTP request.", e);
        }
        List<Book> books = extractFeatueFromJson(jsonResponse);
        return books;
    }

    private static URL createUrl(String inputUrl) {
        URL url = null;

        try {
            url = new URL(inputUrl);
        } catch (MalformedURLException e) {
            Log.e("Log message", "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = null;

        if (url == null)
            return jsonResponse;

        HttpURLConnection urlConnection = null;

        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("log message", "Error Response Code" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();

            if (inputStream != null)
                inputStream.close();
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    private static List<Book> extractFeatueFromJson(String BookJson) {
        if (TextUtils.isEmpty(BookJson))
            return null;

        List<Book> books = new ArrayList<Book>();

        try {
            JSONObject baseJsonResponse = new JSONObject(BookJson);

            JSONArray bookArray = baseJsonResponse.getJSONArray("items");

            for (int i = 0; i < bookArray.length(); i++) {

                JSONObject currentBook = bookArray.getJSONObject(i);

                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                String title = volumeInfo.getString("title");

                String authors = "No Author Found";

                if (volumeInfo.has("authors")) {
                    JSONArray authorArray = volumeInfo.getJSONArray("authors");
                    authors = authorArray.getString(0);
                }

                String url = volumeInfo.getString("infoLink");

                String imageUrl = "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQOF6YZ8M0KhCq_HFv7yjXEXWlQfYPD3v80B8Nxs_lbHApPh0Ps";

                if (volumeInfo.has("imageLinks")) {
                    JSONObject imageObj = volumeInfo.getJSONObject("imageLinks");
                    imageUrl = imageObj.getString("smallThumbnail");
                }
                Log.i("author", authors);
                Log.i("title", title);
                Log.i("imageUrl", imageUrl);
                Book book = new Book(authors, title, url, imageUrl);
                books.add(book);

            }


        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return books;
    }
}
