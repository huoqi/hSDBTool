/*
 * Provider cell label
 */

package io.xinjian.hsdbtool.sdb;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class attTableLabelProvider implements ITableLabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {	
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {	
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	/*
	 * Put Item names in 1st column, attribute name in 2nd column, attribute value in 3rd column, others null
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		if(element.getClass().equals(SDBItem.class)){
			if(columnIndex == 0){
				return ((SDBItem)element).getName();
			}
		}
		else if(element.getClass().equals(SDBAttribute.class)){
			if(columnIndex == 1){
				return ((SDBAttribute)element).getName();
			}
		}
		else if(element.getClass().equals(String.class)){
			if(columnIndex == 2){
				return (String)element;
			}
		}
		return "";
	}

}
