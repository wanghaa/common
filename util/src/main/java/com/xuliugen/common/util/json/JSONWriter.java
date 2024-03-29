package com.xuliugen.common.util.json;

import com.xuliugen.common.util.common.Stack;

import java.io.*;

/**
 * JSON Writer.
 */
public class JSONWriter {

    private static final byte UNKNOWN = 0, ARRAY = 1, OBJECT = 2, OBJECT_VALUE = 3;

    private static class State {
        private byte type;
        private int itemCount = 0;

        State(byte t) {
            type = t;
        }
    }

    private Writer mWriter;

    private State mState = new State(UNKNOWN);

    private Stack<State> mStack = new Stack<State>();

    public JSONWriter(Writer writer) {
        mWriter = writer;
    }

    public JSONWriter(OutputStream is, String charset) throws UnsupportedEncodingException {
        mWriter = new OutputStreamWriter(is, charset);
    }

    /**
     * object begin.
     * @return this.
     */
    public JSONWriter objectBegin() throws IOException {
        beforeValue();

        mWriter.write(JSON.LBRACE);
        mStack.push(mState);
        mState = new State(OBJECT);
        return this;
    }

    /**
     * object end.
     * @return this.
     */
    public JSONWriter objectEnd() throws IOException {
        mWriter.write(JSON.RBRACE);
        mState = mStack.pop();
        return this;
    }

    /**
     * object item.
     * @param name name.
     * @return this.
     */
    public JSONWriter objectItem(String name) throws IOException {
        beforeObjectItem();

        mWriter.write(JSON.QUOTE);
        mWriter.write(escape(name));
        mWriter.write(JSON.QUOTE);
        mWriter.write(JSON.COLON);
        return this;
    }

    /**
     * array begin.
     * @return this.
     */
    public JSONWriter arrayBegin() throws IOException {
        beforeValue();

        mWriter.write(JSON.LSQUARE);
        mStack.push(mState);
        mState = new State(ARRAY);
        return this;
    }

    /**
     * array end, return array value.
     * @return this.
     */
    public JSONWriter arrayEnd() throws IOException {
        mWriter.write(JSON.RSQUARE);
        mState = mStack.pop();
        return this;
    }

    /**
     * value.
     * @return this.
     */
    public JSONWriter valueNull() throws IOException {
        beforeValue();

        mWriter.write(JSON.NULL);
        return this;
    }

    /**
     * value.
     * @param value value.
     * @return this.
     * @throws IOException
     */
    public JSONWriter valueString(String value) throws IOException {
        beforeValue();

        mWriter.write(JSON.QUOTE);
        mWriter.write(escape(value));
        mWriter.write(JSON.QUOTE);
        return this;
    }

    /**
     * value.
     * @param value value.
     * @return this.
     * @throws IOException
     */
    public JSONWriter valueBoolean(boolean value) throws IOException {
        beforeValue();

        mWriter.write(value ? "true" : "false");
        return this;
    }

    /**
     * value.
     * @param value value.
     * @return this.
     * @throws IOException
     */
    public JSONWriter valueInt(int value) throws IOException {
        beforeValue();

        mWriter.write(String.valueOf(value));
        return this;
    }

    /**
     * value.
     * @param value value.
     * @return this.
     * @throws IOException
     */
    public JSONWriter valueLong(long value) throws IOException {
        beforeValue();

        mWriter.write(String.valueOf(value));
        return this;
    }

    /**
     * value.
     * @param value value.
     * @return this.
     * @throws IOException
     */
    public JSONWriter valueFloat(float value) throws IOException {
        beforeValue();

        mWriter.write(String.valueOf(value));
        return this;
    }

    /**
     * value.
     * @param value value.
     * @return this.
     * @throws IOException
     */
    public JSONWriter valueDouble(double value) throws IOException {
        beforeValue();

        mWriter.write(String.valueOf(value));
        return this;
    }

    private void beforeValue() throws IOException {
        switch (mState.type) {
            case ARRAY:
                if (mState.itemCount++ > 0)
                    mWriter.write(JSON.COMMA);
                return;
            case OBJECT:
                throw new IOException("Must call objectItem first.");
            case OBJECT_VALUE:
                mState.type = OBJECT;
                return;
        }
    }

    private void beforeObjectItem() throws IOException {
        switch (mState.type) {
            case OBJECT_VALUE:
                mWriter.write(JSON.NULL);
            case OBJECT:
                mState.type = OBJECT_VALUE;
                if (mState.itemCount++ > 0)
                    mWriter.write(JSON.COMMA);
                return;
            default:
                throw new IOException("Must call objectBegin first.");
        }
    }

    private static final String[] CONTROL_CHAR_MAP = new String[]{
            "\\u0000", "\\u0001", "\\u0002", "\\u0003", "\\u0004", "\\u0005", "\\u0006", "\\u0007",
            "\\b", "\\t", "\\n", "\\u000b", "\\f", "\\r", "\\u000e", "\\u000f",
            "\\u0010", "\\u0011", "\\u0012", "\\u0013", "\\u0014", "\\u0015", "\\u0016", "\\u0017",
            "\\u0018", "\\u0019", "\\u001a", "\\u001b", "\\u001c", "\\u001d", "\\u001e", "\\u001f"
    };

    private static String escape(String str) {
        if (str == null)
            return str;
        int len = str.length();
        if (len == 0)
            return str;

        char c;
        StringBuilder sb = null;
        for (int i = 0; i < len; i++) {
            c = str.charAt(i);
            if (c < ' ') // control char.
            {
                if (sb == null) {
                    sb = new StringBuilder(len << 1);
                    sb.append(str, 0, i);
                }
                sb.append(CONTROL_CHAR_MAP[c]);
            } else {
                switch (c) {
                    case '\\':
                    case '/':
                    case '"':
                        if (sb == null) {
                            sb = new StringBuilder(len << 1);
                            sb.append(str, 0, i);
                        }
                        sb.append('\\').append(c);
                        break;
                    default:
                        if (sb != null)
                            sb.append(c);
                }
            }
        }
        return sb == null ? str : sb.toString();
    }
}