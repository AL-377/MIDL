package precheck;

import gen.MIDLLexer;
import gen.MIDLParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.*;

/**
 * 整合前端
 * 词法-语法-语义
 */
public class PreCheck {
    private static final String inputFileName = "./src/main/txt/inputLines.txt";

    public static MeanCheck mc;
    public static void preCheck() throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new FileReader(inputFileName));
        StringBuilder codes= new StringBuilder();
        String line;
        int cnt = 1;
        while((line = bufferedReader.readLine()) != null) {
            System.out.printf("[%03d]: %s",cnt,line);
            System.out.println();
            codes.append(line).append("\n");
            cnt++;
        }

        //词法检查-语法检查
        CharStream input = CharStreams.fromString(codes.toString());
        MIDLLexer lexer = new MIDLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MIDLParser parser = new MIDLParser(tokens);
        ParseTree tree = parser.specification();
        //遍历分析树-语义检查
        System.out.println("Sym-Check Process...");
        mc = new MeanCheck();
        mc.visit(tree);
        System.out.println("Sym-Check Finished.");
        //打印错误信息
        System.err.println("Errors TraceBack: ");
        System.err.println(mc.er.getErrors());

        //打印符号表
        System.out.println("Print SymTable: ");
        System.out.println(mc.st.toString());

        bufferedReader.close();
    }

    public static void main(String[] args) throws IOException {
        preCheck();
    }
}
