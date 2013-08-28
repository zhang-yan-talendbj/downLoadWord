package com.synnex.word;

import static org.junit.Assert.*;

import org.junit.Test;


/**
 * Unit test for simple App.
 */
public class AppTest {
	@org.junit.Test
	public void testname() throws Exception {
		boolean number = App.isNumber('9');
		
		assertTrue(number);
	}
	
	@Test
	public void ttt() throws Exception {
		String wordString = "1, comparisons  [k?m'p?r?snz]";
		String word = wordString.substring(wordString.indexOf(',')+2,wordString.indexOf("  "));
			System.out.println(word);
	}
}
