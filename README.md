# MIDL
Compiler experiment based on MIDL by Aidan Lew.

## Structure
### utils
There are some tools I have created to aid the progress.

#### tree.py
- Func:  This is for AST visualization（AST format -> AST tree）
- Input：the paths generated by AST visitor（see also ASTFormat.java , MIDL2AST.java）
- Output: AST Tree Picture
 ![Sample](https://github.com/AL-377/MIDL/blob/main/demo/tree_sample.png)
- Tip: The AST format（output of ASTFormat） is like ： root（son1（son11（）...）son2（）...）

### src
The source files and files for definition.

#### MIDLLEX.g4
Lexical definition

#### MIDL.g4
Grammatical definition

#### gen
The files for grammatical and lexical analysis.(generated by ANTLR)

#### ast
- ASTFormat.java: The AST formatter
- MIDL2AST.java: MIDL to AST(entrance)

#### precheck
- Calculator.java: Semantic calculation
- ErrorRecorder.java: Record the errors
- MeanCheck.java: Semantic analysis
- PreCheck.java: Integrate the front process of the Compiler. (entrance)

#### symtab
- SymList.java
- SymNode.java
- SymTab.java
- TestSymTab.java
Define the symbol table,the structure is illustrated in the following png.

#### backGen
- GenHxx.java: MIDL->Hxx（main entrance）
- hxxTemplate.stg: define hxx templates files(search **stringTemplate** for more details)
##### msgStructure
- define the data structure to store the message from symtable(used to fill the hxx templates) 



