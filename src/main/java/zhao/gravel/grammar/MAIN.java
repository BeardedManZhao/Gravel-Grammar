package zhao.gravel.grammar;

import zhao.gravel.grammar.core.BuiltInGrammar;
import zhao.gravel.grammar.core.BuiltInReg;
import zhao.gravel.grammar.core.CommandCallback;
import zhao.gravel.grammar.core.model.AnalyticalModel;

/**
 * @author zhao
 */
public class MAIN {
    public static void main(String[] args) {
        // 装载到回调器
        final CommandCallback sql = CommandCallback.createGet(
                // 设置 SQL解析 的正则匹配表达式类型，在这里使用的是能够被 REGULAR_MODEL_1 解析的模式
                BuiltInReg.SQL_EXTRACTION_REGULAR_MODEL_1,
                // 获取到 SQL查询 语法对象 并设置 表 与 where子句的回调函数
                BuiltInGrammar.SQL_SELECT.get(
                        hashMap -> "当前位于表处理函数 " + hashMap
                )
        );
        // 设置解析模式为 正则提取1号组数据
        sql.setAnalyticalModel(AnalyticalModel.REGULAR_MODEL_1);
        // 在回调器中执行命令
        System.out.println(sql.run("SELECT DISTINCT * from zhao;"));
    }
}
