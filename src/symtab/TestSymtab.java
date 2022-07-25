package symtab;

import gen.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.*;
import java.io.*;
import java.util.ArrayList;

import ast.ASTFormat;
import precheck.ErrorRecorder;
import precheck.MeanCheck;

/**
 * MIDL转换抽象语法树的启动类
 * Input:按行读取输入文件
 * Output:输出格式化抽象语法树到对应文件
 */
public class TestSymtab{

    private static final String inputFileName = "./src/main/txt/inputLines.txt";

    public static void test() throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new FileReader(inputFileName));
        StringBuilder codes= new StringBuilder();
        String line;
        int cnt = 1;
        while((line = bufferedReader.readLine()) != null) {
            System.out.println("["+cnt + "]: " + line);
            codes.append(line);
            cnt++;
        }

        //词法检查-语法检查
        CharStream input = CharStreams.fromString(codes.toString());
        MIDLLexer lexer = new MIDLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MIDLParser parser = new MIDLParser(tokens);
        ParseTree tree = parser.specification();
        //遍历分析树-语义检查
        MeanCheck mc = new MeanCheck();
        mc.visit(tree);

        bufferedReader.close();

    }
    public static void main(String[] args) throws Exception {

        test();

    }
}
