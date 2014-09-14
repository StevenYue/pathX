package Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

import org.w3c.dom.Document;

import pathX.data.Intersect;
import xml_utilities.InvalidXMLFileFormatException;
import xml_utilities.XMLUtilities;

public class XMLTest {
	public static void main(String[] args) {
		ArrayList<Integer> a= new ArrayList<Integer>();
		a.add(1);
		a.add(2);
		a.add(3);
		
		a.add(0, 0);
		
		for(Integer i:a){
			System.out.println(i);
		}
		
		
//		Integer[][] a = {{12,3},{33,1},{99,0},{77,8}};
//		Arrays.sort(a, new Comparator<Integer[]>() {
//			public int compare(Integer[] o1, Integer[] o2) {
//				 return Integer.compare(o2[0], o1[0]);
//			}
//		});
//		for(int i=0;i<a.length;i++)
//			for(int j=0;j<a[0].length;j++)
//				System.out.println(a[i][j]);
//		
//		Random r = new Random();
//		System.out.println(r.nextInt(3));
//		XMLUtilities xm = new XMLUtilities();
//		try {
//			File f1 = new File("./data/pathX/LevelFiles/Level-Benfica.xml");
//			File f2 = new File("./data/PathXLevelSchema.xsd");
//			System.out.println(f1.getAbsolutePath());
//			System.out.println(f2.getAbsolutePath());
//			Document doc = xm.loadXMLDocument(f1.getAbsolutePath(), f2.getAbsolutePath());
//			System.out.println("aaa");
//		} catch (InvalidXMLFileFormatException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
