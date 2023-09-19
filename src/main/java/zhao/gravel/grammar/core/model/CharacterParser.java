package zhao.gravel.grammar.core.model;

import zhao.utils.StrUtils;

import java.util.HashMap;

/**
 * 字符串解析器，通过将匹配模式字符串做为分隔符或其它数据，具体需要根据函数的描述来使用。
 * <p>
 * A string parser that uses matching pattern strings as delimiters or other data, depending on the description of the function.
 *
 * @author zhao
 */
public class CharacterParser implements Parser {

    /**
     * 解析器与匹配模式字符串映射表
     * <p>
     * Parser and Matching Pattern String Mapping Table
     */
    private static final HashMap<String, CharacterParser> HASH_MAP = new HashMap<>();

    private final String pattern;

    private CharacterParser(String pattern) {
        this.pattern = pattern;
    }

    /**
     * 获取到指定匹配模式的解析器对象。
     *
     * @param pattern 匹配模式的字符串
     * @return 解析器
     */
    public static CharacterParser getInstance(String pattern) {
        CharacterParser parser = HASH_MAP.get(pattern);
        if (parser == null) {
            parser = new CharacterParser(pattern);
            HASH_MAP.put(parser.getPattern(), parser);
        }
        return parser;
    }

    @Override
    public String getPattern() {
        return this.pattern;
    }

    @Override
    public String[] split(String grammar) {
        return StrUtils.splitBy(grammar, this.pattern);
    }
}
