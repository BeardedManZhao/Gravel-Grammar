package zhao.gravel.grammar.command;

import java.util.ArrayList;
import java.util.Map;

/**
 * 具有保存变量功能的参数对象，该参数的子对象如果为 SaveParam 对象，那么子对象将会直接继承当前param保存的所有变量。
 *
 * @author zhao
 */
public class SaveParam extends GrammarParam {

    protected final ArrayList<Object> arrayList;

    protected SaveParam(String name, ArrayList<Object> arrayList, Syntax... allSyntax) {
        super(name, allSyntax);
        for (Syntax syntax : allSyntax) {
            if (syntax instanceof SaveParam) {
                final ArrayList<Object> arrayList1 = ((SaveParam) syntax).getArrayList();
                if (arrayList1 != arrayList) {
                    // 内存地址不一样 向子语法中添加保存的元素
                    arrayList1.addAll(this.getArrayList());
                }
            }
        }
        this.arrayList = arrayList;
    }

    /**
     * 创建出一个语法对象(需要注意的是，所有的子语法对象都应该与当前的语法对象存储同一个 arrayList 容器)。
     *
     * @param name      该语法对象对应的参数名称。
     * @param arrayList 该语法对象用于变量保存操作的容器 (需要注意的是，所有的子语法对象都应该与当前的语法对象存储同一个 arrayList 容器)。。
     * @param allSyntax 该语法对象中的所有子语法对象(需要注意的是，所有的子语法对象都应该与当前的语法对象存储同一个 arrayList 容器)。
     * @return 语法对象
     */
    public static Syntax create(String name, ArrayList<Object> arrayList, Syntax... allSyntax) {
        return new SaveParam(name, arrayList, allSyntax);
    }

    /**
     * 创建出一个语法对象
     *
     * @param name      该语法对象对应的参数名称。
     * @param allSyntax 该语法对象中的所有子语法对象
     * @return 语法对象
     */
    public static Syntax create(String name, Syntax... allSyntax) {
        return new SaveParam(name, new ArrayList<>(), allSyntax);
    }

    public void save(Object object) {
        this.arrayList.add(object);
    }

    public ArrayList<Object> getArrayList() {
        return this.arrayList;
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
        final Syntax syntax = super.get(syntaxName);
        if (WILDCARD.equals(syntax.getSyntaxName())) {
            // 将当前参数存储到list中。
            this.save(syntaxName);
        }
        // 然后才继续下一层解析。
        return syntax;
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
        // 首先判断当前被添加的子语法是否支持变量的保存
        if (syntax instanceof SaveParam) {
            // 支持就直接将当前保存的所有数据提供给下一层
            ((SaveParam) syntax).arrayList.addAll(this.getArrayList());
        }
        super.addSubSyntax(syntax);
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
        throw new UnsupportedOperationException("Please call the 'addSubSyntax (Syntax syntax)' function to append the sub syntax.");
    }

    /**
     * 将 当前语法对象以及其子语法对象中用于变量存储的 list 对象 清空。
     * <p>
     * Clear the list object used for variable storage in the current syntax object and its child syntax objects
     */
    @Override
    public void clearVariable() {
        this.arrayList.clear();
        super.clearVariable();
    }
}
