package br.mackenzie.mackleaps.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

public class Marshalling {

	private Marshalling() {
	}
	public static String marshalInfo(Object info) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
		return mapper.writeValueAsString(info);
	}

	public static <T> T unmarshalInfo(String infoJson, Class<T> className) throws IOException {
		if (infoJson == null) {
			infoJson = "null";
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
		return mapper.readValue(infoJson, className);
	}
	
}
