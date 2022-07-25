package precheck;

import java.util.ArrayList;

/**
 * 收集记录错误信息
 */
public class ErrorRecorder {
    public ArrayList<String> errors;
    public final static String[] ErrorType ={"redefined","undefined","typeError","overflow","outIndex"};

    public ErrorRecorder()
    {
        this.errors=new ArrayList<String>();
    }
    public void addError(String line,String type,String name,int error,String expected)
    {
        if(expected==null)
            errors.add("line "+line+" exception:" + type + " " + name + " "+ErrorType[error]);
        else
            errors.add("line "+line+" exception:" + type + " " + name + " "+ErrorType[error]+" , Expected: "+expected);
    }

    public String getErrors()
    {
        String allError="";
        for(String error:errors)
        {
            allError += error+"\n";
        }
        return allError;
    }

}
