package redblacktree;

/**
 * 红黑树实现
 */
public class RedBlackTree {

	private TreeNode head = null;
	int size = 0;

	/**
	 * 添加
	 * @param value
	 * @return true-添加成功  false-重复数据
	 */
	public boolean addValue(int value){
		if(head == null){
			head = TreeNode.createHead(value);
			size++;
			return true;
		}
		TreeNode parentNode = findValue(value);
		if(parentNode.value == value){
			//已经插入了
			return false;
		}

		//创建新的节点
		TreeNode newNode = TreeNode.createNewNode(value, parentNode);
		size++;
		//连续两个节点为红色节点， 需要旋转
		treeRotateAfterInsert(parentNode, newNode);
		return true;
	}

	/**
	 * 用于解决添加数据之后， 树的旋转
	 * @param parent
	 * @param child
	 */
	private void treeRotateAfterInsert(TreeNode parent, TreeNode child){
		//说明child就是根节点了， 改变颜色并替换根节点
		if(parent == null){
			child.toBlack();
			this.head = child;
			return;
		}

		if(parent.isBlack()){
			return;
		}

		TreeNode pp = parent.parent;
		if(pp == null){
			//parent为根节点
			System.out.println("根节点不为black, 不是理想情况, 临时修正");
			parent.toBlack();
			return;
		}

		//分为4种情况, 四种解法使用的是枚举，
		// 没有使用思想， 这样的话，为后续的扩展不方便， 使之落入下乘；
		// 之后修改成左旋与右旋， 然后修改节点颜色；
		if(parent == pp.left){
			if(child == parent.left){
				roteLL(pp, parent, child);
			}else{
				roteLR(pp, parent, child);
			}
		}else{
			if(child == parent.left){
				roteRL(pp, parent,child);
			}else{
				roteRR(pp, parent, child);
			}
		}
	}

	/**
	 * 转化结构如图，
	 *         pp(B)                                  p(R)
	 *        /     \                               /     \
	 *      p(R)   ppRC                          cL(B)   pp(B)
	 *    /     \               ==>                     /    \
	 *  cL(R)   pRC                                   pRC   ppRC
	 * @param pp
	 * @param p
	 * @param cL
	 */
	private void roteLL(TreeNode pp, TreeNode p, TreeNode cL){
		TreeNode ppp = pp.parent;
		TreeNode pR = p.right;

		p.changeParent(ppp);
		pp.changeParent(p);
		if(pR != null) {
			pR.changeParent(pp);
		}else{
			pp.left = null;
		}
		cL.toBlack();

		treeRotateAfterInsert(ppp, p);
	}

	/**
	 * 转化结构如图，
	 *               pp(B)                                  pp(B)                           cR(R)
	 *              /     \                                /     \                         /     \
	 *            p(R)   ppRC                            cR(R)   ppRC                   p(B)     pp(B)
	 *          /     \               ==>              /     \             ==>         /   \     /    \
	 *        pLC   cR(R)                            p(R)   cRRC                     pLC  cRLC  cRRC  ppRC
	 *             /    \                           /   \
	 *           cRLC   cRRC                      pLC   cRLC
	 * @param pp
	 * @param p
	 * @param cR
	 */
	private void roteLR(TreeNode pp, TreeNode p, TreeNode cR){
		TreeNode ppp = pp.parent;
		TreeNode cRR = cR.right;
		TreeNode cRL = cR.left;

		pp.changeParent(cR);
		p.changeParent(cR);
		cR.changeParent(ppp);
		if(cRL != null) {
			cRL.changeParent(p);
		}else{
			p.right = null;
		}
		if(cRR != null) {
			cRR.changeParent(pp);
		}else{
			pp.left = null;
		}
		p.toBlack();

		treeRotateAfterInsert(ppp, cR);
	}
	/**
	 * 转化结构如图，
	 *               pp(B)                         pp(B)                                  cL(R)
	 *              /     \                       /     \                                /     \
	 *           ppLC   p(R)                   ppLC   cL(R)                          pp(B)     p(B)
	 *                 /   \           ==>            /   \             ==>         /    \     /    \
	 *              cL(R)  pRC                     cLLC   p(R)                    ppLC  cLLC  cLRC  pRC
	 *             /    \                                /   \
	 *           cLLC   cLRC                           cLRC  pRC
	 * @param pp
	 * @param p
	 * @param cL
	 */
	private void roteRL(TreeNode pp, TreeNode p, TreeNode cL){
		TreeNode ppp = pp.parent;
		TreeNode cLL = cL.left;
		TreeNode cLR = cL.right;

		pp.changeParent(cL);
		p.changeParent(cL);
		cL.changeParent(ppp);
		if(cLL != null) {
			cLL.changeParent(pp);
		}else{
			pp.right = null;
		}
		if(cLR != null) {
			cLR.changeParent(p);
		}else{
			p.left = null;
		}
		p.toBlack();

		treeRotateAfterInsert(ppp, cL);
	}

