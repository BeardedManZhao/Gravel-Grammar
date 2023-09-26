# Gravel-Grammar

## introduce

A processing framework for parsing various syntax such as command code and performing automatic callbacks can achieve
good syntax processing results. Registering the command class to the command callback class can achieve automatic
processing effects, and the API is concise.

### Framework access method

The framework has been uploaded to the Maven repository, and can be imported into the project through the Maven
dependencies below, and used as described.

```xml

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
        ArrayList<Object> arrayList = new ArrayList<>();
        // Start building a syntax tree with variable saving function
        return SaveParam.create(
                "use", arrayList,
                SaveParam.create(
                        Syntax.WILDCARD, arrayList,
                        // Set the executor of the use [param] show command
                        new ActuatorParam("show") {
                            @Override
                            public Object run() {
                                return "show " + arrayList.get(0);
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
        ArrayList<Object> arrayList = new ArrayList<>();
        // Start building a syntax tree with variable saving function
        return SaveParam.create(
                "use", arrayList,
                SaveParam.create(
                        Syntax.WILDCARD, arrayList,
                        // Set the executor of the use [param] show command
                        new ActuatorParam("show") {
                            @Override
                            public Object run() {
                                return "show " + arrayList.get(0);
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
                arrayList -> "当前位于表处理函数 " + arrayList,
                arrayList -> "当前位于where子句处理函数 " + arrayList
        );
        // Load to callback
        final CommandCallback sql = CommandCallback.createGet(
                " ",
                instance
        );
        // Executing commands in a callback
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
