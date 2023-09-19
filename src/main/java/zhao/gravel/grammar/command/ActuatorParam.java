package zhao.gravel.grammar.command;

import java.util.Map;

/**
 * @author zhao
 */
public abstract class ActuatorParam implements Syntax {

    private final String name;

    protected ActuatorParam(String name) {
        this.name = name;
    }

    /**
     * @return 当前语法对象 对应的参数名称。
     */
    @Override
    public String getSyntaxName() {
        return this.name;
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
        throw new UnsupportedOperationException("The actuator parameter does not have the ability to store sub syntax, and the actuator parameter object should be the last added object");
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
        throw new UnsupportedOperationException("The actuator parameter does not have the ability to store sub syntax, and the actuator parameter object should be the last added object");
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
        return null;
    }

    /**
     * @return 当前执行器参数的执行逻辑函数，执行完毕之后会返回一个任意数据类型。
     * <p>
     * The execution logic function of the current executor parameter will return an arbitrary data type after execution.
     */
    public abstract Object run();
}
