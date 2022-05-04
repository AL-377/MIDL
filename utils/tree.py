import re
import networkx as nx
import matplotlib.pyplot as plt

# 路径
paths = ""
# 树的深度
depth = 0
# 统计左括号与对应的右括号位置关系，即确认某一个父亲的域
headEnd = {}
# 记录走过的节点
yet = []
tempDad = None

"""
树的节点
"""
class TreeNode:
    def __init__(self, val):
        self.val = val
        self.childs = []
        self.dad = None


"""
        由格式化输出,生成可视化树（对于格式化输出，将）两边加空格）
        (specification (definition (type_decl (struct_type struct a { member_list } ) ) ; ) )
"""
def buildTree(step,node,ends):

    if step == ends:
        return
    if yet[step]:
        return
    else:
        yet[step] = 1

    if paths[step] == ')':
        return

    if paths[step][-1] == '(':
        newNode = TreeNode(paths[step])

        if node.val[-1] == '(' or node.val == 'root':
            newNode.dad = node
        else:
            newNode.dad = node.dad

        if newNode.dad is not None:
            newNode.dad.childs.append(newNode)

        nextEnd = headEnd[step] + 1
        for st in range(step+1,nextEnd):
            buildTree(st,newNode,nextEnd)

    else:
        newNode = TreeNode(paths[step])
        if node.val[-1] == '(':
            newNode.dad = node
        else:
            newNode.dad = node.dad

        if newNode.dad is not None:
            newNode.dad.childs.append(newNode)

        for st in range(step+1,ends):
            buildTree(st,newNode,ends)

"""
    得到字典headEnd
"""
def preProcess():
    stacks=[]
    for (i,p) in enumerate(paths):
        # print(p)
        if len(p)==0:
            continue
        if p[-1] == '(':
            stacks.append(i)
        if p[0] == ')':
            headEnd[stacks[-1]] = i
            stacks = stacks[:-1]
    for k,v in headEnd.items():
        print(f'{k} -> {v}')


"""
    先序遍历树
"""

def dfs(node):
    print(node.val)
    if len(node.childs) == 0:
        return
    for cds in node.childs:
        dfs(cds)

""" 
    层序遍历+建图
"""
def drawTree(root):
    dg = nx.DiGraph()
    # 解决同名问题
    counts = 0
    nodes = [[root, 0,root.val+"#"+str(counts)]]
    # 节点+深度
    dg.add_node(nodes[0][2], subset=0)
    while len(nodes) > 0:
        print(nodes[0][0].val+":")
        for node in nodes[0][0].childs:
            nodes.append([node, nodes[0][1] + 1,node.val + "#" + str(counts)])
            filtVal1 = re.sub(r"\(","",node.val + "#" + str(counts))
            filtVal2 = re.sub(r"\(", "", nodes[0][2])
            dg.add_node(filtVal1, subset=nodes[0][1] + 1)
            dg.add_edge(filtVal2, filtVal1)
            counts += 1
        nodes = nodes[1:]

    pos = nx.multipartite_layout(dg)
    nx.draw_networkx(dg, node_size=1000, pos=pos)
    plt.show()


if __name__ == '__main__':
    paths = input("Input the results of visitor: ")
    paths = re.sub(r"\s+", " ", paths)
    paths = paths.strip().split(' ')
    preProcess()
    root = TreeNode('root')
    yet = [0] * len(paths)
    buildTree(0, root,headEnd[0])
    drawTree(root)
