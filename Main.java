import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) throws IOException {
		File file = new File("DFA.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String st;
		int statesnum = 0;
		int dim = 0;
		ArrayList<String> states = new ArrayList<String>();
		ArrayList<String> accept = new ArrayList<String>();
		ArrayList<String> key = new ArrayList<String>();
		ArrayList<String> value = new ArrayList<String>();
		
		//reading txt file
		while ((st = br.readLine()) != null) {
			String[] line = st.split("=");
			key.add(line[0]);
			value.add(line[1]);
			if (line[0].equals("Q")) {
				String[] temp = line[1].split(",");
				for (int i = 0; i < temp.length; i++) {
					states.add(temp[i]);
				}
				statesnum = states.size();
			}

			if (line[0].equals("A")) {
				String[] acceptarray = line[1].split(",");
				for (int i = 0; i < acceptarray.length; i++) {
					accept.add(acceptarray[i]);
				}

			}

		}
		
		dim = statesnum + 3;
		
		// Matrix column and row name writing
		String map[][] = new String[dim][dim];
		for (int i = 2; i < dim - 1; i++) {
			map[0][i] = states.get(i - 2);
			map[i][0] = states.get(i - 2);
		}
		map[0][1] = "S";
		map[1][0] = "S";
		map[0][dim - 1] = "A";
		map[dim - 1][0] = "A";

		//fill in the second row and columns
		for (int i = 1; i < dim; i++) {

			map[1][i] = "-";

		}
		for (int i = 1; i < dim; i++) {
			map[i][1] = "-";
		}
		// fill in accept state columns
		for (int i = 2; i < dim; i++) {
			map[dim - 1][i] = "-";
		}
		for (int i = 2; i < dim; i++) {

			map[i][dim - 1] = "-";
			for (int j = 0; j < accept.size(); j++) {
				map[i][dim - 1] = "-";

			}
		}
		//matrix filling		
		for (int i = 2; i < dim - 1; i++) {
			for (int j = 2; j < dim - 1; j++) {
				String left = map[i][0];
				String up = map[0][j];
				for (int k = 0; k < key.size(); k++) {
					String[] keys = key.get(k).split(",");
					if (keys[0].equals(left) && value.get(k).equals(up)) {
						if (map[i][j] != null) {
							String old = map[i][j];
							if (left == up) {
								old = old.replace("(", "");
								old = old.replace(")", "");
								old = old.replace("*", "");
								map[i][j] = "(" + old + "U" + keys[1] + ")*";
							} else {
								map[i][j] = old + "U" + keys[1];
							}
						} else {
							if (left == up) {
								map[i][j] = "(" + keys[1] + ")*";
							} else {
								map[i][j] = keys[1];
							}
						}
					}
				}
				if (map[i][j] == null) {
					map[i][j] = "-";
				}
			}
		}
		System.out.println(statesnum + 2 + " GNFA");
		System.out.println("S and A added.");
		
		// printing matrix
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				System.out.print(map[i][j] + " ");
			}
			System.out.println("");
		}

		// matrix update by rows and columns to be removed
		int icounter = 1;
		int icounters = 1;
		for (int i = 1; i < dim - 1; i++) {
			int jcounter = 1;
			int jcounters = 1;
			for (int j = 3; j < dim - 1; j++) {

				String temp = "";
				if (i == 1) {
					if (!(map[i][j - jcounters].equals("-"))) {
						temp += map[i][j - jcounters];
					}
					if (!(map[i + icounters][j - jcounters].equals("-"))) {
						temp += map[i + icounters][j - jcounters];
					}
					if (!(map[i + icounters][j].equals("-"))) {
						temp += map[i + icounters][j];

					}
					if (map[i][j].equals("-")) {
						map[i][j] = temp;
					}
					jcounters++;

				}
				if (i > 2 && j > 2) {
					if (!(map[i][j - jcounter].equals("-"))) {
						temp += map[i][j - jcounter];
					}
					if (!(map[i - icounter][j - jcounter].equals("-"))) {
						temp += map[i - icounter][j - jcounter];
					}
					if (!(map[i - icounter][j].equals("-"))) {
						temp += map[i - icounter][j];
					}
					if (map[i][j].equals("-")) {
						map[i][j] = temp;
					} else {
						if (map[i][j].contains("*")) {
							if (!(temp.equals(""))) {
								map[i][j] = map[i][j].replace("(", "");
								map[i][j] = map[i][j].replace(")", "");
								map[i][j] = map[i][j].replace("*", "");
								map[i][j] = "(" + map[i][j] + "U" + temp + ")*";
							} else {

								map[i][j] = map[i][j].replace("(", "");
								map[i][j] = map[i][j].replace(")", "");
								map[i][j] = map[i][j].replace("*", "");
								map[i][j] = "(" + map[i][j] + ")*";
							}
						} else {
							map[i][j] = map[i][j] + "U" + temp;
						}
						jcounter++;

					}
				}

			}
			if (i == 1) {
				icounters++;
				if (statesnum == 2) {
					for (int p = 2; p < dim; p++) {
						map[2][p] = "-";
					}
				}
			}
			if (i > 2) {
				icounter++;
			}
		}

		for (int i = 2; i < dim; i++) {
			map[2][i] = "-";
			map[i][2] = "-";
		}

		System.out.println("");
		System.out.println(statesnum + 1 + " GNFA");
		System.out.println(states.get(0) + " removed");

		// printing matrix
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				System.out.print(map[i][j] + " ");
			}
			System.out.println("");
		}

		System.out.println("");
		System.out.println(statesnum + " GNFA");

		System.out.println(states.get(1) + " removed");

		String temps = "";
		for (int i = 1; i < dim - 1; i++) {
			for (int j = 4; j < dim - 1; j++) {
				if (i == 1) {
					if (!(map[i][j - 1].equals("-"))) {
						temps += "(" + map[i][j - 1] + ")";
					}
					if (!(map[i + 2][j - 1].equals("-"))) {
						temps += "(" + map[i + 2][j - 1] + ")";
					}
					if (!(map[i + 2][j].equals("-"))) {
						temps += "(" + map[i + 2][j] + ")";
					}
					map[i][j] = "(" + map[i][j] + "U" + temps + ")";
				}
			}
		}
		String tempa = "";
		for (int i = 1; i < dim - 1; i++) {
			for (int j = dim - 1; j < dim; j++) {
				if (i == 1) {
					if (!(map[i][j - 2].equals("-"))) {
						tempa += "(" + map[i][j - 2] + ")";
					}
					if (!(map[i + 2][j - 2].equals("-"))) {
						tempa += "(" + map[i + 2][j - 2] + ")";
					}
					if (!(map[i + 2][j].equals("-"))) {
						tempa += "(" + map[i + 2][j] + ")";
					}
					map[i][j] = "(" + tempa + ")";
				}
			}
		}
		if (statesnum == 2) {
			String temptwo = "";
			for (int i = 1; i < dim - 1; i++) {
				for (int j = dim - 1; j < dim; j++) {
					if (i == 1) {
						if (!(map[i][j - 1].equals("-"))) {
							temptwo += "(" + map[i][j - 1] + ")";
						}
						if (!(map[i + 2][j - 1].equals("-"))) {
							temptwo += "(" + map[i + 2][j - 1] + ")";
						}
						if (!(map[i + 2][j].equals("-"))) {
							temptwo += "(" + map[i + 2][j] + ")";
						}
						map[i][j] = "(" + temptwo + ")";
					}
				}

			}
			for (int i = 1; i < dim - 1; i++) {
				map[i][3] = "-";
			}
		}
		if (statesnum == 3) {
			// we visit the states one by one and remove
			int icounter2 = 1;

			for (int i = 2; i < dim - 1; i++) {
				int jcounter2 = 1;
				for (int j = 4; j < dim; j++) {

					String temp = "";
					if (i > 3 && j > 3) {
						if (!(map[i][j - jcounter2].equals("-"))) {
							temp += "(" + map[i][j - jcounter2] + ")";
						}
						if (!(map[i - icounter2][j - jcounter2].equals("-"))) {
							temp += "(" + map[i - icounter2][j - jcounter2] + ")";
						}
						if (!(map[i - icounter2][j].equals("-"))) {
							temp += "(" + map[i - icounter2][j] + ")";
						}
						if (map[i][j].equals("-")) {
							map[i][j] = temp;
						} else {
							if (map[i][j].contains("*")) {
								map[i][j] = map[i][j].replace("(", "");
								map[i][j] = map[i][j].replace(")", "");
								map[i][j] = map[i][j].replace("*", "");
								map[i][j] = "(" + map[i][j] + "U" + temp + ")*";
							} else {
								map[i][j] = map[i][j] + "U" + temp;
							}

						}
					}
					jcounter2++;

				}
				if (i > 3) {
					icounter2++;
				}

			}
			if (statesnum == 3) {
				for (int i = 3; i < dim; i++) {
					map[3][i] = "-";
					map[i][3] = "-";
				}
				map[1][3] = "-";
			}
			
			// printing matrix
			for (int i = 0; i < dim; i++) {
				for (int j = 0; j < dim; j++) {
					System.out.print(map[i][j] + " ");
				}
				System.out.println("");
			}

			System.out.println("");
			System.out.println(statesnum - 1 + " GNFA");

			System.out.println(states.get(2) + " removed");
			String tempson = "";
			for (int i = 1; i < dim - 1; i++) {
				for (int j = dim - 1; j < dim; j++) {
					if (i == 1) {
						if (!(map[i][j - 1].equals("-"))) {
							tempson += "(" + map[i][j - 1] + ")";
						}
						if (!(map[i + 3][j - 1].equals("-"))) {
							tempson += "(" + map[i + 3][j - 1] + ")";
						}
						if (!(map[i + 3][j].equals("-"))) {
							tempson += "(" + map[i + 3][j] + ")";
						}
						map[i][j] = "(" + map[i][j] + "U" + tempson + ")";
					}
				}
			}
			for (int i = 4; i < dim; i++) {
				map[4][i] = "-";
				map[i][4] = "-";
			}

			map[1][4] = "-";
		}
		// printing matrix
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				System.out.print(map[i][j] + " ");
			}
			System.out.println("");
		}
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("REGULAR EXP : " + map[1][dim - 1]);

	}
}