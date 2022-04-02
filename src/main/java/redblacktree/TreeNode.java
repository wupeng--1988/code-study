package redblacktree;

import com.sun.org.apache.regexp.internal.RE;

/**
 * ���������� ��һ������Ķ�����
 * 1��ÿ���ڵ�����Ǻ�ɫ���ߺ���
 * 2�����ڵ��Ǻ�ɫ
 * 3��ÿ��Ҷ�ӽڵ�(NIL)�Ǻ�ɫ
 * 4�����һ���ڵ��Ǻ�ɫ����ô�����ӽڵ���Ǻ�ɫ
 * 5����һ���ڵ㵽�ýڵ������ڵ������·���ϰ�����ͬ��Ŀ�ĺڽڵ�
 */

public class TreeNode {

	public static final int BLACK = 0, RED = 1;

	//��ǰ��ֵ
	public int value;
	public int color;
	public TreeNode parent;
	public TreeNode left, right;

	//Ĭ�ϴ���һ����ɫ��
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
	 * ����ͨ��plantUml �鿴
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
