package zhao.gravel.grammar.command;

import zhao.gravel.grammar.StreamString;
import zhao.utils.IOUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author zhao
 */
public class GrammarParam extends StreamString implements Syntax {

    protected final HashMap<String, Syntax> syntaxHashMap;
    private final String name;
    private final String help_info;
    protected Syntax defaultSyntax;
    private int hash;
    private boolean randomHash;

    protected GrammarParam(String name, boolean toLower, String help_info, Syntax... allSyntax) {
        this.help_info = help_info;
        // 为什么这里改了之后 parse 出来的结果就不对了
        this.name = toLower ? name.toLowerCase(Locale.ROOT) : name;
        syntaxHashMap = new HashMap<>(allSyntax.length + 4);
        for (Syntax syntax : allSyntax) {
            syntaxHashMap.put(syntax.getSyntaxName(), syntax);
        }
        this.randomHash = false;
        this.defaultSyntax = this.syntaxHashMap.getOrDefault(NotFindParam.WILDCARD, NotFindParam.NOT_FIND);
        hash = this.hashCode();
    }

    protected GrammarParam(String name, String help_info, Syntax... allSyntax) {
        this(name, true, help_info, allSyntax);
    }

    /**
     * 创建出一个语法对象
     *
     * @param name      该语法对象对应的参数名称。
     * @param allSyntax 该语法对象中的所有子语法对象
     * @return 语法对象
     */
    public static Syntax create(String name, Syntax... allSyntax) {
        return create(name, name + " : No Help Info!!!!", allSyntax);
    }

    /**
     * 创建出一个语法对象
     *
     * @param name      该语法对象对应的参数名称。
     * @param help_info 该语法对象相关的解析信息。
     * @param allSyntax 该语法对象中的所有子语法对象
     * @return 语法对象
     */
    public static Syntax create(String name, String help_info, Syntax... allSyntax) {
        final GrammarParam grammarParam = new GrammarParam(name, help_info, allSyntax);
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
        final Syntax syntax = this.syntaxHashMap.get(syntaxName.toLowerCase(Locale.ROOT));
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
            outStream.append(String.valueOf(this.getHashId())).append('[').append(this.getSyntaxName()).append(']')
                    .append(" --> ")
                    .append(String.valueOf(value.getHashId())).append('[').append(value.getSyntaxName()).println(']');
            value.toString(outStream);
        }
    }

    /**
     * @return 如果希望随机hash数值展示的不是真正的 hash 而是一个随机数值，这里返回的为 true；
     */
    @Override
    public boolean isRandomHash() {
        return this.randomHash;
    }

    /**
     * @param isRandomHash 如果希望随机hash数值展示的不是真正的 hash 而是一个随机数值，这里应设置为 true；
     */
    @Override
    public void setRandomHash(boolean isRandomHash) {
        this.randomHash = isRandomHash;
    }

    /**
     * @return hashcode
     */
    @Override
    public double getHashId() {
        return this.isRandomHash() ? Math.random() : this.hash;
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

    @Override
    public GrammarParam clone() {
        try {
            GrammarParam clone = (GrammarParam) super.clone();
            // TODO: 复制此处的可变状态，这样此克隆就不能更改初始克隆的内部
            clone.hash = clone.hashCode();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     * @return 当前语法参数对象对应的帮助信息，可能是其代表的意义，也可能是其使用方法。
     * <p>
     * The help information corresponding to the current syntax parameter object may be its representative meaning or its usage method.
     */
    @Override
    public String getINFO() {
        return this.help_info;
    }
}
