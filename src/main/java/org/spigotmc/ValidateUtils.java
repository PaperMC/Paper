package org.spigotmc;

public class ValidateUtils
{

    public static String limit(String str, int limit)
    {
        if ( str.length() > limit )
        {
            return str.substring( 0, limit );
        }
        return str;
    }
}
