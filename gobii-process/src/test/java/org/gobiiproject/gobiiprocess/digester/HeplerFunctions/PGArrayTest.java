package org.gobiiproject.gobiiprocess.digester.HeplerFunctions;

import org.gobiiproject.gobiiprocess.digester.HelperFunctions.PGArray;
import org.junit.Assert;
import org.junit.Test;

public class PGArrayTest {

	@Test
	public void testDelimiterSeparatedStringToPgArrayString() {
		Assert.assertEquals("{\"\"+\"\",\"\"-\"\",\"\"z\"\"}", PGArray.delimiterSeparatedStringToPgArrayString("+/-/z"));
		Assert.assertEquals("{\"\"A\"\",\"\"b\"\",\"\"Y\"\"}", PGArray.delimiterSeparatedStringToPgArrayString("A|b|Y"));
		Assert.assertEquals("{\"\".\"\",\"\".\"\",\"\".\"\"}", PGArray.delimiterSeparatedStringToPgArrayString(".\\.\\."));
		Assert.assertEquals("{\"\"h\"\",\"\"z\"\",\"\"k\"\"}", PGArray.delimiterSeparatedStringToPgArrayString("h,z,k"));
		Assert.assertEquals("{\"\"A\"\",\"\"M\"\",\"\"Z\"\"}", PGArray.delimiterSeparatedStringToPgArrayString("A;M;Z"));
		Assert.assertEquals("{\"\"+\"\",\"\"-\"\",\"\"z\"\"}", PGArray.delimiterSeparatedStringToPgArrayString("+:-:z"));
		Assert.assertEquals("{\"\"+\"\",\"\"-\"\",\"\"z\"\"}", PGArray.delimiterSeparatedStringToPgArrayString("+/-\\z"));
		Assert.assertEquals("{\"\"+\"\",\"\"-\"\",\"\"z\"\"}", PGArray.delimiterSeparatedStringToPgArrayString("+:-;z"));
		Assert.assertEquals("{\"\"+\"\",\"\"-\"\",\"\"z\"\"}", PGArray.delimiterSeparatedStringToPgArrayString("{+;-;z}"));
		Assert.assertEquals("{\"\"+\"\",\"\"-\"\",\"\"z\"\"}", PGArray.delimiterSeparatedStringToPgArrayString("(+|-|z)"));
		Assert.assertEquals("{\"\"+\"\",\"\"-\"\",\"\"z\"\"}", PGArray.delimiterSeparatedStringToPgArrayString("[+:-:z]"));
		Assert.assertEquals("{\"\"+\"\",\"\"-\"\",\"\"z\"\"}", PGArray.delimiterSeparatedStringToPgArrayString("[+:::::-::\\|/,z}"));
		Assert.assertEquals("{\"\"+\"\",\"\"-\"\",\"\"z\"\"}", PGArray.delimiterSeparatedStringToPgArrayString("(+;-;z}"));
		Assert.assertEquals("{\"\"0\"\",\"\"1\"\",\"\"9\"\"}", PGArray.delimiterSeparatedStringToPgArrayString("(0,1,9}"));
	}
}
