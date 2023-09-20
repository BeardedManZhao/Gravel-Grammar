package zhao.gravel.grammar.core;

import zhao.gravel.grammar.StreamString;
import zhao.gravel.grammar.command.ActuatorParam;
import zhao.gravel.grammar.command.Syntax;
import zhao.gravel.grammar.core.model.AnalyticalModel;
import zhao.gravel.grammar.core.model.Parser;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static zhao.gravel.grammar.command.NotFindParam.NOT_FIND;


/**
 * 命令回调函数类，在这里可以将语法对象，模式等组件结合起来，实现命令的解析。
 * <p>
 * Command callback function class, where syntax objects, patterns, and other components can be combined to achieve command parsing.
 *
 * @author zhao
 */
public class CommandCallback extends StreamString implements SyntaxCallback, Syntax {

    protected final HashMap<String, Syntax> allSyntaxTree;
    protected final String patternStr;
    private final int hash;
    protected Parser parser;

    /**
     * 实例化函数
     *
     * @param pattern 匹配模式字符串
     * @param syntax  所有的语法树对象
     */
    protected CommandCallback(String pattern, HashMap<String, Syntax> syntax) {
        this.allSyntaxTree = syntax;
        this.patternStr = pattern;
        hash = this.hashCode();
    }

    /**
     * 创建出一个回调函数器对象。
     *
     * @param pattern 回调器对象解析命令的时候使用的解析匹配模式字符串。
     * @param syntax  语法对象，一般情况下此应该是一个语法树，能够按照树的方式来进行识别操作
     * @return 具有指定解析模式以及指定的语法的回调函数器对象。
     */
    public static SyntaxCallback create(String pattern, Syntax... syntax) {
        final HashMap<String, Syntax> hashMap = new HashMap<>();
        for (Syntax syntax1 : syntax) {
            hashMap.put(syntax1.getSyntaxName(), syntax1);
        }
        final CommandCallback commandCallback = new CommandCallback(pattern, hashMap);
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
     * 将当前回调器中包含的所有子语法树的图以 mermaid 的方式绘制出来。
     *
     * @param isLR 是否以左右的方式来进行图的绘制，如果是就为true。
     */
    @Override
    public void toString(PrintWriter printWriter, boolean isLR) {
        printWriter.append("graph ").println(isLR ? "LR" : "BT");
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
        for (Syntax value : this.allSyntaxTree.values()) {
            value.toString(outStream);
        }
    }

    /**
     * @return hashcode
     */
    @Override
    public int getHashId() {
        return this.hash;
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
        this.allSyntaxTree.put(syntax.getSyntaxName(), syntax);
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
        this.allSyntaxTree.putAll(allSyntax);
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
        return this.allSyntaxTree.getOrDefault(syntaxName, NOT_FIND);
    }

    @Override
    public String toString() {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        this.toString(printWriter, false);
        return stringWriter.getBuffer().toString();
    }
}
