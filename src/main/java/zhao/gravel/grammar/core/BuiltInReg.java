package zhao.gravel.grammar.core;

import zhao.utils.RegularConstant;

/**
 * 内置正则表达式匹配对象。
 * <p>
 * Built-in regular expression matching objects.
 *
 * @author zhao
 */
public enum BuiltInReg {

    /**
     * 此正则表达式能识别大部分的关键字和数学表达式，需要在提取模式下使用，并将识别到的合法内容存储在第 1 编号组中，用于被查询与获取。
     * <p>
     * This regular expression can recognize most keywords and mathematical expressions, and needs to be used in extraction mode. The recognized legitimate content is stored in the first numbered group for query and retrieval.
     */
    SQL_EXTRACTION_REGULAR_MODEL_1 {
        @Override
        public String getPattern() {
            return RegularConstant.SQL_EXTRACT.get().toString();
        }
    };

    /**
     * @return 当前项对应正则表达式的字符串对象。
     * <p>
     * The string object corresponding to the regular expression for the current item.
     */
    public abstract String getPattern();
}
