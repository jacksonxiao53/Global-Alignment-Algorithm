/*
 * Jackson Xiao 
 */
import java.io.*;
import java.util.*;

public class GlobalAlignment {
	private String x;
	private String y;
	private int rowLen;
	private int colLen;
	private int gap;
	private int mismatch;
	private int match;
	private MatrixObject[][] scoreArray;
	public ArrayList<String> topAlignment;
	public ArrayList<String> bottomAlignment;
	public GlobalAlignment(String a1, String a2, int gap, int mismatch, int match) {
		x = a1;
		y = a2;
		rowLen = y.length();
		colLen = x.length();
		this.gap = gap;
		this.mismatch = mismatch;
		this.match = match;
		scoreArray = new MatrixObject[y.length()+1][x.length()+1];
		topAlignment = new ArrayList<>();
		bottomAlignment = new ArrayList<>();
	}
	
	public void fillMatrix() {
		int diagonal, left, up;
		int max;
		
		for (int col = 0; col <= colLen; col++) {
			int val = gap * col;
			MatrixObject obj = new MatrixObject(val);
			scoreArray[0][col] = obj;
			if(col != 0) {
				obj.prev.add("left");
			}
		}
		for(int row = 0; row <= rowLen; row++) {
			int val = gap * row;
			MatrixObject obj = new MatrixObject(val);
			scoreArray[row][0] = obj;
			if(row != 0) {
				obj.prev.add("up");
			}
		}
	
		
		for(int row = 1; row <= rowLen;row++) {
			for(int col = 1; col <= colLen; col++) {
				if(x.charAt(col-1) == y.charAt(row-1)) {
					diagonal = scoreArray[row-1][col-1].val + match;
				}
				else {
					diagonal = scoreArray[row-1][col-1].val +mismatch;
				}
				left = scoreArray[row][col-1].val + gap;
				up = scoreArray[row-1][col].val + gap;
				max = diagonal;
				if(up > diagonal) max = up;
				if(left > diagonal) max = left;
				MatrixObject obj = new MatrixObject(max);
				scoreArray[row][col] = obj;
				if(up == max) obj.prev.add("up");
				if(left == max) obj.prev.add("left");
				if(diagonal == max) obj.prev.add("diagonal");
			}
		}
	}
	
	public void printMatrix() {
		for(int i = 0; i<scoreArray.length;i++) {
			for(int j = 0; j<scoreArray[i].length;j++) {
				System.out.print(scoreArray[i][j].prev);
			}
			System.out.println();
		}
	}
	
	public void findAlignments(int row, int col, String a1, String a2, String prevDirection) {
		if(row == 0 && col == 0) {
			topAlignment.add(a1);
			bottomAlignment.add(a2);
		}
		else {
			ArrayList<String> prev = scoreArray[row][col].prev;
			for(String p : prev) {
				if(p.equals("diagonal")) {
					findAlignments(row-1,col-1,x.charAt(col-1)+a1,y.charAt(row-1)+a2,"diagonal");
				}
				else if(p.equals("up")) {
					findAlignments(row-1,col,' '+a1, y.charAt(row-1)+a2,"up");
				}
				else if(p.equals("left")) {
					findAlignments(row,col-1,x.charAt(col-1)+a1,' '+a2,"left");
				}
			}
		}
	}
	public void printAlignments() {
		int count = 1;
		System.out.println("The maximum score is: " + getScore());
		for(int i = 0; i<topAlignment.size();i++) {
			String top = topAlignment.get(i);
			String bottom = bottomAlignment.get(i);
			System.out.println("Alignment " + count + "----------------------");
			System.out.println(top);
			for(int j = 0; j< top.length();j++) {
				char c1 = top.charAt(j);
				char c2 = bottom.charAt(j);
				if(c1 == c2) {
					System.out.print('|');
				}
				else if(c1 == ' ' || c2 == ' ' ) {
					System.out.print('-');
				}
				else if (c1 != c2) {
					System.out.print(';');
				}
			}
			System.out.println();
			System.out.println(bottom);
			count++;
		}
	}
	public int getRowLen() {
		return rowLen;
	}
	public int getColLen() {
		return colLen;
	}
	public int getScore() {
		return scoreArray[rowLen][colLen].val;
	}
	
	public static List<String> readFile(String filename) {        
        List <String> sequenceList = new ArrayList <String> ();        
        try {            
            BufferedReader reader = new BufferedReader(new FileReader(filename));            
            String line = reader.readLine();
            StringBuilder sb = new StringBuilder();
            while (!(line = reader.readLine()).equals(">SequenceName")) {    
                sb.append(line);            
            }
            sequenceList.add(sb.toString());
            
            sb.setLength(0);
            while((line = reader.readLine()) != null) {
            	sb.append(line);
            }
            sequenceList.add(sb.toString());
      
            reader.close();        
        } 
        catch (Exception e) {            
            System.err.format("Exception occurred trying to read '%s'.", filename);            
            e.printStackTrace();        
        }        
        return sequenceList;    
    }
	
	public static void main(String[] args) {
		System.out.print("Enter the name of the file: ");
		Scanner scanner = new Scanner(System.in);
        String filename = scanner.next();
        scanner.close();
        List<String> sequences = readFile(filename);
        GlobalAlignment ga = new GlobalAlignment(sequences.get(0),sequences.get(1),-2,-1,1);
        ga.fillMatrix();
        ga.findAlignments(ga.getRowLen(),ga.getColLen(),"","","");
        ga.printAlignments();

        
	}
	
	

}
