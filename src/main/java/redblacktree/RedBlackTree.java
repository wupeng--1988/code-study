package redblacktree;

/**
 * �����ʵ��
 */
public class RedBlackTree {

	private TreeNode head = null;
	int size = 0;

	/**
	 * ���
	 * @param value
	 * @return true-��ӳɹ�  false-�ظ�����
	 */
	public boolean addValue(int value){
		if(head == null){
			head = TreeNode.createHead(value);
			size++;
			return true;
		}
		TreeNode parentNode = findValue(value);
		if(parentNode.value == value){
			//�Ѿ�������
			return false;
		}

		//�����µĽڵ�
		TreeNode newNode = TreeNode.createNewNode(value, parentNode);
		size++;
		//���������ڵ�Ϊ��ɫ�ڵ㣬 ��Ҫ��ת
		treeRotateAfterInsert(parentNode, newNode);
		return true;
	}

	/**
	 * ���ڽ���������֮�� ������ת
	 * @param parent
	 * @param child
	 */
	private void treeRotateAfterInsert(TreeNode parent, TreeNode child){
		//˵��child���Ǹ��ڵ��ˣ� �ı���ɫ���滻���ڵ�
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
			//parentΪ���ڵ�
			System.out.println("���ڵ㲻Ϊblack, �����������, ��ʱ����");
			parent.toBlack();
			return;
		}

		//��Ϊ4�����, ���ֽⷨʹ�õ���ö�٣�
		// û��ʹ��˼�룬 �����Ļ���Ϊ��������չ�����㣬 ʹ֮�����³ˣ�
		// ֮���޸ĳ������������� Ȼ���޸Ľڵ���ɫ��
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
	 * ת���ṹ��ͼ��
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
	 * ת���ṹ��ͼ��
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
	 * ת���ṹ��ͼ��
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
	 * ת���ṹ��ͼ��
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
	 * �Ƴ�����
	 * @param value
	 * @return
	 */
	public boolean removeValue(int value){
		TreeNode deleteNode = findValue(value);
		//�������˽ڵ㣬 ֱ�ӷ���
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
			//Ҷ�ӽڵ�
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
			//ӵ�������ӽڵ�
			TreeNode replaceNode = findNextNode(node.right);

			//ʹ�øպô��ڵĽڵ�, ����ɾ���ڵ㣬   ��֮ǰ�Ĵ���ڵ����ڵ�λ��ɾ��
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
			//ֻ����ڵ�
			node.left.changeParent(p);
			if(p == null){
				this.head = node.left;
				node.left.toBlack();
				return;
			}
			adjust = node.left;
		}else{
			//ֻ���ҽڵ�
			node.right.changeParent(p);
			if(p == null){
				this.head = node.right;
				node.right.toBlack();
				return;
			}
			adjust = node.right;
		}

		//����Ϊһ�ñ�׼�ĺ����
		if(node.isRed()){
			//��ɾ���Ľڵ��Ǻ�ɫ�ڵ㣬 ���õ���
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
	 * �Ƿ������Ӧֵ
	 * @param value
	 * @return
	 */
	public boolean containValue(int value){
		TreeNode node = findValue(value);
		return node != null && node.value == value;
	}

	/**
	 * �����Ƿ��������ֵ,
	 * ���ֲ������� ʱ����O(logn)
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
