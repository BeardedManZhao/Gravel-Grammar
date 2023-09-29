package zhao.gravel.grammar.command;

import java.io.PrintWriter;

/**
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
