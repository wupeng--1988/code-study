package redblacktree;

import com.sun.org.apache.regexp.internal.RE;

/**
 * 二叉红黑树， 是一个特殊的二叉树
 * 1、每个节点必须是红色或者黑叔
 * 2、根节点是黑色
 * 3、每个叶子节点(NIL)是黑色
 * 4、如果一个节点是红色，那么他的子节点必是黑色
 * 5、从一个节点到该节点的子孙节点的所有路径上包含相同数目的黑节点
 */

public class TreeNode {

	public static final int BLACK = 0, RED = 1;

	//当前的值
	public int value;
	public int color;
	public TreeNode parent;
	public TreeNode left, right;

	//默认创建一个黑色的
	private TreeNode(int value, TreeNode parent, TreeNode left, TreeNode right, int color) {
		this.value = value;
		this.parent = parent;
		this.left = left;
		this.right = right;
		this.color = color;
		if(parent != null){
			if(parent.value > value){
				parent.left = this;
			}else{
				parent.right = this;
			}
		}
	}

	public static TreeNode createHead(int value){
		return new TreeNode(value, null, null, null, BLACK);
	}

	public static TreeNode createNewNode(int value, TreeNode parent){
		return new TreeNode(value, parent, null, null, RED);
	}

	public static TreeNode createNewNode(int value, TreeNode parent, TreeNode left, TreeNode right){
		return new TreeNode(value, parent, left, right, RED);
	}

	public TreeNode changeLeft(TreeNode newNode){
		TreeNode result = left;
		this.left = newNode;
		return result;
	}

	public TreeNode changeRight(TreeNode newNode){
		TreeNode result = right;
		this.right = newNode;
		return result;
	}

	public TreeNode changeParent(TreeNode newNode){
		TreeNode result = parent;
		this.parent = newNode;

		if(newNode != null){
			if(newNode.value > value){
				newNode.left = this;
			}else{
				newNode.right = this;
			}
		}
		return result;
	}

	public void toBlack(){
		this.color = BLACK;
	}

	public void toRed(){
		this.color = RED;
	}

	public boolean isBlack(){
		return color == BLACK;
	}

	public boolean isRed(){
		return color == RED;
	}

	public String getPrintString(){
		if(color == RED){
			return value + "#red";
		}else{
			return value + "#black";
		}
	}

	/**
	 * 可以通过plantUml 查看
	 */
	public void printTree(){
		if(left == null && right == null){
			return;
		}
		String printString = getPrintString();
		if(left != null) {
			System.out.println(printString + " --> " + left.getPrintString());
			left.printTree();
		}else{
			System.out.println(printString + " --> NL" + value);
		}
		if(right != null) {
			System.out.println(printString + " --> " + right.getPrintString());
			right.printTree();
		}else{
			System.out.println(printString + " --> NR" + value);
		}
	}

}
