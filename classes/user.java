package class_lib;

import java.util.*;
import java.lang.*;
import class_lib.*;


public class user {
    public String name;
    private String pass;  
    public user(String n, String p) throws passworderror, usererror{
        int f = 0;
        char c;
        for(int i = 0; i<n.length(); i++){
            c = n.charAt(i);
            if(n.length() < 5 || !(c == '_' || Character.isLetter(c) || Character.isDigit(c))){
                f = 0;
                break;
            }
            else{
                f =1;
            }
        }
        if(f == 1){
            name = n;
        }
        else{
            throw new usererror();
        }

        if(p.length()>=8){
            pass = p;
        }
        else{
            throw new passworderror();
        }       
    }
    public String get_pass(){
        return pass;
    }
}
