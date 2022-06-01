package com.github.cafeduke.util;

import com.github.cafeduke.fincal.Valuation;

public class Util
{
    public static void hr()
    {
        System.out.println("----------------------------------------------------------------------------------------------------");
    }

    public static void heading(String mesg)
    {
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.println(mesg);
        System.out.println("----------------------------------------------------------------------------------------------------");
    }
    
    /**
     * {@code arg[index]} is expected to have the option switch for which value
     * needs to be retrieved. If {@code (index+1)} is NOT within the argument boundary
     * an exception is thrown, else the switch value is returned.
     * 
     * @param arg Array of arguments.
     * @param index Index of the switch
     * @return Value of the switch.
     */
    public static String getSwitchValue(String arg[], int index)
    {
        if (index + 1 >= arg.length)
            throw new IllegalArgumentException("Option " + arg[index] + " needs a value");
        return arg[index + 1];
    }

    public static String[] validateArgCSV(String arg[], int index)
    {
        return getSwitchValue(arg, index).split(",");
    }

    public static Double validateArgDouble(String arg[], int index)
    {
        return Double.parseDouble(getSwitchValue(arg, index));
    }    
}
