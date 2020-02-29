package com.dexer.dscript;


public class Main {

    public static void main(String[] args) {
	// write your code here
//        DCode code=new DCode("I like;my{lampese}dddadf;dsgs;ever{forever}dddd;faf;for(){sss}");
//        DNode node=code.anaylze();
//        System.out.println("内容        条件");
//        for(DNode n : node.getSubNodes()){
//            System.out.println(n.getContent()+"     "+n.getCondition());
//        }
        String str=
                                        "var a=233;\n" +
                                        "var b=233;\n" +
                                        "var c=a==b;\n" +
                                        "System.output(c);";
        DComplier dc=new DComplier(new DCode(str));
        dc.compileWithoutPretreatment();
        //System.out.println(DExpression.isEquation("a==f"));
    }

}
