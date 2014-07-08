package com.aws.dialog;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

public class AboutJDialog extends JDialog {

	private static final long serialVersionUID = 870986280527144537L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public AboutJDialog() {
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(400, 260, 300, 185);
		getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("300px"),},
			new RowSpec[] {
				RowSpec.decode("114px"),
				RowSpec.decode("39px"),}));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, "1, 1, center, center");
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("257px"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("34px"),
				RowSpec.decode("34px"),
				RowSpec.decode("34px"),}));
		{
			JLabel label = new JLabel("AWS SimpleDB Tool");
			contentPanel.add(label, "2, 2, center, center");
			label.setHorizontalAlignment(SwingConstants.CENTER);
		}
		{
			JLabel label = new JLabel("Version 0.1");
			contentPanel.add(label, "2, 3, center, center");
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		}
		{
			LinkLabel linkLabel = new LinkLabel("https://github.com/huoqi/hSDBTool", "https://github.com/huoqi/hSDBTool");
			contentPanel.add(linkLabel, "2, 4, center, center");
			linkLabel.setHorizontalAlignment(SwingConstants.CENTER);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, "1, 2, center, top");
			{
				JButton okButton = new JButton("OK");
				okButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		
	}

}
