package com.floridakeys.autocomplete;

/**
 * @description     Json Parse From Network
 *
 */
import com.floridakeys.network.netConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonParse {

    public JsonParse(){}

    public List<SuggestGetSet> getParseJsonServer(SuggestionType type, String keyword)
    {
        List<SuggestGetSet> ListData = new ArrayList<SuggestGetSet>();
        try {
            String temp = keyword.replace(" ", "%20");
            String url = "";
            if (type == SuggestionType.ARTIST)
                url = String.format("%s?term=%s", netConfig.SERVICE_GET_KEYWORD_ARTIST, temp);
            else if (type == SuggestionType.VENUE)
                url = String.format("%s?term=%s", netConfig.SERVICE_GET_KEYWORD_VENUES, temp);
            else if (type == SuggestionType.EVENT)
                url = String.format("%s?term=%s", netConfig.SERVICE_GET_KEYWORD_EVENT, temp);

            URL js = new URL(url);

            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();

            // Parse key array
            JSONArray jsonArray = new JSONArray(line);
            for(int i = 0; i < jsonArray.length(); i++){
                String value = jsonArray.getString(i);
                ListData.add(new SuggestGetSet(value));
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return ListData;

    }

}
