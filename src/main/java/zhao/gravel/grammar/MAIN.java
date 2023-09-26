package zhao.gravel.grammar;

import zhao.gravel.grammar.command.ActuatorParam;
import zhao.gravel.grammar.command.SaveParam;
import zhao.gravel.grammar.command.Syntax;
import zhao.gravel.grammar.core.CommandCallback;
import zhao.gravel.grammar.core.model.AnalyticalModel;

import java.util.ArrayList;

/**
 * @author zhao
 */
public class MAIN {
    public static void main(String[] args) {
        // 实例化一个语法对象的第一层
        final Syntax syntax = getSyntax();
        // 创建一个回调器
        final CommandCallback get = CommandCallback.createGet(
                // 设置语法器在解析命令的时候使用的匹配模式
                "\\s+",
                // 将语法树提供给回调器
                syntax
        );
        // 设置回调器的解析模式 TODO 设置为按照字符串解析 这也是默认的解析模式
        get.setAnalyticalModel(AnalyticalModel.CHARACTER_PATTERN);
        // TODO 当然，也可以设置为按照正则解析
        get.setAnalyticalModel(AnalyticalModel.REGULAR_MODEL);
        // 开始执行一些语句并打印结果
        System.out.println(get.run("use zhao show"));
        System.out.println(get.run("use zhao show_list"));
    }

    private static Syntax getSyntax() {
        // 准备一个变量容器
        ArrayList<Object> arrayList = new ArrayList<>();
        // 开始构建 具有变量保存功能的语法树
        return SaveParam.create(
                "use", arrayList,
                SaveParam.create(
                        Syntax.WILDCARD, arrayList,
                        // 设置 use [param] show 命令的执行器
                        new ActuatorParam("show") {
                            @Override
                            public Object run() {
                                return "show " + arrayList.get(0);
                            }
                        },
                        // 设置 use [param] rm 命令的执行器
                        new ActuatorParam("show_list") {
                            @Override
                            public Object run() {
                                return arrayList.clone();
                            }
                        }
                )
        );
    }
}
