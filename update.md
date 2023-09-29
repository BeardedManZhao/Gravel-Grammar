# 更新日志

## 版本1.0.20230929

### SQL解析模式的优化

* 支持针对去重关键字的解析支持
* 支持针对大小写语法的解析支持，能够不区分大小写。

```java
package zhao.gravel.grammar;

import zhao.gravel.grammar.command.Syntax;
import zhao.gravel.grammar.core.BuiltInGrammar;
import zhao.gravel.grammar.core.BuiltInReg;
import zhao.gravel.grammar.core.CommandCallback;
import zhao.gravel.grammar.core.model.AnalyticalModel;

/**
 * @author zhao
 */
public class MAIN {
    public static void main(String[] args) {
        // 获取到 SQL查询 语法对象 并设置 表 与 where子句的回调函数
        final Syntax instance = BuiltInGrammar.SQL_SELECT.get(
                hashMap -> "当前位于表处理函数 " + hashMap,
                hashMap -> "当前位于where子句处理函数 " + hashMap,
                hashMap -> "当前位于group by处理函数 " + hashMap,
                hashMap -> "当前位于order by处理函数 " + hashMap,
                hashMap -> "当前位于  limit 处理函数 " + hashMap
        );
        // 装载到回调器
        final CommandCallback sql = CommandCallback.createGet(
                // 设置 SQL解析 的正则匹配表达式类型，在这里使用的是能够被 REGULAR_MODEL_1 解析的模式
                BuiltInReg.SQL_EXTRACTION_REGULAR_MODEL_1,
                instance
        );
        // 设置解析模式为 正则提取1号组数据
        sql.setAnalyticalModel(AnalyticalModel.REGULAR_MODEL_1);
        // 在回调器中执行命令
        System.out.println(sql.run("SELECT * from zhao;"));
        System.out.println(sql.run("select * FROM zhao where age=20;"));
        System.out.println(sql.run("select * from zhao WHERE age=20 group by age;"));
        System.out.println(sql.run("select * from zhao where age=20 OrDer by age limit 10 20;"));
        System.out.println(sql.run("select", "*", "from", "zhao;"));
    }
}
```

```
当前位于表处理函数 {select=*, from=zhao;}
当前位于where子句处理函数 {select=*, from=zhao, where=age=20;}
当前位于group by处理函数 {select=*, group by=age;, from=zhao, where=age=20}
当前位于  limit 处理函数 {select=*, order by=age, limit=10 20, from=zhaoTable, where=age=20}
当前位于表处理函数 {select=*, from=zhao;}

进程已结束,退出代码0
```

* 支持通过语法树中的每个参数对象的 "getINFO" 函数获取到有关当前参数的信息与帮助信息。

```java
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
```

```
此处应为被查询的字段
这里输入的应为一个字符串，用于标识需要做为分组字段的字段名。

进程已结束,退出代码0
```
