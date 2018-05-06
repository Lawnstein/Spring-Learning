package com.iceps.spring.redis.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * JDK序列化.
 * 
 * @author Lawnstein
 * @version $Revision:$
 */
public class JDKObjectSerializer {

	/**
	 * 序列化
	 * 
	 * @param t
	 * @return
	 */
	public static byte[] serialize(Object t) {
		ObjectOutputStream out = null;
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			out = new ObjectOutputStream(bout);
			out.writeObject(t);
			out.flush();
			byte[] target = bout.toByteArray();
			return target;
		} catch (IOException e) {
			throw new RuntimeException("JDKMsgSerializer serialize " + (t == null ? "nvl" : t.getClass()) + " exception", e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * 反序列化
	 * 
	 * @param bytes
	 * @return
	 */
	public static <T> T deserialize(byte[] bytes) {
		try {
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
			return (T) in.readObject();
		} catch (IOException e) {
			throw new RuntimeException("JDKMsgSerializer deserialize exception", e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("JDKMsgSerializer deserialize exception", e);
		}
	}

	public static void main(String[] args) {
		Map m = new HashMap();
		m.put("a", "a");
		m.put("b", "b");
		m.put("c", "c");
		System.out.println(m);
		byte[] out = JDKObjectSerializer.serialize(m);
		System.out.println("outBytes.size=" + out.length + ":" + new String(out));
		Object n = JDKObjectSerializer.deserialize(out);
		System.out.println(n);
	}
}
