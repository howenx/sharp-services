package util;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import play.Logger;

import java.io.IOException;

/**
 * 序列化
 * Created by howen on 16/1/13.
 */
public class StringUnicodeSerializer extends JsonSerializer<String> {

    private final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();
    private final int[] ESCAPE_CODES = CharTypes.get7BitOutputEscapes();


    private void writeUnicodeEscape(StringBuilder sb, char c) throws IOException {
        sb.append('\\');
        sb.append('u');
        sb.append(HEX_CHARS[(c >> 12) & 0xF]);
        sb.append(HEX_CHARS[(c >> 8) & 0xF]);
        sb.append(HEX_CHARS[(c >> 4) & 0xF]);
        sb.append(HEX_CHARS[c & 0xF]);
    }

    private void writeShortEscape(StringBuilder sb, char c) throws IOException {
//        sb.append('\\');
        sb.append(c);
    }

    @Override
    public void serialize(String str, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonGenerationException {

        StringBuilder sb = new StringBuilder();

        for (char c : str.toCharArray()) {
            if (c >= 0x80) {
                writeUnicodeEscape(sb, c); // 为所有非ASCII字符生成转义的unicode字符
            } else {
                // 为ASCII字符中前128个字符使用转义的unicode字符
                int code = (c < ESCAPE_CODES.length ? ESCAPE_CODES[c] : 0);
                if (code == 0) {
                    sb.append(c); // 此处不用转义
                } else if (code < 0) {
                    writeUnicodeEscape(sb, (char) (-code - 1)); // 通用转义字符
                } else {
                    writeShortEscape(sb, (char) code); // 短转义字符 (\n \t ...)
                }
            }
        }
        Logger.error("阿斯顿发生地方:->>>>>>   "+sb.toString());
        gen.writeString(sb.toString());

    }


    @Override
    public Class<String> handledType() {
        return String.class;
    }
}