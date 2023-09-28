package zhao.gravel.grammar.core;

/**
 * 内置正则表达式匹配对象。
 * <p>
 * Built-in regular expression matching objects.
 *
 * @author zhao
 */
public enum BuiltInReg {
    SQL_EXTRACTION_REGULAR_MODEL_1 {
        @Override
        public String getPattern() {
            return "((?:distinct|DISTINCT)\\s+?\\S+|\\w+\\s+by|\\d+\\s*?\\d+\\s*?\\d+|\\S+);*";
        }
    };

    public abstract String getPattern();
}
