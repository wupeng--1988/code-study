package redblacktree;

/**
 * ���������
 */
public class RedBlackTreeNew implements Collection{

	//ͷ�ڵ�
	private Node head;
	//����
	private int size;

	public int size() {
		return size;
	}

	public RedBlackTreeNew() {
		this.head = null;
		this.size = 0;
	}

	public void clear(){
		this.head = null;
		this.size = 0;
	}

	@Override
	public boolean addValue(int value) {
		if(this.head == null){
			this.head = Node.createHeadNode(value);
			size++;
			return true;
		}
		Node node = find(value);
		//�����ظ�ֵ
		if(node != null && node.value == value){
			return false;
		}
		Node newNode = Node.createNewNode(value, node);
		size++;
		dealDoubleRed(node, newNode);
		return true;
	}

	/**
	 * ר�Ŵ������������ڵ�Ϊ��ɫ�����
	 * @param p
	 * @param n
	 */
	private void dealDoubleRed(Node p, Node n){
		if(p == null){
			this.head = n;
			n.red = false;
			return;
		}
		//���ڵ��к�ɫ�� ���ô���
		if(!p.red){
			return;
		}
		//����pp �������� null�� �����null�� pһ���Ǻ�ɫ�ڵ�
		Node pp = p.parent;
		if(pp == null){
			p.red = false;
			this.head = p;
			return;
		}

		if(pp.left == p){
			Node ppr = pp.right;
			if(ppr != null && ppr.red){
				p.red = false;
				ppr.red =false;
				pp.red = true;

				dealDoubleRed(pp.parent, pp);
			}else{
				if(p.left == n){
					//�������
					rotateRight(pp);
					n.red = false;

					dealDoubleRed(p.parent, p);
				}else{
					//�������
					rotateLeft(p);
					rotateRight(pp);
					p.red = false;

					dealDoubleRed(n.parent, n);
				}
			}
		}else{
			Node ppl = pp.left;
			if(ppl != null && ppl.red){
				p.red = false;
				ppl.red = false;
				pp.red = true;
				dealDoubleRed(pp.parent, pp);
			}else {
				if (p.left == n) {
					//�������
					rotateRight(p);
					rotateLeft(pp);
					p.red = false;

					dealDoubleRed(n.parent, n);
				} else {
					//�������
					rotateLeft(pp);
					n.red = false;

					dealDoubleRed(p.parent, p);
				}
			}
		}
	}

	@Override
	public boolean removeValue(int value) {
		if(this.head == null){
			return false;
		}
		Node node = find(value);
		//û�ж�Ӧ��ֵ
		if(node == null || node.value != value){
			return false;
		}
		size--;
		deleteNode(node);
		return true;
	}

	private void deleteNode(Node p){
		Node pl = p.left, pr = p.right;
		Node replace;
		if(pl != null && pr != null){
			//�ҵ�����Ľڵ�
			Node nextValue = findNextValue(p.right);
			//���������ڵ����ɫ
			boolean tempColor = nextValue.red;
			nextValue.red = p.red;
			p.red = tempColor;

			Node pp = p.parent;
			Node nr = nextValue.right;
			if(pp != null) {
				if (pp.left == p) {
					pp.left = nextValue;
				} else {
					pp.right = nextValue;
				}
			}else{
				this.head = nextValue;
			}

			if(nextValue != p.right){
				Node np = nextValue.parent;
				p.parent = np;
				if(np != null){
					if(np.left == nextValue){
						np.left = p;
					}else{
						np.right = p;
					}
				}
				nextValue.right = pr;
				pr.parent = nextValue;
			}else{
				p.parent = nextValue;
				nextValue.right = p;
			}
			p.right = nr;
			p.left = null;
			nextValue.left = pl;
			pl.parent = nextValue;
			nextValue.parent = pp;

			if(nr != null){
				nr.parent = p;
				replace = nr;
			}else{
				replace = p;
			}
		}else if(pl != null){
			replace = pl;
		}else if(pr != null){
			replace = pr;
		}else {
			replace = p;
		}

		//��������Ľڵ㲻��Ҫɾ���Ľڵ㣬 ��ֱ��ɾ���ڵ�p
		if(replace != p){
			Node pp = p.parent;
			if(pp == null){
				this.head = replace;
			}else if(pp.left == p){
				pp.left = replace;
			}else{
				pp.right = replace;
			}
			replace.parent = pp;
			p.left = p.right = p.parent = null;
		}
		//���������
		if(!p.red){
			dealDoubleColor(replace);
		}
		//�������֮�� ���ڵ�pɾ����,������Ҷ�ӽڵ�
		if(replace == p) {
			Node pp = p.parent;
			if(pp == null){
			}else if(pp.left == p){
				pp.left = null;
			}else{
				pp.right = null;
			}
		}
	}

