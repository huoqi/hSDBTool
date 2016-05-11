package io.xinjian.hsdbtool.dialog;

import org.eclipse.jface.action.Action;

public class AboutAction extends Action {
	public AboutAction(){
		super("About hSDBTool", AS_PUSH_BUTTON);
	}

	public void run() {
		AboutJDialog aboutDialog = new AboutJDialog();
		aboutDialog.setVisible(true);
	}
}
