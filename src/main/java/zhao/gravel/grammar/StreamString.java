package zhao.gravel.grammar;

import java.io.PrintWriter;

/**
 * 当前类用于支持有关toString函数的流程图模式的调用，通过此类，可以实现将当前类的字符串传递给一个 PrintStream 对象的效果。
 * <p>
 * The current class is used to support the call of flowchart patterns related to the toString function. Through this class, the effect of passing the string of the current class to a PrintStream object can be achieved.
 *
 * @author zhao
 */
public abstract class StreamString {
    /**
     * 将当前回调器中包含的所有子语法树的图以 mermaid 的方式绘制出来。
     * <p>
     * Draw a graph of all sub syntax trees contained in the current grammar in mermaid format.
     *
     * @param isLR      是否以左右的方式来进行图的绘制，如果是就为true。
     *                  <p>
     *                  Is the drawing done in a left and right manner? If so, it is true.
     * @param outStream 图代码的输出数据流。
     *                  <p>
     *                  graph code.
     */
    public void toString(PrintWriter outStream, boolean isLR) {
        outStream.append("graph ").println(isLR ? "LR" : "BR");
        this.toString(outStream);
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
    public abstract void toString(PrintWriter outStream);
}
