package com.iceps.spring.redis.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Kryo序列化.
 * 
 * @author Lawnstein
 * @version $Revision:$
 */
public class KryoObjectSerializer {
	public static final int MIN_BUFFSIZE = 1024 * 10; /* 10K */
	public static final int MID_BUFFSIZE = 1024 * 1024 * 10; /* 10M */
	public static final int MAX_BUFFSIZE = 1024 * 1024 * 1024; /* 1G */
	public static final int SHORT_BYTES_LENGTH = 2;
	public static final int INT_BYTES_LENGTH = 4;
	public static final int LONG_BYTES_LENGTH = 8;

	private static ThreadLocal<Kryo> threadLocalKryo = new ThreadLocal<Kryo>() {
		protected Kryo initialValue() {
			Kryo kryo = new Kryo();
			kryo.register(List.class);
			kryo.register(ArrayList.class);
			kryo.register(Map.class);
			kryo.register(HashMap.class);
			kryo.register(String.class);
			kryo.register(byte.class);
			kryo.register(short.class);
			kryo.register(Short.class);
			kryo.register(int.class);
			kryo.register(Integer.class);
			kryo.register(long.class);
			kryo.register(Long.class);
			kryo.register(float.class);
			kryo.register(Float.class);
			kryo.register(double.class);
			kryo.register(Double.class);
			kryo.register(BigDecimal.class);
			return kryo;
		};
	};

	public static Kryo getKryo() {
		return threadLocalKryo.get();
	}

	/**
	 * 序列化
	 * 
	 * @param t
	 * @return
	 */
	public static byte[] serialize(Object t) {
		Kryo kryo = getKryo();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Output output = new Output(MIN_BUFFSIZE, MAX_BUFFSIZE);
		output.setOutputStream(baos);
		kryo.writeClassAndObject(output, t);
		output.flush();
		output.close();
		byte[] b = baos.toByteArray();
		try {
			baos.flush();
			baos.close();
		} catch (IOException e) {
			throw new RuntimeException("close ByteArrayOutputStream on serialize IOException", e);
		}
		return b;
	}

	/**
	 * 反序列化
	 * 
	 * @param bytes
	 * @return
	 */
	public static <T> T deserialize(byte[] bytes) {
		Kryo kryo = getKryo();
		// Input input = new Input(MID_BUFFSIZE);
		Input input = new Input();
		input.setBuffer(bytes);
		T t = (T) kryo.readClassAndObject(input);
		input.close();
		return t;
	}

	/**
	 * 深度克隆
	 * 
	 * @param t
	 * @return
	 */
	public static <T> T deepClone(T t) {
		Kryo kryo = getKryo();
		try {
			Output output = new Output(new ByteArrayOutputStream());
			kryo.writeObject(output, t);
			Input input = new Input(output.getBuffer());
			return kryo.readObject(input, (Class<T>) t.getClass());
		} catch (Exception e) {
			throw new RuntimeException("deep clone fail", e);
		}
	}

	// 以下 是整型数 和 网络字节序的 byte[] 数组之间的转换
	public static byte[] longToBytes(long n) {
		byte[] b = new byte[8];
		b[7] = (byte) (n & 0xff);
		b[6] = (byte) (n >> 8 & 0xff);
		b[5] = (byte) (n >> 16 & 0xff);
		b[4] = (byte) (n >> 24 & 0xff);
		b[3] = (byte) (n >> 32 & 0xff);
		b[2] = (byte) (n >> 40 & 0xff);
		b[1] = (byte) (n >> 48 & 0xff);
		b[0] = (byte) (n >> 56 & 0xff);
		return b;
	}

	public static void longToBytes(long n, byte[] array, int offset) {
		array[7 + offset] = (byte) (n & 0xff);
		array[6 + offset] = (byte) (n >> 8 & 0xff);
		array[5 + offset] = (byte) (n >> 16 & 0xff);
		array[4 + offset] = (byte) (n >> 24 & 0xff);
		array[3 + offset] = (byte) (n >> 32 & 0xff);
		array[2 + offset] = (byte) (n >> 40 & 0xff);
		array[1 + offset] = (byte) (n >> 48 & 0xff);
		array[0 + offset] = (byte) (n >> 56 & 0xff);
	}

	public static long bytesToLong(byte[] array) {
		return ((((long) array[0] & 0xff) << 56) | (((long) array[1] & 0xff) << 48) | (((long) array[2] & 0xff) << 40)
				| (((long) array[3] & 0xff) << 32) | (((long) array[4] & 0xff) << 24) | (((long) array[5] & 0xff) << 16)
				| (((long) array[6] & 0xff) << 8) | (((long) array[7] & 0xff) << 0));
	}

