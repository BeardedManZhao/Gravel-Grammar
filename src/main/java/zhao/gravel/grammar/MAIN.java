package zhao.gravel.grammar;

import zhao.gravel.grammar.core.BuiltInGrammar;
import zhao.gravel.grammar.core.BuiltInReg;
import zhao.gravel.grammar.core.CommandCallback;

/**
 * @author zhao
 */
public class MAIN {
    public static void main(String[] args) {
        // 获取到 SQL_Select 语法树 对应的回调函数
        final CommandCallback callback = CommandCallback.createGet(
                BuiltInReg.SQL_EXTRACTION_REGULAR_MODEL_1, BuiltInGrammar.SQL_SELECT.get()
        );
        // 打印其中的语法树
        System.out.println(callback.get("select").get("*").get("from").get("data").get("where"));
    }
}
