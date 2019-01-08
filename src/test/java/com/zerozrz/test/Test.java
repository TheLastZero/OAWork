package com.zerozrz.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {

	/**
	 * +-* () / 四则运算包含加减乘除，括号这几种情况
	 * 括号可能有一个或者多个
	 * 括号内可能还包含着括号
	 */
	@org.junit.Test
	public void test() {
		String str = "(1+2)+1.2+2*3.5/((2+8)-(1+1+(2+3)))+(5+6)";
		
		char [] str2 =str.toCharArray();
		
		String [] s1 = str.split("\\)");
		
		System.out.println(Arrays.asList(s1));
		
		
		//String s = getlr(str2, 0);
		//System.out.println(s);
		
//		char l = '(';
//		char r = ')';
//		
//		System.out.println(str2[0]==l);
//		
//		int start=-1;
//		
//		String result1 = "";
//		
//		for(int i=0;i<str2.length;i++) {
//			
//			if(str2[i] == l) {//找到左括号
//				start = i;
//				
//				//从这个下标开始找右括号
//				
//			}else {
//				result1 += str2[i];
//			}
//			
//		}
		
	}
	
	/**
	 * 计算左括号与右括号之间的下标，只要有右括号就计算上一个左括号到现在下标这一段字符串加减乘除的值
	 * @param str
	 * @param start
	 * @return
	 */
	String res ="";
	int dg = 0;//记录递归层数
	public String getlr(char [] str,int start) {
		
		
		char l = '(';
		char r = ')';
		
		List<Integer> lList = new ArrayList<Integer>();//0第一层，值为下标
		
		int ll=0;//用来记录当前左括号的下标
		int ceng=-1;//用来记录当前左括号为第几层
		
		boolean f = true;//能否添加字符串
		String res = "";
		String res2 = "";
		
		
		for(int i=start;i<str.length;i++) {
			
			if(str[i]==l) {
				//System.out.println("左括号数量");
				ceng++;
				
				f=false;
				i++;
				
				lList.add(i);//将当前左括号下标放进去
			}
			
			if(str[i]==r) {
				//System.out.println("右括号数量");
				f=true;
				
				int j = lList.get(ceng);//根据下标取对应层数，加一从左括号后面开始取值
				
				String result = "";
				for(;j<i;j++) {
					result += str[j];
				}
				System.out.println(result);
				
				
				ceng--;
			}
			
			if(f==true) {
				res += str[i];
			}	
//			}else {
//				res2 += str[i];
//			}
			
		}
			
		//System.out.println("res2："+res2);
		return res;
	}
	
	
	public String testgetResult(String str) {//String str
		
		int start = -1;
		
		char [] str2 = str.toCharArray();
		double c= 1;
		int i=0;
		if(str2[0]=='-') {//第一个为-号
			c = -1;//线面所有左边的参数传参时都要*c
			i=1;
		}
		for(;i<str2.length;i++) {
			
			if((str2[i] == '+' || str2[i] == '-' || str2[i] == '*' || str2[i] == '/' ) && str2[i] != '.' ) {//寻找第一个加减乘除符号
				
				if(start!=-1) {//表示这已经是第二次找到符号了
					//要进行运算的左边的数字是
					String lnum="";
					
					for(int l =0;l<start;l++) {
						lnum += str2[l];
					}
					
					//要进行运算的右边的数字是
					String rnum="";
					for(int ri =start+1;ri<i;ri++) {
						rnum += str2[ri];
					}
					
//					System.out.println("左边的数字是："+lnum);
//					System.out.println("右边的数字是："+rnum);
					
					String s;
					
					if((str2[i]=='*' || str2[i]=='/') && (str2[start]!='*' && str2[start]!='/')) {//如果第二个符号是乘号或者除号,并且第一个符号不为乘除号的时候
						System.out.println("右边的数字后面是乘除号");
						String startStr = "";
						String remStr = "";
						
						for(int rem = 0;rem<str2.length;rem++) {
							if(rem<=start) {
								startStr += str2[rem];
							}else {
								remStr += str2[rem];
							}
							
						}
						
						
//						System.out.println("后面为:"+remStr);
						s = startStr + testgetResult(remStr);
						
					}else {
						String remStr = "";
						for(int rem = i;rem<str2.length;rem++) {
							remStr += str2[rem];
						}
						
						s =getjjcc(Double.parseDouble(lnum), Double.parseDouble(rnum), str2[start]+"")+remStr;
					}
					
					
//					System.out.println("结果是："+s);
					return testgetResult(s);
					
				}else {//第一次找到符号
					start = i;
				}
				
				
			}
			
		}
		
		if(start==-1) {//表示一个加减乘除符号都没有
			return str;
		}else {//循环最后一次，表示只有一次加减乘除符号
			//要进行运算的左边的数字是
			String lnum="";
			for(int l =0;l<start;l++) {
				lnum += str2[l];
			}
			
			//要进行运算的右边的数字是
			String rnum="";
			for(int ri =start+1;ri<str2.length;ri++) {
				rnum += str2[ri];
			}
			
//			System.out.println("左边的数字是："+lnum);
//			System.out.println("右边的数字是："+rnum);
			
			return getjjcc(Double.parseDouble(lnum), Double.parseDouble(rnum), str2[start]+"");
			
		}
			
		
	}
	
	public String getjjcc(double l,double r,String f) {
		
//		if(l<0 && r>0) {
//			double i = 0;
//			i = l;
//			l=r;
//			r=i;
//		}
			
		
		if(f.equals("+")) {
			return (l+r)+"";
		}else if(f.equals("-")) {
			return (l-r)+"";
		}else if(f.equals("*")) {
			return (l*r)+"";
		}else if(f.equals("/")) {
			return (l/r)+"";
		}
		
		return "";
	}
	
//	@org.junit.Test
	public String test2(String str) {
		//String str = "1+5+2-3*10/2.5+10*10";
//		String str = "1+1/5-1*5";
		System.out.println("取得的字符串为："+str);
		
		String sResult="";
		if(str.indexOf("-")!=-1) {//表示有-号
			String [] str2 = str.split("\\+");//根据+号分割
			for(String str3 : str2) {
				sResult += testgetResult(str3)+"+0";
			}
			
			
		}else {
			sResult =testgetResult(str);
		}
		
		sResult = testgetResult(sResult);
		
		
		System.out.println("最终的结果为："+sResult);
		
		return sResult;
	}
	
	
	@org.junit.Test
	public void test3() {
		
		double a = -4.0;
		double b = 0100;
		System.out.println(a+b);
	}

	
	@org.junit.Test
	public void test4() {
		
		Integer a  = new Integer(126);
		Integer b  = new Integer(126);
		System.out.println(a==b);
	}
	
}