	/**
	 * ����һ���ڵ�ӵ��������ɫ�����
	 *  �ڵ㱾�����ɫ����  ����ĺ�ɫ
	 *
	 *  ˵���� pΪn�ĸ��ڵ㣬 B(p -> n_leaf_node):p��n��Ҷ�ӽڵ�ĺ�ɫ�ڵ������] , B(p -> nBrother_leaf_node):p �� n���ֵܽڵ��Ҷ�ӽڵ������
	 *  �� n Ϊ���ڵ㣬 ��ô������n��������ɫ�� ����
	 *  �� nΪ ��+��ʱ�� ������n��������ɫ�� ����
	 *
	 *  �� nΪ ��+�ڣ� ��n��Ϊ���ڵ�ʱ
	 *  ��ô B(p -> n_leaf_node) >= 2
	 *  �� B(p -> nBrother_leaf_node) = B(p -> n_leaf_node) >= 2
	 *  ���Եó� n���ֵܽڵ�һ����Ϊnull�� һ����ֵ
	 *
	 *  a: n���ֵܽڵ�Ϊ��ɫ  -> ִ����n��ת�� ��ɫ������ ��ô���ṹ���n���ֵܽڵ�Ϊ��ɫ�����
	 *  b: n���ֵܽڵ�Ϊ��ɫ�� ��n���ֵܽڵ���ӽڵ�ȫΪ��ɫ�� ��n���ֵܽڵ�����Ϊ��ɫ�� ��p����Ϊ n�ڵ㣬 �ݹ鴦��
	 *  c��n���ֵܽڵ�Ϊ��ɫ�� ��n���ֵܽڵ���ӽڵ�Ϊ1��1�죬
	 *  d��n��С�ܽڵ�Ϊ��ɫ�� ��n���ֵܽڵ���ӽڵ�Ϊ 2��
	 * @param n �˽ڵ���˫ɫ�ڵ㣬 n��ʡ����ɫ+��ɫ
	 */
	private void dealDoubleColor(Node n){
		if(n == null){
			return;
		}
		Node p = n.parent;
		//��������˸��ڵ㣬 ��ô����Ϊ���ڵ�
		if(p == null){
			n.red = false;
			this.head = n;
			return;
		}
		//���������ɫΪ��ɫ�� �������ɫΪ��ɫ����
		if(n.red){
			n.red = false;
			return;
		}
		if(p.left == n){
			dealDoubleBlackLeft(p, n);
		}else{
			dealDoubleBlackRight(p, n);
		}
	}

	// ���� ˫�ڽڵ� �����
	private void dealDoubleBlackLeft(Node p, Node n){
		Node b = p.right;
		if(b.red){
			Node bl = b.left;
			//����ֵܽڵ�Ϊ��ɫ
			rotateLeft(p);
			b.red = false;
			p.red = true;
			if(bl == null){
				dealDoubleColor(p);
				return;
			}
			//���µ����ֵܽڵ�
			b = p.right;
		}
		//�ֵܽڵ�Ϊ��ɫ�����, ��������ֵܽڵ���ӽڵ��п����� �սڵ�
		Node bl = b.left, br = b.right;
		boolean bl_red = bl != null && bl.red;
		boolean br_red = br != null && br.red;
		//�ֵܽڵ���ӽڵ�Ϊ˫��
		if(!bl_red && !br_red){
			//�ֵܽڵ���ӽڵ�Ϊ˫��, ���ֵܽڵ�����Ϊ��ɫ�� ��p����ĺ�ɫ�ڵ�����-1��  pΪ˫ɫ�ڵ㣬 �ݹ鴦��
			b.red = true;
			dealDoubleColor(p);
			return;
		}
		//����ҽڵ㲻Ϊ��ɫ�� ����Ϊ��ɫ
		if(!br_red){
			//��n���ֵܽڵ���ҽڵ����Ϊ��ɫ
			rotateRight(b);
			b.red = true;
			bl.red = false;

			//�����ֵܽڵ�
			b = p.right;
			bl = b.left;
			br = b.right;
		}

		//�ֵܽڵ���ҽڵ�Ϊ��ɫ�� ��ڵ�Ϊ��ɫ
		rotateLeft(p);
		if(br != null) {
			br.red = false;
		}
		b.red = p.red;
		p.red = false;
	}

	//����˫�ڽڵ����ұ�
	private void dealDoubleBlackRight(Node p, Node n){
		Node b = p.left;
		if(b.red){
			Node br = b.right;
			//����ֵܽڵ�Ϊ��ɫ
			rotateRight(p);
			b.red = false;
			p.red = true;
			if(br == null){
				dealDoubleColor(p);
				return;
			}

			//���µ����ڵ�
			b = p.left;
		}
		//�ֵܽڵ�Ϊ��ɫ�����, ��������ֵܽڵ���ӽڵ��п����� �սڵ�
		Node bl = b.left, br = b.right;
		boolean bl_red = bl != null && bl.red;
		boolean br_red = br != null && br.red;
		if(!bl_red && !br_red){
			//�ֵܽڵ���ӽڵ�Ϊ˫��, ���ֵܽڵ�����Ϊ��ɫ�� ��p����ĺ�ɫ�ڵ�����-1��  pΪ˫ɫ�ڵ㣬 �ݹ鴦��
			b.red = true;
			dealDoubleColor(p);
			return;
		}
		if(!bl_red){
			//�ֵܽڵ����ڵ�Ϊ��ɫ
			rotateLeft(b);
			br.red = false;
			b.red = true;

			//�����ֵܽڵ�
			b = p.left;
			bl = b.left;
			br = b.right;
		}
		//�ֵܽڵ���ҽڵ�Ϊ��ɫ�� ��ڵ�Ϊ��ɫ
		rotateRight(p);
		if(bl != null) {
			bl.red = false;
		}
		b.red = p.red;
		p.red = false;
	}

