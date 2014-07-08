/*
 * Get input objects and distribute them into three columns
 */

package com.aws.sdb;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class attTreeContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {	
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	
	}
	
	/*
	 * inputElement: List<SDItem>¡¢List<SDAttribute>¡¢List<String>. These are all List object
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		List<?> items = (List<?>)inputElement;
		return items.toArray();
	}

	/*
	 * input SDItem -->get SDAttribute[]
	 * input SDAttribute -->get SDBAttribute.value[]
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		if(parentElement.getClass().equals(SDBItem.class)){
			List<SDBAttribute> attributes = ((SDBItem)parentElement).getAttributes();
			return attributes.toArray();
		}
		if((parentElement.getClass().equals(SDBAttribute.class))){
			SDBAttribute attribute = (SDBAttribute)parentElement;
			return attribute.getValues().toArray();
		}
		return new String[]{""};
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	/*
	 * SDItem has children SDAttributes and SDBAttribute has children SDBAttribute.values, SDAttribute.value has no child.
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		if(element.getClass().equals(SDBItem.class) || element.getClass().equals(SDBAttribute.class) ){
			return true;
		}
		return false;
	}

}
