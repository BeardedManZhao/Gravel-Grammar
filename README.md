# Gravel-Grammar

## introduce

A processing framework for parsing various syntax such as command code and performing automatic callbacks can achieve
good syntax processing results. Registering the command class to the command callback class can achieve automatic
processing effects, and the API is concise.

## Practical usage examples

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