package com.github.ragudos.kompeter.utilities.io.load;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Load {
	
	private InputStream in;
	
	public void find(String path) {
		try {
			in = Load.class.getClassLoader().getResourceAsStream(path);
			if(in == null) {
				throw new FileNotFoundException("File not found:" + path);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public InputStream getInputStream() {
		return in;
	}

}
