package org.bukkit.fillr;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Grabs the latest info for a given plugin from fill.bukkit.org
 */
public class FillReader {
    // TODO change this to what it will actually be...

    private static final String BASE_URL = "http://taylorkelly.me/pnfo.php";
    private String currVersion;
    private String file;
    private String name;
    private String notes;
    private boolean stable;

    public FillReader(String name) {
        try {
            String result = "";

            try {
                URL url = new URL(BASE_URL + "?name=" + name);

                System.out.println(BASE_URL + "?name=" + name);
                URLConnection conn = url.openConnection();
                StringBuilder buf = new StringBuilder();
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;

                while ((line = rd.readLine()) != null) {
                    buf.append(line);
                }
                result = buf.toString();
                rd.close();
                JSONParser parser = new JSONParser();
                Object obj;

                obj = parser.parse(result);
                JSONObject jsonObj = (JSONObject) obj;

                this.currVersion = (String) jsonObj.get("plugin_version");
                this.name = (String) jsonObj.get("plugin_name");
                this.file = (String) jsonObj.get("plugin_file");
                this.stable = (Boolean) jsonObj.get("plugin_stable");
                this.notes = (String) jsonObj.get("plugin_notes");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCurrVersion() {
        return currVersion;
    }

    public String getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public String getNotes() {
        return notes;
    }

    public void setStable(boolean stable) {
        this.stable = stable;
    }

    public boolean isStable() {
        return stable;
    }
}
