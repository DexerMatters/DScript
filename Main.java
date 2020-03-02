package com.dexer.dscript;


import java.io.File;

import static com.dexer.dscript.DClass.*;
import static com.dexer.dscript.DFunction.*;

public class Main {

    public static void main(String[] args) {
	// write your code here
//        DCode code=new DCode("I like;my{lampese}dddadf;dsgs;ever{forever}dddd;faf;for(){sss}");
//        DNode node=code.anaylze();
//        System.out.println("内容        条件");
//        for(DNode n : node.getSubNodes()){
//            System.out.println(n.getContent()+"     "+n.getCondition());
//        }

        DComplier dc=new DComplier(new File("E:\\java project\\DScript\\dscripts\\demo.ds")){
            @Override
            public void preLoad() {
                createClass("System");
                getClassByName("System").addFunction(new DFunction("output",
                        new Param[]{new Param("Object","str")}, STATIC|NATIVE,
                        new NativeCode(){
                            @Override
                            public ParamIns run(ParamIns[] pi) {
                                System.out.println(pi[0].value);
                                return new ParamIns("Void",null);
                            }
                        }));
                getClassByName("System").addFunction(new DFunction("add",
                        new Param[]{new Param("Integer","int"),new Param("Integer","int2")}, STATIC|NATIVE,
                        new NativeCode(){
                            @Override
                            public ParamIns run(ParamIns[] pi) {
                                int r=Integer.parseInt(pi[0].value)+Integer.parseInt(pi[1].value);
                                return new ParamIns("Integer",Integer.toString(r));
                            }
                        }));
            }
        };
        dc.compileWithoutPretreatment(0,0);
        //System.out.println(DExpression.isEquation("a==f"));
    }

}
