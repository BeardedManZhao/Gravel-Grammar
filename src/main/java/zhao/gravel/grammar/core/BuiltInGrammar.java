package zhao.gravel.grammar.core;

import zhao.gravel.grammar.command.ActuatorParam;
import zhao.gravel.grammar.command.ActuatorTF;
import zhao.gravel.grammar.command.SaveParam;
import zhao.gravel.grammar.command.Syntax;

import java.util.ArrayList;

/**
 * 内置语法对象。
 */
public enum BuiltInGrammar {


    /**
     * SQL SELECT 系列的语法树对象
     */
    SQL_SELECT {
        /**
         * @param arrayList  用于存储命令解析过程中需要保存的数据的容器。
         * @param transformation 该语法器执行器层的函数执行逻辑，其输入数据为每层的参数字符串，输出数据可以为null也可以为
         * @return 指定模式下对应的语法对象组件，该组件可以直接被添加到回调器中。
         * <p>
         * The corresponding syntax object component in the specified mode can be directly added to the callback.
         */
        @Override
        public Syntax get(ArrayList<Object> arrayList, ActuatorTF... transformation) {
            if (transformation.length < 2) {
                throw new UnsupportedOperationException("The number of functions you provided is incorrect. The functions you need to provide include [Table Processing Function] and [Where Clause Processing Function]");
            }


            /* group by */
            final Syntax group_by = SaveParam.create(
                    "group by", arrayList,
                    new ActuatorParam(Syntax.WILDCARD) {
                        /**
                         * @return 当前执行器参数的执行逻辑函数，执行完毕之后会返回一个任意数据类型。
                         * <p>
                         * The execution logic function of the current executor parameter will return an arbitrary data type after execution.
                         */
                        @Override
                        public Object run() {
                            return transformation[2].function(arrayList);
                        }
                    }
            );


            /* where 子句 */
            final ActuatorParam whereC = new ActuatorParam(Syntax.WILDCARD) {

                /**
                 * @return 当前执行器参数的执行逻辑函数，执行完毕之后会返回一个任意数据类型。
                 * <p>
                 * The execution logic function of the current executor parameter will return an arbitrary data type after execution.
                 */
                @Override
                public Object run() {
                    return transformation[1].function(arrayList);
                }
            };
            whereC.addSubSyntax(group_by.clone());



            /* table */
            final ActuatorParam table = new ActuatorParam(Syntax.WILDCARD) {
                @Override
                public Object run() {
                    return transformation[0].function(arrayList);
                }
            };
            table.addSubSyntax(
                    SaveParam.create(
                            "where", arrayList,
                            whereC
                    )
            );
            table.addSubSyntax(group_by);


            // 首先将 SQL 语法树准备出来 然后直接返回
            return SaveParam.create(
                    "select",
                    arrayList,
                    SaveParam.create(
                            Syntax.WILDCARD,
                            arrayList,
                            SaveParam.create(
                                    "from",
                                    arrayList,
                                    table
                            )
                    )
            );
        }
    };


    /**
     * @param arrayList      用于存储命令解析过程中需要保存的数据的容器。
     *                       <p>
     *                       A container used to store data that needs to be saved during command parsing.
     * @param transformation 该语法器执行器层的函数执行逻辑，其输入数据为每层的参数字符串，输出数据可以为null也可以为
     * @return 指定模式下对应的语法对象组件，该组件可以直接被添加到回调器中。
     * <p>
     * The corresponding syntax object component in the specified mode can be directly added to the callback.
     */
    public abstract Syntax get(ArrayList<Object> arrayList, ActuatorTF... transformation);

    /**
     * @param transformation 该语法器执行器层的函数执行逻辑，其输入数据为每层的参数字符串，输出数据可以为null也可以为
     * @return 指定模式下对应的语法对象组件，该组件可以直接被添加到回调器中。
     * <p>
     * The corresponding syntax object component in the specified mode can be directly added to the callback.
     */
    public Syntax get(ActuatorTF... transformation) {
        return this.get(new ArrayList<>(), transformation);
    }

    public Syntax get(ArrayList<Object> arrayList) {
        return this.get(arrayList, new ActuatorTF[0]);
    }

}
