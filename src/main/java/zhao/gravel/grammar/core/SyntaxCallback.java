package zhao.gravel.grammar.core;

/**
 * 语法回调器接口，其中包含针对一些命令语法对象的存储与回调处理的函数。
 */
public interface SyntaxCallback {

    void setAnalyticalModel();

    Object run(String grammar);

    Object run(String... grammar);
}
