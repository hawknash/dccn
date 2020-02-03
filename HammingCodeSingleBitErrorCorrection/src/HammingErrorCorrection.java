import java.util.*;
public class HammingErrorCorrection {
	public int getHammingBit(int m){
		int r=0;
		while(Math.pow(2,r)<(m+r+1)){
			r++;
		}
		return r;
	}
	ArrayList<Integer> getBinaryEqui(int d){
		int temp=d;
		ArrayList<Integer> bindata=new ArrayList<Integer>();
		while(temp!=0){
			bindata.add(temp%2);
			temp/=2;
		}
		Collections.reverse(bindata);
		return bindata;
	}
	String prepareData(ArrayList<Integer> act_data,int m,int r) {
		//System.out.println(m+"<-m  "+r+"<-r  "+act_data.size()+"<-as");
		for(int i=0;i<r;i++) {
			int pos=(int)Math.pow(2, i)-1;
			act_data.add(pos,0);
		}
		int tl=(int)Math.pow(2, r);
		while(tl>act_data.size()) {
			act_data.add(0);
		}
		int arr[]=new int[tl];
		for(int i=0;i<tl;i++) {
			arr[i]=i+1;
		}
		String s="";
		for(int i=0;i<act_data.size();i++) {
			s+=act_data.get(i)+"";
		}
		//System.out.println("str1:"+s);
		for(int i=0;i<r;i++) {
			int count=0;
			int pos=(int)Math.pow(2, i)-1;
			for(int j=0;j<tl;j++) {
				//System.out.println("-----"+arr[j]);
				if(arr[j]%2==1) {
					//System.out.println("--     ---"+arr[j]);
					if(act_data.get(j)==1)
						count++;
				}
					
				arr[j]/=2;
			}
			//System.out.println("count:"+count);
			if(count%2==1) {
				act_data.set(pos, 1);
			}
		}
		//System.out.println("wh->"+act_data.size());
		s="";
		for(int i=0;i<act_data.size();i++) {
			s+=act_data.get(i)+"";
		}
		//System.out.println("str2:"+s);
		return s;
	}
	public static void main(String[] args) {
//		int data=13;
//		HammingErrorCorrection hec=new HammingErrorCorrection();
//		ArrayList<Integer> bindata=hec.getBinaryEqui(data);
//		int m=bindata.size();
//		int r=hec.getHammingBit(m); 
//		//System.out.println();
//		int act_data_len = m+r;
//		String sent_data=hec.prepareData(bindata, m, r);
//		System.out.println("Hamming code:"+sent_data);	
//		hec.checkCorrectness(sent_data);
		
	}
	void checkCorrectness(String str) {
		int r=3;
		int m=4;
		int tl=(int)Math.pow(2, r);
		int arr[]=new int[tl];
		for(int i=0;i<tl;i++) {
			arr[i]=i+1;
		}
		ArrayList<Integer> errs=new ArrayList<Integer>();
		String s="";
		//System.out.println(str);
		for(int i=0;i<r;i++) {
			int count=0;
			for(int j=0;j<tl;j++) {
				if(arr[j]%2==0) {
					//System.out.println(str.charAt(j)+"<--");
					if(str.charAt(j)=='1') {
						count++;
					}
				}
				//System.out.println("arr:"+arr[j]);
				arr[j]/=2;
			}
			//System.out.println("count--->"+count);
			if(count%2==0) {
				errs.add(0);
			}
			else {
				errs.add(1);
			}
		}
		boolean er=false;
		for(int i=0;i<errs.size();i++) {
			//System.out.println(errs.get(i));
			if(errs.get(i)==1) {
				er=true;
			}
		}
		if(er) {
			System.out.println("wrong data received");
		}
		else {
			System.out.println("correct data is received");
		}
	}
}