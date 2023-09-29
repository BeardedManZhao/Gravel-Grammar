package zhao.gravel.grammar;

import zhao.gravel.grammar.command.Syntax;
import zhao.gravel.grammar.core.BuiltInGrammar;

/**
 * @author zhao
 */
public class MAIN {
    public static void main(String[] args) {
        // 获取到 SQL_Select 语法树
        final Syntax syntax = BuiltInGrammar.SQL_SELECT.get();
        // 从其中获取到 select 参数后面 的参数 应该怎么写 在这里我们随意指定一个字段
        final Syntax syntax1 = syntax.get("name");
        // 查看 参数 name 位置的帮助信息
        System.out.println(syntax1.getINFO());
        // 从其中获取到 group by 参数后面 的参数 应该怎么写 在这里我们随意指定一个字段 age
        final Syntax syntax2 = syntax.get("name").get("from").get("zhaoTable").get("group by").get("age");
        // 查看 参数 age 位置的帮助信息
        System.out.println(syntax2.getINFO());
    }
}
