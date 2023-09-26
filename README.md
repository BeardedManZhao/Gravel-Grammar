# Gravel-Grammar

## introduce

A processing framework for parsing various syntax such as command code and performing automatic callbacks can achieve
good syntax processing results. Registering the command class to the command callback class can achieve automatic
processing effects, and the API is concise.

### 获取方式

```xml

```

## 模块

### 语法对象

接口名称：zhao.gravel.grammar.command.Syntax

#### GrammarParam 类

此对象是一个最基本的语法类，其中存储着一个语法树的完整结构，并具有子语法对象的查询功能，通过针对此对象的嵌套，可以实现有效的语法树的构造，下面就是一个简单的语法树构造示例。

```java
package zhao.gravel.grammar;

import zhao.gravel.grammar.command.ActuatorParam;
import zhao.gravel.grammar.command.GrammarParam;
import zhao.gravel.grammar.command.Syntax;

/**
 * @author zhao
 */
public class MAIN {
    public static void main(String[] args) {
        // 实例化一个语法对象的第一层
        final Syntax syntax = getSyntax();
        // 查看语法树结构
        System.out.println(syntax);
    }

    private static Syntax getSyntax() {
        return GrammarParam.create(
                "get",
                // 实例化语法对象的第二层的第一个分支
                GrammarParam.create(
                        "data",
                        // 实例化 get data 123 命令的执行器
                        new ActuatorParam("123") {
                            @Override
                            public Object run() {
                                return "执行 get data 123 命令";
                            }
                        }
                ),
                // 实例化语法对象的第二层的第二个分支 这里是一个执行器
                new ActuatorParam("123") {
                    @Override
                    public Object run() {
                        return "执行 get 123 命令";
                    }
                }
        );
    }
}
```

```mermaid
graph BR
509886383[get] --> 997110508[123]
997110508([123]) --> 0.28360680445372355[runCommand!!!!]
509886383[get] --> 1435804085[data]
1435804085[data] --> 985922955[123]
985922955([123]) --> 0.5785985540770648[runCommand!!!!]
1435804085[data] --> 569859827[notFind]
509886383[get] --> 900317552[notFind]
```

#### SaveParam 类

顾名思义这是一个具有保存功能的类，如果想要从此类中提取出一个通配对象，那么其会将当前提取的参数做为变量保存到一个容器中，一般来说，在最终执行器中会根据变量来进行相对应的操作，接下来是一个简单的示例。

```java
package zhao.gravel.grammar;

import zhao.gravel.grammar.command.ActuatorParam;
import zhao.gravel.grammar.command.SaveParam;
import zhao.gravel.grammar.command.Syntax;

import java.util.ArrayList;

/**
 * @author zhao
 */
public class MAIN {
    public static void main(String[] args) {
        // 实例化一个语法对象的第一层
        final Syntax syntax = getSyntax();
        // 查看语法树结构
        System.out.println(syntax);
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
                                return arrayList;
                            }
                        }
                )
        );
    }
}
```

```mermaid
graph BR
1854778591[use] --> 509886383[^_^]
509886383[^_^] --> 985922955[show]
985922955([show]) --> 0.5214996948029694[runCommand!!!!]
509886383[^_^] --> 1784662007[rm]
1784662007([rm]) --> 0.29766585007391[runCommand!!!!]
```

### 回调器对象

类名称：zhao.gravel.grammar.core.SyntaxCallback

回调器是一种用于搜索语法树节点，并执行其对应逻辑的组件，其能够按照一定的规则来进行语法的解析，同时可以根据参数的有效路径找到指定的执行器对象，下面就是一个简单的示例，在示例中我们将语法树装载到了回调器中，并使用回调器执行了对应的命令。

```java
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

```

```
show zhao
[zhao]
```

## Practical usage examples

### 手动实现语法解析器

[image]

在下面展示的就是通过 create 函数创建一个解析树并将树提供给回调器的操作示例。

