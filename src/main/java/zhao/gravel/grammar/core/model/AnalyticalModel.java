package zhao.gravel.grammar.core.model;

public enum AnalyticalModel {

    /**
     * 字符匹配模式
     */
    CHARACTER_PATTERN {

        String pattern = " ";

        @Override
        void setPattern(String pattern) {

        }

        @Override
        String getPattern() {
            return null;
        }

        @Override
        String[] parse(String grammar) {
            return new String[0];
        }
    },

    /**
     * 正则匹配模式
     */
    Regular_MODEL {
        @Override
        void setPattern(String pattern) {

        }

        @Override
        String getPattern() {
            return null;
        }

        @Override
        String[] parse(String grammar) {
            return new String[0];
        }
    };

    abstract void setPattern(String pattern);

    abstract String getPattern();

    abstract String[] parse(String grammar);
}
