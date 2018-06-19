package com.trgr.dockets.core.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class JaxbMarshallingUtil {
	
	public static String marshal(Object obj) {
		Writer writer = new StringWriter();
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
			Marshaller marshller = jaxbContext.createMarshaller();
			marshller.marshal(obj, writer);
			return writer.toString();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		} finally {
			try { writer.close(); }
			catch (IOException e) { throw new RuntimeException(e); }
		}
	}

	public static Object unmarshal(String xml, Class<?> clazz) {
		StringReader reader = new StringReader(xml);
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
			Unmarshaller unmarshller = jaxbContext.createUnmarshaller();
			return unmarshller.unmarshal(reader);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		} finally {
			reader.close();
		}
	}
}
