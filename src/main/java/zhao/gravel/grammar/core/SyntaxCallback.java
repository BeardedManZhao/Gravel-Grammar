package zhao.gravel.grammar.core;

import zhao.gravel.grammar.core.model.AnalyticalModel;

/**
 * 语法回调器接口，其中包含针对一些命令语法对象的存储与回调处理的函数。
 * <p>
 * Syntax callback interface, which includes functions for storing and callback processing of some command syntax objects.
 */
public interface SyntaxCallback {

    /**
     * 设置本回调类在解析命令的时候要使用的语法解析模式。
     *
     * @param analyticalModel 需要使用的语法解析模式。
     *                        <p>
     *                        The syntax parsing mode that needs to be used.
     * @see zhao.gravel.grammar.core.model.AnalyticalModel
     */
    void setAnalyticalModel(AnalyticalModel analyticalModel);

    /**
     * 运行一个命令，在这里会把命令传递给语法树去逐一执行与处理。
     * <p>
     * Run a command, where it will be passed to the syntax tree for execution and processing one by one.
     *
     * @param grammar 需要被解析的命令，在这里是一个字符串整体，回调类会自动的根据解析模式进行拆分。
     *                <p>
     *                The command that needs to be parsed here is a string as a whole, and the callback class will automatically get it based on the parsing mode.
     * @return 根据语法执行的运行结果对象。
     * <p>
     * Run result object executed according to syntax.
     */
    Object run(String grammar);

    /**
     * 运行一个命令，在这里会把命令传递给语法树去逐一执行与处理。
     * <p>
     * Run a command, where it will be passed to the syntax tree for execution and processing one by one.
     *
     * @param grammar 需要被解析的命令，在这里是一个字符串数组，回调类不会自动的根据解析模式进行拆分。
     *                <p>
     *                The command that needs to be parsed here is an array of strings, and the callback class will not automatically get based on the parsing mode.
     * @return 根据语法执行的运行结果对象。
     * <p>
     * Run result object executed according to syntax.
     */
    Object run(String... grammar);
}
