package zhao.gravel.grammar.core.model;

/**
 * 解析器对象接口，其能够按照各自特有的模式来针对语句进行解析。
 * <p>
 * The parser object interface, which can parse statements according to their own unique patterns.
 *
 * @author zhao
 */
public interface Parser {

    /**
     * @return 当前解析器对应的匹配模式字符串。
     * <p>
     * The matching pattern string corresponding to the current parser.
     */
    String getPattern();

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
    String[] split(String grammar);
}
