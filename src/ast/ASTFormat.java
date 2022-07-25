package ast;

import gen.MIDLBaseVisitor;
import gen.MIDLParser;

import java.util.Objects;

/**
 *  AST formatter 进行抽象语法树的格式化输出
 * 参考：AST的设计PPT
 */
public class ASTFormat extends MIDLBaseVisitor<String> {

    //存储抽象语法树
    public String astParseTree = "";
    /**
     * specification -> definition { definition }
     * @param ctx
     * @return
     */
    @Override
    public String visitSpecification(MIDLParser.SpecificationContext ctx) {
        astParseTree += "specification( ";
        for (int i = 0; i < ctx.getChildCount(); i++) {
            visit(ctx.getChild(i));
        }
        astParseTree += " ) ";
        return null;
    }

    /**
     * definiton -> type_decl“;”| module “;”
     * @param ctx
     * @return
     */
    @Override
    public String visitDefinition(MIDLParser.DefinitionContext ctx) {

        visit(ctx.getChild(0));
        return null;
    }

    /***
     * module -> “module”ID “{” definition { definition } “}”
     * @param ctx
     * @return
     */
    @Override
    public String visitModule(MIDLParser.ModuleContext ctx) {
        System.out.println("LINE:" + ctx.getStart().getLine());  //check
        astParseTree += "module( ";
        astParseTree += "ID:" + ctx.getChild(1).getText()+" ";
        //不visit两边的大括号了
        for(int i = 3;i < ctx.getChildCount()-1;i++) {
            visit(ctx.getChild(i));
        }
        astParseTree += " ) ";
        return null;
    }

    /**
     * type_decl -> struct_type | “struct” ID
     *
     * @param ctx
     * @return
     */
    @Override
    public String visitType_decl(MIDLParser.Type_declContext ctx) {
        //struct_type
        if (ctx.getChildCount() == 1) {
            visit(ctx.getChild(0));
        }
        //"struct" ID
        else {
            astParseTree +="struct( ";
            astParseTree +="ID:"+ctx.ID().getText()+" ";
            astParseTree +=" ) ";
        }
        return null;
    }

    /***
     * struct_type->“struct” ID “{”   member_list “}”
     * @param ctx
     * @return
     */
    @Override
    public String visitStruct_type(MIDLParser.Struct_typeContext ctx) {
        astParseTree +="struct( ";
        astParseTree +="ID:"+ctx.getChild(1).getText()+" ";
        visit(ctx.getChild(3));
        astParseTree +=" ) ";
        return null;
    }

    /**
     * member_list{ type_spec declarators “;” }
     *
     * @param ctx
     * @return
     */
    @Override
    public String visitMember_list(MIDLParser.Member_listContext ctx) {
        int n = ctx.getChildCount();
        if (n == 0) {
            return null;
        }
        else {
            for (int i = 0; i < n / 3; i++) {
                astParseTree += "member( ";
                visit(ctx.getChild(3 * i));
                visit(ctx.getChild(3 * i + 1));
                astParseTree +=" ) ";
            }
        }
        return null;
    }

    /**
     * type_spec -> scoped_name | base_type_spec | struct_type
     *
     * @param ctx
     * @return
     */
    @Override
    public String visitType_spec(MIDLParser.Type_specContext ctx) {
        visit(ctx.getChild(0));
        return null;
    }

    /**
     * scoped_name -> [“::”] ID {“::” ID }
     *
     * @param ctx
     * @return
     */
    @Override
    public String visitScoped_name(MIDLParser.Scoped_nameContext ctx) {
        astParseTree += "namespace( ";
        int i;
        if(Objects.equals(ctx.getChild(0).getText(), "::")){
            i = 1;
        }
        else{
            i = 0;
        }
        for (; i < ctx.getChildCount(); i += 2) {
            astParseTree += ctx.getChild(i).getText()+" ";
        }
        astParseTree += " ) ";
        return null;
    }

    /**
     * base_type_spec->floating_pt_type|integer_type|“char”|“string”|“boolean”
     *
     * @param ctx
     * @return
     */
    @Override
    public String visitBase_type_spec(MIDLParser.Base_type_specContext ctx) {
        //终结符
        if (ctx.getChild(0).getChildCount() == 0) {
            astParseTree += ctx.getChild(0).getText()+" ";
        }
        else {
            visit(ctx.getChild(0));
        }
        return null;
    }

