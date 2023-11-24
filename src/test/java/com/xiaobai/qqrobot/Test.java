package com.xiaobai.qqrobot;

import java.util.Arrays;
import java.util.Stack;

/**
 * @author xiaobai
 * @date 2023/11/4-20:31
 */
public class Test {
    static class ListNode {
      int val;
      ListNode next;
      ListNode() {}
      ListNode(int val) { this.val = val; }
      ListNode(int val, ListNode next) { this.val = val; this.next = next; }
  }

    public int[] reverseBookList(ListNode head) {
        Stack<Integer> resultStack = new Stack<>();
        while(head!=null){
            resultStack.push(head.val);
            head = head.next;
        }
        int[] result = new int[resultStack.size()];
        for(int i=0;i<resultStack.size();i++){
            result[i] = resultStack.pop();
        }
        return result;
    }
    public static void main(String[] args) {
        System.out.println(Arrays.toString(new Test().reverseBookList(new ListNode(3, new ListNode(6, new ListNode(4, new ListNode(1)))))));

    }
}
