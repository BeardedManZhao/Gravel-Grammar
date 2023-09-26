package zhao.gravel.grammar.command;

import java.io.PrintWriter;

/**
 * @author zhao
 */
public abstract class ActuatorParam extends GrammarParam {


    protected ActuatorParam(String name) {
        super(name);
    }

    /**
     * @return 当前执行器参数的执行逻辑函数，执行完毕之后会返回一个任意数据类型。
     * <p>
     * The execution logic function of the current executor parameter will return an arbitrary data type after execution.
     */
    public abstract Object run();

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
                .append(String.valueOf(this.getHashId()))
                .append("([")
                .append(this.getSyntaxName())
                .append("]) --> ")
                .append(String.valueOf(Math.random()))
                .println("[runCommand!!!!]");

    }

}
