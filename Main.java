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
                                "var a=\"aaa\";\n" +
                                "var b=556;\n" +
                                "b=45623;\n" +
                                "System.output(\"I Like Pineapple\");";
        DComplier dc=new DComplier(new DCode(str));
        dc.compileWithoutPretreatment();
        System.out.println(24|6);
    }

}
