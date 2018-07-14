package entity;

public class CENTException extends Exception
{
    private static final long serialVersionUID = 1;

    private String code;
    private String message;
    private String field_name;
    private String source;
    

    public CENTException(String strCode)
    {
        this.SetCode(strCode);
        this.SetMessage("");       
    }

    public CENTException(String strCode, String strMessage)
    {
        this.SetCode(strCode);
        this.SetMessage(strMessage);
        this.SetFieldName("");
    }
    
    public CENTException(String strCode, String strMessage, String strFieldName)
    {
        this.SetCode(strCode);
        this.SetMessage(strMessage);
        this.SetFieldName(strFieldName);
    }    

    public String GetCode()
    {
        return code;
    }

    public void SetCode(String code)
    {
        this.code = code;
    }

    public String GetMessage()
    {
        return message;
    }

    public void SetMessage(String message)
    {
        this.message = message;
    }

    public String GetFieldName()
    {
        return field_name;
    }

    public void SetFieldName(String strFieldName)
    {
        this.field_name = strFieldName;
    }

}
