package zhao.gravel.grammar.core;

import zhao.gravel.grammar.command.*;

import java.util.HashMap;

/**
 * 内置语法对象。
 */
public enum BuiltInGrammar {


    /**
     * SQL SELECT 系列的语法树对象
     */
    SQL_SELECT {
        /**
         * @param hashMap  用于存储命令解析过程中需要保存的数据的容器。
         * @param transformation 该语法器执行器层的函数执行逻辑，其输入数据为每层的参数字符串，输出数据可以为null也可以为
         * @return 指定模式下对应的语法对象组件，该组件可以直接被添加到回调器中。
         * <p>
         * The corresponding syntax object component in the specified mode can be directly added to the callback.
         */
        @Override
        public Syntax get(HashMap<String, Object> hashMap, ActuatorTF... transformation) {

            /* limit */
            final Syntax limit = SaveParam.create("limit", hashMap,
                    new ActuatorAliasParam(Syntax.WILDCARD, "这里输入的应为一个或两个数字，用于标识需要查询的部分数据范围。", "offset count") {
                        @Override
                        public Object run() {
                            BuiltInGrammar.check(transformation, 5, "缺少[" + this.getAliasName() + "]参数对应的执行逻辑，此逻辑应位于匿名表达式数组中索引为4的位置。");
                            return transformation[4].function(hashMap);
                        }
                    });

            /* order by */
            final ActuatorAliasParam orderByC = new ActuatorAliasParam(Syntax.WILDCARD, "这里输入的应为一个字符串，用于标识需要做为排序字段的字段名。", "Order by clause") {
                @Override
                public Object run() {
                    BuiltInGrammar.check(transformation, 4, "缺少[" + this.getAliasName() + "]参数对应的执行逻辑，此逻辑应位于匿名表达式数组中索引为3的位置。");
                    return transformation[3].function(hashMap);
                }
            };
            final Syntax order_by = SaveParam.create(
                    "order by", hashMap,
                    orderByC
            );
            orderByC.addSubSyntax(limit);

            /* group by */
            final ActuatorAliasParam groupByC = new ActuatorAliasParam(Syntax.WILDCARD, "这里输入的应为一个字符串，用于标识需要做为分组字段的字段名。", "group by fieldName") {
                /**
                 * @return 当前执行器参数的执行逻辑函数，执行完毕之后会返回一个任意数据类型。
                 * <p>
                 * The execution logic function of the current executor parameter will return an arbitrary data type after execution.
                 */
                @Override
                public Object run() {
                    BuiltInGrammar.check(transformation, 3, "缺少[" + this.getAliasName() + "]参数对应的执行逻辑，此逻辑应位于匿名表达式数组中索引为2的位置。");
                    return transformation[2].function(hashMap);
                }
            };
            final Syntax group_by = SaveParam.create("group by", hashMap, groupByC);
            groupByC.addSubSyntax(order_by);
            groupByC.addSubSyntax(limit);

            /* where 子句 */
            final ActuatorParam whereC = new ActuatorAliasParam(Syntax.WILDCARD, "这里输入的应为一个等式/不等式，如果此等式成立则代表满足条件。", "Where clause condition") {

                /**
                 * @return 当前执行器参数的执行逻辑函数，执行完毕之后会返回一个任意数据类型。
                 * <p>
                 * The execution logic function of the current executor parameter will return an arbitrary data type after execution.
                 */
                @Override
                public Object run() {
                    BuiltInGrammar.check(transformation, 2, "缺少[" + this.getAliasName() + "]参数对应的执行逻辑，此逻辑应位于匿名表达式数组中索引为1的位置。");
                    return transformation[1].function(hashMap);
                }
            };
            whereC.addSubSyntax(group_by);
            whereC.addSubSyntax(order_by);
            whereC.addSubSyntax(limit);

            /* table */
            final ActuatorParam table = new ActuatorAliasParam(Syntax.WILDCARD, "这里输入的应为一个字符串，代表的是表的名字。", "table Name") {

                @Override
                public Object run() {
                    BuiltInGrammar.check(transformation, 1, "缺少[" + this.getAliasName() + "]参数对应的执行逻辑，此逻辑应位于匿名表达式数组中索引为0的位置。");
                    return transformation[0].function(hashMap);
                }
            };
            table.addSubSyntax(
                    SaveParam.create(
                            "where", hashMap,
                            whereC
                    )
            );
            table.addSubSyntax(group_by);
            table.addSubSyntax(order_by);
            table.addSubSyntax(limit);

            // 首先将 SQL 语法树准备出来 然后直接返回
            return SaveParam.create(
                    "select", "SQL 语法中的查询数据操作需要使用到的关键字。",
                    hashMap,
                    SaveParam.create(
                            Syntax.WILDCARD, "此处应为被查询的字段",
                            hashMap,
                            SaveParam.create(
                                    "from", "SQL 语法中常用于指定被操作/查询表的名字。",
                                    hashMap,
                                    table
                            )
                    )
            );
        }
    };

    /**
     * 检查传递的匿名函数的数量是否正确
     *
     * @param actuatorTFS 需要被检查的匿名函数组
     * @param okLen       正确或预期的数值
     * @param errorMsg    当检查不满足的时候会返回带有此信息的异常数据。
     */
    public static void check(ActuatorTF[] actuatorTFS, int okLen, String errorMsg) {
        if (actuatorTFS.length < okLen) {
            throw new UnsupportedOperationException(errorMsg);
        }
    }

    /**
     * @param hashMap        用于存储命令解析过程中需要保存的数据的容器。
     *                       <p>
     *                       A container used to store data that needs to be saved during command parsing.
     * @param transformation 该语法器执行器层的函数执行逻辑，其输入数据为每层的参数字符串，输出数据可以为null也可以为
     * @return 指定模式下对应的语法对象组件，该组件可以直接被添加到回调器中。
     * <p>
     * The corresponding syntax object component in the specified mode can be directly added to the callback.
     */
    public abstract Syntax get(HashMap<String, Object> hashMap, ActuatorTF... transformation);

    /**
     * @param transformation 该语法器执行器层的函数执行逻辑，其输入数据为每层的参数字符串，输出数据可以为null也可以为
     * @return 指定模式下对应的语法对象组件，该组件可以直接被添加到回调器中。
     * <p>
     * The corresponding syntax object component in the specified mode can be directly added to the callback.
     */
    public Syntax get(ActuatorTF... transformation) {
        return this.get(new HashMap<>(), transformation);
    }

    public Syntax get(HashMap<String, Object> hashMap) {
        return this.get(hashMap, new ActuatorTF[0]);
    }
}
