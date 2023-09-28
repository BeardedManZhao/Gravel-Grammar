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