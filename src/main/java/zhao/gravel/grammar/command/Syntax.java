package zhao.gravel.grammar.command;

import java.util.Map;

/**
 * 语法对象，一般情况下此是一个嵌套对象，能够按照层的方式来存储语法的不同部分。
 * <p>
 * A grammar object, usually a nested object, can store different parts of the grammar in a hierarchical manner.
 */
public interface Syntax {

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


}
