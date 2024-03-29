package com.xuliugen.common.util.json;

import com.xuliugen.common.util.StringUtils;
import com.xuliugen.common.util.bytecode.Wrapper;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * JSON to Object visitor.
 */
class J2oVisitor implements JSONVisitor {

    public static final boolean[] EMPTY_BOOL_ARRAY = new boolean[0];

    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    public static final char[] EMPTY_CHAR_ARRAY = new char[0];

    public static final short[] EMPTY_SHORT_ARRAY = new short[0];

    public static final int[] EMPTY_INT_ARRAY = new int[0];

    public static final long[] EMPTY_LONG_ARRAY = new long[0];

    public static final float[] EMPTY_FLOAT_ARRAY = new float[0];

    public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];

    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    private Class<?>[] mTypes;

    private Class<?> mType = Object[].class;

    private Object mValue;

    private Wrapper mWrapper;

    private JSONConverter mConverter;

    private Stack<Object> mStack = new Stack<Object>();

    J2oVisitor(Class<?> type, JSONConverter jc) {
        mType = type;
        mConverter = jc;
    }

    J2oVisitor(Class<?>[] types, JSONConverter jc) {
        mTypes = types;
        mConverter = jc;
    }

    public void begin() {
    }

    public Object end(Object obj, boolean isValue) throws ParseException {
        mStack.clear();
        try {
            return mConverter.readValue(mType, obj);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public void objectBegin() throws ParseException {
        mStack.push(mValue);
        mStack.push(mType);
        mStack.push(mWrapper);

        if (mType == Object.class || Map.class.isAssignableFrom(mType)) {
            if (!mType.isInterface() && mType != Object.class) {
                try {
                    mValue = mType.newInstance();
                } catch (Exception e) {
                    throw new IllegalStateException(e.getMessage(), e);
                }
            } else if (mType == ConcurrentMap.class) {
                mValue = new ConcurrentHashMap<String, Object>();
            } else {
                mValue = new HashMap<String, Object>();
            }
            mWrapper = null;
        } else {
            try {
                mValue = mType.newInstance();
                mWrapper = Wrapper.getWrapper(mType);
            } catch (IllegalAccessException e) {
                throw new ParseException(StringUtils.toString(e));
            } catch (InstantiationException e) {
                throw new ParseException(StringUtils.toString(e));
            }
        }
    }

    public Object objectEnd(int count) {
        Object ret = mValue;
        mWrapper = (Wrapper) mStack.pop();
        mType = (Class<?>) mStack.pop();
        mValue = mStack.pop();
        return ret;
    }

    public void objectItem(String name) {
        mStack.push(name); // push name.
        mType = (mWrapper == null ? Object.class : mWrapper.getPropertyType(name));
    }

    @SuppressWarnings("unchecked")
    public void objectItemValue(Object obj, boolean isValue) throws ParseException {
        String name = (String) mStack.pop();  // pop name.
        if (mWrapper == null) {
            ((Map<String, Object>) mValue).put(name, obj);
        } else {
            if (mType != null) {
                if (isValue && obj != null) {
                    try {
                        obj = mConverter.readValue(mType, obj);
                    } catch (IOException e) {
                        throw new ParseException(StringUtils.toString(e));
                    }
                }
                if (mValue instanceof Throwable && "message".equals(name)) {
                    try {
                        Field field = Throwable.class.getDeclaredField("detailMessage");
                        if (!field.isAccessible()) {
                            field.setAccessible(true);
                        }
                        field.set(mValue, obj);
                    } catch (NoSuchFieldException e) {
                        throw new ParseException(StringUtils.toString(e));
                    } catch (IllegalAccessException e) {
                        throw new ParseException(StringUtils.toString(e));
                    }
                } else if (mValue instanceof Throwable && "suppressed".equals(name)) { // modified by lishen
                    // simply ignore this field for jdk 1.7+
                } else if (!CLASS_PROPERTY.equals(name)) {
                    mWrapper.setPropertyValue(mValue, name, obj);
                }
            }
        }
    }

    public void arrayBegin() throws ParseException {
        mStack.push(mType);

        if (mType.isArray())
            mType = mType.getComponentType();
        else if (mType == Object.class || Collection.class.isAssignableFrom(mType))
            mType = Object.class;
        else
            throw new ParseException("Convert error, can not load json array data into class [" + mType.getName() + "].");
    }

    @SuppressWarnings("unchecked")
    public Object arrayEnd(int count) throws ParseException {
        Object ret;
        mType = (Class<?>) mStack.get(-1 - count);

        if (mType.isArray()) {
            ret = toArray(mType.getComponentType(), mStack, count);
        } else {
            Collection<Object> items;
            if (mType == Object.class || Collection.class.isAssignableFrom(mType)) {
                if (!mType.isInterface() && mType != Object.class) {
                    try {
                        items = (Collection<Object>) mType.newInstance();
                    } catch (Exception e) {
                        throw new IllegalStateException(e.getMessage(), e);
                    }
                } else if (mType.isAssignableFrom(ArrayList.class)) { // List
                    items = new ArrayList<Object>(count);
                } else if (mType.isAssignableFrom(HashSet.class)) { // Set
                    items = new HashSet<Object>(count);
                } else if (mType.isAssignableFrom(LinkedList.class)) { // Queue
                    items = new LinkedList<Object>();
                } else { // Other
                    items = new ArrayList<Object>(count);
                }
            } else {
                throw new ParseException("Convert error, can not load json array data into class [" + mType.getName() + "].");
            }
            for (int i = 0; i < count; i++)
                items.add(mStack.remove(i - count));
            ret = items;
        }
        mStack.pop();
        return ret;
    }

    public void arrayItem(int index) throws ParseException {
        if (mTypes != null && mStack.size() == index + 1) {
            if (index < mTypes.length)
                mType = mTypes[index];
            else
                throw new ParseException("Can not load json array data into [" + name(mTypes) + "].");
        }
    }

    public void arrayItemValue(int index, Object obj, boolean isValue) throws ParseException {
        if (isValue && obj != null) {
            try {
                obj = mConverter.readValue(mType, obj);
            } catch (IOException e) {
                throw new ParseException(e.getMessage());
            }
        }

        mStack.push(obj);
    }

    private static Object toArray(Class<?> c, Stack<Object> list, int len) throws ParseException {
        if (c == String.class) {
            if (len == 0) {
                return EMPTY_STRING_ARRAY;
            } else {
                Object o;
                String ss[] = new String[len];
                for (int i = len - 1; i >= 0; i--) {
                    o = list.pop();
                    ss[i] = (o == null ? null : o.toString());
                }
                return ss;
            }
        }
        if (c == boolean.class) {
            if (len == 0) return EMPTY_BOOL_ARRAY;
            Object o;
            boolean[] ret = new boolean[len];
            for (int i = len - 1; i >= 0; i--) {
                o = list.pop();
                if (o instanceof Boolean)
                    ret[i] = ((Boolean) o).booleanValue();
            }
            return ret;
        }
        if (c == int.class) {
            if (len == 0) return EMPTY_INT_ARRAY;
            Object o;
            int[] ret = new int[len];
            for (int i = len - 1; i >= 0; i--) {
                o = list.pop();
                if (o instanceof Number)
                    ret[i] = ((Number) o).intValue();
            }
            return ret;
        }
        if (c == long.class) {
            if (len == 0) return EMPTY_LONG_ARRAY;
            Object o;
            long[] ret = new long[len];
            for (int i = len - 1; i >= 0; i--) {
                o = list.pop();
                if (o instanceof Number)
                    ret[i] = ((Number) o).longValue();
            }
            return ret;
        }
        if (c == float.class) {
            if (len == 0) return EMPTY_FLOAT_ARRAY;
            Object o;
            float[] ret = new float[len];
            for (int i = len - 1; i >= 0; i--) {
                o = list.pop();
                if (o instanceof Number)
                    ret[i] = ((Number) o).floatValue();
            }
            return ret;
        }
        if (c == double.class) {
            if (len == 0) return EMPTY_DOUBLE_ARRAY;
            Object o;
            double[] ret = new double[len];
            for (int i = len - 1; i >= 0; i--) {
                o = list.pop();
                if (o instanceof Number)
                    ret[i] = ((Number) o).doubleValue();
            }
            return ret;
        }
        if (c == byte.class) {
            if (len == 0) return EMPTY_BYTE_ARRAY;
            Object o;
            byte[] ret = new byte[len];
            for (int i = len - 1; i >= 0; i--) {
                o = list.pop();
                if (o instanceof Number)
                    ret[i] = ((Number) o).byteValue();
            }
            return ret;
        }
        if (c == char.class) {
            if (len == 0) return EMPTY_CHAR_ARRAY;
            Object o;
            char[] ret = new char[len];
            for (int i = len - 1; i >= 0; i--) {
                o = list.pop();
                if (o instanceof Character)
                    ret[i] = ((Character) o).charValue();
            }
            return ret;
        }
        if (c == short.class) {
            if (len == 0) return EMPTY_SHORT_ARRAY;
            Object o;
            short[] ret = new short[len];
            for (int i = len - 1; i >= 0; i--) {
                o = list.pop();
                if (o instanceof Number)
                    ret[i] = ((Number) o).shortValue();
            }
            return ret;
        }

        Object ret = Array.newInstance(c, len);
        for (int i = len - 1; i >= 0; i--)
            Array.set(ret, i, list.pop());
        return ret;
    }

    private static String name(Class<?>[] types) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            if (i > 0)
                sb.append(", ");
            sb.append(types[i].getName());
        }
        return sb.toString();
    }
}