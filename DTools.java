package com.dexer.dscript;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import static com.dexer.dscript.DReference.*;
public class DTools {

    static int indexOf(String s,char c){
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)==c
                    &&!DReference.hasCovered(s,i,DReference.BRACLET_STRING)
                    &&!DReference.hasCovered(s,i,DReference.BRACLET_CURLY)
                    &&!DReference.hasCovered(s,i,DReference.BRACKET_NORMAL)
                    &&!DReference.hasCovered(s,i,DReference.BRACKET_SQUARE)){
                return i;
            }
        }
        return -1;
    }
    static String[] split(String s,String c){
        ArrayList<String> a=new ArrayList<>();
        String[] ss=s.split("");
        String p="";
        for(int i=0;i<s.length();i++){
            if(ss[i].equals(c)
                    &&!DReference.hasCovered(s,i,DReference.BRACLET_STRING)
                    &&!DReference.hasCovered(s,i,DReference.BRACLET_CURLY)
                    &&!DReference.hasCovered(s,i,DReference.BRACKET_NORMAL)
                    &&!DReference.hasCovered(s,i,DReference.BRACKET_SQUARE)){
                a.add(p);
                p="";
            }else p+=ss[i];
            if(i==s.length()-1){
                a.add(p);
            }
        }
        String[] res=a.toArray(new String[0]);
        if(res.length==0) return new String[]{s};
        return res;
    }
    static String getContentInBracket(String str,char[] c){
        String[] strs=str.split("");
        String res="";
        int mode=0;
        for(int i=0;i<strs.length;i++){

            if(strs[i].charAt(0)==c[0]&&getIndexCovered(str,i,c)==0&&mode==0)
                mode=1;
            if(strs[i].charAt(0)==c[1]&&getIndexCovered(str,i,c)==0&&mode==1)
                break;
            if(mode==1)
                res+=strs[i];
        }
        return res.substring(1);

    }
    static String[] getContentInBracket_(String str,char[] c){
        String[] strs=str.split("");
        ArrayList<String> ress=new ArrayList<>();
        String res="";
        int mode=0;
        for(int i=0;i<strs.length;i++){

            if(strs[i].charAt(0)==c[0]&&getIndexCovered(str,i,c)==0&&mode==0)
                mode=1;
            if(strs[i].charAt(0)==c[1]&&getIndexCovered(str,i,c)==0&&mode==1) {
                ress.add(res.substring(1));
                mode=0;
                res="";
            }
            if(mode==1)
                res+=strs[i];
        }
        return ress.toArray(new String[0]);

    }
}
