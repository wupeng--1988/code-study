package redblacktree;

import org.apache.commons.lang.math.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class TestTree {

	public static void main(String[] args){
		int node_size = 64;
//		RedBlackTreeNew redBlackTree = new RedBlackTreeNew();
//		List<Integer> input = new ArrayList<>();
//		for(int index = 1; index <= node_size; index++){
//			int value = RandomUtils.nextInt(200);
//			while(input.contains(value)){
//				value = RandomUtils.nextInt(200);
//			}
//			redBlackTree.addValue(value);
//			input.add(value);
//		}
//
//		System.out.println(input);
		int[] input = new int[]{142, 108, 107, 117, 100, 94, 165, 128, 137, 19, 27, 176, 158, 81, 82, 160, 12, 47, 69, 104, 130, 74, 6, 65, 14, 134, 62, 20, 174, 60, 44, 24, 88, 9, 152, 2, 42, 183, 110, 170, 90, 32, 177, 38, 98, 190, 157, 51, 13, 45, 99, 133, 67, 103, 11, 7, 125, 1, 91, 179, 150, 50, 61, 196};
		RedBlackTreeNew redBlackTree = new RedBlackTreeNew();
		for(int value : input){
			redBlackTree.addValue(value);
		}
//		redBlackTree.removeValue(137);
		System.out.println(redBlackTree.printTree());
	}

	private final static int[] BASE_INPUT = new int[]{10000, 5000, 15000, 2500, 7500, 12500, 17500, 1250, 3750, 6250, 8750, 11250, 13750, 16250, 18750, 625, 1875, 3125, 4375, 5625, 6875, 8125, 9375, 10625, 11875, 13125, 14375, 15625, 16875, 18125, 19375, 312, 937, 1562, 2187, 2812, 3437, 4062, 4687, 5312, 5937, 6562, 7187, 7812, 8437, 9062, 9687, 10312, 10937, 11562, 12187, 12812, 13437, 14062, 14687, 15312, 15937, 16562, 17187, 17812, 18437, 19062, 19687, 156, 468, 781, 1093, 1406, 1718, 2031, 2343, 2656, 2968, 3281, 3593, 3906, 4218, 4531, 4843, 5156, 5468, 5781, 6093, 6406, 6718, 7031, 7343, 7656, 7968, 8281, 8593, 8906, 9218, 9531, 9843, 10156, 10468, 10781, 11093, 11406, 11718, 12031, 12343, 12656, 12968, 13281, 13593, 13906, 14218, 14531, 14843, 15156, 15468, 15781, 16093, 16406, 16718, 17031, 17343, 17656, 17968, 18281, 18593, 18906, 19218, 19531, 19843};
	private static RedBlackTreeNew genBaseTree(){
		RedBlackTreeNew redBlackTree = new RedBlackTreeNew();
		for(int value : BASE_INPUT){
			redBlackTree.addValue(value);
		}
		return redBlackTree;
	}


	//删除节点在右侧， 兄弟节点为黑色， 子节点为双红
	private static void test(){
		int base = 10000;
		int depth = 6;
		//排除6层平衡红黑树
		List<Integer> check = new ArrayList<>();
		List<Integer> input = new ArrayList<>();
		input.add(base);
		check.add(0);
		check.add(base);
		check.add(2*base);
		for(int index = 1; index <= depth; index++){
			List<Integer> newCheck = new ArrayList<>();
			newCheck.add(check.get(0));
			int length = check.size();
			for(int i = 1; i < length; i++){
				int start = check.get(i-1), end = check.get(i);
				int newValue = (start >> 1) + (end >> 1) + (start & end & 1);
				input.add(newValue);
				newCheck.add(newValue);
				newCheck.add(end);
			}
			check = newCheck;
		}
		System.out.println(input);
		genTree(input);
	}

	private static void genTree(List<Integer> values){
		RedBlackTreeNew redBlackTree = new RedBlackTreeNew();
		for(int value : values){
			redBlackTree.addValue(value);
		}
		System.out.println(redBlackTree.printTree());
	}

}