    /**
     * floating_pt_type -> “float” | “double” | “long double”
     *
     * @param ctx
     * @return
     */
    @Override
    public String visitFloating_pt_type(MIDLParser.Floating_pt_typeContext ctx) {

        astParseTree += ctx.getChild(0).getText()+" ";
        return null;
    }

    /**
     * integer_type -> signed_int | unsigned_int
     *
     * @param ctx
     * @return
     */
    @Override
    public String visitInteger_type(MIDLParser.Integer_typeContext ctx) {

        visit(ctx.getChild(0));
        return null;
    }

    /**
     * signed_int->(“short”|“int16”)
     * |(“long”|“int32”)
     * |(“long long”|“int64”)
     * |“int8”
     * @param ctx
     * @return
     */
    @Override
    public String visitSigned_int(MIDLParser.Signed_intContext ctx) {

        astParseTree += ctx.getChild(0).getText()+" ";

        return null;
    }

    /**
     * unsigned_int -> (“unsigned short”| “uint16”)
     *    | (“unsigned long”| “uint32”)
     *    | (“unsigned long long” | “uint64”)
     *    | “uint8”
     * @param ctx
     * @return
     */
    @Override
    public String visitUnsigned_int(MIDLParser.Unsigned_intContext ctx) {

        astParseTree += ctx.getChild(0).getText()+" ";
        return null;
    }

    /**
     * declarators -> declarator {“,” declarator }
     *
     * @param ctx
     * @return
     */
    @Override
    public String visitDeclarators(MIDLParser.DeclaratorsContext ctx) {
        int n = ctx.getChildCount();
        for(int i = 0;i < n;i++)
        {
            //除去逗号
            if(ctx.getChild(i).getChildCount()!=0)
                visit(ctx.getChild(i));
        }
        return null;
    }

    /**
     * declarator -> simple_declarator | array_declarator
     *
     * @param ctx
     * @return
     */
    @Override
    public String visitDeclarator(MIDLParser.DeclaratorContext ctx) {
        visit(ctx.getChild(0));
        return null;
    }

    /**
     * simple_declarator -> ID [“=” or_expr]
     *
     * @param ctx
     * @return
     */
    @Override
    public String visitSimple_declarator(MIDLParser.Simple_declaratorContext ctx) {

        if (ctx.getChildCount() != 1) {
            astParseTree += "=( ";
            astParseTree += "ID:" + ctx.getChild(0).getText()+" ";
            visit(ctx.or_expr());
            astParseTree += " ) ";
        }
        else astParseTree += "ID:" + ctx.getChild(0).getText()+" ";
        return null;
    }

    /**
     * array_declarator -> ID “[” or_expr “]” [“=” exp_list ]
     *
     * @param ctx
     * @return
     */
    @Override
    public String visitArray_declarator(MIDLParser.Array_declaratorContext ctx) {
        astParseTree += "array( ";
        astParseTree += "ID:"+ctx.getChild(0).getText() + " ";

        visit(ctx.getChild(2));

        if (ctx.getChildCount() != 4) {
            visit(ctx.exp_list());
        }
        astParseTree += " ) ";
        return null;
    }

    /**
     * exp_list -> “[” or_expr { “,”or_expr } “]”
     *
     * @param ctx
     * @return
     */
    @Override
    public String visitExp_list(MIDLParser.Exp_listContext ctx) {
        astParseTree += "arrayValues( ";
        int n = ctx.getChildCount();
        for (int i = 0; i < n; i ++) {
            if(ctx.getChild(i).getChildCount()>0)
                visit(ctx.getChild(i));
        }
        astParseTree += " ) ";
        return null;
    }

    /**
     * or_expr -> xor_expr {“|” xor_expr }
     *
     * @param ctx
     * @return
     */
    @Override
    public String visitOr_expr(MIDLParser.Or_exprContext ctx) {
        int n = ctx.getChildCount();
        if(n > 1) {
            for (int i = 1; i < n; i += 2) {
                astParseTree += "|( ";
                visit(ctx.getChild(i - 1));
                if (i == n - 2)
                    visit(ctx.getChild(i + 1));
            }
            for (int i = 1; i < n; i += 2) {
                astParseTree += " ) ";
            }
        }
        else visit(ctx.getChild(0));
        return null;
    }

