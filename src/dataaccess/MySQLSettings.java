package dataaccess;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MySQLSettings {

	private static String url;
	private static String username;
	private static String pass;
	private static String[] infoArr;

	public static void parse() throws FileNotFoundException, IOException, ParseException {
		Object obj = new JSONParser().parse(new FileReader("mysqlsettings.json"));

		JSONObject jobj = (JSONObject) obj;

		url = (String) jobj.get("url");
		username = (String) jobj.get("username");
		pass = (String) jobj.get("password");
		infoArr = new String[] { url, username, pass };
	}

	public static String[] getInfo() {
		return infoArr;
	}

}
