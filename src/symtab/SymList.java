package symtab;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * 符号表中的元素链
 */
public class SymList {
    public SymNode head;
    public SymList()
    {
        head = new SymNode();
    }

    /**
     * 头插
     * @param sn
     */
    public void insertNode(SymNode sn)
    {
        sn.setNext(head.getNext());
        head.setNext(sn);
    }

    public SymNode getHead()
    {
        return head;
    }

    /**
     * tofind中非null的值进行匹配
     *
     * @param toFind
     * @return
     */
    public SymNode findNode(SymNode toFind)
    {
        SymNode tp = head;
        while((tp=tp.getNext())!=null){
            //tp = tp.getNext();
            if(toFind.getName()!=null && !(toFind.getName().equals(tp.getName()))){
                continue;
            }
            if(toFind.getModuleName()!=null && !(toFind.getModuleName().equals(tp.getModuleName()))){
                continue;
            }
            if(toFind.getStructName()!=null && !(toFind.getStructName().equals(tp.getStructName()))){
                continue;
            }
            if(toFind.getType()!=null && !(toFind.getType().equals(tp.getType()))){
                continue;
            }
            if(toFind.getVal()!=null && !(toFind.getVal().equals(tp.getVal()))){
                continue;
            }
            break;
        }
        return tp;
    }

    /**
     * tofind中非null的值进行匹配所有的
     *
     * @param toFind
     * @return
     */
    public ArrayList<SymNode> findNodes(SymNode toFind)
    {
        ArrayList<SymNode> ans = new ArrayList<SymNode>();

        SymNode tp = head;
        while((tp=tp.getNext())!=null){
            //tp = tp.getNext();
            if(toFind.getName()!=null && !(toFind.getName().equals(tp.getName()))){
                continue;
            }
            if(toFind.getModuleName()!=null && !(toFind.getModuleName().equals(tp.getModuleName()))){
                continue;
            }
            if(toFind.getStructName()!=null && !(toFind.getStructName().equals(tp.getStructName()))){
                continue;
            }
            if(toFind.getType()!=null && !(toFind.getType().equals(tp.getType()))){
                continue;
            }
            if(toFind.getVal()!=null && !(toFind.getVal().equals(tp.getVal()))){
                continue;
            }
            ans.add(tp);
        }
        return ans;
    }


    @Override
    public String toString()
    {
        SymNode tp = head.getNext();
        String nodes="";
        while(tp!=null){
            nodes += tp.toString()+"\n";
            tp = tp.getNext();
        }
        return nodes;
    }

}
