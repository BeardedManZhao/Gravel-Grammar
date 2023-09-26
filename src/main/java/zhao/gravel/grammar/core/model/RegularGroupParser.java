package zhao.gravel.grammar.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

/**
 * 按组提取正则解析器。
 * <p>
 * Regular parsers by group.
 *
 * @author zhao
 */
public class RegularGroupParser extends RegularParser {

    /**
     * 解析器与匹配模式字符串映射表
     * <p>
     * Parser and Matching Pattern String Mapping Table
     */
    protected static final HashMap<String, RegularGroupParser> REGULAR_GROUP_PARSER_HASH_MAP = new HashMap<>();
    private int groupNum;

    protected RegularGroupParser(String pattern) {
        super(pattern);
    }

    /**
     * 获取到指定匹配模式的解析器对象。
     *
     * @param pattern 匹配模式的字符串
     * @return 解析器
     */
    public static RegularGroupParser getInstance(String pattern) {
        RegularGroupParser parser = REGULAR_GROUP_PARSER_HASH_MAP.get(pattern);
        if (parser == null) {
            parser = new RegularGroupParser(pattern);
            REGULAR_GROUP_PARSER_HASH_MAP.put(parser.getPattern(), parser);
        }
        return parser;
    }

    /**
     * 将一个语法语句按照指定的匹配模式进行拆分。
     * <p>
     * Split a grammar statement according to the specified matching pattern.
     *
     * @param grammar 需要被拆分的字符串。
     *                <p>
     *                The string that needs to be get.
     * @return 拆分之后会返回字符串的许多子串。
     * <p>
     * After splitting, many substrings of the string will be returned.
     */
    @Override
    public String[] get(String grammar) {
        ArrayList<String> arrayList = new ArrayList<>();
        final Matcher matcher = this.pattern.matcher(grammar);
        while (matcher.find()) {
            arrayList.add(matcher.group(this.getGroupNum()));
        }
        return arrayList.toArray(new String[0]);
    }

    /**
     * @return 当前正则匹配的组编号
     * <p>
     * The group number of the current regular match
     */
    public int getGroupNum() {
        return groupNum;
    }

    /**
     * @param groupNum 设置当前正则匹配的组编号
     *                 <p>
     *                 Set the group number for the current regular match
     */
    public RegularGroupParser setGroupNum(int groupNum) {
        this.groupNum = groupNum;
        return this;
    }
}
