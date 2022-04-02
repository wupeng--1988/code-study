package redblacktree;

/**
 * 将当前的值设置为int 类型， 用于构造基础模型
 * 后面将数据修改成泛型
 */
public interface Collection {

	boolean addValue(int value);

	boolean removeValue(int value);

	boolean containValue(int value);

}
