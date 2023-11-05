package zhao.gravel.grammar.command;

/**
 * 构造一个具有别名支持的执行器对象，在此执行器中，如果提供了别名，那么就可以使用两个名字来对待此执行器。
 * <p>
 * Construct an executor object with alias support, in which if an alias is provided, two names can be used to treat this executor.
 * <p>
 * PS：一般来说，别名是用来展示，原名是用来做映射的。
 *
 * @author zhao
 */
public abstract class ActuatorAliasParam extends ActuatorParam {

    protected final String alias;

    /**
     * 执行器的名称
     *
     * @param name 执行器的名称 会在语法树的图中显示，也可以做为语法对象使用
     */
    protected ActuatorAliasParam(String name) {
        super(name);
        this.alias = super.getSyntaxName();
    }

    protected ActuatorAliasParam(String name, String aliasName) {
        this(name, aliasName + ": No Help Info!!!", aliasName);
    }

    protected ActuatorAliasParam(String name, String help_info, String aliasName) {
        super(name, help_info);
        this.alias = aliasName;
    }

    /**
     * @return 当前执行器组件的别名，别名与原名称不会互相干扰，具有不同的作用。
     * <p>
     * The alias of the current actuator component, which does not interfere with the original name and has different functions.
     */
    public String getAliasName() {
        return alias;
    }

    /**
     * @return 当前执行器对象在 mermaid 图中的名字代码。
     * <p>
     * The name code of the current executor object in the mermaid diagram.
     */
    @Override
    protected String getMermaidName() {
        return this.getHashId() + "([" + this.getAliasName() + "])";
    }
}
