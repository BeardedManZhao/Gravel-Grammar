package zhao.gravel.grammar;

import zhao.gravel.grammar.command.ActuatorTF;
import zhao.gravel.grammar.core.BuiltInGrammar;
import zhao.gravel.grammar.core.BuiltInReg;
import zhao.gravel.grammar.core.CommandCallback;
import zhao.gravel.grammar.core.model.AnalyticalModel;

import java.util.AbstractMap;

/**
 * @author zhao
 */
public class MAIN {
    public static void main(String[] args) {
        final CommandCallback callback = CommandCallback.createGet(
                BuiltInReg.SQL_EXTRACTION_REGULAR_MODEL_1, BuiltInGrammar.SQL_SELECT.get(
                        (ActuatorTF) AbstractMap::toString,
                        (ActuatorTF) AbstractMap::toString,
                        (ActuatorTF) AbstractMap::toString,
                        (ActuatorTF) AbstractMap::toString
                )
        );
        callback.setAnalyticalModel(AnalyticalModel.REGULAR_MODEL_1);
        System.out.println(callback.run("select * from zhao where age > 20;"));
    }
}