	/**
	 * 转化结构如图，
	 *         pp(B)                                p(R)
	 *        /     \                             /     \
	 *      ppL    p(R)                         pp(B)   cR(B)
	 *           /     \      ==>              /   \
	 *         pLC   cR(R)                   ppL  pLC
	 * @param pp
	 * @param p
	 * @param cR
	 */
	private void roteRR(TreeNode pp, TreeNode p, TreeNode cR){
		TreeNode ppp = pp.parent;
		TreeNode pL = p.left;

		pp.changeParent(p);
		p.changeParent(ppp);
		if(pL != null) {
			pL.changeParent(pp);
		}else{
			pp.right = null;
		}
		cR.toBlack();

		treeRotateAfterInsert(ppp, p);
	}

	/**
	 * 移除数据
	 * @param value
	 * @return
	 */
	public boolean removeValue(int value){
		TreeNode deleteNode = findValue(value);
		//不包含此节点， 直接返回
		if(deleteNode == null || deleteNode.value != value){
			return false;
		}
		deleteNode(deleteNode);
		return true;
	}

	private void deleteNode(TreeNode node){
		TreeNode p = node.parent;
		TreeNode adjust = null;
		if(node.left == null && node.right == null){
			//叶子节点
			if(p == null){
				this.head = null;
				return;
			}
			if(node.value > p.value){
				p.right = null;
			}else{
				p.left = null;
			}
		}else if(node.left != null && node.right != null){
			//拥有左右子节点
			TreeNode replaceNode = findNextNode(node.right);

			//使用刚好大于的节点, 代替删除节点，   将之前的代替节点所在的位置删除
			TreeNode newNode = TreeNode.createNewNode(replaceNode.value, p);
			node.left.changeParent(newNode);
			node.right.changeParent(newNode);
			if(node.isRed()){
				newNode.toRed();
			}else{
				newNode.toBlack();
			}

			deleteNode(replaceNode);
			return;
		}else if(node.left != null){
			//只有左节点
			node.left.changeParent(p);
			if(p == null){
				this.head = node.left;
				node.left.toBlack();
				return;
			}
			adjust = node.left;
		}else{
			//只有右节点
			node.right.changeParent(p);
			if(p == null){
				this.head = node.right;
				node.right.toBlack();
				return;
			}
			adjust = node.right;
		}

		//调整为一棵标准的红黑树
		if(node.isRed()){
			//被删除的节点是红色节点， 不用调整
			return;
		}
	}

	public TreeNode findNextNode(TreeNode node){
		while(node.left != null){
			node = node.left;
		}
		return node;
	}

	/**
	 * 是否包含对应值
	 * @param value
	 * @return
	 */
	public boolean containValue(int value){
		TreeNode node = findValue(value);
		return node != null && node.value == value;
	}

	/**
	 * 查找是否包含次数值,
	 * 二分查找树， 时间是O(logn)
	 * @param value
	 * @return
	 */
	public TreeNode findValue(int value){
		TreeNode element = head, parent = head;
		while(element != null){
			if(element.value == value){
				return element;
			}else if(element.value > value){
				parent = element;
				element = element.left;
			}else{
				parent = element;
				element = element.right;
			}
		}
		return parent;
	}

	public void printTree(){
		System.out.println("@startuml");
		System.out.println("skinparam activity{");
		System.out.println("FontName Impact");
		System.out.println("FontColor white");
		System.out.println("backgroundcolor black");
		System.out.println("}");
		System.out.println("(*) -->" + head.value);
		head.printTree();
		System.out.println("@enduml");
	}

}
