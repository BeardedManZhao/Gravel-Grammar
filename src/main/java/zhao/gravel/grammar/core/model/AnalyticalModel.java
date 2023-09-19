package zhao.gravel.grammar.core.model;

public enum AnalyticalModel {

    /**
     * 字符匹配模式
     * <p>
     * Character matching mode
     */
    CHARACTER_PATTERN {
        /**
         * 根据指定的匹配模式获取到解析器对象。
         * <p>
         * Retrieve the parser object based on the specified matching pattern.
         *
         * @param pattern 指定的解析匹配模式，不同的AnalyticalModel模式对此参数有不同的要求与使用方法。
         *                <p>
         *                The specified parsing matching pattern has different requirements and usage methods for this parameter for different AnalyticalModel patterns.
         * @return 构建出来的解析器对象。
         * <p>
         * The constructed parser object.
         */
        @Override
        public Parser getParser(String pattern) {
            return CharacterParser.getInstance(pattern);
        }
    },

    /**
     * 正则匹配模式
     * <p>
     * Regular matching pattern
     */
    REGULAR_MODEL {
        /**
         * 根据指定的匹配模式获取到解析器对象。
         * <p>
         * Retrieve the parser object based on the specified matching pattern.
         *
         * @param pattern 指定的解析匹配模式，不同的AnalyticalModel模式对此参数有不同的要求与使用方法。
         *                <p>
         *                The specified parsing matching pattern has different requirements and usage methods for this parameter for different AnalyticalModel patterns.
         * @return 构建出来的解析器对象。
         * <p>
         * The constructed parser object.
         */
        @Override
        public Parser getParser(String pattern) {
            return RegularParser.getInstance(pattern);
        }
    };

    /**
     * 根据指定的匹配模式获取到解析器对象。
     * <p>
     * Retrieve the parser object based on the specified matching pattern.
     *
     * @param pattern 指定的解析匹配模式，不同的AnalyticalModel模式对此参数有不同的要求与使用方法。
     *                <p>
     *                The specified parsing matching pattern has different requirements and usage methods for this parameter for different AnalyticalModel patterns.
     * @return 构建出来的解析器对象。
     * <p>
     * The constructed parser object.
     */
    public abstract Parser getParser(String pattern);
}
