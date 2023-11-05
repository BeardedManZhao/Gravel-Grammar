# Gravel-Grammar

## introduce

A processing framework for parsing various syntax such as command code and performing automatic callbacks can achieve
good syntax processing results. Registering the command class to the command callback class can achieve automatic
processing effects, and the API is concise.

### Framework access method

The framework has been uploaded to the Maven repository, and can be imported into the project through the Maven
dependencies below, and used as described.

```xml

<dependencies>
    <dependency>
        <groupId>io.github.BeardedManZhao</groupId>
        <artifactId>gravel-Grammar</artifactId>
        <version>1.0.20230927</version>
    </dependency>
</dependencies>
```

## module

Here, we have introduced many components of the framework, which helps users understand the API calling methods of the
framework, quickly get started with the framework, and integrate it into their respective projects.

### Grammar Object

Interface name: zhao.gravel.grammar.command.Syntax

#### GrammarParam class

This object is the most basic syntax class, which stores the complete structure of a syntax tree and has the query
function of sub syntax objects. By nesting this object, effective syntax tree construction can be achieved. Below is a
simple syntax tree construction example.

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
        // Instantiate the first level of a syntax object
        final Syntax syntax = getSyntax();
        // View the syntax tree structure
        System.out.println(syntax);
    }

    private static Syntax getSyntax() {
        return GrammarParam.create(
                "get",
                // instantiate the first branch of the second level of the syntax object
                GrammarParam.create(
                        "data",
                        // instantiate the executor for the get data 123 command
                        new ActuatorParam("123") {
                            @Override
                            public Object run() {
                                return "Execute the get data 123 command";
                            }
                        }
                ),
                // Instantiate the second branch of the second level of the syntax object Here is an executor
                new ActuatorParam("123") {
                    @Override
                    public Object run() {
                        return "Execute the get 123 command";
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

#### SaveParam class

As the name suggests, this is a class with save function. If you want to extract a wildcard object from this class, it
will save the currently extracted parameters as variables into a container. Generally speaking, in the final executor,
corresponding operations will be performed based on the variables. Here is a simple example.

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
        // Instantiate the first level of a syntax object
        final Syntax syntax = getSyntax();
        // View the syntax tree structure
        System.out.println(syntax);
    }

    private static Syntax getSyntax() {
        // Prepare a variable container
        HashMap<String, Object> arrayList = new ArrayList<>();
        // Start building a syntax tree with variable saving function
        return SaveParam.create(
                "use", arrayList,
                SaveParam.create(
                        Syntax.WILDCARD, arrayList,
                        // Set the executor of the use [param] show command
                        new ActuatorParam("show") {
                            @Override
                            public Object run() {
                                return "show " + arrayList.get("show");
                            }
                        },
                        // Set the executor of the use [param] rm command
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

### Callback Object

class name：zhao.gravel.grammar.core.SyntaxCallback

A callback is a component used to search for syntax tree nodes and execute their corresponding logic. It can parse
syntax according to certain rules and find the specified executor object based on the valid path of parameters. Below is
a simple example where we load the syntax tree into the callback and execute the corresponding command using the
callback.

```java
package zhao.gravel.grammar;

import zhao.gravel.grammar.command.ActuatorParam;
import zhao.gravel.grammar.command.SaveParam;
import zhao.gravel.grammar.command.Syntax;
import zhao.gravel.grammar.core.CommandCallback;
import zhao.gravel.grammar.core.model.AnalyticalModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author zhao
 */
public class MAIN {
    public static void main(String[] args) {
        // Instantiate the first layer of a grammar object
        final Syntax syntax = getSyntax();
        // Create a callback
        final CommandCallback get = CommandCallback.createGet(
                // Set the matching mode used by the grammar when parsing commands
                "\\s+",
                // Provide a syntax tree to the callback
                syntax
        );
        // Set the callback's parsing mode TODO to parse by string, which is also the default parsing mode
        get.setAnalyticalModel(AnalyticalModel.CHARACTER_PATTERN);
        // TODO, of course, can also be set to parse according to regular rules
        get.setAnalyticalModel(AnalyticalModel.REGULAR_MODEL);
        // Start executing some statements and printing the results
        System.out.println(get.run("use zhao show"));
        System.out.println(get.run("use zhao show_list"));
    }

    private static Syntax getSyntax() {
        // Prepare a variable container
        HashMap<String, Object> arrayList = new ArrayList<>();
        // Start building a syntax tree with variable saving function
        return SaveParam.create(
                "use", arrayList,
                SaveParam.create(
                        Syntax.WILDCARD, arrayList,
                        // Set the executor of the use [param] show command
                        new ActuatorParam("show") {
                            @Override
                            public Object run() {
                                return "show " + arrayList.get("show");
                            }
                        },
                        // Set the executor of the use [param] rm command
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

```
show zhao
[zhao]
```

### Distribution Construction Grammar

Here, we will show an example that reads: "Implementing the following command will store path parameters in the variable
pool, and when the result is run, it will return the Map set of variables.

```shell
java -jar [path] --class [path]
```

Next is the implementation of the relevant code and the display of the running results.

```java
package zhao.gravel.grammar;

import zhao.gravel.grammar.command.ActuatorParam;
import zhao.gravel.grammar.command.GrammarParam;
import zhao.gravel.grammar.command.SaveParam;
import zhao.gravel.grammar.command.Syntax;
import zhao.gravel.grammar.core.CommandCallback;

import java.util.HashMap;

/**
 * @author zhao
 */
public class MAIN {
    public static void main(String[] args) {
        // 首先 构建第一个语法 java 由于此语法 后面只有 -jar 其不需要存储变量 因此我们可以直接使用 GrammarParam 进行构造
        final Syntax java = GrammarParam.create("java");

        // 然后 继续实现 -jar 参数 需要注意的是 此参数的子句是个变量 因此此参数需要为 SaveParam
        // 又因为此参数的存储需要一个集合 因此在这里我们需要将集合先构建出来 然后构建 SaveParam
        final HashMap<String, Object> hashMap = new HashMap<>();
        final Syntax jar_1 = SaveParam.create("-jar", hashMap);
        // 将此语法做为 java 命令的子语法对象
        java.addSubSyntax(jar_1);

        // 接下来我们构建 -jar 的子句变量，在这里需要知道一个事情，所有为 Syntax.WILDCARD 的SaveParma 都代表是一个变量
        // 因此在这里我们需要将此参数的名字设置为 Syntax.WILDCARD
        final Syntax syntax_1 = SaveParam.create(Syntax.WILDCARD, hashMap);
        // 然后将其做为 -jar 命令的子语法
        jar_1.addSubSyntax(syntax_1);

        // 接下来我们需要构建 --class 参数 此对象的子句也是个变量 因此需要使用 SaveParam
        final Syntax class_1 = SaveParam.create("--class", hashMap);
        // 将此语法做为 -jar 的子句变量的子语法 因为在命令中 他排在 -jar 子句变量的后面
        syntax_1.addSubSyntax(class_1);

        // 最后我们开始构建执行器 因为在这里就是最后一个命令了，可以开始执行了
        // 同时又因为其是一个变量 因此这里应该是一个名称为 Syntax.WILDCARD 的执行器
        final ActuatorParam actuatorParam = new ActuatorParam(Syntax.WILDCARD) {
            @Override
            public Object run() {
                // 在这里按题目要求 将 变量的 Map 返回出来
                return hashMap.clone();
            }
        };
        // 在将执行器做为 --class 命令的子语法对象
        class_1.addSubSyntax(actuatorParam);
        // 将整个 java 命令提供给回调器
        final CommandCallback commandCallback = CommandCallback.createGet(
                // 在这里我们要指定回调器解析命令的方式 在这里是使用的按照 \\s+ 正则切分
                "\\s+", java
        );

        // 打印回调器的语法树
        System.out.println(commandCallback);
        // 开始执行
        final Object run = commandCallback.run("java -jar zhao.jar --class zhao.com.core.run.xxx");
        // 打印函数结果
        System.out.println(run);
    }
}
```

```
graph BR
5.57041912E8[java] --> 9.85922955E8[-jar]
9.85922955E8[-jar] --> 1.435804085E9[^_^]
1.435804085E9[^_^] --> 1.784662007E9[--class]
1.784662007E9[--class] --> 1.854778591E9[^_^]
1.854778591E9([^_^]) --> 0.8658649515777185[runCommand!!!!]
5.57041912E8[java] --> 1.239731077E9[notfind]

{--class=zhao.com.core.run.xxx, -jar=zhao.jar}
```

## Practical usage examples

### Manually implementing a grammar parser

```java
package zhao.gravel.grammar;

import zhao.gravel.grammar.command.ActuatorParam;
import zhao.gravel.grammar.command.SaveParam;
import zhao.gravel.grammar.command.Syntax;

/**
 * @author zhao
 */
public class MAIN {
    public static void main(String[] args) {
        final Syntax syntax = SaveParam.create(
                "c1", SaveParam.create(
                        "c2", SaveParam.create(
                                "c3", SaveParam.create(
                                        "c4"
                                )
                        )
                )
        );
        // 最后添加一个执行器 执行器本身也是一个 Syntax 对象 也可以直接在 create 中进行构造
        syntax.addSubSyntax(new ActuatorParam("run") {
            @Override
            public Object run() {
                return "ok!!!";
            }
        });
    }
}
```

The following is an example of creating a parse tree through the create function and providing the tree to the callback.

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
                // Firstly, provide the command parsing mode string, where we get it with spaces
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
        // Obtain the SQL query syntax object and set the callback function for the table and where clause
        final Syntax instance = BuiltInGrammar.SQL_SELECT.get(
                hashMap -> "当前位于表处理函数 " + hashMap,
                hashMap -> "当前位于where子句处理函数 " + hashMap,
                hashMap -> "当前位于group by处理函数 " + hashMap,
                hashMap -> "当前位于order by处理函数 " + hashMap,
                hashMap -> "当前位于  limit 处理函数 " + hashMap
        );
        // Load to callback
        final CommandCallback sql = CommandCallback.createGet(
                // Set the type of regular matching expression for SQL parsing, where it can be REGULAR_ MODEL_ 1 Parsed mode
                BuiltInReg.SQL_EXTRACTION_REGULAR_MODEL_1,
                instance
        );
        // Set the parsing mode to regular extraction of group 1 data
        sql.setAnalyticalModel(AnalyticalModel.REGULAR_MODEL_1);
        // Executing commands in a callback
        System.out.println(sql.run("select * from zhao;"));
        System.out.println(sql.run("select * from zhao where age=20;"));
        System.out.println(sql.run("select * from zhao where age=20 group by age;"));
        System.out.println(sql.run("select * from zhao where age=20 order by age limit 10 20;"));
        System.out.println(sql);
    }
}

```

The following are the running results, where we can see that two commands were successfully processed and the
corresponding results were returned. When we printed the callback, we actually printed the code of the syntax tree
structure diagram in the callback.

```
当前位于表处理函数 {select=*, from=zhao;}
当前位于where子句处理函数 {select=*, from=zhao, where=age=20;}
当前位于group by处理函数 {select=*, group by=age;, from=zhao, where=age=20}
当前位于  limit 处理函数 {select=*, order by=age, limit=10 20;, from=zhao, where=age=20}
graph BR
1.740000325E9[select] --> 9.32607259E8[^_^]
9.32607259E8[^_^] --> 1.57627094E8[from]
1.57627094E8[from] --> 7.18231523E8[^_^]
7.18231523E8[^_^] --> 1.504109395E9[order by]
1.504109395E9[order by] --> 1.025799482E9[^_^]
1.025799482E9[^_^] --> 3.98887205E8[limit]
3.98887205E8[limit] --> 1.468177767E9[^_^]
1.468177767E9([offset count]) --> 0.5627176415604082[runCommand!!!!]
1.025799482E9([Order by clause]) --> 0.19879988313180497[runCommand!!!!]
7.18231523E8[^_^] --> 3.98887205E8[limit]
3.98887205E8[limit] --> 1.468177767E9[^_^]
1.468177767E9([offset count]) --> 0.8433204212510476[runCommand!!!!]
7.18231523E8[^_^] --> 1.349414238E9[where]
1.349414238E9[where] --> 7.62218386E8[^_^]
7.62218386E8[^_^] --> 1.504109395E9[order by]
1.504109395E9[order by] --> 1.025799482E9[^_^]
1.025799482E9[^_^] --> 3.98887205E8[limit]
3.98887205E8[limit] --> 1.468177767E9[^_^]
1.468177767E9([offset count]) --> 0.12488731309599099[runCommand!!!!]
1.025799482E9([Order by clause]) --> 0.38751006766773[runCommand!!!!]
7.62218386E8[^_^] --> 3.98887205E8[limit]
3.98887205E8[limit] --> 1.468177767E9[^_^]
1.468177767E9([offset count]) --> 0.405136527007931[runCommand!!!!]
7.62218386E8[^_^] --> 1.873653341E9[group by]
1.873653341E9[group by] --> 1.908316405E9[^_^]
1.908316405E9[^_^] --> 1.504109395E9[order by]
1.504109395E9[order by] --> 1.025799482E9[^_^]
1.025799482E9[^_^] --> 3.98887205E8[limit]
3.98887205E8[limit] --> 1.468177767E9[^_^]
1.468177767E9([offset count]) --> 0.3493721134215404[runCommand!!!!]
1.025799482E9([Order by clause]) --> 0.18895448365909584[runCommand!!!!]
1.908316405E9[^_^] --> 3.98887205E8[limit]
3.98887205E8[limit] --> 1.468177767E9[^_^]
1.468177767E9([offset count]) --> 0.03925965668096232[runCommand!!!!]
1.908316405E9([group by fieldName]) --> 0.8819858478815389[runCommand!!!!]
7.62218386E8([Where clause condition]) --> 0.9041228337851761[runCommand!!!!]
7.18231523E8[^_^] --> 1.873653341E9[group by]
1.873653341E9[group by] --> 1.908316405E9[^_^]
1.908316405E9[^_^] --> 1.504109395E9[order by]
1.504109395E9[order by] --> 1.025799482E9[^_^]
1.025799482E9[^_^] --> 3.98887205E8[limit]
3.98887205E8[limit] --> 1.468177767E9[^_^]
1.468177767E9([offset count]) --> 0.9798902184562694[runCommand!!!!]
1.025799482E9([Order by clause]) --> 0.40897488489984957[runCommand!!!!]
1.908316405E9[^_^] --> 3.98887205E8[limit]
3.98887205E8[limit] --> 1.468177767E9[^_^]
1.468177767E9([offset count]) --> 0.02504592954225726[runCommand!!!!]
1.908316405E9([group by fieldName]) --> 0.8615177648386168[runCommand!!!!]
7.18231523E8([table Name]) --> 0.1773248339953667[runCommand!!!!]
```

```mermaid
graph BR
1.740000325E9[select] --> 9.32607259E8[^_^]
9.32607259E8[^_^] --> 1.57627094E8[from]
1.57627094E8[from] --> 7.18231523E8[^_^]
7.18231523E8[^_^] --> 1.504109395E9[order by]
1.504109395E9[order by] --> 1.025799482E9[^_^]
1.025799482E9[^_^] --> 3.98887205E8[limit]
3.98887205E8[limit] --> 1.468177767E9[^_^]
1.468177767E9([offset count]) --> 0.5627176415604082[runCommand!!!!]
1.025799482E9([Order by clause]) --> 0.19879988313180497[runCommand!!!!]
7.18231523E8[^_^] --> 3.98887205E8[limit]
3.98887205E8[limit] --> 1.468177767E9[^_^]
1.468177767E9([offset count]) --> 0.8433204212510476[runCommand!!!!]
7.18231523E8[^_^] --> 1.349414238E9[where]
1.349414238E9[where] --> 7.62218386E8[^_^]
7.62218386E8[^_^] --> 1.504109395E9[order by]
1.504109395E9[order by] --> 1.025799482E9[^_^]
1.025799482E9[^_^] --> 3.98887205E8[limit]
3.98887205E8[limit] --> 1.468177767E9[^_^]
1.468177767E9([offset count]) --> 0.12488731309599099[runCommand!!!!]
1.025799482E9([Order by clause]) --> 0.38751006766773[runCommand!!!!]
7.62218386E8[^_^] --> 3.98887205E8[limit]
3.98887205E8[limit] --> 1.468177767E9[^_^]
1.468177767E9([offset count]) --> 0.405136527007931[runCommand!!!!]
7.62218386E8[^_^] --> 1.873653341E9[group by]
1.873653341E9[group by] --> 1.908316405E9[^_^]
1.908316405E9[^_^] --> 1.504109395E9[order by]
1.504109395E9[order by] --> 1.025799482E9[^_^]
1.025799482E9[^_^] --> 3.98887205E8[limit]
3.98887205E8[limit] --> 1.468177767E9[^_^]
1.468177767E9([offset count]) --> 0.3493721134215404[runCommand!!!!]
1.025799482E9([Order by clause]) --> 0.18895448365909584[runCommand!!!!]
1.908316405E9[^_^] --> 3.98887205E8[limit]
3.98887205E8[limit] --> 1.468177767E9[^_^]
1.468177767E9([offset count]) --> 0.03925965668096232[runCommand!!!!]
1.908316405E9([group by fieldName]) --> 0.8819858478815389[runCommand!!!!]
7.62218386E8([Where clause condition]) --> 0.9041228337851761[runCommand!!!!]
7.18231523E8[^_^] --> 1.873653341E9[group by]
1.873653341E9[group by] --> 1.908316405E9[^_^]
1.908316405E9[^_^] --> 1.504109395E9[order by]
1.504109395E9[order by] --> 1.025799482E9[^_^]
1.025799482E9[^_^] --> 3.98887205E8[limit]
3.98887205E8[limit] --> 1.468177767E9[^_^]
1.468177767E9([offset count]) --> 0.9798902184562694[runCommand!!!!]
1.025799482E9([Order by clause]) --> 0.40897488489984957[runCommand!!!!]
1.908316405E9[^_^] --> 3.98887205E8[limit]
3.98887205E8[limit] --> 1.468177767E9[^_^]
1.468177767E9([offset count]) --> 0.02504592954225726[runCommand!!!!]
1.908316405E9([group by fieldName]) --> 0.8615177648386168[runCommand!!!!]
7.18231523E8([table Name]) --> 0.1773248339953667[runCommand!!!!]
```