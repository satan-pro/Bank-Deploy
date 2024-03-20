package class_lib;

import java.util.*;
import java.lang.*;
import class_lib.*;


public class Admin extends user{ 
    static int access = 1;
    public Admin() throws passworderror, usererror{
        super("admin","admin");
    }
    public String get_pass(){
        return super.get_pass();
    }
}