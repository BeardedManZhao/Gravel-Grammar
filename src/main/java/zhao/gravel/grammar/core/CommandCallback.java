package zhao.gravel.grammar.core;

import zhao.gravel.grammar.command.ActuatorParam;
import zhao.gravel.grammar.command.Syntax;
import zhao.gravel.grammar.core.model.AnalyticalModel;
import zhao.gravel.grammar.core.model.Parser;

import java.util.Map;

import static zhao.gravel.grammar.command.NotFindParam.NOT_FIND;


/**
 * 命令回调函数类
 *
 * @author zhao
 */
public class CommandCallback implements SyntaxCallback, Syntax {

    protected final Syntax syntax;
    protected final String patternStr;
    protected Parser parser;

    protected CommandCallback(String pattern, Syntax syntax) {
        this.syntax = syntax;
        this.patternStr = pattern;
    }

    public static SyntaxCallback create(String pattern, Syntax syntax) {
        final CommandCallback commandCallback = new CommandCallback(pattern, syntax);
        commandCallback.setAnalyticalModel(AnalyticalModel.REGULAR_MODEL);
        return commandCallback;
    }

    /**
     * 设置本回调类在解析命令的时候要使用的语法解析模式。
     *
     * @param analyticalModel 需要使用的语法解析模式。
     *                        <p>
     *                        The syntax parsing mode that needs to be used.
     * @see AnalyticalModel
     */
    @Override
    public void setAnalyticalModel(AnalyticalModel analyticalModel) {
        this.parser = analyticalModel.getParser(this.patternStr);
    }

    /**
     * 运行一个命令，在这里会把命令传递给语法树去逐一执行与处理。
     * <p>
     * Run a command, where it will be passed to the syntax tree for execution and processing one by one.
     *
     * @param grammar 需要被解析的命令，在这里是一个字符串整体，回调类会自动的根据解析模式进行拆分。
     *                <p>
     *                The command that needs to be parsed here is a string as a whole, and the callback class will automatically split it based on the parsing mode.
     * @return 根据语法执行的运行结果对象。
     * <p>
     * Run result object executed according to syntax.
     */
    @Override
    public Object run(String grammar) {
        return this.run(this.parser.split(grammar));
    }

    /**
     * 运行一个命令，在这里会把命令传递给语法树去逐一执行与处理。
     * <p>
     * Run a command, where it will be passed to the syntax tree for execution and processing one by one.
     *
     * @param grammar 需要被解析的命令，在这里是一个字符串数组，回调类不会自动的根据解析模式进行拆分。
     *                <p>
     *                The command that needs to be parsed here is an array of strings, and the callback class will not automatically split based on the parsing mode.
     * @return 根据语法执行的运行结果对象。
     * <p>
     * Run result object executed according to syntax.
     */
    @Override
    public Object run(String... grammar) {
        Syntax now = this;
        for (String s : grammar) {
            // 获取语法
            final Syntax syntax = now.get(s);
            // 判断是不是null
            if (syntax == null) {
                break;
            } else {
                now = syntax;
            }
            // 判断是否需要执行
            if (now instanceof ActuatorParam) {
                // 执行
                return ((ActuatorParam) now).run();
            }
            // 其它情况就不执行
        }
        // 如果到了最后都没有找到执行器，就代表没找到
        return NOT_FIND.get(grammar[grammar.length - 1]);
    }

    /**
     * @return 当前语法对象 对应的参数名称。
     */
    @Override
    public String getSyntaxName() {
        return this.patternStr;
    }

    /**
     * 向此语法对象添加子语法树对象，子语法树将可以被此语法树调用。
     * <p>
     * Add a sub syntax tree object to this syntax object, and the sub syntax tree will be called by this syntax tree.
     *
     * @param syntax 需要被添加的子语法树对象。
     *               <p>
     *               The sub syntax tree object that needs to be added.
     */
    @Override
    public void addSubSyntax(Syntax syntax) {
        throw new UnsupportedOperationException("Callbacks do not support adding sub syntax. The correct step should be: 'Create a syntax object and then instantiate the callback through the syntax object'.");
    }

    /**
     * 向此语法对象添加子语法树对象，子语法树将可以被此语法树调用。
     * <p>
     * Add a sub syntax tree object to this syntax object, and the sub syntax tree will be called by this syntax tree.
     *
     * @param allSyntax 需要被添加的子语法树对象。
     *                  <p>
     *                  The sub syntax tree object that needs to be added.
     */
    @Override
    public void addSubSyntax(Map<String, Syntax> allSyntax) {
        throw new UnsupportedOperationException("Callbacks do not support adding sub syntax. The correct step should be: 'Create a syntax object and then instantiate the callback through the syntax object'.");
    }

    /**
     * 根据 syntaxName 获取到对应的 syntax 对象。
     * <p>
     * Obtain the corresponding syntax object based on syntax Name.
     *
     * @param syntaxName 需要获取的对象对应的名称，一般来说这里也就是命令的某个参数。
     *                   <p>
     *                   The name of the object that needs to be obtained, which is generally a parameter of the command.
     * @return syntaxName 对应的 syntax 对象。
     * <p>
     * The syntax object corresponding to syntax Name.
     */
    @Override
    public Syntax get(String syntaxName) {
        return this.syntax;
    }
}
