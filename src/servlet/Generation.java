package servlet;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Generation {

	// input params
	private int tablesCount, exampleCouts,exampleLowerbound,exampleUpperbound,MaxNumber,MinNumber;
	private boolean leftHand, rightHand,miniabakus,noTransaction1to10,formulaCheck,changeHands;
	private int formula;

	protected String answerConstruct() {

		//checkParamsLogic();

		String table = "";
		for (int j = 0; j < exampleCouts; j++) {
			// one table format
			ArrayList<int[]> outputTable = new ArrayList<int[]>();
			for (int i = 0; i < tablesCount; i++) {

				int[] dataRow = createDataRow();
				outputTable.add(dataRow);
			}
			table = table + printTable(outputTable, j + 1) + "<p>" + "</p>";
		}

		String styleCss = "<head><link  rel='stylesheet' type='text/css' href='/Abakus/resources/css/style.css'></head>";
		String answer = "<!DOCTYPE HTML>";
		answer = answer + "<html>" + styleCss + "<body>";
		answer = answer + "<div class='d6'><h3><span>" + "Абакус. Таблицы для тренировки. " + "</span></h3></div>" + "<p>"
				+ table + "</p>";
		answer = answer + "</body></html>";
		return answer;
	}

	// formulate description data before table ;
	protected String descriptionOfTable(int numberOfCurrentTable) {
		String descriptionOfTable = "Таблица " + numberOfCurrentTable + " из " + exampleCouts + ". В таблице "
				+ tablesCount + " столбцов, " + "слагаемых от " + exampleLowerbound + " до  " + exampleUpperbound
				+ ". Значения слагаемых находятся в диапазоне от " + MinNumber + " до " + MaxNumber + ".";
		if (leftHand)
			descriptionOfTable = descriptionOfTable + " Включено ограничение 'левая рука'.";
		if (rightHand)
			descriptionOfTable = descriptionOfTable + " Включено ограничение 'правая рука'.";
		if (miniabakus)
			descriptionOfTable = descriptionOfTable + " Включено ограничение 'миниабакус'.";
		if (noTransaction1to10)
			descriptionOfTable = descriptionOfTable + " Отключен перенос разрядов.";
		if (formulaCheck)
			descriptionOfTable = descriptionOfTable + " Включена формула '" + formula + "'.";

		return descriptionOfTable;
	}

	// generate one example (one full line)
	protected int[] createDataRow() {
		int exampleLength = generationNumber(exampleUpperbound, exampleLowerbound);
		int[] lineArray = new int[exampleLength];
		int iPreviousNumber;
		int itempNumber;
		for (int j = 0; j < exampleLength; j++) {
			iPreviousNumber = 0;
			if (j >0) {
				for (int jj = 0; jj < j; jj++) {
					iPreviousNumber = iPreviousNumber + lineArray[jj];
				}
			}
			if (j % 3 == 0) {
				itempNumber = generationByFormula(iPreviousNumber);
			} else {
				itempNumber = generationNumber(MaxNumber, MinNumber);
			}

			while (!sumCheck(iPreviousNumber, itempNumber)) {
				itempNumber = generationNumber(MaxNumber, MinNumber);
			}

			lineArray[j] = itempNumber;
		}
		return lineArray;
	}

	protected String printTable(ArrayList<int[]> outputTable, int numberOfCurrentTable) {
		String table = "<p>" + descriptionOfTable(numberOfCurrentTable) + "</p>" + "<table>";

		table = table + "<tr>";
		int[][] outputArray = outputTable.toArray(new int[][] {});
		for (int k = 0; k <= outputTable.size() + 1; k++) {
			for (int i = 0; i < outputArray.length; i++) {
				table = table + "<td>";
				if (outputArray[i].length >= k + 1) {

					table = table + outputArray[i][k];
				} else {
					table = table + " ";
				}

				table = table + "</td>";
			}
			table = table + "</tr>";
		}

		for (int iRowSum = 0; iRowSum < outputTable.size(); iRowSum++) {
			int rowSum = 0;

			int[] oneRow = new int[exampleCouts];
			oneRow = outputTable.get(iRowSum);
			for (int j = 0; j < oneRow.length; j++) {
				rowSum = rowSum + oneRow[j];
			}
			table = table + "<th>" + rowSum + "</th>";

		}
		table = table + "</table>" + "<p>" + "</p>";
		return table;

	}

	protected int generationNumber(int MaxNumber, int MinNumber) {
		int generationNumber = MinNumber + (int) (Math.random() * ((MaxNumber - MinNumber) + 1));
		return generationNumber;
	}
	
	protected int generationByFormula(int iPreviousNumber) {
		int result = (5+formula)-iPreviousNumber;
		return result;
	}

	// split ranges by ;
	protected String[] getNumberFromString(String inputString) {
		inputString = inputString.replaceAll(" ", "");
		String[] parts = (inputString).split(";");
		return parts;
	}

	// convert text fields for checkboxes
	protected boolean strToBool(String inputText) {
		if (inputText == null)
			return false;
		else
			return true;
	}

	protected void checkParamsLogic() {
		if (changeHands == true && (rightHand == true || leftHand == true)) {
			this.rightHand = false;
			this.leftHand = false;
		}
		if (rightHand == true && leftHand == true) {
			this.leftHand = false;
		}
		if (rightHand) {
			this.MaxNumber = 9;
			this.MinNumber = -9;
		}
		if (miniabakus) {
			if (leftHand) {
				if (MaxNumber > 40)
					this.MaxNumber = 40;
				if (MinNumber < -40)
					this.MinNumber = -40;
			} else {
				if (MaxNumber > 4)
					this.MaxNumber = 4;
				if (MinNumber < -4)
					this.MinNumber = -4;
			}
		}
	}

	protected boolean sumCheck(int a, int b) {
		boolean sumCheck = true;
		if (!MoreThan0(a, b))
			sumCheck = false;
		if (rightHand && !isItRightHand(a, b))
			sumCheck = false;
		if (leftHand && !isItLeftHand(a, b))
			sumCheck = false;
		if (miniabakus && !itMiniabakus(a, b))
			sumCheck = false;
		if (noTransaction1to10 && !checknoTransaction1to10(a, b))
			sumCheck = false;
		if (changeHands && !checkChangeHands(a, b))
			sumCheck = false;
		if (b == 0)
			sumCheck = false;
		return sumCheck;
	}

	// sumChecks
	protected boolean MoreThan0(int a, int b) {
		boolean MoreThan0 = false;
		if (a + b >= 0 && a + b <= MaxNumber)
			MoreThan0 = true;
		return MoreThan0;

	}

	protected boolean itMiniabakus(int a, int b) {
		boolean itMiniabakus = false;
		if (!isItLeftHand(a, b)) {
			if (Math.abs(a + b) <= 4)
				itMiniabakus = true;
		} else {
			if ((int) (Math.abs(a + b) / 10) <= 4)
				itMiniabakus = true;
		}
		return itMiniabakus;
	}

	protected boolean isItLeftHand(int a, int b) {
		boolean isItLeftHand = false;
		if (a % 10 == 0 && b % 10 == 0 && isItRightHand(a / 10, b / 10))
			isItLeftHand = true;
		return isItLeftHand;
	}

	protected boolean isItRightHand(int a, int b) {
		boolean isItRightHand = false;
		if (Math.abs(a + b) <= 9)
			isItRightHand = true;
		return isItRightHand;
	}

	protected boolean Limit99(int a, int b) {
		boolean Limit99 = false;
		if (Math.abs(a + b) <= 99)
			Limit99 = true;
		return Limit99;
	}

	protected boolean checknoTransaction1to10(int a, int b) {
		boolean checknoTransaction1to10 = false;
		int d = ((a % 10) + (b % 10));
		if (d <= 9 && d >= 0 && Math.abs(a + b) != 10)
			checknoTransaction1to10 = true;
		return checknoTransaction1to10;
	}

	protected boolean checkChangeHands(int a, int b) {
		boolean checkChangeHands = false;
		if (isItLeftHand(0, b)) {
			if (checknoTransaction1to10((int) (a % 10), (int) (b % 10))) {
				checkChangeHands = true;
			}
		}
		if (isItRightHand(0, b)) {
			if (isItRightHand(a, b)) {
				checkChangeHands = true;
			}
		}
		return checkChangeHands;
	}

	// getter and setter
	protected int getTablesCount() {
		return tablesCount;
	}

	protected void setTablesCount(String tablesCount) {
		this.tablesCount = Integer.parseInt(tablesCount);
	}

	protected int getMaxNumber() {
		return MaxNumber;
	}

	protected void setMaxNumber(String maxmin) {
		String[] maximin = getNumberFromString(maxmin);
		MaxNumber = Integer.parseInt(maximin[1]);
	}

	protected int getMinNumber() {
		return MinNumber;
	}

	protected void setMinNumber(String maxmin) {
		String[] maximin = getNumberFromString(maxmin);
		MinNumber = Integer.parseInt(maximin[0]);
	}

	protected boolean isLeftHand() {
		return leftHand;
	}

	protected void setLeftHand(String leftHandText) {
		this.leftHand = strToBool(leftHandText);
		;
	}

	protected boolean isRightHand() {
		return rightHand;
	}

	protected void setRightHand(String rightHandText) {
		this.rightHand = strToBool(rightHandText);
	}

	protected boolean isMiniabakus() {
		return miniabakus;
	}

	protected void setMiniabakus(String miniabakusText) {
		this.miniabakus = strToBool(miniabakusText);
		;
	}

	protected boolean isNoTransaction1to10() {
		return noTransaction1to10;
	}

	protected void setNoTransaction1to10(String noTransaction1to10Text) {
		this.noTransaction1to10 = strToBool(noTransaction1to10Text);
	}

	protected boolean isFormulaCheck() {
		return formulaCheck;
	}

	protected void setFormulaCheck(String formulaCheckText) {
		this.formulaCheck = strToBool(formulaCheckText);
		;
	}

	protected boolean isChangeHands() {
		return changeHands;
	}

	protected void setChangeHands(String changeHandsText) {
		this.changeHands = strToBool(changeHandsText);
		;
	}

	protected int getFormula() {
		return formula;
	}

	protected void setFormula(String formula) {
		if (formulaCheck)
		{
			this.formula = Integer.parseInt(formula);
		}
	}

	protected int getexampleCouts() {
		return exampleCouts;
	}

	protected void setexampleCouts(String exampleLength) {
		this.exampleCouts = Integer.parseInt(exampleLength);
	}

	protected int getExampleLowerbound() {
		return exampleLowerbound;
	}

	protected void setExampleLowerbound(String counts) {
		String[] bounds = getNumberFromString(counts);
		this.exampleLowerbound = Integer.parseInt(bounds[0]);
	}

	protected int getExampleUpperbound() {
		return exampleUpperbound;
	}

	protected void setExampleUpperbound(String counts) {
		String[] bounds = getNumberFromString(counts);
		this.exampleUpperbound = Integer.parseInt(bounds[1]);
	}

}
