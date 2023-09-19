package zhao.gravel.grammar.command;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhao
 */
public class GrammarParam implements Syntax {

    private final HashMap<String, Syntax> syntaxHashMap;
    private final String name;

    protected GrammarParam(String name, Syntax... allSyntax) {
        this.name = name;
        syntaxHashMap = new HashMap<>(allSyntax.length + 4);
        for (Syntax syntax : allSyntax) {
            syntaxHashMap.put(syntax.getSyntaxName(), syntax);
        }
    }

    /**
     * 创建出一个语法对象
     *
     * @param name      该语法对象对应的参数名称。
     * @param allSyntax 该语法对象中的所有子语法对象
     * @return 语法对象
     */
    public static Syntax create(String name, Syntax... allSyntax) {
        final GrammarParam grammarParam = new GrammarParam(name, allSyntax);
        grammarParam.addSubSyntax(NotFindParam.NOT_FIND);
        return grammarParam;
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
        this.syntaxHashMap.put(syntax.getSyntaxName(), syntax);
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
        this.syntaxHashMap.putAll(allSyntax);
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
        return this.syntaxHashMap.get(syntaxName);
    }
}
