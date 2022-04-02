import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class TodayCode extends Parent{

	static {
		System.out.println("todaycode static");
	}

	static int value = 2;

	public static void main(String[] args){
//		int length = 19;
//		int[] value = new int[length];
//		for(int index = 0; index < length; index++){
//			value[index] = index;
//		}
//		for(int index = -1; index <= length; index++){
//			System.out.println(findValue(value, index));
//		}
		System.out.println((5>>1) + (5>>1) + (5 & 5 & 1));
	}

	public List<Integer> busiestServers(int k, int[] arrival, int[] load) {
		//�����б���Ҫ�м�¼��  �ҵ���һ�������б���Ҫ
		//ÿ��������ִ�����������
		int[] runProcess = new int[k];
		//��ʼ���ڵ�
		Node[] node = new Node[k];
		for(int index = 0; index < k; index++){
			int after = index+1;
			if(after >= k){
				after -= k;
			}
			node[index] = new Node(true, after);
		}
		//ʵ���ͷ�ʱ��
		TreeMap<Integer, List<Integer>> needReleaseServer = new TreeMap<>();

		int maxIndex = arrival.length;
		for(int index = 0; index < maxIndex; index++){
			int startServer = index % k;
			int time = arrival[index];

			//�ͷ���Ҫ���е�server
			Iterator<Map.Entry<Integer, List<Integer>>> iterator = needReleaseServer.entrySet().iterator();
			boolean hasAdd = false;
			while(iterator.hasNext()){
				Map.Entry<Integer, List<Integer>> entry = iterator.next();
				if(entry.getKey() > time){
					break;
				}
				iterator.remove();
				for(int value: entry.getValue()){
					//���¶�������ڵ�
					node[value].idle = true;
				}
				hasAdd = true;
			}
			if(hasAdd){
				resetNode(node, k);
			}
			int releaseTime = time + load[index];
			if(node[startServer].idle){
				//��ǰ����
				occupyServer(startServer, releaseTime, runProcess, needReleaseServer, node, k);
			}
			int serverId = node[startServer].after;
			//��ʾû�п��е�
			if(serverId == -1){
				continue;
			}
			occupyServer(serverId, releaseTime, runProcess, needReleaseServer, node, k);
		}

		//�ⲿ�ֿ��Բ����� ��ε��¼����Ӷ�Ϊ
		int maxProcess = 0;
		List<Integer> result = new ArrayList<>();
		for(int index = 0; index < k; index++){
			if(runProcess[index] > maxProcess){
				result.clear();
				result.add(index);
				maxProcess = runProcess[index];
			}else if(runProcess[index] == maxProcess){
				result.add(index);
			}
		}
		return result;
	}

	private void resetNode(Node[] node, int nodeLength){
		int start = -1;
		for(int index = 0; index < nodeLength; index++){
			if(node[index].idle){
				start = index;
				break;
			}
		}
		if(start == -1){
			return;
		}
		int after = start;
		//�ӵ�һ�����е�ǰһ����ʼ����
		start = indexReduce(start, nodeLength);
		int index = start;
		do{
			node[index].after = after;
			if(node[index].idle){
				after = index;
			}
			index = indexReduce(index, nodeLength);
		}while (index != start);
	}

	//ռ�÷�����
	private void occupyServer(int serverId, int releaseTime, int[] runProcess, TreeMap<Integer, List<Integer>> needReleaseServer, Node[] node, int nodeLength){
		runProcess[serverId]++;
		List<Integer> serverIds = needReleaseServer.computeIfAbsent(releaseTime, K -> new ArrayList<>());
		serverIds.add(serverId);

		node[serverId].idle = false;
		int newAfter = (node[serverId].after != serverId) ? node[serverId].after : -1;
		int index = indexReduce(serverId, nodeLength);
		while(node[index].after == serverId) {
			node[index].after = newAfter;
			index = indexReduce(index, nodeLength);
		}
	}

	private int indexReduce(int index, int length){
		index -= 1;
		return (index < 0) ? index+length : index;
	}

	class Node{
		boolean idle;
		int after;

		public Node(boolean idle, int after) {
			this.idle = idle;
			this.after = after;
		}
	}


	public static int findValue (int[] value, int k){
		int result = -1;
		int start = 0, end = value.length - 1;
		while(start <= end){
			int min = (start + end) / 2;
			if(value[min] == k){
				result = min;
				break;
			}else if(value[min] > k){
				end = min-1;
			}else{
				start = min+1;
			}
		}
		return result;
	}

	public int countZero(int n){
		Map<Integer, Integer> count_5 = new HashMap<>();
		int result = 0;
		for(int index = 5; index <= n; index += 5){
			int litter = index / 5;
			int count = count_5.getOrDefault(litter, 0) + 1;
			result += count;
			count_5.put(index, count);
		}
		return result;
	}

	public int countZeroPro(int n){
		//���ջ���Ϊͳ��5������
		int result = 0;
		for(int divisor = 5; divisor <= n; divisor *= 5){
			int count = n / divisor;
			result += count;
		}
		return result;
	}

	//ָ������֮��
	private Map<Integer, Integer> countIndex = new HashMap<>();

	private int findMinK(int n, int k){
		int length = 0, times = 1, sumIndex = 1;
		int temp = n;
		countIndex.put(-1, 0);
		while(temp > 0){
			countIndex.put(length, sumIndex);

			temp /= 10;
			length++;

			times *= 10;
			sumIndex += times;
		}
		return doFindMinK(n, times/10, 0, length-1, k);
	}

	private int doFindMinK(int maxValue, int times, int headNumber, int tailLength, int k){
		if( k == 0){
			return headNumber;
		}
		int head_maxValue = maxValue/times;
		int tail_maxValue = maxValue%times;
		for(int index = 0; index <= 9; index++){
			int currentHead = headNumber*10 + index;
			if(currentHead == 0){
				continue;
			}
			//����������� ͷ�� < maxValueͷ�� tailLength  ͷ�� > maxValueͷ��  tailLength-1 ͷ�� == maxValueͷ�� tailLength+ (maxValueβ��+1)
			int maxCount;
			if(currentHead < head_maxValue){
				maxCount = countIndex.get(tailLength);
			}else if(currentHead > head_maxValue){
				maxCount = countIndex.get(tailLength-1);
			}else{
				maxCount = countIndex.get(tailLength-1) + tail_maxValue + 1;
			}

			if(maxCount < k){
				k -= maxCount;
			}else{
				return doFindMinK(maxValue, times/10, currentHead, tailLength-1, k-1);
			}
		}
		return -1;
	}


	public long subArrayRanges(int[] nums) {
		long result = 0;
		int length = nums.length;
		//�ָ����
		for(int start = 0; start < length-1; start++){
			int max = nums[start], min = nums[start];
			for(int end = start + 1; end < length; end++){
				max = Math.max(nums[end], max);
				min = Math.min(nums[end], min);
				result += (max-min);
			}
		}
		return result;
	}

	private long simulationOperate2(int[] nums){
		long result = 0;
		int length = nums.length;
		//�ָ����
		for(int start = 0; start < length-1; start++){
			int max = nums[start], min = nums[start];
			for(int end = start + 1; end < length; end++){
				max = Math.max(nums[end], max);
				min = Math.min(nums[end], min);
				result += (max-min);
			}
		}
		return result;
	}

	//ģ�ⷨ��� ʱ�临�Ӷ�:O(n^2), �д����ظ��Ĺ���
	private long simulationOperate(int[] nums){
		long result = 0;
		int length = nums.length;
		//�ָ����
		for(int childLength = 2; childLength <= length; childLength++){
			//�������Сջ
			LinkedList<Integer> maxStack = new LinkedList<>(), minStack = new LinkedList<>();
			for(int index = 0; index < childLength-1; index++){
				addStack(maxStack, minStack, null, nums[index]);
			}
			for(int index = childLength-1; index < length; index++){
				Integer removeValue = (index-childLength >= 0) ? nums[index-childLength] : null;
				addStack(maxStack, minStack, removeValue, nums[index]);
				result += (maxStack.getFirst() - minStack.getFirst());
			}
		}
		return result;
	}

	//ά��ջֵ
	private void addStack(LinkedList<Integer> maxStack, LinkedList<Integer> minStack, Integer removeValue, Integer addValue){
		//�Ƴ���ֵ
		if(removeValue != null){
			if(!maxStack.isEmpty() && maxStack.getFirst().equals(removeValue)){
				maxStack.removeFirst();
			}
			if(!minStack.isEmpty() && minStack.getFirst().equals(removeValue)){
				minStack.removeFirst();
			}
		}
		while(!maxStack.isEmpty()){
			if(maxStack.getLast() >= addValue){
				break;
			}
			maxStack.removeLast();
		}
		while (!minStack.isEmpty()){
			if(minStack.getLast() <= addValue){
				break;
			}
			minStack.removeLast();
		}
		maxStack.addLast(addValue);
		minStack.addLast(addValue);
	}

}