```java
package zhao.gravel.grammar;

import zhao.gravel.grammar.command.ActuatorParam;
import zhao.gravel.grammar.command.GrammarParam;
import zhao.gravel.grammar.command.Syntax;
import zhao.gravel.grammar.core.CommandCallback;
import zhao.gravel.grammar.core.SyntaxCallback;

/**
 * @author zhao
 */
public class MAIN {
    public static void main(String[] args) {
        // Build the first layer of command parser
        final Syntax echo = GrammarParam.create(
                "echo",
                // Build the first branch of the second layer of the command parser
                GrammarParam.create(
                        "[zhao]",
                        // Building the third layer of the command parser, as it is the last layer, we directly use the executor
                        new ActuatorParam("name") {
                            /**
                             * @return 当前执行器参数的执行逻辑函数，执行完毕之后会返回一个任意数据类型。
                             * <p>
                             * The execution logic function of the current executor parameter will return an arbitrary data type after execution.
                             */
                            @Override
                            public Object run() {
                                return "zhao的名字是赵凌宇";
                            }
                        },
                        new ActuatorParam("age") {
                            /**
                             * @return 当前执行器参数的执行逻辑函数，执行完毕之后会返回一个任意数据类型。
                             * <p>
                             * The execution logic function of the current executor parameter will return an arbitrary data type after execution.
                             */
                            @Override
                            public Object run() {
                                return "zhao的年龄是20岁";
                            }
                        }
                ),
                // Here is another branch of the second layer where the executor is directly added
                new ActuatorParam("zhao") {
                    @Override
                    public Object run() {
                        return "zhao";
                    }
                }
        );
        // Instantiate a callback class and load the echo command object into the callback function class
        final SyntaxCallback syntaxCallback = CommandCallback.create(
                // Firstly, provide the command parsing mode string, where we split it with spaces
                " ",
                // Then we provide parameter objects
                echo
        );
        // Start running command
        System.out.println(syntaxCallback.run("echo [zhao] name"));
        System.out.println(syntaxCallback.run("echo [zhao] age"));
        System.out.println(syntaxCallback.run("echo zhao"));
        // The following is the structure diagram of the syntax tree

        /*
         *       echo
         *     /      \
         *   zhao    [zhao]
         *    |     /      \
         *   执行  name    age
         *          |      |
         *         执行    执行
         * */

    }
}

```

### 使用内置的语法树对象

内置语法树是一些常用的通用的语法树对象，我们通过 BuiltInGrammar 枚举类存储这类语法树，接下来我们在下面展示了使用内置SQL查询语法树的示例。

```java
package zhao.gravel.grammar;

import zhao.gravel.grammar.command.Syntax;
import zhao.gravel.grammar.core.BuiltInGrammar;
import zhao.gravel.grammar.core.CommandCallback;

/**
 * @author zhao
 */
public class MAIN {
    public static void main(String[] args) {
        // 获取到 SQL查询 语法对象 并设置 表 与 where子句的回调函数
        final Syntax instance = BuiltInGrammar.SQL_SELECT.get(
                arrayList -> "当前位于表处理函数 " + arrayList,
                arrayList -> "当前位于where子句处理函数 " + arrayList
        );
        // 装载到回调器
        final CommandCallback sql = CommandCallback.createGet(
                " ",
                instance
        );
        // 在回调器中执行命令
        System.out.println(sql.run("select * from zhao;"));
        System.out.println(sql.run("select * from zhao where age=20;"));
        System.out.println(sql);
    }
}

```

下面是运行结果，可以看到其中成功地处理了两个命令并将对应的结果返回了出来，而在我们打印了回调器的时候，实际上是将回调器中的语法树结构图的代码打印了出来。

```
当前位于表处理函数 [*, zhao;]
当前位于where子句处理函数 [*, zhao, age=20;]
graph BR
434091818[select] --> 1468177767[^_^]
1468177767[^_^] --> 1531333864[from]
1531333864[from] --> 1360767589[^_^]
1360767589[^_^] --> 818403870[where]
818403870[where] --> 920011586[^_^]
920011586([^_^]) --> 0.31244290887439063[runCommand!!!!]
1360767589([^_^]) --> 0.9466675124489518[runCommand!!!!]
```

```mermaid
graph BR
434091818[select] --> 1468177767[^_^]
1468177767[^_^] --> 1531333864[from]
1531333864[from] --> 1360767589[^_^]
1360767589[^_^] --> 818403870[where]
818403870[where] --> 920011586[^_^]
920011586([^_^]) --> 0.31244290887439063[runCommand!!!!]
1360767589([^_^]) --> 0.9466675124489518[runCommand!!!!]
```
