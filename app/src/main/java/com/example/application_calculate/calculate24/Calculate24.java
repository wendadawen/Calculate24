package com.example.application_calculate.calculate24;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Calculate24 {

    private static final String[] symbols = {"+", "-", "*", "/"};
    private static final List<List<Integer>> permutations = Arrays.asList(
            Arrays.asList(1, 2, 3, 4),
            Arrays.asList(1, 2, 4, 3),
            Arrays.asList(1, 3, 2, 4),
            Arrays.asList(1, 3, 4, 2),
            Arrays.asList(1, 4, 2, 3),
            Arrays.asList(1, 4, 3, 2),
            Arrays.asList(2, 1, 3, 4),
            Arrays.asList(2, 1, 4, 3),
            Arrays.asList(2, 3, 1, 4),
            Arrays.asList(2, 3, 4, 1),
            Arrays.asList(2, 4, 1, 3),
            Arrays.asList(2, 4, 3, 1),
            Arrays.asList(3, 1, 2, 4),
            Arrays.asList(3, 1, 4, 2),
            Arrays.asList(3, 2, 1, 4),
            Arrays.asList(3, 2, 4, 1),
            Arrays.asList(3, 4, 1, 2),
            Arrays.asList(3, 4, 2, 1),
            Arrays.asList(4, 1, 2, 3),
            Arrays.asList(4, 1, 3, 2),
            Arrays.asList(4, 2, 1, 3),
            Arrays.asList(4, 2, 3, 1),
            Arrays.asList(4, 3, 1, 2),
            Arrays.asList(4, 3, 2, 1)
    );

    // 给4个数字，返回结果为24的字符串数组
    public static ArrayList<String> solve(int[] nums) {
        ArrayList<String> ret = new ArrayList<>();
        // 一共：64 * 24 * 5 种组合
        // 64种运算符组合: 4^3
        for (int i = 0; i < 64; i++) {
            // 位运算计算运算符组合
            String ch1 = symbols[((i>>0&1)+((i>>1&1)<<1))];
            String ch2 = symbols[((i>>2&1)+((i>>3&1)<<1))];
            String ch3 = symbols[((i>>4&1)+((i>>5&1)<<1))];
            // 24种数字组合：4!
            for (List<Integer> p : permutations) {
                String a=""+nums[p.get(0)-1], b=""+nums[p.get(1)-1], c=""+nums[p.get(2)-1], d=""+nums[p.get(3)-1];
                // 五种括号组合
                // ((AB)C)D
                String[] expressionList = new String[]{"(", "(", a, ch1, b, ")", ch2, c, ")", ch3, d};
                if(evalRPN(parseToSuffixExpression(expressionList)) == 24) {
                    ret.add(Arrays.toString(expressionList).replaceAll("(\\[|\\]|,| )", "") + "=24");
                }
                // (A(BC))D
                expressionList = new String[]{"(", a, ch1, "(", b, ch2, c, ")", ")", ch3, d};
                if(evalRPN(parseToSuffixExpression(expressionList)) == 24) {
                    ret.add(Arrays.toString(expressionList).replaceAll("(\\[|\\]|,| )", "") + "=24");
                }
                // (AB)(CD)
                expressionList = new String[]{"(", a, ch1, b, ")", ch2, "(", c, ch3, d, ")"};
                if(evalRPN(parseToSuffixExpression(expressionList)) == 24) {
                    ret.add(Arrays.toString(expressionList).replaceAll("(\\[|\\]|,| )", "") + "=24");
                }
                // A((BC)D)
                expressionList = new String[]{a, ch1, "(", "(", b, ch2, c, ")", ch3, d, ")"};
                if(evalRPN(parseToSuffixExpression(expressionList)) == 24) {
                    ret.add(Arrays.toString(expressionList).replaceAll("(\\[|\\]|,| )", "") + "=24");
                }
                // A(B(CD))
                expressionList = new String[]{ a, ch1, "(", b, ch2, "(", c, ch3, d, ")", ")"};
                if(evalRPN(parseToSuffixExpression(expressionList)) == 24) {
                    ret.add(Arrays.toString(expressionList).replaceAll("(\\[|\\]|,| )", "") + "=24");
                }
            }
        }
        return ret;
    }

    // 中缀表达式转为后缀表达式
    private static List<String> parseToSuffixExpression(String[] expressionList) {
        //创建一个栈用于保存操作符
        Stack<String> opStack = new Stack<>();
        //创建一个list用于保存后缀表达式
        List<String> suffixList = new ArrayList<>();
        for(String item : expressionList){
            //得到数或操作符
            if(isOperator(item)){
                //是操作符 判断操作符栈是否为空
                if(opStack.isEmpty() || "(".equals(opStack.peek()) || priority(item) > priority(opStack.peek())){
                    //为空或者栈顶元素为左括号或者当前操作符大于栈顶操作符直接压栈
                    opStack.push(item);
                }else {
                    //否则将栈中元素出栈如队，直到遇到大于当前操作符或者遇到左括号时
                    while (!opStack.isEmpty() && !"(".equals(opStack.peek())){
                        if(priority(item) <= priority(opStack.peek())){
                            suffixList.add(opStack.pop());
                        }
                    }
                    //当前操作符压栈
                    opStack.push(item);
                }
            }else if(isNumber(item)){
                //是数字则直接入队
                suffixList.add(item);
            }else if("(".equals(item)){
                //是左括号，压栈
                opStack.push(item);
            }else if(")".equals(item)){
                //是右括号 ，将栈中元素弹出入队，直到遇到左括号，左括号出栈，但不入队
                while (!opStack.isEmpty()){
                    if("(".equals(opStack.peek())){
                        opStack.pop();
                        break;
                    }else {
                        suffixList.add(opStack.pop());
                    }
                }
            }else {
                throw new RuntimeException("有非法字符！");
            }
        }
        //循环完毕，如果操作符栈中元素不为空，将栈中元素出栈入队
        while (!opStack.isEmpty()){
            suffixList.add(opStack.pop());
        }
        return suffixList;
    }
    // 计算后缀表达式的值
    private static double evalRPN(List<String> tokens) {
        Deque<Double> stack = new LinkedList<>();
        int n = tokens.size();
        for (int i = 0; i < n; i++) {
            String token = tokens.get(i);
            if (isNumber(token)) {
                stack.push((double)Integer.parseInt(token));
            } else {
                double num2 = stack.pop();
                double num1 = stack.pop();
                switch (token) {
                    case "+":
                        stack.push(num1 + num2);
                        break;
                    case "-":
                        stack.push(num1 - num2);
                        break;
                    case "*":
                        stack.push(num1 * num2);
                        break;
                    case "/":
                        stack.push(num1 / num2);
                        break;
                    default:
                }
            }
        }
        return stack.pop();
    }


    public static boolean isOperator(String op){
        return op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/");
    }

    public static boolean isNumber(String num){
        return num.matches("\\d+");
    }

    public static int priority(String op){
        if(op.equals("*") || op.equals("/")){
            return 1;
        }else if(op.equals("+") || op.equals("-")){
            return 0;
        }
        return -1;
    }

}