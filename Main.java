package com.dexer.dscript;


import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import static com.dexer.dscript.DClass.*;
import static com.dexer.dscript.DFunction.*;

public class Main {
    public static void main(String[] args) {
        //DClassExpression.solveClass("class{pub static asfa(a d,a f){dh df}}");
        new DComplier(new File("D:\\java project\\DScript\\out\\artifacts\\dscript\\script.ds"))
                .compile(0,0);
        Scanner scan_mod = new Scanner(System.in);
        String[] params = scan_mod.nextLine().split(" ");
        if (params[0].equals("dsc")) {
            switch (params[1]){
                case "-v":
                    System.out.println(DComplier.INFO);
                    break;
                case "-run":
                    System.out.println("Running...");
                    DComplier dc_ = new DComplier(new File(params[2]));
                    dc_.compile(0, 0);
                    System.out.println("Complete");
                    break;
                case "-help":
                    System.out.println("//////////////////\n" +
                            "dsc -v\t\t\t\t\tTo check the version\n" +
                            "dsc -run <file_path>\tTo run a script\n" +
                            "dsc -help\t\t\t\tTo show help\n"+
                            "//////////////////");
                    break;
                default:
                    System.out.println("Unknown command.Please check commands with \"dsc -help\"");
            }
        }else
            System.out.println("Unknown command.Please check commands with \"dsc -help\"");
         main(args);
    }
}
