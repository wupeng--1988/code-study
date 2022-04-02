package redblacktree;

/**
 * 红黑树构造
 */
public class RedBlackTreeNew implements Collection{

	//头节点
	private Node head;
	//容量
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
		//存在重复值
		if(node != null && node.value == value){
			return false;
		}
		Node newNode = Node.createNewNode(value, node);
		size++;
		dealDoubleRed(node, newNode);
		return true;
	}

	/**
	 * 专门处理两个连续节点为红色的情况
	 * @param p
	 * @param n
	 */
	private void dealDoubleRed(Node p, Node n){
		if(p == null){
			this.head = n;
			n.red = false;
			return;
		}
		//父节点有黑色， 不用处理
		if(!p.red){
			return;
		}
		//这里pp 不可能是 null， 如果是null， p一定是黑色节点
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
					//左左情况
					rotateRight(pp);
					n.red = false;

					dealDoubleRed(p.parent, p);
				}else{
					//左右情况
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
					//右左情况
					rotateRight(p);
					rotateLeft(pp);
					p.red = false;

					dealDoubleRed(n.parent, n);
				} else {
					//右右情况
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
		//没有对应的值
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
			//找到代替的节点
			Node nextValue = findNextValue(p.right);
			//交换两个节点的颜色
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

		//如果调整的节点不是要删除的节点， 则直接删除节点p
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
		//调整红黑树
		if(!p.red){
			dealDoubleColor(replace);
		}
		//调整完毕之后， 将节点p删除掉,这里是叶子节点
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
	 * 调整一个节点拥有两种颜色的情况
	 *  节点本身的颜色加上  额外的黑色
	 *
	 *  说明： p为n的父节点， B(p -> n_leaf_node):p到n的叶子节点的黑色节点的数量] , B(p -> nBrother_leaf_node):p 到 n的兄弟节点的叶子节点的数量
	 *  当 n 为根节点， 那么仅仅将n调整到黑色， 即可
	 *  当 n为 红+黑时， 仅仅将n调整到黑色， 即可
	 *
	 *  当 n为 黑+黑， 且n不为根节点时
	 *  那么 B(p -> n_leaf_node) >= 2
	 *  由 B(p -> nBrother_leaf_node) = B(p -> n_leaf_node) >= 2
	 *  可以得出 n的兄弟节点一定不为null， 一定有值
	 *
	 *  a: n的兄弟节点为红色  -> 执行向n旋转， 颜色调整， 那么将结构变成n的兄弟节点为黑色的情况
	 *  b: n的兄弟节点为黑色， 且n的兄弟节点的子节点全为黑色， 将n的兄弟节点设置为红色， 将p设置为 n节点， 递归处理
	 *  c：n的兄弟节点为黑色， 且n的兄弟节点的子节点为1黑1红，
	 *  d：n的小弟节点为黑色， 且n的兄弟节点的子节点为 2红
	 * @param n 此节点是双色节点， n本省的颜色+黑色
	 */
	private void dealDoubleColor(Node n){
		if(n == null){
			return;
		}
		Node p = n.parent;
		//如果到达了根节点， 那么处理为根节点
		if(p == null){
			n.red = false;
			this.head = n;
			return;
		}
		//如果本身颜色为红色， 则调整颜色为黑色即可
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

	// 调整 双黑节点 在左边
	private void dealDoubleBlackLeft(Node p, Node n){
		Node b = p.right;
		if(b.red){
			Node bl = b.left;
			//如果兄弟节点为红色
			rotateLeft(p);
			b.red = false;
			p.red = true;
			if(bl == null){
				dealDoubleColor(p);
				return;
			}
			//重新调整兄弟节点
			b = p.right;
		}
		//兄弟节点为黑色的情况, 这种情况兄弟节点的子节点有可能是 空节点
		Node bl = b.left, br = b.right;
		boolean bl_red = bl != null && bl.red;
		boolean br_red = br != null && br.red;
		//兄弟节点的子节点为双黑
		if(!bl_red && !br_red){
			//兄弟节点的子节点为双黑, 将兄弟节点设置为红色， 是p下面的黑色节点数量-1，  p为双色节点， 递归处理
			b.red = true;
			dealDoubleColor(p);
			return;
		}
		//如果右节点不为红色， 调整为红色
		if(!br_red){
			//将n的兄弟节点的右节点调整为红色
			rotateRight(b);
			b.red = true;
			bl.red = false;

			//调整兄弟节点
			b = p.right;
			bl = b.left;
			br = b.right;
		}

		//兄弟节点的右节点为红色， 左节点为黑色
		rotateLeft(p);
		if(br != null) {
			br.red = false;
		}
		b.red = p.red;
		p.red = false;
	}

	//调整双黑节点在右边
	private void dealDoubleBlackRight(Node p, Node n){
		Node b = p.left;
		if(b.red){
			Node br = b.right;
			//如果兄弟节点为红色
			rotateRight(p);
			b.red = false;
			p.red = true;
			if(br == null){
				dealDoubleColor(p);
				return;
			}

			//重新调整节点
			b = p.left;
		}
		//兄弟节点为黑色的情况, 这种情况兄弟节点的子节点有可能是 空节点
		Node bl = b.left, br = b.right;
		boolean bl_red = bl != null && bl.red;
		boolean br_red = br != null && br.red;
		if(!bl_red && !br_red){
			//兄弟节点的子节点为双黑, 将兄弟节点设置为红色， 是p下面的黑色节点数量-1，  p为双色节点， 递归处理
			b.red = true;
			dealDoubleColor(p);
			return;
		}
		if(!bl_red){
			//兄弟节点的左节点为红色
			rotateLeft(b);
			br.red = false;
			b.red = true;

			//调整兄弟节点
			b = p.left;
			bl = b.left;
			br = b.right;
		}
		//兄弟节点的右节点为红色， 左节点为黑色
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
	 * 查找值是否存在，如果存在，返回对应的节点，
	 * 否则，返回应该插入的位置
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
	 * 以 node 为 基点， 向左旋转
	 * @param p
	 * @return  返回旋转之后的头节点
	 */
	private Node rotateLeft(Node p){
		//这样空节点， 或者右边节点为空是无法进行旋转的
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
	 * 以 p 为 基点， 向右旋转
	 * @param p
	 * @return  返回旋转之后的头节点
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

	 	 Node parent;//父节点
		 Node left, right; //左右节点
		 boolean red; //颜色 true-红色， false-黑色
		 int value;

		 private Node(int value, Node parent, Node left, Node right, boolean red) {
		 	this.value = value;
			this.parent = parent;
			this.left = left;
			this.right = right;
			this.red = red;
		 }

		 /**
		  * 构造一个黑色的根节点
		  */
		 public static Node createHeadNode(int value){
		 	return new Node(value, null, null, null, false);
		 }

		 /**
		  * 创建一个新的 红色 的节点， 指定父节点
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
		 * 可以通过plantUml 查看
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
