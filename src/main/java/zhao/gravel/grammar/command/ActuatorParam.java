package zhao.gravel.grammar.command;

import java.io.PrintWriter;

/**
 * 执行参数类，该类在语法树中扮演的一般是可以被执行的参数位置，例如最后一个参数，该类处理最基本的语法树的存储以外，还具有执行语法的能力。
 * <p>
 * Execution parameter class, which generally plays a role in the syntax tree as a parameter position that can be executed, such as the last parameter. In addition to handling the storage of the most basic syntax tree, this class also has the ability to execute syntax.
 *
 * @author zhao
 */
public abstract class ActuatorParam extends GrammarParam {

    protected ActuatorParam(String name) {
        super(name, "No Help Info");
    }

    protected ActuatorParam(String name, String help_info) {
        super(name, help_info);
    }

    /**
     * @return 当前执行器参数的执行逻辑函数，执行完毕之后会返回一个任意数据类型。
     * <p>
     * The execution logic function of the current executor parameter will return an arbitrary data type after execution.
     */
    public abstract Object run();

    /**
     * @return 当前执行器对象在 mermaid 图中的名字代码。
     * <p>
     * The name code of the current executor object in the mermaid diagram.
     */
    protected String getMermaidName() {
        return this.getHashId() + "([" + this.getSyntaxName() + "])";
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
        super.toString(outStream);
        outStream
                .append(this.getMermaidName())
                .append(" --> ")
                .append(String.valueOf(Math.random()))
                .println("[runCommand!!!!]");

    }
}
