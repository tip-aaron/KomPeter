package com.github.ragudos.kompeter.utilities.io.load;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LoadDb extends Load{
	
	public LoadDb(String path) {
		find(path);
	}
	
	public String load() {
		byte[] encoded;
		String text = null;
		try {
			encoded = getInputStream().readAllBytes();
			text = new String(encoded, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}
}
