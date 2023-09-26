package zhao.gravel.grammar.command;

import zhao.utils.transformation.Transformation;

import java.util.ArrayList;

/**
 * 提供给执行器的函数抽象类，其接收一个list对象，并输出一个任意对象；输入的list对象代表的就是在解析函数的过程中产生的所有变量数据的集合，我们可以在这里进行统一的处理。
 * <p>
 * A function abstract class provided to the executor, which receives a list object and outputs an arbitrary object; The input list object represents the collection of all variable data generated during the parsing process of the function, and we can perform unified processing here.
 *
 * @author zhao
 */
public interface ActuatorTF extends Transformation<ArrayList<Object>, Object> {
}
