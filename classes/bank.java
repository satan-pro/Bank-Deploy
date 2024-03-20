package class_lib;


import java.util.*;
import class_lib.stock;




public class bank{
    public static double money = 0;
    public static double liability = 0;
    public static stock[] stocks = new stock[10];
    public static int interest = 10;
    static{
        for(int i = 0; i<10 ;i++){
            stocks[i] = new stock();
        }
    }
}