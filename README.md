# MIDL
Compile experiment based on MIDL.

## Structure
### utils
There are some tools I have created to aid the progress.

#### tree.py
- Func:  This is for AST visualization（AST format -> AST tree）;
- Input：the paths generated by AST visitor（see also ASTFormat.java , MIDL2AST.java）
- Output: AST Tree Pic
- Tip:
 -The AST format（output of ASTFormat） is like ： root（son1（son11（）...）son2（）...）
