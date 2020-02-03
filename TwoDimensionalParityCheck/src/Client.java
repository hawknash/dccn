import java.net.*;
import java.util.*;
import java.io.*; 
public class Client 
{ 
	private Socket socket = null; 
	private DataInputStream input = null; 
	private DataOutputStream out = null;
	public Client(String address, int port) 
	{
		try
		{ 
			socket = new Socket(address, port); 
			System.out.println("Connected");
			input = new DataInputStream(System.in);
			out = new DataOutputStream(socket.getOutputStream()); 
		} 
		catch(Exception u) 
		{ 
			System.out.println(u); 
		} 
		String line = ""; 
		System.out.println("enter the strings \"Over\" to end:");
		while (!line.equals("Over")) 
		{ 
			try
			{ 
				line = input.readLine();
				if(line.length()==0)
					continue;
				String d=prepare(line);
				System.out.println("\tsending:"+line);
				d=generateSingleBitError(d);
				out.writeUTF(d); 
			} 
			catch(IOException i) 
			{ 
				System.out.println(i); 
			} 
		} 
		try
		{ 
			input.close(); 
			out.close(); 
			socket.close(); 
		} 
		catch(IOException i) 
		{ 
			System.out.println(i); 
		} 
	}
	String generateSingleBitError(String s) {
		int l=s.length();
		Random r=new Random();
		int bit=r.nextInt(2);
		int pos=r.nextInt(l-1);
		System.out.println("generated bit:"+bit+"  pos:"+pos+" actual bit:"+s.charAt(pos));
		System.out.println("before:"+s);
		s=s.substring(0,pos-1)+bit+s.substring(pos,l);
		System.out.println("after:"+s);
		return s;
	}
	static int getParity(ArrayList<Integer> alist) {
		int count=0;
		for(int t=0;t<alist.size();t++ ) {
			if(alist.get(t)==1)
				count++;
		}
		if(count%2==0)
			return 0;
		else
			return 1;
	}
	static String prepare(String data) {
		String s="";
		ArrayList<ArrayList<Integer>> datamatrix=new ArrayList<ArrayList<Integer>>();
		for(int i=0;i<data.length();i++) {
			ArrayList<Integer> d=toBinary(data.charAt(i));
			int p=getParity(d);
			d.add(p);
			datamatrix.add(d);
		}
		int rows=datamatrix.size();
		int cols=datamatrix.get(0).size();
		ArrayList<Integer> colpar=new ArrayList<Integer>();
		for(int i=0;i<cols;i++) {
			int count=0;
			for(int j=0;j<rows;j++) {
				if(datamatrix.get(j).get(i)==1)
					count++;
			}
			if(count%2==0) {
				colpar.add(0);
			}
			else {
				colpar.add(1);
			}
		}
		datamatrix.add(colpar);
		for(int i=0;i<datamatrix.size();i++) {
			for(int j=0;j<datamatrix.get(i).size();j++) {
				s+=datamatrix.get(i).get(j)+"";
			}
		}
		//printDataMatrix(datamatrix);
		return s;
	}
	static void printDataMatrix(ArrayList<ArrayList<Integer>> datamatrix) {
		for(int i=0;i<datamatrix.size();i++) {
			for(int j=0;j<datamatrix.get(i).size();j++) {
				System.out.print(datamatrix.get(i).get(j)+" ");
			}
			System.out.println();
		}
	}
	public static ArrayList<Integer> toBinary(int data){
		int temp=data;
        ArrayList<Integer> bindata=new ArrayList<>();
        while(temp!=0){
                bindata.add(temp%2);
                temp/=2;
        }
        while(bindata.size()!=8) {
        	bindata.add(0);
        }
        Collections.reverse(bindata);
        return bindata;
	}
	public static void main(String args[]) 
	{ 
		Client client = new Client("127.0.0.1", 5000); 
	} 
} 