	public static long bytesToLong(byte[] array, int offset) {
		return ((((long) array[offset + 0] & 0xff) << 56) | (((long) array[offset + 1] & 0xff) << 48)
				| (((long) array[offset + 2] & 0xff) << 40) | (((long) array[offset + 3] & 0xff) << 32)
				| (((long) array[offset + 4] & 0xff) << 24) | (((long) array[offset + 5] & 0xff) << 16)
				| (((long) array[offset + 6] & 0xff) << 8) | (((long) array[offset + 7] & 0xff) << 0));
	}

	public static byte[] intToBytes(int n) {
		byte[] b = new byte[4];
		b[3] = (byte) (n & 0xff);
		b[2] = (byte) (n >> 8 & 0xff);
		b[1] = (byte) (n >> 16 & 0xff);
		b[0] = (byte) (n >> 24 & 0xff);
		return b;
	}

	public static void intToBytes(int n, byte[] array, int offset) {
		array[3 + offset] = (byte) (n & 0xff);
		array[2 + offset] = (byte) (n >> 8 & 0xff);
		array[1 + offset] = (byte) (n >> 16 & 0xff);
		array[offset] = (byte) (n >> 24 & 0xff);
	}

	public static int bytesToInt(byte b[]) {
		return b[3] & 0xff | (b[2] & 0xff) << 8 | (b[1] & 0xff) << 16 | (b[0] & 0xff) << 24;
	}

	public static int bytesToInt(byte b[], int offset) {
		return b[offset + 3] & 0xff | (b[offset + 2] & 0xff) << 8 | (b[offset + 1] & 0xff) << 16
				| (b[offset] & 0xff) << 24;
	}

	public static byte[] uintToBytes(long n) {
		byte[] b = new byte[4];
		b[3] = (byte) (n & 0xff);
		b[2] = (byte) (n >> 8 & 0xff);
		b[1] = (byte) (n >> 16 & 0xff);
		b[0] = (byte) (n >> 24 & 0xff);

		return b;
	}

	public static void uintToBytes(long n, byte[] array, int offset) {
		array[3 + offset] = (byte) (n);
		array[2 + offset] = (byte) (n >> 8 & 0xff);
		array[1 + offset] = (byte) (n >> 16 & 0xff);
		array[offset] = (byte) (n >> 24 & 0xff);
	}

	public static long bytesToUint(byte[] array) {
		return ((long) (array[3] & 0xff)) | ((long) (array[2] & 0xff)) << 8 | ((long) (array[1] & 0xff)) << 16
				| ((long) (array[0] & 0xff)) << 24;
	}

	public static long bytesToUint(byte[] array, int offset) {
		return ((long) (array[offset + 3] & 0xff)) | ((long) (array[offset + 2] & 0xff)) << 8
				| ((long) (array[offset + 1] & 0xff)) << 16 | ((long) (array[offset] & 0xff)) << 24;
	}

	public static byte[] shortToBytes(short n) {
		byte[] b = new byte[2];
		b[1] = (byte) (n & 0xff);
		b[0] = (byte) ((n >> 8) & 0xff);
		return b;
	}

	public static void shortToBytes(short n, byte[] array, int offset) {
		array[offset + 1] = (byte) (n & 0xff);
		array[offset] = (byte) ((n >> 8) & 0xff);
	}

	public static short bytesToShort(byte[] b) {
		return (short) (b[1] & 0xff | (b[0] & 0xff) << 8);
	}

	public static short bytesToShort(byte[] b, int offset) {
		return (short) (b[offset + 1] & 0xff | (b[offset] & 0xff) << 8);
	}

	public static byte[] ushortToBytes(int n) {
		byte[] b = new byte[2];
		b[1] = (byte) (n & 0xff);
		b[0] = (byte) ((n >> 8) & 0xff);
		return b;
	}

	public static void ushortToBytes(int n, byte[] array, int offset) {
		array[offset + 1] = (byte) (n & 0xff);
		array[offset] = (byte) ((n >> 8) & 0xff);
	}

	public static int bytesToUshort(byte b[]) {
		return b[1] & 0xff | (b[0] & 0xff) << 8;
	}

	public static int bytesToUshort(byte b[], int offset) {
		return b[offset + 1] & 0xff | (b[offset] & 0xff) << 8;
	}

	public static byte[] ubyteToBytes(int n) {
		byte[] b = new byte[1];
		b[0] = (byte) (n & 0xff);
		return b;
	}

	public static void ubyteToBytes(int n, byte[] array, int offset) {
		array[0] = (byte) (n & 0xff);
	}

	public static int bytesToUbyte(byte[] array) {
		return array[0] & 0xff;
	}

	public static int bytesToUbyte(byte[] array, int offset) {
		return array[offset] & 0xff;
	}

	public static void main(String[] args) {
		Map m = new HashMap();
		m.put("a", "a");
		m.put("b", "b");
		m.put("c", "c");
		System.out.println(m);
		byte[] out = KryoObjectSerializer.serialize(m);
		System.out.println("outBytes.size=" + out.length + ":" + new String(out));
		Object n = KryoObjectSerializer.deserialize(out);
		System.out.println(n);
	}
}