	private Node findNextValue(Node n){
		while(n.left != null){
			n = n.left;
		}
		return n;
	}

	@Override
	public boolean containValue(int value) {
		return false;
	}

	/**
	 * ����ֵ�Ƿ���ڣ�������ڣ����ض�Ӧ�Ľڵ㣬
	 * ���򣬷���Ӧ�ò����λ��
	 * @param value
	 * @return
	 */
	public Node find(int value){
		Node n = this.head, p = n;
		while(n != null){
			if(n.value == value){
				return n;
			}
			p = n;
			if(n.value > value){
				n = n.left;
			}else{
				n = n.right;
			}
		}
		return p;
	}

	/**
	 * �� node Ϊ ���㣬 ������ת
	 * @param p
	 * @return  ������ת֮���ͷ�ڵ�
	 */
	private Node rotateLeft(Node p){
		//�����սڵ㣬 �����ұ߽ڵ�Ϊ�����޷�������ת��
		if(p == null || p.right == null){
			return p;
		}
		Node pp = p.parent;
		Node r = p.right;
		Node rl = r.left;

		if(rl != null){
			rl.parent = p;
		}
		p.right = rl;
		p.parent = r;
		r.left = p;
		r.parent = pp;

		if(pp == null){
			r.red = false;
			this.head = pp;
		}else{
			if(pp.left == p){
				pp.left = r;
			}else{
				pp.right = r;
			}
		}
		return r;
	}

	/**
	 * �� p Ϊ ���㣬 ������ת
	 * @param p
	 * @return  ������ת֮���ͷ�ڵ�
	 */
	public Node rotateRight(Node p){
		if(p == null || p.left == null){
			return p;
		}

		Node pp = p.parent;
		Node l = p.left;
		Node lr = l.right;

		if(lr != null){
			lr.parent = p;
		}
		p.left = lr;
		p.parent = l;
		l.right = p;
		l.parent = pp;

		if(pp == null){
			l.red = false;
			this.head = pp;
		}else{
			if(pp.left == p){
				pp.left = l;
			}else{
				pp.right = l;
			}
		}

		return l;
	}


	public StringBuilder printTree(){
		StringBuilder builder = new StringBuilder();
		builder.append("@startuml").append("\n");
		builder.append("skinparam activity{").append("\n");
		builder.append("FontName Impact").append("\n");
		builder.append("FontColor white").append("\n");
		builder.append("backgroundcolor black").append("\n");
		builder.append("}").append("\n");
		builder.append("(*) -->" + head.value).append("\n");
		builder.append(head.printTree());
		builder.append("@enduml").append("\n");
		return builder;
	}


	private static class Node{

	 	 Node parent;//���ڵ�
		 Node left, right; //���ҽڵ�
		 boolean red; //��ɫ true-��ɫ�� false-��ɫ
		 int value;

		 private Node(int value, Node parent, Node left, Node right, boolean red) {
		 	this.value = value;
			this.parent = parent;
			this.left = left;
			this.right = right;
			this.red = red;
		 }

		 /**
		  * ����һ����ɫ�ĸ��ڵ�
		  */
		 public static Node createHeadNode(int value){
		 	return new Node(value, null, null, null, false);
		 }

		 /**
		  * ����һ���µ� ��ɫ �Ľڵ㣬 ָ�����ڵ�
		  */
		 public static Node createNewNode(int value, Node parent){
		 	Node newNode = new Node(value, parent, null, null, true);
		 	if(parent == null){
		 		return newNode;
			}
		 	if(parent.value > value){
		 		parent.left = newNode;
			}else{
		 		parent.right = newNode;
			}
		 	return newNode;
		 }

		 public boolean isRed(){
		 	return red;
		 }
		public String getPrintString(){
			if(red){
				return value + "#red";
			}else{
				return value + "#black";
			}
		}

		/**
		 * ����ͨ��plantUml �鿴
		 */
		public String printTree(){
			if(left == null && right == null){
				return "";
			}
			StringBuilder builder = new StringBuilder();
			String printString = getPrintString();
			if(left != null) {
				builder.append(printString + " --> " + left.getPrintString()).append("\n");
				builder.append(left.printTree());
			}else{
				builder.append(printString + " --> NL" + value).append("\n");
			}
			if(right != null) {
				builder.append(printString + " --> " + right.getPrintString()).append("\n");
				builder.append(right.printTree());
			}else{
				builder.append(printString + " --> NR" + value).append("\n");
			}
			return builder.toString();
		}
	 }

}
