package zhao.gravel.grammar.command;

import zhao.gravel.grammar.StreamString;
import zhao.utils.IOUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhao
 */
public class GrammarParam extends StreamString implements Syntax {

    protected final HashMap<String, Syntax> syntaxHashMap;
    private final String name;
    private final int hash;
    protected Syntax defaultSyntax;

    protected GrammarParam(String name, Syntax... allSyntax) {
        this.name = name;
        syntaxHashMap = new HashMap<>(allSyntax.length + 4);
        for (Syntax syntax : allSyntax) {
            syntaxHashMap.put(syntax.getSyntaxName(), syntax);
        }
        this.defaultSyntax = this.syntaxHashMap.getOrDefault(NotFindParam.WILDCARD, NotFindParam.NOT_FIND);
        hash = this.hashCode();
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
        if (WILDCARD.equals(syntax.getSyntaxName())) {
            this.defaultSyntax = syntax;
        }
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
        final Syntax syntax = this.get(WILDCARD);
        if (syntax != null) {
            this.defaultSyntax = syntax;
        }
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
        final Syntax syntax = this.syntaxHashMap.get(syntaxName);
        if (syntax != null) {
            return syntax;
        } else {
            return this.getDefault(syntaxName);
        }
    }

    /**
     * 获取到默认的语法对象，当无法获取到子语法的时候，将会直接调用此函数，并将函数返回的语法对象做为下一个执行。
     *
     * @param syntaxName 语法名称。
     * @return 默认的子语法对象。
     */
    @Override
    public Syntax getDefault(String syntaxName) {
        return this.defaultSyntax;
    }

    /**
     * 不指定方向的添加元素
     *
     * @param outStream 图代码的输出数据流。
     *                  <p>
     *                  graph code.
     */
    @Override
    public void toString(PrintWriter outStream) {
        for (Syntax value : this.syntaxHashMap.values()) {
            outStream.append(String.valueOf(this.hash)).append('[').append(this.getSyntaxName()).append(']')
                    .append(" --> ")
                    .append(String.valueOf(value.getHashId())).append('[').append(value.getSyntaxName()).println(']');
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

    @Override
    public String toString() {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        this.toString(printWriter, false);
        final String s = stringWriter.getBuffer().toString();
        IOUtils.close(stringWriter);
        IOUtils.close(printWriter);
        return s;
    }

    /**
     * 将 当前语法对象以及其子语法对象中用于变量存储的 list 对象 清空。
     * <p>
     * Clear the list object used for variable storage in the current syntax object and its child syntax objects
     */
    @Override
    public void clearVariable() {
        for (Syntax value : this.syntaxHashMap.values()) {
            if (value instanceof SaveParam) {
                value.clearVariable();
            }
        }
    }
}
