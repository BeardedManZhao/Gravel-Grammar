package zhao.gravel.grammar.core.model;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * 正则解析器，通过将匹配模式字符串做为分隔符或其它数据，具体需要根据函数的描述来使用。
 * <p>
 * Regular parsers use matching pattern strings as delimiters or other data, depending on the description of the function.
 *
 * @author zhao
 */
public class RegularParser implements Parser {

    /**
     * 解析器与匹配模式字符串映射表
     * <p>
     * Parser and Matching Pattern String Mapping Table
     */
    private static final HashMap<String, RegularParser> HASH_MAP = new HashMap<>();

    private final String patternStr;
    private final Pattern pattern;

    private RegularParser(String pattern) {
        this.patternStr = pattern;
        this.pattern = Pattern.compile(pattern);
    }

    /**
     * 获取到指定匹配模式的解析器对象。
     *
     * @param pattern 匹配模式的字符串
     * @return 解析器
     */
    public static RegularParser getInstance(String pattern) {
        RegularParser parser = HASH_MAP.get(pattern);
        if (parser == null) {
            parser = new RegularParser(pattern);
            HASH_MAP.put(parser.getPattern(), parser);
        }
        return parser;
    }

    /**
     * @return 当前解析器对应的匹配模式字符串。
     * <p>
     * The matching pattern string corresponding to the current parser.
     */
    @Override
    public String getPattern() {
        return this.patternStr;
    }

    /**
     * 将一个语法语句按照指定的匹配模式进行拆分。
     * <p>
     * Split a grammar statement according to the specified matching pattern.
     *
     * @param grammar 需要被拆分的字符串。
     *                <p>
     *                The string that needs to be split.
     * @return 拆分之后会返回字符串的许多子串。
     * <p>
     * After splitting, many substrings of the string will be returned.
     */
    @Override
    public String[] split(String grammar) {
        return this.pattern.split(grammar);
    }
}
