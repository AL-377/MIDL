package backGen;
import backGen.msgStructure.MemMsg;
import backGen.msgStructure.ScopedMemMsg;
import backGen.msgStructure.StructMsg;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import precheck.PreCheck;
import symtab.SymNode;
import symtab.SymTab;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 整合词法语法语义+生成hxx代码文件
 */
public class GenHxx {
    private static final String outputFileName = "./src/main/txt/outputHxx.txt";
    private static SymTab stable;
    private static ArrayList<StructMsg>structs;

    /**
     * signed_int :('short'|'int16')
     *            |('long'|'int32')
     *            |('long long'|'int64')
     *            |'int8'
     *            ;
     *
     * unsigned_int  : ('unsigned short'| 'uint16')
     *               | ('unsigned long'| 'uint32')
     *               | ('unsigned long long' | 'uint64')
     *               | 'uint8'
     *               ;
     */
    private static  Map<String, String> equalType;


    /**
     * 调用前端检查,从生成的符号表中提取所有内容
     * @throws IOException
     */
    public static void catchAllMsg() throws IOException {
        //语义分析
        PreCheck.preCheck();
        stable = PreCheck.mc.st;
        equalType = new HashMap<>();
        equalType.put("int16", "short");
        equalType.put("int32", "long");
        equalType.put("int64", "long long");
        equalType.put("uint16", "unsigned short");
        equalType.put("uint32", "unsigned long");
        equalType.put("uint64", "unsigned long long");

        //收集所有struct
        structs = new ArrayList<StructMsg>();
        SymNode sn = new SymNode();
        sn.setType("struct");
        ArrayList<SymNode> allStructs = stable.lookupAll(sn);

        for(SymNode snd:allStructs)
        {
            StructMsg smg=new StructMsg();

            //确定module
            StringBuilder moduleName = new StringBuilder(snd.getModuleName());
            if(moduleName==null)
                smg.setName(snd.getName());
            // 用逗号分割
            else
            {
                ArrayList<String> modules = new ArrayList<String>(
                        Arrays.asList(moduleName.substring(1,moduleName.length()-1).split(","))
                    );
                moduleName = new StringBuilder();
                for(String mn:modules){
                    moduleName.append(mn.trim()).append("_");
                }
                smg.setName(moduleName + snd.getName());
            }

            //找当前的struct节点普通members
            sn = new SymNode();
            sn.setStructName("["+snd.getName()+"]");
            sn.setModuleName(snd.getModuleName());
            ArrayList<MemMsg> members = catchMembers(sn);
            smg.setMembers(members);

            //找当前的struct节点scope members
            ArrayList<ScopedMemMsg> scopedMembers = catchScopedMembers(sn);
            smg.setScopeMembers(scopedMembers);
            smg.setHasScoped(smg.getScopeMembers().size()>0);
//            if(smg.getScopeMembers().size()>0)
//                System.err.println(scopedMembers.get(0).getName()+" "+scopedMembers.get(0).getType());

            structs.add(smg);
       }

    }

    public static ArrayList<MemMsg> catchMembers(SymNode sn)
    {
        ArrayList<SymNode> allMembers = stable.lookupAll(sn);
        ArrayList<MemMsg> members = new ArrayList<MemMsg>();
        for(SymNode snd:allMembers)
        {
            //scope
            if(snd.getType().contains("::"))
                continue;
            MemMsg member=new MemMsg();

            //首字母大写
            String oriType = snd.getType();
            if(oriType.contains("16")||oriType.contains("32")||oriType.contains("64")){
                oriType = equalType.get(oriType);
            }
            StringBuilder concatType= new StringBuilder();
            String []oriTypes = oriType.split("\\s+");
            for (String type : oriTypes) concatType.append(type.substring(0, 1).toUpperCase()).append(type.substring(1));
            member.setType(concatType.toString());
            member.setName(snd.getName());
            member.setVal(snd.getVal());
            member.setHasVal(snd.getVal()!=null);
            if(snd.getType().startsWith("Array")){
                member.setArray(true);
                member.setType(snd.getType().substring
                        (6,snd.getType().length()-1).substring(0,1).toUpperCase()
                        +snd.getType().substring
                        (6,snd.getType().length()-1).substring(1));
                //获得个数和重新格式数值
                StringBuilder vals = new StringBuilder(snd.getVal());
                String []valSplit = vals.toString().split("#");
                String valNum = valSplit[0];
                vals = new StringBuilder();
                for(int i=1;i < valSplit.length;i++)
                {
                    if(i!=1) vals.append(", ");
                    vals.append(valSplit[i]);
                }
                member.setValNum(valNum);
                member.setVal("{"+vals+"}");
            }
            else {
                member.setArray(false);
                member.setValNum("0");

            }
            //注意是包括
            member.setString(snd.getType().contains("string"));

            members.add(member);
        }

        return members;

    }

    public static ArrayList<ScopedMemMsg> catchScopedMembers(SymNode sn)
    {
        ArrayList<SymNode> allScopedMembers = stable.lookupAll(sn);
        ArrayList<ScopedMemMsg> scopedMembers=new ArrayList<ScopedMemMsg>();
        for(SymNode snd:allScopedMembers)
        {
            //scope
            if(!snd.getType().contains("::"))
                continue;

            ScopedMemMsg scpMember=new ScopedMemMsg();

            //确定module
            StringBuilder moduleName = new StringBuilder(snd.getType());

            ArrayList<String> module1 = new ArrayList<String>(
                    Arrays.asList(moduleName.toString().split("::"))
            );
            String structName = module1.get(module1.size()-1);
            ArrayList<String> module2 =  new ArrayList<String>(Arrays.asList(
                    module1.get(0).substring(1,module1.get(0).length()-1).split(","))
            );
            moduleName = new StringBuilder();
            for(String mn:module2){
                moduleName.append(mn.trim()).append("_");
            }
            scpMember.setType(moduleName + structName);
            scpMember.setName(snd.getName());

            SymNode dad = new SymNode();
            dad.setModuleName(module1.get(0));
            dad.setStructName("["+structName+"]");
            ArrayList<MemMsg> members = catchMembers(dad);
            scpMember.setMembers(members);

            scopedMembers.add(scpMember);
        }

        return scopedMembers;
    }



    public static void main(String[] args) throws IOException {
        //得到所有
        catchAllMsg();

        File outputFile = new File(outputFileName);

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
        String result = "";
        STGroup stg = new STGroupFile("T:\\learning\\CS\\complieEx\\exp1\\src\\main\\java\\backGen\\hxxTemplate.stg");
        //header
        ST hxxST = stg.getInstanceOf("headerTemplate");
        hxxST.add("fileName", "hxx");
        result += hxxST.render()+"\n";

        hxxST = stg.getInstanceOf("bodyTemplate");
        hxxST.add("structs",structs);
        result += hxxST.render()+"\n";

        hxxST = stg.getInstanceOf("footerTemplate");
        hxxST.add("fileName", "hxx");
        result += hxxST.render()+"\n";

        bufferedWriter.write(result);
        bufferedWriter.flush();
        bufferedWriter.close();
    }
}
