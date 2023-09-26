package zhao.gravel.grammar.command;

import java.io.PrintWriter;
import java.util.Map;

/**
 * 语法对象，一般情况下此是一个嵌套对象，能够按照层的方式来存储语法的不同部分。
 * <p>
 * A grammar object, usually a nested object, can store different parts of the grammar in a hierarchical manner.
 */
public interface Syntax extends Cloneable {

    /**
     * 统配符号，以该符号做为参数名称的将会被做为默认的参数对象。
     */
    String WILDCARD = "^_^";

    /**
     * @return 当前语法对象 对应的参数名称。
     */
    String getSyntaxName();

    /**
     * 向此语法对象添加子语法树对象，子语法树将可以被此语法树调用。
     * <p>
     * Add a sub syntax tree object to this syntax object, and the sub syntax tree will be called by this syntax tree.
     *
     * @param syntax 需要被添加的子语法树对象。
     *               <p>
     *               The sub syntax tree object that needs to be added.
     */
    void addSubSyntax(Syntax syntax);

    /**
     * 向此语法对象添加子语法树对象，子语法树将可以被此语法树调用。
     * <p>
     * Add a sub syntax tree object to this syntax object, and the sub syntax tree will be called by this syntax tree.
     *
     * @param allSyntax 需要被添加的子语法树对象。
     *                  <p>
     *                  The sub syntax tree object that needs to be added.
     */
    void addSubSyntax(Map<String, Syntax> allSyntax);

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
    Syntax get(String syntaxName);

    /**
     * 获取到默认的语法对象，当无法获取到子语法的时候，将会直接调用此函数，并将函数返回的语法对象做为下一个执行。
     *
     * @param syntaxName 语法名称。
     * @return 默认的子语法对象。
     */
    Syntax getDefault(String syntaxName);

    /**
     * 将当前回调器中包含的所有子语法树的图以 mermaid 的方式绘制出来。
     * <p>
     * Draw a graph of all sub syntax trees contained in the current grammar in mermaid format.
     *
     * @param outStream 图代码的输出数据流。
     *                  <p>
     *                  graph code.
     */
    void toString(PrintWriter outStream);

    /**
     * @return hashcode
     */
    int getHashId();

    /**
     * 将 当前语法对象以及其子语法对象中用于变量存储的 list 对象 清空。
     * <p>
     * Clear the list object used for variable storage in the current syntax object and its child syntax objects
     */
    void clearVariable();

    /**
     * @return 克隆出来的语法对象。
     * <p>
     * The cloned grammar object.
     */
    Syntax clone();
}
