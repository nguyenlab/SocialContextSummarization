package nguyenlab.docsum.crf.utils;

import java.util.Iterator;

public class Object2Iterator {

	public static Iterable<String> stringIterable(final Object object) {
		return new Iterable<String>() {
			@Override
			public Iterator<String> iterator() {
				return Object2Iterator.stringIterator(object);
			}
		};
	}

	public static Iterator<String> stringIterator(final Object object) {

		if (object instanceof Object[]) {

			return new Iterator<String>() {
				private final Object[] array = (Object[]) object;
				private int i = 0;

				@Override
				public boolean hasNext() {

					return i < array.length;
				}

				@Override
				public String next() {

					return String.valueOf(array[i++]);
				}
			};
		} else if (object instanceof double[]) {
			return new Iterator<String>() {
				private final double[] array = (double[]) object;
				private int i = 0;

				@Override
				public boolean hasNext() {

					return i < array.length;
				}

				@Override
				public String next() {

					return String.valueOf(array[i++]);
				}
			};
		} else if (object instanceof float[]) {
			return new Iterator<String>() {
				private final float[] array = (float[]) object;
				private int i = 0;

				@Override
				public boolean hasNext() {

					return i < array.length;
				}

				@Override
				public String next() {

					return String.valueOf(array[i++]);
				}
			};
		} else if (object instanceof int[]) {
			return new Iterator<String>() {
				private final int[] array = (int[]) object;
				private int i = 0;

				@Override
				public boolean hasNext() {

					return i < array.length;
				}

				@Override
				public String next() {

					return String.valueOf(array[i++]);
				}
			};
		} else if (object instanceof long[]) {
			return new Iterator<String>() {
				private final long[] array = (long[]) object;
				private int i = 0;

				@Override
				public boolean hasNext() {

					return i < array.length;
				}

				@Override
				public String next() {

					return String.valueOf(array[i++]);
				}
			};
		} else if (object instanceof short[]) {
			return new Iterator<String>() {
				private final short[] array = (short[]) object;
				private int i = 0;

				@Override
				public boolean hasNext() {

					return i < array.length;
				}

				@Override
				public String next() {

					return String.valueOf(array[i++]);
				}
			};
		} else if (object instanceof char[]) {
			return new Iterator<String>() {
				private final char[] array = (char[]) object;
				private int i = 0;

				@Override
				public boolean hasNext() {

					return i < array.length;
				}

				@Override
				public String next() {

					return String.valueOf(array[i++]);
				}
			};
		} else if (object instanceof byte[]) {
			return new Iterator<String>() {
				private final byte[] array = (byte[]) object;
				private int i = 0;

				@Override
				public boolean hasNext() {

					return i < array.length;
				}

				@Override
				public String next() {

					return String.valueOf(array[i++]);
				}
			};
		} else if (object instanceof boolean[]) {
			return new Iterator<String>() {
				private final boolean[] array = (boolean[]) object;
				private int i = 0;

				@Override
				public boolean hasNext() {

					return i < array.length;
				}

				@Override
				public String next() {

					return String.valueOf(array[i++]);
				}
			};
		} else {
			throw new UnsupportedOperationException("Input type " + object.getClass().getName() + " not supported.");
		}
	}
}