    /**
     * xor_expr -> and_expr {“^” and_expr }
     *
     * @param ctx
     * @return
     */
    @Override
    public String visitXor_expr(MIDLParser.Xor_exprContext ctx) {
        int n = ctx.getChildCount();
        if(n > 1) {
            for (int i = 1; i < n; i += 2) {
                astParseTree += "^( ";
                visit(ctx.getChild(i - 1));
                if (i == n - 2)
                    visit(ctx.getChild(i + 1));
            }
            for (int i = 1; i < n; i += 2) {
                astParseTree += " ) ";
            }
        }
        else visit(ctx.getChild(0));
        return null;
    }

    /**
     * and_expr -> shift_expr {“&”shift_expr }
     *
     * @param ctx
     * @return
     */
    @Override
    public String visitAnd_expr(MIDLParser.And_exprContext ctx) {
        int n = ctx.getChildCount();
        if(n > 1) {
            for (int i = 1; i < n; i += 2) {
                astParseTree += "&( ";
                visit(ctx.getChild(i - 1));
                if (i == n - 2)
                    visit(ctx.getChild(i + 1));
            }
            for (int i = 1; i < n; i += 2) {
                astParseTree += " ) ";
            }
        }
        else visit(ctx.getChild(0));
        return null;
    }

    /**
     * shift_expr -> add_expr { (“>>” | “<<”) add_expr }
     *
     * @param ctx
     * @return
     */
    @Override
    public String visitShift_expr(MIDLParser.Shift_exprContext ctx) {
        int n = ctx.getChildCount();
        if(n > 1) {
            for (int i = 1; i < n; i += 2) {
                //>>或<<
                astParseTree += ctx.getChild(i).getText()+"( ";
                visit(ctx.getChild(i - 1));
                if (i == n - 2)
                    visit(ctx.getChild(i + 1));
            }
            for (int i = 1; i < n; i += 2) {
                astParseTree += " ) ";
            }
        }
        else visit(ctx.getChild(0));
        return null;
    }

    /**
     * add_expr -> mult_expr {(“+” | “-”)  mult_expr }
     * @param ctx
     * @return
     */
    @Override
    public String visitAdd_expr(MIDLParser.Add_exprContext ctx) {
        int n = ctx.getChildCount();
        if(n > 1) {
            for (int i = 1; i < n; i += 2) {
                //>>或<<
                astParseTree += ctx.getChild(i).getText()+"( ";
                visit(ctx.getChild(i - 1));
                if (i == n - 2)
                    visit(ctx.getChild(i + 1));
            }
            for (int i = 1; i < n; i += 2) {
                astParseTree += " ) ";
            }
        }
        else visit(ctx.getChild(0));
        return null;
    }

    /**
     * mult_expr -> unary_expr { (“*” |“/”|“%”) unary_expr }
     * @param ctx
     * @return
     */
    @Override
    public String visitMult_expr(MIDLParser.Mult_exprContext ctx) {
        int n = ctx.getChildCount();
        if(n > 1) {
            for (int i = 1; i < n; i += 2) {
                //>>或<<
                astParseTree += ctx.getChild(i).getText()+"( ";
                visit(ctx.getChild(i - 1));
                if (i == n - 2)
                    visit(ctx.getChild(i + 1));
            }
            for (int i = 1; i < n; i += 2) {
                astParseTree += " ) ";
            }
        }
        else visit(ctx.getChild(0));
        return null;
    }

    /**
     * unary_expr -> [“-”| “+” | “~”] literal
     * @param ctx
     * @return
     */
    @Override
    public String visitUnary_expr(MIDLParser.Unary_exprContext ctx) {

        if (ctx.getChildCount() != 1) {
            astParseTree += ctx.getChild(0).getText()+"( ";
        }
        visit(ctx.literal());
        if (ctx.getChildCount() != 1) {
            astParseTree += " ) ";
        }
        return null;
    }

    /**
     * literal -> INTEGER | FLOATING_PT | CHAR | STRING | BOOLEAN
     * @param ctx
     * @return
     */
    @Override
    public String visitLiteral(MIDLParser.LiteralContext ctx) {

        astParseTree += ctx.getChild(0)+" ";
        return null;
    }
}
