package tw.idv.ctfan.RoughSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class RoughSet {
	private static boolean debug = true;
	
	private final int m_numOfCondAttr;
	
	public class Element {
		public long[] attribute;
		public long decision;
		
		public Element(long[] attributeValue, long decisionValue) {
			attribute = attributeValue.clone();
			decision = decisionValue;
		}
	}
	
	private ArrayList<Element> m_elements;
	
	public RoughSet(int numberOfConditionAttribute) {
		this.m_numOfCondAttr = numberOfConditionAttribute;
		m_elements = new ArrayList<Element>();
	}
	
	public boolean AddElement(Element element) {
		if(element.attribute.length!=m_numOfCondAttr) {
			return true;
		}
		m_elements.add(element);
		m_calculated = false;
		return false;
	}
	
	private boolean m_calculated = false;
	
	
	public void Calculate() {
		if(m_calculated) return;
		
		ComputeDiscernibilityMatrix();
		ShowDNF();
		TranslateDNF_To_CNF();
		ShowCNF();
		
		CalculateImplicationTable();
		ShowImplicationTable();
		
		ShowImplicationList();
		
		ReduceImplicationList();
		
		ShowReducedPrimeImplicants();
		
		m_calculated = true;
	}
	
	private ArrayList<boolean[]> m_discernibilityFunc_DNF;
	
	private void ComputeDiscernibilityMatrix(){
		m_discernibilityFunc_DNF = new ArrayList<boolean[]>();
		for(int i=0; i<m_elements.size(); i++) {
			Element e1 = m_elements.get(i);
			for(int j=i+1; j<m_elements.size(); j++) {
				Element e2 = m_elements.get(j);
				if(e1.decision == e2.decision) {
					continue;
				}
				else {
					boolean[] diff = new boolean[m_numOfCondAttr];
					for(int k=0; k<m_numOfCondAttr; k++) {
						if(e1.attribute[k]==e2.attribute[k]) {
							diff[k] = false;
						} else {
							diff[k] = true;
						}
					}
					AddDNF(diff);
				}
			}
		}
	}	
	
	private void AddDNF(boolean[] term) {
		boolean found = false;
		for(boolean[] exist:m_discernibilityFunc_DNF) {
			found = false;
			for(int i=0; i<m_numOfCondAttr; i++) {
				if(term[i]!=exist[i]) {
					found = false;
					break;
				} else {
					found = true;
				}
			}
			if(found) break;
		}
		if(!found) m_discernibilityFunc_DNF.add(term);
	}
	
	private ArrayList<boolean[]> m_discernibilityFunc_CNF;
	private boolean[] tmpTerm;
	
	private void TranslateDNF_To_CNF() {
		m_discernibilityFunc_CNF = new ArrayList<boolean[]>();
		tmpTerm = new boolean[m_numOfCondAttr];
		
		Recurrsive_TranslateDNF_To_CNF(0);
	}
	
	private void Recurrsive_TranslateDNF_To_CNF(int level) {
		if(level==m_discernibilityFunc_DNF.size()) {
			AddCNF(tmpTerm.clone());
		} else {
			boolean tmpValue;
			boolean[] term = m_discernibilityFunc_DNF.get(level);
			for(int i=0; i<m_numOfCondAttr; i++) {
				if(term[i]) {
					tmpValue = tmpTerm[i];
					tmpTerm[i] = true;
					Recurrsive_TranslateDNF_To_CNF(level+1);
					tmpTerm[i] = tmpValue;
				}
			}
		}
	}
	
	private void AddCNF(boolean[] term) {
		boolean found = false;
		for(boolean[] exist:m_discernibilityFunc_CNF) {
			found = false;
			for(int i=0; i<m_numOfCondAttr; i++) {
				if(term[i]!=exist[i]) {
					found = false;
					break;
				} else {
					found = true;
				}
			}
			if(found) break;
		}
		if(!found) m_discernibilityFunc_CNF.add(term);
	}
	
	private void ShowDNF() {
		if(!debug) return;
		for(boolean[] term:m_discernibilityFunc_DNF) {
			boolean first = true;
			System.out.print("*(");
			for(int i=0; i<term.length; i++) {
				boolean b = term[i];
				if(b) {
					System.out.print( (first?"":"+") + i );
					first=false;
				}
			}
			System.out.println(")");
		}
	}
	
	private void ShowCNF() {
		if(!debug) return;
		for(boolean[] term:m_discernibilityFunc_CNF) {
			boolean first = true;
			System.out.print("+(");
			for(int i=0; i<term.length; i++) {
				boolean b = term[i];
				if(b) {
					System.out.print( (first?"":"*") + i );
					first=false;
				}
			}
			System.out.println(")");
		}
	}
	

	private class DCTerm implements Comparable<DCTerm> {
		public boolean[] term;
		public boolean[] dontCare;
		
		public DCTerm() {
			this.term = new boolean[m_numOfCondAttr];
			this.dontCare = new boolean[m_numOfCondAttr];
			Arrays.fill(term, false);
			Arrays.fill(dontCare, false);
		}
		
		public DCTerm(boolean[] t) {
			term = t;
			dontCare = new boolean[m_numOfCondAttr];
			Arrays.fill(dontCare, Boolean.FALSE);
		}

		@Override
		public int compareTo(DCTerm cmp) {
			return (NumOfOnes()-cmp.NumOfOnes());
		}
		
		public String toString() {
			String s = "";
			for(int i=0; i<m_numOfCondAttr; i++) {
				if(dontCare[i]) s += "x";
				else {
					s += (term[i]?"1":"0");
				}
			}
			s += " (" + NumOfOnes() + ")";
			return s;
		}
		
		public int NumOfOnes() {
			int count = 0;
			for(int i=0; i<m_numOfCondAttr; i++) {
				if(!dontCare[i]&&term[i]) count++;
			}
			return count;
		}
		
		public DCTerm Combine(DCTerm t2) {
			int diff = 0;
			DCTerm result = new DCTerm();
			for(int i=0; i<m_numOfCondAttr; i++) {
				if(dontCare[i]&&t2.dontCare[i]) {
					result.dontCare[i] = true;
				} else if(dontCare[i]==t2.dontCare[i]) {
					if(term[i]==t2.term[i]) {
						result.term[i] = term[i];
					} else {
						if(diff<1) {
							result.dontCare[i] = true;
							diff++;
						} else {
							return null;
						}
					}
				} else {
					return null;
				}
			}
//			System.out.println(this);
//			System.out.println(t2);
//			System.out.println(result);
			return result;
		}
		
		public boolean Contains(DCTerm t2) {
			for(int i=0; i<m_numOfCondAttr; i++) {
				if(dontCare[i]) continue;
				if(t2.dontCare[i]) return false;
				if(term[i]==t2.term[i]) continue;
				return false;
			}			
			return true;
		}
	}
	
	private ArrayList<ArrayList<DCTerm>> m_implicationTable;
	private ArrayList<DCTerm> m_primeImplicants;
	
	private void CalculateImplicationTable() {
		m_implicationTable = new ArrayList<ArrayList<DCTerm>>();
		m_primeImplicants = new ArrayList<DCTerm>();
		for(int table=0; table<=m_numOfCondAttr+1; table++) {
//			System.out.println("Table " + table + "=================");
			if(table!=0) {
				ArrayList<DCTerm> column1 = m_implicationTable.get(table-1);
				if(column1.isEmpty()) return;
				
				ArrayList<DCTerm> column2 = new ArrayList<DCTerm>();
				
				boolean[] checked = new boolean[column1.size()];
				Arrays.fill(checked, false);
				
				
				for(int upper=0; upper<column1.size(); upper++) {
					int upperOnes = column1.get(upper).NumOfOnes();
					for(int lower=upper+1; lower<column1.size(); lower++) {
						int lowerOnes = column1.get(lower).NumOfOnes();
//						System.out.println("Try to combine:" + column1.get(upper) + " " + column1.get(lower));
						if( (lowerOnes-upperOnes)==0 ) continue;
						if( (lowerOnes-upperOnes)>=2 ) break;
						
						DCTerm result = column1.get(upper).Combine(column1.get(lower));
						if(result!=null) {
							checked[upper] = checked[lower] = true;
							//column2.add(result);
							AddToColumn(result, column2);
						}
					}
				}
					
				for(int i=0; i<checked.length; i++) {
					if(!checked[i]) {
						m_primeImplicants.add(column1.get(i));
					}
				}
					
				Collections.sort(column2);
				m_implicationTable.add(column2);
				
			} else {
				ArrayList<DCTerm> column = new ArrayList<DCTerm>();
				
				for(boolean[] term:m_discernibilityFunc_CNF) {
					column.add(new DCTerm(term));
				}
				Collections.sort(column);
				m_implicationTable.add(column);
			}
		}
	}
	
	private void AddToColumn(DCTerm term, ArrayList<DCTerm> Column) {
		for(DCTerm t:Column) {
//			System.out.println("AddToColumn " + term + ":" + t);
			boolean diff = false;
			for(int i=0; i<m_numOfCondAttr; i++) {
				if(t.dontCare[i] != term.dontCare[i]){
					diff = true;
					break;
				}
				if(!t.dontCare[i] && t.term[i]!=term.term[i]) {
					diff = true;
					break;
				}
			}
			if(!diff) return;
		}
//		System.out.println("Add " + term + " To Column");
		Column.add(term);
	}
	
	private void ShowImplicationTable() {
		if(!debug) return;
		System.out.println("Implication Table==========");
		int c = 0;
		for(ArrayList<DCTerm> column : m_implicationTable) {
			System.out.println("Column " + c++);
			for(DCTerm t:column) {
				System.out.println(t);
			}
			System.out.println("======\n");
		}
	}
	
	private void ShowImplicationList() {
		if(!debug) return;
		System.out.println("Prime Implicants List==========");
		for(DCTerm t:m_primeImplicants) {
			System.out.println(t);
		}
		System.out.println("======\n");
	}
	
	ArrayList<DCTerm> m_reducedPrimeImplicants;
	
	private void ReduceImplicationList() {
		m_reducedPrimeImplicants = new ArrayList<DCTerm>();
		
		ArrayList<DCTerm> originalList = m_implicationTable.get(0);
		
		int colSize = originalList.size();
		int rowSize = m_primeImplicants.size();
		
		boolean[][] coverageTable = new boolean[rowSize][colSize];
		int[] colCount = new int[colSize];
		int[] rowCount = new int[rowSize];
		Arrays.fill(colCount, 0);
		Arrays.fill(rowCount, 0);
		
		for(int col=0; col<colSize; col++) {
			DCTerm colTerm = originalList.get(col);
			for(int row=0; row<rowSize; row++) {
				DCTerm rowTerm = m_primeImplicants.get(row);				
				coverageTable[row][col] = rowTerm.Contains(colTerm);
				if(coverageTable[row][col]) {
					colCount[col]++;
					rowCount[row]++;
				}
			}
		}
		
		PrintCoverageTable(originalList, m_primeImplicants, coverageTable);
		
		boolean[] colChecked = new boolean[colSize];
		boolean[] rowChecked = new boolean[rowSize];
		Arrays.fill(colChecked, false);
		Arrays.fill(rowChecked, false);
		
		for(int col=0; col<colSize; col++) {
			if(colCount[col] == 1) {
				int row;
				for(row=0; row<rowSize; row++) {
					if(coverageTable[row][col]) break;
				}
				AddToColumn(m_primeImplicants.get(row), m_reducedPrimeImplicants);
				rowChecked[row] = true;
			}
		}
		
		for(int row=0; row<rowSize; row++) {
			if(rowChecked[row]) {
				for(int col=0; col<colSize; col++) {
					if(coverageTable[row][col]) {
						colChecked[col] = true;
						for(int row2=0; row2<rowSize; row2++) {
							if(coverageTable[row2][col]) {
								coverageTable[row2][col] = false;
								rowCount[row2]--;
							}
						}
					}
				}
			}
		}
		

		PrintCoverageTable(originalList, m_primeImplicants, coverageTable);
		
		while(true) {
			boolean pass = true;
			for(boolean b:colChecked) {
				pass &= b;
			}
			if(pass) break;
			
			int largestRow = -1;
			for(int row=0; row<rowSize; row++) {
				if(rowChecked[row]) continue;
				if(largestRow!=-1) {
					largestRow = (rowCount[largestRow]<rowCount[row]?row:largestRow);
				} else {
					largestRow = row;
				}				
			}
			if(largestRow == -1) {
				System.err.println("Something missing here");
			}
			
			rowChecked[largestRow] = true;
			this.m_reducedPrimeImplicants.add(m_primeImplicants.get(largestRow));
			for(int col =0; col<colSize; col++) {
				if(coverageTable[largestRow][col]) {
					colChecked[col] = true;
					for(int row2=0; row2<rowSize; row2++) {
						if(coverageTable[row2][col]) {
							coverageTable[row2][col] = false;
							rowCount[row2]--;
						}
					}
				}
			}
			
		}
		
		PrintCoverageTable(originalList, m_primeImplicants, coverageTable);
		
	}
	
	private void PrintCoverageTable(ArrayList<DCTerm> colList, ArrayList<DCTerm> rowList, boolean[][] table) {
		if(!debug) return;	

		for(DCTerm t:colList) {
			System.out.print(t + "|");
		}
		System.out.println();
		for(int row=0; row<table.length; row++) {
			for(boolean col:table[row])
				System.out.print(col?"X":" ");
			System.out.println(" " + rowList.get(row));
		}
	}
	
	private void ShowReducedPrimeImplicants() {
		if(!debug) return;

		System.out.println("Reduced Prime Implicants List==========");
		for(DCTerm t:this.m_reducedPrimeImplicants) {
			System.out.println(t);
		}
		System.out.println("======\n");
	}
	
	public boolean[][] GetReductAttribute(){
		ArrayList<boolean[]> allResult = new ArrayList<boolean[]>();
		boolean[] result = new boolean[m_numOfCondAttr];
		
		for(DCTerm term:m_reducedPrimeImplicants) {
			for(int i=0; i<m_numOfCondAttr; i++) {
				result[i] = !term.dontCare[i];
			}
			allResult.add(result.clone());
		}
		boolean[][] r = new boolean[allResult.size()][];
		for(int i=0; i<allResult.size(); i++)
			r[i] = allResult.get(i);
		return r;
	}
	
	public void ShowReductAttribute() {
		boolean[][] allResult = GetReductAttribute();
		if(allResult == null) return;
		
		System.out.println("============");
		System.out.println("All Result:");
		
		for(int i=0; i<allResult.length; i++){
			System.out.print("" + i + ":[");
			for(int j=0; j<allResult[i].length; j++) {
				if(allResult[i][j])
					System.out.print(" " + j);
			}
			System.out.println("]");
		}
		
	}
	
	public void ShowCoreAttribute() {
		boolean[] core = GetCoreAttribute();
		if(core == null) return;

		System.out.print("Attribute");
		for(int i=0; i<core.length; i++) {
			if(core[i])
				System.out.print(" " + i);
		}
		System.out.println(" are Core Attributes");
	}
	
	public boolean[] GetCoreAttribute() {
		if(!m_calculated) return null;
		
		boolean[] result = new boolean[m_numOfCondAttr];
		
		int[] count = new int[m_numOfCondAttr];
		Arrays.fill(count, 0);
		for(DCTerm term:m_reducedPrimeImplicants){
			for(int i=0; i<m_numOfCondAttr; i++) {
				if(!term.dontCare[i] &&
						term.term[i])
					count[i]++;
			}
		}
		
		for(int i=0; i<m_numOfCondAttr; i++) {
			result[i] = (count[i] == m_reducedPrimeImplicants.size());
		}
		
		return result;
	}
	
	public long GetDecision(Element e){
		return 0;
	}
}

