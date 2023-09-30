package zhao.gravel.grammar.core.model;

/**
 * 解析模式枚举类，其中包含的为处理命令或语法的模式项，不同的项对于命令的处理与解析模式不同。
 * <p>
 * Parsing mode enumeration class, which contains mode items for processing commands or syntax. Different items have different processing and parsing modes for commands.
 *
 * @author zhao
 */
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
    },

    /**
     * 正则匹配模式，此模式会提取出正则中组编号为 1 的表达式对应的所有字符串数据，并将所有提取到的字符串组合成为一个数组。
     * <p>
     * Regular matching mode, which extracts all string data corresponding to the expression with group number 1 in the regular, and combines all extracted strings into an array.
     */
    REGULAR_MODEL_1 {

        private final static int NUM = 1;

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
            return RegularGroupParser.getInstance(pattern).setGroupNum(NUM);
        }
    },

    /**
     * 正则匹配模式，此模式会提取出正则中组编号为 2 的表达式对应的所有字符串数据，并将所有提取到的字符串组合成为一个数组。
     * <p>
     * Regular matching mode, which extracts all string data corresponding to the expression with group number 2 in the regular, and combines all extracted strings into an array.
     */
    REGULAR_MODEL_2 {

        private final static int NUM = 2;

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
            return RegularGroupParser.getInstance(pattern).setGroupNum(NUM);
        }
    },

    /**
     * 正则匹配模式，此模式会提取出正则中组编号为 3 的表达式对应的所有字符串数据，并将所有提取到的字符串组合成为一个数组。
     * <p>
     * Regular matching mode, which extracts all string data corresponding to the expression with group number 3 in the regular, and combines all extracted strings into an array.
     */
    REGULAR_MODEL_3 {

        private final static int NUM = 3;

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
            return RegularGroupParser.getInstance(pattern).setGroupNum(NUM);
        }
    },

    /**
     * 正则匹配模式，此模式会提取出正则中组编号为 4 的表达式对应的所有字符串数据，并将所有提取到的字符串组合成为一个数组。
     * <p>
     * Regular matching mode, which extracts all string data corresponding to the expression with group number 4 in the regular, and combines all extracted strings into an array.
     */
    REGULAR_MODEL_4 {

        private final static int NUM = 4;

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
            return RegularGroupParser.getInstance(pattern).setGroupNum(NUM);
        }
    },

    /**
     * 正则匹配模式，此模式会提取出正则中组编号为 5 的表达式对应的所有字符串数据，并将所有提取到的字符串组合成为一个数组。
     * <p>
     * Regular matching mode, which extracts all string data corresponding to the expression with group number 5 in the regular, and combines all extracted strings into an array.
     */
    REGULAR_MODEL_5 {

        private final static int NUM = 5;

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
            return RegularGroupParser.getInstance(pattern).setGroupNum(NUM);
        }
    },

    /**
     * 正则匹配模式，此模式会提取出正则中组编号为 6 的表达式对应的所有字符串数据，并将所有提取到的字符串组合成为一个数组。
     * <p>
     * Regular matching mode, which extracts all string data corresponding to the expression with group number 6 in the regular, and combines all extracted strings into an array.
     */
    REGULAR_MODEL_6 {

        private final static int NUM = 6;

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
            return RegularGroupParser.getInstance(pattern).setGroupNum(NUM);
        }
    },

    /**
     * 正则匹配模式，此模式会提取出正则中组编号为 7 的表达式对应的所有字符串数据，并将所有提取到的字符串组合成为一个数组。
     * <p>
     * Regular matching mode, which extracts all string data corresponding to the expression with group number 7 in the regular, and combines all extracted strings into an array.
     */
    REGULAR_MODEL_7 {

        private final static int NUM = 7;

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
            return RegularGroupParser.getInstance(pattern).setGroupNum(NUM);
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
