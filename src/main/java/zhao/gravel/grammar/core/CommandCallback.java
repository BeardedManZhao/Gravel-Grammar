package zhao.gravel.grammar.core;

import zhao.gravel.grammar.command.ActuatorParam;
import zhao.gravel.grammar.command.GrammarParam;
import zhao.gravel.grammar.command.Syntax;
import zhao.gravel.grammar.core.model.AnalyticalModel;
import zhao.gravel.grammar.core.model.Parser;

import java.io.PrintWriter;
import java.io.StringWriter;

import static zhao.gravel.grammar.command.NotFindParam.NOT_FIND;


/**
 * 命令回调函数类，在这里可以将语法对象，模式等组件结合起来，实现命令的解析。
 * <p>
 * Command callback function class, where syntax objects, patterns, and other components can be combined to achieve command parsing.
 *
 * @author zhao
 */
public class CommandCallback extends GrammarParam implements SyntaxCallback {

    protected Parser parser;

    /**
     * 实例化函数
     *
     * @param pattern 匹配模式字符串
     * @param syntax  所有的语法树对象
     */
    protected CommandCallback(String pattern, Syntax... syntax) {
        super(pattern, false, syntax);
    }

    /**
     * 创建出一个回调函数器对象。
     *
     * @param pattern 回调器对象解析命令的时候使用的解析匹配模式字符串。
     * @param syntax  语法对象，一般情况下此应该是一个语法树，能够按照树的方式来进行识别操作
     * @return 具有指定解析模式以及指定的语法的回调函数器对象。
     */
    public static CommandCallback createGet(String pattern, Syntax... syntax) {
        final CommandCallback commandCallback = new CommandCallback(pattern, syntax);
        commandCallback.setAnalyticalModel(AnalyticalModel.REGULAR_MODEL);
        return commandCallback;
    }

    public static void clearVariable(Syntax syntax) {
        if (syntax != null) {
            syntax.clearVariable();
        }
    }

    public static CommandCallback createGet(BuiltInReg sql, Syntax instance) {
        return createGet(sql.getPattern(), instance);
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
        this.parser = analyticalModel.getParser(this.getSyntaxName());
    }

    /**
     * 运行一个命令，在这里会把命令传递给语法树去逐一执行与处理。
     * <p>
     * Run a command, where it will be passed to the syntax tree for execution and processing one by one.
     *
     * @param grammar 需要被解析的命令，在这里是一个字符串整体，回调类会自动的根据解析模式进行拆分。
     *                <p>
     *                The command that needs to be parsed here is a string as a whole, and the callback class will automatically get it based on the parsing mode.
     * @return 根据语法执行的运行结果对象。
     * <p>
     * Run result object executed according to syntax.
     */
    @Override
    public Object run(String grammar) {
        return this.run(this.parser.get(grammar));
    }

    /**
     * 运行一个命令，在这里会把命令传递给语法树去逐一执行与处理。
     * <p>
     * Run a command, where it will be passed to the syntax tree for execution and processing one by one.
     *
     * @param grammar 需要被解析的命令，在这里是一个字符串数组，回调类不会自动的根据解析模式进行拆分。
     *                <p>
     *                The command that needs to be parsed here is an array of strings, and the callback class will not automatically get based on the parsing mode.
     * @return 根据语法执行的运行结果对象。
     * <p>
     * Run result object executed according to syntax.
     */
    @Override
    public Object run(String... grammar) {
        final Syntax first = grammar.length > 0 ? this.get(grammar[0]) : null;
        Syntax now = this;
        final int lastIndex = grammar.length - 1;
        for (int i = 0, grammarLength = grammar.length; i < grammarLength; i++) {
            // TODO 开始调试 假设 now=where s=where的C
            String s = grammar[i];
            // 获取语法 TODO where的C
            final Syntax syntax = now.get(s);
            // 判断是不是null TODO 不是
            if (syntax == null) {
                break;
            } else {
                // TODO 更新 now 就是 whereC 的语法
                now = syntax;
            }
            // 判断是否需要执行
            if (now instanceof ActuatorParam) {
                // 如果需要执行就判断是否有子语句，没有就执行
                final int nextIndex = i + 1;
                if (i == lastIndex) {
                    final Object run = ((ActuatorParam) now).run();
                    clearVariable(first);
                    return run;
                }
                final Syntax syntax1 = now.get(grammar[nextIndex]);
                if (syntax1 == null) {
                    final Object run = ((ActuatorParam) now).run();
                    clearVariable(first);
                    return run;
                }
            }
        }
        // 如果到了最后都没有找到执行器，就代表没找到
        return NOT_FIND.get(grammar[lastIndex]);
    }

    /**
     * 将当前回调器中包含的所有子语法树的图以 mermaid 的方式绘制出来。
     *
     * @param isLR 是否以左右的方式来进行图的绘制，如果是就为true。
     */
    @Override
    public void toString(PrintWriter printWriter, boolean isLR) {
        printWriter.append("graph ").println(isLR ? "LR" : "BR");
        this.toString(printWriter);
    }

    /**
     * 将当前回调器中包含的所有子语法树的图以 mermaid 的方式绘制出来。
     * <p>
     * Draw a graph of all sub syntax trees contained in the current grammar in mermaid format.
     *
     * @param outStream 图代码的输出数据流。
     *                  <p>
     *                  graph code.
     */
    @Override
    public void toString(PrintWriter outStream) {
        for (Syntax value : super.syntaxHashMap.values()) {
            value.toString(outStream);
        }
    }

    @Override
    public String toString() {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        this.toString(printWriter, false);
        return stringWriter.getBuffer().toString();
    }
}
