package com.dexer.dscript;
import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.eval.Variable;
import parsii.tokenizer.ParseException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DMathExpression {
    public static DFunction.ParamIns solveMathExpression(String exp,int area_id,int layout_id){
        DFunction.ParamIns pii=null;
        {
            Pattern p = Pattern.compile("\\w+\\.\\w+\\(.*\\)");
            Matcher matcher = p.matcher(exp);
            while (matcher.find()) {
                String target = matcher.group();
                exp = exp.replace(target, DClass.runFunction(target, area_id, layout_id).value);
            }
        }
        {
            Pattern p = Pattern.compile("[a-zA-Z_]+\\.[a-zA-Z_]+");
            Matcher matcher = p.matcher(exp);
            while (matcher.find()) {
                String target = matcher.group();
                exp = exp.replace(target, DClass.getAttribute(target,area_id,layout_id).value);
            }
        }
        {
            Pattern p = Pattern.compile("[a-zA-Z_]+");
            Matcher matcher = p.matcher(exp);
            while (matcher.find()) {
                String target = matcher.group();
                exp = exp.replace(target, DReference.getVariableByName(target,area_id,layout_id).value);
            }
        }
        try {
            Scope scope=new Scope();
            Expression exp_= Parser.parse(exp);
            double res=exp_.evaluate();
            pii=new DFunction.ParamIns("Number",res+"");

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return pii;

    }
}
