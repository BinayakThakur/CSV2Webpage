package custom.processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyLoader {
	Properties props=new Properties();
	public void load() throws FileNotFoundException, IOException {
		   props.load(new FileInputStream("meta.properties"));
	}
	public String getProperty(String key) {
		return props.getProperty(key);}
	public PropertyLoader(){
		try {
			load();
		} catch (FileNotFoundException e) {
			File file=new File(getProperty("meta.properties"));
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
