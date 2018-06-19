/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */
package com.trgr.dockets.core.util;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class HeapDumpUtil {

	public static void createHeapDump(final String javaPath, final String pathPrefix, final String pathSuffix)
			throws IOException {
		final String name = ManagementFactory.getRuntimeMXBean().getName();
		final String pid = name.substring(0, name.indexOf("@"));
		
		// After that we can start jmap process like this:
		final String timestamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
		final String[] cmd = {javaPath + "\\jmap", "-dump:file=" + pathPrefix + timestamp + pathSuffix, pid };
		Runtime.getRuntime().exec(cmd);
	}
}
