/*
 * To show log information with timestamp in txtLog
 */

package com.aws.sdb;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.widgets.Text;

public class Log {
	private Text text;
	private SimpleDateFormat sdf;
	private String log;

	public Log(Text text){
		this.text = text;
		sdf = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
		log = "";
	}
	
	public void showLog(String inputLog){
		log = sdf.format(new Date());
		log += " " + inputLog + "\n";
		text.append(log);
	}
}
