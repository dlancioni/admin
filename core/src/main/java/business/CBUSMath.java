package business;

import entity.CENTData;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author lancioda
 */
class CBUSMath extends CBUS
{
    public CBUSMath(Connection Connection1, CENTData CENTSession1) throws SQLException
    {
        super(Connection1, CENTSession1);
    }
       
    public static double GetPV(double dblFV, double dblRate, int intDays)
    {
        double dblResult = 0;
        double intMonth = 0;
        
        intMonth = (intDays / 30);
        
        dblResult =  dblFV / Math.pow(1 + (dblRate / 100), intMonth);
        
        return dblResult;

    }
    
    public double Round(double dblValue, int dblDecimals, int intCeilOrFloor) 
    {  
        double arredondado = dblValue;  
        
        arredondado *= (Math.pow(10, dblDecimals));  
       
        if (intCeilOrFloor == 0) 
        {  
            arredondado = Math.ceil(arredondado);             
        } 
        else 
        {  
            arredondado = Math.floor(arredondado);  
        }  
        
        arredondado /= (Math.pow(10, dblDecimals)); 
        
        return arredondado;  
    }      

}
